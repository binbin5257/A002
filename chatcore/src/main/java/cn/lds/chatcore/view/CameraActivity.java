package cn.lds.chatcore.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.lds.chatcore.R;
import cn.lds.chatcore.common.BitmapHelper;
import cn.lds.chatcore.common.PopWindowHelper;
import cn.lds.chatcore.common.PopWindowListener;
import cn.lds.chatcore.common.ToolsHelper;

public class CameraActivity extends BaseActivity implements SurfaceHolder.Callback, View.OnClickListener {
    private Camera mCamera;
    private int cameraPosition = 0;//0代表前置摄像头,1代表后置摄像头,默认打开前置摄像头
    SurfaceHolder holder;

    SurfaceView mSurfaceView;
    ImageButton openLight;
    View focusIndex;
    View bootomRly;

    private float pointX, pointY;
    static final int FOCUS = 1;            // 聚焦
    static final int ZOOM = 2;            // 缩放
    private int mode;                      //0是聚焦 1是放大
    //放大缩小
    int curZoomValue = 0;
    private float dist;
    Camera.Parameters parameters;
    private Handler handler = new Handler();
    boolean safeToTakePicture = true;

    private ArrayList<String> pathList = new ArrayList<>();
//    RxPermissions rxPermissions;

    /* 图像数据处理完成后的回调函数 */
    private Camera.PictureCallback mJpeg = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(final byte[] data, Camera camera) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        //将照片改为竖直方向
                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                        Matrix matrix = new Matrix();
                        switch (cameraPosition) {
                            case 0://前
                                matrix.preRotate(90);
                                break;
                            case 1:
                                matrix.preRotate(90);
                                break;
                        }
                        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                        String imageFilePath = saveImageToGallery(getBaseContext(), bitmap);
                        String compressPath = BitmapHelper.compressPic(imageFilePath, 80);
                        pathList.add(compressPath);
                        runOnUiThread(
                                new Runnable() {
                                    @Override
                                    public void run() {

                                        if(pathList.size() == 0){
                                            carIv.setImageResource(R.drawable.bg_car_front);
                                            locationTv.setText("请对准车头位置");
                                        }else if(pathList.size() == 1){
                                            carIv.setImageResource(R.drawable.bg_car_back);
                                            locationTv.setText("请对准车尾位置");
                                        }else if(pathList.size() == 2){
                                            carIv.setImageResource(R.drawable.bg_car_left);
                                            locationTv.setText("请对准车左侧位置");
                                        }else if(pathList.size() == 3){
                                            carIv.setImageResource(R.drawable.bg_car_right);
                                            locationTv.setText("请对准车右侧位置");
                                        }else {
                                            carIv.setVisibility(View.INVISIBLE);
                                            locationTv.setText("拍照完成");
                                            Handler handler = new Handler();
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    //五秒后执行
                                                    Intent intent = new Intent();
                                                    Bundle bundle = new Bundle();
                                                    bundle.putStringArrayList("PATH_LIST",pathList);
                                                    intent.putExtras(bundle);
                                                    setResult(RESULT_OK,intent);
                                                    finish();
                                                }
                                            }, 1000);

                                        }
                                    }
                                }
                        );


                        mCamera.stopPreview();
                        mCamera.startPreview();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    safeToTakePicture = true;
                }
            }).start();
        }
    };
    private ImageView carIv;
    private TextView locationTv;
    private Animation mAnimationRight;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
           if(pathList.size() < 4){
               exitTakePhoto("确定要放弃本次拍照？");
           }else{
               return false;
           }
        }
        return true;
    }
    @Override
    public void finish(){
        super.finish();
        //关闭窗体动画显示
        this.overridePendingTransition(0,R.anim.bottom_pupup_out);
    }


    /**
     * 退出拍照
     */
    private void exitTakePhoto(String content) {
        PopWindowHelper.getInstance().remind(this, new PopWindowListener() {
            @Override
            public void confirm() {
                finish();
            }

            @Override
            public void cancel() {

            }

            @Override
            public void onItemListener(int position) {

            }
        }).setConfirmTx("确定")
                .setCancelTx("取消")
                .setContentTx(content).show(locationTv);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        CameraUtil.init(this);

        initView();

        initData();
    }

    private void initView() {
//        rxPermissions = RxPermissions.getInstance(this);

        mSurfaceView = (SurfaceView) findViewById(R.id.my_surfaceView);
        openLight = (ImageButton) findViewById(R.id.openLight);
        focusIndex = findViewById(R.id.focus_index);
        bootomRly = findViewById(R.id.bootomRly);
        carIv = (ImageView) findViewById(R.id.car_pic);
        locationTv = (TextView) findViewById(R.id.location_tv);
        mAnimationRight = AnimationUtils.loadAnimation(mContext, R.anim.rotate_right);
        mAnimationRight.setFillAfter(true);
        locationTv.setAnimation(mAnimationRight);

        ImageView image1 = (ImageView) findViewById(R.id.back);
        ImageView next = (ImageView) findViewById(R.id.lookPictureIv);
        Button button = (Button) findViewById(R.id.takePhoto);
        ImageButton imageButton2 = (ImageButton) findViewById(R.id.cameraSwitch);

        image1.setOnClickListener(this);
        next.setOnClickListener(this);
        button.setOnClickListener(this);
        openLight.setOnClickListener(this);
        imageButton2.setOnClickListener(this);
    }

    protected void initData() {
        holder = mSurfaceView.getHolder();
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        holder.addCallback(this); // 回调接口

        bootomRly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        mSurfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    // 主点按下
                    case MotionEvent.ACTION_DOWN:
                        pointX = event.getX();
                        pointY = event.getY();
                        mode = FOCUS;
                        break;
                    // 副点按下
                    case MotionEvent.ACTION_POINTER_DOWN:
                        dist = spacing(event);
                        // 如果连续两点距离大于10，则判定为多点模式
                        if (spacing(event) > 10f) {
                            mode = ZOOM;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:
                        mode = FOCUS;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (mode == FOCUS) {
                        } else if (mode == ZOOM) {
                            float newDist = spacing(event);
                            if (newDist > 10f) {
                                float tScale = (newDist - dist) / dist;
                                if (tScale < 0) {
                                    tScale = tScale * 20;
                                }
                                addZoomIn((int) tScale);
                            }
                        }
                        break;
                }
                return false;
            }
        });
        mSurfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    pointFocus((int) pointX, (int) pointY);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                RelativeLayout.LayoutParams layout = new RelativeLayout.LayoutParams(focusIndex.getLayoutParams());
                layout.setMargins((int) pointX - 60, (int) pointY - 60, 0, 0);
                focusIndex.setLayoutParams(layout);
                focusIndex.setVisibility(View.VISIBLE);
                ScaleAnimation sa = new ScaleAnimation(3f, 1f, 3f, 1f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
                sa.setDuration(800);
                focusIndex.startAnimation(sa);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        focusIndex.setVisibility(View.INVISIBLE);
                    }
                }, 700);
            }
        });
    }

    private void addZoomIn(int delta) {
        try {
            Camera.Parameters params = mCamera.getParameters();
            Log.d("Camera", "Is support Zoom " + params.isZoomSupported());
            if (!params.isZoomSupported()) {
                return;
            }
            curZoomValue += delta;
            if (curZoomValue < 0) {
                curZoomValue = 0;
            } else if (curZoomValue > params.getMaxZoom()) {
                curZoomValue = params.getMaxZoom();

            }

            if (!params.isSmoothZoomSupported()) {
                params.setZoom(curZoomValue);
                mCamera.setParameters(params);
                return;
            } else {
                mCamera.startSmoothZoom(curZoomValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //定点对焦的代码
    private void pointFocus(int x, int y) {
        mCamera.cancelAutoFocus();
        parameters = mCamera.getParameters();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            showPoint(x, y);
        }
        mCamera.setParameters(parameters);
        autoFocus();
    }

    /**
     * 两点的距离
     */
    private float spacing(MotionEvent event) {
        if (event == null) {
            return 0;
        }
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    private void showPoint(int x, int y) {
        if (parameters.getMaxNumMeteringAreas() > 0) {
            List<Camera.Area> areas = new ArrayList<Camera.Area>();
            //xy变换了
            int rectY = -x * 2000 / CameraUtil.screenWidth + 1000;
            int rectX = y * 2000 / CameraUtil.screenHeight - 1000;

            int left = rectX < -900 ? -1000 : rectX - 100;
            int top = rectY < -900 ? -1000 : rectY - 100;
            int right = rectX > 900 ? 1000 : rectX + 100;
            int bottom = rectY > 900 ? 1000 : rectY + 100;
            Rect area1 = new Rect(left, top, right, bottom);
            areas.add(new Camera.Area(area1, 800));
            parameters.setMeteringAreas(areas);
        }

        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
    }

    //实现自动对焦
    private void autoFocus() {
        new Thread() {
            @Override
            public void run() {
                try {
                    sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (mCamera == null) {
                    return;
                }
                mCamera.autoFocus(new Camera.AutoFocusCallback() {
                    @Override
                    public void onAutoFocus(boolean success, Camera camera) {
                        if (success) {
                            setupCamera(camera);//实现相机的参数初始化
                        }
                    }
                });
            }
        };
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mCamera.stopPreview();
        startPreview(mCamera, holder);
        autoFocus();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        startPreview(mCamera, holder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        releaseCamera();
    }

    public void onResume() {
        super.onResume();
        if (mCamera == null) {
            mCamera = getCamera(cameraPosition);
            if (holder != null) {
                if(mCamera != null){
                    startPreview(mCamera, holder);
                }else{
                    ToolsHelper.showStatus(mContext,false,"请打开相机权限");
                }

            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        releaseCamera();
    }

    /**
     * 闪光灯开关   开->关->自动
     *
     * @param mCamera
     */
    private void turnLight(Camera mCamera) {
        if (mCamera == null || mCamera.getParameters() == null
                || mCamera.getParameters().getSupportedFlashModes() == null) {
            return;
        }
        Camera.Parameters parameters = mCamera.getParameters();
        String flashMode = mCamera.getParameters().getFlashMode();
        List<String> supportedModes = mCamera.getParameters().getSupportedFlashModes();
        if (Camera.Parameters.FLASH_MODE_OFF.equals(flashMode)
                && supportedModes.contains(Camera.Parameters.FLASH_MODE_ON)) {//关闭状态
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
            mCamera.setParameters(parameters);
            openLight.setImageResource(R.drawable.camera_flash_on);
        } else if (Camera.Parameters.FLASH_MODE_ON.equals(flashMode)) {//开启状态
            if (supportedModes.contains(Camera.Parameters.FLASH_MODE_AUTO)) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
                openLight.setImageResource(R.drawable.camera_flash_auto);
                mCamera.setParameters(parameters);
            } else if (supportedModes.contains(Camera.Parameters.FLASH_MODE_OFF)) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                openLight.setImageResource(R.drawable.camera_flash_off);
                mCamera.setParameters(parameters);
            }
        } else if (Camera.Parameters.FLASH_MODE_AUTO.equals(flashMode)
                && supportedModes.contains(Camera.Parameters.FLASH_MODE_OFF)) {
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            mCamera.setParameters(parameters);
            openLight.setImageResource(R.drawable.camera_flash_off);
        }
    }

    /**
     * 获取Camera实例
     */
    private Camera getCamera(int id) {
        Camera camera = null;
        try {
            camera = Camera.open(id);
        } catch (Exception e) {

        }
        return camera;
    }

    /**
     * 预览相机
     */
    private void startPreview(Camera camera, SurfaceHolder holder) {
        try {
            setupCamera(camera);
            camera.setPreviewDisplay(holder);
            //亲测的一个方法 基本覆盖所有手机 将预览矫正
            CameraUtil.getInstance().setCameraDisplayOrientation(this, cameraPosition, camera);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 释放相机资源
     */
    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 设置
     */
    private void setupCamera(Camera camera) {
        Camera.Parameters parameters = camera.getParameters();

        List<String> focusModes = parameters.getSupportedFocusModes();
        if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
            // Autofocus mode is supported 自动对焦
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        }

        Camera.Size previewSize = CameraUtil.findBestPreviewResolution(camera);
        parameters.setPreviewSize(previewSize.width, previewSize.height);

        Camera.Size pictrueSize = CameraUtil.getInstance().getPropPictureSize(parameters.getSupportedPictureSizes(), 1000);
        parameters.setPictureSize(pictrueSize.width, pictrueSize.height);
//        parameters.setZoom(-100);
        camera.setParameters(parameters);

        int picHeight = CameraUtil.screenWidth * previewSize.width / previewSize.height;
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(CameraUtil.screenWidth, picHeight);
        mSurfaceView.setLayoutParams(params);

    }

    //将bitmap保存在本地，然后通知图库更新
    public String  saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "tcr");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "保存图片失败", Toast.LENGTH_SHORT).show();
        }
        return file.getPath();

        // 其次把文件插入到系统图库
//        try {
//            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//            Toast.makeText(context, "保存图片失败", Toast.LENGTH_SHORT).show();
//        }
        // 最后通知图库更新
//        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file)));
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.back) {
            if(pathList.size() < 4){
                exitTakePhoto("确定要放弃本次拍照？");
            }

        } else if (i == R.id.lookPictureIv) {//指定照片
//                            File appDir = new File(Environment.getExternalStorageDirectory(), "轻牛");
//                            intent.setDataAndType(Uri.fromFile(appDir), "image/*");

            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setType("image/*");

            startActivity(intent);

        } else if (i == R.id.takePhoto) {
            if(pathList.size() < 4){
                if (safeToTakePicture) {
                    safeToTakePicture = false;
                    mCamera.takePicture(null, null, mJpeg);
                }
            }else{
                ToolsHelper.showStatus(mContext,true,"拍照已经完成");
            }


        } else if (i == R.id.openLight) {
            turnLight(mCamera);

        } else if (i == R.id.cameraSwitch) {
            releaseCamera();
            cameraPosition = (cameraPosition + 1) % mCamera.getNumberOfCameras();
            mCamera = getCamera(cameraPosition);
            if (holder != null) {
                startPreview(mCamera, holder);
            }

        }

    }
}
