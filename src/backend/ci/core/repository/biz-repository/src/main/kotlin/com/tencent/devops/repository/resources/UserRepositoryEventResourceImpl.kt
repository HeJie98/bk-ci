package com.tencent.devops.repository.resources

import com.tencent.devops.common.api.pojo.Result
import com.tencent.devops.repository.api.UserRepositoryEventResource
import com.tencent.devops.repository.pojo.ReplayPipelineEvent
import com.tencent.devops.repository.service.RepositoryEventService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserRepositoryEventResourceImpl @Autowired constructor(
    val repositoryEventService: RepositoryEventService
) : UserRepositoryEventResource {
    override fun replay(
        userId: String,
        eventId: String,
        pipelineList: List<ReplayPipelineEvent>?
    ): Result<Boolean> {
        repositoryEventService.replay(
            userId = userId,
            eventId = eventId,
            pipelineList = pipelineList
        )
        return Result(true)
    }

    companion object {
        private const val PageSize = 20
    }
}