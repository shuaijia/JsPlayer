package com.jia.jsplayer.view;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jia.jsplayer.R;


/**
 * 滑动变化音量|亮度框
 */
public class JsVideoSystemOverlay extends FrameLayout {

    public enum SystemType {
        VOLUME, BRIGHTNESS
    }

    private TextView mSystemTitle;
    private ImageView mSystemImage;
    private ProgressBar mProgressBar;

    public JsVideoSystemOverlay(Context context) {
        super(context);
        initialize(context);
    }

    public JsVideoSystemOverlay(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public JsVideoSystemOverlay(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }

    private void initialize(Context context) {
        LayoutInflater.from(context).inflate(R.layout.video_overlay_system, this);

        mSystemTitle = (TextView) findViewById(R.id.system_ui_title);
        mSystemImage = (ImageView) findViewById(R.id.system_ui_image);
        mProgressBar = (ProgressBar) findViewById(R.id.system_ui_seek_bar);

        hide();
    }

    /**
     * 显示 提示
     *
     * @param type     提示类型  音量/ 亮度
     * @param max      最大值
     * @param progress 调整比例
     */
    public void show(SystemType type, int max, int progress) {
        if (type == SystemType.BRIGHTNESS) {
            mSystemTitle.setText("亮度");
            mSystemImage.setImageResource(R.mipmap.system_ui_brightness);
        } else if (type == SystemType.VOLUME) {
            mSystemTitle.setText("音量");
            mSystemImage.setImageResource(progress == 0
                    ? R.mipmap.system_ui_no_volume
                    : R.mipmap.system_ui_volume);
        }
        mProgressBar.setMax(max);
        mProgressBar.setProgress(progress);
        setVisibility(VISIBLE);
    }

    public void hide() {
        setVisibility(GONE);
    }

}
