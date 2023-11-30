package com.devans.profile.service

import com.devans.profile.data.ProfileData
import com.devans.profile.exception.ProfileErrorCodes
import com.devans.profile.exception.ProfileException
import com.devans.profile.mappers.toBusinessProfileValidateRequest
import com.devans.profile.model.BusinessProfile
import com.devans.profile.model.CreateBusinessProfileRequest
import com.devans.profile.model.UpdateBusinessProfileRequest
import com.devans.profile.model.toBusinessProfile
import com.devans.profile.utils.ConstantOutput.PROFILE_UPDATE_REJECTED
import com.devans.profile.utils.ConstantOutput.SUCCESSFULLY_DELETED_PROFILE
import com.devans.profile.utils.ConstantOutput.SUCCESSFULLY_UPDATED_PROFILE
import com.devans.profile.utils.ConstantOutput.UNHANDLED_EXCEPTION
import com.devans.profile.utils.generateLongUniqueIdentifier
import mu.KLoggable
import mu.KLogger
import org.springframework.stereotype.Service

@Service
class ProfileService(
    private val profileData: ProfileData,
    private val validationService: ValidationService
) {

    companion object : KLoggable {
        override val logger: KLogger = logger()
    }

    suspend fun createProfile(createBusinessProfileRequest: CreateBusinessProfileRequest): String {
        logger.info { "Received request to create business profile with request: $createBusinessProfileRequest" }
        val id = generateLongUniqueIdentifier()
        profileData.createProfile(
            profile = createBusinessProfileRequest.toBusinessProfile(id = id)
        )

        return id
    }

    suspend fun getProfileById(profileId: String): BusinessProfile {
        logger.info { "Received request to get business profile by id: $profileId" }
        return profileData.getProfileById(profileId = profileId)
    }

    suspend fun updateProfile(profileId: String, updateBusinessProfileRequest: UpdateBusinessProfileRequest): String {
        logger.info {
            "Received request to update business profile with id : $profileId," +
                " and request: $updateBusinessProfileRequest"
        }
        return try {
            if (validateBusinessProfileUpdate(
                    profileId = profileId,
                    updateBusinessProfileRequest = updateBusinessProfileRequest
                )
            ) {
                logger.info { "Update validation successful, updating profile with id : $profileId" }
                profileData.updateProfile(
                    profile = updateBusinessProfileRequest.toBusinessProfile(profileId = profileId)
                )
                SUCCESSFULLY_UPDATED_PROFILE
            } else {
                logger.info { "Update validation unsuccessful for profile with id : $profileId" }
                throw ProfileException(
                    error = ProfileErrorCodes.PROFILE_UPDATE_FORBIDDEN,
                    message = PROFILE_UPDATE_REJECTED
                )
            }
        } catch (e: ProfileException) {
            throw e
        } catch (e: Exception) {
            logger.info { "Validation check failed due to error : ${e.message}" }
            throw ProfileException(
                error = ProfileErrorCodes.PROFILE_UNHANDLED_EXCEPTION,
                message = UNHANDLED_EXCEPTION
            )
        }
    }

    suspend fun deleteProfile(profileId: String): String {
        logger.info { "Received business profile delete request for profile id : $profileId" }
        profileData.deleteProfile(profileId = profileId)
        return SUCCESSFULLY_DELETED_PROFILE
    }

    private suspend fun validateBusinessProfileUpdate(
        profileId: String,
        updateBusinessProfileRequest: UpdateBusinessProfileRequest
    ): Boolean {
        return try {
            validationService.profileUpdateValidation(
                businessProfileValidateRequest =
                updateBusinessProfileRequest.toBusinessProfileValidateRequest(profileId = profileId)
            )
        } catch (e: Exception) {
            throw e
        }
    }
}
