package com.tencent.devops.repository.pojo

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

@ApiModel("流水线事件回放模型")
data class ReplayPipelineEvent constructor(
    @ApiModelProperty("蓝盾项目ID")
    val projectId: String,
    @ApiModelProperty("流水线ID")
    val pipelineId: String
)