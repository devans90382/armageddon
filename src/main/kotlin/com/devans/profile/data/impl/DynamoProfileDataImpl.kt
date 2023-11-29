package com.devans.profile.data.impl

import BusinessProfileItem
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper
import com.devans.profile.data.ProfileData
import com.devans.profile.data.toBusinessProfile
import com.devans.profile.data.toDynamoItem
import com.devans.profile.data.toUpdateDynamoItem
import com.devans.profile.exception.ProfileErrorCodes
import com.devans.profile.exception.ProfileException
import com.devans.profile.model.BusinessProfile
import mu.KLoggable
import mu.KLogger
import org.springframework.stereotype.Repository

@Repository
class DynamoProfileDataImpl(
    private val dynamoDBMapper: DynamoDBMapper
) : ProfileData {

    companion object : KLoggable {
        override val logger: KLogger = logger()
    }

    override suspend fun createProfile(profile: BusinessProfile) {
        logger.info { "Creating business profile for company: ${profile.companyName} and profileId: ${profile.profileId}" }
        try {
            dynamoDBMapper.save(profile.toDynamoItem())
        } catch (e: Exception) {
            handleProfileException("Creating", e)
        }
    }

    override suspend fun getProfileById(profileId: String): BusinessProfile {
        logger.info { "Get profile by id: $profileId" }
        return try {
            dynamoDBMapper.load(BusinessProfileItem::class.java, profileId)?.toBusinessProfile()
                ?: handleProfileNotFound(profileId)
        } catch (e: Exception) {
            handleProfileException("Getting", e)
        }
    }

    override suspend fun updateProfile(profile: BusinessProfile) {
        logger.info { "Updating profile for id: ${profile.profileId}" }
        val businessProfileItem = dynamoDBMapper.load(BusinessProfileItem::class.java, profile.profileId)
            ?: handleProfileNotFound(profile.profileId)

        try {
            dynamoDBMapper.save(profile.toUpdateDynamoItem(item = businessProfileItem))
        } catch (e: Exception) {
            handleProfileException("Updating", e)
        }
    }

    override suspend fun deleteProfile(profileId: String) {
        logger.info { "Delete profile by id: $profileId" }
        try {
            dynamoDBMapper.load(BusinessProfileItem::class.java, profileId)?.let {
                dynamoDBMapper.delete(it)
            } ?: handleProfileNotFound(profileId)
        } catch (e: Exception) {
            handleProfileException("Deleting", e)
        }
    }

    private suspend fun handleProfileNotFound(profileId: String): Nothing {
        logger.info { "Profile with id: $profileId doesn't exist" }
        throw ProfileException(
            error = ProfileErrorCodes.PROFILE_NOT_FOUND,
            message = "Profile not found with ID: $profileId"
        )
    }

    private suspend fun handleProfileException(operation: String, e: Exception): Nothing {
        logger.info { "Exception in $operation profile from DynamoDB: ${e.message}" }
        throw ProfileException(
            error = ProfileErrorCodes.PROFILE_UNHANDLED_EXCEPTION,
            message = "$operation Profile failed: ${e.message}"
        )
    }
}