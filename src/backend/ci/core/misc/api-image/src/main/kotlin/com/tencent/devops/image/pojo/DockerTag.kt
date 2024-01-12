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

package com.tencent.devops.image.pojo

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Docker标签模型")
data class DockerTag(
    @Schema(description = "标签")
    var tag: String? = null,
    @Schema(description = "仓库")
    var repo: String? = null,
    @Schema(description = "镜像")
    var image: String? = null,
    @Schema(description = "创建者")
    var createdBy: String? = null,
    @Schema(description = "创建时间")
    var created: String? = null,
    @Schema(description = "修改时间")
    var modified: String? = null,
    @Schema(description = "修改者")
    var modifiedBy: String? = null,
    @Schema(description = "描述")
    var desc: String? = "",
    @Schema(description = "大小")
    var size: String? = null,
    @Schema(description = "构件列表")
    var artifactorys: List<String>? = null,
    @Schema(description = "是否已关联到store")
    var storeFlag: Boolean? = null
)
