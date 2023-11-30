package com.devans.profile.service

import com.devans.profile.external.accounting.client.impl.AccountingClientImpl
import com.devans.profile.external.commons.BusinessProfileValidateRequest
import com.devans.profile.external.commons.BusinessProfileValidateResponse
import com.devans.profile.external.commons.client.ValidationClient
import com.devans.profile.external.payroll.client.impl.PayrollClientImpl
import com.devans.profile.metrics.MetricsBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import mu.KLoggable
import mu.KLogger
import org.springframework.stereotype.Service

@Service
class ValidationService(
    private val accountingClientImpl: AccountingClientImpl,
    private val payrollClientImpl: PayrollClientImpl,
    private val metricsBuilder: MetricsBuilder
) {

    companion object : KLoggable {
        override val logger: KLogger = logger()
    }

    suspend fun profileUpdateValidation(businessProfileValidateRequest: BusinessProfileValidateRequest): Boolean {
        logger.info { "Received profile update validation request for profile id: ${businessProfileValidateRequest.profileId}" }
        return coroutineScope {

            try {
                val accountingServiceValidationResult = async(Dispatchers.IO) {
                    validateProfileByService(
                        accountingClientImpl,
                        "Accounting",
                        businessProfileValidateRequest
                    )
                }

                val payrollServiceValidationResult = async(Dispatchers.IO) {
                    validateProfileByService(
                        payrollClientImpl,
                        "Payroll",
                        businessProfileValidateRequest
                    )
                }

                // Similarly add any other service for validation

                accountingServiceValidationResult.await().approved && payrollServiceValidationResult.await().approved
            } catch (e: Exception) {
                throw e
            }
        }
    }

    private suspend fun validateProfileByService(
        client: ValidationClient,
        serviceName: String,
        businessProfileValidateRequest: BusinessProfileValidateRequest
    ): BusinessProfileValidateResponse {
        logger.info { "Calling $serviceName service to validate profile id: ${businessProfileValidateRequest.profileId}" }
        try {
            return client.validate(businessProfileValidateRequest = businessProfileValidateRequest)
        } catch (e: Exception) {
            logger.info {
                "Call to $serviceName service to validate profile id: " +
                    "${businessProfileValidateRequest.profileId} failed, due to error : ${e.message}"
            }
            metricsBuilder.buildExceptionCounter(
                operation = "${serviceName}ProfileValidate"
            )
            throw e
        }
    }
}
