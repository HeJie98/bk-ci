package com.tencent.devops.repository.pojo

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

@ApiModel("代码库触发事件信息")
data class RepositoryEvent constructor(
    @ApiModelProperty("事件Id，网关traceId")
    val eventId: String,
    @ApiModelProperty("代码库标识符")
    val scmId: String,
    @ApiModelProperty("触发器类型")
    val triggerType: String,
    @ApiModelProperty("事件类型")
    val eventType: String,
    @ApiModelProperty("事件信息主体，json格式字符串")
    val eventBody: String
)