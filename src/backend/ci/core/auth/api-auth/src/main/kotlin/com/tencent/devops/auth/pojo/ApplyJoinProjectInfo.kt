package com.tencent.devops.auth.pojo

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "项目-申请加入项目实体类")
data class ApplyJoinProjectInfo(
    @Schema(description = "过期时间")
    val expireTime: String,
    @Schema(description = "申请理由")
    val reason: String
)
