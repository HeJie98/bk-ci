/*
 * Tencent is pleased to support the open source community by making BK-CI 蓝鲸持续集成平台 available.
 *
 * Copyright (C) 2019 THL A29 Limited, a Tencent company.  All rights reserved.
 *
 * BK-CI 蓝鲸持续集成平台 is licensed under the MIT license.
 *
 * A copy of the MIT License is included in this file.
 *
 *
 * Terms of the MIT License:
 * ---------------------------------------------------
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of
 * the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
 * LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN
 * NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.tencent.devops.process.service

import com.tencent.devops.common.api.exception.OperationException
import com.tencent.devops.common.api.pojo.ErrorCode
import com.tencent.devops.common.api.pojo.ErrorType
import com.tencent.devops.common.api.util.JsonUtil
import com.tencent.devops.common.event.pojo.pipeline.PipelineBuildFinishBroadCastEvent
import com.tencent.devops.common.pipeline.enums.BuildStatus
import com.tencent.devops.common.pipeline.enums.StartType
import com.tencent.devops.common.redis.RedisOperation
import com.tencent.devops.model.process.tables.records.TPipelineBuildHistoryRecord
import com.tencent.devops.process.dao.PipelineBuildHistoryDao
import com.tencent.devops.process.engine.dao.PipelineModelTaskDao
import com.tencent.devops.process.engine.service.PipelineRuntimeService
import com.tencent.devops.process.pojo.SubPipelineRefTree
import com.tencent.devops.process.pojo.pipeline.SubPipelineStatus
import org.jooq.DSLContext
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class SubPipelineStatusService @Autowired constructor(
    private val pipelineRuntimeService: PipelineRuntimeService,
    private val redisOperation: RedisOperation,
    private val pipelineBuildHistoryDao: PipelineBuildHistoryDao,
    private val pipelineModeTaskDao: PipelineModelTaskDao,
    private var dslContext: DSLContext
) {

    companion object {
        // 子流水线启动过期时间900分钟
        private const val SUBPIPELINE_STATUS_START_EXPIRED = 54000L

        // 子流水线完成过期时间10分钟
        private const val SUBPIPELINE_STATUS_FINISH_EXPIRED = 600L

        // 子流水线递归调用层次
        private const val SUBPIPELINE_CALL_DEPTH = 2
    }

    private val logger = LoggerFactory.getLogger(SubPipelineStatusService::class.java)

    fun onStart(buildId: String) {
        redisOperation.set(
            key = getSubPipelineStatusKey(buildId),
            value = JsonUtil.toJson(
                SubPipelineStatus(status = BuildStatus.RUNNING.name)
            ),
            expiredInSecond = SUBPIPELINE_STATUS_START_EXPIRED
        )
    }

    fun onFinish(event: PipelineBuildFinishBroadCastEvent) {
        with(event) {
            // 不是子流水线启动或者子流水线是异步启动的，不需要缓存状态
            if (triggerType != StartType.PIPELINE.name ||
                redisOperation.get(getSubPipelineStatusKey(buildId)) == null
            ) {
                return
            }

            redisOperation.set(
                key = getSubPipelineStatusKey(buildId),
                value = JsonUtil.toJson(getSubPipelineStatusFromDB(event.projectId, buildId)),
                expiredInSecond = SUBPIPELINE_STATUS_FINISH_EXPIRED
            )
        }
    }

    private fun getSubPipelineStatusFromDB(projectId: String, buildId: String): SubPipelineStatus {
        val buildInfo = pipelineRuntimeService.getBuildInfo(projectId, buildId)
        return if (buildInfo != null) {
            val status: BuildStatus = when {
                buildInfo.isSuccess() && buildInfo.isStageSuccess() -> BuildStatus.SUCCEED
                buildInfo.isFinish() -> buildInfo.status
                buildInfo.isReadyToRun() -> BuildStatus.RUNNING // QUEUE状态
                buildInfo.isStageSuccess() -> BuildStatus.RUNNING // stage 特性， 未结束，只是卡在Stage审核中
                else -> buildInfo.status
            }
            SubPipelineStatus(
                status = status.name
            )
        } else {
            SubPipelineStatus(
                status = BuildStatus.FAILED.name,
                errorType = ErrorType.USER,
                errorCode = ErrorCode.USER_RESOURCE_NOT_FOUND,
                errorMsg = "找不到对应子流水线的构建记录"
            )
        }
    }

    fun getSubPipelineStatus(projectId: String, buildId: String): SubPipelineStatus {
        val subPipelineStatusStr = redisOperation.get(getSubPipelineStatusKey(buildId))
        return if (subPipelineStatusStr.isNullOrBlank()) {
            getSubPipelineStatusFromDB(projectId, buildId)
        } else {
            val redisSubPipelineStatus = JsonUtil.to(subPipelineStatusStr, SubPipelineStatus::class.java)
            // 如果状态完成,子流水线插件就不会再调用,从redis中删除key
            if (BuildStatus.parse(redisSubPipelineStatus.status).isFinish()) {
                redisOperation.delete(getSubPipelineStatusKey(buildId))
            }
            redisSubPipelineStatus
        }
    }

    fun getSubPipelineRunStatus(
        projectId: String,
        pipelineId: String,
        buildId: String
    ): SubPipelineRefTree? {
        logger.info("获取子流水线状态,projectId=[$projectId],pipelineId=[$pipelineId],buildId=[$buildId]")
        // 子流水线树形结构
        var subPipelineTreeInfo = redisOperation.get(getSubPipelineTreeKey(buildId))
        val start = System.currentTimeMillis()
        if (subPipelineTreeInfo.isNullOrEmpty() ) {
            val historyRecord = pipelineBuildHistoryDao.findHistoryByParentBuildId(
                dslContext = dslContext,
                parentBuildId = buildId
            )
            val pipelineStatus = getSubPipelineStatus(projectId, buildId)
            subPipelineTreeInfo = getSubPipelineStrByBuildHistory(
                dslContext = dslContext,
                history = historyRecord,
                depth = SUBPIPELINE_CALL_DEPTH,
                outStr = StringBuilder("|-\${taskName}(${pipelineStatus.status})\n")
            )
            redisOperation.set(
                getSubPipelineTreeKey(buildId),
                subPipelineTreeInfo,
                60
            )
        }
        val end = System.currentTimeMillis()
        println("耗时==>${(end - start)} ms")
        println("tree = \n$subPipelineTreeInfo")
        return SubPipelineRefTree(
            treeStr = subPipelineTreeInfo
        )
    }

    private fun getSubPipelineStrByBuildHistory(
        dslContext: DSLContext,
        history: List<TPipelineBuildHistoryRecord>,
        depth: Int,
        outStr: StringBuilder
    ): String {
        if (history.isEmpty()) {
            return ""
        }
        history.forEach { it ->
            val historyRecord = pipelineBuildHistoryDao.findHistoryByParentBuildId(
                dslContext = dslContext,
                parentBuildId = it.buildId
            )
            val subDepth = depth - 1
            for (i in 1..SUBPIPELINE_CALL_DEPTH - subDepth) {
                outStr.append("\t")
            }
            val taskInfo = pipelineModeTaskDao.get(
                dslContext = dslContext,
                projectId = it.projectId,
                pipelineId = it.pipelineId,
                taskId = it.parentTaskId
            )
            outStr.append("|-${taskInfo?.taskName ?: "unknown taskName"}")
            val pipelineStatus = getSubPipelineStatus(it.projectId, it.buildId)
            // 仍存在下级子流水线调用，则继续递归
            if (historyRecord.isNotEmpty() && subDepth > 0) {
                outStr.append("(${pipelineStatus.status})")
                val sub = getSubPipelineStrByBuildHistory(
                    dslContext = dslContext,
                    history = historyRecord,
                    depth = subDepth,
                    outStr = StringBuilder()
                )
                outStr.append("\n$sub")
            } else {
                outStr.append("(${pipelineStatus.status})\n")
            }
        }
        return outStr.toString()
    }

    /**
     * 检查递归调用
     */
    fun checkRecursiveCall(
        parentProjectId: String,
        parentPipelineId: String,
        parentBuildId: String,
        pipelineId: String
    ) {
        val subBuild = pipelineBuildHistoryDao.findHistoryByParentBuildId(
            dslContext = dslContext,
            parentBuildId = parentBuildId
        )
        subBuild.forEach {
            if (it.parentPipelineIds.contains(pipelineId)) {
                logger.warn(
                    "subPipeline does not allow loop calls|" +
                        "parentProjectId:${parentProjectId}|" +
                        "parentPipelineId:${parentBuildId}|" +
                        "parentBuildId:$parentBuildId|" +
                        "pipelineId:$pipelineId"
                )
                throw OperationException("子流水线不允许循环调用,循环流水线:pipelineId:$pipelineId")
            }
        }
    }

    private fun getSubPipelineStatusKey(buildId: String): String {
        return "subpipeline:build:$buildId:status"
    }

    private fun getSubPipelineTreeKey(buildId: String): String {
        return "subPipelineTree:build:$buildId:tree"
    }

    private fun getSubPipelineTreeStatusMapKey(buildId: String): String {
        return "subPipelineTree:build:$buildId:statusMap"
    }
}
