package com.devans.profile.external.commons.client

import com.devans.profile.external.commons.BusinessProfileValidateRequest
import com.devans.profile.external.commons.BusinessProfileValidateResponse

interface ValidationClient {

    fun validate(businessProfileValidateRequest: BusinessProfileValidateRequest): BusinessProfileValidateResponse
}