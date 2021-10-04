package com.muiezarif.kidsfitness.activities

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.muiezarif.kidsfitness.ApplicationClass
import com.muiezarif.kidsfitness.R
import com.muiezarif.kidsfitness.models.IntentParams
import com.muiezarif.kidsfitness.utils.*
import kotlinx.android.synthetic.main.activity_child_login_register.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class ChildLoginRegisterActivity : AppCompatActivity(), View.OnClickListener {
    @Inject
    lateinit var sharedPrefsHelper: SharedPrefsHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ApplicationClass.getAppComponent(this).doInjection(this)
        setContentView(R.layout.activity_child_login_register)
        setListeners()
        loadLocale()
    }

    private fun setListeners(){
        cvStudentLogin.setOnClickListener(this)
        cvStudentRegister.setOnClickListener(this)
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
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.cvStudentLogin ->{
                val params = ArrayList<IntentParams>().apply {
                    add(IntentParams("type", Constants.STUDENT_LOGIN))
                    add(IntentParams("user", "Student"))
                }
                navigate<LoginActivity>(params = params,finish = false)
            }
            R.id.cvStudentRegister ->{
                val params = ArrayList<IntentParams>().apply {
                    add(IntentParams("type", Constants.STUDENT_REGISTER))
                    add(IntentParams("user", "Student"))
                }
                navigate<RegisterActivity>(params = params,finish = false)
            }
        }
    }
}