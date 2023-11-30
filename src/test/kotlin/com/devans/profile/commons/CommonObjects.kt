package com.devans.profile.commons

import com.devans.profile.http.model.Address
import com.devans.profile.http.model.CreateBusinessProfileRequest

fun getCreateBusinessProfileRequest() =
    CreateBusinessProfileRequest(
        companyName = "companyName",
        legalName = "123",
        businessAddress = Address(
            line1 = "21 Jump Street",
            line2 = "Hollywood",
            city = "Los Angeles",
            state = "CA",
            country = "United States",
            zip = "560068"
        ),
        legalAddress = Address(
            line1 = "21 Jump Street",
            line2 = "Hollywood",
            city = "Los Angeles",
            state = "CA",
            country = "United States",
            zip = "560068"
        ),
        taxIdentifier = "tax_id",
        email = "example@example.com",
        website = "www.example.com"
    )

fun getBusinessProfileForDomainModel(companyName: String): com.devans.profile.model.BusinessProfile =
    com.devans.profile.model.BusinessProfile(
        profileId = "123",
        companyName = companyName,
        legalName = "123",
        businessAddress = com.devans.profile.model.Address(
            line1 = "21 Jump Street",
            line2 = "Hollywood",
            city = "Los Angeles",
            state = "CA",
            country = "United States",
            zip = "560068"
        ),
        legalAddress = com.devans.profile.model.Address(
            line1 = "21 Jump Street",
            line2 = "Hollywood",
            city = "Los Angeles",
            state = "CA",
            country = "United States",
            zip = "560068"
        ),
        taxIdentifier = "tax_id",
        email = "example@example.com",
        website = "www.example.com"
    )