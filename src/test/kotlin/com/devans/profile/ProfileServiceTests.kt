package com.devans.profile

import com.devans.profile.commons.getBusinessProfileForDomainModel
import com.devans.profile.commons.getCreateBusinessProfileRequest
import com.devans.profile.data.ProfileData
import com.devans.profile.exception.ProfileErrorCodes
import com.devans.profile.exception.ProfileException
import com.devans.profile.mappers.toBusinessProfileValidateRequest
import com.devans.profile.mappers.toDomainObject
import com.devans.profile.model.Address
import com.devans.profile.model.BusinessProfile
import com.devans.profile.model.UpdateBusinessProfileRequest
import com.devans.profile.service.ProfileService
import com.devans.profile.service.ValidationService
import com.devans.profile.utils.ConstantOutput
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions

class ProfileServiceTests {

    @Mock
    lateinit var profileData: ProfileData

    @Mock
    lateinit var validationService: ValidationService

    @InjectMocks
    lateinit var profileService: ProfileService

    @BeforeEach
    fun init() {
        MockitoAnnotations.openMocks(this)
        profileService = ProfileService(profileData = profileData, validationService = validationService)
    }

    @Test
    fun getProfileById_WhenProfileExists_ThenReturnProfile() {
        val existingProfileId = "123"
        val businessProfile = getBusinessProfileForDomainModel(companyName = "companyName")

        runBlocking {
            Mockito.`when`(profileData.getProfileById(profileId = existingProfileId))
                .thenReturn(businessProfile)

            val response = profileService.getProfileById(existingProfileId)

            assertEquals(businessProfile, response)
        }
    }

    @Test
    fun getProfileById_WhenProfileDoesNotExist_ThenThrowsProfileNotFoundException() {
        val profileId = "123"
        val expectedException = ProfileException(
            error = ProfileErrorCodes.PROFILE_NOT_FOUND,
            message = "Profile not found with ID: $profileId"
        )

        runBlocking {
            Mockito.`when`(profileData.getProfileById(profileId = profileId))
                .thenThrow(expectedException)

            assertThrows<ProfileException> { profileService.getProfileById(profileId) }.also {
                assertEquals(it.error, expectedException.error)
            }
        }
    }

    @Test
    fun createProfile_WhenAllCorrect_ThenReturnProfileId() {
        val createBusinessProfileRequest = getCreateBusinessProfileRequest().toDomainObject()

        runBlocking {
            var capturedProfile: BusinessProfile? = null

            doAnswer { invocation ->
                capturedProfile = invocation.getArgument(0)
                null
            }.`when`(profileData).createProfile(any())

            val response = profileService.createProfile(createBusinessProfileRequest = createBusinessProfileRequest)

            verify(profileData).createProfile(any())
            assertEquals(response, capturedProfile!!.profileId)
        }
    }

    @Test
    fun createProfile_WhenSomethingFail_ThenThrowProfileException() {
        val businessProfile = getBusinessProfileForDomainModel(companyName = "company")
        val expectedException = ProfileException(
            error = ProfileErrorCodes.PROFILE_UNHANDLED_EXCEPTION,
            message = "Unhandled exception"
        )

        runBlocking {
            Mockito.`when`(profileData.createProfile(profile = businessProfile)).thenThrow(expectedException)

            assertThrows<ProfileException> {
                profileData.createProfile(profile = businessProfile)
            }.also {
                assertEquals(it.error, expectedException.error)
            }
        }
    }

    @Test
    fun updateProfile_WhenProfileUpdateSuccess_ThenReturnSuccessString() {
        val profileId = "123"
        val profile = getBusinessProfileForDomainModel(companyName = "company")
        val expectedResponse = ConstantOutput.SUCCESSFULLY_UPDATED_PROFILE
        val updateBusinessProfileRequest = getBusinessProfileUpdateRequest()

        runBlocking {
            Mockito.doAnswer { }.`when`(profileData).updateProfile(profile = profile)
            Mockito.`when`(
                validationService.profileUpdateValidation(
                    businessProfileValidateRequest = updateBusinessProfileRequest.toBusinessProfileValidateRequest(
                        profileId = profileId
                    )
                )
            ).thenReturn(true)

            val response = profileService.updateProfile(
                profileId = profileId,
                updateBusinessProfileRequest = updateBusinessProfileRequest
            )
            verify(profileData).updateProfile(any())
            assertEquals(response, expectedResponse)
        }
    }

    @Test
    fun updateProfile_WhenProfileUpdateValidationFailed_ThenThrowProfileUpdateForbiddenException() {
        val profileId = "123"
        val profile = getBusinessProfileForDomainModel(companyName = "company")
        val expectedException = ProfileException(
            error = ProfileErrorCodes.PROFILE_UPDATE_FORBIDDEN,
            message = "Forbidden"
        )
        val updateBusinessProfileRequest = getBusinessProfileUpdateRequest()

        runBlocking {
            Mockito.doAnswer { }.`when`(profileData).updateProfile(profile = profile)
            Mockito.`when`(
                validationService.profileUpdateValidation(
                    businessProfileValidateRequest = updateBusinessProfileRequest.toBusinessProfileValidateRequest(
                        profileId = profileId
                    )
                )
            ).thenReturn(false)

            assertThrows<ProfileException> {
                profileService.updateProfile(
                    profileId = profileId,
                    updateBusinessProfileRequest = updateBusinessProfileRequest
                )
            }.also {
                verifyNoInteractions(profileData)
                assertEquals(it.error, expectedException.error)
            }
        }
    }

    @Test
    fun deleteProfile_WhenDeleteProfileSuccess_ThenReturnDeleteProfileSuccessMessage() {
        val profileId = "123"
        val expectedResponse = ConstantOutput.SUCCESSFULLY_DELETED_PROFILE

        runBlocking {
            Mockito.doAnswer { }.`when`(profileData).deleteProfile(profileId = profileId)
            val response = profileService.deleteProfile(profileId = profileId)

            assertEquals(response, expectedResponse)
        }
    }

    @Test
    fun deleteProfile_WhenDeleteProfileUnsuccessful_ThenThrowsProfileNotFoundException() {
        val profileId = "123"
        val expectedException = ProfileException(
            error = ProfileErrorCodes.PROFILE_NOT_FOUND,
            message = "Profile not found with ID: $profileId"
        )

        runBlocking {
            Mockito.`when`(profileData.deleteProfile(profileId = profileId)).thenThrow(expectedException)

            assertThrows<ProfileException> { profileService.deleteProfile(profileId = profileId) }.also {
                assertEquals(it.error, expectedException.error)
            }
        }
    }


    private fun getBusinessProfileUpdateRequest() =
        UpdateBusinessProfileRequest(
            companyName = "companyName",
            legalName = "123",
            businessAddress = Address(
                line1 = "21 Jump Street",
                line2 = "Hollywood",
                city = "Los Angeles",
                state = "CA",
                country = "United States",
                zip = "560068"
            ),
            legalAddress = Address(
                line1 = "21 Jump Street",
                line2 = "Hollywood",
                city = "Los Angeles",
                state = "CA",
                country = "United States",
                zip = "560068"
            ),
            taxIdentifier = "tax_id",
            email = "example@example.com",
            website = "www.example.com"
        )

}
