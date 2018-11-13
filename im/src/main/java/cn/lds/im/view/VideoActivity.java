package cn.lds.im.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import cn.lds.chat.R;
import cn.lds.chatcore.common.FileHelper;
import cn.lds.chatcore.common.PopWindowHelper;
import cn.lds.chatcore.common.PopWindowListener;
import cn.lds.chatcore.common.ToolsHelper;
import cn.lds.chatcore.view.BaseActivity;

public class VideoActivity extends BaseActivity implements SurfaceHolder.Callback, View.OnTouchListener {
    protected Context mContext;

    //控件相关 flash闪光灯控制  changCamera摄像头切换 confirm确认使用 shoot摄像按钮 cancel取消使用 focus对焦
    protected ImageView flash, changCamera, confirm, shoot, cancel, focus;
    protected TextView time;//摄像时间
    protected SurfaceView surfaceView;//显示图像
    protected RelativeLayout bottom;
    protected ImageView back;

    //时间控制
    protected String shoottime;
    protected int minute = 0, second = 0;

    //摄像头相关
    protected Camera camera;
    protected Camera.AutoFocusCallback myAutoFocusCallback = null;
    protected Bitmap bmp;
    protected int width = 0, height = 0;
    protected float x = 0, y = 0;

    protected SurfaceHolder surfaceHolder;
    protected Handler handler;

    //摄像相关
    protected File myRecVideoFile;
    protected String path;

    // 录制视频的类
    protected MediaRecorder mediarecorder;

    //摄像状态控制
    protected final int STATUS_PREPARE = 0;//准备状态
    protected final int STATUS_SHOOTING = 1;//录制中
    protected final int STATUS_SHOOT = 2;//录制完成
    protected int status_shoot = STATUS_PREPARE;

    protected int layoutID = R.layout.activity_video;
    protected VideoActivity activity;

    protected void setActivity(VideoActivity activity) {
        this.activity = activity;
    }
    protected void setLayoutID(int layoutID) {
        this.layoutID = layoutID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        setContentView(layoutID);
        initConfig();
    }

    protected void initConfig() {
        mContext = VideoActivity.this;
        init();//初始化
        addClick();//绑定点击事件
    }

    protected void addClick() {
        //OnTouch事件绑定
        surfaceView.setOnTouchListener(this);
        bottom.setOnTouchListener(this);

        //OnClick事件绑定
        flash.setOnClickListener(listener);
        changCamera.setOnClickListener(listener);
        confirm.setOnClickListener(listener);
        shoot.setOnClickListener(listener);
        cancel.setOnClickListener(listener);
        back.setOnClickListener(listener);

    }

    /**
     * 初始化
     */
    protected void init() {
        //控件初始化
        flash = (ImageView) findViewById(R.id.iv_shooting_flash);
        changCamera = (ImageView) findViewById(R.id.iv_shooting_camera);
        confirm = (ImageView) findViewById(R.id.iv_shooting_confirm);
        shoot = (ImageView) findViewById(R.id.iv_shooting_shoot);
        cancel = (ImageView) findViewById(R.id.iv_shooting_cancel);
        focus = (ImageView) findViewById(R.id.iv_shooting_focus);
        focus.setVisibility(View.GONE);
        time = (TextView) findViewById(R.id.tv_shooting_time);
        surfaceView = (SurfaceView) findViewById(R.id.sv_shooting_surfaceView);
        bottom = (RelativeLayout) findViewById(R.id.rlt_shooting_bottom);
        back = (ImageView) findViewById(R.id.iv_shooting_back);

        //对焦时需要对焦控件的宽高，imageview会出现获取不到的情况，所以使用bitmap来获取
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.shooting_focus);
        width = bmp.getWidth();
        height = bmp.getHeight();

