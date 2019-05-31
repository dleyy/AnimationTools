package com.example.annimationshow.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.RadioGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.annimationshow.R
import com.example.annimationshow.bean.DataBean
import java.util.*

class InterpolatorAdapter(var data: MutableList<DataBean>) :
    RecyclerView.Adapter<InterpolatorAdapter.InterpolatorHolder>() {

    var currentSelected = 0

    lateinit var checkedFunc: (position: Int) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InterpolatorHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_interpolator_layout,
            parent, false
        )
        return InterpolatorHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: InterpolatorHolder, position: Int) {
        holder.checkBox.let {
            it.text = (data[position].name)
            it.isChecked = data[position].isShown
        }

        holder.checkBox.setOnClickListener {
            data[currentSelected].isShown = false
            data[position].isShown = true
            currentSelected = position
            checkedFunc(position)
            notifyDataSetChanged()
        }
    }


    class InterpolatorHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var checkBox: CheckBox = itemView.findViewById(R.id.Interpolator_box)

    }
}