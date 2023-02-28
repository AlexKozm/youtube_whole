package com.example.controllers

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

@Controller("/health_check")
class HealthController {
    @Get
    fun health() = "Server is working".also { println("health checking") }
}