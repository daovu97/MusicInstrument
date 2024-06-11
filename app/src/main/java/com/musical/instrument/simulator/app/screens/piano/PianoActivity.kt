package com.musical.instrument.simulator.app.screens.piano

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import com.chengtao.pianoview.entity.AutoPlayEntity
import com.chengtao.pianoview.entity.Piano
import com.chengtao.pianoview.listener.OnLoadAudioListener
import com.chengtao.pianoview.listener.OnPianoAutoPlayListener
import com.chengtao.pianoview.listener.OnPianoListener
import com.chengtao.pianoview.utils.AutoPlayUtils
import com.chengtao.pianoview.view.PianoView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.musical.instrument.simulator.app.R
import com.musical.instrument.simulator.app.base.BaseVMActivity
import com.musical.instrument.simulator.app.base.BaseViewModel
import com.musical.instrument.simulator.app.database.AudioDao
import com.musical.instrument.simulator.app.databinding.ActivityPianoBinding
import com.musical.instrument.simulator.app.model.AudioFile
import com.musical.instrument.simulator.app.model.MyPiano
import com.musical.instrument.simulator.app.model.PianoKey
import com.musical.instrument.simulator.app.model.Song
import com.musical.instrument.simulator.app.screens.language.LanguageActivity
import com.musical.instrument.simulator.app.screens.main.ContainerActivity
import com.musical.instrument.simulator.app.screens.main.HomeActivity
import com.musical.instrument.simulator.app.screens.permission.PermissionActivity
import com.musical.instrument.simulator.app.screens.record.RecordListFragment.Companion.ARG_TYPE
import com.musical.instrument.simulator.app.screens.theme.ThemeActivity
import com.musical.instrument.simulator.app.utils.widget.DialogChangeNameBarcode
import com.musical.instrument.simulator.app.utils.widget.RecordSelectDialog
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset
import java.util.Locale
import javax.inject.Inject
import kotlin.jvm.internal.Intrinsics

@HiltViewModel
class PianoViewModel @Inject constructor(private val audioDao: AudioDao) : BaseViewModel() {
    fun saveAudio(audioFile: AudioFile) {
        CoroutineScope(Dispatchers.IO).launch {
            audioDao.add(audioFile)
        }
    }
}

