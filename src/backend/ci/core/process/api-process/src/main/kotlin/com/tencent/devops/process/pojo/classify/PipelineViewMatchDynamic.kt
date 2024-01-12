package com.tencent.devops.process.pojo.classify

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "命中动态组情况")
data class PipelineViewMatchDynamic(
    @Schema(description = "流水线名称")
    val pipelineName: String,
    @Schema(description = "标签列表")
    val labels: List<LabelInfo>
) {
    @Schema(description = "标签信息")
    data class LabelInfo(
        @Schema(description = "标签分组id", required = false)
        val groupId: String,
        @Schema(description = "标签id列表", required = false)
        val labelIds: List<String>
    )
}
