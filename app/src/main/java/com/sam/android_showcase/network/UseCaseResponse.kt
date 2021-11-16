package com.sam.android_showcase.network

interface UseCaseResponse<Type> {

    fun onSuccess(result: Type)

    fun onError(apiErrorMsg: String?)
}
