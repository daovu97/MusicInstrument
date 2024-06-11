package com.musical.instrument.simulator.app.screens.piano

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.DisplayMetrics
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.musical.instrument.simulator.app.R
import com.musical.instrument.simulator.app.model.AudioFile
import com.musical.instrument.simulator.app.model.MyPiano
import com.musical.instrument.simulator.app.model.Song
import com.musical.instrument.simulator.app.model.ThemesPiano

object Utils {
    const val INDEX_BUTTON_C = 0
    const val INDEX_BUTTON_DO = 1
    const val INDEX_BUTTON_O = 2
    const val SEEKBAR_OFFSET_SIZE: Float = -12f
    private const val NAME_THEME_SHARED = "NAME_THEME_SHARED"
    private const val KEY_THEME_PIANO = "KEY_THEME_PIANO"
    const val PIANO_THEME_DEFAULT = 0
    const val PIANO_THEME_PINK = 1
    const val PIANO_THEME_BLUE = 2
    const val PIANO_THEME_YELLOW = 3
    const val KEY_BACK_SCREEN = "KEY_BACK_SCREEN"
    const val VALUE_BACK_SCREEN = "VALUE_BACK_SCREEN"
    const val FLAG_MIC_RECORD = 1
    const val FLAG_PIANO_RECORD = 2
    const val FLAG_TAB_PIANO_RECORD = 1
    const val FLAG_TAB_RECORD = 2

    fun getSongs(): MutableList<Song> {
        val list: MutableList<Song> = mutableListOf()

        list.add(Song(0, "Happy birthday", "Stevie Wonder"))
        list.add(Song(1, "Jingle bell", "James Lord Pierpont's"))
        list.add(Song(2, "Little star", "Jane Taylor"))
        list.add(Song(3, "Last Christmas", "Wham!"))
        list.add(Song(4, "All Of Me", "Stevie Wonder"))
        list.add(Song(5, "Beethoven 5th Symphony", "Beethoven"))
        list.add(Song(6, "Let It Go", "Idina Menzel"))
        list.add(Song(7, "Call Me Maybe", "Carly Rae Jepsen"))

        return list
    }



    fun convertDpToPixel(dp: Float, context: Context): Float {
        val resources = context.resources
        val metrics = resources.displayMetrics
        return dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    fun activeImage(index: Int, list: MutableList<ImageView>): Int {
        list[index].isActivated = true
        for (i in 0 until list.size) {
            if (i != index) {
                list[i].isActivated = false
            }
        }

        return index
    }

    fun getThemesPiano(): MutableList<ThemesPiano> {
        val list: MutableList<ThemesPiano> = mutableListOf()

        list.add(ThemesPiano(PIANO_THEME_DEFAULT, R.drawable.image_piano_default, false))
        list.add(ThemesPiano(PIANO_THEME_PINK, R.drawable.image_piano_pink, false))
        list.add(ThemesPiano(PIANO_THEME_BLUE, R.drawable.image_piano_blue, false))
        list.add(ThemesPiano(PIANO_THEME_YELLOW, R.drawable.image_piano_yellow, false))

        return list
    }

    fun getPianoThemes(context: Context?): Int {
        val sharedPreferences =
            context?.getSharedPreferences(NAME_THEME_SHARED, Context.MODE_PRIVATE)

        return sharedPreferences?.getInt(KEY_THEME_PIANO, PIANO_THEME_DEFAULT)!!
    }

    fun savePianoThemes(context: Context, flag: Int) {
        val sharedPreferences =
            context.getSharedPreferences(NAME_THEME_SHARED, Context.MODE_PRIVATE)
        sharedPreferences?.edit()?.putInt(KEY_THEME_PIANO, flag)?.apply()
    }

    fun getPianoThemes(): MutableList<MyPiano> {
        val list: MutableList<MyPiano> = mutableListOf()

        list.add(MyPiano(PIANO_THEME_DEFAULT, R.drawable.button_piano_start, R.drawable.button_piano_previous, R.drawable.button_piano_next, R.drawable.button_piano_end, R.drawable.status_button_record, R.drawable.white_piano_key, R.drawable.black_piano_key, R.drawable.bg_piano_default))
        list.add(MyPiano(PIANO_THEME_PINK, R.drawable.button_piano_start_pink, R.drawable.button_piano_previsous_pink, R.drawable.button_piano_next_pink, R.drawable.button_piano_end_pink, R.drawable.status_button_record_pink, R.drawable.white_piano_key_pink, R.drawable.black_piano_key_pink, R.drawable.bg_piano_pink))
        list.add(MyPiano(PIANO_THEME_BLUE, R.drawable.button_piano_start, R.drawable.button_piano_previous, R.drawable.button_piano_next, R.drawable.button_piano_end, R.drawable.status_button_record, R.drawable.white_piano_key_blue, R.drawable.black_piano_key_blue, R.drawable.bg_piano_blue))
        list.add(MyPiano(PIANO_THEME_YELLOW, R.drawable.button_piano_start_yellow, R.drawable.button_piano_previous_yellow, R.drawable.button_piano_next_yellow, R.drawable.button_piano_end_yellow, R.drawable.status_button_record_yellow, R.drawable.white_piano_key_yellow, R.drawable.black_piano_key_yellow, R.drawable.bg_piano_yellow))

        return list
    }

    fun checkPermissionStorage(
        context: Context,
    ): Boolean {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            (ContextCompat.checkSelfPermission(
                context, Manifest.permission.READ_MEDIA_AUDIO
            ) == PackageManager.PERMISSION_GRANTED)
            return true
        } else {
            return (ContextCompat.checkSelfPermission(
                    context, Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                    context, Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED) || (ContextCompat.checkSelfPermission(
                    context, Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    context, Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED)
        }
    }

    fun checkPermissionRecordAudio(context: Context): Boolean =
        ContextCompat.checkSelfPermission(
            context, Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
}