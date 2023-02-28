package com.example.model.tables

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "original_video_files")
@JsonSerialize
class OriginalVideoFileEntity() : AbstractEntity(){
    @Column(name = "file_names")
    var name = ""
    @Column(name = "extension")
    var extension = ""
    constructor(name: String, extension: String) : this() {
        this.name = name
        this.extension = extension
    }
}