package com.devans.profile.data.impl

import BusinessProfileItem
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper
import com.devans.profile.data.ProfileData
import com.devans.profile.data.toBusinessProfile
import com.devans.profile.data.toDynamoItem
import com.devans.profile.exception.ProfileErrorCodes
import com.devans.profile.exception.ProfileException
import com.devans.profile.model.BusinessProfile
import org.springframework.stereotype.Repository

@Repository
class DynamoProfileDataImpl(
    private val dynamoDBMapper: DynamoDBMapper
) : ProfileData {

    override suspend fun createProfile(profile: BusinessProfile) {
        dynamoDBMapper.save(profile.toDynamoItem())
    }

    override suspend fun getProfileById(profileId: String): BusinessProfile {
        return dynamoDBMapper.load(BusinessProfileItem::class.java, profileId)?.toBusinessProfile()
            ?: throw ProfileException(
                error = ProfileErrorCodes.PROFILE_NOT_FOUND, message = "Profile not found"
            )
    }

    override suspend fun updateProfile(profile: BusinessProfile) {
        dynamoDBMapper.save(profile.toDynamoItem())
    }

    override suspend fun deleteProfile(profileId: String) {
        val profileItem = dynamoDBMapper.load(BusinessProfileItem::class.java, profileId)
        if (profileItem != null) {
            dynamoDBMapper.delete(profileItem)
        }
    }
}