        refreshView();
        initPath();//初始化文件位置
        initHolder();//
        initFocus();//

    }

    protected void initMedia() {
        if (mediarecorder == null) {
            // 设置摄像头
            if (camera != null) {
                camera.stopPreview();
                camera.unlock();
            }

            // 创建mediarecorder对象
            mediarecorder = new MediaRecorder();
            // 设置摄像头
            mediarecorder.setCamera(camera);
            mediarecorder.setOrientationHint(90);//视频旋转90度
            //mediarecorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);
            mediarecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
            // 设置音频通道
            mediarecorder.setAudioChannels(1);
            // 设置音频采样率
            mediarecorder.setAudioSamplingRate(8000);

            // 设置录制视频源为Camera(相机)
            mediarecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            // 设置录制完成后视频的封装格式THREE_GPP为3gp.MPEG_4为mp4
            mediarecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            // 设置录制的视频编码
            mediarecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
            // 设置录制音频编解码器
            mediarecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            // 设置视频录制的分辨率。必须放在设置编码和格式的后面，否则报错
            mediarecorder.setVideoSize(640, 480);
            // 设置录制的视频帧率。必须放在设置编码和格式的后面，否则报错
//            mediarecorder.setVideoFrameRate(16);
//            CamcorderProfile cProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
//            mediarecorder.setProfile(cProfile);
            // 在这里我提高了帧频率，然后就清晰了
            mediarecorder.setVideoEncodingBitRate(2 * 1024 * 1024);
            mediarecorder.setPreviewDisplay(surfaceHolder.getSurface());
            // 设置视频文件的最大值为10M,单位B
//            mediarecorder.setMaxFileSize(10 * 1024 * 1024);

        }
        // 设置视频文件输出的路径
        mediarecorder.setOutputFile(path);
    }

    protected void refreshView() {
        switch (status_shoot) {
            case STATUS_PREPARE:
                confirm.setVisibility(View.GONE);
                cancel.setVisibility(View.GONE);
                shoot.setVisibility(View.VISIBLE);
                back.setVisibility(View.VISIBLE);
                shoot.setImageResource(R.drawable.btn_shooting_start);
                shoottime = String.format("%02d:%02d", minute, second);
                time.setText(shoottime);
                break;
            case STATUS_SHOOTING:
                confirm.setVisibility(View.GONE);
                cancel.setVisibility(View.GONE);
                shoot.setVisibility(View.VISIBLE);
                back.setVisibility(View.GONE);
                shoot.setImageResource(R.drawable.shooting_stop);
                break;
            case STATUS_SHOOT:
                confirm.setVisibility(View.VISIBLE);
                cancel.setVisibility(View.VISIBLE);
                shoot.setVisibility(View.GONE);
                back.setVisibility(View.GONE);
                break;
        }

    }

    protected void start() {
        initMedia();
        try {
            // 准备录制
            mediarecorder.prepare();
            // 开始录制
            mediarecorder.start();
        } catch (IllegalStateException e) {
            Log.e("录制视频", e.toString());
        } catch (IOException e) {
            Log.e("录制视频", e.toString());
        }
        status_shoot = STATUS_SHOOTING;
        shoot.setEnabled(true);
        handler.post(timeRun); // 调用Runable
    }

    protected void stop() {
        if (mediarecorder != null) {
            try {
                // 停止录制
                mediarecorder.stop();
                // 释放资源
                mediarecorder.release();
            } catch (RuntimeException e) {

            }

            mediarecorder = null;
            camera.stopPreview();
        }
        status_shoot = STATUS_SHOOT;
        shoot.setEnabled(false);
        minute = 0;
        second = 0;
        handler.removeCallbacks(timeRun);
    }

    /**
     * 录制过程中,时间变化,大小变化
     */
    protected Runnable timeRun = new Runnable() {

        @Override
        public void run() {
//            long fileLength=myRecVideoFile.length();
//            if(fileLength<1024 && fileLength>0){
//                size=String.format("%dB", fileLength);
//            }else if(fileLength>=1024 && fileLength<(1024*1024)){
//                fileLength=fileLength/1024;
//                size=String.format("%dK", fileLength);
//            }else if(fileLength>(1024*1024*1024)){
//                fileLength=(fileLength/1024)/1024;
//                size=String.format("%dM", fileLength);
//            }
            if (minute >= 1) {
                stop();
                refreshView();
                return;
            }
            second++;
            if (second == 60) {
                minute++;
                second = 0;
            }
            shoottime = String.format("%02d:%02d", minute, second);
            time.setText(shoottime);
            handler.postDelayed(timeRun, 1000);
        }
    };

    //对焦监听 成功或者失败都在这里处理
    protected void initFocus() {
        myAutoFocusCallback = new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                if (success) {
                    camera.cancelAutoFocus();//只有加上了这一句，才会自动对焦。
                    refreshFocus(false);
                } else {
                    camera.cancelAutoFocus();//只有加上了这一句，才会自动对焦。
                }
            }

        };
    }

    protected void initPath() {
        // 创建路径
        path = FileHelper.getTakeVideoPath();
        Log.d("录制视频的地址为：", path);
        myRecVideoFile = new File(path);
        if (!myRecVideoFile.getParentFile().exists())
            myRecVideoFile.getParentFile().mkdirs(); // 如果不存在则创建
        if (myRecVideoFile.exists()) {
            myRecVideoFile.delete();
        }

    }

    protected void initHolder() {
        surfaceHolder = surfaceView.getHolder();// 取得holder
        surfaceHolder.addCallback(this); // holder加入回调接口
        // setType必须设置，要不出错.
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceHolder.setKeepScreenOn(true);
        // 实例化handle
        handler = new Handler();

    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_shooting_camera:
                    ToolsHelper.showInfo(mContext, "iv_shooting_camera");
                    break;
                case R.id.iv_shooting_cancel:
                    cancel();
                    break;
                case R.id.iv_shooting_confirm:
                    // 关闭画面
                    Intent intent = new Intent();
                    intent.putExtra("path", path);
                    setResult(RESULT_OK, intent);
                    finish();
                    break;
                case R.id.iv_shooting_flash:
                    ToolsHelper.showInfo(mContext, "iv_shooting_flash");
                    break;
                case R.id.iv_shooting_back:
                    finish();
                    break;
                case R.id.iv_shooting_shoot:
                    shoot.setEnabled(false);
                    switch (status_shoot) {
                        case STATUS_PREPARE:
                            start();
                            break;
                        case STATUS_SHOOTING:
                            stop();
                            break;
                    }
                    refreshView();
                    break;

            }
        }

    };

    protected void cancel() {
        PopWindowHelper.getInstance().confirm(mContext, new PopWindowListener() {
            @Override
            public void confirm() {
                status_shoot = STATUS_PREPARE;
                shoot.setEnabled(true);
                refreshView();
                camera.startPreview();
            }

            @Override
            public void cancel() {

            }

            @Override
            public void onItemListener(int position) {

            }
        }).setContentTx(getString(R.string.switchimageactivity_cancel_record)).show(findViewById(R.id.top__iv));


    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // 开启相机
        if (camera == null) {
            // 查找后摄像头
            int CammeraIndex = FindBackCamera();
            if (CammeraIndex == -1) {
                // 查找前摄像头
                CammeraIndex = FindFrontCamera();
                if (CammeraIndex == -1) {
                    ToolsHelper.showInfo(getApplicationContext(), getString(R.string.switchimageactivity_nosupport));
                    return;
                }
            }
            // 打开摄像头
            try {
                camera = Camera.open(CammeraIndex);
            } catch (Exception e) {
                ToolsHelper.showInfo(getApplicationContext(), getString(R.string.switchimageactivity_openfailed));
                return;
            }


            try {
                camera.setPreviewDisplay(surfaceHolder);
                initCamera();
                camera.startPreview();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                camera.release();
            }


        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (camera != null) {
            //实现自动对焦
            camera.autoFocus(myAutoFocusCallback);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // surfaceDestroyed的时候同时对象设置为null
        surfaceView = null;
        surfaceHolder = null;
        mediarecorder = null;
        // 关闭预览并释放资源
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
        finish();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (R.id.rlt_shooting_bottom != v.getId()) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (camera != null) {
                    x = event.getX();
                    y = event.getY();
                    float touchMajor = event.getTouchMajor();
                    float touchMinor = event.getTouchMinor();
                    refreshFocus(true);
                    Rect touchRect = new Rect((int) (x - touchMajor / 2),
                            (int) (y - touchMinor / 2), (int) (x + touchMajor / 2), (int) (y + touchMinor / 2));
                    submitFocusAreaRect(touchRect);
                }
            }
        }
        return true;
    }

    protected void refreshFocus(boolean isFocusing) {
        if (isFocusing) {
            focus.setImageResource(R.drawable.shooting_focusing);
            focus.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams p = (RelativeLayout.LayoutParams) focus.getLayoutParams();
            p.setMargins((int) (x - (width / 2)), (int) (y - (width / 2)), 0, 0);
            focus.setLayoutParams(p);
        } else {
            focus.setImageResource(R.drawable.shooting_focus);
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    focus.setVisibility(View.GONE);
                }
            }, 1000);
        }

    }

    protected void submitFocusAreaRect(final Rect touchRect) {
//        camera.cancelAutoFocus();
        Camera.Parameters parameters = camera.getParameters();
        if (parameters == null
                || parameters.getMaxNumFocusAreas() == 0) {
            return;
        }
        // Convert from View's width and height to  /- 1000
        Rect focusArea = new Rect();
        focusArea.set(touchRect.left * 2000 / surfaceView.getWidth() - 1000,
                touchRect.top * 2000 / surfaceView.getHeight() - 1000,
                touchRect.right * 2000 / surfaceView.getWidth() - 1000,
                touchRect.bottom * 2000 / surfaceView.getHeight() - 1000);
        // Submit focus area to camera
        ArrayList<Camera.Area> focusAreas = new ArrayList<Camera.Area>();
        focusAreas.add(new Camera.Area(focusArea, 1000));
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        parameters.setFocusAreas(focusAreas);
        try {
            camera.setParameters(parameters);
            camera.autoFocus(myAutoFocusCallback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //相机参数的初始化设置
    protected void initCamera() {
        Camera.Parameters parameters = camera.getParameters();
        parameters.setPictureFormat(PixelFormat.JPEG);
        //parameters.setPictureSize(surfaceView.getWidth(), surfaceView.getHeight());  // 部分定制手机，无法正常识别该方法。
//        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);//开启闪光灯
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);//1连续对焦
        setDispaly(parameters, camera);
        try {
            camera.setParameters(parameters);
        } catch (Exception e) {

        }

        camera.setOneShotPreviewCallback(null);
        camera.startPreview();
        camera.autoFocus(myAutoFocusCallback);
//        camera.cancelAutoFocus();// 2如果要实现连续的自动对焦，这一句必须加上

    }

    //控制图像的正确显示方向
    protected void setDispaly(Camera.Parameters parameters, Camera camera) {
        if (Integer.parseInt(Build.VERSION.SDK) >= 8) {
            setDisplayOrientation(camera, 90);
        } else {
            parameters.setRotation(90);
        }

    }

    //实现的图像的正确显示
    protected void setDisplayOrientation(Camera camera, int i) {
        Method downPolymorphic;
        try {
            downPolymorphic = camera.getClass().getMethod("setDisplayOrientation", new Class[]{int.class});
            if (downPolymorphic != null) {
                downPolymorphic.invoke(camera, new Object[]{i});
            }
        } catch (Exception e) {
            Log.e("Came_e", "图像出错");
        }
    }

    //判断前置摄像头是否存在
    protected int FindFrontCamera() {
        int cameraCount = 0;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras(); // get cameras number

        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            Camera.getCameraInfo(camIdx, cameraInfo); // get camerainfo
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                // 代表摄像头的方位，目前有定义值两个分别为CAMERA_FACING_FRONT前置和CAMERA_FACING_BACK后置
                return camIdx;
            }
        }
        return -1;
    }

    //判断后置摄像头是否存在
    protected int FindBackCamera() {
        int cameraCount = 0;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras(); // get cameras number

        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            Camera.getCameraInfo(camIdx, cameraInfo); // get camerainfo
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                // 代表摄像头的方位，目前有定义值两个分别为CAMERA_FACING_FRONT前置和CAMERA_FACING_BACK后置
                return camIdx;
            }
        }
        return -1;
    }
}
