package com.devans.profile.utils

import java.util.UUID

fun generateLongUniqueIdentifier() =
    UUID.randomUUID().toString()