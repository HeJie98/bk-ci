package com.tencent.devops.repository.service.permission

import com.tencent.devops.common.api.enums.RepositoryConfig
import com.tencent.devops.common.api.enums.RepositoryType
import com.tencent.devops.common.api.enums.ScmType
import com.tencent.devops.common.api.exception.PermissionForbiddenException
import com.tencent.devops.common.api.util.HashUtil
import com.tencent.devops.common.api.util.MessageUtil
import com.tencent.devops.common.auth.api.AuthAuthorizationApi
import com.tencent.devops.common.auth.api.AuthPermission
import com.tencent.devops.common.auth.api.pojo.ResourceAuthorizationDTO
import com.tencent.devops.common.auth.api.pojo.ResourceAuthorizationHandoverDTO
import com.tencent.devops.common.auth.api.pojo.ResourceAuthorizationHandoverResult
import com.tencent.devops.common.auth.enums.ResourceAuthorizationHandoverStatus
import com.tencent.devops.common.web.utils.I18nUtil
import com.tencent.devops.repository.constant.RepositoryMessageCode
import com.tencent.devops.repository.service.RepositoryService
import com.tencent.devops.repository.service.RepositoryUserService
import com.tencent.devops.repository.service.github.GithubTokenService
import com.tencent.devops.repository.service.scm.GitOauthService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class RepositoryAuthorizationService constructor(
    private val authAuthorizationApi: AuthAuthorizationApi,
    private val repositoryService: RepositoryService,
    private val githubTokenService: GithubTokenService,
    private val gitOauthService: GitOauthService,
    private val repositoryUserService: RepositoryUserService
) {
    fun batchModifyHandoverFrom(
        projectId: String,
        resourceAuthorizationHandoverList: List<ResourceAuthorizationHandoverDTO>
    ) {
        authAuthorizationApi.batchModifyHandoverFrom(
            projectId = projectId,
            resourceAuthorizationHandoverList = resourceAuthorizationHandoverList
        )
    }

    fun addResourceAuthorization(
        projectId: String,
        resourceAuthorizationList: List<ResourceAuthorizationDTO>
    ) {
        authAuthorizationApi.addResourceAuthorization(
            projectId = projectId,
            resourceAuthorizationList = resourceAuthorizationList
        )
    }

    fun resetRepositoryAuthorization(
        projectId: String,
        preCheck: Boolean,
        resourceAuthorizationHandoverDTOs: List<ResourceAuthorizationHandoverDTO>
    ): Map<ResourceAuthorizationHandoverStatus, List<ResourceAuthorizationHandoverDTO>> {
        logger.info("reset repository authorization|$preCheck|$projectId|$resourceAuthorizationHandoverDTOs")
        return authAuthorizationApi.resetResourceAuthorization(
            projectId = projectId,
            preCheck = preCheck,
            resourceAuthorizationHandoverDTOs = resourceAuthorizationHandoverDTOs,
            handoverResourceAuthorization = ::handoverRepositoryAuthorization
        )
    }

    private fun handoverRepositoryAuthorization(
        preCheck: Boolean,
        resourceAuthorizationHandoverDTO: ResourceAuthorizationHandoverDTO
    ): ResourceAuthorizationHandoverResult {
        with(resourceAuthorizationHandoverDTO) {
            val handoverTo = handoverTo!!
            try {
                validateResourcePermission(
                    userId = resourceAuthorizationHandoverDTO.handoverTo!!,
                    projectCode = resourceAuthorizationHandoverDTO.projectCode,
                    resourceName = resourceAuthorizationHandoverDTO.resourceName,
                    resourceCode = resourceAuthorizationHandoverDTO.resourceCode
                )
                // Check if the grantor has used oauth
                val isUsedOauth = repositoryService.serviceGet(
                    projectId = projectCode,
                    repositoryConfig = RepositoryConfig(
                        repositoryHashId = resourceCode,
                        repositoryName = null,
                        repositoryType = RepositoryType.ID
                    )
                ).getScmType().let { scmType ->
                    when (scmType) {
                        ScmType.GITHUB -> githubTokenService.getAccessToken(handoverTo) != null
                        ScmType.CODE_GIT -> gitOauthService.getAccessToken(handoverTo) != null
                        else -> false
                    }
                }
                if (!isUsedOauth) {
                    return ResourceAuthorizationHandoverResult(
                        status = ResourceAuthorizationHandoverStatus.FAILED,
                        message = MessageUtil.getMessageByLocale(
                            messageCode = RepositoryMessageCode.ERROR_USER_HAVE_NOT_USED_OAUTH,
                            language = I18nUtil.getLanguage(handoverTo)
                        )
                    )
                }
                if (!preCheck) {
                    repositoryUserService.updateRepositoryUserInfo(
                        userId = handoverTo,
                        projectCode = projectCode,
                        repositoryHashId = resourceCode,
                    )
                }
            } catch (ignore: Exception) {
                return ResourceAuthorizationHandoverResult(
                    status = ResourceAuthorizationHandoverStatus.FAILED,
                    message = when (ignore) {
                        is PermissionForbiddenException -> ignore.defaultMessage
                        else -> ignore.message
                    }
                )
            }
            return ResourceAuthorizationHandoverResult(
                status = ResourceAuthorizationHandoverStatus.SUCCESS
            )
        }
    }


    private fun validateResourcePermission(
        userId: String,
        projectCode: String,
        resourceName: String,
        // 代码库hashID
        resourceCode: String
    ) {
        val repositoryId = HashUtil.decodeOtherIdToLong(resourceCode)
        repositoryService.validatePermission(
            user = userId,
            projectId = projectCode,
            repositoryId = repositoryId,
            authPermission = AuthPermission.EDIT,
            message = MessageUtil.getMessageByLocale(
                messageCode = RepositoryMessageCode.USER_EDIT_PEM_ERROR,
                params = arrayOf(userId, projectCode, resourceName),
                language = I18nUtil.getLanguage(userId)
            )
        )
        val repositoryRecord = repositoryService.getRepository(
            projectId = projectCode,
            repositoryConfig = RepositoryConfig(
                repositoryName = null,
                repositoryHashId = resourceCode,
                repositoryType = RepositoryType.ID
            )
        )
        val repository = repositoryService.compose(repositoryRecord)
        repositoryService.checkRepoDownloadPem(
            userId = userId,
            projectId = projectCode,
            repository = repository
        )
        repositoryService.reOauth(
            repository = repository,
            repositoryRecord = repositoryRecord,
            userId = userId,
            projectId = projectCode
        )
    }

    companion object {
        private val logger = LoggerFactory.getLogger(RepositoryAuthorizationService::class.java)
    }
}
