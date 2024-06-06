package com.tencent.devops.auth.pojo.request

import com.tencent.devops.auth.pojo.ResourceMemberInfo
import io.swagger.v3.oas.annotations.media.Schema

@Schema(title = "一键移出用户出项目")
data class RemoveMemberFromProjectReq(
    @get:Schema(title = "目标对象")
    val targetMember: ResourceMemberInfo,
    @get:Schema(title = "授予人")
    val handoverTo: ResourceMemberInfo?
)
