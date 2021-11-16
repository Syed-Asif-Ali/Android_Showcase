package com.sam.android_showcase.features.posts

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sam.android_showcase.GetPostsQuery
import com.sam.android_showcase.R
import com.sam.android_showcase.network.ApiRepo
import com.sam.android_showcase.network.UseCaseResponse
import com.sam.android_showcase.utills.isNetworkAvailable

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
        // Return if all data is loaded
        if (isAllDataLoaded) return

        // Return if network is unavailable
        if (!mContext.isNetworkAvailable()) {
            messageData.postValue(mContext.getString(R.string.no_internet_connection))
            showProgressbar.postValue(false)
            return
        }

        // Return if API is loading
        if(showProgressbar.value!= null && showProgressbar.value!!)  return

        // Load Data
        showProgressbar.postValue(true)
        Log.d("viewModel", "loading posts data page: $page")
        mApiRepo.getPosts(page, limit, object : UseCaseResponse<ArrayList<GetPostsQuery.Data1>>{
            override fun onSuccess(result: ArrayList<GetPostsQuery.Data1>) {
                showProgressbar.postValue(false)
                if(postsData.value == null){
                    val tmp: ArrayList<GetPostsQuery.Data1> = ArrayList()
                    tmp.addAll(result)
                    postsData.postValue(tmp)
                }else{
                    val tmp = postsData.value!!
                    tmp.addAll(result)
                    postsData.postValue(tmp)
                }
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