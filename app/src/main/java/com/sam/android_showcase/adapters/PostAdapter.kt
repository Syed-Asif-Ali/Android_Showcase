package com.sam.android_showcase.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.sam.android_showcase.GetPostsQuery
import com.sam.android_showcase.R
import com.sam.android_showcase.databinding.ListItemPostsBinding

class PostAdapter(private val dataSet: ArrayList<GetPostsQuery.Data1>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onItemClick: ((GetPostsQuery.Data1) -> Unit)? = null

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val listItemPostsBinding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(viewGroup.context), R.layout.list_item_posts, viewGroup, false
        )
        return PostViewHolder(listItemPostsBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PostViewHolder).onBind(getItem(position))
    }

    private fun getItem(position: Int): GetPostsQuery.Data1 = dataSet[position]

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

    private inner class PostViewHolder(private val viewDataBinding: ViewDataBinding) : RecyclerView.ViewHolder(viewDataBinding.root) {
        fun onBind(post: GetPostsQuery.Data1) {
            (viewDataBinding as ListItemPostsBinding).post = post
        }

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(dataSet[adapterPosition])
            }
        }
    }
}