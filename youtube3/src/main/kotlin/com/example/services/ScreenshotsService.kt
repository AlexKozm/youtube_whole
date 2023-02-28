package com.example.services

import com.example.Strings.FilesPaths
import io.micronaut.http.MediaType
import io.micronaut.http.server.types.files.StreamedFile
import jakarta.inject.Singleton
import java.io.File
import java.io.FileInputStream

@Singleton
class ScreenshotsService {
    fun getScreen(id: Int, num: Int): StreamedFile {
        val file = File("${FilesPaths.PATH_TO_SCREENSHOTS}/$id-000$num.png")
        val stream = FileInputStream(file)
        return StreamedFile(stream, MediaType.IMAGE_PNG_TYPE)
    }
}