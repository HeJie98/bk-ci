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
 *
 */

package com.tencent.devops.repository.pojo

import io.swagger.v3.oas.annotations.media.Schema

data class RepoTriggerRefVo(
    val projectId: String,
    @Schema(description = "代码库Id")
    val repositoryHashId: String,
    @Schema(description = "插件code")
    val atomCode: String,
    @Schema(description = "触发类型")
    val triggerType: String,
    @Schema(description = "事件类型描述，根据[eventType]进行国际化")
    val eventTypeDesc: String,
    @Schema(description = "插件参数")
    val taskParams: Map<String, Any>,
    @Schema(description = "触发条件")
    val triggerCondition: Map<String, Any>?,
    @Schema(description = "触发条件md5")
    val triggerConditionMd5: String?,
    @Schema(description = "流水线引用数量")
    val pipelineRefCount: Int,
    val atomLogo: String? = null,
    @Schema(description = "事件类型key")
    val eventType: String
)
