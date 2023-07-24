package com.tencent.devops.process.pojo

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

@ApiModel("代码库触发事件信息")
data class RepositoryEventHistory constructor(
    @ApiModelProperty("事件Id，网关traceId")
    val eventId: String,
    @ApiModelProperty("事件信息")
    val eventMessage: PipelineTriggerEventMessage,
    @ApiModelProperty("事件类型")
    val eventType: String,
    @ApiModelProperty("事件发生时间")
    val eventTime: String,
    @ApiModelProperty("触发总数")
    val triggerCount: Int,
    @ApiModelProperty("触发成功数")
    val successCount: Int
)

data class RepositoryEventInfo constructor(
    @ApiModelProperty("事件Id，网关traceId")
    val eventId: String,
    @ApiModelProperty("事件类型")
    val eventType: String,
    @ApiModelProperty("事件信息")
    val eventMessage: String
)