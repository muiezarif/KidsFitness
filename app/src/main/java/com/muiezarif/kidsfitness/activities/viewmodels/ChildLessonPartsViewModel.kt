package com.muiezarif.kidsfitness.activities.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.muiezarif.kidsfitness.network.api.ApiResponse
import com.muiezarif.kidsfitness.network.repository.Repository
import com.muiezarif.kidsfitness.network.response.GetCategoryLessonsResponse
import com.muiezarif.kidsfitness.network.response.GetLessonPartsResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ChildLessonPartsViewModel @Inject constructor(val repository: Repository): ViewModel() {
    private val disposables = CompositeDisposable()
    private val responseLiveData = MutableLiveData<ApiResponse<GetLessonPartsResponse>>()

    fun categoryLessonPartsResponse(): MutableLiveData<ApiResponse<GetLessonPartsResponse>> {
        return responseLiveData
    }

    fun hitGetCategoryLessonPartsApi(token:String?, category_slug:String?,lang:String?) {
        disposables.add(repository.executeGetCategoriesLessonsPart(token,category_slug)
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