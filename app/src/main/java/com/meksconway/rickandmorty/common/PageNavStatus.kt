package com.meksconway.rickandmorty.common

data class PageNavStatus(
    val title: String?,
    val isBackVisible: Boolean,
    val isFilterBtnVisible: Boolean
) {

    companion object {
        fun rootFragmentStatus(title: String?): PageNavStatus {
            return PageNavStatus(
                title = title,
                isBackVisible = false,
                isFilterBtnVisible = true
            )
        }

        fun childFragmentStatus(title: String?): PageNavStatus {
            return PageNavStatus(
                title = title,
                isBackVisible = true,
                isFilterBtnVisible = false
            )
        }
    }

}