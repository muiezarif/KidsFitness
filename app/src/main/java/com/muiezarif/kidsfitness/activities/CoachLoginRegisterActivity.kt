package com.muiezarif.kidsfitness.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.muiezarif.kidsfitness.R
import com.muiezarif.kidsfitness.models.IntentParams
import com.muiezarif.kidsfitness.utils.Constants
import com.muiezarif.kidsfitness.utils.navigate
import kotlinx.android.synthetic.main.activity_coach_login_register.*

class CoachLoginRegisterActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coach_login_register)
        setListeners()
    }

    private fun setListeners(){
        cvCoachLogin.setOnClickListener(this)
        cvCoachRegister.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.cvCoachLogin ->{
                val params = ArrayList<IntentParams>().apply {
                    add(IntentParams("type", Constants.COACH_LOGIN))
                    add(IntentParams("user", "Coach"))
                }
                navigate<LoginActivity>(params = params,finish = false)
            }
            R.id.cvCoachRegister ->{
                val params = ArrayList<IntentParams>().apply {
                    add(IntentParams("type", Constants.COACH_REGISTER))
                    add(IntentParams("user", "Coach"))
                }
                navigate<RegisterActivity>(params = params,finish = false)
            }
        }
    }
}