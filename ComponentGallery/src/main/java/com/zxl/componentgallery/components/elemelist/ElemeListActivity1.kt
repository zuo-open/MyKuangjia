package com.zxl.componentgallery.components.elemelist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zxl.componentgallery.R
import com.zxl.componentgallery.components.elemelist.adapter.ElemeList1LeftAdapter
import com.zxl.componentgallery.components.elemelist.adapter.ElemeList1RightAdapter
import com.zxl.componentgallery.components.elemelist.bean.ElemeBean
import com.zxl.componentgallery.components.elemelist.bean.ElemeListRightBean

class ElemeListActivity1 : AppCompatActivity() {

    private lateinit var mLeftRecyclerView: RecyclerView
    private lateinit var mRightRecyclerView: RecyclerView
    private lateinit var mLeftAdapter: ElemeList1LeftAdapter
    private lateinit var mRightAdapter: ElemeList1RightAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eleme_list1)
        mLeftRecyclerView = findViewById(R.id.rv_left)
        mRightRecyclerView = findViewById(R.id.rv_right)
        initRecyclerView()
        initData()
    }

    private fun initRecyclerView() {
        mLeftAdapter = ElemeList1LeftAdapter(R.layout.item_elemelist1_left)
        mLeftRecyclerView.adapter = mLeftAdapter
        mRightAdapter = ElemeList1RightAdapter()
        mRightRecyclerView.adapter = mRightAdapter

        (mRightRecyclerView.layoutManager as? GridLayoutManager)?.apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    val item = mRightAdapter.getItem(position)
                    return if (item.isBigTitle) {
                        3
                    } else {
                        1
                    }
                }

            }
        }
    }

    private fun initData() {
        val list = mutableListOf<ElemeBean>()
        val list1 = mutableListOf<ElemeListRightBean>()
        repeat(20) {
            list.add(ElemeBean("分类$it"))
            list1.add(ElemeListRightBean("分类$it", true))
            repeat(10) {
                list1.add(ElemeListRightBean("小标签$it"))
            }
        }
        mLeftAdapter.setList(list)
        mRightAdapter.setList(list1)

    }
}