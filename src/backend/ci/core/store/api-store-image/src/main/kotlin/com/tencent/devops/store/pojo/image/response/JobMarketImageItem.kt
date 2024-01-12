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
package com.tencent.devops.store.pojo.image.response

import com.tencent.devops.store.pojo.image.enums.ImageAgentTypeEnum
import io.swagger.v3.oas.annotations.media.Schema

/**
 * @Description
 * @Date 2019/9/17
 * @Version 1.0
 */
@Schema(description = "镜像详情")
data class JobMarketImageItem(

    @Schema(description = "镜像ID", required = true)
    val imageId: String,

    @Schema(description = "镜像ID（兼容多种解析方式）", required = true)
    val id: String,

    @Schema(description = "镜像代码", required = true)
    val imageCode: String,

    @Schema(description = "镜像代码（兼容多种解析方式）", required = true)
    val code: String,

    @Schema(description = "镜像名称", required = true)
    val imageName: String,

    @Schema(description = "镜像名称（兼容多种解析方式）", required = true)
    val name: String,

    @Schema(description = "研发来源")
    val rdType: String,

    @Schema(description = "镜像适用的Agent类型")
    var agentTypeScope: List<ImageAgentTypeEnum>,

    @Schema(description = "当前Agent类型下是否可用")
    val availableFlag: Boolean,

    @Schema(description = "镜像logo", required = true)
    val logoUrl: String,

    @Schema(description = "镜像图标", required = true)
    val icon: String,

    @Schema(description = "镜像简介", required = true)
    val summary: String,

    @Schema(description = "镜像说明文档链接", required = false)
    val docsLink: String?,

    @Schema(description = "权重", required = true)
    val weight: Int,

    @Schema(description = "镜像来源 BKDEVOPS:蓝盾，THIRD:第三方", required = true)
    val imageSourceType: String,

    @Schema(description = "镜像仓库Url", required = true)
    val imageRepoUrl: String,

    @Schema(description = "镜像仓库名称", required = true)
    val imageRepoName: String,

    @Schema(description = "镜像tag", required = true)
    val imageTag: String,

    @Schema(description = "dockerFile类型", required = true)
    val dockerFileType: String,

    @Schema(description = "dockerFile内容", required = true)
    val dockerFileContent: String,

    @Schema(description = "逗号分隔的Label名称", required = true)
    val labelNames: String,

    @Schema(description = "范畴code", required = true)
    val category: String,

    @Schema(description = "范畴名称", required = true)
    val categoryName: String,

    @Schema(description = "发布者", required = true)
    val publisher: String,

    @Schema(description = "是否为公共镜像 true：是 false：否", required = true)
    val publicFlag: Boolean,

    @Schema(description = "是否可安装 true：可以 false：不可以", required = true)
    val flag: Boolean,

    @Schema(description = "是否推荐 true：推荐 false：不推荐", required = true)
    val recommendFlag: Boolean,

    @Schema(description = "是否官方认证 true：是 false：否", required = true)
    val certificationFlag: Boolean,

    @Schema(description = "是否已安装", required = true)
    var installedFlag: Boolean? = null,

    @Schema(description = "最近修改人", required = true)
    val modifier: String,

    @Schema(description = "最近修改时间", required = true)
    val updateTime: Long
)
