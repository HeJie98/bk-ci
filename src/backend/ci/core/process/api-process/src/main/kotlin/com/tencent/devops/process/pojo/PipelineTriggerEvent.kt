package com.tencent.devops.process.pojo

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

@ApiModel("流水线触发事件模型")
data class PipelineTriggerEvent(
    @ApiModelProperty("蓝盾项目ID")
    val projectId: String,
    @ApiModelProperty("事件ID")
    val eventId: String,
    @ApiModelProperty("触发类型")
    val triggerType: String,
    @ApiModelProperty("事件触发源", required = false)
    val eventSource: String? = "",
    @ApiModelProperty("事件类型")
    val eventType: String?,
    @ApiModelProperty("触发人")
    val triggerUser: String,
    @ApiModelProperty("事件信息")
    val eventMessage: String,
    @ApiModelProperty("触发状态")
    val status: String,
    @ApiModelProperty("流水线Id")
    val pipelineId: String,
    @ApiModelProperty("流水线名称")
    val pipelineName: String,
    @ApiModelProperty("构建Id")
    val buildId: String,
    @ApiModelProperty("构建编号")
    val buildNum: String,
    @ApiModelProperty("原因")
    val reason: String,
    @ApiModelProperty("原因详情", required = false)
    val reasonDetail: String,
    @ApiModelProperty("触发时间")
    val createTime: String
)
