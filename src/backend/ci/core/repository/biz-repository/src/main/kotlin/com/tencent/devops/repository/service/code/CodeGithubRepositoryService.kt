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
package com.tencent.devops.repository.service.code

import com.tencent.devops.common.api.constant.RepositoryMessageCode
import com.tencent.devops.common.api.enums.ScmType
import com.tencent.devops.common.api.exception.OperationException
import com.tencent.devops.common.api.util.HashUtil
import com.tencent.devops.common.service.utils.MessageCodeUtil
import com.tencent.devops.model.repository.tables.records.TRepositoryRecord
import com.tencent.devops.repository.dao.RepositoryDao
import com.tencent.devops.repository.dao.RepositoryGithubDao
import com.tencent.devops.repository.dao.RepositorySettingsDao
import com.tencent.devops.repository.pojo.GithubRepository
import com.tencent.devops.repository.pojo.auth.RepoAuthInfo
import com.tencent.devops.repository.pojo.enums.RepoAuthType
import org.apache.commons.collections4.CollectionUtils
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class CodeGithubRepositoryService @Autowired constructor(
    private val repositoryDao: RepositoryDao,
    private val repositoryGithubDao: RepositoryGithubDao,
    private val dslContext: DSLContext,
    private val repositorySettingsDao: RepositorySettingsDao
) : CodeRepositoryService<GithubRepository> {
    override fun repositoryType(): String {
        return GithubRepository::class.java.name
    }

    override fun create(projectId: String, userId: String, repository: GithubRepository): Long {
        // Github无需检查凭证信息
        var repositoryId: Long = 0L
        dslContext.transaction { configuration ->
            val transactionContext = DSL.using(configuration)
            repositoryId = repositoryDao.create(
                dslContext = transactionContext,
                projectId = projectId,
                userId = userId,
                aliasName = repository.aliasName,
                url = repository.getFormatURL(),
                type = ScmType.GITHUB
            )
            repositoryGithubDao.create(dslContext, repositoryId, repository.projectName, userId)
            checkPacSetting(
                dslContext = transactionContext,
                repository = repository,
                repositoryId = repositoryId,
                isEdit = false
            )
        }
        return repositoryId
    }

    override fun edit(
        userId: String,
        projectId: String,
        repositoryHashId: String,
        repository: GithubRepository,
        record: TRepositoryRecord
    ) {
        // 提交的参数与数据库中类型不匹配
        if (record.type != ScmType.GITHUB.name) {
            throw OperationException(MessageCodeUtil.getCodeLanMessage(RepositoryMessageCode.GITHUB_INVALID))
        }
        val repositoryId = HashUtil.decodeOtherIdToLong(repositoryHashId)
        dslContext.transaction { configuration ->
            val transactionContext = DSL.using(configuration)
            repositoryDao.edit(
                dslContext = transactionContext,
                repositoryId = repositoryId,
                aliasName = repository.aliasName,
                url = repository.getFormatURL()
            )
            repositoryGithubDao.edit(transactionContext, repositoryId, repository.projectName, repository.userName)
            checkPacSetting(
                dslContext = transactionContext,
                repository = repository,
                repositoryId = repositoryId,
                isEdit = true
            )
        }
    }

    override fun compose(repository: TRepositoryRecord): GithubRepository {
        val record = repositoryGithubDao.get(dslContext, repository.repositoryId)
        return GithubRepository(
            aliasName = repository.aliasName,
            url = repository.url,
            userName = repository.userId,
            projectName = record.projectName,
            projectId = repository.projectId,
            repoHashId = HashUtil.encodeOtherLongId(repository.repositoryId)
        )
    }

    override fun getAuthInfo(repositoryIds: List<Long>): Map<Long, RepoAuthInfo> {
        return repositoryGithubDao.list(
            dslContext = dslContext,
            repositoryIds = repositoryIds.toSet()
        )?.associateBy({ it -> it.repositoryId }, {
            RepoAuthInfo(authType = RepoAuthType.OAUTH.name, credentialId = it.userName)
        }) ?: mapOf()
    }

    /**
     * 检查Pac设置
     */
    fun checkPacSetting(dslContext: DSLContext, repository: GithubRepository, repositoryId: Long, isEdit: Boolean) {
        // 匹配代码库设置
        var repositoryIds = repositoryGithubDao.getRepositoryByProjectName(
            dslContext = dslContext,
            projectName = repository.projectName
        ).map { it.repositoryId }
        // 过滤已删除
        repositoryIds = repositoryDao.getRepoByIds(
            dslContext = dslContext,
            repositoryIds = repositoryIds,
            checkDelete = true
        )?.map { it.repositoryId } ?: ArrayList()
        val enablePacRepoIds = repositorySettingsDao.getEnablePacSettingsByRepositoryIds(
            dslContext = dslContext,
            repositoryIds = repositoryIds
        )
        // 若代码库已开启PAC，则不允许开启
        if (isEdit) {
            // 修改
            if (CollectionUtils.size(enablePacRepoIds) > 0 && repositoryId != enablePacRepoIds[0]) {
                throw OperationException(
                    MessageCodeUtil.generateResponseDataObject<String>(
                        RepositoryMessageCode.REPO_ENABLED_PAC,
                        arrayOf(repository.projectName)
                    ).message!!
                )
            }
        } else {
            // 新增
            if (CollectionUtils.size(enablePacRepoIds) > 0 && repository.enablePac == true) {
                throw OperationException(
                    MessageCodeUtil.generateResponseDataObject<String>(
                        RepositoryMessageCode.REPO_ENABLED_PAC,
                        arrayOf(repository.projectName)
                    ).message!!
                )
            }
        }
    }
}
