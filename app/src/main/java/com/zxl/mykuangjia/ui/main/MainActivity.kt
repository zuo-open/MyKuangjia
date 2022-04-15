package com.zxl.mykuangjia

import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zxl.basecommon.base.BaseActivity
import com.zxl.mykuangjia.adapter.MenuAdapter
import com.zxl.mykuangjia.adapter.OnMenuItemClickListener
import com.zxl.mykuangjia.bean.MenuBean
import com.zxl.mykuangjia.databinding.ActivityMainBinding
import org.koin.android.ext.android.get

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>(), View.OnClickListener {
    private lateinit var mBtn1: AppCompatButton
    private lateinit var mBtn2: AppCompatButton
    private lateinit var mMenuDatas: MutableList<MenuBean>
    private lateinit var mMenuRecyclerView: RecyclerView
    private lateinit var mMenuLayoutManager: GridLayoutManager
    private var currentPos = 0

    override fun initData() {
        super.initData()
        mMenuDatas = mViewModel.initMenuData()
        //初始化fragment
        mViewModel.initFragment(
            supportFragmentManager,
            mMenuDatas,
            mDataBinding.frContainer.id,
            currentPos
        )
//        supportFragmentManager.beginTransaction().apply {
//            val homeFragment = HomeFragment()
//            add(mDataBinding.frContainer.id, homeFragment, "1")
//            show(homeFragment)
//            commitAllowingStateLoss()
//        }
    }

    override fun initView() {
        super.initView()
        mMenuLayoutManager = GridLayoutManager(this, mMenuDatas.size, RecyclerView.VERTICAL, false)
        mMenuRecyclerView = mDataBinding.rvNavigation
        mMenuRecyclerView.layoutManager = mMenuLayoutManager
        mViewModel.mMenuAdapter = MenuAdapter(mMenuDatas)
        mMenuRecyclerView.adapter = mViewModel.mMenuAdapter
        mViewModel.mMenuAdapter.setOnMenuItemClickListener(object : OnMenuItemClickListener {
            override fun onMenuItemClick(data: MenuBean, position: Int) {
                mViewModel.bottomNavigationChange(position)
                mViewModel.switFragment(supportFragmentManager, data, currentPos)
                currentPos = position
            }
        })
    }


    override fun onPause() {
        super.onPause()

    }

    override fun onClick(v: View) {


    }

    override fun obtainViewModel(): MainViewModel = get()

    override fun getLayoutId(): Int = R.layout.activity_main
    override fun initViewModelId(): Int = BR.viewModel
}