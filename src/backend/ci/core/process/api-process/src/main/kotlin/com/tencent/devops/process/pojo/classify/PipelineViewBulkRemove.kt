package com.tencent.devops.process.pojo.classify

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "流水线组批量移除")
data class PipelineViewBulkRemove(
    @Schema(description = "流水线ID列表")
    val pipelineIds: List<String>,
    @Schema(description = "视图ID")
    val viewId: String
)
