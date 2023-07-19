package com.tencent.devops.auth.service.oauth2.grant

import org.slf4j.LoggerFactory


class AuthorizationCodeTokenGranter : AbstractTokenGranter(GRANT_TYPE) {
    companion object {
        private const val GRANT_TYPE = "authorization_code"
        private val logger = LoggerFactory.getLogger(AuthorizationCodeTokenGranter::class.java)
    }

    override fun getAccessToken(grantType: String): String {
        logger.info("authorization_code getAccessToken")
        // 1.获取授权码，判断授权是否为空
        // 2.判断授权码和客户端id是否对应的上
        // 3.判断授权码是否过期
        // 4.若授权码没有问题，则直接消费授权码，授权码单次有效，直接数据库删除该授权码
        // 5、根据appcode+username获取accessToken
        val accessToken = super.getAccessToken(grantType)
        // 6、删除refreshToken,并创建新的refreshToken.
        return accessToken + "refreshToken"
    }
}
