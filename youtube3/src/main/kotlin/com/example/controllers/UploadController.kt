package com.example.controllers


import com.example.services.UploadVideoService
import io.micronaut.http.HttpResponse

import io.micronaut.http.MediaType.MULTIPART_FORM_DATA
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Part
import io.micronaut.http.annotation.Post
import io.micronaut.http.multipart.StreamingFileUpload
import reactor.core.publisher.Mono


@Controller
class UploadController(
    private val uploadVideoService: UploadVideoService
) {
    @Post(value = "/video", consumes = [MULTIPART_FORM_DATA])
    fun upload(@Part file: StreamingFileUpload): Mono<HttpResponse<String>> = uploadVideoService.uploadVideo(file)
}