package com.devans.profile.exception

enum class ProfileErrorCodes(val code: String): Error {
    PROFILE_NOT_FOUND("PROFILE_NOT_FOUND") {
        override fun getDescription(): String {
            return "No profile found for given Id"
        }

        override fun getHTTPStatus(): Int {
            return 404
        }

        override fun getErrorCode(): String {
            return code
        }
    },

    PROFILE_UPDATE_FORBIDDEN("PROFILE_UPDATE_FORBIDDEN") {
        override fun getDescription(): String {
            return "Profile update forbidden"
        }

        override fun getHTTPStatus(): Int {
            return 403
        }

        override fun getErrorCode(): String {
            return code
        }
    },

    PROFILE_UNHANDLED_EXCEPTION("PROFILE_UNHANDLED_EXCEPTION"){
        override fun getDescription(): String {
            return "Unhandled exception"
        }

        override fun getHTTPStatus(): Int {
            return 503
        }

        override fun getErrorCode(): String {
            return code
        }
    }
}