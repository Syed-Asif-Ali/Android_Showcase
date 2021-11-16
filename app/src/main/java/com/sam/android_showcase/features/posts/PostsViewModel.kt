package com.sam.android_showcase.features.posts

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sam.android_showcase.GetPostsQuery
import com.sam.android_showcase.features.network.ApiRepo
import com.sam.android_showcase.features.network.UseCaseResponse
import com.sam.android_showcase.model.CommonItem

class PostsViewModel(private val mApiRepo: ApiRepo, private val mContext: Application) : ViewModel(){

    private var page = 1
    private val limit = 20
    var isAllDataLoaded = false

    var postsData = MutableLiveData<ArrayList<GetPostsQuery.Data1>>()
    var showProgressbar = MutableLiveData<Boolean>()
    var messageData = MutableLiveData<String>()
    var selected: GetPostsQuery.Data1? = null

    fun setSelectedItem(item: GetPostsQuery.Data1) {
        selected = item
    }

    fun getPosts() {
        if (isAllDataLoaded) return;
        showProgressbar.postValue(true)
        Log.d("viewModel", "getPosts called")
        mApiRepo.getPosts(page, limit, mContext , object : UseCaseResponse<ArrayList<GetPostsQuery.Data1>>{
            override fun onSuccess(result: ArrayList<GetPostsQuery.Data1>) {
                if(postsData.value == null){
                    val tmp: ArrayList<GetPostsQuery.Data1> = ArrayList()
                    tmp.addAll(result)
                    postsData.postValue(tmp)
                }else{
                    val tmp = postsData.value!!
                    tmp.addAll(result)
                    postsData.postValue(tmp)
                }
                showProgressbar.postValue(false)
                page++
            }

            override fun onError(apiErrorMsg: String?) {
                showProgressbar.postValue(false)
                if(apiErrorMsg!=null) {
                    if(apiErrorMsg.contains("No More Data Found")) isAllDataLoaded = true
                    messageData.postValue(apiErrorMsg!!)
                }
            }
        })
    }
}