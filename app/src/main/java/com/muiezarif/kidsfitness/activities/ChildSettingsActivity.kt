package com.muiezarif.kidsfitness.activities

import android.app.Activity
import android.content.DialogInterface
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.muiezarif.kidsfitness.ApplicationClass
import com.muiezarif.kidsfitness.R
import com.muiezarif.kidsfitness.utils.Constants
import com.muiezarif.kidsfitness.utils.SharedPrefsHelper
import kotlinx.android.synthetic.main.activity_child_settings.*
import java.util.*
import javax.inject.Inject

class ChildSettingsActivity : AppCompatActivity() {
    @Inject
    lateinit var sharedPrefsHelper: SharedPrefsHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ApplicationClass.getAppComponent(this).doInjection(this)
        setContentView(R.layout.activity_child_settings)
        rlChangeLanguage.setOnClickListener {
            showChangeLang()
        }
        ivChildSettingsBack.setOnClickListener {
            finish()
        }
    }
    private fun showChangeLang() {
        val listItems = arrayOf("English", "Chinese", "Deutsch")
        val mBuilder = AlertDialog.Builder(this@ChildSettingsActivity)
        mBuilder.setTitle(resources.getString(R.string.choose_language_string))
        mBuilder.setSingleChoiceItems(
            listItems,
            -1,
            DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    0 -> {
                        setLocale("en")
                        ActivityCompat.recreate(this as Activity)
                    }
                    1 -> {
                        setLocale("zh")
                        ActivityCompat.recreate(this as Activity)
                    }
                    2 -> {
                        setLocale("de")
                        ActivityCompat.recreate(this as Activity)
                    }
                    else -> {
                        setLocale("en")
                        ActivityCompat.recreate(this as Activity)
                    }
                }
//            dialog.dismiss()
            })?.setNeutralButton(this?.resources?.getString(R.string.ok)) { dialog, i ->
            dialog.dismiss()
        }
        mBuilder?.create()?.show()
    }
    private fun setLocale(lang: String) {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        resources.updateConfiguration(config, resources.displayMetrics)
        sharedPrefsHelper.put(Constants.sp_language, lang)
    }

    private fun loadLocale() {
        ActivityCompat.recreate(this as Activity)
        sharedPrefsHelper[Constants.sp_language, ""]?.let { setLocale(it) }
    }
}