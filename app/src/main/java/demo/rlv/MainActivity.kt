package demo.rlv.cehome.com.myapplication

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import demo.rlv.cehome.com.alldemo.view.RlvAdapter
import demo.rlv.view.BaseRecycleViewAdapter
import kotlinx.android.synthetic.main.activity_main.*

private val Tag = "RLV"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initData()

    }

    private fun initData() {
        val list = ArrayList<String>()
        for (i in 0..50) {
            list.add(i.toString() + "")
        }

        val mRlvAdapter = RlvAdapter(this, list)
        rlv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rlv.adapter = mRlvAdapter
        mRlvAdapter.moreType = BaseRecycleViewAdapter.MORE_TYPE.LOAD_ERROR
        mRlvAdapter.setMoreListener {
            Log.d(Tag, "LoadMore")
        }
        mRlvAdapter.setOnHeaderItemClickListener { view, position, entity -> Log.d(Tag, "HeadView") }
        mRlvAdapter.setOnItemClickListener { view, position, entity ->
            Log.d(Tag, "Item$entity")
        }


    }

}
