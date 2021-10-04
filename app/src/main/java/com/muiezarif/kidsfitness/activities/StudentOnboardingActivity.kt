package com.muiezarif.kidsfitness.activities

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import com.muiezarif.kidsfitness.ApplicationClass
import com.muiezarif.kidsfitness.R
import com.muiezarif.kidsfitness.utils.Constants
import com.muiezarif.kidsfitness.utils.SharedPrefsHelper
import com.muiezarif.kidsfitness.utils.navigate
import kotlinx.android.synthetic.main.activity_student_onboarding.*
import java.util.*
import javax.inject.Inject

class StudentOnboardingActivity : AppCompatActivity(), View.OnClickListener {
    @Inject
    lateinit var sharedPrefsHelper: SharedPrefsHelper
    private val arraySpinnerLevel = arrayOf("level-1", "level-2", "level-3")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ApplicationClass.getAppComponent(this).doInjection(this)
        setContentView(R.layout.activity_student_onboarding)
        btnStudentOnboardingDone.setOnClickListener(this)
        init()
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
    private fun init(){
        val adapterLevel: ArrayAdapter<String>? = this.let {
            ArrayAdapter(it, android.R.layout.simple_spinner_item, arraySpinnerLevel)
        }
        sStudentLevel.adapter = adapterLevel
        adapterLevel?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    }
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnStudentOnboardingDone ->{
                sharedPrefsHelper.put(Constants.sp_child_level_slug, sStudentLevel.selectedItem.toString())
                when (intent?.getStringExtra("type")) {
                    Constants.STUDENT_CHANGE_LEVEL -> {
                        navigate<ChildHomeActivity>(finish = true)
                    }
                    else ->{
                        navigate<ChildHomeActivity>(finishAll = true)
                    }
                }
            }
        }
    }
}