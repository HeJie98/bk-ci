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

package com.tencent.devops.store.pojo.template

import com.tencent.devops.store.pojo.common.Category
import com.tencent.devops.store.pojo.common.HonorInfo
import com.tencent.devops.store.pojo.common.Label
import com.tencent.devops.store.pojo.common.index.StoreIndexInfo
import com.tencent.devops.store.pojo.common.StoreUserCommentInfo
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "模板详情")
data class TemplateDetail(
    @Schema(description = "模板ID", required = true)
    val templateId: String,
    @Schema(description = "模板代码", required = true)
    val templateCode: String,
    @Schema(description = "模板名称", required = true)
    val templateName: String,
    @Schema(description = "模板logo", required = false)
    val logoUrl: String?,
    @Schema(description = "所属模板分类代码", required = false)
    val classifyCode: String?,
    @Schema(description = "所属模板分类名称", required = false)
    val classifyName: String?,
    @Schema(description = "下载量", required = true)
    val downloads: Int,
    @Schema(description = "星级评分", required = false)
    val score: Double?,
    @Schema(description = "简介", required = false)
    val summary: String?,
    @Schema(description = "模板状态，INIT：初始化|AUDITING：审核中|AUDIT_REJECT：审核驳回|RELEASED：已发布|" +
        "GROUNDING_SUSPENSION：上架中止|UNDERCARRIAGED：已下架", required = true)
    val templateStatus: String,
    @Schema(description = "模板描述", required = false)
    val description: String?,
    @Schema(description = "版本号", required = false)
    val version: String?,
    @Schema(description = "模板研发类型，SELF_DEVELOPED：自研 THIRD_PARTY：第三方开发", required = true)
    val templateRdType: String,
    @Schema(description = "范畴列表", required = false)
    val categoryList: List<Category>?,
    @Schema(description = "标签列表", required = false)
    val labelList: List<Label>?,
    @Schema(description = "是否为最新版本模板 true：最新 false：非最新", required = true)
    val latestFlag: Boolean,
    @Schema(description = "发布者", required = false)
    val publisher: String?,
    @Schema(description = "发布者描述", required = false)
    val pubDescription: String?,
    @Schema(description = "是否可安装标识", required = false)
    val flag: Boolean?,
    @Schema(description = "是否有处于上架状态的模板版本", required = true)
    val releaseFlag: Boolean,
    @Schema(description = "用户评论信息", required = true)
    val userCommentInfo: StoreUserCommentInfo,
    @Schema(description = "荣誉信息", required = false)
    val honorInfos: List<HonorInfo>? = null,
    @Schema(description = "指标信息", required = false)
    val indexInfos: List<StoreIndexInfo>? = null
)