@AndroidEntryPoint
class PianoActivity :
    BaseVMActivity<ActivityPianoBinding, PianoViewModel>(ActivityPianoBinding::inflate),
    OnSeekBarChangeListener, OnPianoAutoPlayListener, OnLoadAudioListener, OnClickListener,
    OnPianoListener, DialogChangeNameBarcode.Listener, RecordSelectDialog.Listener {

    override val viewModel: PianoViewModel by (this as ComponentActivity).viewModels()

    private lateinit var list: MutableList<Song>
    private var dialogRecord: RecordSelectDialog? = null

    //    private lateinit var dialogSaveRecord: Dialog
    private var indexSpinner = 0
    private var scrollProgress = 0
    private val USE_CONFIG_FILE: Boolean = true
    private var isPlay = false
    private var litterStarList: ArrayList<AutoPlayEntity>? = null
    private val CONFIG_FILE_NAME = "simple_little_star_config"
    private val listImage: MutableList<ImageView> = mutableListOf()
    private var themesPiano: MutableList<MyPiano> = mutableListOf()
    private var valuesBackScreen: String? = null
    private var mediaRecorder: MediaRecorder? = null
    private var isRecording = false
    private var isRecordingMic = false
    private lateinit var fileCurrent: File
    private var isRecordingPiano = false
    private var flagStopRecord = false
    private var progressCurrent: Int = 0
    private var listRecordPiano: MutableList<PianoKey> = mutableListOf()
    private var flag: Int = 2
    private var flagInit = false

    companion object {
        var mScale: Float = 1f
    }

    override fun setupView(savedInstanceState: Bundle?) {
        super.setupView(savedInstanceState)
        init()
        onClick()
    }

    private fun init() {
        valuesBackScreen = intent.getStringExtra(Utils.KEY_BACK_SCREEN)
        getThemePiano()
        // not show note piano
        PianoView.isShowNote = false
        list = mutableListOf()
        list = Utils.getSongs()
        val arrayAdapter: ArrayAdapter<Song> =
            ArrayAdapter(this@PianoActivity, R.layout.item_spinner, list)
        binding.spinner.apply {
            adapter = arrayAdapter
        }
        binding.spinner.setSelection(indexSpinner)
        binding.pv.setSoundPollMaxStream(10)
//        binding.buttonUp.isActivated = false
        if (USE_CONFIG_FILE) {
            val assetManager = assets
            try {
                litterStarList = AutoPlayUtils.getAutoPlayEntityListByCustomConfigInputStream(
                    assetManager.open(CONFIG_FILE_NAME)
                )
            } catch (e: IOException) {
                Log.e("HELLO", e.message!!)
            }
        }
        getListImage()

//        dialogRecord = MyDialog.getDialogRecord(this@PianoActivity, R.layout.dialog_record)
//        dialogRecord = RecordSelectDialog()

        // init database
//        helper = AudioHelper(this@PianoActivity, "Audio.db", null, 1)
//        helper.queryData(
//            "CREATE TABLE IF NOT EXISTS AUDIO(ID INTEGER PRIMARY KEY AUTOINCREMENT, FILE_PATH VARCHAR(1000), CREATED_AT LONG, NAME VARCHAR(1000), CATEGORY INT)"
//        )
//        helper.queryData(
//            "CREATE TABLE IF NOT EXISTS ${Key.table_piano_record_file_name}(ID INTEGER PRIMARY KEY AUTOINCREMENT, JSON_OBJECT VARCHAR(1000000), CREATED_AT LONG, NAME VARCHAR(1000))"
//        )
        progressCurrent = binding.seekBarSpeed.progress
        setStatusBarGradiant()
        binding.spinner.isEnabled = false
    }

    private fun getThemePiano() {
        themesPiano = Utils.getPianoThemes()
        val mPiano: MyPiano = themesPiano[Utils.getPianoThemes(this@PianoActivity)]
        // show data
        binding.apply {
            imgBg.setImageResource(mPiano.background!!)
            btnRecorderNow.setImageResource(mPiano.buttonRecord!!)
            imgSeekbarStart.setImageResource(mPiano.buttonStart!!)
            imgSeekbarPrevious.setImageResource(mPiano.buttonPrevious!!)
//            imgSeekbarNext.setImageResource(mPiano.buttonNext!!)
            imgSeekbarEnd.setImageResource(mPiano.buttonEnd!!)
            Piano.blackDrawable = mPiano.blackKey!!
            Piano.whiteDrawable = mPiano.whiteKey!!
        }
    }

    private fun getListImage() {
        listImage.clear()
        listImage.add(Utils.INDEX_BUTTON_C, binding.buttonC)
        listImage.add(Utils.INDEX_BUTTON_DO, binding.buttonDo)
        listImage.add(Utils.INDEX_BUTTON_O, binding.buttonO)

        listImage[2].isActivated = true
        for (i in 0 until listImage.size) {
            if (i != Utils.INDEX_BUTTON_O) {
                listImage[i].isActivated = false
            }
        }
    }

    private fun onClick() {
        binding.pv.setAutoPlayListener(this)
        binding.pv.setLoadAudioListener(this)
        binding.sb.setOnSeekBarChangeListener(this)
//        binding.imgSeekbarNext.setOnClickListener(this)
        binding.imgSeekbarPrevious.setOnClickListener(this)
        binding.imgSeekbarEnd.setOnClickListener(this)
        binding.imgSeekbarStart.setOnClickListener(this)
//        binding.buttonUp.setOnClickListener(this)
        binding.buttonO.setOnClickListener(this)
        binding.buttonDo.setOnClickListener(this)
        binding.buttonC.setOnClickListener(this)
//        binding.btnRecordList.setOnClickListener(this)
        binding.btnSetting.setOnClickListener(this)
        binding.btnRecorderNow.setOnClickListener(this)
        binding.layoutContainer.setOnClickListener(this)
        binding.viewSupported.setOnClickListener(this)
        binding.imgShowNote.setOnClickListener(this)
        binding.btnStartListAvailable.setOnClickListener(this)
        binding.imgDouble.setOnClickListener(this)
        binding.img2player3.setOnClickListener(this)
        binding.imgStyle.setOnClickListener(this)
        binding.btnBack.setOnClickListener(this)
        binding.tvTheme.setOnClickListener(this)
        binding.tvLanguage.setOnClickListener(this)
        binding.layoutPianoRecord.setOnClickListener(this)
        binding.pv.setPianoListener(this)
        binding.layoutParentSpinner.setOnClickListener {
            if (!isPlay) {
                if (!isRecording) {
                    val intent = Intent(this@PianoActivity, MusicListActivity::class.java)
                    startActivityForResult(intent, 2003)
                    progressCurrent = binding.seekBarSpeed.progress
                    clearRecordVar()
                } else {
                    updateLanguage()
                    Toast.makeText(
                        this@PianoActivity,
                        getString(R.string.warning_record),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        binding.viewSupportedSpeed.setOnClickListener {
            if (!isPlay) {
                if (!isRecording) {
                    val intent = Intent(this@PianoActivity, MusicListActivity::class.java)
                    startActivityForResult(intent, 2003)
                    progressCurrent = binding.seekBarSpeed.progress
                    clearRecordVar()
                } else {
                    updateLanguage()
                    Toast.makeText(
                        this@PianoActivity,
                        getString(R.string.warning_record),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        binding.seekBarSpeed.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.seekBarSpeed.isEnabled = !isPlay
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
    }

    private fun convertToJSON(pianoKeys: MutableList<PianoKey>): String {
        val jsonArray = JSONArray()

        for (i in 0 until pianoKeys.size) {
            val pianoKey = pianoKeys[i]
            val jsonObject = JSONObject()

            jsonObject.put("type", pianoKey.type)
            jsonObject.put("group", pianoKey.group)
            jsonObject.put("position", pianoKey.position)

            if (i < pianoKeys.size - 1) {
                val nextPianoKey = pianoKeys[i + 1]
                val breakTime = nextPianoKey.currentTime - pianoKey.currentTime
                jsonObject.put("break", breakTime)
            } else {
                jsonObject.put("break", 0)
            }

            jsonArray.put(jsonObject)
        }

        return jsonArray.toString()
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        binding.pv.scroll(progress)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

    override fun onStopTrackingTouch(seekBar: SeekBar?) {}

    override fun onPianoAutoPlayStart() {
        isPlay = true
        binding.btnStartListAvailable.setImageResource(R.drawable.icon_pause)
        binding.seekBarSpeed.isEnabled = false
    }

    override fun onPianoAutoPlayEnd() {
        isPlay = false
        binding.btnStartListAvailable.setImageResource(R.drawable.image_play)
        binding.seekBarSpeed.isEnabled = true
    }

    override fun loadPianoAudioStart() {
        flagInit = true
        binding.layoutLoading.visibility = View.VISIBLE
    }

    override fun loadPianoAudioFinish() {
        binding.layoutLoading.visibility = View.GONE
        mScale = binding.pv.scale
//        binding.layoutTop.visibility = View.VISIBLE
        flagInit = false

    }

    override fun loadPianoAudioError(e: Exception?) {}

    override fun loadPianoAudioProgress(progress: Int) {}

    override fun onClick(view: View?) {
        var progress: Int
        when (view!!.id) {
            binding.imgSeekbarStart.id -> {
                binding.sb.progress -= 10
//                if (scrollProgress == 0) {
//                    progress = 0
//                } else {
//                    progress = binding.sb.progress - scrollProgress
//                    if (progress < 0) {
//                        progress = 0
//                    }
//                }
//                binding.sb.progress = progress
            }

            binding.imgSeekbarEnd.id -> {
                binding.sb.progress += 10
//                if (scrollProgress == 0) {
//                    progress = 100
//                } else {
//                    progress = binding.sb.progress + scrollProgress
//                    if (progress > 100) {
//                        progress = 100
//                    }
//                }
//                binding.sb.progress = progress
            }

//            binding.buttonUp.id -> {
//                binding.buttonUp.isActivated = !binding.buttonUp.isActivated
//                if (binding.buttonUp.isActivated) {
////                    binding.layoutTop.visibility = View.GONE
//                } else {
////                    binding.layoutTop.visibility = View.VISIBLE
//                }
//
//            }

            binding.buttonO.id -> {
                getFlag(Utils.activeImage(Utils.INDEX_BUTTON_O, listImage))
            }

            binding.buttonC.id -> {
                getFlag(Utils.activeImage(Utils.INDEX_BUTTON_C, listImage))
            }

            binding.buttonDo.id -> {
                getFlag(Utils.activeImage(Utils.INDEX_BUTTON_DO, listImage))
            }

//            binding.btnRecordList.id -> {
//                if (isPlay) {

//                } else {
//                    startActivity(Intent(this@PianoActivity, MusicListActivity::class.java))
//                    clearRecordVar()
//                }
//            }

            binding.btnRecorderNow.id -> {
                if (!flagInit) {
                    record()
                }
            }

            binding.btnSetting.id -> {
                if (!isRecording) {
                    binding.backgroundDialog.visibility = View.VISIBLE
                } else {
                    updateLanguage()
                    Toast.makeText(
                        this@PianoActivity,
                        getString(R.string.warning_record),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            binding.layoutContainer.id -> {
                binding.backgroundDialog.visibility = View.GONE
            }

            binding.viewSupported.id -> {
                binding.backgroundDialog.visibility = View.GONE
            }

            binding.imgShowNote.id -> {
                binding.imgShowNote.isActivated = !binding.imgShowNote.isActivated
            }

//            binding.btnStartListAvailable.id -> {
//                if (isPlay) {
//                    isPlay = false
//                    binding.btnStartListAvailable.setImageResource(R.drawable.image_play)
//                    binding.pv.autoPlayHandler.sendEmptyMessage(1)
//                    binding.seekBarSpeed.isEnabled = true
//                } else {
//                    isPlay = true
//                    binding.seekBarSpeed.isEnabled = false
//                    binding.btnStartListAvailable.setImageResource(R.drawable.icon_pause)
//                    val itemName = binding.spinner.selectedItem.toString()
//                    progressCurrent = binding.seekBarSpeed.progress
//                    val arrayList: ArrayList<AutoPlayEntity> = ArrayList()
//                    val parsedList = if (itemName.matches("Happy birthday".toRegex())) {
//                        val loadJSONFromAsset: String? = loadJSONFromAsset("happy_birthday.json")
//                        jsonToAutoList(loadJSONFromAsset!!)
//                    } else if (itemName.matches("Jingle bell".toRegex())) {
//                        val loadJSONFromAsset: String? = loadJSONFromAsset("jingle_bell.json")
//                        jsonToAutoList(loadJSONFromAsset!!)
//                    } else if (itemName.matches("Little star".toRegex())) {
//                        val loadJSONFromAsset: String? = loadJSONFromAsset("little_star.json")
//                        jsonToAutoList(loadJSONFromAsset!!)
//                    } else if (itemName.matches("Last Christmas".toRegex())) {
//                        val loadJSONFromAsset: String? = loadJSONFromAsset("last_christmas.json")
//                        jsonToAutoList(loadJSONFromAsset!!)
//                    } else if (itemName.matches("All Of Me".toRegex())) {
//                        val loadJSONFromAsset: String? = loadJSONFromAsset("all_of_me.json")
//                        jsonToAutoList(loadJSONFromAsset!!)
//                    } else if (itemName.matches("Beethoven 5th Symphony".toRegex())) {
//                        val loadJSONFromAsset: String? =
//                            loadJSONFromAsset("beethoven_5th_symphony.json")
//                        jsonToAutoList(loadJSONFromAsset!!)
//                    } else if (itemName.matches("Let It Go".toRegex())) {
//                        val loadJSONFromAsset: String? = loadJSONFromAsset("let_it_go.json")
//                        jsonToAutoList(loadJSONFromAsset!!)
//                    } else if (itemName.matches("Call Me Maybe".toRegex())) {
//                        val loadJSONFromAsset: String? = loadJSONFromAsset("call_me_maybe.json")
//                        jsonToAutoList(loadJSONFromAsset!!)
//                    } else {
//                        null
//                    }
//                    arrayList.clear()
//                    arrayList.addAll(parsedList!!)
//                    when (binding.seekBarSpeed.progress) {
//                        in 0 until 10 -> {
//                            for (item in arrayList) {
//                                item.currentBreakTime *= 3
//                            }
//                        }
//
//                        in 10 until 20 -> {
//                            for (item in arrayList) {
//                                item.currentBreakTime *= 2
//                            }
//                        }
//
//                        else -> {
//                            for (item in arrayList) {
//                                item.currentBreakTime *= 1
//                            }
//                        }
//                    }
//                    binding.pv.autoPlay(
//                        arrayList,
//                        binding.sb,
//                        progressCurrent,
//                        binding.seekBarSpeed
//                    )
//                }
//            }

            binding.img2player3.id -> {
                if (!isRecording) {
                    startActivity(Intent(this@PianoActivity, DoubleKeyActivity::class.java))
                    clearRecordVar()
                } else {
                    updateLanguage()
                    Toast.makeText(
                        this@PianoActivity,
                        getString(R.string.warning_record),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            binding.img2player.id -> {
                if (!isRecording) {
                    startActivity(Intent(this@PianoActivity, TwoPlayerActivity::class.java))
                    clearRecordVar()
                } else {
                    updateLanguage()
                    Toast.makeText(
                        this@PianoActivity,
                        getString(R.string.warning_record),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            binding.imgSeekbarPrevious.id -> {
                finish()
//                binding.sb.progress -= 10
            }

//            binding.imgSeekbarNext.id -> {
//                binding.sb.progress += 10
//            }

            binding.imgStyle.id -> {
                if (!isRecording) {
                    startActivity(Intent(this@PianoActivity, StylePianoActivity::class.java))
                    clearRecordVar()
                } else {
                    updateLanguage()
                    Toast.makeText(
                        this@PianoActivity,
                        getString(R.string.warning_record),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            binding.btnBack.id -> {
                if (valuesBackScreen != null) {
                    startActivity(Intent(this@PianoActivity, HomeActivity::class.java))
                    finishAffinity()
                    clearRecordVar()
                } else {
                    finish()
                }
            }

            binding.tvTheme.id -> {
                startActivity(Intent(this@PianoActivity, ThemeActivity::class.java))
                clearRecordVar()
            }

            binding.tvLanguage.id -> {
                startActivity(Intent(this@PianoActivity, LanguageActivity::class.java))
                clearRecordVar()
            }

            binding.layoutPianoRecord.id -> {
                startActivity(Intent(this@PianoActivity, ContainerActivity::class.java))
                clearRecordVar()
            }
        }
    }

    private fun updateLanguage() {
//        val newLocale = Locale(PreferencesHelper.getLanguage())
//        val configuration = resources.configuration
//        configuration.setLocale(newLocale)
//        resources.updateConfiguration(configuration, resources.displayMetrics)
    }

    private fun getFlag(flag: Int) {
        this.flag = flag
        when (flag) {
            // press O
            2 -> {
                PianoView.isShowNote = false
                binding.pv.setCorD0(false)
            }

            // press D0
            1 -> {
                PianoView.isShowNote = true
                val piano = Piano(this, mScale, true)
                piano.initPiano()
                PianoView.isCorD0 = true
                for (i in piano.whitePianoKeys.indices) {
                    for (j in piano.whitePianoKeys[i].indices) {
                        binding.pv.whitePianoKeys = piano.whitePianoKeys
                    }
                }
                binding.pv.setCorD0(true)
            }

            // press C
            0 -> {
                PianoView.isShowNote = true
                binding.pv.setCorD0(false)
                PianoView.isCorD0 = false
                val piano = Piano(this, mScale, false)
                piano.initPiano()
                for (i in piano.whitePianoKeys.indices) {
                    for (j in piano.whitePianoKeys[i].indices) {
                        binding.pv.whitePianoKeys = piano.whitePianoKeys
                    }
                }
            }

            else -> {
                PianoView.isShowNote = true
            }
        }
    }

    private fun jsonToAutoList(str: String): ArrayList<AutoPlayEntity> {
        val gson = Gson()
        val listType = object : TypeToken<ArrayList<AutoPlayEntity>>() {}.type
        return gson.fromJson(str, listType)
    }

    @Throws(IOException::class)
    private fun loadJSONFromAsset(str: String): String? {
        var inputStream: InputStream? = null
        return try {
            inputStream = assets.open(str)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            String(buffer, Charset.forName("UTF-8"))
        } catch (e: IOException) {
            e.printStackTrace()
            null
        } finally {
            inputStream?.close()
        }
    }

    private fun record() {
        if (binding.btnRecorderNow.isActivated) { // dang ghi am
            // thuc hien dung ghi am
            binding.btnRecorderNow.isActivated = false
            flagStopRecord = false
            DialogChangeNameBarcode.newInstance("").show(supportFragmentManager, "")
//            dialogSaveRecord.show()
        } else { // chua ghi am
            dialogRecord = RecordSelectDialog()
            dialogRecord?.show(supportFragmentManager, "")
            flagStopRecord = true
        }
    }

    private fun recordMic() {
        val isAcceptPermission =
            Utils.checkPermissionRecordAudio(this@PianoActivity) and Utils.checkPermissionStorage(
                this@PianoActivity
            )
        if (isAcceptPermission) { // thuc hien ghi am
            flagStopRecord = false
            isRecordingMic = true
            isRecording = true
            isRecordingPiano = false
            binding.btnRecorderNow.isActivated = true
            dialogRecord?.dismiss()
            dialogRecord = null
            handleRecord()
        } else {
            startActivity(Intent(this@PianoActivity, PermissionActivity::class.java))
            clearRecordVar()
        }
    }

    private fun recordPiano() {
        val isAcceptPermission =
            Utils.checkPermissionRecordAudio(this@PianoActivity) and Utils.checkPermissionStorage(
                this@PianoActivity
            )
        if (isAcceptPermission) { // thuc hien ghi am
            flagStopRecord = false
            isRecordingPiano = true
            isRecording = true
            isRecordingMic = false
            binding.btnRecorderNow.isActivated = true
            dialogRecord?.dismiss()
            dialogRecord = null
        } else {
            startActivity(Intent(this@PianoActivity, PermissionActivity::class.java))
            clearRecordVar()
        }
    }

    private fun handleRecord() {
        var file: File
        try {
            if (mediaRecorder == null) {
                mediaRecorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    MediaRecorder(this@PianoActivity)
                } else {
                    MediaRecorder()
                }
            }
            if (mediaRecorder != null) {
                mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
                mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS)
                mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                mediaRecorder?.setAudioChannels(1)
                mediaRecorder?.setAudioEncodingBitRate(128000)
                mediaRecorder?.setAudioSamplingRate(48000)
                val filesDir = filesDir
                val file2 = File(filesDir, "/" + System.currentTimeMillis() + ".aac")
                file2.also { file = it }
                if (!file2.exists()) {
                    file.createNewFile()
                }
                if (file != null) {
                    Intrinsics.checkNotNull(file)
                    mediaRecorder?.setOutputFile(file.path)
                    mediaRecorder?.prepare()
                    mediaRecorder?.start()
                    binding.btnRecorderNow.isActivated = true
                    fileCurrent = file
                    return
                }
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace()
            Log.e("Error_Record_File", "${e.message}")
        } catch (e2: java.lang.Exception) {
            Log.e("Error_Record_File", "${e2.message}")
            e2.printStackTrace()
        }
    }

    override fun onPause() {
        super.onPause()
        if (isRecording && isRecordingMic) {
            binding.btnRecorderNow.isActivated = false
            flagStopRecord = true
            mediaRecorder?.stop()
        }
        if (isRecording && isRecordingPiano) {
            binding.btnRecorderNow.isActivated = false
            flagStopRecord = true
        }
        if (isPlay) {
            binding.pv.stopAutoPlay()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isRecording) {
            isRecording = false
            isRecordingPiano = false
            isRecordingMic = false
        }
        binding.pv.releaseAutoPlay()
    }

    override fun onResume() {
        super.onResume()
        isPlay = false
    }

//    override fun onBackPressed() {
//        if (!flagInit) {
//            if (valuesBackScreen != null) {
//                PreferencesHelper.increaseCountBackScreen(this)
//                startActivity(Intent(this@PianoActivity, HomeActivity::class.java))
//                finishAffinity()
//                clearRecordVar()
//            } else {
//                PreferencesHelper.increaseCountBackScreen(this)
//                finish()
//
//            }
//        } else {
//            return
//        }
//    }

    override fun onPianoInitFinish() {}

    override fun onPianoClick(
        type: Piano.PianoKeyType?,
        voice: Piano.PianoVoice?,
        group: Int,
        positionOfGroup: Int,
    ) {
        if (isRecordingPiano) {
            listRecordPiano.add(
                PianoKey(
                    type!!.value,
                    group,
                    positionOfGroup,
                    System.currentTimeMillis()
                )
            )
        } else {
            listRecordPiano.clear()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 2003 && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                binding.seekBarSpeed.progress = progressCurrent
                val indexItem = data.getIntExtra("index_item", 0)
                binding.spinner.setSelection(indexItem)
                val listMusic = when (indexItem) {
                    0 -> {
                        val loadJSONFromAsset: String? = loadJSONFromAsset("happy_birthday.json")
                        jsonToAutoList(loadJSONFromAsset!!)
                    }

                    1 -> {
                        val loadJSONFromAsset: String? = loadJSONFromAsset("jingle_bell.json")
                        jsonToAutoList(loadJSONFromAsset!!)
                    }

                    2 -> {
                        val loadJSONFromAsset: String? = loadJSONFromAsset("little_star.json")
                        jsonToAutoList(loadJSONFromAsset!!)
                    }

                    3 -> {
                        val loadJSONFromAsset: String? = loadJSONFromAsset("last_christmas.json")
                        jsonToAutoList(loadJSONFromAsset!!)
                    }

                    5 -> {
                        val loadJSONFromAsset: String? =
                            loadJSONFromAsset("beethoven_5th_symphony.json")
                        jsonToAutoList(loadJSONFromAsset!!)
                    }

                    4 -> {
                        val loadJSONFromAsset: String? = loadJSONFromAsset("all_of_me.json")
                        jsonToAutoList(loadJSONFromAsset!!)
                    }

                    6 -> {
                        val loadJSONFromAsset: String? = loadJSONFromAsset("let_it_go.json")
                        jsonToAutoList(loadJSONFromAsset!!)
                    }

                    7 -> {
                        val loadJSONFromAsset: String? = loadJSONFromAsset("call_me_maybe.json")
                        jsonToAutoList(loadJSONFromAsset!!)
                    }

                    else -> {
                        val loadJSONFromAsset: String? = loadJSONFromAsset("happy_birthday.json")
                        jsonToAutoList(loadJSONFromAsset!!)
                    }
                }
                val arrayList: ArrayList<AutoPlayEntity> = ArrayList()
                arrayList.clear()
                arrayList.addAll(listMusic)
                when (binding.seekBarSpeed.progress) {
                    in 0 until 10 -> {
                        for (item in arrayList) {
                            item.currentBreakTime *= 3
                        }
                    }

                    in 10 until 20 -> {
                        for (item in arrayList) {
                            item.currentBreakTime *= 2
                        }
                    }

                    else -> {
                        for (item in arrayList) {
                            item.currentBreakTime *= 1
                        }
                    }
                }
                binding.pv.autoPlay(arrayList, binding.sb, progressCurrent, binding.seekBarSpeed)
                isPlay = true
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.pv.autoPlay(
                        arrayList,
                        binding.sb,
                        progressCurrent,
                        binding.seekBarSpeed
                    )
                }, 1000)
            }
        }
    }

    private fun clearRecordVar() {
        isRecording = false
        isRecordingPiano = false
        isRecordingMic = false
        if (!flagStopRecord) {
            mediaRecorder?.stop()
            mediaRecorder = null
        }
        flagStopRecord = true
    }

    override fun dialogChangeNameBarcodeOnCancel() {
        clearRecordVar()
    }

    override fun dialogChangeNameBarcodeOnClickOk(value: String) {
        if (isRecordingMic and isRecording) {
            if (!flagStopRecord) {
                mediaRecorder?.stop()
                mediaRecorder = null
            }
            val audioFile = AudioFile(
                filePath = fileCurrent.path,
                createdAt = System.currentTimeMillis(),
                name = if (value.trim().isEmpty()) fileCurrent.name else value,
                category = AudioFile.TYPE_AUDIO
            )

            viewModel.saveAudio(audioFile)
            isRecording = false
            isRecording = false
            isRecordingMic = false
            isRecordingPiano = false

            ContainerActivity.start(
                this,
                ContainerActivity.ChildFrag.RECORDLIST,
                bundleOf(ARG_TYPE to AudioFile.TYPE_AUDIO)
            )
            clearRecordVar()

        } else if (isRecordingPiano and isRecording) {
            val jsonString = convertToJSON(listRecordPiano)

            val audioFile = AudioFile(
                name = if (value.trim().isEmpty()) System.currentTimeMillis().toString() else value,
                category = AudioFile.TYPE_PIANO,
                jsonString = jsonString
            )
            viewModel.saveAudio(audioFile)
            listRecordPiano.clear()
            isRecording = false
            isRecordingPiano = false
            isRecordingMic = false

            ContainerActivity.start(
                this,
                ContainerActivity.ChildFrag.RECORDLIST,
                bundleOf(ARG_TYPE to AudioFile.TYPE_PIANO)
            )
            clearRecordVar()
        }
        flagStopRecord = true
    }

    override fun onRecordSelectMicro() {
        recordMic()
    }

    override fun onRecordSelectPiano() {
        recordPiano()
    }

    override fun onRecordSelectCancel() {
        flagStopRecord = true
        isRecording = false
        isRecordingPiano = false
        isRecordingMic = false
    }
}