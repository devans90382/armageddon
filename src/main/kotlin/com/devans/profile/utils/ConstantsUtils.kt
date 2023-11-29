package com.devans.profile.utils

object CircuitBreakerNames {
    const val AccountingCircuitBreaker = "AccountingCircuitBreaker"
    const val PayrollCircuitBreaker = "PayrollCircuitBreaker"
    const val PaymentsCircuitBreaker = "PaymentsCircuitBreaker"
}

object ClientNames {
    const val AccountingClient = "AccountingClient"
    const val PayrollClient = "PayrollClient"
    const val PaymentsClient = "PaymentsClient"
}

object ConstantOutput {
    const val SUCCESSFULLY_UPDATED_PROFILE = "SUCCESSFULLY_UPDATED_PROFILE"
    const val SUCCESSFULLY_DELETED_PROFILE = "SUCCESSFULLY_DELETED_PROFILE"
    const val UNHANDLED_EXCEPTION = "Unhandled exception encountered"
    const val PROFILE_UPDATE_REJECTED = "Profile update request rejected"
}