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
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sam.android_showcase.GetPostsQuery
import com.sam.android_showcase.R
import com.sam.android_showcase.adapters.PostAdapter
import com.sam.android_showcase.databinding.FragmentPostsBinding
import com.sam.android_showcase.utills.enableBackButton
import com.sam.android_showcase.utills.isNetworkAvailable
import com.sam.android_showcase.utills.replaceFragmentWithAnim
import org.koin.android.viewmodel.ext.android.sharedViewModel

class PostsFragment : Fragment() {

    private var _binding: FragmentPostsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PostsViewModel by sharedViewModel()

    var mAdapter: PostAdapter? = null
    val postList = ArrayList<GetPostsQuery.Data1>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentPostsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().enableBackButton(false)

        initListView()

        // Add Observers
        with(viewModel) {
            postsData.observe(viewLifecycleOwner, Observer {
                if(it != null){
                    postList.clear()
                    postList.addAll(it)
                    mAdapter?.notifyDataSetChanged()
                }
            })

            messageData.observe(viewLifecycleOwner, Observer {
                Toast.makeText(requireContext(), it, LENGTH_LONG).show()
            })

            showProgressbar.observe(viewLifecycleOwner, Observer { isVisible ->
                if(postList.isEmpty()) {
                    binding.startloader.visibility = if (isVisible) VISIBLE else GONE
                    binding.bottomloader.visibility = GONE
                }
                else {
                    binding.startloader.visibility = GONE
                    binding.bottomloader.visibility = if (isVisible) VISIBLE else GONE
                }
            })
        }

        //Load Data
        if (requireContext().isNetworkAvailable()) {
            if(postList.isEmpty() && viewModel.postsData.value == null) viewModel.getPosts()
        } else {
            Toast.makeText(requireContext(), getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show()
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
                    viewModel.getPosts()
                }
            }
        })

        mAdapter!!.onItemClick = { item ->
            viewModel.setSelectedItem(item)
            replaceFragmentWithAnim(PostDetailFragment())
        }
    }

    override fun onDestroyView() {
        mAdapter = null
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private val TAG = PostsFragment::class.java.name
    }
}