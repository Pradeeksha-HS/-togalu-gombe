package com.togalugombe.data.model

data class Play(
    val id: String,
    val title: String,
    val subtitle: String,
    val cover: Int,
    val scenes: List<Scene>
)