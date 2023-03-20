package com.zxl.mvidemo3.model.main

interface ParticalChange {
    fun reduce(state: ArticalState): ArticalState
}

sealed class InitParticalChange : ParticalChange {
    override fun reduce(state: ArticalState): ArticalState {
        return when (this) {
            is Init -> {
                state.copy(isLoading = true)
            }
            is SUCCESS -> {
                state.copy(artical = data, isLoading = false)
            }
            is FAIL -> {
                state.copy(message = message, isLoading = false)
            }
        }
    }

    object Init : InitParticalChange()//初始化
    data class SUCCESS(val data: Article) : InitParticalChange()//成功
    data class FAIL(val message: String) : InitParticalChange()//失败
}


sealed class MoreParticalChange : ParticalChange {
    override fun reduce(state: ArticalState): ArticalState {
        return when (this) {
            is Init -> {
                state.copy(isLoading = true)
            }
            is SUCCESS -> {
                state.artical?.apply {
                    data.datas = this.datas + data.datas
                }
                state.copy(artical = data, isLoading = false)
            }
            is FAIL -> {
                state.copy(message = message, isLoading = false)
            }
        }
    }

    object Init : MoreParticalChange()//初始化
    data class SUCCESS(val data: Article) : MoreParticalChange()//成功
    data class FAIL(val message: String) : MoreParticalChange()//失败
}


sealed class RefreshParticalChange : ParticalChange {
    override fun reduce(state: ArticalState): ArticalState {
        return when (this) {
            is Init -> {
                state.copy(isLoading = true)
            }
            is SUCCESS -> {
                state.copy(artical = data, isLoading = false)
            }
            is FAIL -> {
                state.copy(message = message, isLoading = false)
            }
        }
    }

    object Init : RefreshParticalChange()//初始化
    data class SUCCESS(val data: Article) : RefreshParticalChange()//成功
    data class FAIL(val message: String) : RefreshParticalChange()//失败
}


sealed class DeleteParticalChange : ParticalChange {
    override fun reduce(state: ArticalState): ArticalState {
        return when (this) {
            is SUCCESS -> {
                state.artical?.apply {
                    datas = datas.filterNot { it.id == id }
                }
                state.copy(artical = state.artical)

            }
            is FAIL -> {
                state.copy(message = message)
            }
        }
    }

    data class SUCCESS(val id: String) : DeleteParticalChange()
    data class FAIL(val message: String) : DeleteParticalChange()
}