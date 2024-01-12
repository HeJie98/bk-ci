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

package com.tencent.devops.repository.pojo

import com.tencent.devops.common.api.enums.ScmType
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "代码库模型-基本信息")
data class RepositoryInfoWithPermission(
    @Schema(description = "仓库哈希ID", required = true)
    val repositoryHashId: String,
    @Schema(description = "仓库别名", required = true)
    val aliasName: String,
    @Schema(description = "URL", required = true)
    val url: String,
    @Schema(description = "类型", required = true)
    val type: ScmType,
    @Schema(description = "最后更新时间", required = true)
    val updatedTime: Long,
    @Schema(description = "最后更新用户", required = false)
    val updatedUser: String?,
    @Schema(description = "创建时间", required = true)
    val createTime: Long,
    @Schema(description = "创建人", required = true)
    val createUser: String,
    @Schema(description = "能否被编辑", required = true)
    val canEdit: Boolean,
    @Schema(description = "能否被删除", required = true)
    val canDelete: Boolean,
    @Schema(description = "能否被查看", required = true)
    val canView: Boolean? = null,
    @Schema(description = "能否被使用", required = true)
    val canUse: Boolean? = null,
    @Schema(description = "认证类型", required = false)
    val authType: String = "",
    @Schema(description = "svn的protocal类型（http|ssh）", required = false)
    val svnType: String? = null,
    @Schema(description = "授权身份", required = true)
    val authIdentity: String? = null
)
