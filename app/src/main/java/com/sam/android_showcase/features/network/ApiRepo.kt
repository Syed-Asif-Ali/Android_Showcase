package com.sam.android_showcase.features.network

import android.content.Context
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.sam.android_showcase.GetPostsQuery
import com.sam.android_showcase.apolloClient
import com.sam.android_showcase.type.PageQueryOptions
import com.sam.android_showcase.type.PaginateOptions

class ApiRepo {

    //-- Get Posts
    fun getPosts(page: Int, limit: Int, context: Context, listener: UseCaseResponse<ArrayList<GetPostsQuery.Data1>>) {
        var posts = ArrayList<GetPostsQuery.Data1>()

        var options = PageQueryOptions(paginate = Input.optional(
            PaginateOptions(
                Input.optional(page),
                Input.optional(limit))
        ))

        apolloClient(context).query(GetPostsQuery(Input.optional(options)))
            .enqueue(object : ApolloCall.Callback<GetPostsQuery.Data>() {
                override fun onResponse(response: Response<GetPostsQuery.Data>) {
                    val resPosts = response.data?.posts?.data?.filterNotNull()
                    if(resPosts != null && resPosts.isNotEmpty()) {
                        posts.addAll(resPosts)
                        listener.onSuccess(posts)
                    }
                    else {listener.onError("No More Data Found")}
                }

                override fun onFailure(e: ApolloException) {
                    listener.onError(e.message)
                }
            })
    }
}