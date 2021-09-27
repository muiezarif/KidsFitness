package com.muiezarif.kidsfitness.activities

import android.content.DialogInterface
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.muiezarif.kidsfitness.ApplicationClass
import com.muiezarif.kidsfitness.R
import com.muiezarif.kidsfitness.utils.Constants
import com.muiezarif.kidsfitness.utils.SharedPrefsHelper
import com.muiezarif.kidsfitness.utils.navigate
import kotlinx.android.synthetic.main.activity_select_user_type.*
import java.util.*
import javax.inject.Inject

class SelectUserTypeActivity : AppCompatActivity(), View.OnClickListener {
    @Inject
    lateinit var sharedPrefsHelper: SharedPrefsHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ApplicationClass.getAppComponent(this).doInjection(this)
        setContentView(R.layout.activity_select_user_type)
        if (sharedPrefsHelper[Constants.sp_language,""] == null || sharedPrefsHelper[Constants.sp_language,""] == "") {
            showChangeLang()
        }
        setListeners()
    }
    private fun setListeners(){
        cvStudent.setOnClickListener(this)
        cvCoach.setOnClickListener(this)
    }
    private fun showChangeLang(){
        val listItems = arrayOf("English","Chinese","Deutsch")
        val mBuilder = AlertDialog.Builder(this@SelectUserTypeActivity)
        mBuilder.setTitle(resources.getString(R.string.choose_language_string))
        mBuilder.setSingleChoiceItems(listItems,-1, DialogInterface.OnClickListener{ dialog, which ->
            when(which){
                0->{
                    setLocale("en")
                    recreate()
                }
                1->{
                    setLocale("zh")
                    recreate()
                }
                2->{
                    setLocale("de")
                    recreate()
                }
                else->{
                    setLocale("en")
                    recreate()
                }
            }
        })
        mBuilder.create().setCanceledOnTouchOutside(false)
        mBuilder.setCancelable(false)
        mBuilder.show()
    }
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.cvStudent ->{
                navigate<ChildLoginRegisterActivity>(finish = false)
            }
            R.id.cvCoach ->{
                navigate<CoachLoginRegisterActivity>(finish = false)
            }
        }
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
}