package com.devans.profile.exception

open class ProfileException(
    open val error: Error,
    override val message: String? = null,
) : RuntimeException(message ?: error.getDescription())

interface Error {
    fun getDescription(): String
    fun getHTTPStatus(): Int
    fun getErrorCode(): String
}
