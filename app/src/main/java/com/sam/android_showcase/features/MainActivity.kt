package com.sam.android_showcase.features

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sam.android_showcase.R
import com.sam.android_showcase.features.posts.PostsFragment
import com.sam.android_showcase.utills.addFragment
import com.sam.android_showcase.utills.appExitDialog
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            addFragment(PostsFragment())
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        try {
            if (supportFragmentManager.backStackEntryCount > 0)
                supportFragmentManager.popBackStack()
            else
                this.appExitDialog()
        } catch (e: Exception) { e.printStackTrace() }
    }
}