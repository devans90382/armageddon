package com.devans.profile.exception
import com.devans.profile.http.model.Error
import com.devans.profile.http.model.ErrorResponse
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(ProfileException::class)
    fun handleProfileException(e: ProfileException): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            error = Error.valueOf(e.error.getErrorCode()),
            errorCode = e.error.getErrorCode(),
            message = e.message ?: e.error.getDescription()
        )

        return ResponseEntity(errorResponse, HttpStatusCode.valueOf(e.error.getHTTPStatus()))
    }
}
