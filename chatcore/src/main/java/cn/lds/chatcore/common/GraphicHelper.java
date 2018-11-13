package cn.lds.chatcore.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Thumbnails;
import android.util.Base64;
import android.view.ViewGroup.LayoutParams;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.lds.chatcore.MyApplication;
import cn.lds.chatcore.R;

public class GraphicHelper {

    public static final int IMAGE_CAPTURE = 10;
    public static final int VIDEO_CAPTURE = 40;

    public static final int IMAGE_SELECT = 20;

    public static final int CROP_PICTURE = 30;

    public static final String IMAGE_TYPE = "image/*";

    // public static final String IMAGE_EXTENSION_JPEG = "jpg";

    public static final String TYPE_JPEG = "image/jpeg";

    /**
     * 添加本地图片的Intent
     */
    public static Intent getImageIntent() {
        Intent intent = new Intent();
        intent.setType(IMAGE_TYPE);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        return intent;
    }

    /**
     * 计算图片的缩放值
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }


    /**
     * 根据路径获得图片并压缩返回bitmap用于显示
     *
     * @return
     */
    public static Bitmap getSmallBitmap(String filePath) {
        //		final BitmapFactory.Options options = new BitmapFactory.Options();
        //		options.inJustDecodeBounds = true;
        //		BitmapFactory.decodeFile(filePath, options);
        //
        //		// Calculate inSampleSize
        //		options.inSampleSize = calculateInSampleSize(options, 480, 800);
        //
        //		// Decode bitmap with inSampleSize set
        //		options.inJustDecodeBounds = false;

        return getScaleBitmap(filePath, 480, 800);
    }

    /**
     * 根据路径获得图片并压缩返回bitmap用于显示
     *
     * @return
     */
    public static Bitmap getScaleBitmap(String filePath, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * 拍摄小视频的Intent
     */
    public static Intent getVideoIntent(String path) {

        File file = new File(path);
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs(); // 如果不存在则创建
        if (file.exists()) {
            file.delete();
        }

        Intent intent = new Intent();
        intent.setAction("android.media.action.VIDEO_CAPTURE");
        intent.addCategory("android.intent.category.DEFAULT");

        // 在这里的QUALITY参数，值为两个，一个是0，一个是1，代表录制视频的清晰程度，0最不清楚，1最清楚
        // 没有0-1的中间值，另外，使用1也是比较占内存的，测试了一下，录制1分钟，大概内存是43M多
        // 使用0，录制1分钟大概内存是几兆
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
        // 限制时长 ，参数61代表61秒，可以根据需求自己调，最高应该是2个小时。
        // 当在这里设置时长之后，录制到达时间，系统会自动保存视频，停止录制
        // intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10);
        // 限制大小 限制视频的大小，这里是20兆。当大小到达的时候，系统会自动停止录制
        intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 1024 * 1024 * 20);

        //如果修改使用mediarecord来录制，则需要设置下面两个参数
        //MediaRecorder.VideoEncoder.MPEG_4_SP
        //MediaRecorder.AudioEncoder.AAC

        Uri uri = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

        return intent;
    }

    /**
     * 拍照的Intent
     *
     * @param picpath
     */
    public static Intent getPhotoIntent(String picpath) {
        File file = new File(picpath);
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs(); // 如果不存在则创建
        if (file.exists()) {
            file.delete();
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        return intent;
    }

    /**
     * 将图片插入系统图库的方法，如果不执行系统图库可能看不到该图片
     *
     * @param context
     * @param path    文件地址
     */
    public static void insertGystemGallery(Context context, String path) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(new File(path));
        intent.setData(uri);
        context.sendBroadcast(intent);
    }

