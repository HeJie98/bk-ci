package com.tencent.devops.common.auth.utils

import com.tencent.devops.common.auth.api.AuthPermission
import com.tencent.devops.common.auth.api.AuthResourceType

object RbacAuthUtils {

    fun buildAction(authPermission: AuthPermission, authResourceType: AuthResourceType): String {
        // TODO 待确定，rbac有list类型，不需要做转换
        val action = if (authPermission == AuthPermission.LIST) {
            AuthPermission.VIEW.value
        } else {
            authPermission.value
        }
        return "${extResourceType(authResourceType)}_$action"
    }

    fun extResourceType(authResourceType: AuthResourceType): String {
        return when (authResourceType) {
            AuthResourceType.QUALITY_GROUP -> "quality_group"
            AuthResourceType.QUALITY_RULE -> "rule"
            AuthResourceType.EXPERIENCE_TASK -> "experience_task"
            AuthResourceType.EXPERIENCE_GROUP -> "experience_group"
            else -> authResourceType.value
        }
    }

    fun extResourceTypeCheck(authResourceType: String): Boolean {
        val extResourceTypeList = mutableListOf<String>()
        extResourceTypeList.add(AuthResourceType.QUALITY_GROUP.value)
        extResourceTypeList.add(AuthResourceType.QUALITY_RULE.value)
        extResourceTypeList.add(AuthResourceType.EXPERIENCE_TASK.value)
        extResourceTypeList.add(AuthResourceType.EXPERIENCE_GROUP.value)
        // 前端experience的枚举值为EXPERIENCE_TASK: experience, EXPERIENCE_GROUP: experienceGroup
        extResourceTypeList.add("experience")
        extResourceTypeList.add("experienceGroup")
        if (extResourceTypeList.contains(authResourceType)) {
            return true
        }
        return false
    }

    fun buildActionList(authPermissions: Set<AuthPermission>, authResourceType: AuthResourceType): List<String> {
        val actions = mutableListOf<String>()
        authPermissions.forEach {
            actions.add(buildAction(it, authResourceType))
        }
        return actions
    }

    fun getAuthPermissionByAction(action: String): AuthPermission {
        val permissionStr = action.substringAfterLast("_")
        return AuthPermission.get(permissionStr)
    }

    fun getResourceTypeByStr(resourceTypeStr: String): AuthResourceType {
        return when (resourceTypeStr) {
            "quality_group" -> AuthResourceType.QUALITY_GROUP
            "rule" -> AuthResourceType.QUALITY_RULE
            "experience_task" -> AuthResourceType.EXPERIENCE_TASK
            "experience_group" -> AuthResourceType.EXPERIENCE_GROUP
            else -> AuthResourceType.get(resourceTypeStr)
        }
    }
}
