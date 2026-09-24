package com.example.exp8

enum class ImageSourceType {
    DRAWABLE,
    LOCAL_STORAGE,
    URL
}

data class ImageItem(
    val id: String,
    val title: String,
    val sourceType: ImageSourceType,
    val drawableResId: Int? = null,
    var filePath: String? = null,
    val url: String? = null,
    var isSelected: Boolean = false
)
