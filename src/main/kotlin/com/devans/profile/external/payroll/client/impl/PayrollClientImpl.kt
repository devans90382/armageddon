package com.devans.profile.external.payroll.client.impl

import com.devans.profile.external.commons.BusinessProfileValidateRequest
import com.devans.profile.external.commons.BusinessProfileValidateResponse
import com.devans.profile.external.commons.client.ValidationClient
import com.devans.profile.external.payroll.client.PayrollClient
import com.devans.profile.utils.ClientNames
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component
class PayrollClientImpl(
    @Qualifier(ClientNames.PayrollClient)
    private val payrollClient: PayrollClient
) : ValidationClient {

    override fun validate(
        businessProfileValidateRequest: BusinessProfileValidateRequest
    ): BusinessProfileValidateResponse {
        return payrollClient.validateProfile(
            businessProfileValidateRequest = businessProfileValidateRequest
        )
    }
}