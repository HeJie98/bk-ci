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

package com.tencent.devops.repository.dao

import com.tencent.devops.model.repository.tables.TRepositoryPipelineTask
import com.tencent.devops.model.repository.tables.records.TRepositoryCodeGitRecord
import com.tencent.devops.model.repository.tables.records.TRepositoryPipelineTaskRecord
import org.jooq.DSLContext
import org.jooq.Result
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class RepositoryPipelineTaskDao {
    fun delete(dslContext: DSLContext, pipelineId: String): Int {
        with(TRepositoryPipelineTask.T_REPOSITORY_PIPELINE_TASK) {
            return dslContext.deleteFrom(this)
                .where(PIPELINE_ID.eq(pipelineId))
                .execute()
        }
    }

    fun save(
        dslContext: DSLContext,
        projectId: String,
        pipelineId: String,
        pipelineName: String,
        repositoryHashId: String,
        taskId: String,
        atomCode: String
    ){
        val now = LocalDateTime.now()
        with(TRepositoryPipelineTask.T_REPOSITORY_PIPELINE_TASK) {
            dslContext.insertInto(
                this,
                PROJECT_ID,
                PIPELINE_ID,
                PIPELINE_NAME,
                REPOSITORY_HASH_ID,
                TASK_ID,
                ATOM_CODE,
                CREATE_TIME,
                UPDATE_TIME
            )
                .values(
                    projectId,
                    pipelineId,
                    pipelineName,
                    repositoryHashId,
                    taskId,
                    atomCode,
                    now,
                    now
                ).execute()
        }
    }

    fun list(
        dslContext: DSLContext,
        projectId: String,
        repositoryHashId: String,
        limit: Int,
        offset: Int
    ): Result<TRepositoryPipelineTaskRecord> {
        with(TRepositoryPipelineTask.T_REPOSITORY_PIPELINE_TASK) {
            val conditions = listOf(
                PROJECT_ID.eq(projectId),
                REPOSITORY_HASH_ID.eq(repositoryHashId)
            )
            return dslContext.selectFrom(this)
                .where(conditions).orderBy(PIPELINE_ID)
                .limit(limit).offset(offset)
                .fetch()
        }
    }
}
