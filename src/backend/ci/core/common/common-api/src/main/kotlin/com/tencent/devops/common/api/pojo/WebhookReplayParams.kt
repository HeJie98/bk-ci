package com.tencent.devops.common.api.pojo

import com.tencent.devops.common.api.enums.ScmType
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

@ApiModel("webhook重放参数模型")
data class WebhookReplayParams constructor(
    @ApiModelProperty("代码库类型")
    val scmType: ScmType,
    @ApiModelProperty("事件类型")
    val eventType: String,
    @ApiModelProperty("X-Token")
    val secret: String? = null,
    @ApiModelProperty("X-TRACE-ID")
    val traceId: String? = null,
    @ApiModelProperty("hook信息主体")
    val body: String,
    @ApiModelProperty("触发流水线信息")
    val pipelineList: List<ReplayPipelineInfo>?
)

@ApiModel("流水线事件回放模型")
data class ReplayPipelineInfo constructor(
    @ApiModelProperty("蓝盾项目ID")
    val projectId: String,
    @ApiModelProperty("流水线ID")
    val pipelineId: String
)