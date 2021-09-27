package com.muiezarif.kidsfitness.activities

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.muiezarif.kidsfitness.ApplicationClass
import com.muiezarif.kidsfitness.R
import com.muiezarif.kidsfitness.activities.viewmodels.CategoryViewModel
import com.muiezarif.kidsfitness.adapters.CategoryRecyclerViewAdapter
import com.muiezarif.kidsfitness.listeners.GenericAdapterCallback
import com.muiezarif.kidsfitness.network.api.ApiResponse
import com.muiezarif.kidsfitness.network.api.Status
import com.muiezarif.kidsfitness.network.response.GetChildCategoryResponse
import com.muiezarif.kidsfitness.network.response.GetChildCategoryResponseItem
import com.muiezarif.kidsfitness.utils.*
import kotlinx.android.synthetic.main.activity_coach_on_boarding.*
import kotlinx.android.synthetic.main.activity_select_category.*
import kotlinx.android.synthetic.main.activity_select_coach_categories.*
import kotlinx.android.synthetic.main.activity_student_onboarding.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class SelectCoachCategoriesActivity : AppCompatActivity(), View.OnClickListener,
    GenericAdapterCallback {
    @Inject
    lateinit var sharedPrefsHelper: SharedPrefsHelper

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var selectCategoryViewModel: CategoryViewModel
    private var childCategoriesList: ArrayList<GetChildCategoryResponseItem> = ArrayList()
    private lateinit var childCategoriesAdapter: CategoryRecyclerViewAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ApplicationClass.getAppComponent(this).doInjection(this)
        setContentView(R.layout.activity_select_coach_categories)
        setupViewModel()
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
        selectCategoryViewModel =
            ViewModelProvider(this, viewModelFactory).get(CategoryViewModel::class.java)
        selectCategoryViewModel.categoriesResponse()
            .observe(this, Observer<ApiResponse<GetChildCategoryResponse>> { t ->
                consumeResponse(t)
            })
    }

    private fun consumeResponse(apiResponse: ApiResponse<GetChildCategoryResponse>?) {
        when (apiResponse?.status) {
            Status.LOADING -> {
//                pbSignIn.visibility = View.VISIBLE
            }
            Status.SUCCESS -> {
//                pbSignIn.visibility = View.GONE
                renderSuccessResponse(apiResponse.data as GetChildCategoryResponse)
            }
            Status.ERROR -> {
//                pbSignIn.visibility = View.GONE
                toast(this, apiResponse.error.toString())
            }
            else -> {

            }
        }
    }

    private fun renderSuccessResponse(response: GetChildCategoryResponse) {
        try {
            if (childCategoriesList.size > 0) {
                childCategoriesList.removeAll(childCategoriesList)
            }
            childCategoriesList.addAll(response.result)
            setCategoriesRecyclerAdapter()
        } catch (e: Exception) {
            toast(this, e.message.toString())
        }
    }

    private fun setCategoriesRecyclerAdapter() {
//        rvStudentCategories.setLayoutManager(
//            LinearLayoutManager(
//                this,
//                LinearLayoutManager.HORIZONTAL,
//                false
//            )
//        )
        rvCoachCategories.setLayoutManager(GridLayoutManager(this, 2))
        childCategoriesAdapter = CategoryRecyclerViewAdapter(childCategoriesList, this, this,sharedPrefsHelper[Constants.sp_language,""].toString())
        rvCoachCategories.adapter = childCategoriesAdapter
        childCategoriesAdapter.notifyDataSetChanged()
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
            "CategoryClick" -> {
                clickedObj as GetChildCategoryResponseItem
                sharedPrefsHelper.put(Constants.sp_child_category_slug, clickedObj.category_slug)
                when (intent?.getStringExtra("type")) {
                    Constants.STUDENT_CHANGE_CATEGORY -> {
                        navigate<CoachHomeActivity>(finish = true)
                    }
                    else ->{
                        navigate<CoachOnBoardingActivity>(finish = false)
                    }
                }

            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {

        }
    }

    override fun onResume() {
        super.onResume()
        selectCategoryViewModel.hitGetCategoryLessonsApi()
    }}