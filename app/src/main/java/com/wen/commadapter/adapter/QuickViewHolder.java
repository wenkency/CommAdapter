package com.wen.commadapter.adapter;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.WeakReference;


/**
 * RecyclerView的通用适配器---》》QuickViewHolder
 */

public class QuickViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<WeakReference<View>> mViews;
    private int mLayoutId;

    public QuickViewHolder(View itemView) {
        this(itemView, -1);
    }

    public QuickViewHolder(View itemView, int layoutId) {
        super(itemView);
        mViews = new SparseArray<>();
        this.mLayoutId = layoutId;

    }

    public int getLayoutId() {
        return mLayoutId;
    }

    /**
     * 设置条目的点击事件
     */
    public QuickViewHolder setOnClickListener(View.OnClickListener listener) {
        itemView.setOnClickListener(listener);
        return this;
    }

    /**
     * 设置条目的长按事件
     */
    public QuickViewHolder setOnLongClickListener(View.OnLongClickListener listener) {
        itemView.setOnLongClickListener(listener);
        return this;
    }

    /**
     * 设置View的点击事件
     *
     * @return
     */
    public QuickViewHolder setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        if (view != null) {
            view.setOnClickListener(listener);
        }
        return this;
    }

    /**
     * 获取条目的View
     */
    public View getView() {
        return itemView;
    }

    /**
     * 根据ID获取条目里面的View
     */
    public <T extends View> T getView(int viewId) {
        WeakReference<View> viewWeakReference = mViews.get(viewId);
        View view = null;
        if (viewWeakReference == null) {
            view = itemView.findViewById(viewId);
            if (view != null) {
                mViews.put(viewId, new WeakReference<>(view));
            }
        } else {
            view = viewWeakReference.get();
        }
        return (T) view;
    }

    public QuickViewHolder setText(int viewId, CharSequence text) {
        TextView tv = getView(viewId);
        if (tv != null && !TextUtils.isEmpty(text)) {
            tv.setText(text);
        }
        return this;
    }

    /**
     * 设置图片背景颜色
     */
    public QuickViewHolder setTextColor(int viewId, int color) {
        TextView view = getView(viewId);
        if (view != null) {
            view.setTextColor(color);
        }
        return this;
    }

    /**
     * 设置控件是否可见
     */
    public QuickViewHolder setVisible(int viewId, int visible) {
        View view = getView(viewId);
        view.setVisibility(visible);
        return this;
    }

    /**
     * 设置控件选中
     */
    public QuickViewHolder setChecked(int viewId, boolean checked) {
        Checkable view = getView(viewId);
        view.setChecked(checked);
        return this;
    }

    /**
     * 设置控件背景
     */
    public QuickViewHolder setBackgroundRes(int viewId, int backgroundRes) {
        View view = getView(viewId);
        view.setBackgroundResource(backgroundRes);
        return this;
    }

    /**
     * 设置图片
     */
    public QuickViewHolder setImageResource(int viewId, int imageResId) {
        ImageView imageView = getView(viewId);
        imageView.setImageResource(imageResId);
        return this;
    }

    /**
     * 设置图片
     */
    public QuickViewHolder setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView imageView = getView(viewId);
        imageView.setImageBitmap(bitmap);
        return this;
    }


}
