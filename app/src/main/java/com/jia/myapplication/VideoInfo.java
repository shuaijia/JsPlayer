package com.jia.myapplication;

import com.jia.jsplayer.bean.IVideoInfo;

/**
 * Describtion：
 * Created by jia on 2017/9/2.
 * 滴水穿石，非一日之功
 */

public class VideoInfo implements IVideoInfo {

    private String title;

    private String path;

    public VideoInfo(String title, String path) {
        this.title = title;
        this.path = path;
    }

    @Override
    public String getVideoTitle() {
        return title;
    }

    @Override
    public String getVideoPath() {
        return path;
    }
}
