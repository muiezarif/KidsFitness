package com.muiezarif.kidsfitness.listeners


interface GenericAdapterCallback {
    fun <T> getClickedObject(clickedObj: T, position: T, callingID: String = "")
    fun <T> getClickedObjectWithViewHolder(clickedObj: T,viewHolder:T, position: T, callingID: String = "")
}