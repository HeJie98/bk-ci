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

package com.tencent.devops.scm.pojo

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

/*
* {
        "id": 11323,
        "username": "git-user1",
        "web_url": "http://git.example.tencent.com/u/git-user1",
        "name": "git-user1",
        "state": "active",
        "avatar_url": "git.example.tencent.com/uploads/user/avatar/111323/a75ba2727c7a409cab1d15dd993149aa.jpg",
        "access_level": 30
}
*
* 组/项目的权限access_level包括：

GUEST = 10
FOLLOWER = 15
REPORTER = 20
DEVELOPER = 30
MASTER = 40
OWNER = 50
*
* */

@Schema(description = "git成员模型")
data class GitMember(
    @Schema(description = "成员id")
    val id: Int,
    @Schema(description = "用户名")
    val username: String,
    @Schema(description = "状态")
    val state: String,
    @Schema(description = "权限级别", name = "access_level")
    @JsonProperty("access_level")
    val accessLevel: Int
)
