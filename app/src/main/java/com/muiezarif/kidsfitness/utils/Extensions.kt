package com.muiezarif.kidsfitness.utils

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.muiezarif.kidsfitness.models.IntentParams

inline fun <reified T : Activity> Activity.navigate(
    params: List<IntentParams> = emptyList(), finish: Boolean = true, finishAll:Boolean = false, sfr:Boolean = false,
    anim: String = ""
) {
    val intent = Intent(this, T::class.java)
    for (parameter in params) {
        parameter.value?.let {
            when (it) {
                is Boolean -> {
                    intent.putExtra(parameter.key, it)
                }
                is Double -> {
                    intent.putExtra(parameter.key, it)
                }
                is Float -> {
                    intent.putExtra(parameter.key, it)
                }
                is Int -> {
                    intent.putExtra(parameter.key, it)
                }
                else -> {
                    intent.putExtra(parameter.key, it as String)
                }
            }
        }
    }
    if (finish) {
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    if (finishAll){
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
    }
    if (sfr) {
        startActivityForResult(intent,1010)
    }else{
        startActivity(intent)
    }
    if (finish) {
        finish()
    }
    //    if (anim.equals(SLIDE_RIGHT)) {
    //        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    //    }
}
inline fun <reified T : Activity> Fragment.navigate(
    params: List<IntentParams> = emptyList(), finish: Boolean = true, finishAll:Boolean = false,
    anim: String = ""
) {
    val intent = Intent(context, T::class.java)
    for (parameter in params) {
        parameter.value?.let {
            when (it) {
                is Boolean -> {
                    intent.putExtra(parameter.key, it)
                }
                is Double -> {
                    intent.putExtra(parameter.key, it)
                }
                is Float -> {
                    intent.putExtra(parameter.key, it)
                }
                is Int -> {
                    intent.putExtra(parameter.key, it)
                }
                else -> {
                    intent.putExtra(parameter.key, it as String)
                }
            }
        }
    }
    if (finish) {
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    if (finishAll){
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
    }
    startActivity(intent)
//    if (finish) {
//        finish()
//    }
    //    if (anim.equals(SLIDE_RIGHT)) {
    //        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    //    }
}

fun Activity.showToast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}
