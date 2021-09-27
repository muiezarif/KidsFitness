package com.muiezarif.kidsfitness.network.repository

import com.muiezarif.kidsfitness.network.api.ApiInterface
import com.muiezarif.kidsfitness.network.response.*
import io.reactivex.Observable
import okhttp3.MultipartBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.http.Field
import retrofit2.http.Header
import retrofit2.http.Part
import retrofit2.http.Path

class Repository(private val apiCallInterface: ApiInterface) {

    private val BEARER = "jwt "

    fun executeSignIn(parameters: Map<String, String>): Observable<LoginResponse> {
        return apiCallInterface.SignIn(parameters)
    }
    fun executeRegister(parameters: Map<String, String>): Observable<RegisterResponse> {
        return apiCallInterface.RegisterStudent(parameters)
    }
    fun executeStudentLessonCompleted(token: String?,parameters: Map<String, String>): Observable<Void> {
        return apiCallInterface.StudentLessonCompleted(BEARER+token,parameters)
    }
    fun executeStudentLessonPartCompleted(token: String?,parameters: Map<String, String>): Observable<Void> {
        return apiCallInterface.StudentLessonPartCompleted(BEARER+token,parameters)
    }
    fun executeGetCategories(): Observable<GetChildCategoryResponse> {
        return apiCallInterface.GetCategories()
    }
    fun executeGetCategoriesLessons(token: String?, category_slug:String?, level_type:String?): Observable<GetCategoryLessonsResponse> {
        return apiCallInterface.GetCategoryLessons(token,category_slug,level_type)
    }
    fun executeGetCategoriesLessonsPart(token: String?,category_slug:String?): Observable<GetLessonPartsResponse> {
        return apiCallInterface.GetCategoryLessonParts(token,category_slug)
    }

}