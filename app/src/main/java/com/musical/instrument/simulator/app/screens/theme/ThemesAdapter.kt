package com.musical.instrument.simulator.app.screens.theme

import android.view.View
import com.bumptech.glide.Glide
import com.musical.instrument.simulator.app.base.BaseSingleAdapter
import com.musical.instrument.simulator.app.base.BaseViewHolder
import com.musical.instrument.simulator.app.base.setOnSingleClickListener
import com.musical.instrument.simulator.app.databinding.ItemThemesBinding

class ThemesAdapter(
    private var clickItem: (ThemeBackground) -> Unit
) : BaseSingleAdapter<ThemeBackground, ItemThemesBinding>(ItemThemesBinding::inflate) {

    override fun createViewHolder(binding: ItemThemesBinding): BaseViewHolder<ItemThemesBinding> {
        return BaseViewHolder(binding).apply {
            binding.containerItem.setOnSingleClickListener {
                val item =
                    getItemAt(this.adapterPosition) ?: return@setOnSingleClickListener
                currentSelectedID = item.id
                clickItem.invoke(item)
                notifyDataSetChanged()
            }
        }
    }

    var currentSelectedID: Int = ThemeBackground.current.id

    override fun bindingViewHolder(holder: BaseViewHolder<ItemThemesBinding>, position: Int) {
        holder.binding.let { binding ->
            val item = getItemAt(position) ?: return
            item.source?.let {
                Glide.with(binding.root.context).load(it).into(binding.imageItem)
            } ?: binding.imageItem.setImageDrawable(null)

            if (item.id == currentSelectedID) {
                binding.iconDone.visibility = View.VISIBLE
            } else {
                binding.iconDone.visibility = View.GONE
            }
        }
    }
}