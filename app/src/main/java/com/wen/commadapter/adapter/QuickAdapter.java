package com.wen.commadapter.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wen.commadapter.R;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView、ListView、GridView通用的适配器
 */

public abstract class QuickAdapter<T> extends BaseAdapter {
    private Context mContext;
    private List<T> mData;
    private int mLayoutId;
    private QuickMultiSupport<T> mSupport;
    private boolean isRecycler;
    private int mPosition;

    public QuickAdapter(Context context, List<T> data, int layoutId) {
        this.mContext = context;
        this.mData = data == null ? new ArrayList<T>() : new ArrayList<T>(data);
        this.mLayoutId = layoutId;
    }

    public QuickAdapter(Context context, List<T> data, QuickMultiSupport<T> support) {
        this(context, data, 0);
        this.mSupport = support;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public T getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        QuickViewHolder holder;
        if (convertView == null) {
            int layoutId = mLayoutId;
            // 多条目的
            if (mSupport != null) {
                layoutId = mSupport.getLayoutId(mData.get(position));
            }
            // 创建ViewHolder
            holder = createListHolder(parent, layoutId);
        } else {
            holder = (QuickViewHolder) convertView.getTag();
            // 防止失误，还要判断
            if (mSupport != null) {
                int layoutId = mSupport.getLayoutId(mData.get(position));
                // 如果布局ID不一样，又重新创建
                if (layoutId != holder.getLayoutId()) {
                    // 创建ViewHolder
                    holder = createListHolder(parent, layoutId);
                }
            }

        }
        // 绑定View的数据
        convert(holder, mData.get(position), position);
        return holder.itemView;
    }

    /**
     * 创建ListView的Holer
     */
    @NonNull
    private QuickViewHolder createListHolder(ViewGroup parent, int layoutId) {
        QuickViewHolder holder;
        View itemView = LayoutInflater.from(mContext).inflate(layoutId, parent, false);
        holder = new QuickViewHolder(itemView, layoutId);
        itemView.setTag(holder);
        return holder;
    }

    /**
     * ViewType的数量
     */
    @Override
    public int getViewTypeCount() {
        // 多条目的
        if (mSupport != null) {
            return mSupport.getViewTypeCount() + super.getViewTypeCount();
        }
        return super.getViewTypeCount();
    }

    /**
     * 这个方法是共用的
     */
    @Override
    public int getItemViewType(int position) {
        mPosition = position;
        // 多条目的
        if (mSupport != null) {
            return mSupport.getItemViewType(mData.get(position));
        }
        return super.getItemViewType(position);
    }


    // RecyclerView=================================================================================
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        isRecycler = true;
        // 如果是多条目，viewType就是布局ID
        View view;
        if (mSupport != null) {
            Object tagPosition = parent.getTag(R.id.view_position);
            int layoutId = mSupport.getLayoutId(mData.get(mPosition));
            // 如果是滚动布局
            if (tagPosition != null) {
                int position = (int) tagPosition;
                layoutId = mSupport.getLayoutId(mData.get(position));
            }
            view = LayoutInflater.from(mContext).inflate(layoutId, parent, false);
        } else {
            view = LayoutInflater.from(mContext).inflate(mLayoutId, parent, false);
        }

        QuickViewHolder holder = new QuickViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof QuickViewHolder) {
            convert((QuickViewHolder) holder, mData.get(position), position);
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        if (mSupport == null || recyclerView == null) {
            return;
        }
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            final GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();
            // 如果设置合并单元格就占用SpanCount那个多个位置
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (mSupport.isSpan(mData.get(position))) {
                        return gridLayoutManager.getSpanCount();
                    } else if (spanSizeLookup != null) {
                        return spanSizeLookup.getSpanSize(position);
                    }
                    return 1;
                }
            });
            gridLayoutManager.setSpanCount(gridLayoutManager.getSpanCount());
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        if (mSupport == null) {
            return;
        }
        int position = holder.getLayoutPosition();
        // 如果设置合并单元格
        if (mSupport.isSpan(mData.get(position))) {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
        }
    }

    // RecyclerView=================================================================================


    /**
     * 绑定View的数据
     */
    protected abstract void convert(QuickViewHolder holder, T item, int position);


    //==========================================数据相关================================================
    public void add(T elem) {
        mData.add(elem);
        notifyData();

    }


    public void addAll(List<T> data) {
        mData.addAll(data);
        notifyData();
    }

    public void addFirst(T elem) {
        mData.add(0, elem);
        notifyData();
    }

    public void set(T oldElem, T newElem) {
        set(mData.indexOf(oldElem), newElem);
        notifyData();
    }

    public void set(int index, T elem) {
        mData.set(index, elem);
        notify();
    }

    public void remove(T elem) {
        mData.remove(elem);
        notifyData();
    }

    public void remove(int index) {
        mData.remove(index);
        notifyData();
    }

    public void replaceAll(List<T> elem) {
        mData.clear();
        mData.addAll(elem);
        notifyData();
    }

    /**
     * 清除
     */
    public void clear() {
        mData.clear();
        notifyData();
    }

    private void notifyData() {
        if (isRecycler) {
            notifyDataSetChanged();
        } else {
            notifyListDataSetChanged();
        }
    }

    public boolean contains(T elem) {
        return mData.contains(elem);
    }


    public List<T> getData() {
        return mData;
    }
}
