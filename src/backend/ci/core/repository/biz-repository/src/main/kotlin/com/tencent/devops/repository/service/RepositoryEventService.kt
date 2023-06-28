package com.tencent.devops.repository.service

import com.tencent.devops.common.api.exception.ParamBlankException
import com.tencent.devops.repository.dao.RepositoryEventDao
import com.tencent.devops.repository.pojo.ReplayPipelineEvent
import com.tencent.devops.repository.pojo.RepositoryEvent
import org.jooq.DSLContext
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
@SuppressWarnings("ALL")
class RepositoryEventService @Autowired constructor(
    val repositoryEventDao: RepositoryEventDao,
    val dslContext: DSLContext
){
    fun saveRepositoryEvent(repositoryEvent: RepositoryEvent) {
        with(repositoryEvent) {
            if (eventId.isBlank()) {
                throw ParamBlankException("Invalid eventId")
            }
            if (scmId.isBlank()) {
                throw ParamBlankException("Invalid scmId")
            }
            if (triggerType.isBlank()) {
                throw ParamBlankException("Invalid triggerType")
            }
            if (eventType.isBlank()) {
                throw ParamBlankException("Invalid eventType")
            }
            repositoryEventDao.saveRepositoryEvent(
                dslContext = dslContext,
                repositoryEvent = repositoryEvent
            )
        }
    }



    fun replay(
        userId: String,
        eventId: String,
        pipelineList: List<ReplayPipelineEvent>?
    ) {
        if (userId.isBlank()){
            throw ParamBlankException("Invalid userId")
        }
        if (eventId.isBlank()){
            throw ParamBlankException("Invalid eventId")
        }
        logger.info("replay repository event|userId[$userId]|eventId[$eventId]")
    }

    companion object {
        private val logger = LoggerFactory.getLogger(RepositoryEventService::class.java)
    }
}
