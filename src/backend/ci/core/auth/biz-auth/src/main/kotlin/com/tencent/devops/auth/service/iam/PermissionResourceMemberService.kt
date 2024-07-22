package com.tencent.devops.auth.service.iam

import com.tencent.devops.auth.pojo.ResourceMemberInfo
import com.tencent.devops.auth.pojo.dto.GroupMemberRenewalDTO
import com.tencent.devops.auth.pojo.request.GroupMemberCommonConditionReq
import com.tencent.devops.auth.pojo.request.GroupMemberHandoverConditionReq
import com.tencent.devops.auth.pojo.request.GroupMemberRenewalConditionReq
import com.tencent.devops.auth.pojo.request.GroupMemberSingleRenewalReq
import com.tencent.devops.auth.pojo.request.RemoveMemberFromProjectReq
import com.tencent.devops.auth.pojo.vo.GroupDetailsInfoVo
import com.tencent.devops.auth.pojo.vo.MemberGroupCountWithPermissionsVo
import com.tencent.devops.auth.pojo.vo.ResourceMemberCountVO
import com.tencent.devops.common.api.model.SQLPage
import com.tencent.devops.common.auth.api.pojo.BkAuthGroup
import com.tencent.devops.common.auth.api.pojo.BkAuthGroupAndUserList

@Suppress("LongParameterList", "TooManyFunctions")
interface PermissionResourceMemberService {
    fun getResourceGroupMembers(
        projectCode: String,
        resourceType: String,
        resourceCode: String,
        group: BkAuthGroup?
    ): List<String>

    fun getResourceGroupAndMembers(
        projectCode: String,
        resourceType: String,
        resourceCode: String
    ): List<BkAuthGroupAndUserList>

    /**
     * 获取资源下全部成员数量
     * 如获取流水线A下所有成员数量，会把拥有者/执行者/编辑者/查看者总数量都返回
     * */
    fun getResourceMemberCount(
        projectCode: String,
        resourceType: String,
        resourceCode: String
    ): ResourceMemberCountVO

    /**
     * 获取资源下全部成员
     * 如获取流水线A下所有成员，会把拥有者/执行者/编辑者/查看者成员都返回
     * */
    fun listResourceMembers(
        projectCode: String,
        memberType: String?,
        userName: String?,
        deptName: String?,
        page: Int,
        pageSize: Int
    ): SQLPage<ResourceMemberInfo>

    /**
     * 获取用户有权限的用户组
     * */
    fun getMemberGroups(
        projectCode: String,
        resourceType: String,
        member: String,
        start: Int,
        end: Int
    ): List<Int>

    /**
     * 获取用户有权限的用户组数量
     * */
    fun getMemberGroupsCount(
        projectCode: String,
        memberId: String
    ): List<MemberGroupCountWithPermissionsVo>

    fun batchDeleteResourceGroupMembers(
        projectCode: String,
        iamGroupId: Int,
        members: List<String>? = emptyList(),
        departments: List<String>? = emptyList()
    ): Boolean

    fun batchDeleteResourceGroupMembers(
        userId: String,
        projectCode: String,
        removeMemberDTO: GroupMemberCommonConditionReq
    ): Boolean

    fun batchHandoverGroupMembers(
        userId: String,
        projectCode: String,
        handoverMemberDTO: GroupMemberHandoverConditionReq
    ): Boolean

    fun removeMemberFromProject(
        userId: String,
        projectCode: String,
        removeMemberFromProjectReq: RemoveMemberFromProjectReq
    ): Boolean

    fun roleCodeToIamGroupId(
        projectCode: String,
        roleCode: String
    ): Int

    fun autoRenewal(
        projectCode: String,
        resourceType: String,
        resourceCode: String
    )

    // 需审批版本
    fun renewalGroupMember(
        userId: String,
        projectCode: String,
        resourceType: String,
        groupId: Int,
        memberRenewalDTO: GroupMemberRenewalDTO
    ): Boolean

    // 无需审批版本
    fun renewalGroupMember(
        userId: String,
        projectCode: String,
        renewalConditionReq: GroupMemberSingleRenewalReq
    ): GroupDetailsInfoVo

    fun batchRenewalGroupMembers(
        userId: String,
        projectCode: String,
        renewalConditionReq: GroupMemberRenewalConditionReq
    ): Boolean

    fun addGroupMember(
        projectCode: String,
        memberId: String,
        /*user or department or template*/
        memberType: String,
        expiredAt: Long,
        iamGroupId: Int
    ): Boolean

    @Suppress("LongParameterList")
    fun batchAddResourceGroupMembers(
        projectCode: String,
        iamGroupId: Int,
        expiredTime: Long,
        members: List<String>? = emptyList(),
        departments: List<String>? = emptyList()
    ): Boolean
}
