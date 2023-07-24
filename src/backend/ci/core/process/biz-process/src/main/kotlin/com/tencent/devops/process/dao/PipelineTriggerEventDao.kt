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

package com.tencent.devops.process.dao

import com.tencent.devops.common.api.util.JsonUtil
import com.tencent.devops.model.process.tables.TPipelineTriggerEvent
import com.tencent.devops.model.process.tables.records.TPipelineTriggerEventRecord
import com.tencent.devops.process.pojo.PipelineTriggerEvent
import com.tencent.devops.process.pojo.RepositoryEventInfo
import org.jooq.Condition
import org.jooq.DSLContext
import org.jooq.Result
import org.springframework.stereotype.Repository
import java.sql.Timestamp
import java.time.LocalDateTime

@Suppress("ALL")
@Repository
class PipelineTriggerEventDao {
    fun savePipelineTriggerEvent(
        dslContext: DSLContext,
        pipelineTriggerEvent: PipelineTriggerEvent
    ) {
        with(TPipelineTriggerEvent.T_PIPELINE_TRIGGER_EVENT) {
            dslContext.insertInto(this).columns(
                PROJECT_ID,
                EVENT_ID,
                TRIGGER_TYPE,
                EVENT_SOURCE,
                EVENT_TYPE,
                TRIGGER_USER,
                EVENT_MESSAGE,
                STATUS,
                PIPELINE_ID,
                PIPELINE_NAME,
                BUILD_ID,
                BUILD_NUM,
                REASON,
                REASON_DETAIL,
                CREATE_TIME
            ).values(
                pipelineTriggerEvent.projectId,
                pipelineTriggerEvent.eventId,
                pipelineTriggerEvent.triggerType.name,
                pipelineTriggerEvent.eventSource,
                pipelineTriggerEvent.eventType?.name,
                pipelineTriggerEvent.triggerUser,
                JsonUtil.toJson(pipelineTriggerEvent.eventMessage, false),
                pipelineTriggerEvent.status,
                pipelineTriggerEvent.pipelineId,
                pipelineTriggerEvent.pipelineName,
                pipelineTriggerEvent.buildId,
                pipelineTriggerEvent.buildNum,
                pipelineTriggerEvent.reason,
                pipelineTriggerEvent.reasonDetail,
                LocalDateTime.now()
            ).execute()
        }
    }

    fun list(
        dslContext: DSLContext,
        projectId: String? = null,
        repoHashId: String? = null,
        eventId: String? = null,
        eventType: String? = null,
        triggerType: String? = null,
        triggerUser: String? = null,
        pipelineId: String? = null,
        pipelineName: String? = null,
        startTime: Long? = null,
        endTime: Long? = null,
        limit: Int? = null,
        offset: Int? = null
    ): Result<TPipelineTriggerEventRecord> {
        return with(TPipelineTriggerEvent.T_PIPELINE_TRIGGER_EVENT) {
            val conditions = buildConditions(
                projectId = projectId,
                repoHashId = repoHashId,
                eventId = eventId,
                eventType = eventType,
                triggerUser = triggerUser,
                triggerType = triggerType,
                pipelineId = pipelineId,
                pipelineName = pipelineName,
                startTime = startTime,
                endTime = endTime
            )
            val step = dslContext.selectFrom(this)
                .where(conditions)
                .orderBy(CREATE_TIME.desc())
            if (limit != null && offset != null) {
                step.limit(limit).offset(offset)
            }
            step.fetch()
        }
    }

    fun count(
        dslContext: DSLContext,
        projectId: String? = null,
        repoHashId: String? = null,
        eventId: String? = null,
        eventType: String? = null,
        triggerType: String? = null,
        triggerUser: String? = null,
        pipelineId: String? = null,
        pipelineName: String? = null,
        startTime: Long? = null,
        endTime: Long? = null
    ): Int {
        return with(TPipelineTriggerEvent.T_PIPELINE_TRIGGER_EVENT) {
            val conditions = buildConditions(
                projectId = projectId,
                repoHashId = repoHashId,
                eventId = eventId,
                eventType = eventType,
                triggerUser = triggerUser,
                triggerType = triggerType,
                pipelineId =pipelineId,
                pipelineName = pipelineName
            )
            dslContext.selectFrom(this)
                .where(conditions)
                .orderBy(CREATE_TIME.desc())
                .count()
        }
    }

