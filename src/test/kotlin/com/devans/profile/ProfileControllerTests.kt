package com.devans.profile

import com.devans.profile.commons.getBusinessProfileForDomainModel
import com.devans.profile.commons.getCreateBusinessProfileRequest
import com.devans.profile.controllers.ProfileController
import com.devans.profile.exception.ProfileErrorCodes
import com.devans.profile.exception.ProfileException
import com.devans.profile.http.model.Address
import com.devans.profile.http.model.BusinessProfile
import com.devans.profile.http.model.UpdateBusinessProfileRequest
import com.devans.profile.mappers.toDomainObject
import com.devans.profile.service.ProfileService
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
import org.springframework.http.HttpStatus

class ProfileControllerTests {

    @Mock
    lateinit var profileService: ProfileService

    @InjectMocks
    lateinit var profileController: ProfileController

    @BeforeEach
    fun init() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun getProfileById_WhenProfileExists_ThenReturnProfile() {
        val existingProfileId = "123"
        val companyName = "CompanyA"

        runBlocking {
            Mockito.`when`(profileService.getProfileById(profileId = existingProfileId))
                .thenReturn(getBusinessProfileForDomainModel(companyName = companyName))

            val responseEntity = profileController.getProfileById(existingProfileId)

            assertEquals(HttpStatus.OK, responseEntity.statusCode)
            assertEquals(getBusinessProfileForApiModel(companyName = companyName), responseEntity.body)
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
            Mockito.`when`(profileService.getProfileById(profileId = profileId)).thenThrow(expectedException)

            assertThrows<ProfileException> {
                profileController.getProfileById(profileId = profileId)
            }.also { exception ->
                assertEquals(
                    HttpStatus.NOT_FOUND.value(),
                    exception.error.getHTTPStatus(),
                    "HTTP status should be NOT_FOUND"
                )
                assertEquals(
                    expectedException.message, exception.message, "Exception message should match"
                )
            }
        }
    }

    @Test
    fun createProfile_WhenAllCorrect_ThenReturnProfileId() {
        val profileId = "123"
        val createBusinessProfileRequest = getCreateBusinessProfileRequest()

        runBlocking {
            Mockito.`when`(profileService.createProfile(createBusinessProfileRequest = createBusinessProfileRequest.toDomainObject()))
                .thenReturn(profileId)
            val responseEntity =
                profileController.createProfile(createBusinessProfileRequest = createBusinessProfileRequest)

            assertEquals(responseEntity.statusCode, HttpStatus.CREATED)
            assertEquals(responseEntity.body, profileId)
        }
    }

    @Test
    fun createProfile_WhenSomethingFail_ThenThrowProfileException() {
        val createBusinessProfileRequest = getCreateBusinessProfileRequest()

        val expectedException = ProfileException(
            error = ProfileErrorCodes.PROFILE_UNHANDLED_EXCEPTION,
            message = "Unhandled exception"
        )

        runBlocking {
            Mockito.`when`(profileService.createProfile(createBusinessProfileRequest = createBusinessProfileRequest.toDomainObject()))
                .thenThrow(expectedException)

            assertThrows<ProfileException> {
                profileController.createProfile(createBusinessProfileRequest = createBusinessProfileRequest)
            }
        }
    }

    @Test
    fun updateProfile_WhenProfileUpdateSuccess_ThenReturnSuccessString() {
        val profileId = "123"
        val expectedResponse = ConstantOutput.SUCCESSFULLY_UPDATED_PROFILE

        val updateBusinessProfileRequest = getUpdateBusinessProfileRequest()

        runBlocking {
            Mockito.`when`(
                profileService.updateProfile(
                    profileId = profileId,
                    updateBusinessProfileRequest = updateBusinessProfileRequest.toDomainObject()
                )
            ).thenReturn(expectedResponse)

            val responseEntity = profileController.updateProfile(
                profileId = profileId,
                updateBusinessProfileRequest = updateBusinessProfileRequest
            )

            assertEquals(expectedResponse, responseEntity.body)
        }
    }

    @Test
    fun updateProfile_WhenProfileUpdateValidationFailed_ThenThrowProfileUpdateForbiddenException() {
        val profileId = "123"
        val expectedException = ProfileException(
            error = ProfileErrorCodes.PROFILE_UPDATE_FORBIDDEN,
            message = "Update Forbidden"
        )

        val updateBusinessProfileRequest = getUpdateBusinessProfileRequest()

        runBlocking {
            Mockito.`when`(
                profileService.updateProfile(
                    profileId = profileId,
                    updateBusinessProfileRequest = updateBusinessProfileRequest.toDomainObject()
                )
            ).thenThrow(expectedException)

            assertThrows<ProfileException> {
                profileController.updateProfile(
                    profileId = profileId,
                    updateBusinessProfileRequest = updateBusinessProfileRequest
                )
            }.also {
                assertEquals(expectedException.error, it.error)
            }
        }
    }

    @Test
    fun deleteProfile_WhenDeleteProfileSuccess_ThenReturnDeleteProfileSuccessMessage() {
        val profileId = "123"
        val expectedResponse = ConstantOutput.SUCCESSFULLY_DELETED_PROFILE

        runBlocking {
            Mockito.`when`(profileService.deleteProfile(profileId = profileId)).thenReturn(expectedResponse)

            val responseEntity = profileController.deleteProfile(profileId = profileId)

            assertEquals(responseEntity.body, expectedResponse)
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
            Mockito.`when`(profileService.deleteProfile(profileId = profileId)).thenThrow(expectedException)

            assertThrows<ProfileException> {
                profileController.deleteProfile(profileId = profileId)
            }.also {
                assertEquals(expectedException.error, it.error)
            }
        }
    }

    private fun getUpdateBusinessProfileRequest() =
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

    private fun getBusinessProfileForApiModel(companyName: String): BusinessProfile =
        BusinessProfile(
            profileId = "123",
            companyName = companyName,
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
