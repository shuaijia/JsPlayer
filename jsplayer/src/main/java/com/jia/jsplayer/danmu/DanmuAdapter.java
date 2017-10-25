package com.jia.jsplayer.danmu;

import android.view.View;

import java.util.HashMap;
import java.util.Stack;

/**
 * Description: 弹幕适配器
 * Created by jia on 2017/9/25.
 * 人之所以能，是相信能
 */
public abstract class DanmuAdapter<M> {

//    // 使用HashMap，以类型和对应view的栈为key-value存储，实现缓存
//    private HashMap<Integer, Stack<View>> cacheViews;
//    // 类型数组
//    private int[] typeArray;
//
//    public DanmuAdapter() {
//        cacheViews = new HashMap<>();
//        typeArray = getViewTypeArray();
//        for (int i = 0; i < typeArray.length; i++) {
//            Stack<View> stack = new Stack<>();
//            cacheViews.put(typeArray[i], stack);
//        }
//    }
//
//    /**
//     * 将弹幕itemView加入缓存（压栈）
//     *
//     * @param type
//     * @param view
//     */
//    synchronized public void addViewToCache(int type, View view) {
//        if (cacheViews.containsKey(type)) {
//            cacheViews.get(type).push(view);
//        } else {
//            throw new Error("your cache has not this type");
//        }
//    }
//
//    /**
//     * 将itemView移出缓存（弹栈）
//     *
//     * @param type
//     * @return
//     */
//    synchronized public View removeViewFromCache(int type) {
//        if (cacheViews.containsKey(type) && cacheViews.get(type).size() > 0)
//            return cacheViews.get(type).pop();
//        else
//            return null;
//
//    }
//
//    /**
//     * 减小缓存大小
//     */
//    public void shrinkCacheSize() {
//        int[] typeArray = getViewTypeArray();
//        for (int i = 0; i < typeArray.length; i++) {
//            if (cacheViews.containsKey(typeArray[i])) {
//                Stack<View> typeStack = cacheViews.get(typeArray[i]);
//                int length = typeStack.size();
//                // 循环弹栈，直到大小变为原来一半
//                while (typeStack.size() > (int) (length / 2.0 + 0.5)) {
//                    typeStack.pop();
//                }
//                cacheViews.put(typeArray[i], typeStack);
//            }
//        }
//    }
//
//    /**
//     * 获取缓存大小
//     *
//     * @return
//     */
//    public int getCacheSize() {
//        int size = 0;
//        int[] types = getViewTypeArray();
//        for (int i = 0; i < types.length; i++) {
//            size = size + cacheViews.get(types[i]).size();
//        }
//        return size;
//    }

    /**
     * 获取类型数组
     *
     * @return
     */
    public abstract int[] getViewTypeArray();

    /**
     * 获取单行弹幕 高度
     *
     * @return
     */
    public abstract int getSingleLineHeight();

    /**
     * 获取itemView
     *
     * @param entry
     * @param convertView
     * @return
     */
    public abstract View getView(M entry, View convertView);
}
