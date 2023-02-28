package com.example.repositories

import com.example.model.tables.OriginalVideoFileEntity
import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository

@Repository
interface OriginalVideoFileDAO : CrudRepository<OriginalVideoFileEntity, Int> {
}