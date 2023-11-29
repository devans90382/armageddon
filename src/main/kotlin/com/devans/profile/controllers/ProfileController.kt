package com.devans.profile.controllers

import com.devans.profile.http.api.ProfileApi
import com.devans.profile.http.model.BusinessProfile
import com.devans.profile.http.model.CreateBusinessProfileRequest
import com.devans.profile.http.model.UpdateBusinessProfileRequest
import com.devans.profile.mappers.toApiObject
import com.devans.profile.mappers.toDomainObject
import com.devans.profile.service.ProfileService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class ProfileController(private val profileService: ProfileService) : ProfileApi {

    override suspend fun updateProfile(
        profileId: String,
        updateBusinessProfileRequest: UpdateBusinessProfileRequest
    ): ResponseEntity<String> {
        val updated: Boolean = profileService.updateProfile(
            profileId = profileId, updateBusinessProfileRequest = updateBusinessProfileRequest.toDomainObject()
        )
        return if (updated) {
            ResponseEntity.ok("Profile updated successfully")
        } else {
            ResponseEntity.ok().body("Profile not updated")
        }
    }

    override suspend fun createProfile(createBusinessProfileRequest: CreateBusinessProfileRequest): ResponseEntity<String> {
        val profileId: String = profileService.createProfile(
            createBusinessProfileRequest = createBusinessProfileRequest.toDomainObject()
        )
        return ResponseEntity.status(HttpStatus.CREATED).body(profileId)
    }

    override suspend fun deleteProfile(profileId: String): ResponseEntity<String> {
        val deleted: Boolean = profileService.deleteProfile(profileId = profileId)
        return if (deleted) {
            ResponseEntity.ok("Profile deleted successfully")
        } else {
            ResponseEntity.notFound().build()
        }
    }

    override suspend fun getProfileById(profileId: String): ResponseEntity<BusinessProfile> {
        return ResponseEntity.ok(profileService.getProfileById(profileId = profileId).toApiObject())
    }
}
