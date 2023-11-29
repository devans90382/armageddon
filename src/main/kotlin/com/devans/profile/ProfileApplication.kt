package com.devans.profile

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.devans.profile"])
class ProfileApplication

fun main(args: Array<String>) {
	runApplication<ProfileApplication>(*args)
}
