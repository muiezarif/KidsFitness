package com.muiezarif.kidsfitness.activities

import android.app.Activity
import android.app.Dialog
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.android.billingclient.api.*
import com.muiezarif.kidsfitness.ApplicationClass
import com.muiezarif.kidsfitness.R
import com.muiezarif.kidsfitness.activities.viewmodels.CategoryViewModel
import com.muiezarif.kidsfitness.adapters.CategoryRecyclerViewAdapter
import com.muiezarif.kidsfitness.adapters.SubscriptionProductRecyclerAdapter
import com.muiezarif.kidsfitness.listeners.GenericAdapterCallback
import com.muiezarif.kidsfitness.network.api.ApiResponse
import com.muiezarif.kidsfitness.network.api.Status
import com.muiezarif.kidsfitness.network.response.GetChildCategoryResponse
import com.muiezarif.kidsfitness.network.response.GetChildCategoryResponseItem
import com.muiezarif.kidsfitness.network.response.SubscriptionResponse
import com.muiezarif.kidsfitness.utils.*
import kotlinx.android.synthetic.main.activity_coach_on_boarding.*
import kotlinx.android.synthetic.main.activity_select_category.*
import kotlinx.android.synthetic.main.activity_select_coach_categories.*
import kotlinx.android.synthetic.main.activity_student_onboarding.*
import kotlinx.android.synthetic.main.custom_dialog_products.view.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class SelectCoachCategoriesActivity : AppCompatActivity(), View.OnClickListener,
    GenericAdapterCallback, PurchasesUpdatedListener {
    @Inject
    lateinit var sharedPrefsHelper: SharedPrefsHelper

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var selectCategoryViewModel: CategoryViewModel
    private var childCategoriesList: ArrayList<GetChildCategoryResponseItem> = ArrayList()
    private lateinit var childCategoriesAdapter: CategoryRecyclerViewAdapter
    private lateinit var billingClient: BillingClient
    private lateinit var skuDetailsParams: SkuDetailsParams.Builder
    private lateinit var subscriptionAdapter: SubscriptionProductRecyclerAdapter
    var mSkuDetailsList: ArrayList<SkuDetails> = ArrayList()
    lateinit var mSkuDetails: SkuDetails
    private var skuList: ArrayList<String> = ArrayList()
    private lateinit var dialogBox: Dialog
    private lateinit var dialog: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ApplicationClass.getAppComponent(this).doInjection(this)
        setContentView(R.layout.activity_select_coach_categories)
        setupViewModel()
        skuList.add(Constants.sku_monthly_premium)
        startBillingConnection()
        dialogBox = Dialog(this)
        dialog = LayoutInflater.from(this).inflate(R.layout.custom_dialog_products, null, false)
        subscriptionAdapter = SubscriptionProductRecyclerAdapter(this, mSkuDetailsList, this)
        dialog.rvSubscriptions.adapter = subscriptionAdapter
        dialogBox.setContentView(dialog)
        loadLocale()
    }

    private fun setLocale(lang: String) {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        resources.updateConfiguration(config, resources.displayMetrics)
        sharedPrefsHelper.put(Constants.sp_language, lang)
    }

    private fun loadLocale() {
        sharedPrefsHelper[Constants.sp_language, ""]?.let { setLocale(it) }
    }

    private fun startBillingConnection() {
        billingClient =
            BillingClient.newBuilder(this).setListener(this).enablePendingPurchases().build()
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingServiceDisconnected() {
//                Toast.makeText(
//                    context,
//                    "Billing Client Disconnected",
//                    Toast.LENGTH_SHORT
//                ).show()
                startBillingConnection()
            }

            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult?.responseCode == BillingClient.BillingResponseCode.OK) {
//                    Toast.makeText(
//                        context,
//                        "Billing Client Connected",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                    getPurchases()
                    loadAllSkus()
                } else {
                    Toast.makeText(
                        this@SelectCoachCategoriesActivity,
                        "Billing Client Error" + billingResult?.responseCode,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun loadAllSkus() {
        if (billingClient.isReady) {
            skuDetailsParams = SkuDetailsParams.newBuilder()
            skuDetailsParams.setSkusList(skuList).setType(BillingClient.SkuType.SUBS)
            billingClient.querySkuDetailsAsync(
                skuDetailsParams.build()
            ) { billingResult, skuDetailsList ->
                if (skuDetailsList != null && billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    for (skuDetail in skuDetailsList) {
//                        Toast.makeText(
//                            context,
//                            skuDetail.toString(),
//                            Toast.LENGTH_SHORT
//                        ).show()
                        val fSkuDetail = skuDetail as SkuDetails
                        mSkuDetailsList.add(skuDetail)
                        if (fSkuDetail.sku == Constants.sku_monthly_premium) {
                            mSkuDetails = fSkuDetail
                        }
                    }
                    subscriptionAdapter =
                        SubscriptionProductRecyclerAdapter(this, mSkuDetailsList, this)
                }
            }
        } else {
            Toast.makeText(this, "Billing Client Not ready", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun setupViewModel() {
        selectCategoryViewModel =
            ViewModelProvider(this, viewModelFactory).get(CategoryViewModel::class.java)
        selectCategoryViewModel.categoriesResponse()
            .observe(this, Observer<ApiResponse<GetChildCategoryResponse>> { t ->
                consumeResponse(t)
            })
        selectCategoryViewModel.subscriptionResponse()
            .observe(this, Observer<ApiResponse<SubscriptionResponse>> { t ->
                consumeSubscriptionResponse(t)
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
    private fun consumeSubscriptionResponse(apiResponse: ApiResponse<SubscriptionResponse>?) {
        when (apiResponse?.status) {
            Status.LOADING -> {
//                binding.pbSignIn.visibility = View.VISIBLE
            }
            Status.SUCCESS -> {
//                binding.pbSignIn.visibility = View.GONE
                renderSubscriptionSuccessResponse(apiResponse.data as SubscriptionResponse)
            }
            Status.ERROR -> {
//                binding.pbSignIn.visibility = View.GONE
                toast(this, apiResponse.error.toString())
            }
            else -> {

            }
        }
    }

    private fun renderSubscriptionSuccessResponse(subscriptionResponse: SubscriptionResponse) {
//        context?.let { toast(it, subscriptionResponse.toString()) }
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
        childCategoriesAdapter = CategoryRecyclerViewAdapter(
            childCategoriesList,
            this,
            this,
            sharedPrefsHelper[Constants.sp_language, ""].toString()
        )
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
                if (sharedPrefsHelper[Constants.sp_premium_user, false]) {
                    sharedPrefsHelper.put(
                        Constants.sp_child_category_slug,
                        clickedObj.category_slug
                    )
                    when (intent?.getStringExtra("type")) {
                        Constants.STUDENT_CHANGE_CATEGORY -> {
                            navigate<CoachHomeActivity>(finish = true)
                        }
                        else -> {
                            navigate<CoachOnBoardingActivity>(finish = false)
                        }
                    }
                } else {
                    dialogBox.show()
                }
            }
            "SubscriptionItemClicked" -> {
                clickedObj as SkuDetails
                var params = BillingFlowParams.newBuilder()
                    .setSkuDetails(clickedObj).build()
                billingClient.launchBillingFlow(this as Activity, params)
            }
        }
    }

    private fun getPurchases() {
        var result = billingClient.queryPurchases(BillingClient.SkuType.SUBS)
        var purchases = result.purchasesList
        if (purchases != null) {
            for (purchase in purchases) {
                if (purchase.sku == Constants.sku_monthly_premium) {
                    sharedPrefsHelper.put(Constants.sp_premium_user, true)
                } else {
                    sharedPrefsHelper.put(Constants.sp_premium_user, false)
                }
            }
        }
    }

    private fun handlePurchase(purchase: Purchase) {
        if (purchase.sku == Constants.sku_monthly_premium && purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            if (!purchase.isAcknowledged) {
                var params =
                    AcknowledgePurchaseParams.newBuilder().setPurchaseToken(purchase.purchaseToken)
                        .build()
                billingClient.acknowledgePurchase(
                    params
                ) { billingResult ->
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                        Log.i("BILLING", purchase.toString())
                        getPurchases()
                        Toast.makeText(
                            this,
                            "Purchase Acknowledged",
                            Toast.LENGTH_SHORT
                        ).show()
                        Toast.makeText(
                            this,
                            "Now You Have Access To Add Products",
                            Toast.LENGTH_SHORT
                        ).show()

                        val parameters: Map<String, String> =
                            mapOf(
                                "order_id" to purchase.orderId,
                                "product_id" to purchase.sku,
                                "purchase_time" to getDate(purchase.purchaseTime),
                                "purchase_token" to purchase.purchaseToken
                            )
//                        context?.let { toast(it, parameters.toString()) }
                        selectCategoryViewModel.hitUserSubscriptionApi(
                            sharedPrefsHelper[Constants.sp_token, ""],
                            parameters, sharedPrefsHelper[Constants.sp_language, ""]
                        )
                    }
                }
            }
        } else if (purchase.sku == Constants.sku_monthly_premium && purchase.purchaseState == Purchase.PurchaseState.PENDING) {
            Toast.makeText(
                this,
                "Your Payment Is Pending",
                Toast.LENGTH_SHORT
            ).show()
        } else if (purchase.sku == Constants.sku_monthly_premium && purchase.purchaseState == Purchase.PurchaseState.UNSPECIFIED_STATE) {
            Toast.makeText(
                this,
                "Something wrong with billing,Please try again",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun getDate(timestamp: Long): String {
        val calendar = Calendar.getInstance(Locale.ENGLISH)
        calendar.timeInMillis = timestamp * 1000L
        val date = DateFormat.format("dd-MM-yyyy", calendar).toString()
        return date
    }

    override fun onClick(v: View?) {
        when (v?.id) {

        }
    }

    override fun onResume() {
        super.onResume()
        selectCategoryViewModel.hitGetCategoryLessonsApi()
    }

    override fun onPurchasesUpdated(
        billingResult: BillingResult,
        purchases: MutableList<Purchase>?
    ) {
        if (purchases != null && billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
            for (purchase in purchases) {
                handlePurchase(purchase)
            }
        } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            Toast.makeText(
                this,
                "You Canceled",
                Toast.LENGTH_SHORT
            ).show()
        } else if (billingResult.responseCode == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
            Toast.makeText(
                this,
                "You Can Only Use One Subscription For One Account Thanks. To Buy Subscription For Another Account Please use another google play store account",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}