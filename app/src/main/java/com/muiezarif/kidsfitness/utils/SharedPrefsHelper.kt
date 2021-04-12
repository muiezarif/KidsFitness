package com.muiezarif.kidsfitness.utils

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPrefsHelper @Inject
constructor(private val mSharedPreferences: SharedPreferences) {

    private val KEY_FCM_TOKEN = "key_agrareno_fcm_token"
    private val KEY_IS_NEW_FCM = "key_agrareno_is_new_FCM"
    private val KEY_SESSION_TOKEN = "key_agrareno_session_token"
    private val KEY_VERSION_NAME = "key_agrareno_version_name"
    private val KEY_IS_LOGIN = "is_logged_in_ec_dashboard"
    private val KEY_USER_DATA = "key_sports_app_user_data"
    private val KEY_GET_USER = "key_sports_app_get_user"

    fun put(key: String, value: String) {
        mSharedPreferences.edit().putString(key, value).apply()
    }

    fun put(key: String, value: Int) {
        mSharedPreferences.edit().putInt(key, value).apply()
    }

    fun put(key: String, value: Float) {
        mSharedPreferences.edit().putFloat(key, value).apply()
    }

    fun put(key: String, value: Boolean) {
        mSharedPreferences.edit().putBoolean(key, value).apply()
    }

    operator fun get(key: String, defaultValue: String): String? {
        return mSharedPreferences.getString(key, defaultValue)
    }

    operator fun get(key: String, defaultValue: Int): Int? {
        return mSharedPreferences.getInt(key, defaultValue)
    }

    operator fun get(key: String, defaultValue: Float): Float? {
        return mSharedPreferences.getFloat(key, defaultValue)
    }

    operator fun get(key: String, defaultValue: Boolean): Boolean {
        return mSharedPreferences.getBoolean(key, defaultValue)
    }

    fun deleteSavedData(key: String) {
        mSharedPreferences.edit().remove(key).apply()
    }
    fun clearData(){
        mSharedPreferences.edit().clear().apply()
    }
    private fun delete(key: String) {
        if (mSharedPreferences.contains(key)) {
            mSharedPreferences.edit().remove(key).apply()
        }
    }

    companion object {
        var PREF_KEY_ACCESS_TOKEN = "access-fcmToken"
    }


    fun isLoggedIn(): Boolean {
        return get(KEY_IS_LOGIN, false) ?: false
    }

    fun userLoggedIn(isLoggedIn: Boolean){
        put(KEY_IS_LOGIN, isLoggedIn)
    }


    fun saveIsNewFcmToken(isNewToken: Boolean) {
        put(KEY_IS_NEW_FCM, isNewToken)
    }

    fun isNewFcmToken(): Boolean {
        return get(KEY_IS_NEW_FCM, false)
    }

    fun saveFcmToken(fcmToken: String) {
        put(KEY_FCM_TOKEN, fcmToken)
    }

    fun getFcmToken(): String? {
        return get(KEY_FCM_TOKEN, "")
    }

    fun saveToken(fcmToken: String) {
        put(KEY_SESSION_TOKEN, fcmToken)
    }

    fun getToken(): String? {
        return get(KEY_SESSION_TOKEN, "")
    }

    fun saveAppVersion(appVersion: String) {
        put(KEY_VERSION_NAME, appVersion)
    }

    fun saveUserData(userDataString: String){
        put(KEY_USER_DATA, userDataString)
    }

    fun getUserData(): String? {
        return get(KEY_USER_DATA, "")
    }

//    fun getUser(): SignInData? {
//        val json= getUserData().toString()
//        val gson = Gson()
//        val obj = gson.fromJson(json, SignInData::class.java)
//        return obj
//    }

    fun getAppVersion(): String? {
        return get(KEY_VERSION_NAME, "")
    }

    private fun has(key: String): Boolean {
        return mSharedPreferences.contains(key)
    }

}