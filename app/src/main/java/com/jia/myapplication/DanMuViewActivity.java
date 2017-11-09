package com.jia.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jia.jsplayer.danmu.DanmuModel;
import com.jia.jsplayer.danmu.DanmuView;

import java.util.Random;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class DanMuViewActivity extends Activity implements View.OnClickListener {

    // 弹幕
    private DanmuView danmu;
    // 弹幕开关
    private ImageView iv_danmu_switch;
    // 设置区域
    private LinearLayout ll_danmu_setting;
    // 透明度
    private SeekBar seek_light;
    // 速度
    private SeekBar seek_speed;
    // 大小
    private SeekBar seek_size;
    // 上中
    private RadioButton tv_gravity110;
    // 下中
    private RadioButton tv_gravity011;
    // 上中下
    private RadioButton tv_gravity111;
    // 输入框
    private EditText et;
    // 发送
    private Button bt_send;

    // 适配器
    private MyDanmuAdapter adapter = new MyDanmuAdapter(this);

    public String DANMU[] = {"腌疙瘩，炸麻叶", "一种鸡蛋蒸虾酱", "鲜味妙不可言", "撒了芝麻的吊炉烧饼，焦香四溢", "西红柿鸡蛋面", "那浓郁深沉的酱油味仍然让我无比想念", "即使是二姨炒的土豆片", "蒸馍馍"};

    public int COLOR[] = {Color.BLUE, Color.WHITE, Color.YELLOW, Color.RED};

    private Random random = new Random();

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 200) {
                MyDanmuModel danmuEntity = new MyDanmuModel();
                danmuEntity.setContent(DANMU[random.nextInt(8)]);
                danmuEntity.setType(random.nextInt(4));
                danmuEntity.setGoodNum(random.nextInt(100) + 1);
                danmuEntity.setGood(false);
                danmu.addDanmu(danmuEntity);
                handler.sendEmptyMessageDelayed(200, 1000);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏

        setContentView(R.layout.activity_dan_mu_view);

        initView();

        danmu.setAdapter(adapter);
        danmu.setGravity(1);
        danmu.setSpeed(DanmuView.NORMAL_SPEED);

        handler.sendEmptyMessageDelayed(200, 1000);
    }

    private void initView() {
        danmu = findViewById(R.id.danmu);
        // 弹幕开关
        iv_danmu_switch = findViewById(R.id.iv_danmu_switch);
        iv_danmu_switch.setOnClickListener(this);
        // 设置区域
        ll_danmu_setting = findViewById(R.id.ll_danmu_setting);
        // 透明度
        seek_light = findViewById(R.id.seek_light);
        seek_light.setMax(255);
        seek_light.setProgress(0);
        seek_light.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                adapter.setAlpha((255 - i) / 255.0f);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        // 速度
        seek_speed = findViewById(R.id.seek_speed);
        seek_speed.setMax(80);
        seek_speed.setProgress(danmu.getGravity() * 10);
        seek_speed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (i / 10 == 0) {
                    danmu.setSpeed(10);
                } else {

                    danmu.setSpeed(i / 10);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        // 大小
        seek_size = findViewById(R.id.seek_size);
        seek_size.setMax(30);
        seek_size.setProgress(15);
        seek_size.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                adapter.setTextSize(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        // 上中
        tv_gravity110 = findViewById(R.id.tv_gravity110);
        tv_gravity110.setOnClickListener(this);
        // 下中
        tv_gravity011 = findViewById(R.id.tv_gravity011);
        tv_gravity011.setOnClickListener(this);
        // 上中下
        tv_gravity111 = findViewById(R.id.tv_gravity111);
        tv_gravity111.setOnClickListener(this);
        // 输入框
        et = findViewById(R.id.et);
        // 发送
        bt_send = findViewById(R.id.bt_send);
        bt_send.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_danmu_switch:

                if (ll_danmu_setting.getVisibility() == View.VISIBLE) {
                    ll_danmu_setting.setVisibility(View.GONE);
                } else {
                    ll_danmu_setting.setVisibility(View.VISIBLE);
                }

                break;
            case R.id.bt_send:

                if (TextUtils.isEmpty(et.getText().toString())) {
                    Toast.makeText(DanMuViewActivity.this, "请输入内容", Toast.LENGTH_SHORT).show();
                } else {
                    MyDanmuModel model = new MyDanmuModel();
                    model.setContent(et.getText().toString());
                    model.setType(random.nextInt(4));
                    model.setGoodNum(0);
                    model.setGood(false);
                    danmu.addDanmu(model);

                    et.setText("");
                }


                break;
            case R.id.tv_gravity110:

                danmu.setGravity(3);


                break;
            case R.id.tv_gravity011:

                danmu.setGravity(6);

                break;
            case R.id.tv_gravity111:

                danmu.setGravity(7);

                break;
        }
    }

}
