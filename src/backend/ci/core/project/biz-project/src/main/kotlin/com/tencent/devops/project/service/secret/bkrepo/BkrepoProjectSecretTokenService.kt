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

package com.tencent.devops.project.service.secret.bkrepo

import com.tencent.bkrepo.repository.pojo.project.UserProjectCreateRequest
import com.tencent.devops.common.api.util.JsonUtil
import com.tencent.devops.project.pojo.ProjectCallbackData
import com.tencent.devops.project.pojo.SecretRequestParam
import com.tencent.devops.project.pojo.secret.bkrepo.BkrepoProjectSecretParam
import org.slf4j.LoggerFactory
import java.lang.Exception

class BkrepoProjectSecretTokenService : BkrepoSecretTokenCommonService<BkrepoProjectSecretParam> {
    override fun getSecretRequestParam(
        userId: String,
        projectId: String,
        secretParam: BkrepoProjectSecretParam
    ): SecretRequestParam {
        return SecretRequestParam(
            header = getCommonHeaders(userId, projectId),
            url = secretParam.url,
            secretType = BkrepoProjectSecretParam.classType
        )
    }

    override fun getRequestBody(secretParam: BkrepoProjectSecretParam, projectCallbackData: ProjectCallbackData): String {
        val projectId = projectCallbackData.getProjectId()
        val userProjectCreateRequest = UserProjectCreateRequest(
            name = projectId,
            displayName = projectId,
            description = projectId
        )
        logger.info("start wrapping the request body|$userProjectCreateRequest")
        return JsonUtil.toJson(userProjectCreateRequest, false)
    }

    override fun requestFail(exception: Exception) {
        super.requestFail(exception)
    }

    override fun requestSuccess() {
        super.requestSuccess()
    }

    companion object{
        private val logger = LoggerFactory.getLogger(BkrepoProjectSecretTokenService::class.java)
    }
}