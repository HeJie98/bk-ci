package com.tencent.devops.common.auth.authorization

import com.tencent.devops.auth.api.service.ServiceAuthAuthorizationResource
import com.tencent.devops.common.auth.api.AuthAuthorizationApi
import com.tencent.devops.common.auth.api.pojo.ResourceAuthorizationDTO
import com.tencent.devops.common.auth.api.pojo.ResourceAuthorizationHandoverDTO
import com.tencent.devops.common.auth.api.pojo.ResourceAuthorizationHandoverResult
import com.tencent.devops.common.auth.enums.ResourceAuthorizationHandoverStatus
import com.tencent.devops.common.client.Client
import org.slf4j.LoggerFactory

class AuthAuthorizationService(
    private val client: Client
) : AuthAuthorizationApi {
    override fun batchModifyHandoverFrom(
        projectId: String,
        resourceAuthorizationHandoverList: List<ResourceAuthorizationHandoverDTO>
    ) {
        logger.info("batch modify handoverfrom|$projectId|$resourceAuthorizationHandoverList")
        client.get(ServiceAuthAuthorizationResource::class).batchModifyHandoverFrom(
            projectId = projectId,
            resourceAuthorizationHandoverList = resourceAuthorizationHandoverList
        )
    }

    override fun addResourceAuthorization(
        projectId: String,
        resourceAuthorizationList: List<ResourceAuthorizationDTO>
    ) {
        logger.info("add resource authorization|$projectId|$resourceAuthorizationList")
        client.get(ServiceAuthAuthorizationResource::class).addResourceAuthorization(
            projectId = projectId,
            resourceAuthorizationList = resourceAuthorizationList
        )
    }

    override fun resetResourceAuthorization(
        projectId: String,
        preCheck: Boolean,
        resourceAuthorizationHandoverDTOs: List<ResourceAuthorizationHandoverDTO>,
        handoverResourceAuthorization: (
            Boolean,
            ResourceAuthorizationHandoverDTO
        ) -> ResourceAuthorizationHandoverResult
    ): Map<ResourceAuthorizationHandoverStatus, List<ResourceAuthorizationHandoverDTO>> {
        logger.info("reset resource authorization|$preCheck|$projectId|$resourceAuthorizationHandoverDTOs")
        val (successList, failedList) = resourceAuthorizationHandoverDTOs.map { resourceAuthorization ->
            val result = try {
                handoverResourceAuthorization.invoke(
                    preCheck,
                    resourceAuthorization
                )
            } catch (ignore: Exception) {
                ResourceAuthorizationHandoverResult(
                    status = ResourceAuthorizationHandoverStatus.FAILED,
                    message = ignore.message
                )
            }
            when (result.status) {
                ResourceAuthorizationHandoverStatus.SUCCESS -> resourceAuthorization
                else -> resourceAuthorization.copy(handoverFailedMessage = result.message)
            }
        }.partition { it.handoverFailedMessage == null }
        return mapOf(
            ResourceAuthorizationHandoverStatus.SUCCESS to successList,
            ResourceAuthorizationHandoverStatus.FAILED to failedList
        )
    }

    companion object {
        private val logger = LoggerFactory.getLogger(AuthAuthorizationService::class.java)
    }
}
