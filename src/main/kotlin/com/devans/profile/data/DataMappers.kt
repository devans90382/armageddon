package com.devans.profile.data

import BusinessProfileItem
import com.devans.profile.model.BusinessProfile
import com.devans.profile.utils.getCurrentEpochMilliSecond

fun BusinessProfile.toDynamoItem(): BusinessProfileItem {
    return BusinessProfileItem(
        profileId = this.profileId,
        companyName = this.companyName,
        legalName = this.legalName,
        businessAddress = this.businessAddress,
        legalAddress = this.legalAddress,
        taxIdentifier = this.taxIdentifier,
        email = this.email,
        website = this.website,
        createdAt = getCurrentEpochMilliSecond(),
        updatedAt = getCurrentEpochMilliSecond()
    )
}

fun BusinessProfileItem.toBusinessProfile(): BusinessProfile {
    return BusinessProfile(
        profileId = this.profileId,
        companyName = this.companyName,
        legalName = this.legalName,
        businessAddress = this.businessAddress,
        legalAddress = this.legalAddress,
        taxIdentifier = this.taxIdentifier,
        email = this.email,
        website = this.website
    )
}
