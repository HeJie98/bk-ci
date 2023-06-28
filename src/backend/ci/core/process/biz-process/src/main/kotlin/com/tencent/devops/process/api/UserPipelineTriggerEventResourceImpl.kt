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

package com.tencent.devops.process.api

import com.tencent.devops.common.api.pojo.Page
import com.tencent.devops.common.api.pojo.Result
import com.tencent.devops.common.api.util.PageUtil
import com.tencent.devops.common.web.RestResource
import com.tencent.devops.process.api.user.UserPipelineTriggerEventResource
import com.tencent.devops.process.pojo.PipelineTriggerEvent
import com.tencent.devops.process.service.PipelineTriggerEventService
import com.tencent.devops.repository.pojo.RepositoryEventHistory
import org.springframework.beans.factory.annotation.Autowired

@RestResource
@SuppressWarnings("ALL")
class UserPipelineTriggerEventResourceImpl @Autowired constructor(
    val pipelineTriggerEventService: PipelineTriggerEventService
) : UserPipelineTriggerEventResource {
    override fun byPipeline(
        userId: String,
        projectId: String,
        pipelineId: String,
        triggerType: String?,
        eventType: String?,
        triggerUser: String?,
        page: Int?,
        pageSize: Int?
    ): Result<Page<PipelineTriggerEvent>> {
        val pageNotNull = page ?: 0
        val pageSizeNotNull = pageSize ?: PageSize
        val limit = PageUtil.convertPageSizeToSQLLimit(pageNotNull, pageSizeNotNull)
        val result = pipelineTriggerEventService.byPipeline(
            userId = userId,
            projectId = projectId,
            pipelineId = pipelineId,
            triggerType = triggerType,
            triggerUser = triggerUser,
            eventType = eventType,
            limit = limit.limit,
            offset = limit.offset
        )
        return Result(
            Page(
                count = result.count,
                page = pageNotNull,
                pageSize = pageSizeNotNull,
                records = result.records
            )
        )
    }

    override fun byRepoHashId(
        userId: String,
        projectId: String,
        repoHashId: String,
        triggerType: String?,
        eventType: String?,
        triggerUser: String?,
        pipelineName: String?,
        page: Int?,
        pageSize: Int?
    ): Result<Page<RepositoryEventHistory>> {
        val pageNotNull = page ?: 0
        val pageSizeNotNull = pageSize ?: PageSize
        val limit = PageUtil.convertPageSizeToSQLLimit(pageNotNull, pageSizeNotNull)
        val result = pipelineTriggerEventService.byRepoHashId(
            userId = userId,
            projectId = projectId,
            repoHashId = repoHashId,
            triggerType = triggerType,
            eventType = eventType,
            triggerUser = triggerUser,
            pipelineName = pipelineName,
            limit = limit.limit,
            offset = limit.offset
        )
        return Result(
            Page(
                count = result.count,
                page = pageNotNull,
                pageSize = pageSizeNotNull,
                records = result.records
            )
        )
    }

    override fun getTriggerDetail(
        userId: String,
        projectId: String,
        repoHashId: String,
        eventId: String,
        triggerType: String?,
        eventType: String?,
        triggerUser: String?,
        pipelineName: String?,
        page: Int?,
        pageSize: Int?
    ): Result<Page<PipelineTriggerEvent>> {
        val pageNotNull = page ?: 0
        val pageSizeNotNull = pageSize ?: PageSize
        val limit = PageUtil.convertPageSizeToSQLLimit(pageNotNull, pageSizeNotNull)
        val result = pipelineTriggerEventService.getTriggerDetail(
            userId = userId,
            projectId = projectId,
            eventId = eventId,
            repoHashId = repoHashId,
            pipelineName = pipelineName,
            limit = limit.limit,
            offset = limit.offset
        )
        return Result(
            Page(
                count = result.count,
                page = pageNotNull,
                pageSize = pageSizeNotNull,
                records = result.records
            )
        )
    }

    companion object {
        private const val PageSize = 20
    }
}
