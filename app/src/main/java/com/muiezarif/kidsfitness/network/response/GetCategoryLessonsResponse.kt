package com.muiezarif.kidsfitness.network.response
//class GetCategoryLessonsResponse : ArrayList<GetCategoryLessonsResponseItem>()
//data class GetCategoryLessonsResponse(
//    val message: String,
//    val result: List<GetCategoryLessonsResponseItem>,
//    val status: Boolean
//)
//
//data class GetCategoryLessonsResponseItem(
//    val category: GetCategoryLessonsCategory,
//    val coach: Int,
//    val id: Int,
//    val lesson_completed: Boolean,
//    val lesson_image: String,
//    val lesson_level: String,
//    val lesson_slug: String,
//    val title: String
//)
//
//data class GetCategoryLessonsCategory(
//    val category_image: String,
//    val category_name: String,
//    val category_slug: String
//)
data class GetCategoryLessonsResponse(
    val message: String,
    val result: List<GetCategoryLessonsResponseItem>,
    val status: Boolean
)

data class GetCategoryLessonsResponseItem(
    val category: GetCategoryLessonsCategory,
    val coach: Int,
    val id: Int,
    val lesson_completed: Any,
    val lesson_image: String,
    val lesson_level: String,
    val lesson_slug: String,
    val title: String
)

data class GetCategoryLessonsCategory(
    val category_image: String,
    val category_name: String,
    val category_slug: String
)