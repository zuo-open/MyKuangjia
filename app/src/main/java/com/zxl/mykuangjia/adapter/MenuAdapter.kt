package com.zxl.mykuangjia.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zxl.mykuangjia.R
import com.zxl.mykuangjia.bean.MenuBean

class MenuAdapter(val datas: MutableList<MenuBean>) : RecyclerView.Adapter<MenuViewHolder>() {

    private var listener: OnMenuItemClickListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_menu, parent, false)
        return MenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val data = datas.get(position)
        when (data.enable) {
            true -> {
                Glide.with(holder.itemView).load(R.drawable.menu_press).into(holder.ivHead)
            }
            false -> {
                Glide.with(holder.itemView).load(R.drawable.menu_normal).into(holder.ivHead)
            }
        }
        holder.tvTitle.setText(data.title)
        holder.itemView.setOnClickListener {
            listener?.apply {
                //menu按键点击
                onMenuItemClick(data, holder.absoluteAdapterPosition)
            }
        }


    }

    override fun getItemCount(): Int = datas.size


    fun setOnMenuItemClickListener(listener: OnMenuItemClickListener) {
        this.listener = listener
    }

}

//menu按键点击
interface OnMenuItemClickListener {
    fun onMenuItemClick(data: MenuBean, position: Int)
}


class MenuViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val ivHead = view.findViewById<ImageView>(R.id.iv_head)
    val tvTitle = view.findViewById<TextView>(R.id.tv_title)
}