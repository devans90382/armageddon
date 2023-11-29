package com.devans.profile.external.commons

data class BusinessProfileValidateRequest(
    val profileId: String,
    val companyName: String,
    val legalName: String,
    val businessAddress: Address,
    val legalAddress: Address,
    val taxIdentifier: String,
    val email: String,
    val website: String
)

data class Address(
    val line1: String,
    val line2: String,
    val city: String,
    val state: String,
    val zip: String,
    val country: String
)

data class BusinessProfileValidateResponse(
    val approved: Boolean
)