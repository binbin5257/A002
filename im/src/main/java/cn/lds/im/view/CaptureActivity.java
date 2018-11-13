package cn.lds.im.view;

import android.Manifest;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.io.IOException;
import java.util.Vector;

import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.chat.R;
import cn.lds.im.common.permission.PermissionHelper;
import cn.lds.im.common.zxing.camera.CameraManager;
import cn.lds.im.common.zxing.decoding.CaptureActivityHandler;
import cn.lds.im.common.zxing.decoding.InactivityTimer;
import cn.lds.im.common.zxing.view.ViewfinderView;
import cn.lds.im.view.barcode.BarcodeRoute;
import cn.lds.chatcore.view.BaseActivity;

/**
 * 扫描二维码Activity
 *
 * @author quwei
 */
public class CaptureActivity extends BaseActivity implements Callback {


    // 当前上下文对象
    protected Context mContext;
    protected CaptureActivityHandler handler;
    protected boolean hasSurface;

    // zxing解码相关
    protected Vector<BarcodeFormat> decodeFormats;
    protected String characterSet;
    protected InactivityTimer inactivityTimer;

    // 媒体播放器
    protected MediaPlayer mediaPlayer;
    // 手机哔哔声音控制
    protected boolean playBeep;
    protected static final float BEEP_VOLUME = 0.10f;
    // 手机震动控制
    protected boolean vibrate;

    // 标题
    @ViewInject(R.id.top_title_tv)
    protected TextView topTitle;
    // 返回按钮
    @ViewInject(R.id.top_back_btn)
    protected Button btnBack;
    // 扫描视图
    @ViewInject(R.id.viewfinder_view)
    protected ViewfinderView viewfinderView;

    protected BarcodeRoute barcodeRoute;

    protected CaptureActivity activity;
    protected int layoutID = R.layout.activity_capture_scan;

    public void setLayoutID(int layoutID) {
        this.layoutID = layoutID;
    }

    public void setActivity(CaptureActivity activity) {
        this.activity = activity;
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutID);
        ViewUtils.inject(CaptureActivity.class,this);
        if(null != activity){
            ViewUtils.inject(activity);
        }

        initConfig();
    }
    protected void initConfig() {
        initView();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            // 检查权限
            PermissionHelper.checkPermission(this,
                    Manifest.permission.CAMERA
            );
        }
    }

    /**
     * 初始化视图
     */
    protected void initView() {

        // 初始化对象
        mContext = this;
        // 设置标题
        topTitle.setText(R.string.capture_title);
        // 设置返回按钮
        btnBack.setVisibility(View.VISIBLE);
        // 初始化弹出窗口
        CameraManager.init(mContext);

        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
    }


    /**
     * 解码成功触发
     *
     * @param result  解码结果
     * @param barcode 解码图片
     */
    public void handleDecode(Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        String resultString = result.getText();
        System.out.println("--->> 解析的二维码：" + resultString);

        //handleResult(resultString);
        if (barcodeRoute == null) {
            barcodeRoute = new BarcodeRoute(this);
        }
        barcodeRoute.handleResult(resultString);
    }

    /**
     * 准备开始扫描
     */
    public void prepareScan() {
        // 关闭相机
        resetCamera();
        // 重置视图
        resetSurfaceView();
        // 重置声音
        resetBeepSound();
    }

    /**
     * 初始化相机
     *
     * @param surfaceHolder
     */
    protected void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            ToolsHelper.showStatus(mContext, false, getString(R.string.toast_error_camera));
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
        }
    }

    /**
     * 关闭相机
     */
    protected void resetCamera() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    /**
     * 重置SurfaceView
     */
    protected void resetSurfaceView() {
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(CaptureActivity.this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();
    }

    /**
     * 初始化声音
     */
    protected void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    /**
     * 重置声音
     */
    protected void resetBeepSound() {
        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;
    }

    protected static final long VIBRATE_DURATION = 200L;

    /**
     * 播放声音和震动
     */
    protected void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    protected final OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

    /**
     * 点击返回按钮
     *
     * @param v
     */
    @OnClick(R.id.top_back_btn)
    public void btnTopBackClick(View v) {
        finish();
    }

    /**
     * 重新加载界面调用
     */
    @Override
    protected void onResume() {
        super.onResume();
        // 重置
        resetSurfaceView();
        // 设置声音播放标志
        resetBeepSound();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        resetCamera();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        if (null != barcodeRoute)
            barcodeRoute.unregister();
        super.onDestroy();
    }
}
