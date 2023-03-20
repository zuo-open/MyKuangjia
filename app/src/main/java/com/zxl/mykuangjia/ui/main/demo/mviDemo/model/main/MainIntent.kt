package com.zxl.mvidemo3.model.main

sealed class MainIntent {
    data class InitIntent(val page: Int) : MainIntent()
    data class LoadMore(val page: Int) : MainIntent()
    data class Refresh(val page: Int) : MainIntent()
    data class DELETE(val id: String) : MainIntent()
}
