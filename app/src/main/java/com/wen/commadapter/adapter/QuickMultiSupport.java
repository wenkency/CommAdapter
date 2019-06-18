package com.wen.commadapter.adapter;

/**
 * ListView GridView RecyclerView多条目适配
 */

public interface QuickMultiSupport<T> {
    /**
     * 获取View类型的数量
     */
    int getViewTypeCount();

    /**
     * 根据数据，获取多条目布局ID
     */
    int getLayoutId(T data);

    /**
     * 根据数据，获取ItemViewType
     */
    int getItemViewType(T data);


    /**
     * 是否合并条目-->>使用RecyclerView时，无效请用原生的RecyclerView
     */
    boolean isSpan(T data);
}
