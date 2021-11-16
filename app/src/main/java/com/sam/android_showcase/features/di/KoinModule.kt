package com.sam.android_showcase.features.di

import com.sam.android_showcase.features.network.ApiRepo
import com.sam.android_showcase.features.posts.PostsViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module


val appModule = module {

    single { ApiRepo() }

    viewModel{PostsViewModel(get(), androidApplication())}
}