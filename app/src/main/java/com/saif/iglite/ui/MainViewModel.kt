package com.saif.iglite.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.saif.iglite.data.AppDatabase
import com.saif.iglite.data.PostEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(app: Application) : AndroidViewModel(app) {
    private val dao = AppDatabase.get(app).postDao()

    val postsFlow = dao.getAll().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = emptyList()
    )

    fun add(post: PostEntity) = viewModelScope.launch { dao.insert(post) }
    fun update(post: PostEntity) = viewModelScope.launch { dao.update(post) }
    fun delete(post: PostEntity) = viewModelScope.launch { dao.delete(post) }
}