package com.example.controllers

import com.example.services.DownloadVideoService
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.QueryValue

@Controller
class DownloadVideoController(
    private val downloadVideoService: DownloadVideoService
) {
    @Get("/decoded_video")
    fun getDecodedVideo(@QueryValue id: Int) = downloadVideoService.getDecodedVideo(id)
}