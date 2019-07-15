package demo.rlv.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import demo.rlv.cehome.com.myapplication.R;

/**
 * @author yangzc
 * @data 2019/7/15 17:54
 * @desc
 */
public class RlvJavaAdapter extends BaseRecycleViewAdapter<String> {


    protected RlvJavaAdapter(Context context, List<String> list) {
        super(context, list);
    }

    @Override
    protected int getViewResource() {
        return R.layout.item_normal;
    }

    @Override
    protected RecyclerView.ViewHolder getRecycleViewHolder(View view) {
        return new RlvViewHolder(view);
    }

    @Override
    protected void dataRead(RecyclerView.ViewHolder holder, int position) {
        RlvViewHolder mRlvViewHolder = (RlvViewHolder) holder;
        mRlvViewHolder.textView.setText(mList.get(position));
    }


    @Override
    protected int getViewHeaderResource() {
        return R.layout.item_head;
    }


    @Override
    protected void dataReadByHeader(RecyclerView.ViewHolder holder, int position) {
        RlvHeadViewHolder mRlvHeadViewHolder = (RlvHeadViewHolder) holder;
        mRlvHeadViewHolder.tv_head.setText("这个是头部");

    }

    private static class RlvViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;

        public RlvViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv);
        }
    }


    private static class RlvHeadViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_head;

        public RlvHeadViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_head = itemView.findViewById(R.id.tv_head);
        }
    }

}
