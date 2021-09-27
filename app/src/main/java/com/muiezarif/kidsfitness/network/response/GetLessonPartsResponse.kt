package com.muiezarif.kidsfitness.network.response

data class GetLessonPartsResponse(
    val message: String,
    val result: List<GetLessonPartsResult>,
    val status: Boolean
)

data class GetLessonPartsResult(
    val is_completed: Boolean,
    val lesson_video_completed: Boolean,
    val video_name: String,
    val video_name_chinese: String,
    val video_name_german: String,
    val video_slug: String,
    val video_url: String
)