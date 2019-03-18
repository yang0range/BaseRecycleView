package demo.rlv.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import demo.rlv.cehome.com.myapplication.R;


/**
 *
 * RecyclerView Adatper 基类
 */
public abstract class BaseRecycleViewAdapter<T> extends RecyclerView.Adapter {

    public static final int TYPE_NORMAL = 0;

    public static final int TYPE_HEADER = 1;

    public static final int TYPE_FOOTER = 2;

    public static final int TYPE_AUTO_MORE = 3;

    private AutoMoreListener mMoreListener;

    protected List<T> mList;

    protected Context mContext;

    protected OnItemClickListener<T> mOnItemClickListener;

    protected OnItemLongClickListener mOnItemLongClickListener;

    protected OnHeaderItemClickListener mOnHeaderItemClickListener;

    private MORE_TYPE mMoreType = MORE_TYPE.AUTO_LOAD;

    public enum MORE_TYPE {
        AUTO_LOAD, LOAD_END, LOAD_ERROR
    }

    protected BaseRecycleViewAdapter(Context context, List<T> list) {
        mContext = context;
        mList = list;
    }

    protected int getRealPosition(int position) {
        if (getViewHeaderResource() > 0) {
            return position - 1;
        } else {
            return position;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (getMoreLoadLayoutResId() > 0 && position == getItemCount() - 1) {
            return TYPE_AUTO_MORE;
        }
        if (position == 0) {
            if (mList == null || mList.isEmpty()) {
                if (getViewHeaderResource() > 0) {
                    return TYPE_HEADER;
                } else if (getFooterViewResource() > 0) {
                    return TYPE_FOOTER;
                } else {
                    return TYPE_NORMAL;
                }
            } else {
                if (getViewHeaderResource() > 0) {
                    return TYPE_HEADER;
                } else {
                    return TYPE_NORMAL;
                }
            }
        } else if (position == getItemCount() - 1) {
            if (getFooterViewResource() > 0) {
                return TYPE_FOOTER;
            } else {
                return TYPE_NORMAL;
            }
        } else {

            return TYPE_NORMAL;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            return getRecycleViewHeaderHolder(LayoutInflater.from(mContext).inflate(getViewHeaderResource(), parent, false));
        } else if (viewType == TYPE_FOOTER) {
            return getRecycleViewFooterHolder(LayoutInflater.from(mContext).inflate(getFooterViewResource(), parent, false));
        } else if (viewType == TYPE_AUTO_MORE) {
            return new MoreLoadViewHolder(LayoutInflater.from(mContext).inflate(getMoreLoadLayoutResId(), parent, false));
        } else {
            return getRecycleViewHolder(LayoutInflater.from(mContext).inflate(getViewResource(), parent, false));
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final int viewType = getItemViewType(position);
        if (viewType == TYPE_AUTO_MORE) {
            bindMoreViewHolder((MoreLoadViewHolder) holder);
            return;
        }
        if (viewType == TYPE_HEADER) {
            dataReadByHeader(holder, position);
            if (mOnHeaderItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnHeaderItemClickListener.onHeaderClick(holder.itemView, position, mList.get(position));
                    }
                });
            }
        } else if (viewType == TYPE_FOOTER) {
            dataReadByFooter(holder, position);
        } else {
            final int pos = getRealPosition(position);
            dataRead(holder, pos);
            if (mOnItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (viewType != TYPE_NORMAL) {
                            return;
                        }
                        mOnItemClickListener.onItemClick(holder.itemView, position, mList.get(pos));
                    }
                });
            }
            if (mOnItemLongClickListener != null) {
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (viewType != TYPE_NORMAL) {
                            return false;
                        } else {
                            mOnItemLongClickListener.onItemLongClick(holder.itemView, position, mList.get(pos));
                            return true;
                        }
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        int count = mList == null ? 0 : mList.size();
        if (count == 0) {
            return count;
        }
        if (getViewHeaderResource() > 0) {
            count += 1;
        }
        if (getFooterViewResource() > 0) {
            count += 1;
        }
        if (getMoreLoadLayoutResId() > 0) {
            count += 1;
        }
        return count;
    }

    protected static class MoreLoadViewHolder extends RecyclerView.ViewHolder {

        private TextView mTvMsg;

        public MoreLoadViewHolder(View itemView) {
            super(itemView);
            mTvMsg = (TextView) itemView.findViewById(R.id.tv_more_loading);
        }
    }

    protected int getMoreLoadLayoutResId() {

        return R.layout.item_new_more_footer_layout;
    }

    private void bindMoreViewHolder(MoreLoadViewHolder holder) {
        if (mMoreType == MORE_TYPE.AUTO_LOAD) {
            holder.mTvMsg.setText(mContext.getString(R.string.list_index_auto_loading));
        } else if (mMoreType == MORE_TYPE.LOAD_END) {
            holder.mTvMsg.setText(getFooterTip());
        } else if (mMoreType == MORE_TYPE.LOAD_ERROR) {
            holder.mTvMsg.setText(mContext.getString(R.string.list_index_loading_error));
            holder.mTvMsg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mMoreListener != null) {
                        setMoreType(MORE_TYPE.AUTO_LOAD);
                        notifyDataSetChanged();
                        mMoreListener.load();
                    }
                }
            });
        }
    }

    protected abstract int getViewResource();

    protected int getViewHeaderResource() {
        return 0;
    }

    protected int getFooterViewResource() {
        return 0;
    }

    protected RecyclerView.ViewHolder getRecycleViewHeaderHolder(View view) {
        throw new NullPointerException("getRecycleViewHeaderHolderv not init");
    }

    protected RecyclerView.ViewHolder getRecycleViewFooterHolder(View view) {
        throw new NullPointerException("getRecycleViewFooterHolder not init");
    }

    protected abstract RecyclerView.ViewHolder getRecycleViewHolder(View view);

    protected abstract void dataRead(RecyclerView.ViewHolder holder, int position);

    protected void dataReadByHeader(RecyclerView.ViewHolder holder, int position) {
        throw new NullPointerException("dataReadByHolder not init");
    }

    protected void dataReadByFooter(RecyclerView.ViewHolder holder, int position) {
        throw new NullPointerException("dataReadByFooter not init");
    }

    /**
     * 由于不同列表底部显示的东西可能不一样 所以由子类传入
     *
     * @return
     */
    protected String getFooterTip() {
        return mContext.getString(R.string.list_index_loading_end);
    }

    public interface OnItemClickListener<T> {

        void onItemClick(View view, int position, T entity);

    }

    public interface OnItemLongClickListener<T> {

        void onItemLongClick(View view, int position, T entity);
    }

    public interface OnHeaderItemClickListener<T> {

        void onHeaderClick(View view, int position, T entity);
    }

    public void setOnItemClickListener(OnItemClickListener<T> listener) {
        mOnItemClickListener = listener;
    }

    public void setOnHeaderItemClickListener(OnHeaderItemClickListener<T> listener) {
        mOnHeaderItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener<T> itemLongClickListener) {
        mOnItemLongClickListener = itemLongClickListener;
    }

    public void setMoreType(MORE_TYPE moreType) {
        mMoreType = moreType;
    }

    public MORE_TYPE getMoreType() {
        return mMoreType;
    }

    public void setMoreListener(AutoMoreListener listener) {
        mMoreListener = listener;
    }

    public interface AutoMoreListener {

        void load();
    }

}
