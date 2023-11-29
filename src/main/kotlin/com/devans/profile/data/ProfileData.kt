package com.devans.profile.data

import com.devans.profile.model.BusinessProfile

interface ProfileData {

    suspend fun createProfile(profile: BusinessProfile)

    suspend fun getProfileById(profileId: String): BusinessProfile

    suspend fun updateProfile(profile: BusinessProfile)

    suspend fun deleteProfile(profileId: String)
}
