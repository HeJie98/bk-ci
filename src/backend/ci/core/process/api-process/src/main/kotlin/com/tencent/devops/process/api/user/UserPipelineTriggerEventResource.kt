package com.tencent.devops.process.api.user

import com.tencent.devops.common.api.auth.AUTH_HEADER_USER_ID
import com.tencent.devops.common.api.auth.AUTH_HEADER_USER_ID_DEFAULT_VALUE
import com.tencent.devops.common.api.pojo.Page
import com.tencent.devops.common.api.pojo.Result
import com.tencent.devops.process.pojo.PipelineTriggerEvent
import com.tencent.devops.repository.pojo.RepositoryEventHistory
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.HeaderParam
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.MediaType

@Api(tags = ["USER_PIPELINE"], description = "用户-流水线触发事件")
@Path("/user/pipelines/triggerEvent")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Suppress("ALL")
interface UserPipelineTriggerEventResource {
    @ApiOperation("根据流水线ID查询触发信息")
    @GET
    @Path("/{projectId}/{pipelineId}/byPipeline")
    fun byPipeline(
        @ApiParam(value = "用户ID", required = true, defaultValue = AUTH_HEADER_USER_ID_DEFAULT_VALUE)
        @HeaderParam(AUTH_HEADER_USER_ID)
        userId: String,
        @ApiParam("项目ID", required = true)
        @PathParam("projectId")
        projectId: String,
        @ApiParam("流水线Id", required = true)
        @PathParam("pipelineId")
        pipelineId: String,
        @ApiParam("触发器类型", required = false)
        @QueryParam("triggerType")
        triggerType: String?,
        @ApiParam("事件类型", required = false)
        @QueryParam("eventType")
        eventType: String?,
        @ApiParam("触发人", required = false)
        @QueryParam("triggerUser")
        triggerUser: String?,
        @ApiParam("第几页", required = false, defaultValue = "1")
        @QueryParam("page")
        page: Int?,
        @ApiParam("每页多少条", required = false, defaultValue = "20")
        @QueryParam("pageSize")
        pageSize: Int?
    ): Result<Page<PipelineTriggerEvent>>

    @ApiOperation("根据代码库HashID查询触发信息")
    @GET
    @Path("/{projectId}/{repoHashId}/byRepoHashId")
    fun byRepoHashId(
        @ApiParam(value = "用户ID", required = true, defaultValue = AUTH_HEADER_USER_ID_DEFAULT_VALUE)
        @HeaderParam(AUTH_HEADER_USER_ID)
        userId: String,
        @ApiParam("项目ID", required = true)
        @PathParam("projectId")
        projectId: String,
        @ApiParam("代码库hashId", required = true)
        @PathParam("repoHashId")
        repoHashId: String,
        @ApiParam("触发器类型", required = false)
        @QueryParam("triggerType")
        triggerType: String?,
        @ApiParam("事件类型", required = false)
        @QueryParam("eventType")
        eventType: String?,
        @ApiParam("触发人", required = false)
        @QueryParam("triggerUser")
        triggerUser: String?,
        @ApiParam("流水线名称", required = false)
        @QueryParam("pipelineName")
        pipelineName: String?,
        @ApiParam("第几页", required = false, defaultValue = "1")
        @QueryParam("page")
        page: Int?,
        @ApiParam("每页多少条", required = false, defaultValue = "20")
        @QueryParam("pageSize")
        pageSize: Int?
    ): Result<Page<RepositoryEventHistory>>

    @ApiOperation("根据代码库HashID查询触发信息")
    @GET
    @Path("/{projectId}/{repoHashId}/repoTriggerDetail")
    fun getTriggerDetail(
        @ApiParam(value = "用户ID", required = true, defaultValue = AUTH_HEADER_USER_ID_DEFAULT_VALUE)
        @HeaderParam(AUTH_HEADER_USER_ID)
        userId: String,
        @ApiParam("项目ID", required = true)
        @PathParam("projectId")
        projectId: String,
        @ApiParam("代码库hashId", required = true)
        @PathParam("repoHashId")
        repoHashId: String,
        @ApiParam("事件ID", required = true)
        @QueryParam("eventId")
        eventId: String,
        @ApiParam("触发器类型", required = false)
        @QueryParam("triggerType")
        triggerType: String?,
        @ApiParam("事件类型", required = false)
        @QueryParam("eventType")
        eventType: String?,
        @ApiParam("触发人", required = false)
        @QueryParam("triggerUser")
        triggerUser: String?,
        @ApiParam("流水线名称", required = false)
        @QueryParam("pipelineName")
        pipelineName: String?,
        @ApiParam("第几页", required = false, defaultValue = "1")
        @QueryParam("page")
        page: Int?,
        @ApiParam("每页多少条", required = false, defaultValue = "20")
        @QueryParam("pageSize")
        pageSize: Int?
    ): Result<Page<PipelineTriggerEvent>>
}