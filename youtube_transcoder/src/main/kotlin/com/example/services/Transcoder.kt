package com.example.services

import com.example.Strings.FilesPaths
import com.example.model.dtos.RabbitDTO
import com.example.model.tables.DecodedVideoFileEntity
import com.example.repositories.DecodedVideoFileDAO
import com.example.repositories.OriginalVideoFileDAO
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory
import ws.schild.jave.Encoder
import ws.schild.jave.MultimediaObject
import ws.schild.jave.ScreenExtractor
import ws.schild.jave.encode.AudioAttributes
import ws.schild.jave.encode.EncodingAttributes
import ws.schild.jave.encode.VideoAttributes
import ws.schild.jave.info.VideoSize
import java.io.File


@Singleton
class Transcoder (
    private val decodedVideoFileDAO: DecodedVideoFileDAO,
    private val originalVideoFileDAO: OriginalVideoFileDAO
) {
    private val encoder = Encoder()
    private val log = LoggerFactory.getLogger(javaClass)

    init {
        encoder.supportedEncodingFormats.forEach { println(it) }
    }

    private fun encodeToMP4(source: File, target: File, videoSize: VideoSize?) {
        val audio = AudioAttributes().setCodec(null)
        val video = VideoAttributes().setBitRate(null).setFrameRate(30)
            .setSize(videoSize)
        val attrs = EncodingAttributes().setAudioAttributes(audio).setVideoAttributes(video)
            .setOutputFormat("mp4")
        log.info("Decoding")
        encoder.encode(MultimediaObject(source), target, attrs, null)
//                log.info(MultimediaObject(source).info.video.toString())
        log.info("Decoded")
    }

    /**
     * @return true if encoding copleted without errors
     */
    private fun encodeFile(rabbitDTO: RabbitDTO, source: File, target: File, videoSize: VideoSize): Boolean {
        if (rabbitDTO.extension in encoder.supportedEncodingFormats) {
            try {
                log.info("Setting encode info")
                encodeToMP4(source, target, videoSize)
                getPhoto(source, File(FilesPaths.PATH_TO_SCREENSHOTS), rabbitDTO)
                return true
            } catch (ex: Exception) {
                ex.stackTrace.forEach { log.error(it.toString()) }
            }
        }
        return false
    }

    fun transcode(rabbitDTO: RabbitDTO, videoSize: VideoSize) {
        val originalVideoFileEntity = originalVideoFileDAO.findById(rabbitDTO.id).get()
        if (originalVideoFileEntity.extension in encoder.supportedDecodingFormats) {
            val decodedVideoFileEntity = DecodedVideoFileEntity()
            decodedVideoFileEntity.originId = rabbitDTO.id
            decodedVideoFileDAO.save(decodedVideoFileEntity)
            val (source, target) = videoFileCreation(rabbitDTO, decodedVideoFileEntity)
            val encodeResult = encodeFile(rabbitDTO, source, target, videoSize)
            if (encodeResult) {
                decodedVideoFileEntity.resolution = videoSize.height
                decodedVideoFileDAO.update(decodedVideoFileEntity)
            } else {
                decodedVideoFileDAO.delete(decodedVideoFileEntity)
            }
        } else {
            log.info("Unsupported format for decoding: ${originalVideoFileEntity.extension}")
        }

    }

    private fun videoFileCreation(rabbitDTO: RabbitDTO, decodedVideoFileEntity: DecodedVideoFileEntity): Pair<File, File> {
        val source = File("${FilesPaths.PATH_TO_ORIGIN_FILES}/${rabbitDTO.id}.${rabbitDTO.extension}")
        decodedVideoFileEntity.extension = "mp4"
        val target = File("${FilesPaths.PATH_TO_ENCODED_FILES}/${decodedVideoFileEntity.id}.${decodedVideoFileEntity.extension}")
        target.createNewFile()
        log.info("source file: ${source.name}")
        log.info("target file: ${target.name}")
        return Pair(source, target)
    }



    fun getPhoto(source: File, target: File, rabbitDTO: RabbitDTO) {
        val multimediaObject = MultimediaObject(source)
        val durationInSeconds = multimediaObject.info.duration / 1000
        val screenExtractor = ScreenExtractor()
        screenExtractor.render(
            multimediaObject,
            -1, -1,
            (durationInSeconds / 3).toInt(),
            target,
            rabbitDTO.id.toString(),
            "png",
            1
        )
    }
}