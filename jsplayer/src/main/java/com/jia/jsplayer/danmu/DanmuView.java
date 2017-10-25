package com.jia.jsplayer.danmu;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.jia.jsplayer.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 弹幕自定义view
 * Created by jia on 2017/9/25.
 * 人之所以能，是相信能
 */
public class DanmuView extends ViewGroup {

    // 移动速度
    public static final int LOWER_SPEED = 1;
    public static final int NORMAL_SPEED = 4;
    public static final int HIGH_SPEED = 8;

    // 出现位置
    public final static int GRAVITY_TOP = 1;    //001
    public final static int GRAVITY_CENTER = 2;  //010
    public final static int GRAVITY_BOTTOM = 4;  //100
    public final static int GRAVITY_FULL = 7;   //111

    private int gravity = GRAVITY_FULL;

    private int speed = 4;

    private int spanCount = 6;

    private int WIDTH, HEIGHT;

    private int singltLineHeight;

    private DanmuAdapter adapter;

    public List<View> spanList;

    private OnItemClickListener onItemClickListener;


    public DanmuView(Context context) {
        this(context, null, 0);
    }

    public DanmuView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DanmuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        spanList = new ArrayList<>();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        WIDTH = MeasureSpec.getSize(widthMeasureSpec);
        HEIGHT = MeasureSpec.getSize(heightMeasureSpec);

        spanCount = HEIGHT / singltLineHeight;
        // 创建同样大小的view集合
        for (int i = 0; i < spanCount; i++) {
            if (spanList.size() < spanCount) {
                spanList.add(i, null);
            }
        }
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {

    }

    /**
     * 添加弹幕view
     *
     * @param model
     */
    public void addDanmu(final DanmuModel model) {
        if (adapter == null) {
            throw new Error("DanmuAdapter(an interface need to be implemented) can't be null,you should call setAdapter firstly");
        }

        View dmView = null;
//        if (adapter.getCacheSize() >= 1) {
//            dmView = adapter.getView(model, adapter.removeViewFromCache(model.getType()));
//            if (dmView == null)
//                addTypeView(model, dmView, false);
//            else
////                addTypeView(model, dmView, true);
//                addTypeView(model, dmView, false);
//        } else {
            dmView = adapter.getView(model, null);
            addTypeView(model, dmView, false);
//        }

        //添加监听
        dmView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null)
                    onItemClickListener.onItemClick(model);
            }
        });
    }

    /**
     * 添加view
     */
    public void addTypeView(DanmuModel model, View child, boolean isReused) {
        super.addView(child);
        child.measure(0, 0);
        //把宽高拿到，宽高都是包含ItemDecorate的尺寸
        int width = child.getMeasuredWidth();
        int height = child.getMeasuredHeight();

        //获取最佳行数
        int bestLine = getBestLine();
        // 设置子view位置
        child.layout(WIDTH, singltLineHeight * bestLine, WIDTH + width, singltLineHeight * bestLine + height);

        InnerEntity innerEntity = null;
        innerEntity = (InnerEntity) child.getTag(R.id.tag_inner_entity);
        if (!isReused || innerEntity == null) {
            innerEntity = new InnerEntity();
        }
        innerEntity.model = model;
        innerEntity.bestLine = bestLine;
        child.setTag(R.id.tag_inner_entity, innerEntity);

        spanList.set(bestLine, child);
    }

    /**
     * 计算最佳位置
     *
     * @return
     */
    private int getBestLine() {
        // 转换为2进制
        int gewei = gravity % 2;
        int temp = gravity / 2;
        int shiwei = temp % 2;
        temp = temp / 2;
        int baiwei = temp % 2;

        // 将所有的行分为三份,前两份行数相同,将第一份的行数四舍五入
        int firstLine = (int) (spanCount / 3.0 + 0.5);

        List<Integer> legalLines = new ArrayList<>();
        if (gewei == 1) {
            for (int i = 0; i < firstLine; i++) {
                legalLines.add(i);
            }
        }
        if (shiwei == 1) {
            for (int i = firstLine; i < firstLine * 2; i++) {
                legalLines.add(i);
            }
        }
        if (baiwei == 1) {
            for (int i = firstLine * 2; i < spanCount; i++) {
                legalLines.add(i);
            }
        }

        int bestLine = 0;
        // 如果有空行,将空行返回
        for (int i = 0; i < spanCount; i++) {
            if (spanList.get(i) == null) {
                bestLine = i;
                if (legalLines.contains(bestLine))
                    return bestLine;
            }
        }

        float minSpace = Integer.MAX_VALUE;
        // 没有空行，就找最大空间的
        for (int i = spanCount - 1; i >= 0; i--) {
            if (legalLines.contains(i)) {
                if (spanList.get(i).getX() + spanList.get(i).getWidth() <= minSpace) {
                    minSpace = spanList.get(i).getX() + spanList.get(i).getWidth();
                    bestLine = i;
                }
            }
        }

        return bestLine;
    }

    private class MyRunnable implements Runnable {
        @Override
        public void run() {
            int count = 0;
            Message msg = null;
            while(true){
                if(count < 7500){
                    count ++;
                }
                else{
                    count = 0;
//                    if(DanmuView.this.getChildCount() < adapter.getCacheSize() / 2){
//                        adapter.shrinkCacheSize();
//                        System.gc();
//                    }
                }
                if(DanmuView.this.getChildCount() >= 0){
                    msg = new Message();
                    msg.what = 1; //移动view
                    handler.sendMessage(msg);
                }

                try {
                    Thread.sleep(16);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                for(int i=0;i<DanmuView.this.getChildCount();i++){
                    View view = DanmuView.this.getChildAt(i);
                    if(view.getX()+view.getWidth() >= 0)
                        // 向左滑动
                        view.offsetLeftAndRight(0 - speed);
                    else{
                        //添加到缓存中
                        int type = ((InnerEntity)view.getTag(R.id.tag_inner_entity)).model.getType();
//                        adapter.addViewToCache(type,view);
                        DanmuView.this.removeView(view);

                    }
                }
            }

        }
    };

    public void setAdapter(DanmuAdapter adapter) {
        this.adapter = adapter;
        singltLineHeight = adapter.getSingleLineHeight();
        new Thread(new MyRunnable()).start();
    }

    public int getGravity() {
        return gravity;
    }

    public void setGravity(int gravity) {
        this.gravity = gravity;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getScanCount() {
        return spanCount;
    }

    public void setScanCount(int spanCount) {
        this.spanCount = spanCount;
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    class InnerEntity {
        public int bestLine;
        public DanmuModel model;
    }

    public interface OnItemClickListener {
        void onItemClick(DanmuModel model);
    }
}
