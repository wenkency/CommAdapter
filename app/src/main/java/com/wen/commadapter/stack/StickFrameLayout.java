package com.wen.commadapter.stack;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.wen.commadapter.R;

import java.lang.reflect.Method;


/**
 * 悬浮布局封装
 */
public class StickFrameLayout extends FrameLayout {
    private RecyclerView mRecyclerView;
    // 悬浮根布局
    private FrameLayout mStickyLayout;
    // 要悬浮的布局
    private View mStickView;
    // 偏移量
    private int mOffset = 0;

    public StickFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StickFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 1. 加载布局完成之后
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        // 添加监听
        addOnScrollListener();
        // 添加悬浮根布局
        addStickyLayout();
    }


    /**
     * 添加滚动监听
     */
    private void addOnScrollListener() {
        mRecyclerView = (RecyclerView) getChildAt(0);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                StickFrameLayout.this.onScrolled();
            }
        });
    }

    /**
     * 滚动监听事件处理
     */
    private void onScrolled() {
        RecyclerView.Adapter adapter = mRecyclerView.getAdapter();
        RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
        if (adapter == null || layoutManager == null || adapter.getItemCount() <= 0) {
            return;
        }
        if (adapter instanceof IStick) {
            IStick stick = (IStick) adapter;
            int stickPosition = stick.getStickPosition();
            if (mStickView == null) {
                // 根据类型创建ViewHolder
                mStickyLayout.setTag(R.id.view_position, stickPosition);
                RecyclerView.ViewHolder viewHolder = adapter.onCreateViewHolder(mStickyLayout, stick.getStickViewType());
                // 根据位置绑定View
                adapter.onBindViewHolder(viewHolder, stickPosition);
                mStickView = viewHolder.itemView;
                mStickyLayout.addView(mStickView);
            }
            //这是是处理第一次打开时，吸顶布局已经添加到StickyLayout，但StickyLayout的高依然为0的情况。
            if (mStickyLayout.getChildCount() > 0 && mStickyLayout.getHeight() == 0) {
                mStickyLayout.requestLayout();
            }
            //设置StickyLayout显示或者隐藏。
            int firstVisibleItemPosition = findFirstVisibleItemPosition(mRecyclerView);
            View topView = layoutManager.findViewByPosition(stickPosition);

            // 1. 判断要不要偏移
            changeOffset(mOffset);
            // 1. 大于悬浮的位置都显示
            if (firstVisibleItemPosition >= stickPosition) {
                mStickyLayout.setVisibility(View.VISIBLE);
            } else if (topView != null) {
                // 2. 偏移大于悬浮到顶部的距离就显示
                boolean isShow = mOffset >= topView.getTop();
                if (isShow) {
                    mStickyLayout.setVisibility(View.VISIBLE);
                } else {
                    mStickyLayout.setVisibility(View.GONE);
                }
            } else {
                mStickyLayout.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 手动设置显示
     *
     * @param visible
     */
    public void setStickyVisibility(int visible) {
        if (mStickyLayout != null) {
            mStickyLayout.setVisibility(visible);
        }
    }

    /**
     * 找第一个可见条目的位置
     */
    private int findFirstVisibleItemPosition(RecyclerView recyclerView) {
        int firstVisibleItem = -1;
        RecyclerView.LayoutManager layout = recyclerView.getLayoutManager();
        if (layout != null) {
            if (layout instanceof GridLayoutManager) {
                firstVisibleItem = ((GridLayoutManager) layout).findFirstVisibleItemPosition();
            } else if (layout instanceof LinearLayoutManager) {
                firstVisibleItem = ((LinearLayoutManager) layout).findFirstVisibleItemPosition();
            } else if (layout instanceof StaggeredGridLayoutManager) {
                int[] firstPositions = new int[((StaggeredGridLayoutManager) layout).getSpanCount()];
                ((StaggeredGridLayoutManager) layout).findFirstVisibleItemPositions(firstPositions);
                firstVisibleItem = getMin(firstPositions);
            }
        }
        return firstVisibleItem;
    }

    private int getMin(int[] arr) {
        int min = arr[0];
        for (int x = 1; x < arr.length; x++) {
            if (arr[x] < min)
                min = arr[x];
        }
        return min;
    }

    /**
     * 添加悬浮根布局
     */
    private void addStickyLayout() {
        mStickyLayout = new FrameLayout(getContext());
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        mStickyLayout.setLayoutParams(lp);
        super.addView(mStickyLayout, lp);
    }

    /**
     * 设置偏移量
     */
    public void setStickOffset(int offset) {
        changeOffset(offset);
    }

    /**
     * 改变偏移量
     */
    private void changeOffset(int offset) {
        if (mOffset != offset) {
            if (mStickyLayout != null) {
                mOffset = offset;
                LayoutParams lp = (LayoutParams) mStickyLayout.getLayoutParams();
                lp.topMargin = offset;
                mStickyLayout.setLayoutParams(lp);
            }
        }
    }


    @Override
    protected int computeVerticalScrollOffset() {
        if (mRecyclerView != null) {
            try {
                Method method = View.class.getDeclaredMethod("computeVerticalScrollOffset");
                method.setAccessible(true);
                return (int) method.invoke(mRecyclerView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return super.computeVerticalScrollOffset();
    }


    @Override
    protected int computeVerticalScrollRange() {
        if (mRecyclerView != null) {
            try {
                Method method = View.class.getDeclaredMethod("computeVerticalScrollRange");
                method.setAccessible(true);
                return (int) method.invoke(mRecyclerView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return super.computeVerticalScrollRange();
    }

    @Override
    protected int computeVerticalScrollExtent() {
        if (mRecyclerView != null) {
            try {
                Method method = View.class.getDeclaredMethod("computeVerticalScrollExtent");
                method.setAccessible(true);
                return (int) method.invoke(mRecyclerView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return super.computeVerticalScrollExtent();
    }

    @Override
    public void scrollBy(int x, int y) {
        if (mRecyclerView != null) {
            mRecyclerView.scrollBy(x, y);
        } else {
            super.scrollBy(x, y);
        }
    }

    @Override
    public void scrollTo(int x, int y) {
        if (mRecyclerView != null) {
            mRecyclerView.scrollTo(x, y);
        } else {
            super.scrollTo(x, y);
        }
    }
}
