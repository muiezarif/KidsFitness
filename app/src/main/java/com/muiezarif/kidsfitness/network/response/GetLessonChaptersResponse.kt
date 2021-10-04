package com.muiezarif.kidsfitness.network.response

data class GetLessonChaptersResponse(
    val message: String,
    val result: List<GetLessonChaptersResult>,
    val status: Boolean
)

data class GetLessonChaptersResult(
    val chapter_chinese_description: String,
    val chapter_description: String,
    val chapter_german_description: String,
    val chapter_slug: String,
    val chapter_title: String,
    val chapter_title_chinese: String,
    val chapter_title_german: String,
    val id: Int
)