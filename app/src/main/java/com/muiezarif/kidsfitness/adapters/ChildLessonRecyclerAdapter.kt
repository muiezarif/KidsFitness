package com.muiezarif.kidsfitness.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.muiezarif.kidsfitness.R
import com.muiezarif.kidsfitness.listeners.GenericAdapterCallback
import com.muiezarif.kidsfitness.models.LessonsModel
import com.muiezarif.kidsfitness.network.response.GetCategoryLessonsResponseItem
import com.muiezarif.kidsfitness.utils.GlideHelper
import kotlinx.android.synthetic.main.item_lessons_view.view.*

class ChildLessonRecyclerAdapter(var list: ArrayList<GetCategoryLessonsResponseItem>, var context: Context, var genericAdapterCallback: GenericAdapterCallback):
    RecyclerView.Adapter<ChildLessonRecyclerAdapter.ItemViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.item_lessons_view, parent, false)
        return ItemViewHolder(v)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        var model = list[position]
        GlideHelper.loadFull(holder.child_lesson_item.ivChildLessonImage,model.lesson_image,R.drawable.icon_sample_image)
        holder.child_lesson_item.tvChildLessonName.setText(model.title)
        holder.child_lesson_item.cvChildLessonItem.setOnClickListener {
            genericAdapterCallback.getClickedObjectWithViewHolder(model,holder,position,"ChildLessonClick")
        }
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var child_lesson_item = itemView.findViewById<CardView>(R.id.cvChildLessonItem)
    }
}