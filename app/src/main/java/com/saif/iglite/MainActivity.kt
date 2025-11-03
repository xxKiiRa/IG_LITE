package com.saif.iglite

import StoryAdapter
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.saif.iglite.data.PostEntity
import com.saif.iglite.databinding.ActivityMainBinding
import com.saif.iglite.model.SampleStories
import com.saif.iglite.ui.MainViewModel
import com.saif.iglite.ui.PostAdapter
import com.saif.iglite.ui.post.AddEditPostDialog
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var b: ActivityMainBinding
    private val vm: MainViewModel by viewModels()
    private lateinit var postAdapter: PostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)

        ViewCompat.setOnApplyWindowInsetsListener(b.root) { v, insets ->
            val sysBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(sysBars.left, sysBars.top, sysBars.right, sysBars.bottom)
            insets
        }

        b.rvStories.apply {
            layoutManager = LinearLayoutManager(
                this@MainActivity,
                RecyclerView.HORIZONTAL,
                false
            )
            adapter = StoryAdapter(SampleStories.items)
        }

        postAdapter = PostAdapter(
            onEdit = { post -> showAddEditDialog(post) },
            onDelete = { post ->
                vm.delete(post)
                Snackbar.make(b.root, "Hapus ${post.username}", Snackbar.LENGTH_SHORT).show()
            }
        )
        b.rvPosts.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = postAdapter
        }

        lifecycleScope.launch {
            vm.postsFlow.collectLatest { posts ->
                postAdapter.submitList(posts) 
            }
        }

        b.fabAdd.setOnClickListener { showAddEditDialog(null) }
    }

    private fun showAddEditDialog(edit: PostEntity?) {
        val dialog = AddEditPostDialog().apply { editing = edit }
        dialog.show(supportFragmentManager, "AddEdit")

        supportFragmentManager.setFragmentResultListener(
            AddEditPostDialog.RESULT_KEY, this
        ) { _, bundle ->
            val result = bundle.get("post") as PostEntity
            if (edit == null) {
                vm.add(result)
                Snackbar.make(b.root, "Post tersimpan", Snackbar.LENGTH_SHORT).show()
            } else {
                vm.update(result.copy(id = edit.id, createdAt = edit.createdAt))
                Snackbar.make(b.root, "Post diperbarui", Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}
