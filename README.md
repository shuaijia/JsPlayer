# JsPlayer

### 先上效果
![image](https://raw.githubusercontent.com/shuaijia/JsPlayer/master/img/aa.png)
![image](https://raw.githubusercontent.com/shuaijia/JsPlayer/master/img/bb.png)
![image](https://raw.githubusercontent.com/shuaijia/JsPlayer/master/img/cc.png)
![image](https://raw.githubusercontent.com/shuaijia/JsPlayer/master/img/dd.png)
![image](https://raw.githubusercontent.com/shuaijia/JsPlayer/master/img/ee.png)
![image](https://raw.githubusercontent.com/shuaijia/JsPlayer/master/img/ff.png)

### 历史版本
version | update 
----|------
v1.0 | 基础功能
v1.5 | 弹幕
v1.5.1 | 优化弹幕

## 使用
### 1、添依赖
```
allprojects {
    repositories {
        ...
        maven { url 'https://www.jitpack.io' }
    }
}
```
```
dependencies {
    compile 'com.github.shuaijia:JsPlayer:v1.0'
//or
    compile 'com.github.shuaijia:JsPlayer:v1.5'
//or
    compile 'com.github.shuaijia:JsPlayer:v1.5.1'
}
```
### 2、涉及到网络和文件读取，需要清单文件加权限
```
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```
### 3、需要切换大小屏播放的话，在清单文件的对应Activity添加
```
android:configChanges="orientation|keyboardHidden|screenSize"
```
### 4、布局中引用
```
<com.jia.jsplayer.video.JsPlayer
    android:id="@+id/player"
    android:layout_width="match_parent"
    android:layout_height="260dp" />
```
### 5、设置视频信息
```
player.setPath(new VideoInfo("艺术人生", path));
```
其中VideoInfo集成IVideoInfo，需重写getVideoTitle和getVideoPath方法，返回视频标题和路径，当然，**路径可以是本地路径，也可以是在线视频地址**。
### 6、实现播放处理回调
```
player.setOnVideoControlListener(new OnVideoControlListener() {
    @Override
    public void onStartPlay() {
        player.startPlay();
    }

    @Override
    public void onBack() {

    }

    @Override
    public void onFullScreen() {
        DisplayUtils.toggleScreenOrientation(MainActivity.this);
    }

    @Override
    public void onRetry(int errorStatus) {

    }
});
```
在onStartPlay回调中调用startPlay方法，既可开始播放。
### 7、生命周期
```
@Override
protected void onStop() {
    super.onStop();
    player.onStop();
}

@Override
protected void onDestroy() {
    super.onDestroy();
    player.onDestroy();
}
```
### 注意：返回键的控制和横竖屏切换
```
 @Override
public void onBackPressed() {
    if (!DisplayUtils.isPortrait(this)) {
        if (!player.isLock()) {
            DisplayUtils.toggleScreenOrientation(this);
        }
    } else {
    super.onBackPressed();
    }
}
```
```
@Override
public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
```

## 弹幕举例：
### 1、实体类的编写

创建实体类，但注意：**必须继承DanmuModel**

```
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
```

### 2、适配器

创建适配器，继承DanmuAdapter，设置泛型为刚刚创建的Model；
类似于ListView的Adapter写法：构造、ViewHolder和getView方法；
实现getViewTypeArray和getSingleLineHeight方法。

```
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
```
有木有很像ListView的Adapter！
相信大家一看就能明白，就不再多说。

### 3、配置基本信息

```
jsplayer_danmu.setDanMuAdapter(new MyDanmuAdapter(this));
jsplayer_danmu.setDanMuGravity(3);
jsplayer_danmu.setDanMuSpeed(DanmuView.NORMAL_SPEED);
```

### 4、创建实体类并设置给DanmuView

```
MyDanmuModel danmuEntity = new MyDanmuModel();
danmuEntity.setType(0);
danmuEntity.setContent(DANMU[random.nextInt(8)]);
danmuEntity.setTextColor(COLOR[random.nextInt(4)]);
jsplayer_danmu.addDanmu(danmuEntity);
```
更多精彩内容，请关注我的微信公众号——安卓干货营
![这里写图片描述](http://img.blog.csdn.net/20171009123234198?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvamlhc2h1YWk5NA==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)			

