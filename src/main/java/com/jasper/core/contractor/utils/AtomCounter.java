package com.jasper.core.contractor.utils;

public class AtomCounter {

    private OnUpdateListener onUpdateListener;
    private int value=0;

    public synchronized int incrementAndGet(){
        value++;
        onUpdateListener.onUpdate(value);
        return value;
    }

    public void setOnUpdateListener(OnUpdateListener onUpdateListener) {
        this.onUpdateListener = onUpdateListener;
    }

    public interface OnUpdateListener {
        void onUpdate(int value);
    }

}
