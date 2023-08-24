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

package com.tencent.devops.auth.service.migrate

import com.tencent.bk.sdk.iam.service.v2.V2ManagerService
import com.tencent.devops.auth.dao.AuthResourceGroupDao
import com.tencent.devops.auth.service.AuthResourceService
import com.tencent.devops.auth.service.iam.PermissionResourceGroupService
import com.tencent.devops.common.auth.api.AuthResourceType
import com.tencent.devops.common.auth.api.pojo.DefaultGroupType
import com.tencent.devops.common.auth.api.pojo.PermissionHandoverDTO
import org.apache.commons.lang3.RandomUtils
import org.jboss.logging.Logger
import org.jooq.DSLContext

class MigratePermissionHandoverService constructor(
    private val v2ManagerService: V2ManagerService,
    private val groupService: PermissionResourceGroupService,
    private val authResourceGroupDao: AuthResourceGroupDao,
    private val authResourceService: AuthResourceService,
    private val dslContext: DSLContext
) {
    fun handoverPermissions(permissionHandoverDTO: PermissionHandoverDTO) {
        val handoverFrom = permissionHandoverDTO.handoverFrom
        val handoverToList = permissionHandoverDTO.handoverToList
        val resourceType = permissionHandoverDTO.resourceType
        permissionHandoverDTO.projectList.forEach { projectCode ->
            if (permissionHandoverDTO.managerPermission) {
                val projectManagerGroupId = authResourceGroupDao.get(
                    dslContext = dslContext,
                    projectCode = projectCode,
                    resourceType = AuthResourceType.PROJECT.value,
                    resourceCode = projectCode,
                    groupCode = DefaultGroupType.MANAGER.value
                )
                handoverToList.forEach { handoverTo ->
                    groupService.addGroupMember(
                        userId = handoverTo,
                        memberType = USER_TYPE,
                        expiredDay = GROUP_EXPIRED_DAY[RandomUtils.nextInt(2, 4)],
                        groupId = projectManagerGroupId!!.relationId.toInt()
                    )
                }
            }
            var offset = 0
            val limit = 100
            do {
                val resourceList = authResourceService.listByCreator(
                    resourceType = permissionHandoverDTO.resourceType,
                    projectCode = projectCode,
                    creator = handoverFrom,
                    offset = offset,
                    limit = limit
                )
                resourceList.forEach { resource ->
                    val resourceCode = resource.resourceCode
                    val handoverTo = handoverToList.random()
                    logger.info("handover resource permissions :$projectCode|$resourceCode|$handoverFrom|$handoverTo")
                    authResourceService.updateCreator(
                        projectCode = projectCode,
                        resourceType = resourceType,
                        resourceCode = resourceCode,
                        creator = handoverTo
                    )
                    val resourceManagerGroup = authResourceGroupDao.get(
                        dslContext = dslContext,
                        projectCode = projectCode,
                        resourceType = resourceType,
                        resourceCode = resourceCode,
                        groupCode = DefaultGroupType.MANAGER.value
                    )
                    groupService.addGroupMember(
                        userId = handoverTo,
                        memberType = USER_TYPE,
                        expiredDay = GROUP_EXPIRED_DAY[RandomUtils.nextInt(2, 4)],
                        groupId = resourceManagerGroup!!.relationId.toInt()
                    )
                    v2ManagerService.deleteRoleGroupMemberV2(
                        resourceManagerGroup.relationId.toInt(),
                        USER_TYPE,
                        handoverFrom
                    )
                }
                offset += limit
            } while (resourceList.size == limit)
        }
    }

    companion object {
        // v0默认用户组过期时间,2年或者3年
        private val GROUP_EXPIRED_DAY = listOf(180L, 360L, 720L, 1080L)
        private const val USER_TYPE = "user"
        private val logger = Logger.getLogger(MigratePermissionHandoverService::class.java)
    }
}
