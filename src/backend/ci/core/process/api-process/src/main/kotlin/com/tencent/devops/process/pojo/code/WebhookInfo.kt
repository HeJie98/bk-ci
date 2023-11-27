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

package com.tencent.devops.process.pojo.code

import com.tencent.devops.common.pipeline.enums.StartType
import com.tencent.devops.common.service.utils.HomeHostUtil
import io.swagger.annotations.ApiModelProperty

data class WebhookInfo(
    @ApiModelProperty("代码库类型", required = true)
    val codeType: String?, // CodeType.name
    @ApiModelProperty("代码库完整名称", required = true)
    val nameWithNamespace: String?,
    @ApiModelProperty("仓库url链接", required = false)
    val webhookRepoUrl: String?,
    @ApiModelProperty("分支名（目标分支）", required = false)
    val webhookBranch: String?,
    @ApiModelProperty("别名", required = false)
    val webhookAliasName: String?,
    @ApiModelProperty("webhook类型", required = false)
    val webhookType: String?,
    @ApiModelProperty("事件类型", required = false)
    val webhookEventType: String?,
    @ApiModelProperty("提交信息", required = false)
    val webhookMessage: String?,
    @ApiModelProperty("提交信息id", required = false)
    val webhookCommitId: String?,
    @ApiModelProperty("参考信息(commit_id,mr_id,tag,issue_id,review_id,note_id等)", required = true)
    val refId: String?,
    @ApiModelProperty("合并后commitId", required = false)
    val webhookMergeCommitSha: String?,
    @ApiModelProperty("源分支", required = false)
    val webhookSourceBranch: String?,
    @ApiModelProperty("mr id", required = false)
    val mrId: String?,
    @ApiModelProperty("mr iid", required = false)
    val mrIid: String?,
    @ApiModelProperty("mrUrl", required = false)
    val mrUrl: String?,
    @ApiModelProperty("webhook仓库授权用户", required = false)
    val repoAuthUser: String?,
    @ApiModelProperty("tag 名称", required = false)
    val tagName: String?,
    @ApiModelProperty("issue iid", required = false)
    val issueIid: String?,
    @ApiModelProperty("note id", required = false)
    val noteId: String?,
    @ApiModelProperty("review id", required = false)
    val reviewId: String?,
    @ApiModelProperty("事件链接，webhook触发时使用", required = false)
    val eventUrl: String?,
    @ApiModelProperty("父流水线项目Id", required = false)
    val parentProjectId: String?,
    @ApiModelProperty("父流水线Id", required = false)
    val parentPipelineId: String?,
    @ApiModelProperty("父流水线构建Id", required = false)
    val parentBuildId: String?,
    @ApiModelProperty("父流水线插件Id", required = false)
    val parentTaskId: String?,
    @ApiModelProperty("父流水线名称", required = false)
    val parentPipelineName: String? = "",
    @ApiModelProperty("父流水线构建序号", required = false)
    var parentPipelineBuildNum: String? = "",
    @ApiModelProperty("启动类型", required = false)
    val startType: String? = StartType.WEB_HOOK.name
){
    fun getLinkUrl() = when (startType) {
        StartType.WEB_HOOK.name -> {
            eventUrl
        }

        StartType.PIPELINE.name -> {
            "${HomeHostUtil.innerServerHost()}/console" +
                    "/pipeline/$parentBuildId/$parentPipelineId/detail/$parentBuildId/executeDetail"
        }

        else -> ""
    }
}
