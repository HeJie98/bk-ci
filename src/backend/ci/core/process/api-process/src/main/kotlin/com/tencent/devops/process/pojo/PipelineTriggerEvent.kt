package com.tencent.devops.process.pojo

import com.tencent.devops.common.pipeline.enums.PipelineTriggerType
import com.tencent.devops.common.pipeline.pojo.element.trigger.enums.CodeEventType
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

@ApiModel("流水线触发事件模型")
data class PipelineTriggerEvent(
    @ApiModelProperty("蓝盾项目ID")
    val projectId: String,
    @ApiModelProperty("事件ID")
    val eventId: String,
    @ApiModelProperty("触发类型")
    val triggerType: PipelineTriggerType,
    @ApiModelProperty("事件触发源", required = false)
    val eventSource: String? = "",
    @ApiModelProperty("事件类型")
    val eventType: CodeEventType?,
    @ApiModelProperty("触发人")
    val triggerUser: String,
    @ApiModelProperty("事件信息")
    val eventMessage: PipelineTriggerEventMessage,
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

@ApiModel("流水线触发事件信息模型")
data class PipelineTriggerEventMessage(
    @ApiModelProperty("链接标题,commit事件——short commit sha;MR/CR/NOTE/ISSUE事件——对应的IID", required = false)
    val linkTitle: String? = "",
    @ApiModelProperty("链接地址", required = false)
    val linkUrl: String? = "",
    @ApiModelProperty("事件描述,commit事件——[branch_name]commit;MR/CR/NOTE/ISSUE事件——对应的事件名全称")
    val eventDesc: String = "",
    @ApiModelProperty("事件动作", required = false)
    val actionDesc: String? = ""
)