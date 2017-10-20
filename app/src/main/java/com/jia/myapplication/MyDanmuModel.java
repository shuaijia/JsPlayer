package com.jia.myapplication;

import com.jia.jsplayer.danmu.DanmuModel;

/**
 * Description:弹幕实体类
 * Created by jia on 2017/9/25.
 * 人之所以能，是相信能
 */
public class MyDanmuModel extends DanmuModel {

    public String content;
    public int goodNum;
    public boolean isGood;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getGoodNum() {
        return goodNum;
    }

    public void setGoodNum(int goodNum) {
        this.goodNum = goodNum;
    }

    public boolean isGood() {
        return isGood;
    }

    public void setGood(boolean good) {
        isGood = good;
    }
}