    /**
     * 添加本地图片的裁剪Intent
     */
    public static Intent getSelectCropPictureIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);

        intent.setType("image/*");
        intent.putExtra("crop", "true");// crop=true 有这句才能出来最后的裁剪页面.

        intent.putExtra("aspectX", 1);// 这两项为裁剪框的比例.
        intent.putExtra("aspectY", 1);// x:y=1:1

        // intent.putExtra("circleCrop", "true");

        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 96);
        intent.putExtra("outputY", 96);

        // 图片格式
        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("return-data", true);

        return intent;
    }

    public static Drawable getRepeatDrawable(BitmapDrawable paramBitmapDrawable) {
        try {
            paramBitmapDrawable.setAntiAlias(true);
            paramBitmapDrawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
            paramBitmapDrawable.setDither(true);

        } catch (Exception localException) {

        }
        return paramBitmapDrawable;
    }

    /**
     * Decode a bitmap
     */
    public static Bitmap decodeBitmap(String path) {

        int degree = readPictureDegree(path);

        BitmapFactory.Options bfo = new BitmapFactory.Options();
        bfo.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, bfo);

        bfo.inSampleSize = computeSampleSize(bfo, -1, 960 * 960);
        bfo.inJustDecodeBounds = false;

        Bitmap df = resizeBitmap(BitmapFactory.decodeFile(path, bfo), 720, degree);
        return df;
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface
                    .ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    public static Bitmap getTakePhotoBitmap() {
        return getSmallBitmap(FileHelper.getTakePhotoPath());
    }

    public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);

        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    public static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h /
                minSideLength));

        if (upperBound < lowerBound) {
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm     需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }

    public static Bitmap resizeBitmap(Bitmap bitmap, int newWidth, int angle) {
        if (bitmap == null)
            return null;

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float temp = ((float) height) / ((float) width);
        if (width < newWidth) {
            newWidth = width;
        }
        int newHeight = (int) ((newWidth) * temp);
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        matrix.postRotate(angle);
        // matrix.postRotate(45);
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        // bitmap.recycle();
        return resizedBitmap;
    }

    public static Drawable resizeDrawable(Drawable image, int size) {
        Bitmap b = ((BitmapDrawable) image).getBitmap();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, size, size, false);
        return new BitmapDrawable(bitmapResized);
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        // 取 drawable 的长宽
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();

        // 取 drawable 的颜色格式
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config
                .RGB_565;
        // 建立对应 bitmap
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        // 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        // 把 drawable 内容画到画布中
        drawable.draw(canvas);
        return bitmap;
    }

    public static byte[] getByteFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.JPEG, 50, baos);
        byte[] bytes = baos.toByteArray();
        return bytes;
    }

    // public static String getHashFromBase64Byte(byte[] data) {
    // if (data == null)
    // return null;
    // MessageDigest digest;
    // try {
    // digest = MessageDigest.getInstance("SHA-1");
    // } catch (NoSuchAlgorithmException e) {
    // e.printStackTrace();
    // return null;
    // }
    // digest.update(data);
    // return StringUtils.encodeHex(digest.digest());
    // }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
        if (bitmap == null)
            return null;
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        // final Rect rectd = new Rect(0, 0, bitmap.getWidth()*1.5,
        // bitmap.getHeight()1.5);
        final RectF rectF = new RectF(rect);

        // canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public static void saveBitmapToFileFromPath(Bitmap mBitmap, String path) {
        try {
            File myFile = new File(path);
            if (!myFile.getParentFile().exists()) {
                myFile.getParentFile().mkdirs();
            }

            FileOutputStream fOut = null;
            fOut = new FileOutputStream(myFile);

            mBitmap.compress(Bitmap.CompressFormat.JPEG, 50, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            LogHelper.wtf(e);
        }

    }

    /**
     * Get real path
     */
    public static String getRealImagePath(Activity activity, Uri uri) {
        if (uri.toString().startsWith("content")) {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = activity.managedQuery(uri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else if (uri.toString().startsWith("file")) {
            String path = Uri.decode(uri.toString());
            return path.substring(7, path.length());
        } else {
            return "";
        }
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            LogHelper.wtf(e);
            return null;
        }
    }


    public static Bitmap getVideoThumbnail(String videoPath,int w,int h) {
        Bitmap bitmap = null;
        // 获取视频的缩略图
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, Thumbnails.FULL_SCREEN_KIND);
        if (null == bitmap) {
            // TODO （临时注释掉）
            bitmap = BitmapFactory.decodeResource(MyApplication.getInstance().getApplicationContext().getResources(),
                    R.drawable.ic_help);
        }
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, bitmap.getWidth() > bitmap.getHeight() ? h : w, bitmap
                .getWidth() > bitmap.getHeight() ? w : h, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);


        return bitmap;
    }
    /**
     * 压缩图片。
     *
     * @param path 图片路径
     * @param size 图片最大尺寸
     * @return 压缩后的图片
     * @throws java.io.IOException
     */
    public static Bitmap compressImage(String path, int size) throws IOException {
        Bitmap bitmap = null;
        // 取得图片
        InputStream is = new FileInputStream(path);
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 这个参数代表，不为bitmap分配内存空间，只记录一些该图片的信息（例如图片大小），说白了就是为了内存优化
        options.inJustDecodeBounds = true;
        // 通过创建图片的方式，取得options的内容（这里就是利用了java的地址传递来赋值）
        BitmapFactory.decodeStream(is, null, options);
        // 关闭流
        is.close();


        int scalesize = 1;

        if (options.outWidth < size && options.outHeight < size) {

        } else {
            if (options.outWidth >= options.outHeight) {
                scalesize = (int) (options.outWidth / size);
            } else {
                scalesize = (int) (options.outHeight / size);
            }
        }

        // 重新取得流，注意：这里一定要再次加载，不能二次使用之前的流！
        is = new FileInputStream(path);
        // 这个参数表示 新生成的图片为原始图片的几分之一。
        options.inSampleSize = scalesize;
        // 这里之前设置为了true，所以要改为false，否则就创建不出图片
        options.inJustDecodeBounds = false;
        // 使用RGB565，否则会OOM
        options.inPreferredConfig = Config.RGB_565;
        // 同时设置才会有效
        options.inPurgeable = true;
        // 当系统内存不够时候图片自动被回收
        options.inInputShareable = true;
        // 创建Bitmap
        bitmap = BitmapFactory.decodeStream(is, null, options);
        return bitmap;
    }

    /**
     * 缩放图片到固定文件大小
     *
     * @param bm      需要缩放的图片
     * @param maxSize 目标文件大小，单位：KB
     * @return
     */
    public static Bitmap imageZoom(Bitmap bm, double maxSize) {
        // 图片允许最大空间 单位：KB
        // 将bitmap放至数组中，意在bitmap的大小（与实际读取的原文件要大）
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        // 将字节换成KB
        double mid = b.length / 1024;
        // 判断bitmap占用空间是否大于允许最大空间 如果大于则压缩 小于则不压缩
        if (mid > maxSize) {
            // 获取bitmap大小 是允许最大大小的多少倍
            double i = mid / maxSize;
            // 开始压缩 此处用到平方根 将宽带和高度压缩掉对应的平方根倍
            // 保持刻度和高度和原bitmap比率一致，压缩后也达到了最大大小占用空间的大小
            bm = zoomImage(bm, bm.getWidth() / Math.sqrt(i), bm.getHeight() / Math.sqrt(i));
        }
        return bm;
    }

    /***
     * 图片的缩放方法
     *
     * @param bgimage   ：源图片资源
     * @param newWidth  ：缩放后宽度
     * @param newHeight ：缩放后高度
     * @return
     */
    public static Bitmap zoomImage(Bitmap bgimage, double newWidth, double newHeight) {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width, (int) height, matrix, true);
        return bitmap;
    }

    /**
     * bitmap转为base64
     *
     * @param bitmap
     * @return
     */
    public static String bitmapToBase64(Bitmap bitmap) {

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * base64转为bitmap
     *
     * @param base64Data
     * @return
     */
    public static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public static LayoutParams scaleDidplayChatImage(int thumbnailWidth, int thumbnailHeight) {
        int displayWidth, displayHeight;

        double scale = MyApplication.getInstance().getWidth() * 0.4;
        if (thumbnailWidth >= thumbnailHeight) {
            displayWidth = (int) scale; //MyApplication.getInstance().getWidth() * ;
            displayHeight = (int) (scale * ((thumbnailHeight * 1.0) / thumbnailWidth));
        } else {
            displayHeight = (int) scale;
            displayWidth = (int) (scale * ((thumbnailWidth * 1.0) / thumbnailHeight));
        }

        LayoutParams mParams = new android.widget.LinearLayout.LayoutParams(displayWidth, displayHeight);
        return mParams;
    }
}
