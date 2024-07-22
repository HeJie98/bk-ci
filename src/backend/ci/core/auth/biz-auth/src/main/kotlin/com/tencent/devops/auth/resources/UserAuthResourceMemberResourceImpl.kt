package com.tencent.devops.auth.resources

import com.tencent.devops.auth.api.user.UserAuthResourceMemberResource
import com.tencent.devops.auth.pojo.ResourceMemberInfo
import com.tencent.devops.auth.pojo.request.GroupMemberCommonConditionReq
import com.tencent.devops.auth.pojo.request.GroupMemberHandoverConditionReq
import com.tencent.devops.auth.pojo.request.GroupMemberRenewalConditionReq
import com.tencent.devops.auth.pojo.request.GroupMemberSingleRenewalReq
import com.tencent.devops.auth.pojo.request.RemoveMemberFromProjectReq
import com.tencent.devops.auth.pojo.vo.GroupDetailsInfoVo
import com.tencent.devops.auth.pojo.vo.MemberGroupCountWithPermissionsVo
import com.tencent.devops.auth.service.iam.PermissionResourceMemberService
import com.tencent.devops.common.api.model.SQLPage
import com.tencent.devops.common.api.pojo.Result
import com.tencent.devops.common.auth.api.BkManagerCheck
import com.tencent.devops.common.web.RestResource

@RestResource
class UserAuthResourceMemberResourceImpl(
    private val permissionResourceMemberService: PermissionResourceMemberService
) : UserAuthResourceMemberResource {
    @BkManagerCheck
    override fun listProjectMembers(
        userId: String,
        projectId: String,
        memberType: String?,
        userName: String?,
        deptName: String?,
        page: Int,
        pageSize: Int
    ): Result<SQLPage<ResourceMemberInfo>> {
        return Result(
            permissionResourceMemberService.listResourceMembers(
                projectCode = projectId,
                memberType = memberType,
                userName = userName,
                deptName = deptName,
                page = page,
                pageSize = pageSize
            )
        )
    }

    @BkManagerCheck
    override fun renewalGroupMember(
        userId: String,
        projectId: String,
        renewalConditionReq: GroupMemberSingleRenewalReq
    ): Result<GroupDetailsInfoVo> {
        return Result(
            permissionResourceMemberService.renewalGroupMember(
                userId = userId,
                projectCode = projectId,
                renewalConditionReq = renewalConditionReq
            )
        )
    }

    @BkManagerCheck
    override fun batchRenewalGroupMembers(
        userId: String,
        projectId: String,
        renewalConditionReq: GroupMemberRenewalConditionReq
    ): Result<Boolean> {
        return Result(
            permissionResourceMemberService.batchRenewalGroupMembers(
                userId = userId,
                projectCode = projectId,
                renewalConditionReq = renewalConditionReq
            )
        )
    }

    @BkManagerCheck
    override fun batchRemoveGroupMembers(
        userId: String,
        projectId: String,
        removeMemberDTO: GroupMemberCommonConditionReq
    ): Result<Boolean> {
        return Result(
            permissionResourceMemberService.batchDeleteResourceGroupMembers(
                userId = userId,
                projectCode = projectId,
                removeMemberDTO = removeMemberDTO
            )
        )
    }

    @BkManagerCheck
    override fun batchHandoverGroupMembers(
        userId: String,
        projectId: String,
        handoverMemberDTO: GroupMemberHandoverConditionReq
    ): Result<Boolean> {
        return Result(
            permissionResourceMemberService.batchHandoverGroupMembers(
                userId = userId,
                projectCode = projectId,
                handoverMemberDTO = handoverMemberDTO
            )
        )
    }

    @BkManagerCheck
    override fun removeMemberFromProject(
        userId: String,
        projectId: String,
        removeMemberFromProjectReq: RemoveMemberFromProjectReq
    ): Result<Boolean> {
        return Result(
            permissionResourceMemberService.removeMemberFromProject(
                userId = userId,
                projectCode = projectId,
                removeMemberFromProjectReq = removeMemberFromProjectReq
            )
        )
    }

    @BkManagerCheck
    override fun getMemberGroupCount(
        userId: String,
        projectId: String,
        memberId: String
    ): Result<List<MemberGroupCountWithPermissionsVo>> {
        return Result(
            permissionResourceMemberService.getMemberGroupsCount(
                projectCode = projectId,
                memberId = memberId
            )
        )
    }
}
