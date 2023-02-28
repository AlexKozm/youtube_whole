package com.example.model.dtos

import com.example.model.tables.DecodedVideoFileEntity
import com.example.model.tables.OriginalVideoFileEntity
import java.io.Serializable

class VideoInfoDTO(
    val id: Int,
    val name: String,
    val resolution: Int
) : Serializable {
    constructor(video: DecodedVideoFileEntity, originalVideo: OriginalVideoFileEntity) : this(
        video.id, "${originalVideo.name}.${video.extension}", video.resolution
    )
}