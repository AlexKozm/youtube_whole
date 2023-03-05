package com.example.model.tables

import java.io.Serializable
import javax.persistence.*

@MappedSuperclass
abstract class AbstractEntity : Serializable {
    @Id
    @Column(nullable = false, name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0
        private set
    //may be it was useful to add creation time
}