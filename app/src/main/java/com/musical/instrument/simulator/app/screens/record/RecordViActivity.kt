package com.musical.instrument.simulator.app.screens.record

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import com.musical.instrument.simulator.app.base.BaseVMActivity
import com.musical.instrument.simulator.app.base.BaseViewModel
import com.musical.instrument.simulator.app.base.hidden
import com.musical.instrument.simulator.app.base.setOnBackClick
import com.musical.instrument.simulator.app.base.setOnSingleClickListener
import com.musical.instrument.simulator.app.base.visible
import com.musical.instrument.simulator.app.database.AudioDao
import com.musical.instrument.simulator.app.databinding.ActivityRecordWaveBinding
import com.musical.instrument.simulator.app.model.AudioFile
import com.musical.instrument.simulator.app.screens.main.ContainerActivity
import com.musical.instrument.simulator.app.screens.record.RecordListFragment.Companion.ARG_TYPE
import com.musical.instrument.simulator.app.utils.widget.DialogChangeNameBarcode
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import javax.inject.Inject
import kotlin.jvm.internal.Intrinsics

const val REQUEST_CODE = 200

@AndroidEntryPoint
class RecordViActivity :
    BaseVMActivity<ActivityRecordWaveBinding, RecordViewModel>(ActivityRecordWaveBinding::inflate),
    Timer.OnTimerTickListener, DialogChangeNameBarcode.Listener {

    override val viewModel: RecordViewModel by (this as ComponentActivity).viewModels()

    private lateinit var amplitudes: ArrayList<Float>
    private var permissions = arrayOf(Manifest.permission.RECORD_AUDIO)
    private var permissionGranted = false

    private var recorder: MediaRecorder? = null
    private var dirFile: File? = null
    private var isRecording = false

    private var duration = ""

    private lateinit var timer: Timer

    override fun setupView(savedInstanceState: Bundle?) {
        super.setupView(savedInstanceState)

        permissionGranted = ActivityCompat.checkSelfPermission(
            this, permissions[0]
        ) == PackageManager.PERMISSION_GRANTED


        if (!permissionGranted) ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE)

        timer = Timer(this)

        binding.btnRecord.setOnClickListener {
            when {
                isRecording -> stopRecorder()
                else -> startRecording()
            }
        }

        binding.btnDelete.setOnClickListener {
            stopRecorder()
            dirFile?.deleteOnExit()
        }

        binding.btnRecordStop.setOnSingleClickListener {
            stopRecorder()
            DialogChangeNameBarcode.newInstance("").show(supportFragmentManager, "")
        }

        binding.headerView.setOnBackClick {
            finish()
        }

    }

    private fun save(name: String) {
        dirFile ?: return
        val audioFile = AudioFile(
            filePath = dirFile!!.path,
            createdAt = System.currentTimeMillis(),
            name = if (name.trim().isEmpty()) dirFile!!.name else name,
            category = AudioFile.TYPE_AUDIO
        )

        viewModel.saveAudio(audioFile)

        ContainerActivity.start(
            this, ContainerActivity.ChildFrag.RECORDLIST, bundleOf(ARG_TYPE to AudioFile.TYPE_AUDIO)
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE) permissionGranted =
            grantResults[0] == PackageManager.PERMISSION_GRANTED
    }

    private fun startRecording() {
        dirFile = null
        if (!permissionGranted) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE)
            return
        }
        // start recording

        recorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(this)
        } else {
            MediaRecorder()
        }

        var file: File

        recorder?.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setAudioChannels(1)
            setAudioEncodingBitRate(128000)
            setAudioSamplingRate(48000)

            val filesDir = filesDir
            val file2 = File(filesDir, "/" + System.currentTimeMillis() + ".aac")
            file2.also { file = it }
            if (!file2.exists()) {
                file.createNewFile()
            }

            try {
                Intrinsics.checkNotNull(file)
                setOutputFile(file.path)
                prepare()
                start()
                dirFile = file
            } catch (e: IOException) {

            }
        }

        binding.btnDelete.isClickable = true
        binding.btnRecord.hidden()
        binding.btnRecordStop.visible()
        isRecording = true
        timer.start()
        binding.btnDelete.visible()
    }

    override fun onStop() {
        super.onStop()
        recorder?.apply {
            release()
        }
        recorder = null
    }

    private fun stopRecorder() {
        timer.stop()
        recorder?.apply {
            stop()
            release()
        }
        recorder = null

        isRecording = false
        binding.btnDelete.isClickable = false
        binding.btnDelete.hidden()

        binding.btnRecord.visible()
        binding.btnRecordStop.hidden()
        binding.tvTimer.text = "00:00.00"
        amplitudes = binding.waveformView.clear()
    }

    override fun onTimerTick(duration: String) {
        binding.tvTimer.text = duration
        this.duration = duration.dropLast(3)
        binding.waveformView.addAmplitude(recorder?.maxAmplitude?.toFloat() ?: 0f)
    }

    override fun dialogChangeNameBarcodeOnCancel() {
        stopRecorder()
        dirFile?.deleteOnExit()
    }

    override fun dialogChangeNameBarcodeOnClickOk(value: String) {
        save(value)
    }
}

@HiltViewModel
class RecordViewModel @Inject constructor(private val dao: AudioDao) : BaseViewModel() {
    fun saveAudio(audioFile: AudioFile) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.add(audioFile)
        }
    }
}