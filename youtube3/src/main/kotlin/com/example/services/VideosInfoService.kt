package com.example.services

import com.example.model.dtos.DecodedVideosDTO
import com.example.model.dtos.VideoIdNameDTO
import com.example.model.dtos.VideoInfoDTO
import com.example.model.dtos.VideosInfoDTO
import com.example.model.tables.DecodedVideoFileEntity
import com.example.repositories.DecodedVideoFileDAO
import com.example.repositories.OriginalVideoFileDAO
import io.micronaut.data.model.Pageable
import io.micronaut.http.HttpResponse
import io.micronaut.http.MutableHttpResponse
import jakarta.inject.Singleton

@Singleton
class VideosInfoService(
    private val originalVideoFileDAO: OriginalVideoFileDAO,
    private val decodedVideoFileDAO: DecodedVideoFileDAO
) {
    fun getVideosInfo(): MutableHttpResponse<VideosInfoDTO> {
        val videos: MutableList<VideoIdNameDTO> = mutableListOf()
        /*
         * we can just do:
         *  val videos = originalVideoFileDAO.findAll()
         * and in the VideosInfoDTO accept
         *  Iterable<OriginalVideoFileEntity>
         * */
        originalVideoFileDAO.findAll().forEach { videos += VideoIdNameDTO(it) }
        val ret = VideosInfoDTO(videos)
        return HttpResponse.ok(ret)
    }
    fun getVideoInfo(id: Int): MutableHttpResponse<DecodedVideosDTO> {
        return try {
            val originalVideo = originalVideoFileDAO.findById(id).get()
            val videos = mutableListOf<VideoInfoDTO>()
            decodedVideoFileDAO.findByOriginId(id).forEach { videos += VideoInfoDTO(it, originalVideo)}
            val ret = DecodedVideosDTO(videos)
            HttpResponse.ok(ret)
        } catch (ex: NoSuchElementException) {
            HttpResponse.notFound()
        }

    }

    //getting some count of videos on page which number is N
    fun getNthVideosInfo(numberOfPage: Int, countOfVideosInPage: Int): MutableList<DecodedVideoFileEntity> {
        return decodedVideoFileDAO.list(Pageable.from((numberOfPage - 1) * countOfVideosInPage, countOfVideosInPage)).content
    }
}