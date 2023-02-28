package com.example.repositories

import com.example.model.tables.DecodedVideoFileEntity
import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository

@Repository
interface DecodedVideoFileDAO : CrudRepository<DecodedVideoFileEntity, Int> {
    fun findByOriginId(id: Int): List<DecodedVideoFileEntity>
}