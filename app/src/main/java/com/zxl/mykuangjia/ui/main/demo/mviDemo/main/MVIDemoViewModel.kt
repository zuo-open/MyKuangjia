package com.zxl.mvidemo3.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zxl.mvidemo3.model.main.*
import com.zxl.mykuangjia.ui.main.demo.mviDemo.main.MVIRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MVIDemoViewModel(val repository: MVIRepository) : ViewModel() {


    private val _event = Channel<Event.ReportEvent.DeleteReportEvent>()

    val event = _event.receiveAsFlow()

    private val _intents = MutableSharedFlow<MainIntent>()

    val intent = _intents.onEach {
        Log.e("result", it.toString())
    }.intentToStateFlow().scanEvent().scan(ArticalState.initial) { state, particalChange ->
        particalChange.reduce(state)
    }.onEach {
        Log.e("result", it.toString())
    }.flowOn(Dispatchers.IO).stateIn(
        viewModelScope,
        SharingStarted.Eagerly, ArticalState.initial
    )


//    /**
//     * 获取数据
//     */
//    @OptIn(FlowPreview::class)
//    fun getArticle(page: Int) =
//        flowOf(
//            repository.getArticleFromRemote(page),
//            repository.getArticalFromLocal()
//        ).flattenMerge().transformWhile {
//            emit(it.article)
//            //true
//            it.abort
//        }.map {
//            // 1 / 0
//            ArticalState(it, false)
//        }.catch {
//            if (it is Exception) {
//                //这里发射新数据会将初始值进行返回
//                emit(ArticalState(null, false, it.message ?: "服务器异常，请稍后重试"))
//            }
//        }

    /**
     * 接收界面发送过来的意图,并向流中发射数据
     */
    fun receiveIntents(intent: MainIntent) {
        viewModelScope.launch {
            _intents.emit(intent)
        }
    }

    /**
     * 将意图(intent)转换成流(state)
     */
    @OptIn(FlowPreview::class)
    private fun Flow<MainIntent>.intentToStateFlow() =
        merge(filterIsInstance<MainIntent.InitIntent>().flatMapConcat {
            it.toParticalChange()
        }, filterIsInstance<MainIntent.LoadMore>().flatMapConcat {
            it.toParticalChange()
        }, filterIsInstance<MainIntent.Refresh>().flatMapConcat {
            it.toParticalChange()
        }, filterIsInstance<MainIntent.DELETE>().flatMapConcat {
            it.toParticalChange()
        })


    private fun MainIntent.DELETE.toParticalChange() = flowOf(repository.deleteItem(id)).onStart {

    }.map {
        DeleteParticalChange.SUCCESS(it)
    }


    private fun MainIntent.InitIntent.toParticalChange() =
        flowOf(
            repository.getArticleFromRemote(page),
            repository.getArticalFromLocal()
        ).flattenMerge().transformWhile {
            emit(it.article)
            //true
            it.abort
        }.map { article ->
            // 1 / 0
            takeIf { !article.datas.isNullOrEmpty() }?.let {
                InitParticalChange.SUCCESS(article)
            } ?: kotlin.run {
                InitParticalChange.FAIL("未获取到相关数据")
            }
        }.onStart {
            // emit(InitParticalChange.Init)
        }.catch {
            if (it is Exception) {
                //这里发射新数据会将初始值进行返回
                emit(InitParticalChange.FAIL(it.message ?: "服务器异常，请重试"))
            }
        }

    private fun MainIntent.LoadMore.toParticalChange() =
        flowOf(
            repository.getArticleFromRemote(page),
            repository.getArticalFromLocal()
        ).flattenMerge().transformWhile {
            emit(it.article)
            //true
            it.abort
        }.map { article ->
            // 1 / 0
            takeIf { !article.datas.isNullOrEmpty() }?.let {
                MoreParticalChange.SUCCESS(article)
            } ?: kotlin.run {
                MoreParticalChange.FAIL("未获取到相关数据")
            }
        }.onStart {
            //emit(MoreParticalChange.Init)
        }.catch {
            if (it is Exception) {
                //这里发射新数据会将初始值进行返回
                emit(MoreParticalChange.FAIL(it.message ?: "服务器异常，请重试"))
            }
        }

    private fun MainIntent.Refresh.toParticalChange() =
        flowOf(
            repository.getArticleFromRemote(page),
            repository.getArticalFromLocal()
        ).flattenMerge().transformWhile {
            emit(it.article)
            //true
            it.abort
        }.map { article ->
            // 1 / 0
            takeIf { !article.datas.isNullOrEmpty() }?.let {
                RefreshParticalChange.SUCCESS(article)
            } ?: kotlin.run {
                RefreshParticalChange.FAIL("未获取到相关数据")
            }
        }.onStart {
            //emit(RefreshParticalChange.Init)
        }.catch {
            if (it is Exception) {
                //这里发射新数据会将初始值进行返回
                emit(RefreshParticalChange.FAIL(it.message ?: "服务器异常，请重试"))
            }
        }

    private fun Flow<ParticalChange>.scanEvent() =
        onEach {
            val event = when (it) {
                is DeleteParticalChange.SUCCESS -> {
                    Event.ReportEvent.DeleteReportEvent("删除成功")
                }
                is DeleteParticalChange.FAIL -> {
                    val fail = it
                    fail.message
                    Event.ReportEvent.DeleteReportEvent(message = fail.message)
                }
                else -> {
                    return@onEach
                }

            }
            _event.send(event)

        }


}


