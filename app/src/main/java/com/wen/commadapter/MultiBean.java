package com.wen.commadapter;

/**
 * Created by Administrator on 2018/3/20.
 */

public class MultiBean implements MainActivity.IViewType {
    public String name;

    @Override
    public int getItemViewType() {
        return 1;
    }


    @Override
    public String toString() {
        return name;
    }
}
