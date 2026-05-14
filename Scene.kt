package com.togalugombe.data.model

data class Scene(
    val num: Int,
    val title: String,
    val summary: String,
    val detail: String,
    val music: String = ""
)