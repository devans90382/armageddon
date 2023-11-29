package com.devans.profile.config

import com.devans.profile.external.commons.CircuitBreakerConfiguration
import com.devans.profile.external.commons.ClientConfiguration
import com.devans.profile.external.commons.RetryConfig
import com.devans.profile.external.commons.initClient
import com.devans.profile.external.payroll.client.PayrollClient
import com.devans.profile.utils.ClientNames
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class PayrollConfig(
    private val profileConfig: ProfileConfig,
    private val meterRegistry: MeterRegistry,
) {

    @Bean(name = [ClientNames.PayrollClient])
    fun payrollClientConfig() = initClient(
        configuration = ClientConfiguration(
            url = profileConfig.integrations.payroll.apiEndpoint,
            connectTimeoutInMillis = profileConfig.integrations.payroll.connectTimeoutInMillis,
            readTimeoutInMillis = profileConfig.integrations.payroll.readTimeoutInMillis,
            followRedirects = profileConfig.integrations.payroll.followRedirects,
            retry = RetryConfig(
                startingInterval = profileConfig.integrations.payroll.retryConfig.feignStartingInterval,
                maxRetryPeriod = profileConfig.integrations.payroll.retryConfig.feignMaximumRetryPeriod,
                maxAttempts = profileConfig.integrations.payroll.retryConfig.feignMaximumRetriesAllowed
            ),
            circuitBreakerConfiguration = CircuitBreakerConfiguration(
                name = profileConfig.integrations.payroll.circuitBreakerConfiguration.name,
                failureRateThreshold = profileConfig.integrations.payroll.circuitBreakerConfiguration.failureRateThreshold,
                slowCallRateThreshold = profileConfig.integrations.payroll.circuitBreakerConfiguration.slowCallRateThreshold,
                waitDurationInOpenStateInMillis = profileConfig.integrations.payroll.circuitBreakerConfiguration.waitDurationInOpenStateInMillis,
                slowCallDurationThresholdInMillis = profileConfig.integrations.payroll.circuitBreakerConfiguration.slowCallDurationThresholdInMillis,
                permittedNumberOfCallsInHalfOpenState = profileConfig.integrations.payroll.circuitBreakerConfiguration.permittedNumberOfCallsInHalfOpenState,
                minimumNumberOfCalls = profileConfig.integrations.payroll.circuitBreakerConfiguration.minimumNumberOfCalls,
                slidingWindowType = profileConfig.integrations.payroll.circuitBreakerConfiguration.slidingWindowType,
                slidingWindowSize = profileConfig.integrations.payroll.circuitBreakerConfiguration.slidingWindowSize
            )
        ),
        meterRegistry = meterRegistry,
        feignClass = PayrollClient::class
    )
}