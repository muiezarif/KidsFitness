package com.muiezarif.kidsfitness.network.api

class Urls {
    companion object{
        const val BASE_URL = "http://testsportsapp.pythonanywhere.com/api/v1/"

        const val REGISTER_USER_STUDENT = "account/create/student/"//put your end point here
        const val SIGN_IN = "account/login/"
        const val LESSON_COMPLETED = "lesson/{id}/completed/"
        const val LESSON_PART_COMPLETED = "lesson/video/{id}/completed/"
        const val GET_LESSONS = "lesson/{category_slug}/{level_type}/"
        const val GET_LESSON_PARTS = "lesson/{category_slug}/video/"
        const val GET_CATEGORIES = "category/"
    }
}