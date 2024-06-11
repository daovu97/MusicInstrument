package com.musical.instrument.simulator.app.adapter

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.musical.instrument.simulator.app.R
import com.musical.instrument.simulator.app.base.BaseSingleAdapter
import com.musical.instrument.simulator.app.base.BaseViewHolder
import com.musical.instrument.simulator.app.base.setOnSingleClickListener
import com.musical.instrument.simulator.app.databinding.ItemRecordBinding
import com.musical.instrument.simulator.app.model.AudioFile
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RecordsAdapter(
    val clickItem: (AudioFile, Boolean) -> Unit,
    val onTapMore: (AudioFile, View) -> Unit
) : BaseSingleAdapter<AudioFile, ItemRecordBinding>(ItemRecordBinding::inflate) {

    var selectedID = -1
    override fun createViewHolder(binding: ItemRecordBinding): BaseViewHolder<ItemRecordBinding> {
        return BaseViewHolder(binding)
    }

    fun setSelected(id: Int) {
        selectedID = id
        notifyDataSetChanged()
    }

    override fun bindingViewHolder(holder: BaseViewHolder<ItemRecordBinding>, position: Int) {
        val binding = holder.binding

        fun convertLongToFormattedDateTime(timeInMillis: Long): String {
            val dateFormat = SimpleDateFormat("MMM d, yyyy - HH:mm", Locale.US)
            val dateTime = Date(timeInMillis)
            return dateFormat.format(dateTime)
        }

        fun onBind(item: AudioFile, index: Int) {
            binding.textRecordName.text = item.name
            binding.textTime.text = convertLongToFormattedDateTime(item.createdAt)

            if (item.id == selectedID) {
                binding.image.setImageResource(R.drawable.icon_pause)
            } else {
                binding.image.setImageResource(R.drawable.image_play)
            }

            binding.imageView2.setImageResource(if (item.category == AudioFile.TYPE_AUDIO) R.drawable.img_record else R.drawable.image_item_piano)

            binding.layoutMain.setOnSingleClickListener {
                clickItem.invoke(item, item.id == selectedID)
            }
            binding.iconMore.setOnSingleClickListener {
                onTapMore.invoke(item, binding.iconMore)
            }
        }

        getItemAt(position)?.let { onBind(it, position) }
    }
}

class ManagerAudio(private val context: Context) {

    private var mediaPlayer: MediaPlayer? = null
    fun isPlaying() = mediaPlayer?.isPlaying == true

    fun getMax(): Int {
        val mAudioManager =
            context.getSystemService(AppCompatActivity.AUDIO_SERVICE) as AudioManager
        return mAudioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM)
    }

    fun play(
        uri: String,
        onStart: (() -> Unit)? = null,
        onCompletionListener: (() -> Unit)? = null
    ) {
        release()
        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build()
            )
            isLooping = false
        }
        mediaPlayer?.play(context, uri, onStart, onCompletionListener)
    }

    fun onPause() {
        mediaPlayer?.pause()
    }

    fun setVolume(volume: Float) {
        mediaPlayer?.setVolume(volume, volume)
    }

    fun release() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

}

private fun MediaPlayer.play(
    context: Context, uri: String,
    onStart: (() -> Unit)? = null,
    onCompletionListener: (() -> Unit)? = null
) {
    reset()
    val mediaPath = Uri.parse(uri)
    setDataSource(context, mediaPath)

    prepareAsync()
    setOnPreparedListener {
        start()
        onStart?.invoke()
    }

    setOnCompletionListener {
        onCompletionListener?.invoke()
    }
    isLooping = false
}

fun <B : ViewBinding> Context.createPopupWindow(
    createBinding: (LayoutInflater, ViewGroup?, Boolean) -> B, setupView: (B, PopupWindow) -> Unit
): PopupWindow {
    val binding = createBinding.invoke(LayoutInflater.from(this), null, false)
    val popupWindow = PopupWindow(binding.root)
    val width: Int = LinearLayout.LayoutParams.WRAP_CONTENT
    binding.root.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
    setupView.invoke(binding, popupWindow)
    val height: Int = binding.root.measuredHeight
    popupWindow.width = width
    popupWindow.height = height
    popupWindow.isFocusable = false
    popupWindow.setBackgroundDrawable(null)
    return popupWindow
}