package com.muiezarif.kidsfitness.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.muiezarif.kidsfitness.R
import com.muiezarif.kidsfitness.listeners.GenericAdapterCallback

class CoachHomeActivity : AppCompatActivity(), View.OnClickListener, GenericAdapterCallback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coach_home)
    }

    override fun onClick(v: View?) {
        when(v?.id){

        }
    }

    override fun <T> getClickedObject(clickedObj: T, position: T, callingID: String) {

    }

    override fun <T> getClickedObjectWithViewHolder(
        clickedObj: T,
        viewHolder: T,
        position: T,
        callingID: String
    ) {

    }
}