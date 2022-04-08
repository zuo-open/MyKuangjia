package com.zxl.mykuangjia

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import com.zxl.basecommon.base.BaseViewModel
import com.zxl.mykuangjia.adapter.MenuAdapter
import com.zxl.mykuangjia.bean.MenuBean
import com.zxl.mykuangjia.factory.MenuFragmentFactory
import java.lang.Exception

class MainViewModel : BaseViewModel() {

    lateinit var mMenuAdapter: MenuAdapter

    var menuData = mutableListOf<MenuBean>()

    //初始化菜单数据
    fun initMenuData(): MutableList<MenuBean> {
        menuData = mutableListOf<MenuBean>()
        menuData.add(MenuBean("1", "首页", "", "", true, MenuFragmentFactory.TAG_HOME))
        menuData.add(MenuBean("2", "组件", "", "", false, MenuFragmentFactory.TAG_COMPONENT))
        menuData.add(MenuBean("3", "服务", "", "", false, MenuFragmentFactory.TAG_SERVICE))
        return menuData
    }

    //初始化fragment
    fun initFragment(
        fragmentManager: FragmentManager,
        datas: MutableList<MenuBean>,
        id: Int,
        currentPos: Int
    ) {
        fragmentManager.beginTransaction().apply {
            datas.forEachIndexed { index, menuBean ->
                var fragment = MenuFragmentFactory.getFragmentByUrl(menuBean)
                val findFragmentByTag = fragmentManager.findFragmentByTag(menuBean.id ?: "")
                if (findFragmentByTag != null && findFragmentByTag.isAdded) {
                    fragment = findFragmentByTag
                } else {
                    add(R.id.fr_container, fragment, menuBean.id ?: "")
                }
                if (currentPos == index) {
                    show(fragment)
                } else {
                    hide(fragment)
                }
            }

            commitAllowingStateLoss()

        }

    }

    //切换fragment
    fun switFragment(fragmentManager: FragmentManager, menuBean: MenuBean, currentPos: Int) {
        try {
            fragmentManager.beginTransaction().apply {
                //当前需要改变的fragment
                val changeFragment = fragmentManager.findFragmentByTag(menuBean.id ?: "")
                //当前展示的fragment
                val showFragment =
                    fragmentManager.findFragmentByTag(menuData.get(currentPos).id ?: "")
                if (changeFragment != showFragment) {
                    showFragment?.apply {
                        hide(this)
                    }
                    changeFragment?.apply {
                        show(this)
                    }
                }
                commitAllowingStateLoss()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun bottomNavigationChange(position: Int) {
        menuData.forEach {
            it.enable = false
        }
        menuData.filterIndexed { index, menuBean ->
            index == position
        }?.forEach {
            it.enable = true
        }
        mMenuAdapter.notifyDataSetChanged()
    }


}