package com.muiezarif.kidsfitness.activities

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.muiezarif.kidsfitness.ApplicationClass
import com.muiezarif.kidsfitness.R
import com.muiezarif.kidsfitness.activities.viewmodels.LoginViewModel
import com.muiezarif.kidsfitness.network.api.ApiResponse
import com.muiezarif.kidsfitness.network.api.Status
import com.muiezarif.kidsfitness.network.response.LoginResponse
import com.muiezarif.kidsfitness.utils.*
import com.muiezarif.kidsfitness.utils.Constants.COACH_LOGIN
import com.muiezarif.kidsfitness.utils.Constants.STUDENT_LOGIN
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*
import javax.inject.Inject

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    @Inject
    lateinit var sharedPrefsHelper: SharedPrefsHelper
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var loginViewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ApplicationClass.getAppComponent(this).doInjection(this)
        setContentView(R.layout.activity_login)
        setupViewModel()
        setListeners()
        getIntentData()
        loadLocale()
    }
    private fun setupViewModel(){
        loginViewModel = ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java)
        loginViewModel.signInResponse().observe(this, Observer<ApiResponse<LoginResponse>> { t ->
            consumeResponse(t)
        })
    }

    private fun consumeResponse(apiResponse: ApiResponse<LoginResponse>?) {
        when (apiResponse?.status) {
            Status.LOADING -> pbSignIn.visibility = View.VISIBLE
            Status.SUCCESS -> {
                pbSignIn.visibility = View.GONE
                renderSuccessResponse(apiResponse.data as LoginResponse)
            }
            Status.ERROR -> {
                pbSignIn.visibility = View.GONE
                toast(this, apiResponse.error.toString())
            }
            else -> {

            }
        }
    }

    private fun renderSuccessResponse(response: LoginResponse) {
        if (response.status) {
            try {
                when (intent?.getStringExtra("type")) {
                    COACH_LOGIN -> {
                        sharedPrefsHelper.put(Constants.sp_username, response.result.user.user.username)
                        sharedPrefsHelper.put(Constants.sp_token, response.result.user.token)
                        sharedPrefsHelper.put(Constants.sp_email, response.result.user.user.email)
                        sharedPrefsHelper.put(Constants.sp_uid, response.result.user.user.id)
                        sharedPrefsHelper.put(Constants.sp_userType, Constants.COACH_TYPE)
                        navigate<SelectCoachCategoriesActivity>(finish = true,finishAll = true)
                    }
                    STUDENT_LOGIN -> {
                        sharedPrefsHelper.userLoggedIn(true)
                        sharedPrefsHelper.put(Constants.sp_username, response.result.user.user.username)
                        sharedPrefsHelper.put(Constants.sp_token, response.result.user.token)
                        sharedPrefsHelper.put(Constants.sp_email, response.result.user.user.email)
                        sharedPrefsHelper.put(Constants.sp_uid, response.result.user.user.id)
                        sharedPrefsHelper.put(Constants.sp_userType, Constants.STUDENT_TYPE)
                        navigate<SelectCategoryActivity>(finish = true,finishAll = true)
                    }
                }

            } catch (e: Exception) {
                logE("EXCEPTION" + e.message)
            }
            logE(response.result.user.user.toString())
        }else{
            toast(this,response.message)
        }
    }

    private fun logE(message: String) {
        D.e("Login Activity", message)
    }

    private fun setListeners(){
        btnLogin.setOnClickListener(this)
    }
    private fun setLocale(lang:String){
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        resources.updateConfiguration(config,resources.displayMetrics)
        sharedPrefsHelper.put(Constants.sp_language,lang)
    }
    private fun loadLocale(){
        sharedPrefsHelper[Constants.sp_language, ""]?.let { setLocale(it) }
    }
    private fun getIntentData() {
        when (intent?.getStringExtra("type")) {
            COACH_LOGIN -> {
                tvLoginHeading.setText(resources.getString(R.string.coach_login))
            }
            STUDENT_LOGIN ->{
                tvLoginHeading.setText(resources.getString(R.string.student_login))
            }
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnLogin ->{
                if (!etLoginEmail.text.toString().isNullOrEmpty()) {
                    if (!etLoginPassword.text.toString().isNullOrEmpty()) {
                        when (intent?.getStringExtra("type")) {
                            COACH_LOGIN -> {
                                var params = mapOf(
                                    "email" to etLoginEmail.text.toString().trim(),
                                    "password" to etLoginPassword.text.toString().trim()
                                )
                                loginViewModel.hitSignInApi(params,"")

                            }
                            STUDENT_LOGIN -> {
                                var params = mapOf(
                                    "email" to etLoginEmail.text.toString().trim(),
                                    "password" to etLoginPassword.text.toString().trim()
                                )
                                loginViewModel.hitSignInApi(params,"")
                            }
                        }
                    }else{
                        toast(this,"Please add password")
                    }
                }else{
                    toast(this,"Please add email")
                }
            }
        }
    }
}