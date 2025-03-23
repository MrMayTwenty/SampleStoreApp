package com.aaa.samplestore.presentation

data class ViewModelState<T>(
    val isLoading: Boolean = false,
    val data: T? = null,
    val error: String? = null
)