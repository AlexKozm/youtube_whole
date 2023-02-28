package com.example.model.dtos

import com.example.model.tables.OriginalVideoFileEntity
import kotlinx.serialization.Serializable

@Serializable
class RabbitDTO (
    val id: Int,
    val name: String,
    val extension: String
) {
    constructor(entity: OriginalVideoFileEntity) : this(entity.id, entity.name, entity.extension)
}