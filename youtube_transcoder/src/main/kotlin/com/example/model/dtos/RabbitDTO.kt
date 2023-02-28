package com.example.model.dtos

import java.io.Serializable


class RabbitDTO (
    val id: Int,
    val name: String,
    val extension: String
)  : Serializable {
    constructor() : this(0, "", "")
}