package com.tencent.devops.process.service

import com.fasterxml.jackson.core.type.TypeReference
import com.tencent.devops.common.api.exception.ParamBlankException
import com.tencent.devops.common.api.model.SQLPage
import com.tencent.devops.common.api.util.DateTimeUtil
import com.tencent.devops.common.api.util.JsonUtil
import com.tencent.devops.common.pipeline.enums.PipelineTriggerType
import com.tencent.devops.common.pipeline.pojo.element.trigger.enums.CodeEventType
import com.tencent.devops.process.dao.PipelineTriggerEventDao
import com.tencent.devops.process.pojo.PipelineTriggerEvent
import com.tencent.devops.process.pojo.PipelineTriggerEventMessage
import com.tencent.devops.process.pojo.RepositoryEventHistory
import org.jooq.DSLContext
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.format.DateTimeFormatter

@Suppress("ALL")
@Service
class PipelineTriggerEventService @Autowired constructor(
    val pipelineTriggerEventDao: PipelineTriggerEventDao,
    val dslContext: DSLContext
) {
    fun byPipeline(
        userId: String,
        projectId: String,
        pipelineId: String,
        eventType: String?,
        triggerType: String?,
        triggerUser: String?,
        startTime: Long?,
        endTime: Long?,
        limit: Int,
        offset: Int
    ): SQLPage<PipelineTriggerEvent> {
        val count = pipelineTriggerEventDao.count(
            dslContext = dslContext,
            eventType = eventType,
            triggerType = triggerType,
            triggerUser = triggerUser,
            pipelineId = pipelineId,
            startTime = startTime,
            endTime = endTime
        )
        val records = pipelineTriggerEventDao.list(
            dslContext = dslContext,
            eventType = eventType,
            triggerUser = triggerUser,
            triggerType = triggerType,
            pipelineId = pipelineId,
            startTime = startTime,
            endTime = endTime,
            limit = limit,
            offset = offset
        )
        val eventHistory = mutableListOf<PipelineTriggerEvent>()
        records.map {
            with(it) {
                eventHistory.add(
                    PipelineTriggerEvent(
                        projectId = projectId,
                        eventId = eventId,
                        triggerType = PipelineTriggerType.defaultValueOf(triggerType, PipelineTriggerType.MANUAL),
                        triggerUser = triggerUser ?: "",
                        eventType = if (eventType.isNullOrBlank()) {
                            null
                        } else {
                            CodeEventType.valueOf(eventType)
                        },
                        eventMessage = JsonUtil.anyTo(
                            any = eventMessage,
                            object : TypeReference<PipelineTriggerEventMessage>() {}
                        ),
                        status = status,
                        pipelineId = pipelineId,
                        pipelineName = pipelineName,
                        buildId = buildId,
                        buildNum = buildNum,
                        reason = reason,
                        reasonDetail = reasonDetail ?: "",
                        createTime = createTime.format(
                            DateTimeFormatter.ofPattern(DateTimeUtil.YYYY_MM_DD_HH_MM_SS)
                        )
                    )
                )
            }
        }
        return SQLPage(count = count.toLong(), records = eventHistory)
    }

    fun byRepoHashId(
        userId: String,
        projectId: String,
        repoHashId: String,
        triggerType: String?,
        eventType: String?,
        triggerUser: String?,
        pipelineName: String?,
        startTime: Long?,
        endTime: Long?,
        limit: Int,
        offset: Int
    ): SQLPage<RepositoryEventHistory> {
        val records = mutableListOf<RepositoryEventHistory>()
        val repositoryEventList = pipelineTriggerEventDao.listByRepoId(
            dslContext = dslContext,
            repoHashId = repoHashId,
            triggerType = triggerType,
            eventType = eventType,
            triggerUser = triggerUser,
            pipelineName = pipelineName,
            startTime = startTime,
            endTime = endTime,
            limit = limit,
            offset = offset
        )

        repositoryEventList.forEach { eventInfo ->
            val repoTriggerList = pipelineTriggerEventDao.list(
                dslContext = dslContext,
                eventId = eventInfo.eventId
            )
            records.add(
                RepositoryEventHistory(
                    eventId = eventInfo.eventId,
                    eventMessage = JsonUtil.anyTo(
                        any = eventInfo.eventMessage,
                        object : TypeReference<PipelineTriggerEventMessage>() {}
                    ),
                    eventType = eventInfo.eventType,
                    eventTime = repoTriggerList[0].createTime.format(
                        DateTimeFormatter.ofPattern(DateTimeUtil.YYYY_MM_DD_HH_MM_SS)
                    ),
                    triggerCount = repoTriggerList.count(),
                    successCount = repoTriggerList.count { it.status == SUCCESS_FLAG }
                )
            )
        }

        val repositoryEventCount = pipelineTriggerEventDao.countByRepoId(
            dslContext = dslContext,
            repoHashId = repoHashId,
            triggerType = triggerType,
            eventType = eventType,
            triggerUser = triggerUser,
            pipelineName = pipelineName,
            startTime = startTime,
            endTime = endTime
        )
        return SQLPage(count = repositoryEventCount.toLong(), records = records)
    }

    fun savePipelineTriggerEvent(pipelineTriggerEvent: PipelineTriggerEvent) {
        with(pipelineTriggerEvent) {
            if (eventId.isBlank()) {
                throw ParamBlankException("Invalid eventId")
            }
            if (projectId.isBlank()) {
                throw ParamBlankException("Invalid projectId")
            }
            if (pipelineId.isBlank()) {
                throw ParamBlankException("Invalid pipelineId")
            }
            logger.info("save pipeline trigger event|[$pipelineTriggerEvent]")
            pipelineTriggerEventDao.savePipelineTriggerEvent(
                dslContext = dslContext,
                pipelineTriggerEvent = pipelineTriggerEvent
            )
        }
    }

    fun getTriggerDetail(
        userId: String,
        projectId: String,
        repoHashId: String,
        eventId: String,
        pipelineName: String?,
        limit: Int,
        offset: Int
    ): SQLPage<PipelineTriggerEvent> {
        if (userId.isBlank()) {
            throw ParamBlankException("Invalid userId")
        }
        if (eventId.isBlank()) {
            throw ParamBlankException("Invalid eventId")
        }
        if (projectId.isBlank()) {
            throw ParamBlankException("Invalid projectId")
        }
        if (eventId.isBlank()) {
            throw ParamBlankException("Invalid eventId")
        }
        val records = pipelineTriggerEventDao.list(
            dslContext = dslContext,
            projectId = projectId,
            repoHashId = repoHashId,
            eventId = eventId,
            pipelineName = pipelineName,
            limit = limit,
            offset = offset
        ).map {
            PipelineTriggerEvent(
                projectId = it.projectId,
                eventId = it.eventId,
                eventSource = it.eventSource,
                triggerType = PipelineTriggerType.defaultValueOf(it.triggerType, PipelineTriggerType.MANUAL),
                triggerUser = it.triggerUser ?: "",
                eventType = if (it.eventType.isNullOrBlank()) {
                    null
                } else {
                    CodeEventType.valueOf(it.eventType)
                },
                eventMessage = JsonUtil.anyTo(
                    any = it.eventMessage,
                    object : TypeReference<PipelineTriggerEventMessage>() {}
                ),
                status = it.status,
                pipelineId = it.pipelineId,
                pipelineName = it.pipelineName,
                buildId = it.buildId,
                buildNum = it.buildNum,
                reason = it.reason,
                reasonDetail = it.reasonDetail,
                createTime = it.createTime.format(DateTimeFormatter.ofPattern(DateTimeUtil.YYYY_MM_DD_HH_MM_SS))
            )
        }
        val count = pipelineTriggerEventDao.count(
            dslContext = dslContext,
            projectId = projectId,
            repoHashId = repoHashId,
            eventId = eventId,
            pipelineName = pipelineName
        )
        return SQLPage(count = count.toLong(), records = records)
    }

    companion object {
        const val SUCCESS_FLAG = "success"
        private val logger = LoggerFactory.getLogger(PipelineTriggerEventService::class.java)
    }
}