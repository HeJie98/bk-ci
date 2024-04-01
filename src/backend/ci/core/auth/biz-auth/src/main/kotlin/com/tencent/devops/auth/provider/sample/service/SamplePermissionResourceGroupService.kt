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

package com.tencent.devops.auth.provider.sample.service

import com.tencent.devops.auth.pojo.dto.GroupMemberRenewalDTO
import com.tencent.devops.auth.pojo.dto.RenameGroupDTO
import com.tencent.devops.auth.pojo.vo.IamGroupInfoVo
import com.tencent.devops.auth.pojo.vo.IamGroupMemberInfoVo
import com.tencent.devops.auth.pojo.vo.IamGroupPoliciesVo
import com.tencent.devops.auth.service.iam.PermissionResourceGroupService
import com.tencent.devops.common.api.pojo.Pagination

class SamplePermissionResourceGroupService : PermissionResourceGroupService {

    override fun listGroup(
        projectId: String,
        resourceType: String,
        resourceCode: String,
        page: Int,
        pageSize: Int
    ): Pagination<IamGroupInfoVo> {
        return Pagination(false, emptyList())
    }

    override fun listUserBelongGroup(
        userId: String,
        projectId: String,
        resourceType: String,
        resourceCode: String
    ): List<IamGroupMemberInfoVo> {
        return emptyList()
    }

    override fun getGroupPolicies(
        userId: String,
        projectId: String,
        resourceType: String,
        groupId: Int
    ): List<IamGroupPoliciesVo> {
        return emptyList()
    }

    override fun renewal(
        userId: String,
        projectId: String,
        resourceType: String,
        groupId: Int,
        memberRenewalDTO: GroupMemberRenewalDTO
    ): Boolean {
        return true
    }

    override fun deleteGroupMember(
        userId: String,
        projectId: String,
        resourceType: String,
        groupId: Int
    ): Boolean {
        return true
    }

    override fun deleteGroup(
        userId: String,
        projectId: String,
        resourceType: String,
        groupId: Int
    ): Boolean {
        return true
    }

    override fun createGroupByGroupCode(
        userId: String,
        projectId: String,
        resourceType: String,
        groupCode: String
    ): Boolean {
        return true
    }

    override fun rename(
        userId: String,
        projectId: String,
        resourceType: String,
        groupId: Int,
        renameGroupDTO: RenameGroupDTO
    ): Boolean {
        return true
    }

    override fun addGroupMember(
        userId: String,
        /*user 或 department*/
        memberType: String,
        expiredAt: Long,
        groupId: Int
    ): Boolean {
        return true
    }
}