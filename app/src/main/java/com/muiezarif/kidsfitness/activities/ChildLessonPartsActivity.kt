package com.muiezarif.kidsfitness.activities

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.muiezarif.kidsfitness.ApplicationClass
import com.muiezarif.kidsfitness.R
import com.muiezarif.kidsfitness.activities.viewmodels.ChildHomeViewModel
import com.muiezarif.kidsfitness.activities.viewmodels.ChildLessonPartsViewModel
import com.muiezarif.kidsfitness.adapters.ChildLessonPartsRecyclerAdapter
import com.muiezarif.kidsfitness.adapters.ChildLessonRecyclerAdapter
import com.muiezarif.kidsfitness.listeners.GenericAdapterCallback
import com.muiezarif.kidsfitness.models.IntentParams
import com.muiezarif.kidsfitness.models.LessonPartModel
import com.muiezarif.kidsfitness.models.LessonsModel
import com.muiezarif.kidsfitness.network.api.ApiResponse
import com.muiezarif.kidsfitness.network.api.Status
import com.muiezarif.kidsfitness.network.response.*
import com.muiezarif.kidsfitness.utils.*
import kotlinx.android.synthetic.main.activity_child_home.*
import kotlinx.android.synthetic.main.activity_child_lesson_parts.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class ChildLessonPartsActivity : AppCompatActivity(), View.OnClickListener, GenericAdapterCallback {
    @Inject
    lateinit var sharedPrefsHelper: SharedPrefsHelper

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var childLessonPartsViewModel: ChildLessonPartsViewModel
    private var childLessonsPartsList: ArrayList<GetLessonChaptersResult> = ArrayList()
    private lateinit var adapterChildLessonParts: ChildLessonPartsRecyclerAdapter
    private var lessonSlug = ""
    private val gson = Gson()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ApplicationClass.getAppComponent(this).doInjection(this)
        setContentView(R.layout.activity_child_lesson_parts)
        setupViewModel()
        getIntentData()
        ivChildLessonPartsBack.setOnClickListener(this)
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

    private fun getIntentData() {
        when (intent?.getStringExtra("type")) {
            Constants.STUDENT_CAT_LESSON_PARTS -> {
                lessonSlug = intent.getStringExtra("lesson_slug").toString()
            }
        }
    }

    private fun setupViewModel() {
        childLessonPartsViewModel =
            ViewModelProvider(this, viewModelFactory).get(ChildLessonPartsViewModel::class.java)
        childLessonPartsViewModel.lessonChaptersResponse()
            .observe(this, Observer<ApiResponse<GetLessonChaptersResponse>> { t ->
                consumeResponse(t)
            })
    }

    private fun consumeResponse(apiResponse: ApiResponse<GetLessonChaptersResponse>?) {
        when (apiResponse?.status) {
            Status.LOADING -> {
//                pbSignIn.visibility = View.VISIBLE
            }
            Status.SUCCESS -> {
//                pbSignIn.visibility = View.GONE
                renderSuccessResponse(apiResponse.data as GetLessonChaptersResponse)
            }
            Status.ERROR -> {
//                pbSignIn.visibility = View.GONE
                toast(this, apiResponse.error.toString())
            }
            else -> {

            }
        }
    }

    private fun renderSuccessResponse(response: GetLessonChaptersResponse) {
        try {
            if (childLessonsPartsList.size > 0) {
                childLessonsPartsList.removeAll(childLessonsPartsList)
            }
            childLessonsPartsList.addAll(response.result)
            setChildLessonsPartRecyclerAdapter()
        } catch (e: Exception) {

        }

    }


    private fun setChildLessonsPartRecyclerAdapter() {
        adapterChildLessonParts = ChildLessonPartsRecyclerAdapter(childLessonsPartsList, this, this,sharedPrefsHelper[Constants.sp_language,""].toString())
        rvChildLessonParts.adapter = adapterChildLessonParts
        adapterChildLessonParts.notifyDataSetChanged()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ivChildLessonPartsBack -> {
                finish()
            }
        }
    }

    override fun <T> getClickedObject(clickedObj: T, position: T, callingID: String) {

    }

    override fun <T> getClickedObjectWithViewHolder(
        clickedObj: T,
        viewHolder: T,
        position: T,
        callingID: String
    ) {
        when (callingID) {
            "ChildLessonChapterClick" -> {
                clickedObj as GetLessonChaptersResult
                position as Int
                val params = ArrayList<IntentParams>().apply {
                    add(IntentParams("type", Constants.STUDENT_CAT_LESSON_PARTS))
                    add(IntentParams("check", "student_cat_lesson_parts"))
                    add(IntentParams("lesson_slug", clickedObj.chapter_slug))
                }
                navigate<ChapterVideosActivity>(params = params,finish = false)

            }
        }
    }

    override fun onResume() {
        childLessonPartsViewModel.hitGetLessonChaptersApi(
            sharedPrefsHelper[Constants.sp_token, ""],
            lessonSlug,
            ""
        )
        super.onResume()
    }
}