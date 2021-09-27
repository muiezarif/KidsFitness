package com.muiezarif.kidsfitness.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.muiezarif.kidsfitness.R
import com.muiezarif.kidsfitness.utils.navigate
import kotlinx.android.synthetic.main.activity_select_user_type.*

class SelectUserTypeActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_user_type)
        setListeners()
    }
    private fun setListeners(){
        cvStudent.setOnClickListener(this)
        cvCoach.setOnClickListener(this)
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
}