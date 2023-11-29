package com.devans.profile.config

import com.devans.profile.external.commons.RetryConfig
import com.devans.profile.utils.CircuitBreakerNames
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import reactor.util.retry.Retry


@ConfigurationProperties("devans.profile")
@Configuration
class ProfileConfig {

    val integrations = Integrations()

    class Integrations {
        val accounting = Accounting()
        val payroll = Payroll()
        val payments = Payments()

        class Accounting {
            val apiEndpoint: String = "http://localhost:9091"
            val connectTimeoutInMillis: Long = 100000
            val readTimeoutInMillis: Long = 100000
            val followRedirects: Boolean = false

            val retryConfig = RetryConfig()

            class RetryConfig {
                var feignMaximumRetriesAllowed: Int = 3
                var feignStartingInterval: Long = 3
                var feignMaximumRetryPeriod: Long = 3
            }

            val circuitBreakerConfiguration = CircuitBreakerConfiguration()

            class CircuitBreakerConfiguration {
                var name = CircuitBreakerNames.AccountingCircuitBreaker
                var failureRateThreshold: Float = 50f
                var slowCallRateThreshold: Float = 10f
                var waitDurationInOpenStateInMillis: Long = 1000
                var slowCallDurationThresholdInMillis: Long = 1000
                var permittedNumberOfCallsInHalfOpenState: Int = 3
                var minimumNumberOfCalls: Int = 100
                var slidingWindowType: String = "TIME_BASED"
                var slidingWindowSize: Int = 10
            }
        }

        class Payroll {
            val apiEndpoint: String = "http://localhost:9092"
            val connectTimeoutInMillis: Long = 100000
            val readTimeoutInMillis: Long = 100000
            val followRedirects: Boolean = false

            val retryConfig = RetryConfig()

            class RetryConfig {
                var feignMaximumRetriesAllowed: Int = 3
                var feignStartingInterval: Long = 3
                var feignMaximumRetryPeriod: Long = 3
            }

            val circuitBreakerConfiguration = CircuitBreakerConfiguration()

            class CircuitBreakerConfiguration {
                var name = CircuitBreakerNames.AccountingCircuitBreaker
                var failureRateThreshold: Float = 50f
                var slowCallRateThreshold: Float = 10f
                var waitDurationInOpenStateInMillis: Long = 1000
                var slowCallDurationThresholdInMillis: Long = 1000
                var permittedNumberOfCallsInHalfOpenState: Int = 3
                var minimumNumberOfCalls: Int = 100
                var slidingWindowType: String = "TIME_BASED"
                var slidingWindowSize: Int = 10
            }
        }

        class Payments {}
    }
}