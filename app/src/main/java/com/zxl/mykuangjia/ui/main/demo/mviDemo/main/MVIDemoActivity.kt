package com.zxl.mykuangjia.ui.main.demo.mviDemo.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.zxl.mvidemo3.main.MVIDemoViewModel
import com.zxl.mvidemo3.model.main.ArticalState
import com.zxl.mvidemo3.model.main.MainIntent
import com.zxl.mykuangjia.R
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get

class MVIDemoActivity : AppCompatActivity() {

    private val mViewModel: MVIDemoViewModel = get()
    private lateinit var rv: RecyclerView
    private lateinit var mAdapter: ArticalAdapter
    private lateinit var mRefreshLayout: RefreshLayout
    private lateinit var btnLoading: AppCompatButton
    private var page = 0

    private val intents by lazy {
        merge(
            //flowOf(MainIntent.InitIntent(page)),//初始化数据
            //加载首页
            refreshData(),
            //删除条目
            deletItem()
        )
    }

    private fun deletItem(): Flow<MainIntent> = callbackFlow {
        mAdapter.setOnItemClickListener { adapter, view, position ->
            val id = mAdapter.data.get(position).id
            trySend(MainIntent.DELETE(id))
        }
        awaitClose { mAdapter.setOnItemClickListener(null) }
    }

    private fun refreshData() = callbackFlow {
        mRefreshLayout.setOnRefreshListener {
            page = 0
            trySend(MainIntent.Refresh(page))
        }
        mRefreshLayout.setOnLoadMoreListener {
            page++
            trySend(MainIntent.LoadMore(page))
        }
        awaitClose {
            Log.e("result", "结束了")
        }
//        btnLoading.setOnClickListener {
//            page++
//            trySend(MainIntent.InitIntent(page))
//        }
//        awaitClose {
//            btnLoading.setOnClickListener(null)
//        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mvi)
        rv = findViewById(R.id.rv)
        mRefreshLayout = findViewById(R.id.refreshLayout)
        btnLoading = findViewById(R.id.btn_load)
        mAdapter = ArticalAdapter(mutableListOf())
        rv.adapter = mAdapter
//        mViewModel.getArticle(1).collectIn(this) {
//            showArtical(it)
//        }

        intents.onEach {
            mViewModel.receiveIntents(it)
        }.launchIn(lifecycleScope)

        mViewModel.intent.collectIn(this) {
            showArtical(it)
        }

        mViewModel.event.collectIn(this) {
            Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
        }


//        lifecycleScope.launch {
//            repeatOnLifecycle(Lifecycle.State.STARTED) {
//                mViewModel.getArticle(1).collect {
//                    Log.e("result", it.toString())
//                }
//            }
//        }
    }

    private fun showArtical(articalModel: ArticalState) {

        articalModel.apply {
            takeIf { isLoading }?.let {
                Log.e("result", "正在加载中...")
                if (!mRefreshLayout.getLoadingState()) {
                    mRefreshLayout.autoRefresh()
                }
            } ?: kotlin.run {
                Log.e(
                    "result", "数据加载完成..."
                )
            }
            message?.apply {
                mRefreshLayout.finishLoading()
                Log.e(
                    "result", this
                )
                Toast.makeText(this@MVIDemoActivity, message, Toast.LENGTH_SHORT).show()
            }
            artical?.apply {
                Log.e(
                    "result", "获取到相关数据${this}"
                )
                if (mRefreshLayout.isRefreshing) {
                    mRefreshLayout.finishLoading()
                    mAdapter.setNewInstance(datas.toMutableList())

                } else if (mRefreshLayout.isLoading) {
                    mRefreshLayout.finishLoading()
                    mAdapter.data.addAll(datas.toMutableList())
                    mAdapter.notifyDataSetChanged()
                } else {
                    mAdapter.setNewInstance(datas.toMutableList())
                }

            }
        }
    }
}


fun RefreshLayout.finishLoading() {
    if (isLoading) {
        finishLoadMore()
    } else if (isRefreshing) {
        finishRefresh()
    }
}


fun RefreshLayout.getLoadingState(): Boolean {
    return isLoading || isRefreshing
}

fun <T> Flow<T>.collectIn(
    lifecycleOwner: LifecycleOwner,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    action: (T) -> Unit
): Job = lifecycleOwner.lifecycleScope.launch {
    flowWithLifecycle(lifecycleOwner.lifecycle, minActiveState).onEach { action(it) }.collect()
}
