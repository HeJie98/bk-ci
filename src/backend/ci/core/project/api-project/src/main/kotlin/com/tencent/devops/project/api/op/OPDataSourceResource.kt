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

package com.tencent.devops.project.api.op

import com.tencent.devops.common.api.auth.AUTH_HEADER_DEVOPS_USER_ID
import com.tencent.devops.common.api.auth.AUTH_HEADER_DEVOPS_USER_ID_DEFAULT_VALUE
import com.tencent.devops.common.api.enums.SystemModuleEnum
import com.tencent.devops.common.api.pojo.Result
import com.tencent.devops.common.api.pojo.ShardingRuleTypeEnum
import com.tencent.devops.common.web.annotation.BkField
import com.tencent.devops.common.web.constant.BkStyleEnum
import com.tencent.devops.project.pojo.DataBasePiecewiseInfo
import com.tencent.devops.project.pojo.DataSource
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import javax.validation.Valid
import javax.ws.rs.Consumes
import javax.ws.rs.DELETE
import javax.ws.rs.GET
import javax.ws.rs.HeaderParam
import javax.ws.rs.POST
import javax.ws.rs.PUT
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.MediaType

@Api(tags = ["OP_DATA_SOURCE"], description = "OP-数据源")
@Path("/op/dataSources")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
interface OPDataSourceResource {

    @ApiOperation("添加数据源")
    @POST
    @Path("/add")
    fun addDataSource(
        @ApiParam("用户ID", required = true, defaultValue = AUTH_HEADER_DEVOPS_USER_ID_DEFAULT_VALUE)
        @HeaderParam(AUTH_HEADER_DEVOPS_USER_ID)
        @BkField(minLength = 1, maxLength = 50)
        userId: String,
        @ApiParam(value = "数据源信息请求报文体", required = true)
        @Valid
        dataSource: DataSource
    ): Result<Boolean>

    @ApiOperation("更新数据源信息")
    @PUT
    @Path("/ids/{id}/update")
    fun updateDataSource(
        @ApiParam("用户ID", required = true, defaultValue = AUTH_HEADER_DEVOPS_USER_ID_DEFAULT_VALUE)
        @HeaderParam(AUTH_HEADER_DEVOPS_USER_ID)
        @BkField(minLength = 1, maxLength = 50)
        userId: String,
        @ApiParam("数据源ID", required = true)
        @PathParam("id")
        @BkField(patternStyle = BkStyleEnum.ID_STYLE)
        id: String,
        @ApiParam(value = "数据源信息请求报文体", required = true)
        @Valid
        dataSource: DataSource
    ): Result<Boolean>

    @ApiOperation("根据ID获取数据源信息")
    @GET
    @Path("/ids/{id}/get")
    fun getDataSourceById(
        @ApiParam("数据源ID", required = true)
        @PathParam("id")
        @BkField(patternStyle = BkStyleEnum.ID_STYLE)
        id: String
    ): Result<DataSource?>

    @ApiOperation("根据ID删除数据源信息")
    @DELETE
    @Path("/ids/{id}/delete")
    fun deleteDataSourceById(
        @ApiParam("用户ID", required = true, defaultValue = AUTH_HEADER_DEVOPS_USER_ID_DEFAULT_VALUE)
        @HeaderParam(AUTH_HEADER_DEVOPS_USER_ID)
        @BkField(minLength = 1, maxLength = 50)
        userId: String,
        @ApiParam("数据源ID", required = true)
        @PathParam("id")
        @BkField(patternStyle = BkStyleEnum.ID_STYLE)
        id: String
    ): Result<Boolean>

    @ApiOperation("根据PROJECT_ID获取数据库分片信息")
    @GET
    @Path("/piecewise/get")
    fun getDataBasePiecewiseById(
        @ApiParam("项目ID", required = true)
        @QueryParam("projectId")
        projectId: String,
        @ApiParam("微服务code", required = true)
        @QueryParam("moduleCode")
        moduleCode: SystemModuleEnum,
        @ApiParam("集群名称", required = true)
        @QueryParam("clusterName")
        clusterName: String,
        @ApiParam("规则类型", required = true)
        @QueryParam("ruleType")
        ruleType: ShardingRuleTypeEnum = ShardingRuleTypeEnum.DB
    ): Result<DataBasePiecewiseInfo?>
}
