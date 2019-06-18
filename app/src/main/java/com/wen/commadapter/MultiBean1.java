package com.wen.commadapter;

/**
 * Created by Administrator on 2018/3/20.
 */

public class MultiBean1 implements MainActivity.IViewType {
    public String name;

    @Override
    public int getItemViewType() {
        return 2;
    }


    @Override
    public String toString() {
        return name;
    }
}
