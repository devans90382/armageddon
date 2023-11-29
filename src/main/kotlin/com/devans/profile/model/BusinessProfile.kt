package com.devans.profile.model

/*
Business Profile

Company Name
Legal Name
Business Address (Line1, Line 2, City, State, Zip, Country)
Legal Address
Tax Identifiers (PAN, EIN)
Email
Website
 */
data class BusinessProfile(
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
