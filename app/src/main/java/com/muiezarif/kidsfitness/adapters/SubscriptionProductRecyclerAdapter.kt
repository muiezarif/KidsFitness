package com.muiezarif.kidsfitness.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.android.billingclient.api.SkuDetails
import com.muiezarif.kidsfitness.R
import com.muiezarif.kidsfitness.listeners.GenericAdapterCallback
import kotlinx.android.synthetic.main.inventory_item.view.*

class SubscriptionProductRecyclerAdapter (var context: Context?, var skuDetailsList:ArrayList<SkuDetails>, var genericCallback: GenericAdapterCallback): RecyclerView.Adapter<SubscriptionProductRecyclerAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.inventory_item, parent, false)
        return ItemViewHolder(v)
    }

    override fun getItemCount(): Int {
        return skuDetailsList.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.item.sku_title.text = skuDetailsList[position].title
        holder.item.sku_description.text = skuDetailsList[position].description
        holder.item.sku_price.text = skuDetailsList[position].price
        holder.item.setOnClickListener {
            genericCallback.getClickedObjectWithViewHolder(skuDetailsList[position],holder,position,"SubscriptionItemClicked")
        }
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val item = itemView.findViewById<CardView>(R.id.cvSubscriptionInventoryItem)
    }
}