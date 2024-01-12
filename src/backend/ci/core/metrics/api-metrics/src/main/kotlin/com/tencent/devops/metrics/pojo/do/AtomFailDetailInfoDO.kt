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

package com.tencent.devops.metrics.pojo.`do`

import com.fasterxml.jackson.annotation.JsonFormat
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "插件失败详情信息")
data class AtomFailDetailInfoDO(
    @Schema(description = "项目ID")
    val projectId: String,
    @Schema(description = "流水线ID")
    val pipelineId: String,
    @Schema(description = "流水线名称")
    val pipelineName: String,
    @Schema(description = "渠道代码")
    val channelCode: String,
    @Schema(description = "域名")
    var domain: String? = null,
    @Schema(description = "构建ID")
    val buildId: String,
    @Schema(description = "构建序号")
    val buildNum: Int,
    @Schema(description = "插件代码")
    val atomCode: String,
    @Schema(description = "插件名称")
    val atomName: String,
    @Schema(description = "插件在model中的位置")
    val atomPosition: String,
    @Schema(description = "插件分类代码")
    val classifyCode: String,
    @Schema(description = "启动用户")
    val startUser: String,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "启动时间")
    val startTime: LocalDateTime?,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "结束时间")
    val endTime: LocalDateTime?,
    @Schema(description = "错误的类型标识")
    val errorType: Int?,
    @Schema(description = "错误的类型标识名称")
    var errorTypeName: String? = null,
    @Schema(description = "错误的标识码")
    val errorCode: Int?,
    @Schema(description = "错误描述")
    val errorMsg: String?
)
