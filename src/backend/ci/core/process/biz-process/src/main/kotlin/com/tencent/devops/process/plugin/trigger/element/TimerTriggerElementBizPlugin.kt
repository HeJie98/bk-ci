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

package com.tencent.devops.process.plugin.trigger.element

import com.tencent.devops.common.api.enums.ScmType
import com.tencent.devops.common.api.exception.ErrorCodeException
import com.tencent.devops.common.pipeline.container.Container
import com.tencent.devops.common.pipeline.container.Stage
import com.tencent.devops.common.pipeline.container.TriggerContainer
import com.tencent.devops.common.pipeline.enums.ChannelCode
import com.tencent.devops.common.pipeline.pojo.element.atom.BeforeDeleteParam
import com.tencent.devops.common.pipeline.pojo.element.atom.ElementCheckResult
import com.tencent.devops.common.pipeline.pojo.element.trigger.TimerTriggerElement
import com.tencent.devops.process.constant.ProcessMessageCode
import com.tencent.devops.process.constant.ProcessMessageCode.ERROR_TIMER_TRIGGER_SVN_BRANCH_NOT_EMPTY
import com.tencent.devops.process.plugin.ElementBizPlugin
import com.tencent.devops.process.plugin.annotation.ElementBiz
import com.tencent.devops.process.plugin.trigger.service.PipelineTimerService
import com.tencent.devops.process.plugin.trigger.service.PipelineTimerTriggerTaskService
import com.tencent.devops.process.pojo.pipeline.PipelineYamlVo
import org.slf4j.LoggerFactory

@ElementBiz
class TimerTriggerElementBizPlugin constructor(
    private val pipelineTimerService: PipelineTimerService,
    private val timerTriggerTaskService: PipelineTimerTriggerTaskService
) : ElementBizPlugin<TimerTriggerElement> {

    override fun elementClass(): Class<TimerTriggerElement> {
        return TimerTriggerElement::class.java
    }

    override fun check(
        projectId: String?,
        userId: String,
        stage: Stage,
        container: Container,
        element: TimerTriggerElement,
        contextMap: Map<String, String>,
        appearedCnt: Int,
        isTemplate: Boolean,
        oauthUser: String?
    ) = ElementCheckResult(true)

    override fun afterCreate(
        element: TimerTriggerElement,
        projectId: String,
        pipelineId: String,
        pipelineName: String,
        userId: String,
        channelCode: ChannelCode,
        create: Boolean,
        container: Container,
        yamlInfo: PipelineYamlVo?
    ) {
        val params = (container as TriggerContainer).params.associate { it.id to it.defaultValue.toString() }
        logger.info("[$pipelineId]|$userId| Timer trigger [${element.name}] enable=${element.elementEnabled()}")
        val crontabExpressions = timerTriggerTaskService.getCrontabExpressions(
            params = params,
            element = element
        )
        val repo = timerTriggerTaskService.getRepo(
            projectId = projectId,
            element = element,
            params = params,
            yamlInfo = yamlInfo
        )
        // svn仓库分支必填
        if (repo != null && repo.getScmType() == ScmType.CODE_SVN && element.branches.isNullOrEmpty()) {
            throw ErrorCodeException(
                errorCode = ERROR_TIMER_TRIGGER_SVN_BRANCH_NOT_EMPTY
            )
        }
        if (crontabExpressions.isNotEmpty()) {
            val result = pipelineTimerService.saveTimer(
                projectId = projectId,
                pipelineId = pipelineId,
                userId = userId,
                crontabExpressions = crontabExpressions,
                channelCode = channelCode,
                repoHashId = repo?.repoHashId,
                branchs = element.branches?.toSet(),
                noScm = element.noScm,
                taskId = element.id ?: "",
                startParam = element.startParam
            )
            logger.info("[$pipelineId]|$userId| Update pipeline timer|crontab=$crontabExpressions")
            if (result.isNotOk()) {
                throw ErrorCodeException(
                    errorCode = ProcessMessageCode.ILLEGAL_TIMER_CRONTAB
                )
            }
        } else {
            pipelineTimerService.deleteTimer(projectId, pipelineId, userId, element.id ?: "")
            logger.info("[$pipelineId]|$userId| Delete pipeline timer")
        }
    }

    override fun beforeDelete(element: TimerTriggerElement, param: BeforeDeleteParam) {
        if (param.pipelineId.isNotBlank()) {
            pipelineTimerService.deleteTimer(param.projectId, param.pipelineId, param.userId, element.id ?: "")
            pipelineTimerService.deleteTimerBranch(projectId = param.projectId, pipelineId = param.pipelineId)
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(TimerTriggerElementBizPlugin::class.java)
    }
}
