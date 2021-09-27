package com.muiezarif.kidsfitness.network.response

//class GetChildCategoryResponse : ArrayList<GetChildCategoryResponseItem>()
//
//data class GetChildCategoryResponseItem(
//    val category_image: String,
//    val category_name: String,
//    val category_slug: String
//)

data class GetChildCategoryResponse(
    val message: String,
    val result: List<GetChildCategoryResponseItem>,
    val status: Boolean
)

data class GetChildCategoryResponseItem(
    val category_image: String,
    val category_name: String,
    val category_chinese: String,
    val category_german: String,
    val category_slug: String
)