package com.devans.profile.config

import com.devans.profile.external.accounting.client.AccountingClient
import com.devans.profile.external.commons.CircuitBreakerConfiguration
import com.devans.profile.external.commons.ClientConfiguration
import com.devans.profile.external.commons.RetryConfig
import com.devans.profile.external.commons.initClient
import com.devans.profile.utils.ClientNames
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AccountingConfig(
    private val profileConfig: ProfileConfig,
    private val meterRegistry: MeterRegistry,
) {

    @Bean(name = [ClientNames.AccountingClient])
    fun accountingClientConfig() = initClient(
        configuration = ClientConfiguration(
            url = profileConfig.integrations.accounting.apiEndpoint,
            connectTimeoutInMillis = profileConfig.integrations.accounting.connectTimeoutInMillis,
            readTimeoutInMillis = profileConfig.integrations.accounting.readTimeoutInMillis,
            followRedirects = profileConfig.integrations.accounting.followRedirects,
            retry = RetryConfig(
                startingInterval = profileConfig.integrations.accounting.retryConfig.feignStartingInterval,
                maxRetryPeriod = profileConfig.integrations.accounting.retryConfig.feignMaximumRetryPeriod,
                maxAttempts = profileConfig.integrations.accounting.retryConfig.feignMaximumRetriesAllowed
            ),
            circuitBreakerConfiguration = CircuitBreakerConfiguration(
                name = profileConfig.integrations.accounting.circuitBreakerConfiguration.name,
                failureRateThreshold = profileConfig.integrations.accounting.circuitBreakerConfiguration.failureRateThreshold,
                slowCallRateThreshold = profileConfig.integrations.accounting.circuitBreakerConfiguration.slowCallRateThreshold,
                waitDurationInOpenStateInMillis = profileConfig.integrations.accounting.circuitBreakerConfiguration.waitDurationInOpenStateInMillis,
                slowCallDurationThresholdInMillis = profileConfig.integrations.accounting.circuitBreakerConfiguration.slowCallDurationThresholdInMillis,
                permittedNumberOfCallsInHalfOpenState = profileConfig.integrations.accounting.circuitBreakerConfiguration.permittedNumberOfCallsInHalfOpenState,
                minimumNumberOfCalls = profileConfig.integrations.accounting.circuitBreakerConfiguration.minimumNumberOfCalls,
                slidingWindowType = profileConfig.integrations.accounting.circuitBreakerConfiguration.slidingWindowType,
                slidingWindowSize = profileConfig.integrations.accounting.circuitBreakerConfiguration.slidingWindowSize
            )
        ),
        meterRegistry = meterRegistry,
        feignClass = AccountingClient::class
    )
}