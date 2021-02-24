package io.tricefal.core

import org.slf4j.LoggerFactory
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.task.AsyncTaskExecutor
import org.springframework.scheduling.annotation.AsyncConfigurer
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.context.request.async.CallableProcessingInterceptor
import org.springframework.web.context.request.async.TimeoutCallableProcessingInterceptor
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer
import java.util.concurrent.Callable


@Configuration
@EnableAsync
@EnableScheduling
class AsyncConfiguration : AsyncConfigurer {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Bean(name = ["taskExecutor"])
    override fun getAsyncExecutor(): AsyncTaskExecutor {
        logger.debug("Creating Async Task Executor")
        val executor = ThreadPoolTaskExecutor()
        executor.corePoolSize = 5
        executor.maxPoolSize = 10
        executor.setQueueCapacity(25)
        return executor
    }

    override fun getAsyncUncaughtExceptionHandler(): AsyncUncaughtExceptionHandler {
        return SimpleAsyncUncaughtExceptionHandler()
    }

    /** Configure async support for Spring MVC.  */
    @Bean
    fun webMvcConfigurerConfigurer(taskExecutor: AsyncTaskExecutor?, callableProcessingInterceptor: CallableProcessingInterceptor?): WebMvcConfigurer {
        return object : WebMvcConfigurer() {
            override fun configureAsyncSupport(configurer: AsyncSupportConfigurer) {
                configurer.setDefaultTimeout(360000).setTaskExecutor(taskExecutor!!)
                configurer.registerCallableInterceptors(callableProcessingInterceptor)
                super.configureAsyncSupport(configurer)
            }
        }
    }

    @Bean
    fun callableProcessingInterceptor(): CallableProcessingInterceptor {
        return object : TimeoutCallableProcessingInterceptor() {
            override fun <T> handleTimeout(request: NativeWebRequest?, task: Callable<T>): Any? {
                logger.error("timeout!")
                return super.handleTimeout<T>(request, task)
            }
        }
    }
}