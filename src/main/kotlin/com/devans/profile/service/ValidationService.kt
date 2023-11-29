package com.devans.profile.service

import com.devans.profile.external.accounting.client.impl.AccountingClientImpl
import com.devans.profile.external.commons.BusinessProfileValidateRequest
import com.devans.profile.external.payroll.client.impl.PayrollClientImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import mu.KLoggable
import mu.KLogger
import org.springframework.stereotype.Service

@Service
class ValidationService(
    private val accountingClientImpl: AccountingClientImpl,
    private val payrollClientImpl: PayrollClientImpl
) {

    companion object : KLoggable {
        override val logger: KLogger = logger()
    }

    suspend fun profileUpdateValidation(businessProfileValidateRequest: BusinessProfileValidateRequest): Boolean {
        logger.info { "Received profile update validation request for profile id: ${businessProfileValidateRequest.profileId}" }
        return coroutineScope {

            // Calling accounting service for business profile validation check
            val accountingServiceValidationResult = async(Dispatchers.IO) {
                logger.info { "Calling Accounting service to validate profile id: ${businessProfileValidateRequest.profileId}" }
                accountingClientImpl.validate(businessProfileValidateRequest)
            }

            // Calling payroll service for business profile validation check
            val payrollServiceValidationResult = async(Dispatchers.IO) {
                logger.info { "Calling Payroll service to validate profile id: ${businessProfileValidateRequest.profileId}" }
                payrollClientImpl.validate(businessProfileValidateRequest)
            }

            // Similarly other calls can be added, and they will be added asynchronously

            accountingServiceValidationResult.await().approved && payrollServiceValidationResult.await().approved
        }
    }

}