package com.saif.iglite.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.saif.iglite.R
import com.saif.iglite.data.PostEntity
import com.saif.iglite.databinding.ItemPostBinding


class PostAdapter(
    private val onEdit: (PostEntity) -> Unit,
    private val onDelete: (PostEntity) -> Unit
) : ListAdapter<PostEntity, PostAdapter.VH>(DIFF) {

    inner class VH(val b: ItemPostBinding) : RecyclerView.ViewHolder(b.root) {
        init {
            b.btnMore.setOnClickListener { showMenu(it) }
        }
        private fun showMenu(v: View) {
            val item = currentList[bindingAdapterPosition]
            PopupMenu(v.context, v).apply {
                menuInflater.inflate(R.menu.menu_post, menu)
                setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.action_edit -> onEdit(item)
                        R.id.action_delete -> onDelete(item)
                    }
                    true
                }
            }.show()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val b = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(b)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val p = getItem(position)
        with(holder.b) {
            tvUsername.text = p.username
            tvCaption.text = p.caption
            Glide.with(ivPhoto).load(p.imageUri).into(ivPhoto)
        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<PostEntity>() {
            override fun areItemsTheSame(o: PostEntity, n: PostEntity) = o.id == n.id
            override fun areContentsTheSame(o: PostEntity, n: PostEntity) = o == n
        }
    }
}

private fun Any.setOnClickListener(function: () -> Unit) {
    TODO("Not yet implemented")
}
