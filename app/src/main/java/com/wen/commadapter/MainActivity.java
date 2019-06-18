package com.wen.commadapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ListView;

import com.wen.commadapter.adapter.QuickAdapter;
import com.wen.commadapter.adapter.QuickMultiSupport;
import com.wen.commadapter.adapter.QuickViewHolder;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<IViewType> mData = new ArrayList<>();
    private QuickMultiSupport<IViewType> mQuickSupport;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initViews();
    }

    /**
     * 多条目的ViewType
     */

    public interface IViewType {
        int getItemViewType();
    }

    private void initData() {
        for (int i = 0; i < 199; i++) {
            if (i % 10 == 0) {
                MultiBean bean = new MultiBean();
                bean.name = "mData--------" + i;
                mData.add(bean);
            } else {
                MultiBean1 bean = new MultiBean1();
                bean.name = "mData--------" + i;
                mData.add(bean);
            }
        }
        // 多条目支持
        mQuickSupport = new QuickMultiSupport<IViewType>() {
            // 条目总共两种类型
            @Override
            public int getViewTypeCount() {
                return 2;
            }

            // 根据不用的JavaBean返回不同的布局
            @Override
            public int getLayoutId(IViewType data) {
                if (data instanceof MultiBean) {
                    return R.layout.item_list1;
                }
                return R.layout.item_list;
            }

            @Override
            public int getItemViewType(IViewType data) {
                return data.getItemViewType();
            }

            @Override
            public boolean isSpan(IViewType data) {
                // 是否占用一个条目，针对RecyclerView
                if (data instanceof MultiBean) {
                    return true;
                }
                return false;
            }
        };
    }


    private void initViews() {
        ListView listView = findViewById(R.id.list_view);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        // ListView设置Adapter
        listView.setAdapter(new CommAdapter(this, mData, mQuickSupport));
        // RecyclerView设置Adapter
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(new CommAdapter(this, mData, mQuickSupport));
    }

    class CommAdapter extends QuickAdapter<IViewType> {
        public CommAdapter(Context context, List<IViewType> data, int layoutId) {
            super(context, data, layoutId);
        }

        public CommAdapter(Context context, List<IViewType> data, QuickMultiSupport<IViewType> support) {
            super(context, data, support);
        }

        @Override
        protected void convert(QuickViewHolder holder, IViewType item, final int position) {
            holder.setText(R.id.tv, item.toString());
            holder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 点击移除当前条目
                    remove(position);
                }
            });
        }
    }
}
