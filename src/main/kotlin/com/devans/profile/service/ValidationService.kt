package com.devans.profile.service

import com.devans.profile.external.accounting.client.impl.AccountingClientImpl
import com.devans.profile.external.commons.BusinessProfileValidateRequest
import com.devans.profile.external.payroll.client.impl.PayrollClientImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.springframework.stereotype.Service

@Service
class ValidationService(
    private val accountingClientImpl: AccountingClientImpl,
    private val payrollClientImpl: PayrollClientImpl
) {

    suspend fun profileUpdateValidation(businessProfileValidateRequest: BusinessProfileValidateRequest): Boolean {
        return coroutineScope {
            val accountingServiceValidationResult = async(Dispatchers.IO) {
                accountingClientImpl.validate(businessProfileValidateRequest)
            }

            val payrollServiceValidationResult = async(Dispatchers.IO) {
                payrollClientImpl.validate(businessProfileValidateRequest)
            }

            accountingServiceValidationResult.await().approved && payrollServiceValidationResult.await().approved
        }
    }

}