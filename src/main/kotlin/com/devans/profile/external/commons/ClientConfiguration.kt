package com.devans.profile.external.commons

data class ClientConfiguration(
    val url: String,
    val connectTimeoutInMillis: Long,
    val readTimeoutInMillis: Long,
    val followRedirects: Boolean,
    val retry: RetryConfig,
    val circuitBreakerConfiguration: CircuitBreakerConfiguration,
)

data class CircuitBreakerConfiguration(
    val name: String,
    val failureRateThreshold: Float,
    val slowCallRateThreshold: Float,
    val waitDurationInOpenStateInMillis: Long,
    val slowCallDurationThresholdInMillis: Long,
    val permittedNumberOfCallsInHalfOpenState: Int,
    val minimumNumberOfCalls: Int,
    val slidingWindowType: String,
    val slidingWindowSize: Int,
)

data class RetryConfig(
    val startingInterval: Long,
    val maxRetryPeriod: Long,
    val maxAttempts: Int
)

