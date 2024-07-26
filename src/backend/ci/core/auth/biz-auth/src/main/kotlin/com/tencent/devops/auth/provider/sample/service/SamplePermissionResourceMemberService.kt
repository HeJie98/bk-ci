package com.tencent.devops.auth.provider.sample.service

import com.tencent.devops.auth.pojo.ResourceMemberInfo
import com.tencent.devops.auth.pojo.dto.GroupMemberRenewalDTO
import com.tencent.devops.auth.pojo.enum.BatchOperateType
import com.tencent.devops.auth.pojo.enum.JoinedType
import com.tencent.devops.auth.pojo.enum.RemoveMemberButtonControl
import com.tencent.devops.auth.pojo.request.GroupMemberCommonConditionReq
import com.tencent.devops.auth.pojo.request.GroupMemberHandoverConditionReq
import com.tencent.devops.auth.pojo.request.GroupMemberRenewalConditionReq
import com.tencent.devops.auth.pojo.request.GroupMemberSingleRenewalReq
import com.tencent.devops.auth.pojo.request.RemoveMemberFromProjectReq
import com.tencent.devops.auth.pojo.vo.BatchOperateGroupMemberCheckVo
import com.tencent.devops.auth.pojo.vo.GroupDetailsInfoVo
import com.tencent.devops.auth.pojo.vo.MemberGroupCountWithPermissionsVo
import com.tencent.devops.auth.pojo.vo.ResourceMemberCountVO
import com.tencent.devops.auth.service.iam.PermissionResourceMemberService
import com.tencent.devops.common.api.model.SQLPage
import com.tencent.devops.common.auth.api.pojo.BkAuthGroup
import com.tencent.devops.common.auth.api.pojo.BkAuthGroupAndUserList

class SamplePermissionResourceMemberService : PermissionResourceMemberService {
    override fun getResourceGroupMembers(
        projectCode: String,
        resourceType: String,
        resourceCode: String,
        group: BkAuthGroup?
    ): List<String> {
        return emptyList()
    }

    override fun getResourceGroupAndMembers(
        projectCode: String,
        resourceType: String,
        resourceCode: String
    ): List<BkAuthGroupAndUserList> {
        return emptyList()
    }

    override fun batchAddResourceGroupMembers(
        projectCode: String,
        iamGroupId: Int,
        expiredTime: Long,
        members: List<String>?,
        departments: List<String>?
    ) = true

    override fun batchDeleteResourceGroupMembers(
        projectCode: String,
        iamGroupId: Int,
        members: List<String>?,
        departments: List<String>?
    ): Boolean = true

    override fun roleCodeToIamGroupId(
        projectCode: String,
        roleCode: String
    ): Int = 0

    override fun autoRenewal(
        projectCode: String,
        resourceType: String,
        resourceCode: String
    ) = Unit

    override fun renewalGroupMember(
        userId: String,
        projectCode: String,
        resourceType: String,
        groupId: Int,
        memberRenewalDTO: GroupMemberRenewalDTO
    ): Boolean = true

    override fun renewalGroupMember(
        userId: String,
        projectCode: String,
        renewalConditionReq: GroupMemberSingleRenewalReq
    ): GroupDetailsInfoVo = GroupDetailsInfoVo(
        resourceCode = "resourceCode",
        resourceName = "resourceName",
        resourceType = "resourceType",
        groupId = 0,
        groupName = "",
        groupDesc = "",
        expiredAtDisplay = "",
        expiredAt = 0,
        joinedTime = 0,
        removeMemberButtonControl = RemoveMemberButtonControl.OTHER,
        joinedType = JoinedType.DIRECT,
        operator = ""
    )

    override fun batchRenewalGroupMembers(
        userId: String,
        projectCode: String,
        renewalConditionReq: GroupMemberRenewalConditionReq
    ): Boolean = true

    override fun batchDeleteResourceGroupMembers(
        userId: String,
        projectCode: String,
        removeMemberDTO: GroupMemberCommonConditionReq
    ): Boolean = true

    override fun batchHandoverGroupMembers(
        userId: String,
        projectCode: String,
        handoverMemberDTO: GroupMemberHandoverConditionReq
    ): Boolean = true

    override fun batchOperateGroupMembersCheck(
        userId: String,
        projectCode: String,
        batchOperateType: BatchOperateType,
        conditionReq: GroupMemberCommonConditionReq
    ): BatchOperateGroupMemberCheckVo = BatchOperateGroupMemberCheckVo(
        totalCount = 0,
        inoperableCount = 0
    )

    override fun removeMemberFromProject(
        userId: String,
        projectCode: String,
        removeMemberFromProjectReq: RemoveMemberFromProjectReq
    ): Boolean = true

    override fun addGroupMember(
        projectCode: String,
        memberId: String,
        memberType: String,
        expiredAt: Long,
        iamGroupId: Int
    ): Boolean = true

    override fun getProjectMemberCount(projectCode: String): ResourceMemberCountVO =
        ResourceMemberCountVO(
            userCount = 0,
            departmentCount = 0
        )

    override fun listProjectMembers(
        projectCode: String,
        memberType: String?,
        userName: String?,
        deptName: String?,
        page: Int,
        pageSize: Int
    ): SQLPage<ResourceMemberInfo> {
        return SQLPage(count = 0, records = emptyList())
    }

    override fun getMemberGroupsCount(
        projectCode: String,
        memberId: String
    ): List<MemberGroupCountWithPermissionsVo> {
        return emptyList()
    }

    override fun getMemberGroupsDetails(
        projectId: String,
        memberId: String,
        resourceType: String?,
        iamGroupIds: List<Int>?,
        start: Int?,
        limit: Int?
    ): SQLPage<GroupDetailsInfoVo> {
        return SQLPage(0, records = emptyList())
    }
}
