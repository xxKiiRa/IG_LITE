package com.saif.iglite.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val username: String,
    val caption: String,
    val imageUri: String,
    val createdAt: Long = System.currentTimeMillis()
) : Parcelable