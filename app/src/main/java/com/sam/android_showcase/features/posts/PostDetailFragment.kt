package com.sam.android_showcase.features.posts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sam.android_showcase.R
import com.sam.android_showcase.utills.enableBackButton
import org.koin.android.viewmodel.ext.android.sharedViewModel
import androidx.databinding.DataBindingUtil
import com.sam.android_showcase.databinding.FragmentPostDetailBinding


class PostDetailFragment  : Fragment() {

    private val viewModel: PostsViewModel by sharedViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentPostDetailBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_post_detail, container, false)
        binding.postsViewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().enableBackButton(true)
    }

}