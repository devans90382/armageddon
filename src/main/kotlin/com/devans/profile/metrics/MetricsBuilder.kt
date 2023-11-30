package com.devans.profile.metrics

import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.stereotype.Component

@Component
class MetricsBuilder(
    private val meterRegistry: MeterRegistry
) {

    private val prefix = "profile"

    fun buildCounter(
        name: String,
        description: String? = null
    ) = Counter
        .builder("$prefix.$name.count")
        .description(description)
        .register(meterRegistry)
        .increment()

    fun buildExceptionCounter(
        operation: String
    ) = Counter
        .builder("$prefix.exception.count")
        .description("Number of exceptions for $operation")
        .tag("operation", operation)
        .register(meterRegistry)
        .increment()
}
