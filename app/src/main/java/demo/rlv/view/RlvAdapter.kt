package demo.rlv.cehome.com.alldemo.view

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import demo.rlv.cehome.com.myapplication.R
import demo.rlv.view.BaseRecycleViewAdapter


/**
 * @author yangzc
 * @data 2019/3/14 10:56
 * @desc
 */
open class RlvAdapter(context: Context, list: List<String>) : BaseRecycleViewAdapter<String>(context, list as MutableList<String>?) {

    override fun getViewResource(): Int {
        return R.layout.item_normal
    }


    override fun getViewHeaderResource(): Int {
        return return R.layout.item_head
    }

    override fun getRecycleViewHolder(view: View): RecyclerView.ViewHolder {
        return RlvViewHolder(view)
    }

    override fun dataRead(holder: RecyclerView.ViewHolder, position: Int) {
        val h = holder as RlvViewHolder
        h.tv.text = mList[position]
    }

    override fun getRecycleViewHeaderHolder(view: View): RecyclerView.ViewHolder {
        return RlvHeadViewHolder(view)
    }

    override fun dataReadByHeader(holder: RecyclerView.ViewHolder?, position: Int) {
        val h = holder as RlvHeadViewHolder
        h.tv_head.text = "这个是头部"
    }


    private inner class RlvViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal val tv: TextView = itemView.findViewById(R.id.tv)
    }

    private inner class RlvHeadViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal val tv_head: TextView = itemView.findViewById(R.id.tv_head)
    }


}
