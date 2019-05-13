package com.example.annimationshow.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.example.annimationshow.R
import com.example.annimationshow.bean.DataBean

class InterpolatorAdapter(var data:ArrayList<DataBean>):RecyclerView.Adapter<InterpolatorAdapter.InterpolatorHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InterpolatorHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_interpolator_layout,
            parent,false)
        return InterpolatorHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: InterpolatorHolder, position: Int) {
        holder.checkBox.let {
            it.text=(data[position].name)
            it.isChecked=data[position].isShown
        }
    }


    class InterpolatorHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var checkBox:CheckBox = itemView.findViewById(R.id.Interpolator_box)

    }
}