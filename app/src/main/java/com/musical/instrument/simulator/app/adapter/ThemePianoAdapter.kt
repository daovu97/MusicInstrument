package com.musical.instrument.simulator.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.musical.instrument.simulator.app.databinding.ItemPianoBinding
import com.musical.instrument.simulator.app.model.ThemesPiano

class ThemePianoAdapter(
    private var context: Context,
    private var list: MutableList<ThemesPiano>,
    private var viewPager2: ViewPager2
): RecyclerView.Adapter<ThemePianoAdapter.ThemPianoViewHolder>() {
    inner class ThemPianoViewHolder(private val binding: ItemPianoBinding): RecyclerView.ViewHolder(binding.root) {
        fun onBind(item: ThemesPiano) {
            binding.imageView.setImageResource(item.image!!.toInt())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThemPianoViewHolder =
        ThemPianoViewHolder(
            ItemPianoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ThemPianoViewHolder, position: Int) {
        val item = list[position] ?: return
        holder.onBind(item)
    }
}