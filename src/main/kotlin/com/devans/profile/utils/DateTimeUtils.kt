package com.devans.profile.utils

import java.time.Instant

fun getCurrentEpochMilliSecond(): Long =
    Instant.now().toEpochMilli()