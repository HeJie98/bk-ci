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

package com.tencent.devops.common.pipeline.enums

import com.tencent.devops.common.api.enums.ScmType
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

@ApiModel("流水线触发类型")
enum class PipelineTriggerType {
    // WEB_HOOK 触发
    @ApiModelProperty("SVN 代码库")
    CODE_SVN,
    @ApiModelProperty("GIT 代码库")
    CODE_GIT,
    @ApiModelProperty("Gitlab 代码库")
    CODE_GITLAB,
    @ApiModelProperty("Github 代码库")
    GITHUB,
    @ApiModelProperty("TGIT 代码库")
    CODE_TGIT,
    @ApiModelProperty("P4 代码库")
    CODE_P4,

    // 手动触发
    @ApiModelProperty("手动触发")
    MANUAL,

    // 定时触发
    @ApiModelProperty("定时触发")
    TIME_TRIGGER,

    // 服务触发
    @ApiModelProperty("服务触发")
    SERVICE,

    // 流水线触发
    @ApiModelProperty("流水线触发")
    PIPELINE,

    // 远程触发
    @ApiModelProperty("远程触发")
    REMOTE;

    companion object {
        fun toScmType(pipelineTriggerType: String): ScmType {
            return ScmType.valueOf(pipelineTriggerType)
        }

        fun toStartType(pipelineTriggerType: String): StartType {
            return StartType.toStartType(pipelineTriggerType)
        }

        fun defaultValueOf(
            value: String?,
            default: PipelineTriggerType
        ): PipelineTriggerType = if (value.isNullOrBlank()) {
            default
        } else {
            valueOf(value)
        }
    }
}