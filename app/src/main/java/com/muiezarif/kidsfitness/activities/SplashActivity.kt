package com.muiezarif.kidsfitness.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.muiezarif.kidsfitness.ApplicationClass
import com.muiezarif.kidsfitness.R
import com.muiezarif.kidsfitness.utils.SharedPrefsHelper
import com.muiezarif.kidsfitness.utils.navigate
import javax.inject.Inject

class SplashActivity : AppCompatActivity() {
    @Inject
    lateinit var sharedPrefsHelper: SharedPrefsHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        ApplicationClass.getAppComponent(this).doInjection(this)
        Handler().postDelayed(Runnable {
            if(sharedPrefsHelper.isLoggedIn()){
                navigate<SelectCategoryActivity>(finish = true)
            }else {
                navigate<SelectUserTypeActivity>(finish = true)
            }
//            navigate<SelectUserTypeActivity>(finish = true)
        },  3000)
    }
}