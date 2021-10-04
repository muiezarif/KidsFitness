package com.muiezarif.kidsfitness.network.api

class Urls {
    companion object{
        const val BASE_URL = "http://178.128.204.185/en/api/v1/"

        const val REGISTER_USER_STUDENT = "account/create/student/"//put your end point here
        const val REGISTER_USER_COACH = "account/create/coach/"//put your end point here
        const val SIGN_IN = "account/login/"
        const val LESSON_COMPLETED = "lesson/{id}/completed/"
        const val LESSON_PART_COMPLETED = "lesson/video/{id}/completed/"
        const val GET_LESSONS = "fetch-lesson/{category_slug}/{level_type}/"
        const val GET_LESSON_PARTS = "lesson/{category_slug}/video/"
        const val GET_CATEGORIES = "category/"
        const val GET_LESSON_CHAPTERS = "chapter/{lesson_slug}"
    }
}