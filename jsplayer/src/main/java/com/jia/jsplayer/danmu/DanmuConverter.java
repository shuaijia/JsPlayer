package com.jia.jsplayer.danmu;

import android.view.View;

/**
 * Description: 弹幕转换器
 * Created by jia on 2017/9/25.
 * 人之所以能，是相信能
 */
public abstract class DanmuConverter<M> {

    abstract View converter(M model);

    abstract int getSingleLineHeight();

}
