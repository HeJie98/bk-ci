/*
 * Tencent is pleased to support the open source community by making BK-CI 蓝鲸持续集成平台 available.
 *
 * Copyright (C) 2019 THL A29 Limited, a Tencent company.  All rights reserved.
 *
 * BK-CI 蓝鲸持续集成平台 is licensed under the MIT license.
 *
 * A copy of the MIT License is included in this file.
 *
 *
 * Terms of the MIT License:
 * ---------------------------------------------------
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of
 * the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
 * LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN
 * NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.tencent.devops.quality.api.v2.pojo.request

import com.tencent.devops.common.notify.enums.NotifyType
import com.tencent.devops.quality.pojo.enum.RuleOperation
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "规则更新请求")
data class RuleUpdateRequest(
    @Schema(description = "规则名称", required = true)
    val name: String,
    @Schema(description = "规则描述", required = true)
    val desc: String,
    @Schema(description = "指标类型", required = true)
    val indicatorIds: List<CreateRequestIndicator>,
    @Schema(description = "控制点", required = true)
    val controlPoint: String,
    @Schema(description = "控制点位置", required = true)
    val controlPointPosition: String,
    @Schema(description = "生效的流水线id集合", required = true)
    val range: List<String>,
    @Schema(description = "生效的流水线模板id集合", required = true)
    val templateRange: List<String>,
    @Schema(description = "操作类型", required = true)
    val operation: RuleOperation,
    @Schema(description = "通知类型", required = false)
    val notifyTypeList: List<NotifyType>?,
    @Schema(description = "通知组名单", required = false)
    val notifyGroupList: List<String>?,
    @Schema(description = "通知人员名单", required = false)
    val notifyUserList: List<String>?,
    @Schema(description = "审核通知人员", required = false)
    val auditUserList: List<String>?,
    @Schema(description = "审核超时时间", required = false)
    val auditTimeoutMinutes: Int?,
    @Schema(description = "红线匹配的id(必填)", required = true)
    val gatewayId: String?
) {
    data class CreateRequestIndicator(
        val hashId: String,
        val operation: String,
        val threshold: String
    )
}
