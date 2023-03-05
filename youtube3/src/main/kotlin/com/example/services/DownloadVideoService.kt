package com.example.services

import com.example.Strings.FilesPaths
import com.example.repositories.DecodedVideoFileDAO
import com.example.repositories.OriginalVideoFileDAO
import io.micronaut.http.server.types.files.SystemFile
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory
import java.io.File
import kotlin.math.log


@Singleton
class DownloadVideoService(
    private val decodedVideoFileDAO: DecodedVideoFileDAO,
    private val originalVideoFileDAO: OriginalVideoFileDAO
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    //It's better to use runCatching in this case I think
    fun getDecodedVideo(id: Int): Result<SystemFile> = runCatching {
            logger.info("Trying to get video with id: $id")

            val entity = decodedVideoFileDAO.findById(id).get()
            val originEntity = originalVideoFileDAO.findById(entity.originId).get()
            val file = File("${FilesPaths.PATH_TO_ENCODED_FILES}/${entity.id}.${entity.extension}")
    //        val stream: InputStream = FileInputStream(file)
    //        return StreamedFile(stream, MediaType.ALL_TYPE)
            SystemFile(file).attach("${originEntity.name}.${entity.extension}")
        }
            .onFailure {
            logger.error("There is an exception while getting the video: ${it.printStackTrace()}")
        }
}