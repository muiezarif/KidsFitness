package com.muiezarif.kidsfitness.activities

import android.app.Activity
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.muiezarif.kidsfitness.ApplicationClass
import com.muiezarif.kidsfitness.R
import com.muiezarif.kidsfitness.activities.viewmodels.ChildHomeViewModel
import com.muiezarif.kidsfitness.activities.viewmodels.LoginViewModel
import com.muiezarif.kidsfitness.adapters.ChildLessonRecyclerAdapter
import com.muiezarif.kidsfitness.listeners.GenericAdapterCallback
import com.muiezarif.kidsfitness.models.IntentParams
import com.muiezarif.kidsfitness.models.LessonsModel
import com.muiezarif.kidsfitness.network.api.ApiResponse
import com.muiezarif.kidsfitness.network.api.Status
import com.muiezarif.kidsfitness.network.response.GetCategoryLessonsResponse
import com.muiezarif.kidsfitness.network.response.GetCategoryLessonsResponseItem
import com.muiezarif.kidsfitness.network.response.LoginResponse
import com.muiezarif.kidsfitness.utils.*
import kotlinx.android.synthetic.main.activity_child_home.*
import kotlinx.android.synthetic.main.activity_coach_home.*
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


class ChildHomeActivity : AppCompatActivity(), View.OnClickListener, GenericAdapterCallback {
    @Inject
    lateinit var sharedPrefsHelper: SharedPrefsHelper

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var childHomeViewModel: ChildHomeViewModel
    private var childLessonsList: ArrayList<GetCategoryLessonsResponseItem> = ArrayList()
    private lateinit var childLessonsAdapter: ChildLessonRecyclerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ApplicationClass.getAppComponent(this).doInjection(this)
        setContentView(R.layout.activity_child_home)
        setupViewModel()
        setListeners()
        tvChildUsername.setText(resources.getString(R.string.text_welcome) + " \n"+sharedPrefsHelper[Constants.sp_username, ""]+"!")
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
    private fun setupViewModel() {
        childHomeViewModel =
            ViewModelProvider(this, viewModelFactory).get(ChildHomeViewModel::class.java)
        childHomeViewModel.categoryLessonsResponse()
            .observe(this, Observer<ApiResponse<GetCategoryLessonsResponse>> { t ->
                consumeResponse(t)
            })
    }

    private fun consumeResponse(apiResponse: ApiResponse<GetCategoryLessonsResponse>?) {
        when (apiResponse?.status) {
            Status.LOADING -> {
//                pbSignIn.visibility = View.VISIBLE
            }
            Status.SUCCESS -> {
//                pbSignIn.visibility = View.GONE
                renderSuccessResponse(apiResponse.data as GetCategoryLessonsResponse)
            }
            Status.ERROR -> {
//                pbSignIn.visibility = View.GONE
                toast(this, apiResponse.error.toString())
            }
            else -> {

            }
        }
    }

    private fun renderSuccessResponse(response: GetCategoryLessonsResponse) {
        try {
            if (childLessonsList.size > 0) {
                childLessonsList.removeAll(childLessonsList)
            }
            childLessonsList.addAll(response.result)
            setChildLessonsRecyclerAdapter()
        } catch (e: Exception) {

        }

    }

    private fun setListeners() {
        ivChildSettings.setOnClickListener(this)
        ivChildSignOut.setOnClickListener(this)
        ivChangeCategory.setOnClickListener(this)
        ivChangeLevel.setOnClickListener(this)
    }

    private fun setChildLessonsRecyclerAdapter() {
        rvChildLessons.setLayoutManager(GridLayoutManager(this, 2))
//        rvChildLessons.setLayoutManager(
//            LinearLayoutManager(
//                this,
//                LinearLayoutManager.HORIZONTAL,
//                false
//            )
//        )
        childLessonsAdapter = ChildLessonRecyclerAdapter(childLessonsList, this, this,sharedPrefsHelper[Constants.sp_language,""].toString())
        rvChildLessons.adapter = childLessonsAdapter
        childLessonsAdapter.notifyDataSetChanged()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ivChildSignOut -> {
                sharedPrefsHelper.clearData()
                navigate<SelectUserTypeActivity>(finish = true)
            }
            R.id.ivChildSettings -> {
                navigate<ChildSettingsActivity>(finish = false)
            }
            R.id.ivChangeCategory -> {
                val params = ArrayList<IntentParams>().apply {
                    add(IntentParams("type", Constants.STUDENT_CHANGE_CATEGORY))
                    add(IntentParams("check", "change_category"))
                }
                navigate<SelectCategoryActivity>(params = params,finish = false)
            }
            R.id.ivChangeLevel ->{
                val params = ArrayList<IntentParams>().apply {
                    add(IntentParams("type", Constants.STUDENT_CHANGE_LEVEL))
                    add(IntentParams("check", "change_level"))
                }
                navigate<StudentOnboardingActivity>(params = params,finish = false)
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
        clickedObj as GetCategoryLessonsResponseItem
        val params = ArrayList<IntentParams>().apply {
            add(IntentParams("type", Constants.STUDENT_CAT_LESSON_PARTS))
            add(IntentParams("check", "student_cat_lesson_parts"))
            add(IntentParams("lesson_slug", clickedObj.lesson_slug))
        }
        navigate<ChildLessonPartsActivity>(params = params,finish = false)
    }

    override fun onResume() {
        loadLocale()
        childHomeViewModel.hitGetCategoryLessonsApi(sharedPrefsHelper[Constants.sp_token, ""],sharedPrefsHelper[Constants.sp_child_category_slug, ""],sharedPrefsHelper[Constants.sp_child_level_slug, ""],"")
        super.onResume()
    }
}