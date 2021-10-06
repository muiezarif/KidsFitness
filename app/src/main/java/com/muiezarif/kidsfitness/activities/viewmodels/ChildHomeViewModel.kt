package com.muiezarif.kidsfitness.activities.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.muiezarif.kidsfitness.network.api.ApiResponse
import com.muiezarif.kidsfitness.network.repository.Repository
import com.muiezarif.kidsfitness.network.response.GetCategoryLessonsResponse
import com.muiezarif.kidsfitness.network.response.UpdateUserResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ChildHomeViewModel  @Inject constructor(val repository: Repository): ViewModel(){
    private val disposables = CompositeDisposable()
    private val responseLiveData = MutableLiveData<ApiResponse<GetCategoryLessonsResponse>>()
    private val responseUpdateUserInfoLiveData = MutableLiveData<ApiResponse<UpdateUserResponse>>()

    fun categoryLessonsResponse(): MutableLiveData<ApiResponse<GetCategoryLessonsResponse>> {
        return responseLiveData
    }
    fun updateUserInfoResponse(): MutableLiveData<ApiResponse<UpdateUserResponse>> {
        return responseUpdateUserInfoLiveData
    }
    fun hitUpdateUserInfoApi(authorizationToken: String?,id:Int?,parameters: Map<String, String?>,lang:String?) {
        disposables.add(repository.executeUpdateUserInfo(authorizationToken,id,parameters,lang)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { d -> responseUpdateUserInfoLiveData.setValue(ApiResponse.loading()) }
            .subscribe(
                { result -> responseUpdateUserInfoLiveData.setValue(ApiResponse.success(result)) },
                { throwable ->
                    responseUpdateUserInfoLiveData.setValue(ApiResponse.error(throwable))
                }
            ))
    }
    fun hitGetCategoryLessonsApi(token:String?, category_slug:String?, level_type:String?,lang:String?) {
        disposables.add(repository.executeGetCategoriesLessons(token,category_slug,level_type)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { d -> responseLiveData.setValue(ApiResponse.loading()) }
            .subscribe(
                { result -> responseLiveData.setValue(ApiResponse.success(result)) },
                { throwable ->
                    responseLiveData.setValue(ApiResponse.error(throwable))
                }
            ))
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}