# JsPlayer

### 先上效果
![image](https://raw.githubusercontent.com/shuaijia/JsPlayer/master/img/aa.png)
![image](https://raw.githubusercontent.com/shuaijia/JsPlayer/master/img/bb.png)
![image](https://raw.githubusercontent.com/shuaijia/JsPlayer/master/img/cc.png)
![image](https://raw.githubusercontent.com/shuaijia/JsPlayer/master/img/dd.png)
![image](https://raw.githubusercontent.com/shuaijia/JsPlayer/master/img/ee.png)

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

## 弹幕相关
