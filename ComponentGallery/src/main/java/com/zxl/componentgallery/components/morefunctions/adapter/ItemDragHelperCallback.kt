package com.zxl.componentgallery.components.morefunctions.adapter

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.zxl.componentgallery.components.morefunctions.bean.TAG_TITLE
import java.util.*

/**
 * RecyclerView 滑动监听类
 */
class ItemDragHelperCallback(val mAdapter: MoreFunctionAdapter) :
    ItemTouchHelper.Callback() {

    /**
     * 设置滑动类型标记
     *
     * 返回一个整数类型的标识，用于判断Item那种移动行为是允许的
     */
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return if (mAdapter.isDragEnable(viewHolder.bindingAdapterPosition)) {
            makeMovementFlags(
                ItemTouchHelper.RIGHT
                        or ItemTouchHelper.LEFT or ItemTouchHelper.DOWN or ItemTouchHelper.UP, 0
            )
        } else {
            makeMovementFlags(0, 0)
        }
    }


    /**
     * 拖拽切换Item的回调
     *
     * @return 如果Item切换了位置，返回true；反之，返回false
     */
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        var flag = false
        val fromPosition = viewHolder.layoutPosition //得到拖动ViewHolder的position

        val toPosition = target.layoutPosition//得到目标ViewHolder的position
        mAdapter.data.indexOfFirst { it.type == TAG_TITLE }.takeIf { it != -1 && it > toPosition }
            ?.also {
                if (fromPosition < toPosition) {
                    for (i in fromPosition until toPosition) {
                        Collections.swap(mAdapter.data, i, i + 1)
                    }
                } else {
                    for (i in fromPosition downTo toPosition + 1) {
                        Collections.swap(mAdapter.data, i, i - 1)
                    }
                }
                mAdapter.notifyItemMoved(fromPosition, toPosition)
                flag = true
            } ?: kotlin.run {
            flag = false
        }
        return flag

    }


    override fun isLongPressDragEnabled(): Boolean {
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
    }
}