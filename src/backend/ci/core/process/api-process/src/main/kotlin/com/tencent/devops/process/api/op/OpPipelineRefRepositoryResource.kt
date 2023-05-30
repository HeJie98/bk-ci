package com.tencent.devops.process.api.op

import com.tencent.devops.common.api.pojo.Result
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import javax.ws.rs.Consumes
import javax.ws.rs.PUT
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.MediaType

@Api(tags = ["OP_PIPELINE_REF_REPOSITORY"], description = "OP-流水线依赖代码库")
@Path("/op/refRepository")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
interface OpPipelineRefRepositoryResource {

    @ApiOperation("保存流水线依赖代码库关系")
    @PUT
    @Path("/savePipelineRefRepositoryInfo")
    fun savePipelineRefRepositoryInfo(
        @ApiParam("蓝盾项目ID", required = false)
        @QueryParam("projectId")
        projectId: String?
    ): Result<Boolean>
}
