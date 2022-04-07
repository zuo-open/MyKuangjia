package com.zxl.componentgallery.components.morefunctions


import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.zxl.basecommon.base.BaseActivity
import com.zxl.componentgallery.BR
import com.zxl.componentgallery.R
import com.zxl.componentgallery.components.morefunctions.adapter.ItemDragHelperCallback
import com.zxl.componentgallery.components.morefunctions.adapter.MoreFunctionAdapter
import com.zxl.componentgallery.components.morefunctions.bean.*
import com.zxl.componentgallery.databinding.ActivityMoreFunctionsBinding
import org.koin.android.ext.android.get


class MoreFunctionsActivity : BaseActivity<MoreFunctionViewModel, ActivityMoreFunctionsBinding>() {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: MoreFunctionAdapter
    private lateinit var mDatas: MutableList<FunctionBean>


    override fun getLayoutId(): Int = R.layout.activity_more_functions


    override fun initView() {
        super.initView()
        initRecyclerView()
        initData1()
    }

    private fun initData1() {
        mDatas = mutableListOf<FunctionBean>()
        mDatas.add(FunctionBean("我的单据", "", TAG_FUNCTION, true, TAG_WDYW))
        mDatas.add(FunctionBean("出差申请", "", TAG_FUNCTION, true, TAG_YDBG))
        mDatas.add(FunctionBean("商旅预订", "", TAG_FUNCTION, true, TAG_YXGL))
        mDatas.add(FunctionBean("差旅报销", "", TAG_FUNCTION, true, TAG_WDYW))
        mDatas.add(FunctionBean("离店确认", "", TAG_FUNCTION, true, TAG_YDBG))
        mDatas.add(FunctionBean("报销授权", "", TAG_FUNCTION, true, TAG_YXGL))
        mDatas.add(FunctionBean("行程变更", "", TAG_FUNCTION, true, TAG_WDYW))
        mDatas.add(FunctionBean("个人报销", "", TAG_FUNCTION, true, TAG_YDBG))
        mDatas.add(FunctionBean("全部应用", "", TAG_TITLE))

        mDatas.add(FunctionBean("我的业务", "", TAG_SUBTITLE))
        mDatas.add(FunctionBean("会议申请", "", TAG_FUNCTION))
        mDatas.add(FunctionBean("培训申请", "", TAG_FUNCTION))
        mDatas.add(FunctionBean("通用申请", "", TAG_FUNCTION))
        mDatas.add(FunctionBean("非业务表单", "", TAG_FUNCTION))
        mDatas.add(FunctionBean("长庆商旅", "", TAG_FUNCTION))
        mDatas.add(FunctionBean("个人借款", "", TAG_FUNCTION))
        mDatas.add(FunctionBean("邮寄申请", "", TAG_FUNCTION))
        mDatas.add(FunctionBean("对公报销", "", TAG_FUNCTION))
        mDatas.add(FunctionBean("商旅分析", "", TAG_FUNCTION))

        mDatas.add(FunctionBean("影像管理", "", TAG_SUBTITLE))
        mDatas.add(FunctionBean("票夹", "", TAG_FUNCTION))
        mDatas.add(FunctionBean("发票助手", "", TAG_FUNCTION))
        mDatas.add(FunctionBean("扫一扫", "", TAG_FUNCTION))

        mDatas.add(FunctionBean("移动办公", "", TAG_SUBTITLE))
        mDatas.add(FunctionBean("项目沙盘", "", TAG_FUNCTION))
        mAdapter.setList(mDatas)

    }

    private fun initRecyclerView() {
        mRecyclerView = mDataBinding.rv
        mRecyclerView.layoutManager = GridLayoutManager(this, 5, RecyclerView.VERTICAL, false)
        //mRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        mAdapter = MoreFunctionAdapter()
        mAdapter.setGridSpanSizeLookup { gridLayoutManager, viewType, position ->
            val type = mAdapter.getItem(position).type
            return@setGridSpanSizeLookup if (type == TAG_FUNCTION) {
                1
            } else {
                5
            }

        }
        mRecyclerView.adapter = mAdapter
        ItemTouchHelper(ItemDragHelperCallback(mAdapter))
            .attachToRecyclerView(mRecyclerView)

        mAdapter.setOnItemClickListener { adapter, view, position ->
//            val tempData= mutableListOf<FunctionBean>()
            adapter.let { adapter as? MoreFunctionAdapter }?.also {
                val data = it.data
                val titlePosition = data.indexOfFirst { it.type == TAG_TITLE }
                val removeAt = data.removeAt(position)
                if (position < titlePosition) {
                    removeAt.show = false
                    data.add(removeAt)
                    mAdapter.notifyItemMoved(position, data.size - 1)
                    mAdapter.notifyItemChanged(position)
                    mAdapter.notifyItemChanged(data.size - 1)
                } else {
                    removeAt.show = true
                    data.add(titlePosition, removeAt)
                    mAdapter.notifyItemMoved(position, titlePosition)
                    mAdapter.notifyItemChanged(position)
                    mAdapter.notifyItemChanged(titlePosition)
                }
            }


        }
    }


    override fun obtainViewModel(): MoreFunctionViewModel = get()
    override fun initViewModelId(): Int = BR.viewModel


}