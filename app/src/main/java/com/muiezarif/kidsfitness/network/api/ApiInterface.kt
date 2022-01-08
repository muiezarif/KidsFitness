package com.muiezarif.kidsfitness.network.api

import com.muiezarif.kidsfitness.network.response.*
import io.reactivex.Observable
import okhttp3.MultipartBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiInterface {

    @POST(Urls.SIGN_IN)
    fun SignIn(@Body params: Map<String, String>): Observable<LoginResponse>
    @POST(Urls.REGISTER_USER_STUDENT)
    fun RegisterStudent(@Body params: Map<String, String>): Observable<RegisterResponse>
    @POST(Urls.REGISTER_USER_COACH)
    fun RegisterCoach(@Body params: Map<String, String>): Observable<RegisterResponse>
    @POST(Urls.LESSON_COMPLETED)
    fun StudentLessonCompleted(@Header("Authorization") token: String?,@QueryMap params: Map<String, String>): Observable<Void>
    @POST(Urls.LESSON_PART_COMPLETED)
    fun StudentLessonPartCompleted(@Header("Authorization") token: String?,@QueryMap params: Map<String, String>): Observable<Void>
    @GET(Urls.GET_CATEGORIES)
    fun GetCategories():Observable<GetChildCategoryResponse>
    @GET(Urls.GET_LESSONS)
    fun GetCategoryLessons(@Header("Authorization") token: String?,@Path("category_slug") category_slug:String?,@Path("level_type") level_type:String?):Observable<GetCategoryLessonsResponse>
    @GET(Urls.GET_LESSON_PARTS)
    fun GetCategoryLessonParts(@Header("Authorization") token: String?,@Path("category_slug") category_slug:String?):Observable<GetLessonPartsResponse>
    @GET(Urls.GET_LESSON_CHAPTERS)
    fun GetLessonChapters(@Header("Authorization") token: String?,@Path("lesson_slug") category_slug:String?):Observable<GetLessonChaptersResponse>
    @PATCH(Urls.UPDATE_USER_INFO)
    fun UpdateUserInfo(@Header("Authorization") token: String?,@Path("id") id:Int?,@Body params: Map<String, String?>):Observable<UpdateUserResponse>
    @POST(Urls.CREATE_SUBSCRIPTION)
    fun UserSubscription(@Header("Authorization") token: String?,@Body params: Map<String, String>?): Observable<SubscriptionResponse>
}