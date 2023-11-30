package com.devans.profile.config

import com.sun.net.httpserver.HttpServer
import io.micrometer.core.instrument.Meter
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics
import io.micrometer.core.instrument.binder.system.DiskSpaceMetrics
import io.micrometer.core.instrument.binder.system.ProcessorMetrics
import io.micrometer.core.instrument.binder.system.UptimeMetrics
import io.micrometer.core.instrument.config.MeterFilter
import io.micrometer.core.instrument.distribution.DistributionStatisticConfig
import io.micrometer.prometheus.PrometheusConfig
import io.micrometer.prometheus.PrometheusMeterRegistry
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.File
import java.net.InetSocketAddress

@Configuration
class MeterRegistryConfig(
    private val profileConfig: ProfileConfig
) {
    @Bean
    fun initializeMeterRegistry(): MeterRegistry {
        val registry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT)
        registry.config().commonTags("service", profileConfig.observabilityConfig.name)

        ClassLoaderMetrics().bindTo(registry)
        JvmMemoryMetrics().bindTo(registry)
        JvmGcMetrics().bindTo(registry)
        JvmThreadMetrics().bindTo(registry)
        UptimeMetrics().bindTo(registry)
        ProcessorMetrics().bindTo(registry)
        DiskSpaceMetrics(File(System.getProperty("user.dir"))).bindTo(registry)

        registry.config().meterFilter(object : MeterFilter {
            override fun configure(id: Meter.Id, config: DistributionStatisticConfig): DistributionStatisticConfig {
                return DistributionStatisticConfig.builder().percentiles(0.99, 0.95).build().merge(config)
            }
        })

        val server = HttpServer.create(InetSocketAddress(profileConfig.observabilityConfig.scrapperPort), 0)
        server.createContext(
            profileConfig.observabilityConfig.scrapperPath
        ) {
            val response = registry.scrape()
            it.sendResponseHeaders(200, response.toByteArray().size.toLong())
            val o = it.responseBody
            o.write(response.toByteArray())
            o.close()
        }

        server.createContext(
            "/profile/health"
        ) {
            it.sendResponseHeaders(200, 0)
            it.responseBody.close()
        }

        Thread {
            server.start()
        }.start()

        return registry
    }
}
