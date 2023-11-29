package com.devans.profile.mappers

import com.devans.profile.external.commons.Address
import com.devans.profile.external.commons.BusinessProfileValidateRequest
import com.devans.profile.model.UpdateBusinessProfileRequest

fun com.devans.profile.model.Address.toExtAddress() = Address(
    line1 = this.line1,
    city = this.city,
    line2 = this.line2,
    state = this.state,
    country = this.country,
    zip = this.zip
)

fun UpdateBusinessProfileRequest.toBusinessProfileValidateRequest(
    profileId: String
): BusinessProfileValidateRequest =
    BusinessProfileValidateRequest(
        profileId = profileId,
        companyName = this.companyName,
        legalName = this.legalName,
        businessAddress = this.businessAddress.toExtAddress(),
        legalAddress = this.legalAddress.toExtAddress(),
        taxIdentifier = this.taxIdentifier,
        email = this.email,
        website = this.website
    )