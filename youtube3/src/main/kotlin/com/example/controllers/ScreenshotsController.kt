package com.example.controllers

import com.example.services.ScreenshotsService
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.QueryValue


@Controller
class ScreenshotsController (
    private val screenshotsService: ScreenshotsService
) {
    @Get("/screenshots")
    fun getScreenshot(@QueryValue id: Int, @QueryValue num: Int)
        = screenshotsService.getScreen(id, num)
}