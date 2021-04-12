package com.muiezarif.kidsfitness.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.muiezarif.kidsfitness.ApplicationClass
import com.muiezarif.kidsfitness.R
import com.muiezarif.kidsfitness.activities.viewmodels.CategoryViewModel
import com.muiezarif.kidsfitness.activities.viewmodels.LoginViewModel
import com.muiezarif.kidsfitness.adapters.CategoryRecyclerViewAdapter
import com.muiezarif.kidsfitness.adapters.ChildLessonRecyclerAdapter
import com.muiezarif.kidsfitness.listeners.GenericAdapterCallback
import com.muiezarif.kidsfitness.models.LessonsModel
import com.muiezarif.kidsfitness.network.api.ApiResponse
import com.muiezarif.kidsfitness.network.api.Status
import com.muiezarif.kidsfitness.network.response.GetChildCategoryResponse
import com.muiezarif.kidsfitness.network.response.GetChildCategoryResponseItem
import com.muiezarif.kidsfitness.network.response.LoginResponse
import com.muiezarif.kidsfitness.utils.*
import kotlinx.android.synthetic.main.activity_child_home.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_select_category.*
import javax.inject.Inject

class SelectCategoryActivity : AppCompatActivity(), View.OnClickListener, GenericAdapterCallback {
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
        setContentView(R.layout.activity_select_category)
        setupViewModel()
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
        rvStudentCategories.setLayoutManager(GridLayoutManager(this, 2))
        childCategoriesAdapter = CategoryRecyclerViewAdapter(childCategoriesList, this, this)
        rvStudentCategories.adapter = childCategoriesAdapter
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
                        navigate<ChildHomeActivity>(finish = true)
                    }
                    else ->{
                        navigate<StudentOnboardingActivity>(finish = false)
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
    }
}