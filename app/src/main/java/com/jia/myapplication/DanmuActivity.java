package com.jia.myapplication;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;

import com.jia.jsplayer.danmu.DanmuView;
import com.jia.jsplayer.listener.OnVideoControlListener;
import com.jia.jsplayer.utils.DisplayUtils;
import com.jia.jsplayer.video.JsPlayer;

import java.util.Random;

public class DanmuActivity extends AppCompatActivity {

    private JsPlayer jsplayer_danmu;

    private Button bt_add_danmu;

    private Button bt__danmu_setting;

    private SeekBar seek_light;

    private SeekBar seek_speed;

    private String path = "http://baobab.wdjcdn.com/1455782903700jy.mp4";

    public String DANMU[] = {"腌疙瘩，炸麻叶", "一种鸡蛋蒸虾酱", "鲜味妙不可言", "撒了芝麻的吊炉烧饼，焦香四溢", "西红柿鸡蛋面", "那浓郁深沉的酱油味仍然让我无比想念", "即使是二姨炒的土豆片", "蒸馍馍"};

    public int COLOR[] = {Color.BLUE, Color.WHITE, Color.YELLOW, Color.RED};

    Random random;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                handler.sendEmptyMessageDelayed(1, 250);
//                MyDanmuModel danmuEntity = new MyDanmuModel();
//                danmuEntity.setType(0);
//                danmuEntity.setContent(DANMU[random.nextInt(8)]);
//                danmuEntity.setTextColor(COLOR[random.nextInt(4)]);
//                jsplayer_danmu.addDanmu(danmuEntity);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_danmu);
        random = new Random();

        jsplayer_danmu = (JsPlayer) findViewById(R.id.jsplayer_danmu);
        bt_add_danmu = (Button) findViewById(R.id.bt_add_danmu);
        bt__danmu_setting = (Button) findViewById(R.id.bt__danmu_setting);
        seek_light = (SeekBar) findViewById(R.id.seek_light);
        seek_light.setMax(255);
        seek_speed = (SeekBar) findViewById(R.id.seek_speed);
        seek_speed.setMax(8);

        seek_speed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                jsplayer_danmu.setDanMuSpeed(seekBar.getProgress());
            }
        });

        seek_light.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                jsplayer_danmu.getDanmu().setAlpha(255 - seekBar.getProgress());
            }
        });

        jsplayer_danmu.setOnVideoControlListener(new OnVideoControlListener() {
            @Override
            public void onStartPlay() {
                jsplayer_danmu.startPlay();
//                handler.sendEmptyMessageDelayed(1,800);
            }

            @Override
            public void onBack() {

            }

            @Override
            public void onFullScreen() {
                DisplayUtils.toggleScreenOrientation(DanmuActivity.this);
            }

            @Override
            public void onRetry(int errorStatus) {

            }
        });

        jsplayer_danmu.setPath(new VideoInfo("极品艺术", path));

        jsplayer_danmu.setDanMuAdapter(new MyDanmuAdapter(this));
        jsplayer_danmu.setDanMuGravity(3);
        jsplayer_danmu.setDanMuSpeed(DanmuView.NORMAL_SPEED);

        bt_add_danmu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDanmuModel danmuEntity = new MyDanmuModel();
                danmuEntity.setContent(DANMU[random.nextInt(8)]);
                danmuEntity.setType(random.nextInt(4));
                danmuEntity.setGoodNum(random.nextInt(100) + 1);
                danmuEntity.setGood(false);
                jsplayer_danmu.addDanmu(danmuEntity);
            }
        });


        bt__danmu_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DanmuActivity.this, DanMuViewActivity.class));
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    @Override
    public void onBackPressed() {
        if (!DisplayUtils.isPortrait(this)) {
            if (!jsplayer_danmu.isLock()) {
                DisplayUtils.toggleScreenOrientation(this);
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        jsplayer_danmu.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        jsplayer_danmu.onDestroy();
    }
}
