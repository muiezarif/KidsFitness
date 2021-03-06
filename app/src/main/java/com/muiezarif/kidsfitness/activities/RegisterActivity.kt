package com.muiezarif.kidsfitness.activities

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.muiezarif.kidsfitness.ApplicationClass
import com.muiezarif.kidsfitness.R
import com.muiezarif.kidsfitness.activities.viewmodels.LoginViewModel
import com.muiezarif.kidsfitness.activities.viewmodels.RegisterViewModel
import com.muiezarif.kidsfitness.models.IntentParams
import com.muiezarif.kidsfitness.network.api.ApiResponse
import com.muiezarif.kidsfitness.network.api.Status
import com.muiezarif.kidsfitness.network.response.LoginResponse
import com.muiezarif.kidsfitness.network.response.RegisterResponse
import com.muiezarif.kidsfitness.utils.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class RegisterActivity : AppCompatActivity(), View.OnClickListener {
    @Inject
    lateinit var sharedPrefsHelper: SharedPrefsHelper

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var registerViewModel: RegisterViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ApplicationClass.getAppComponent(this).doInjection(this)
        setContentView(R.layout.activity_register)
        setupViewModel()
        setListeners()
        getIntentData()
        loadLocale()
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
    private fun setupViewModel() {
        registerViewModel =
            ViewModelProvider(this, viewModelFactory).get(RegisterViewModel::class.java)
        registerViewModel.registerResponse()
            .observe(this, Observer<ApiResponse<RegisterResponse>> { t ->
                consumeResponse(t)
            })
    }

    private fun consumeResponse(apiResponse: ApiResponse<RegisterResponse>?) {
        when (apiResponse?.status) {
            Status.LOADING -> pbRegister.visibility = View.VISIBLE
            Status.SUCCESS -> {
                pbRegister.visibility = View.GONE
                renderSuccessResponse(apiResponse.data as RegisterResponse)
            }
            Status.ERROR -> {
                pbRegister.visibility = View.GONE
                toast(this, apiResponse.error.toString())
            }
            else -> {

            }
        }
    }

    private fun renderSuccessResponse(response: RegisterResponse) {
        if (response.status) {
            try {
                when (intent?.getStringExtra("type")) {
                    Constants.COACH_LOGIN -> {
                        val params = ArrayList<IntentParams>().apply {
                            add(IntentParams("type", Constants.COACH_LOGIN))
                            add(IntentParams("user", "Coach"))
                        }
                        navigate<LoginActivity>(params = params,finish = true)
                    }
                    else -> {
                        val params = ArrayList<IntentParams>().apply {
                            add(IntentParams("type", Constants.STUDENT_LOGIN))
                            add(IntentParams("user", "Student"))
                        }
                        navigate<LoginActivity>(params = params,finish = true)
                    }
                }
            } catch (e: Exception) {
                logE("EXCEPTION" + e.message)
            }
        } else {
            toast(this, response.message)
        }
    }

    private fun logE(message: String) {
        D.e("Login Activity", message)
    }

    private fun setListeners() {
        btnRegister.setOnClickListener(this)
    }

    private fun getIntentData() {
        when (intent?.getStringExtra("type")) {
            Constants.COACH_REGISTER -> {
                tvRegisterHeading.setText(resources.getString(R.string.coach_register))
            }
            Constants.STUDENT_REGISTER -> {
                tvRegisterHeading.setText(resources.getString(R.string.student_register))
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnRegister -> {
                if (!etRegisterEmail.text.toString().isNullOrEmpty()) {
                    if (!etRegisterUsername.text.toString().isNullOrEmpty()) {
                        if (!etRegisterUserAge.text.toString().isNullOrEmpty()) {
                            if (!etRegisterPassword.text.toString()
                                    .isNullOrEmpty() || !etRegisterConfirmPassword.text.toString()
                                    .isNullOrEmpty()
                            ) {
                                if (etRegisterPassword.text.toString() == etRegisterConfirmPassword.text.toString()) {
                                    when (intent?.getStringExtra("type")) {
                                        Constants.COACH_REGISTER -> {
                                            var params = mapOf(
                                                "email" to etRegisterEmail.text.toString().trim(),
                                                "username" to etRegisterUsername.text.toString().trim(),
                                                "password" to etRegisterPassword.text.toString().trim(),
                                                "age" to etRegisterUserAge.text.toString().trim()
                                            )
                                            registerViewModel.hitCoachRegisterApi(params, "")
                                        }
                                        Constants.STUDENT_REGISTER -> {
                                            var params = mapOf(
                                                "email" to etRegisterEmail.text.toString().trim(),
                                                "username" to etRegisterUsername.text.toString().trim(),
                                                "password" to etRegisterPassword.text.toString().trim(),
                                                "age" to etRegisterUserAge.text.toString().trim()
                                            )
                                            registerViewModel.hitRegisterApi(params, "")
                                        }
                                    }
                                } else {
                                    toast(this,"Password Does not match")
                                }
                            } else {
                                toast(this,"Please enter password")
                            }
                        } else {
                            toast(this,"Please enter age")
                        }
                    } else {
                        toast(this,"Please enter username")
                    }
                } else {
                    toast(this,"Please enter email")
                }
            }
        }
    }
}