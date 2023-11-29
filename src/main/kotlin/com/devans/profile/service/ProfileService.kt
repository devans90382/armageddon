package com.devans.profile.service

import com.devans.profile.data.ProfileData
import com.devans.profile.external.commons.Address
import com.devans.profile.external.commons.BusinessProfileValidateRequest
import com.devans.profile.model.BusinessProfile
import com.devans.profile.model.CreateBusinessProfileRequest
import com.devans.profile.model.UpdateBusinessProfileRequest
import com.devans.profile.model.toBusinessProfile
import com.devans.profile.utils.generateLongUniqueIdentifier
import org.springframework.stereotype.Service

@Service
class ProfileService(
    private val profileData: ProfileData,
    private val validationService: ValidationService
) {

    suspend fun createProfile(createBusinessProfileRequest: CreateBusinessProfileRequest): String {
        val id = generateLongUniqueIdentifier()
        profileData.createProfile(
            profile = createBusinessProfileRequest.toBusinessProfile(id = id)
        )

        return id
    }

    suspend fun getProfileById(profileId: String): BusinessProfile {
        return profileData.getProfileById(profileId = profileId)
    }

    suspend fun updateProfile(profileId: String, updateBusinessProfileRequest: UpdateBusinessProfileRequest): Boolean {
        return try {
            if (doValidation(profileId = profileId, updateBusinessProfileRequest = updateBusinessProfileRequest)) {
                profileData.updateProfile(profile = updateBusinessProfileRequest.toBusinessProfile(profileId = profileId))
                true
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }

    suspend fun deleteProfile(profileId: String): Boolean {
        return try {
            profileData.deleteProfile(profileId = profileId)
            true
        } catch (e: Exception) {
            false
        }
    }

    private suspend fun doValidation(
        profileId: String,
        updateBusinessProfileRequest: UpdateBusinessProfileRequest
    ): Boolean {
        return validationService.profileUpdateValidation(
            businessProfileValidateRequest = BusinessProfileValidateRequest(
                profileId = profileId,
                companyName = updateBusinessProfileRequest.companyName,
                legalName = updateBusinessProfileRequest.legalName,
                businessAddress = updateBusinessProfileRequest.businessAddress.toAddress(),
                legalAddress = updateBusinessProfileRequest.legalAddress.toAddress(),
                taxIdentifier = updateBusinessProfileRequest.taxIdentifier,
                email = updateBusinessProfileRequest.email,
                website = updateBusinessProfileRequest.website
            )
        )
    }
}


private fun com.devans.profile.model.Address.toAddress() = Address(
    line1 = this.line1,
    city = this.city,
    line2 = this.line2,
    state = this.state,
    country = this.country,
    zip = this.zip
)
