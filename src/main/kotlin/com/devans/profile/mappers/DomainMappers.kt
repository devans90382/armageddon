package com.devans.profile.mappers

import com.devans.profile.http.model.BusinessProfile
import com.devans.profile.model.Address
import com.devans.profile.model.CreateBusinessProfileRequest
import com.devans.profile.model.UpdateBusinessProfileRequest

fun com.devans.profile.http.model.CreateBusinessProfileRequest.toDomainObject(): CreateBusinessProfileRequest =
    CreateBusinessProfileRequest(
        companyName = this.companyName,
        legalName = this.legalName,
        businessAddress = Address(
            line1 = this.businessAddress.line1,
            line2 = this.businessAddress.line2,
            city = this.businessAddress.city,
            state = this.businessAddress.state,
            zip = this.businessAddress.zip,
            country = this.businessAddress.country
        ),
        legalAddress = Address(
            line1 = this.legalAddress.line1,
            line2 = this.legalAddress.line2,
            city = this.legalAddress.city,
            state = this.legalAddress.state,
            zip = this.legalAddress.zip,
            country = this.legalAddress.country
        ),
        taxIdentifier = this.taxIdentifier,
        email = this.email,
        website = this.website
    )

fun com.devans.profile.http.model.UpdateBusinessProfileRequest.toDomainObject(): UpdateBusinessProfileRequest =
    UpdateBusinessProfileRequest(
        companyName = this.companyName,
        legalName = this.legalName,
        businessAddress = Address(
            line1 = this.businessAddress.line1,
            line2 = this.businessAddress.line2,
            city = this.businessAddress.city,
            state = this.businessAddress.state,
            zip = this.businessAddress.zip,
            country = this.businessAddress.country
        ),
        legalAddress = Address(
            line1 = this.legalAddress.line1,
            line2 = this.legalAddress.line2,
            city = this.legalAddress.city,
            state = this.legalAddress.state,
            zip = this.legalAddress.zip,
            country = this.legalAddress.country
        ),
        taxIdentifier = this.taxIdentifier,
        email = this.email,
        website = this.website
    )

fun com.devans.profile.model.BusinessProfile.toApiObject(): BusinessProfile =
    BusinessProfile(
        profileId = this.profileId,
        companyName = this.companyName,
        legalName = this.legalName,
        businessAddress = this.businessAddress.toApiObject(),
        legalAddress = this.legalAddress.toApiObject(),
        taxIdentifier = this.taxIdentifier,
        email = this.email,
        website = this.website
    )

fun Address.toApiObject(): com.devans.profile.http.model.Address =
    com.devans.profile.http.model.Address(
        line1 = this.line1,
        line2 = this.line2,
        city = this.city,
        state = this.state,
        zip = this.zip,
        country = this.country
    )
