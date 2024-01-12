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

package com.tencent.devops.process.pojo.pipeline

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "构建模型-ID")
data class SubPipelineStartUpInfo(
    @Schema(description = "参数key值", required = true)
    val key: String,
    @Schema(description = "key值是否可以更改", required = true)
    val keyDisable: Boolean,
    @Schema(description = "key值前端组件类型", required = true)
    val keyType: String,
    @Schema(description = "key值获取方式", required = true)
    val keyListType: String,
    @Schema(description = "key值获取路径", required = true)
    val keyUrl: String,
    @Schema
    val keyUrlQuery: List<String>,
    @Schema(description = "key值获取集合", required = true)
    val keyList: List<StartUpInfo>,
    @Schema(description = "key值是否多选", required = true)
    val keyMultiple: Boolean,
    @Schema(description = "参数value值", required = true)
    val value: Any,
    @Schema(description = "value值是否可以更改", required = true)
    val valueDisable: Boolean,
    @Schema(description = "value值前端组件类型", required = true)
    val valueType: String,
    @Schema(description = "value值获取方式", required = true)
    val valueListType: String,
    @Schema(description = "value值获取路径", required = true)
    val valueUrl: String,
    @Schema
    val valueUrlQuery: List<String>,
    @Schema(description = "value值获取集合", required = true)
    val valueList: List<StartUpInfo>,
    @Schema(description = "value值是否多选", required = true)
    val valueMultiple: Boolean
)
