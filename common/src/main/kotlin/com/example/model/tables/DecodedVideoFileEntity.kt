package com.example.model.tables

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
//@Table(name = "decoded_video_files") <- it superfluous because it's automatically create this name
@JsonSerialize
class DecodedVideoFileEntity() : AbstractEntity() {
    @Column(name = "extension")
    var extension = ""
    @Column(name = "original_video_id")
    var originId: Int = 0
    @Column(name = "resolution")
    var resolution: Int = 0
    constructor(extension: String, originId: Int, resolution: Int) : this() {
        this.extension = extension
        this.originId = originId
        this.resolution = resolution
    }
}