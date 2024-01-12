package com.tencent.devops.notify.tencentcloud.pojo

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

data class EmailBody(
    @JsonProperty("Destination")
    @Schema(description = "收件者信息")
    val destination: List<String>,
    @JsonProperty("FromEmailAddress")
    @Schema(description = "发送者信息")
    val fromEmailAddress: String, // QCLOUDTEAM <noreply@mail.qcloud.com>
    @JsonProperty("ReplyToAddresses")
    @Schema(description = "回复地址")
    val replyToAddresses: String? = null, // qcloud@tencent.com
    @JsonProperty("Template")
    @Schema(description = "邮件模板内容")
    val template: Template,
    @JsonProperty("Subject")
    @Schema(description = "主题")
    val subject: String // YourTestSubject
)

data class Template(
    @JsonProperty("TemplateID")
    val templateID: Int? = null, // 100091
    @JsonProperty("TemplateData")
    val templateData: String? = null // {\"code\":\"1234\"}
)