    fun listByRepoId(
        dslContext: DSLContext,
        repoHashId: String?,
        triggerType: String?,
        eventType: String?,
        triggerUser: String?,
        pipelineName: String?,
        startTime: Long? = null,
        endTime: Long? = null,
        limit: Int,
        offset: Int
    ): Set<RepositoryEventInfo> {
        return with(TPipelineTriggerEvent.T_PIPELINE_TRIGGER_EVENT) {
            val conditions = buildConditions(
                repoHashId = repoHashId,
                triggerType = triggerType,
                triggerUser = triggerUser,
                eventType = eventType,
                pipelineName = pipelineName,
                startTime = startTime,
                endTime = endTime
            )
            dslContext.select(EVENT_ID, EVENT_MESSAGE, EVENT_TYPE)
                .from(this)
                .where(conditions)
                .orderBy(CREATE_TIME.desc())
                .limit(limit)
                .offset(offset)
                .distinct().map {
                    RepositoryEventInfo(
                        eventId = it.value1(),
                        eventType = it.value2(),
                        eventMessage = it.value3()
                    )
                }.toSet()
        }
    }

    fun countByRepoId(
        dslContext: DSLContext,
        repoHashId: String?,
        triggerType: String?,
        eventType: String?,
        triggerUser: String?,
        pipelineName: String?,
        startTime: Long?,
        endTime: Long?
    ): Int {
        return with(TPipelineTriggerEvent.T_PIPELINE_TRIGGER_EVENT) {
            val conditions = buildConditions(
                repoHashId = repoHashId,
                triggerType = triggerType,
                triggerUser = triggerUser,
                eventType = eventType,
                pipelineName = pipelineName,
                startTime = startTime,
                endTime = endTime
            )
            dslContext.select(EVENT_ID, EVENT_MESSAGE, EVENT_TYPE)
                .from(this)
                .where(conditions)
                .orderBy(CREATE_TIME.desc())
                .distinct()
                .count()
        }
    }

    fun listByEventId(
        dslContext: DSLContext,
        eventId: String,
        repoHashId: String?,
        limit: Int?,
        offset: Int?
    ): Result<TPipelineTriggerEventRecord> {
        return with(TPipelineTriggerEvent.T_PIPELINE_TRIGGER_EVENT) {
            val fetch = dslContext.selectFrom(this)
                .where(EVENT_ID.eq(eventId))
            if (limit != null && offset != null) {
                fetch.limit(limit).offset(offset)
            }
            fetch.fetch()
        }
    }


    private fun TPipelineTriggerEvent.buildConditions(
        projectId: String? = null,
        repoHashId: String? = null,
        eventId: String? = null,
        eventType: String? = null,
        triggerUser: String? = null,
        triggerType: String? = null,
        pipelineId: String? = null,
        pipelineName: String? = null,
        startTime: Long? = null,
        endTime: Long? = null
    ) :List<Condition> {
        val conditions = mutableListOf<Condition>()
        if (!projectId.isNullOrBlank()) {
            conditions.add(PROJECT_ID.eq(projectId))
        }
        if (!repoHashId.isNullOrBlank()) {
            conditions.add(EVENT_SOURCE.eq(repoHashId))
        }
        if (!eventId.isNullOrBlank()) {
            conditions.add(EVENT_ID.eq(eventId))
        }
        if (!eventType.isNullOrBlank()) {
            conditions.add(EVENT_TYPE.eq(eventType))
        }
        if (!triggerUser.isNullOrBlank()) {
            conditions.add(TRIGGER_USER.eq(triggerUser))
        }
        if (!triggerType.isNullOrBlank()) {
            conditions.add(TRIGGER_TYPE.eq(triggerType))
        }
        if (!pipelineId.isNullOrBlank()) {
            conditions.add(PIPELINE_ID.eq(pipelineId))
        }
        if (!pipelineName.isNullOrBlank()) {
            conditions.add(PIPELINE_NAME.like(pipelineName))
        }
        if (startTime != null && startTime > 0) {
            conditions.add(CREATE_TIME.ge(Timestamp(startTime).toLocalDateTime()))
        }
        if (endTime != null && endTime > 0) {
            conditions.add(CREATE_TIME.le(Timestamp(endTime).toLocalDateTime()))
        }
        if (!pipelineName.isNullOrBlank()) {
            conditions.add(PIPELINE_NAME.like(pipelineName))
        }
        return conditions
    }
}
