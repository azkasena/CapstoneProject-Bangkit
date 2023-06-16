package com.vie.healthyteeth.disease

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vie.healthyteeth.R

class MyAdapter(private val descList : ArrayList<DiseaseData>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {


    private lateinit var mListener : onItemClickListener

    interface onItemClickListener{

        fun onItemClick(position: Int)
    }

    fun setOnClickListener(listener: onItemClickListener){

        mListener = listener
    }

    class MyViewHolder(itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView){

        val disease_name : TextView = itemView.findViewById(R.id.disease_name)

        init {

            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_row, parent,false)
        return MyViewHolder(itemView, mListener)

    }

    override fun getItemCount(): Int {
        return descList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = descList[position]
        holder.disease_name.text = currentItem.heading
    }
}