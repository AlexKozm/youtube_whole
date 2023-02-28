package com.example.model.dtos

import com.example.model.tables.OriginalVideoFileEntity
import java.io.Serializable

class VideoIdNameDTO(
    val id: Int,
    val name: String
) : Serializable {
    constructor(entity: OriginalVideoFileEntity) : this(entity.id, "${entity.name}.${entity.extension}")
}