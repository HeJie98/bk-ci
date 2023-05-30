package com.tencent.devops.process.api.op

import com.tencent.devops.common.api.pojo.Result
import com.tencent.devops.common.web.RestResource
import com.tencent.devops.process.engine.service.PipelineRefRepositoryService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

@RestResource
class OpPipelineRefRepositoryResourceImpl @Autowired constructor(
    private val pipelineRefRepositoryResource: PipelineRefRepositoryService
) : OpPipelineRefRepositoryResource {

    override fun savePipelineRefRepositoryInfo(projectId: String?): Result<Boolean> {
        val threadPoolExecutor = ThreadPoolExecutor(
            1,
            1,
            0,
            TimeUnit.SECONDS,
            LinkedBlockingQueue(1),
            Executors.defaultThreadFactory(),
            ThreadPoolExecutor.AbortPolicy()
        )
        threadPoolExecutor.submit {
            logger.info("begin savePipelineRefRepositoryInfo threadPoolExecutor-----------")
            try {
                pipelineRefRepositoryResource.batchSavePipelineRefRepositoryInfo(
                    projectId = projectId
                )
                logger.info("end savePipelineRefRepositoryInfo threadPoolExecutor-----------")
            } catch (e: Exception) {
                logger.warn("savePipelineRefRepositoryInfo failed | $e ")
            } finally {
                threadPoolExecutor.shutdown()
            }
        }
        return Result(true)
    }

    companion object{
        private val logger = LoggerFactory.getLogger(OpPipelineRefRepositoryResourceImpl::class.java)
    }
}
