package com.daon.digitalpass.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.daon.digitalpass.data.Pass
import com.daon.digitalpass.databinding.ListItemPassBinding

class PassListAdapter(var data: List<Pass>, val clickListener: PassListListener): RecyclerView.Adapter<PassListAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item, clickListener)
    }

    override fun getItemCount() = data.size

    /*
     *====================================== View holder ===========================================
     */

    class ViewHolder private constructor(val binding: ListItemPassBinding): RecyclerView.ViewHolder(binding.root){
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemPassBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }

        fun bind(item: Pass, clickListener: PassListListener){
            binding.pass = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }
    }

}

/*
 *====================================== View listener ===========================================
 */

class PassListListener(val clickListener: (pass: Pass) -> Unit){
    fun onCLick(pass: Pass) = clickListener(pass)
}

