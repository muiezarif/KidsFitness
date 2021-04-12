package com.muiezarif.kidsfitness.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class PagerAdapter(private val context: Context, private val onBoardingDataList: ArrayList<Int>) :
    RecyclerView.Adapter<PagerAdapter.ViewHolder>() {
    var currentPosition = 0
    private lateinit var view: View

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        view = LayoutInflater.from(parent.context).inflate(onBoardingDataList[currentPosition], parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return onBoardingDataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        currentPosition = position + 1
    }

    inner class ViewHolder(item: View) : RecyclerView.ViewHolder(item)
}