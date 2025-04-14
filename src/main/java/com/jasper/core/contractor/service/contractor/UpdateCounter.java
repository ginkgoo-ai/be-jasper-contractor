package com.jasper.core.contractor.service.contractor;

import com.jasper.core.contractor.utils.AtomCounter;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
@Slf4j
public class UpdateCounter extends AtomCounter implements AtomCounter.OnUpdateListener {
    private int currentProgress=0;
    private final long total;
    private final long start;
    public UpdateCounter(long total) {
        setOnUpdateListener(this);
        this.total=total;
        this.start=System.currentTimeMillis();
    }

    @Override
    public void onUpdate(int value) {
        int progress= BigDecimal.valueOf(value).divide(BigDecimal.valueOf(total),2,BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100)).intValue();
        if(progress!=currentProgress){
            currentProgress=progress;
            long cost=System.currentTimeMillis()-start;
            double p=(double)cost/value;
            long remaining= (long)((total-value)*p);
            log.info("receive contractor progress:{}% ,{}/{},cost {} , remaining {}",progress,value,total,formatTime(cost),formatTime(remaining));
        }
    }


    private String formatTime(long mill){
        long minutes= (mill/1000/60);
        long seconds=mill/1000%60;
        return formatNum(minutes)+":"+formatNum(seconds);
    }
    private String formatNum(long val){
        if(val<10){
            return "0"+val;
        }else{
            return String.valueOf(val);
        }
    }

}
