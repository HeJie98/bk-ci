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

package com.tencent.devops.repository.api

import com.tencent.devops.common.api.enums.ScmType
import com.tencent.devops.common.api.pojo.Result
import com.tencent.devops.repository.pojo.RepoPacSyncFileInfo
import com.tencent.devops.repository.pojo.Repository
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.MediaType

@Api(tags = ["SERVICE_PAC_REPOSITORY"], description = "服务-PAC-代码库")
@Path("/service/repositories/pac")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
interface ServiceRepositoryPacResource {

    @ApiOperation("保存pac同步详情")
    @POST
    @Path("/{projectId}/{repositoryHashId}/initPacSyncDetail")
    fun initPacSyncDetail(
        @ApiParam("项目ID", required = true)
        @PathParam("projectId")
        projectId: String,
        @ApiParam("代码库哈希ID", required = true)
        @PathParam("repositoryHashId")
        repositoryHashId: String,
        @ApiParam("ciDirId", required = true)
        @QueryParam("ciDirId")
        ciDirId: String?,
        @ApiParam("文件同步详情", required = true)
        syncFileInfoList: List<RepoPacSyncFileInfo>
    ): Result<Boolean>

    @ApiOperation("更新pac同步状态")
    @POST
    @Path("/{projectId}/{repositoryHashId}/updatePacSyncStatus")
    fun updatePacSyncStatus(
        @ApiParam("项目ID", required = true)
        @PathParam("projectId")
        projectId: String,
        @ApiParam("代码库哈希ID", required = true)
        @PathParam("repositoryHashId")
        repositoryHashId: String,
        @ApiParam("ciDirId", required = true)
        @QueryParam("ciDirId")
        ciDirId: String,
        @ApiParam("文件同步详情", required = true)
        syncFileInfo: RepoPacSyncFileInfo
    ): Result<Boolean>

    @ApiOperation("根据第三方代码库平台ID获取代码库")
    @GET
    @Path("/")
    fun getPacRepository(
        @ApiParam("第三方仓库ID", required = true)
        @QueryParam("externalId")
        externalId: String,
        @ApiParam("仓库类型", required = true)
        @QueryParam("scmType")
        scmType: ScmType
    ): Result<Repository?>
}