package com.devans.profile.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthController {

    @GetMapping("/profiles/v1/health")
    fun checkHealth(): String = "Hi, my profile is healthy"
}
