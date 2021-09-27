package com.muiezarif.kidsfitness.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.muiezarif.kidsfitness.R
import com.muiezarif.kidsfitness.models.IntentParams
import com.muiezarif.kidsfitness.utils.*
import kotlinx.android.synthetic.main.activity_child_login_register.*

class ChildLoginRegisterActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_child_login_register)
        setListeners()
    }

    private fun setListeners(){
        cvStudentLogin.setOnClickListener(this)
        cvStudentRegister.setOnClickListener(this)
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