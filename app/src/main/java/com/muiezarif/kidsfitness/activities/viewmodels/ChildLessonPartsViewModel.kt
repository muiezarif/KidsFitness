package com.muiezarif.kidsfitness.activities.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.muiezarif.kidsfitness.network.api.ApiResponse
import com.muiezarif.kidsfitness.network.repository.Repository
import com.muiezarif.kidsfitness.network.response.GetCategoryLessonsResponse
import com.muiezarif.kidsfitness.network.response.GetLessonChaptersResponse
import com.muiezarif.kidsfitness.network.response.GetLessonPartsResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ChildLessonPartsViewModel @Inject constructor(val repository: Repository): ViewModel() {
    private val disposables = CompositeDisposable()
    private val responseLiveData = MutableLiveData<ApiResponse<GetLessonPartsResponse>>()
    private val responseChaptersLiveData = MutableLiveData<ApiResponse<GetLessonChaptersResponse>>()

    fun categoryLessonPartsResponse(): MutableLiveData<ApiResponse<GetLessonPartsResponse>> {
        return responseLiveData
    }
    fun lessonChaptersResponse(): MutableLiveData<ApiResponse<GetLessonChaptersResponse>> {
        return responseChaptersLiveData
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
    fun hitGetLessonChaptersApi(token:String?, category_slug:String?,lang:String?) {
        disposables.add(repository.executeGetLessonChapters(token,category_slug)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { d -> responseChaptersLiveData.setValue(ApiResponse.loading()) }
            .subscribe(
                { result -> responseChaptersLiveData.setValue(ApiResponse.success(result)) },
                { throwable ->
                    responseChaptersLiveData.setValue(ApiResponse.error(throwable))
                }
            ))
    }


    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}