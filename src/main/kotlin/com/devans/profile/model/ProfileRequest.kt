package com.devans.profile.model

data class CreateBusinessProfileRequest(
    val companyName: String,
    val legalName: String,
    val businessAddress: Address,
    val legalAddress: Address,
    val taxIdentifier: String,
    val email: String,
    val website: String
)

data class UpdateBusinessProfileRequest(
    val companyName: String,
    val legalName: String,
    val businessAddress: Address,
    val legalAddress: Address,
    val taxIdentifier: String,
    val email: String,
    val website: String
)

fun CreateBusinessProfileRequest.toBusinessProfile(id: String) =
    BusinessProfile(
        profileId = id,
        companyName = this.companyName,
        legalName = this.legalName,
        businessAddress = this.businessAddress,
        legalAddress = this.legalAddress,
        taxIdentifier = this.taxIdentifier,
        email = this.email,
        website = this.website
    )

fun UpdateBusinessProfileRequest.toBusinessProfile(profileId: String) =
    BusinessProfile(
        profileId = profileId,
        companyName = this.companyName,
        legalName = this.legalName,
        businessAddress = this.businessAddress,
        legalAddress = this.legalAddress,
        taxIdentifier = this.taxIdentifier,
        email = this.email,
        website = this.website
    )


