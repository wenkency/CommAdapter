package com.wen.commadapter.adapter;

import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.SpinnerAdapter;

/**
 * RecyclerView、ListView、GridView适配器的基类
 */

public class BaseAdapter extends RecyclerView.Adapter implements ListAdapter, SpinnerAdapter {
    private final DataSetObservable mDataSetObservable = new DataSetObservable();

    // RecyclerView========================================================================
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    // RecyclerView
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    // RecyclerView
    @Override
    public int getItemCount() {
        return 0;
    }
    // RecyclerView========================================================================

    // ListView========================================================================
    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.registerObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.unregisterObserver(observer);
    }

    public void notifyListDataSetChanged() {
        mDataSetObservable.notifyChanged();
    }


    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return getCount() == 0;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}
