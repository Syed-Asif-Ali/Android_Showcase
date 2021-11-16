package com.sam.android_showcase.utills

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit
import com.sam.android_showcase.R

/** This file is responsible for handling all transactions related to fragments*/

var frameId: Int = R.id.fragment_container_view

fun FragmentActivity.addFragment(fragment: Fragment): Fragment {
    supportFragmentManager.commit {
        setReorderingAllowed(true)
        add(frameId, fragment)
    }
    return fragment
}

fun FragmentActivity.replaceFragmentWithAnim(fragment: Fragment): Fragment {
    supportFragmentManager.commit {
        setCustomAnimations(R.anim.push_right_in, R.anim.push_right_out, R.anim.push_left_in, R.anim.push_left_out)
        replace(frameId, fragment)
        setReorderingAllowed(true)
        addToBackStack(fragment.javaClass.name)
    }
    return fragment
}

fun Fragment.replaceFragmentWithAnim(fragment: Fragment): Fragment {
    requireActivity().replaceFragmentWithAnim(fragment)
    return fragment
}