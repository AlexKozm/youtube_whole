package com.example.services

import com.example.Strings.FilesPaths
import com.example.repositories.DecodedVideoFileDAO
import com.example.repositories.OriginalVideoFileDAO
import io.micronaut.http.server.types.files.SystemFile
import jakarta.inject.Singleton
import java.io.File


@Singleton
class DownloadVideoService(
    private val decodedVideoFileDAO: DecodedVideoFileDAO,
    private val originalVideoFileDAO: OriginalVideoFileDAO
) {
    fun getDecodedVideo(id: Int): SystemFile? {
        return try {
            val entity = decodedVideoFileDAO.findById(id).get()
            val originEntity = originalVideoFileDAO.findById(entity.originId).get()
            val file = File("${FilesPaths.PATH_TO_ENCODED_FILES}/${entity.id}.${entity.extension}")
    //        val stream: InputStream = FileInputStream(file)
    //        return StreamedFile(stream, MediaType.ALL_TYPE)
            SystemFile(file).attach("${originEntity.name}.${entity.extension}")
        } catch (exception: NoSuchElementException) {
            null
        }
    }
}