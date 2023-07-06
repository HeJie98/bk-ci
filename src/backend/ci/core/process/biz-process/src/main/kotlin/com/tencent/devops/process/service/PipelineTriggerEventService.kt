package com.tencent.devops.process.service

import com.tencent.devops.common.api.exception.ParamBlankException
import com.tencent.devops.common.api.model.SQLPage
import com.tencent.devops.common.api.util.DateTimeUtil
import com.tencent.devops.process.dao.PipelineTriggerEventDao
import com.tencent.devops.process.pojo.PipelineTriggerEvent
import com.tencent.devops.repository.pojo.RepositoryEventHistory
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
){
    fun byPipeline(
        userId: String,
        projectId: String,
        pipelineId: String,
        eventType: String?,
        triggerType: String?,
        triggerUser: String?,
        limit: Int,
        offset: Int
    ): SQLPage<PipelineTriggerEvent> {
        val count = pipelineTriggerEventDao.count(
            dslContext = dslContext,
            eventType = eventType,
            triggerType = triggerType,
            triggerUser = triggerUser,
            pipelineId = pipelineId
        )
        val records = pipelineTriggerEventDao.list(
            dslContext = dslContext,
            eventType = eventType,
            triggerUser = triggerUser,
            triggerType = triggerType,
            pipelineId = pipelineId,
            limit = limit,
            offset = offset
        )
        val eventHistory = mutableListOf<PipelineTriggerEvent>()
        records.map {
            with(it){
                eventHistory.add(
                    PipelineTriggerEvent(
                        projectId = projectId,
                        eventId = eventId,
                        triggerType = triggerType ?: "",
                        triggerUser = triggerUser ?: "",
                        eventType = eventType,
                        eventMessage = eventMessage,
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
                    eventMessage = eventInfo.eventMessage,
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
        )
        return SQLPage(count = repositoryEventCount.toLong(),records = records)
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
                triggerType = it.triggerType,
                eventSource = it.eventSource,
                eventType = it.eventType,
                triggerUser = it.triggerUser,
                eventMessage = it.eventMessage,
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