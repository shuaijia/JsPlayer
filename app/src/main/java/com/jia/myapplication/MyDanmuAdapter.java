package com.jia.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.jia.jsplayer.danmu.DanmuAdapter;

/**
 * Description: 弹幕适配器
 * Created by jia on 2017/9/25.
 * 人之所以能，是相信能
 */
public class MyDanmuAdapter extends DanmuAdapter<MyDanmuModel> {

    private Context context;

    public MyDanmuAdapter(Context c){
        super();
        context = c;
    }

    @Override
    public int[] getViewTypeArray() {
        int type[] = {0};
        return type;
    }

    @Override
    public int getSingleLineHeight() {
        View view = LayoutInflater.from(context).inflate(R.layout.item_danmu, null);
        //指定行高
        view.measure(0, 0);

        return view.getMeasuredHeight();
    }

    @Override
    public View getView(MyDanmuModel entry, View convertView) {
        ViewHolder vh=null;
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.item_danmu,null);
            vh=new ViewHolder();
            vh.tv=convertView.findViewById(R.id.tv_danmu);
            convertView.setTag(vh);
        }else{
            vh= (ViewHolder) convertView.getTag();
        }

        vh.tv.setText(entry.getContent());
        vh.tv.setTextColor(entry.getTextColor());

        return convertView;
    }

    class ViewHolder{
        TextView tv;
    }
}
