package com.devans.profile.external.commons

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import feign.Logger
import feign.Request
import feign.Retryer
import feign.jackson.JacksonDecoder
import feign.jackson.JacksonEncoder
import feign.micrometer.MicrometerCapability
import feign.okhttp.OkHttpClient
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry
import io.github.resilience4j.feign.FeignDecorators
import io.github.resilience4j.feign.Resilience4jFeign
import io.github.resilience4j.micrometer.tagged.TaggedCircuitBreakerMetrics
import io.micrometer.core.instrument.MeterRegistry
import java.time.Duration
import java.util.concurrent.TimeUnit
import kotlin.reflect.KClass

fun <T : Any> initClient(
    configuration: ClientConfiguration,
    meterRegistry: MeterRegistry,
    feignClass: KClass<T>,
): T {
    val mapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    val circuitBreakerHyperVerge = configuration.circuitBreakerConfiguration

    val circuitBreakerConfig = CircuitBreakerConfig.custom()
        .failureRateThreshold(circuitBreakerHyperVerge.failureRateThreshold)
        .slowCallRateThreshold(circuitBreakerHyperVerge.slowCallRateThreshold)
        .waitDurationInOpenState(Duration.ofMillis(circuitBreakerHyperVerge.waitDurationInOpenStateInMillis))
        .slowCallDurationThreshold(Duration.ofMillis(circuitBreakerHyperVerge.slowCallDurationThresholdInMillis))
        .permittedNumberOfCallsInHalfOpenState(circuitBreakerHyperVerge.permittedNumberOfCallsInHalfOpenState)
        .minimumNumberOfCalls(circuitBreakerHyperVerge.minimumNumberOfCalls)
        .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.TIME_BASED)
        .slidingWindowSize(circuitBreakerHyperVerge.slidingWindowSize)
        .build()

    val circuitBreakerRegistry = CircuitBreakerRegistry.of(circuitBreakerConfig)
    val circuitBreaker = circuitBreakerRegistry.circuitBreaker(circuitBreakerHyperVerge.name)

    TaggedCircuitBreakerMetrics.ofCircuitBreakerRegistry(circuitBreakerRegistry).bindTo(meterRegistry)
    val decorators = FeignDecorators
        .builder()
        .withCircuitBreaker(circuitBreaker)
        .build()

    return Resilience4jFeign.builder(decorators)
        .encoder(JacksonEncoder(mapper))
        .decoder(JacksonDecoder(mapper))
        .client(OkHttpClient())
        .addCapability(MicrometerCapability(meterRegistry))
        .logLevel(Logger.Level.FULL)
        .retryer(
            Retryer.Default(
                configuration.retry.startingInterval,
                configuration.retry.maxRetryPeriod,
                configuration.retry.maxAttempts
            )
        )
        .logger(Logger.ErrorLogger())
        .options(
            Request.Options(
                configuration.connectTimeoutInMillis, TimeUnit.MILLISECONDS,
                configuration.readTimeoutInMillis, TimeUnit.MILLISECONDS,
                configuration.followRedirects
            )
        )
        .target(feignClass.java, configuration.url)
}