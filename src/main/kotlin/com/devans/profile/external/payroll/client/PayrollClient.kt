package com.devans.profile.external.payroll.client

import com.devans.profile.external.commons.BusinessProfileValidateRequest
import com.devans.profile.external.commons.BusinessProfileValidateResponse
import feign.Headers
import feign.RequestLine

interface PayrollClient {

    @Headers("Content-Type: application/json")
    @RequestLine("POST /v1/validateProfile")
    fun validateProfile(
        businessProfileValidateRequest: BusinessProfileValidateRequest
    ): BusinessProfileValidateResponse
}