package com.muiezarif.kidsfitness.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.muiezarif.kidsfitness.R
import com.muiezarif.kidsfitness.listeners.GenericAdapterCallback
import com.muiezarif.kidsfitness.network.response.GetLessonChaptersResult
import com.muiezarif.kidsfitness.network.response.GetLessonPartsResult
import kotlinx.android.synthetic.main.item_lesson_part_view.view.*

class ChapterVideosRecyclerAdapter(var list: ArrayList<GetLessonPartsResult>, var context: Context, var genericAdapterCallback: GenericAdapterCallback, var lang:String):
    RecyclerView.Adapter<ChapterVideosRecyclerAdapter.ItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.item_lesson_part_view, parent, false)
        return ItemViewHolder(v)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        var model = list[position]
        when(lang){
            "en" -> {
                holder.child_part_lesson_item.tvLessonPartName.setText(model.video_name)
            }
            "zh" -> {
                holder.child_part_lesson_item.tvLessonPartName.setText(model.video_name_chinese)
            }
            "de" -> {
                holder.child_part_lesson_item.tvLessonPartName.setText(model.video_name_german)
            }
            else -> {
                holder.child_part_lesson_item.tvLessonPartName.setText(model.video_name)
            }
        }
        holder.child_part_lesson_item.setOnClickListener {
            genericAdapterCallback.getClickedObjectWithViewHolder(model,holder,position,"ChildLessonChapterClick")
        }
    }
    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var child_part_lesson_item = itemView.findViewById<CardView>(R.id.itemChildLessonPart)
    }
}