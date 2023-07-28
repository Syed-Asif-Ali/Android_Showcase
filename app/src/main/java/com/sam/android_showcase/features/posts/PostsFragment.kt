package com.sam.android_showcase.features.posts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.sam.android_showcase.GetPostsQuery
import com.sam.android_showcase.databinding.FragmentPostsBinding
import com.sam.android_showcase.utills.enableBackButton
import com.sam.android_showcase.utills.replaceFragmentWithAnim
import com.sam.android_showcase.utills.setActionBarText
import org.koin.android.viewmodel.ext.android.sharedViewModel
import java.lang.Exception

class PostsFragment : Fragment() {

    private var _binding: FragmentPostsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PostsViewModel by sharedViewModel()
    private var mAdapter: PostAdapter? = null
    private val postList = ArrayList<GetPostsQuery.Data1>()
    private var snackbar: Snackbar? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentPostsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().enableBackButton(false)
        requireActivity().setActionBarText("Posts")

        initListView()
        addViewModelObservers()

        //Load Data if listview is empty
        if(postList.isEmpty() && viewModel.postsData.value == null) viewModel.getPosts()
    }

    private fun addViewModelObservers(){
        with(viewModel) {
            postsData.observe(viewLifecycleOwner) {
                if (it != null) {
                    postList.clear()
                    postList.addAll(it)
                    mAdapter?.notifyDataSetChanged()
                }
            }

            messageData.observe(viewLifecycleOwner, {
                if(it == null) return@observe

                if(viewModel.isAllDataLoaded) {
                    Toast.makeText(requireContext(), it, LENGTH_LONG).show()
                    return@observe
                }

                snackbar = Snackbar.make(binding.root, it, Snackbar.LENGTH_INDEFINITE)
                    .setAction("Retry!") {
                        viewModel.getPosts()
                    }
                snackbar!!.show()
            })

            showProgressbar.observe(viewLifecycleOwner, { isVisible ->
                binding.startloader.visibility = if (postList.isEmpty() && isVisible) VISIBLE else GONE
                binding.bottomloader.visibility = if (postList.isNotEmpty() && isVisible) VISIBLE else GONE
            })
        }
    }

    private fun initListView(){
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        mAdapter = PostAdapter(postList)
        binding.recyclerview.adapter = mAdapter

        //Listener to load data from Bottom
        binding.recyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    if(snackbar == null || !snackbar!!.isShown) viewModel.getPosts()
                }
            }
        })

        mAdapter!!.onItemClick = { item ->
            viewModel.setSelectedItem(item)
            replaceFragmentWithAnim(PostDetailFragment())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        try {
            _binding = null
            mAdapter = null
            if (snackbar != null && snackbar!!.isShown) {
                snackbar!!.dismiss()
                snackbar = null
            }
            viewModel.messageData.value = null
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    companion object {
        private val TAG = PostsFragment::class.java.name
    }
}