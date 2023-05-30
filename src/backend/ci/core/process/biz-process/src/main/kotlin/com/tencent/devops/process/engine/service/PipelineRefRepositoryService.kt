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

package com.tencent.devops.process.engine.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.tencent.devops.common.api.enums.RepositoryConfig
import com.tencent.devops.common.api.enums.RepositoryType
import com.tencent.devops.common.api.exception.ErrorCodeException
import com.tencent.devops.common.api.pojo.PipelineRefRepositoryTaskInfo
import com.tencent.devops.common.api.util.EnvUtils
import com.tencent.devops.common.client.Client
import com.tencent.devops.common.pipeline.Model
import com.tencent.devops.common.pipeline.container.TriggerContainer
import com.tencent.devops.common.pipeline.pojo.element.Element
import com.tencent.devops.common.pipeline.pojo.element.market.MarketBuildAtomElement
import com.tencent.devops.common.pipeline.pojo.element.trigger.WebHookTriggerElement
import com.tencent.devops.common.pipeline.utils.RepositoryConfigUtils
import com.tencent.devops.process.constant.ProcessMessageCode
import com.tencent.devops.process.engine.dao.PipelineResDao
import com.tencent.devops.repository.api.ServiceRepositoryResource
import org.jooq.DSLContext
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * 流水线代码库使用服务
 * @version 1.0
 */
