package com.sam.android_showcase

import android.app.Application
import com.sam.android_showcase.di.appModule
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import org.junit.Before
import org.junit.Test
import org.koin.android.ext.koin.androidContext
import org.koin.core.logger.Level
import org.koin.dsl.koinApplication
import org.koin.test.AutoCloseKoinTest
import org.koin.test.check.checkModules

class KoinTest : AutoCloseKoinTest() {

    @MockK
    lateinit var context: Application

    @Before
    fun setUp() {
        MockKAnnotations.init(this) //for initialization
    }

    @Test
    fun testCoreModule() {
        koinApplication {
            printLogger(Level.DEBUG)
            androidContext(context)
            modules(listOf(appModule))
        }.checkModules()
    }
}