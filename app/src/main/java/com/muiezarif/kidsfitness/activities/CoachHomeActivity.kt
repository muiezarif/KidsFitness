package com.muiezarif.kidsfitness.activities

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.android.billingclient.api.*
import com.muiezarif.kidsfitness.ApplicationClass
import com.muiezarif.kidsfitness.BuildConfig
import com.muiezarif.kidsfitness.R
import com.muiezarif.kidsfitness.activities.viewmodels.ChildHomeViewModel
import com.muiezarif.kidsfitness.adapters.CoachLessonRecyclerAdapter
import com.muiezarif.kidsfitness.listeners.GenericAdapterCallback
import com.muiezarif.kidsfitness.models.IntentParams
import com.muiezarif.kidsfitness.network.api.ApiResponse
import com.muiezarif.kidsfitness.network.api.Status
import com.muiezarif.kidsfitness.network.response.GetCategoryLessonsResponse
import com.muiezarif.kidsfitness.network.response.GetCategoryLessonsResponseItem
import com.muiezarif.kidsfitness.utils.*
import kotlinx.android.synthetic.main.activity_child_home.*
import kotlinx.android.synthetic.main.activity_coach_home.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


class CoachHomeActivity : AppCompatActivity(), View.OnClickListener, GenericAdapterCallback,PurchasesUpdatedListener {
    @Inject
    lateinit var sharedPrefsHelper: SharedPrefsHelper

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var childHomeViewModel: ChildHomeViewModel
    private var childLessonsList: ArrayList<GetCategoryLessonsResponseItem> = ArrayList()
    private lateinit var childLessonsAdapter: CoachLessonRecyclerAdapter
    private lateinit var billingClient: BillingClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ApplicationClass.getAppComponent(this).doInjection(this)
        setContentView(R.layout.activity_coach_home)
        setupViewModel()
        setListeners()
        tvCoachUsername.setText("Welcome \n"+sharedPrefsHelper[Constants.sp_username, ""]+"!")
        loadLocale()
        getPurchases()
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
        ivCoachSettings.setOnClickListener(this)
        ivCoachSignOut.setOnClickListener(this)
        ivCoachChangeCategory.setOnClickListener(this)
        ivCoachChangeLevel.setOnClickListener(this)
    }

    private fun setChildLessonsRecyclerAdapter() {
        rvCoachLessons.setLayoutManager(GridLayoutManager(this, 2))
//        rvChildLessons.setLayoutManager(
//            LinearLayoutManager(
//                this,
//                LinearLayoutManager.HORIZONTAL,
//                false
//            )
//        )
        childLessonsAdapter = CoachLessonRecyclerAdapter(childLessonsList, this, this,sharedPrefsHelper[Constants.sp_language,""].toString())
        rvCoachLessons.adapter = childLessonsAdapter
        childLessonsAdapter.notifyDataSetChanged()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ivCoachSignOut -> {
                sharedPrefsHelper.clearData()
                navigate<SelectUserTypeActivity>(finish = true)
            }
            R.id.ivCoachSettings -> {
                navigate<ChildSettingsActivity>(finish = false)
            }
            R.id.ivCoachChangeCategory -> {
                val params = ArrayList<IntentParams>().apply {
                    add(IntentParams("type", Constants.STUDENT_CHANGE_CATEGORY))
                    add(IntentParams("check", "change_category"))
                }
                navigate<SelectCategoryActivity>(params = params,finish = false)
            }
            R.id.ivCoachChangeLevel ->{
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
        when(callingID){
            "ChildLessonClick" -> {
                val params = ArrayList<IntentParams>().apply {
                    add(IntentParams("type", Constants.STUDENT_CAT_LESSON_PARTS))
                    add(IntentParams("check", "student_cat_lesson_parts"))
                    add(IntentParams("lesson_slug", clickedObj.lesson_slug))
                }
                navigate<ChildLessonPartsActivity>(params = params,finish = false)
            }
            "ChildLessonShareClick" -> {
                try {
                    val shareIntent = Intent(Intent.ACTION_SEND)
                    shareIntent.type = "text/plain"
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Kisszoo")
                    var shareMessage = "\nCheckout sports lessons here\n\n"
                    shareMessage =
                        """
                        ${shareMessage}https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}
                        """.trimIndent()
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
                    startActivity(Intent.createChooser(shareIntent, "choose one"))
                } catch (e: Exception) {
                    //e.toString();
                }
            }
        }
    }
    private fun getPurchases(){
        Log.i("BILLING", "In getPurchase")
        billingClient = BillingClient.newBuilder(this).setListener(this).enablePendingPurchases().build()
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingServiceDisconnected() {
//                Toast.makeText(
//                    this@HomeActivity,
//                    "Billing Client Disconnected",
//                    Toast.LENGTH_SHORT
//                ).show()
            }

            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    Log.i("BILLING", "In Billing client connected")
//                    Toast.makeText(
//                        this@HomeActivity,
//                        "Billing Client Connected",
//                        Toast.LENGTH_SHORT
//                    ).show()
                    var result = billingClient.queryPurchases(BillingClient.SkuType.SUBS)
                    var purchases = result.purchasesList
                    if (purchases != null && purchases.size > 0) {
                        Log.i("BILLING", "In getPurchase purchase check")
                        for(purchase in purchases){
                            if (purchase.sku == Constants.sku_monthly_premium){
//                                sharedPrefsHelper.put(Constants.sp_premium_user,true)
                                Log.i("BILLING", "In getPurchase purchase check if")
                            }else{
                                Log.i("BILLING", "In getPurchase purchase check else")
//                                sharedPrefsHelper.put(Constants.sp_premium_user,false)
                                val parameters: Map<String, String?> =
                                    mapOf(
                                        "email" to sharedPrefsHelper[Constants.sp_email, ""],
                                        "address" to sharedPrefsHelper[Constants.sp_address, ""],
                                        "username" to sharedPrefsHelper[Constants.sp_username, ""],
                                        "is_premium" to "false"
                                    )
//                                homeViewModel.hitUpdateUserInfoApi(
//                                    sharedPrefsHelper[Constants.sp_token, ""],
//                                    sharedPrefsHelper[Constants.sp_uid, 0]?.toInt(),
//                                    parameters,sharedPrefsHelper[Constants.sp_language,""]
//                                )
                            }
                        }
                    }else{
                        val parameters: Map<String, String?> =
                            mapOf(
                                "email" to sharedPrefsHelper[Constants.sp_email, ""],
                                "address" to sharedPrefsHelper[Constants.sp_address, ""],
                                "username" to sharedPrefsHelper[Constants.sp_username, ""],
                                "is_premium" to "false"
                            )
//                        homeViewModel.hitUpdateUserInfoApi(
//                            sharedPrefsHelper[Constants.sp_token, ""],
//                            sharedPrefsHelper[Constants.sp_uid, 0]?.toInt(),
//                            parameters,sharedPrefsHelper[Constants.sp_language,""]
//                        )
                    }
                } else {
                    Toast.makeText(
                        this@CoachHomeActivity,
                        "Billing Client Error" + billingResult.responseCode,
                        Toast.LENGTH_SHORT
                    ).show()
//                    getPurchases()
                }
            }

        })

    }
    override fun onResume() {
//        ActivityCompat.recreate(this as Activity)
        childHomeViewModel.hitGetCategoryLessonsApi(sharedPrefsHelper[Constants.sp_token, ""],sharedPrefsHelper[Constants.sp_child_category_slug, ""],sharedPrefsHelper[Constants.sp_child_level_slug, ""],"")
        super.onResume()
    }

    override fun onPurchasesUpdated(p0: BillingResult, p1: MutableList<Purchase>?) {

    }
}