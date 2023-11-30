package com.devans.profile.external.accounting.client.impl

import com.devans.profile.external.accounting.client.AccountingClient
import com.devans.profile.external.commons.BusinessProfileValidateRequest
import com.devans.profile.external.commons.BusinessProfileValidateResponse
import com.devans.profile.external.commons.client.ValidationClient
import com.devans.profile.utils.ClientNames
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component
class AccountingClientImpl(
    @Qualifier(ClientNames.AccountingClient)
    private val accountingClient: AccountingClient
) : ValidationClient {

    override fun validate(
        businessProfileValidateRequest: BusinessProfileValidateRequest
    ): BusinessProfileValidateResponse {
        return accountingClient.validateProfile(
            businessProfileValidateRequest = businessProfileValidateRequest
        )
    }
}