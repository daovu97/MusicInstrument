package com.musical.instrument.simulator.app.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.musical.instrument.simulator.app.databinding.ItemButtonGuitarBinding
import com.musical.instrument.simulator.app.model.Guitar

class GutiarAdapter(
    private var context: Context,
    private var list: MutableList<Guitar>,
    private var clickItem: ClickItem
): RecyclerView.Adapter<GutiarAdapter.PianoViewHolder>() {
    inner class PianoViewHolder(val binding: ItemButtonGuitarBinding): RecyclerView.ViewHolder(binding.root) {
        fun onBind(item: Guitar, index: Int) {
            val time1 = System.currentTimeMillis()
            binding.imageButtonPiano.setImageDrawable(ContextCompat.getDrawable(context, item.drawable!!))
            binding.imageButtonPiano.isActivated = item.isSelected!!
            val time2 = System.currentTimeMillis()
            Log.d("Check_Time_Adapter", "${time2 - time1}")

            // set event
            binding.imageButtonPiano.setOnClickListener {
                clickItem.chooseButton(item, index)
            }
        }
    }

    interface ClickItem {
        fun chooseButton(item: Guitar, index: Int)
    }

    fun setListGuitar(list: MutableList<Guitar>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PianoViewHolder =
        PianoViewHolder(
            ItemButtonGuitarBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: PianoViewHolder, position: Int) {
        val item = list[position]
        holder.onBind(item, position)
    }
}