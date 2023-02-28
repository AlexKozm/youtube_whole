package com.example.controllers

import com.example.services.VideosInfoService
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.QueryValue

@Controller
class VideoInfoController(
    private val videosInfoService: VideosInfoService
) {
    @Get("/videos")
    fun videosInfo() = videosInfoService.getVideosInfo()

    @Get("/video")
    fun getVideo(@QueryValue id: Int) = videosInfoService.getVideoInfo(id)
}