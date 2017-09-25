package com.jia.myapplication;

import com.jia.jsplayer.danmu.DanmuModel;

/**
 * Description:弹幕实体类
 * Created by jia on 2017/9/25.
 * 人之所以能，是相信能
 */
public class MyDanmuModel extends DanmuModel {

    public String content;
    public int textColor;
    public String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }
}