@Suppress("ALL")
@Service
class PipelineRefRepositoryService @Autowired constructor(
    private val dslContext: DSLContext,
    private val pipelineResDao: PipelineResDao,
    private val objectMapper: ObjectMapper,
    private val client: Client
) {

    private val logger = LoggerFactory.getLogger(javaClass)!!

    fun saveRepositoryRefInfo(
        projectId: String,
        pipelineId: String,
        version: Int?,
        userId: String
    ) {
        val model = getModel(projectId, pipelineId, version)
        if (model == null) {
            logger.info("$pipelineId|$version|model is null")
            return
        }
        // 获取流水线参数
        val triggerContainer = model.stages[0].containers[0] as TriggerContainer
        val params = triggerContainer.params.associate { param ->
            param.id to param.defaultValue.toString()
        }
        val triggerElements = triggerContainer.elements.filterIsInstance<WebHookTriggerElement>()
        val taskInfos = mutableListOf<PipelineRefRepositoryTaskInfo>()
        // 保存触发器中代码库依赖信息
        taskInfos.addAll(
            saveTriggerRefInfo(
                elements = triggerElements,
                params = params,
                projectId = projectId,
                pipelineId = pipelineId,
                pipelineName = model.name
            )
        )
        // 保存其他Stage内的代码库依赖信息
        taskInfos.addAll(
            saveOtherStageRefInfo(
                model = model,
                params = params,
                projectId = projectId,
                pipelineId = pipelineId,
                pipelineName = model.name
            )
        )
        try {
            logger.info("try save pipeline ref repository info|projectId[$projectId]|pipelineId[$pipelineId]")
            client.get(ServiceRepositoryResource::class).savePipelineTaskInfo(
                pipelineId = pipelineId,
                taskInfos = taskInfos
            )
        } catch (e: Exception) {
            logger.warn("Failure to save pipeline ref repository info|projectId[$projectId]|pipelineId[$pipelineId]")
        }
    }

    fun deleteRepositoryRefInfo(
        pipelineId: String
    ){
        try {
            logger.info("try delete pipeline ref repository info|pipelineId[$pipelineId]")
            client.get(ServiceRepositoryResource::class).deletePipelineTaskInfo(
                pipelineId = pipelineId
            )
        } catch (e: Exception) {
            logger.warn("Failure to delete pipeline ref repository info|pipelineId[$pipelineId]")
        }
    }

    private fun saveTriggerRefInfo(
        elements: List<WebHookTriggerElement>,
        params: Map<String, String>,
        projectId: String,
        pipelineId: String,
        pipelineName: String?
    ): MutableList<PipelineRefRepositoryTaskInfo> {
        val taskInfos = mutableListOf<PipelineRefRepositoryTaskInfo>()
        elements.forEach { element ->
            val webhookElementParams = getElementRepositoryConfig(element, variable = params)
                ?: return@forEach
            with(webhookElementParams) {
                taskInfos.add(
                    PipelineRefRepositoryTaskInfo(
                        projectId = projectId,
                        pipelineId = pipelineId,
                        pipelineName = pipelineName ?: "",
                        repositoryHashId = repositoryHashId!!,
                        taskId = element.id,
                        atomCode = element.getAtomCode()
                    )
                )
            }
        }
        return taskInfos
    }

    private fun saveOtherStageRefInfo(
        model: Model,
        params: Map<String, String>,
        projectId: String,
        pipelineId: String,
        pipelineName: String?
    ): MutableList<PipelineRefRepositoryTaskInfo> {
        val taskInfos = mutableListOf<PipelineRefRepositoryTaskInfo>()
        if (model.stages.size > 1) {
            val atomCodes = listOf("checkout", "gitCodeRepo")
            for (i in 1 until model.stages.size) {
                val stage = model.stages[i]
                stage.containers.forEach { container ->
                    run out@{
                        container.elements.forEach {
                            if (atomCodes.contains(it.getAtomCode())) {
                                val marketBuildAtomElement = it as MarketBuildAtomElement
                                // 输入参数
                                val inputParam = marketBuildAtomElement.data["input"]
                                if (inputParam !is Map<*, *>) return@out
                                when (RepositoryType.parseType(inputParam["repositoryType"] as String?)) {
                                    RepositoryType.ID -> {
                                        taskInfos.add(
                                            PipelineRefRepositoryTaskInfo(
                                                projectId = projectId,
                                                pipelineId = pipelineId,
                                                pipelineName = pipelineName ?: "",
                                                repositoryHashId = (inputParam["repositoryHashId"] as String),
                                                taskId = it.id,
                                                atomCode = it.getAtomCode() ?: ""
                                            )
                                        )
                                    }
                                    else -> null
                                }
                            }
                        }
                    }
                }
            }
        }
        return taskInfos
    }

    private fun getModel(projectId: String, pipelineId: String, version: Int? = null): Model? {
        val modelString =
            pipelineResDao.getVersionModelString(dslContext, projectId, pipelineId, version) ?: return null
        return try {
            objectMapper.readValue(modelString, Model::class.java)
        } catch (e: Exception) {
            logger.warn("get process($pipelineId) model fail", e)
            null
        }
    }

    private fun getRepositoryConfig(
        repoHashId: String?,
        repoName: String?,
        repoType: RepositoryType?,
        variable: Map<String, String>? = null
    ): RepositoryConfig {
        return when (repoType) {
            RepositoryType.ID -> RepositoryConfig(repoHashId, null, RepositoryType.ID)
            RepositoryType.NAME -> {
                val repositoryName = if (variable == null || variable.isEmpty()) {
                    repoName!!
                } else {
                    EnvUtils.parseEnv(repoName!!, variable)
                }
                RepositoryConfig(null, repositoryName, RepositoryType.NAME)
            }

            else -> {
                if (!repoHashId.isNullOrBlank()) {
                    RepositoryConfig(repoHashId, null, RepositoryType.ID)
                } else if (!repoName.isNullOrBlank()) {
                    val repositoryName = if (variable == null || variable.isEmpty()) {
                        repoName
                    } else {
                        EnvUtils.parseEnv(repoName, variable)
                    }
                    RepositoryConfig(null, repositoryName, RepositoryType.NAME)
                } else {
                    // 两者不能同时为空
                    throw ErrorCodeException(
                        errorCode = ProcessMessageCode.ERROR_PARAM_WEBHOOK_ID_NAME_ALL_NULL
                    )
                }
            }
        }
    }

    private fun getElementRepositoryConfig(
        element: Element,
        variable: Map<String, String>
    ): RepositoryConfig? {
        if (element !is WebHookTriggerElement) {
            return null
        }
        val elementRepositoryConfig = RepositoryConfigUtils.buildConfig(element)
        return with(elementRepositoryConfig) {
            getRepositoryConfig(
                repoHashId = repositoryHashId,
                repoName = repositoryName,
                repoType = repositoryType,
                variable = variable
            )
        }
    }
}
