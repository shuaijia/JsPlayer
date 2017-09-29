package com.jia.jsplayer.video;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.SurfaceHolder;

import com.jia.jsplayer.listener.OnPlayerCallback;

/**
 * 只包含最基础的播放器功能，MediaPlayer可以替换成其他框架的播放器
 * UI对刷新都需要在UI线程中完成，但是，surfaceview可以在非UI线程中完成刷新。
 * 这样以来就很方便了，比如在线播放，就不需要自己去写handler来实现两个
 * 线程之间的通信了，直接可以在非UI线程中播放视频。
 * Created by jia on 2017/9/2.
 */
public class JsMediaPlayer {
    private static final String TAG = "JsPlayer";

    //出错状态
    public static final int STATE_ERROR = -1;
    //通常状态
    public static final int STATE_IDLE = 0;
    //视频正在准备
    public static final int STATE_PREPARING = 1;
    //视频已经准备好
    public static final int STATE_PREPARED = 2;
    //视频正在播放
    public static final int STATE_PLAYING = 3;
    //视频暂停
    public static final int STATE_PAUSED = 4;
    //视频播放完成
    public static final int STATE_PLAYBACK_COMPLETED = 5;

    // 播放核心使用MediaPlayer
    private MediaPlayer player;
    // 当前状态
    private int curState = STATE_IDLE;
    // 当前缓冲进度
    private int currentBufferPercentage;
    // *视频路径
    private String path;

    // 播放监听
    private OnPlayerCallback onPlayerListener;
    // 播放视频承载的view
    private SurfaceHolder surfaceHolder;

    //出错回调，可以和其他回调一样在openVideo中写成匿名内部类，但因为该方法多次调用，所以将其提出
    private MediaPlayer.OnErrorListener onErrorListener = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            setCurrentState(STATE_ERROR);
            if (onPlayerListener != null) {
                onPlayerListener.onError(mp, what, extra);
            }
            return true;
        }
    };

    public void openVideo() {
        if (path == null || surfaceHolder == null) {
            return;
        }

        reset();

        player = new MediaPlayer();

        // 准备好的监听
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                //因为后面播放时要判断当前视频状态，所以在此一定要先将状态改变为STATE_PREPARED
                //即已经准备好，否则在第一次打开视频时无法自动播放
                setCurrentState(STATE_PREPARED);
                if (onPlayerListener != null) {
                    onPlayerListener.onPrepared(mp);
                }
            }
        });

        // 缓冲监听
        player.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                if (onPlayerListener != null) {
                    onPlayerListener.onBufferingUpdate(mp, percent);
                }
                currentBufferPercentage = percent;
            }
        });

        // 播放完成监听
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (onPlayerListener != null) {
                    onPlayerListener.onCompletion(mp);
                }
                setCurrentState(STATE_PLAYBACK_COMPLETED);
            }
        });

        // 信息监听
        player.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                if (onPlayerListener != null) {
                    // 701 加载中
                    if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
                        onPlayerListener.onLoadingChanged(true);
                        // 702 加载完成
                    } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
                        onPlayerListener.onLoadingChanged(false);
                    }
                }
                return false;
            }
        });

        // 出错监听
        player.setOnErrorListener(onErrorListener);

        // 视频大小切换监听
        player.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
            @Override
            public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                if (onPlayerListener != null) {
                    onPlayerListener.onVideoSizeChanged(mp, width, height);
                }
            }
        });

        currentBufferPercentage = 0;
        try {
            /**
             * 在这里开始真正的播放
             */
            player.setDataSource(path);
            player.setDisplay(surfaceHolder);
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setScreenOnWhilePlaying(true);
            player.prepareAsync();
            Log.e(TAG, "openVideo: " );
            setCurrentState(STATE_PREPARING);
        } catch (Exception e) {
            Log.e(TAG, "openVideo: " + e.toString());
            setCurrentState(STATE_ERROR);
            onErrorListener.onError(player, MediaPlayer.MEDIA_ERROR_UNKNOWN, 0);
        }
    }

    /**
     * 开始播放
     */
    public void start() {
        if (isInPlaybackState()) {
            player.start();
            setCurrentState(STATE_PLAYING);
        }
    }

    /**
     * 暂停播放
     */
    public void pause() {
        if (isInPlaybackState()) {
            if (player.isPlaying()) {
                player.pause();
                setCurrentState(STATE_PAUSED);
            }
        }
    }

    /**
     * 停止播放
     */
    public void stop() {
        if (player != null) {
            player.stop();
            player.release();

            player = null;
            surfaceHolder = null;
            setCurrentState(STATE_IDLE);
        }
    }

    /**
     * 重新播放
     */
    public void restart() {
        openVideo();
    }

    /**
     * 重置
     */
    public void reset() {
        if (player != null) {
            player.reset();
            player.release();
            setCurrentState(STATE_IDLE);
        }
    }


    /**
     * 设置当前播放状态
     *
     * @param state
     */
    private void setCurrentState(int state) {
        curState = state;
        if (onPlayerListener != null) {
            onPlayerListener.onStateChanged(state);
            switch (state) {
                case STATE_IDLE:
                case STATE_ERROR:
                case STATE_PREPARED:
                    onPlayerListener.onLoadingChanged(false);
                    break;
                case STATE_PREPARING:
                    onPlayerListener.onLoadingChanged(true);
                    break;
            }
        }
    }


    /**
     * 判断是否正在播放
     *
     * @return
     */
    public boolean isPlaying() {
        return (isInPlaybackState() && player.isPlaying());
    }

    public boolean isInPlaybackState() {
        return (player != null &&
                curState != STATE_ERROR &&
                curState != STATE_IDLE &&
                curState != STATE_PREPARING);
    }

    /**
     * 定位到
     *
     * @param progress
     */
    public void seekTo(int progress) {
        if (isInPlaybackState()) {
            player.seekTo(progress);
        }
    }

    /**
     * 获取视频总时长
     *
     * @return
     */
    public int getDuration() {
        if (isInPlaybackState()) {
            return player.getDuration();
        }

        return -1;
    }

    /**
     * 获得当前播放位置
     *
     * @return
     */
    public int getCurrentPosition() {
        if (isInPlaybackState()) {
            return player.getCurrentPosition();
        }
        return 0;
    }

    public MediaPlayer getPlayer() {
        return player;
    }

    public void setPlayer(MediaPlayer player) {
        this.player = player;
    }

    public int getCurState() {
        return curState;
    }

    /**
     * 获得当前缓冲进度
     *
     * @return
     */
    public int getBufferPercentage() {
        if (player != null) {
            return currentBufferPercentage;
        }
        return 0;
    }

    public void setCurrentBufferPercentage(int currentBufferPercentage) {
        this.currentBufferPercentage = currentBufferPercentage;
    }

    public String getPath() {
        return path;
    }

    /**
     * 设置播放路径，开始播放
     *
     * @param path
     */
    public void setPath(String path) {
        this.path = path;
//        openVideo();
    }

    public OnPlayerCallback getOnPlayerListener() {
        return onPlayerListener;
    }

    public void setOnPlayerListener(OnPlayerCallback onPlayerListener) {
        this.onPlayerListener = onPlayerListener;
    }

    public SurfaceHolder getSurfaceHolder() {
        return surfaceHolder;
    }

    public void setSurfaceHolder(SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
    }
}
