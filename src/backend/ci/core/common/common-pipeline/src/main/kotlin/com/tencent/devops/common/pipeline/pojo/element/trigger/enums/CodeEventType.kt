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

package com.tencent.devops.common.pipeline.pojo.element.trigger.enums

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty


@ApiModel("代码库事件类型")
enum class CodeEventType {
    // git event
    @ApiModelProperty("Git 推送事件")
    PUSH,
    @ApiModelProperty("Git 标签推送事件")
    TAG_PUSH,
    @ApiModelProperty("Git 合并请求事件")
    MERGE_REQUEST,
    @ApiModelProperty("Git MR已合并事件")
    MERGE_REQUEST_ACCEPT,
    @ApiModelProperty("Git 议题事件")
    ISSUES,
    @ApiModelProperty("Git 评论事件")
    NOTE,
    @ApiModelProperty("Git 评审事件")
    REVIEW,

    // github event
    @ApiModelProperty("GitHub 创建Branch/Tag事件")
    CREATE,
    @ApiModelProperty("GitHub PR事件")
    PULL_REQUEST,

    // svn event
    @ApiModelProperty("SVN POST_COMMIT事件")
    POST_COMMIT,
    @ApiModelProperty("SVN LOCK_COMMIT事件")
    LOCK_COMMIT,
    @ApiModelProperty("SVN PRE_COMMIT事件")
    PRE_COMMIT,

    // p4 event
    @ApiModelProperty("P4 CHANGE_COMMIT事件")
    CHANGE_COMMIT,
    @ApiModelProperty("P4 PUSH_SUBMIT事件")
    PUSH_SUBMIT,
    @ApiModelProperty("P4 CHANGE_CONTENT事件")
    CHANGE_CONTENT,
    @ApiModelProperty("P4 CHANGE_SUBMIT事件")
    CHANGE_SUBMIT,
    @ApiModelProperty("P4 PUSH_CONTENT事件")
    PUSH_CONTENT,
    @ApiModelProperty("P4 PUSH_CONTENT事件")
    PUSH_COMMIT,
    @ApiModelProperty("P4 FIX_ADD事件")
    FIX_ADD,
    @ApiModelProperty("P4 FIX_DELETE事件")
    FIX_DELETE,
    @ApiModelProperty("P4 FORM_COMMIT事件")
    FORM_COMMIT,
    @ApiModelProperty("P4 SHELVE_COMMIT事件")
    SHELVE_COMMIT,
    @ApiModelProperty("P4 SHELVE_DELETE事件")
    SHELVE_DELETE,
    @ApiModelProperty("P4 SHELVE_SUBMIT事件")
    SHELVE_SUBMIT;
}
