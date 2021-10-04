package com.muiezarif.kidsfitness.activities

import android.content.res.Configuration
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.analytics.AnalyticsListener
import com.google.android.exoplayer2.analytics.PlaybackStatsListener
import com.google.android.exoplayer2.audio.AudioListener
import com.google.android.exoplayer2.video.VideoListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.halilibo.bvpkotlin.BetterVideoPlayer
import com.halilibo.bvpkotlin.VideoCallback
import com.halilibo.bvpkotlin.VideoProgressCallback
import com.muiezarif.kidsfitness.ApplicationClass
import com.muiezarif.kidsfitness.R
import com.muiezarif.kidsfitness.adapters.LessonPartsPagerAdapter
import com.muiezarif.kidsfitness.listeners.GenericAdapterCallback
import com.muiezarif.kidsfitness.models.LessonPartVideo
import com.muiezarif.kidsfitness.network.response.GetLessonPartsResult
import com.muiezarif.kidsfitness.utils.Constants
import com.muiezarif.kidsfitness.utils.SharedPrefsHelper
import com.muiezarif.kidsfitness.utils.toast
import kotlinx.android.synthetic.main.activity_child_part_lesson_full_view.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class ChildPartLessonFullViewActivity : AppCompatActivity(), View.OnClickListener,
    GenericAdapterCallback {
    @Inject
    lateinit var sharedPrefsHelper: SharedPrefsHelper
    private lateinit var pagerAdapter: LessonPartsPagerAdapter
    private var lessonPartsList: ArrayList<GetLessonPartsResult> = ArrayList()
    private var fullPlayed = false
    private val gson = Gson()
    private var videoListString: String = ""
    private var startVideoPosition = 0
    private var lastPlayed = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ApplicationClass.getAppComponent(this).doInjection(this)
        setContentView(R.layout.activity_child_part_lesson_full_view)
        getIntentData()
        setListeners()
        hideNextPrevBtn()
        loadLocale()
    }
    private fun setLocale(lang:String){
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        resources.updateConfiguration(config,resources.displayMetrics)
        sharedPrefsHelper.put(Constants.sp_language,lang)
    }
    private fun loadLocale(){
        sharedPrefsHelper[Constants.sp_language, ""]?.let { setLocale(it) }
    }
    private fun setListeners(){
        ivNextVideo.setOnClickListener(this)
        ivBackVideo.setOnClickListener(this)
        ivChildLessonPartsFullViewBack.setOnClickListener(this)
    }
    private fun getIntentData() {
        val type: java.lang.reflect.Type? = object : TypeToken<List<GetLessonPartsResult?>?>() {}.type
        when (intent?.getStringExtra("type")) {
            Constants.STUDENT_CAT_LESSON_PART_VIDEO -> {
                videoListString = intent.getStringExtra("lesson_part_video_list").toString()
                startVideoPosition = intent.getIntExtra("lesson_part_video_clicked_position",0)
                lessonPartsList = gson.fromJson(videoListString, type)
                exoLessonPartPlayer.setSource(lessonPartsList[startVideoPosition].video_url)
                exoLessonPartPlayer.player.playWhenReady = true
                exoLessonPartPlayer.player.addAnalyticsListener(object: AnalyticsListener {
                    override fun onIsPlayingChanged(
                        eventTime: AnalyticsListener.EventTime,
                        isPlaying: Boolean
                    ) {

                    }

                    override fun onPlayerStateChanged(
                        eventTime: AnalyticsListener.EventTime,
                        playWhenReady: Boolean,
                        playbackState: Int
                    ) {
                        if (!exoLessonPartPlayer.player.isPlaying) {
                            if (exoLessonPartPlayer.player.currentPosition > exoLessonPartPlayer.player.duration || exoLessonPartPlayer.player.currentPosition == exoLessonPartPlayer.player.duration){
                                alertDialog()
                            }
                        }
                    }
                })
            }
        }
    }
    private fun alertDialog(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle(resources.getString(R.string.text_great))
        builder.setMessage(resources.getString(R.string.text_doing_great))
        builder.setIcon(android.R.drawable.btn_star)
        builder.setPositiveButton(resources.getString(R.string.text_continue)){ dialogInterface,which ->
//            player.reset()
            if (startVideoPosition <= (lessonPartsList.size-1)) {
                if (startVideoPosition == (lessonPartsList.size-1)){
                    lastPlayed = true
                }else if (startVideoPosition > (lessonPartsList.size-1)){

                }else{
                    startVideoPosition = startVideoPosition.plus(1)
                }
                if (!lastPlayed) {
                    exoLessonPartPlayer.setSource(lessonPartsList[startVideoPosition].video_url)
                }
//                bvpLessonPartPlayer.setSource(Uri.parse(lessonPartsList[startVideoPosition!!].video_url))
            }
            hideNextPrevBtn()
            dialogInterface.dismiss()
        }
        builder.setNegativeButton(resources.getString(R.string.text_cancel)){dialogInterface,which ->
            finish()
            dialogInterface.dismiss()
        }
        val alertDialog: AlertDialog? = builder.create()
        alertDialog?.setCancelable(false)
        alertDialog?.show()
    }
    private fun hideNextPrevBtn(){
        if (startVideoPosition == 0){
            ivBackVideo.visibility = View.GONE
        }else{
            ivBackVideo.visibility = View.VISIBLE
        }
        if (startVideoPosition == (lessonPartsList.size-1)){
            ivNextVideo.visibility = View.GONE
        }else{
            ivNextVideo.visibility = View.VISIBLE
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.ivBackVideo ->{
                if (startVideoPosition >= 0) {
                    if (startVideoPosition != 0) {
                        startVideoPosition = startVideoPosition.minus(1)
                    }
//                    bvpLessonPartPlayer.reset()
//                    bvpLessonPartPlayer.setSource(Uri.parse(lessonPartsList[startVideoPosition!!].video_url))
                    exoLessonPartPlayer.setSource(lessonPartsList[startVideoPosition].video_url)
                }else{
                    toast(this,resources.getString(R.string.text_no_previous_video))
                }
                hideNextPrevBtn()
            }
            R.id.ivNextVideo ->{
                if (startVideoPosition <= (lessonPartsList.size-1)) {
                    if (startVideoPosition == (lessonPartsList.size-1)){

                    }else if (startVideoPosition > (lessonPartsList.size-1)){

                    }else{
                        startVideoPosition = startVideoPosition.plus(1)
                    }
//                    bvpLessonPartPlayer.reset()
//                    bvpLessonPartPlayer.setSource(Uri.parse(lessonPartsList[startVideoPosition!!].video_url))
                    exoLessonPartPlayer.setSource(lessonPartsList[startVideoPosition].video_url)
                }else{
                    toast(this,resources.getString(R.string.text_no_next_video))
                }
                hideNextPrevBtn()
            }
            R.id.ivChildLessonPartsFullViewBack ->{
                finish()
            }
        }
    }

    override fun <T> getClickedObject(clickedObj: T, position: T, callingID: String) {
        when(callingID){
            "LessonPartVideoCompleted" ->{
//                vpLessonPart.setCurrentItem(vpLessonPart.currentItem +1)
            }
        }
    }

    override fun <T> getClickedObjectWithViewHolder(
        clickedObj: T,
        viewHolder: T,
        position: T,
        callingID: String
    ) {

    }
}