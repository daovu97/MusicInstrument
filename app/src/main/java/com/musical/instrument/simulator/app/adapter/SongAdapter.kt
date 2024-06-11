package com.musical.instrument.simulator.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.musical.instrument.simulator.app.databinding.ItemSongBinding
import com.musical.instrument.simulator.app.model.Song

class SongAdapter(
    private var context: Context,
    private var list: MutableList<Song>,
    private var clickItem: ClickItem
): RecyclerView.Adapter<SongAdapter.SongViewHolder>() {
    inner class SongViewHolder(private val binding: ItemSongBinding): RecyclerView.ViewHolder(binding.root) {
       fun onBind(item: Song, index: Int) {
           binding.itemSongName.text = item.name
           binding.itemSingleName.text = item.singleName

           binding.layoutContainer.setOnClickListener {
               clickItem.clickItem(item, index)
           }
       }
    }

    interface ClickItem {
        fun clickItem(item: Song, index: Int)
    }

    fun setListSong(list: MutableList<Song>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder =
        SongViewHolder(
            ItemSongBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val item = list[position]
        holder.onBind(item, position)
    }
}