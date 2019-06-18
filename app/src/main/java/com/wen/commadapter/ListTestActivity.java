package com.wen.commadapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ListTestActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    private class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.Holder> {

        private LayoutInflater mInflater;
        private Context mContext;
        private List<String> mData;
        private int mLayoutId;

        public RecyclerAdapter(Context context, List<String> data, int layoutId) {
            mInflater = LayoutInflater.from(context);
            this.mContext = context;
            this.mData = data;
            this.mLayoutId = layoutId;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(mLayoutId, parent, false);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            holder.tv.setText(mData.get(position));
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        class Holder extends RecyclerView.ViewHolder {
            public TextView tv;

            public Holder(View itemView) {
                super(itemView);
                tv = itemView.findViewById(R.id.tv);
            }
        }
    }
}
