package com.musical.instrument.simulator.app.screens.record

import android.widget.PopupWindow
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.chengtao.pianoview.entity.AutoPlayEntity
import com.chengtao.pianoview.listener.OnPianoAutoPlayListener
import com.chengtao.pianoview.view.PianoView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.musical.instrument.simulator.app.R
import com.musical.instrument.simulator.app.adapter.ManagerAudio
import com.musical.instrument.simulator.app.adapter.RecordsAdapter
import com.musical.instrument.simulator.app.adapter.createPopupWindow
import com.musical.instrument.simulator.app.base.ActionType
import com.musical.instrument.simulator.app.base.BaseVMFragment
import com.musical.instrument.simulator.app.base.BaseViewModel
import com.musical.instrument.simulator.app.base.Converter
import com.musical.instrument.simulator.app.base.gone
import com.musical.instrument.simulator.app.base.setOnBackClick
import com.musical.instrument.simulator.app.base.setOnSingleClickListener
import com.musical.instrument.simulator.app.base.visible
import com.musical.instrument.simulator.app.data.local.sharedKey
import com.musical.instrument.simulator.app.database.AudioDao
import com.musical.instrument.simulator.app.databinding.ActivityRecordListBinding
import com.musical.instrument.simulator.app.databinding.PopupMoreBinding
import com.musical.instrument.simulator.app.model.AudioFile
import com.musical.instrument.simulator.app.screens.main.ContainerActivity
import com.musical.instrument.simulator.app.screens.main.ContainerActivity.Companion.CHILD_FRG_BUNDLE
import com.musical.instrument.simulator.app.utils.AdsNative
import com.musical.instrument.simulator.app.utils.widget.DialogChangeNameBarcode
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class RecordListFragment :
    BaseVMFragment<ActivityRecordListBinding, RecordListViewModel>(ActivityRecordListBinding::inflate),
    OnPianoAutoPlayListener, DialogChangeNameBarcode.Listener {

    companion object {
        val ARG_TYPE by sharedKey()
    }

    private lateinit var recordsAdapter: RecordsAdapter
    override val viewModel: RecordListViewModel by viewModels()
    private val managerAudio: ManagerAudio by lazy {
        ManagerAudio(this.requireContext())
    }

    private lateinit var pianoView: PianoView

    private var popupWindow: PopupWindow? = null
    private var currentChangeNameItem: AudioFile? = null
    private var isSelectAudio = false

    override fun initView() {
        binding.frNativeAds.removeAllViews()
        pianoView = PianoView(context)
        pianoView.setAutoPlayListener(this)
        viewModel.loadAudio()

        if (activity is ContainerActivity) {
            binding.header.actionLeftType = ActionType.IMAGE
            binding.header.setOnBackClick {
                activity?.finish()
            }
        }

        val type = arguments?.getBundle(CHILD_FRG_BUNDLE)?.getInt(ARG_TYPE) ?: -1

        recordsAdapter = RecordsAdapter(clickItem = { item, isPlay ->
            if (isPlay) {
                pianoView.autoPlayHandler.sendEmptyMessage(1)
                managerAudio.release()
                recordsAdapter.setSelected(-1)
                return@RecordsAdapter
            }
            pianoView.autoPlayHandler.sendEmptyMessage(1)
            managerAudio.release()
            if (item.category == AudioFile.TYPE_PIANO) {
                isSelectAudio = true
                handlePlayMusicFile(item)
                return@RecordsAdapter
            } else {
                isSelectAudio = false
                item.filePath?.let { it1 ->
                    managerAudio.play(it1, onCompletionListener = {
                        recordsAdapter.setSelected(-1)
                    })
                }
            }
            recordsAdapter.setSelected(item.id)
        }) { item, view ->
            binding.maskView.gone()
            popupWindow?.dismiss()
            popupWindow = null

            pianoView.autoPlayHandler.sendEmptyMessage(1)
            managerAudio.release()
            recordsAdapter.setSelected(-1)

            popupWindow = binding.root.context.createPopupWindow(
                createBinding = PopupMoreBinding::inflate,
                setupView = { b, window ->
                    b.linDelete.setOnSingleClickListener {
                        currentChangeNameItem = item
                        window.dismiss()
                        popupWindow = null
                        binding.maskView.gone()
                        DialogChangeNameBarcode.newInstance(item.name)
                            .show(childFragmentManager, "")
                    }

                    b.linDuplicate.setOnSingleClickListener {
                        window.dismiss()
                        popupWindow = null
                        binding.maskView.gone()
                        viewModel.deleteAudio(item)
                    }
                })
            binding.maskView.visible()
            popupWindow?.showAsDropDown(
                view, 0 - Converter.asPixels(24), 0 - Converter.asPixels(24)
            )
        }

        binding.maskView.setOnClickListener {
            popupWindow?.dismiss()
            popupWindow = null
            binding.maskView.gone()
        }

        binding.recycler.apply {
            adapter = recordsAdapter
            layoutManager = LinearLayoutManager(this@RecordListFragment.requireContext())
        }

        viewModel.listAudio.observe(this) {
            if (it.isNotEmpty()) {
                loadAdsNativeRecord()
            }
            recordsAdapter.setupData(if (type == -1) it else it.filter {
                it.category == type
            })
        }
    }

    private fun jsonToAutoList(str: String): ArrayList<AutoPlayEntity> {
        val gson = Gson()
        val listType = object : TypeToken<ArrayList<AutoPlayEntity>>() {}.type
        return gson.fromJson(str, listType)
    }

    private fun handlePlayMusicFile(item: AudioFile) {
        val value = item.jsonString ?: return
        pianoView.initPiano()
        pianoView.autoPlay2(jsonToAutoList(value), pianoView.whitePianoKeys)
    }

    private var isLoadAds = false

    private fun loadAdsNativeRecord() {
        if (isLoadAds) return
        AdsNative.RECORD.loadAds(
            activity,
            R.layout.ads_native_small,
            onAdLoaded = { nativeAd, view ->
                isLoadAds = true
                binding.frNativeAds.removeAllViews()
                binding.frNativeAds.addView(view)
            }) {
            binding.frNativeAds.removeAllViews()
        }
    }

    override fun onPause() {
        super.onPause()
        managerAudio.onPause()
        pianoView.autoPlayHandler.sendEmptyMessage(1)
        binding.maskView.gone()
        popupWindow?.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        managerAudio.release()
        pianoView.autoPlayHandler.sendEmptyMessage(1)
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadAudio()
    }

    override fun onPianoAutoPlayStart() {

    }

    override fun onPianoAutoPlayEnd() {
        if (isSelectAudio) {
            recordsAdapter.setSelected(-1)
            isSelectAudio = false
        }
    }

    override fun dialogChangeNameBarcodeOnCancel() {
    }

    override fun dialogChangeNameBarcodeOnClickOk(value: String) {
        currentChangeNameItem?.let { viewModel.rename(it.copy(name = value)) }
    }
}

@HiltViewModel
class RecordListViewModel @Inject constructor(private val audioDao: AudioDao) : BaseViewModel() {
    val listAudio = MutableLiveData<List<AudioFile>>()

    fun loadAudio() {
        viewModelScope.launch {
            val list = audioDao.getAll()
            listAudio.postValue(list)
        }
    }

    fun deleteAudio(audio: AudioFile) {
        viewModelScope.launch {
            audioDao.delete(audio)
            loadAudio()
            audio.filePath?.let { File(it).deleteOnExit() }
        }
    }

    fun rename(audio: AudioFile) {
        viewModelScope.launch {
            audioDao.add(audio)
            loadAudio()
        }
    }

}