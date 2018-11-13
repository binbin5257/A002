package cn.lds.chatcore.common;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.lidroid.xutils.BitmapUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;

import cn.lds.chatcore.MyApplication;
import cn.lds.chatcore.R;
import cn.lds.chatcore.enums.FileType;

/**
 * Author: wyouflf Date: 13-11-12 Time: 上午10:24
 */
public class BitmapHelper {
	private BitmapHelper() {
	}

	private static BitmapUtils bitmapUtils;

	/**
	 * BitmapUtils不是单例的 根据需要重载多个获取实例的方法
	 *
	 * @param
	 * @return
	 */
	public static BitmapUtils getBitmapUtils(boolean isIncoming) {
		if (bitmapUtils == null) {
			bitmapUtils = new BitmapUtils(MyApplication.getInstance().getApplicationContext(),
					FileHelper.getSaveFilePath(FileType.IMAGE));
			bitmapUtils.configDefaultAutoRotation(true);
			bitmapUtils.configDefaultLoadingImage(R.drawable.slidemenu_avtar);
			bitmapUtils.configDefaultLoadFailedImage(R.drawable.slidemenu_avtar);
			if (isIncoming) {
				bitmapUtils.configDefaultShowOriginal(true);
			}

		}
		return bitmapUtils;
	}

	public static BitmapUtils getVideoBitmapUtils() {
		if (bitmapUtils == null) {
			bitmapUtils = new BitmapUtils(MyApplication.getInstance().getApplicationContext(),
					FileHelper.getSaveFilePath(FileType.IMAGE));
			bitmapUtils.configDefaultAutoRotation(true);
			bitmapUtils.configDefaultShowOriginal(true);

		}
		return bitmapUtils;
	}

	public static BitmapUtils getBitmapUtils(Drawable drawable) {
		bitmapUtils = new BitmapUtils(MyApplication.getInstance().getApplicationContext(),
				FileHelper.getSaveFilePath(FileType.IMAGE));

		bitmapUtils.configDefaultAutoRotation(true);
		bitmapUtils.configDefaultConnectTimeout(3000);
		bitmapUtils.configDefaultReadTimeout(5000);
		bitmapUtils.configThreadPoolSize(10);

		if (null == drawable) {
			bitmapUtils.configDefaultLoadingImage(R.drawable.slidemenu_headavtar);
			bitmapUtils.configDefaultLoadFailedImage(R.drawable.slidemenu_headavtar);
		} else {
			bitmapUtils.configDefaultLoadingImage(drawable);
			bitmapUtils.configDefaultLoadFailedImage(drawable);
		}
		return bitmapUtils;
	}

	public static BitmapUtils getBitmapUtils(Drawable drawableLoading, Drawable drawableFailed) {
		bitmapUtils = new BitmapUtils(MyApplication.getInstance().getApplicationContext(),
				FileHelper.getSaveFilePath(FileType.IMAGE));

		bitmapUtils.configDefaultAutoRotation(true);
		bitmapUtils.configDefaultConnectTimeout(3000);
		bitmapUtils.configDefaultReadTimeout(5000);
		bitmapUtils.configThreadPoolSize(10);

		bitmapUtils.configDefaultLoadingImage(drawableLoading);
		bitmapUtils.configDefaultLoadFailedImage(drawableFailed);
		return bitmapUtils;
	}

	/**
	 * 解析二维码图片,返回结果封装在Result对象中
	 *
	 * @param bitmapPath 待定
	 */
	public static com.google.zxing.Result parseQRcodeBitmap(String bitmapPath) {
		//解析转换类型UTF-8
		Map<DecodeHintType, Object> hints = new EnumMap<DecodeHintType, Object>(DecodeHintType.class);
		Collection<BarcodeFormat> decodeFormats = EnumSet.noneOf(BarcodeFormat.class);
		decodeFormats.addAll(EnumSet.of(BarcodeFormat.QR_CODE));
		hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);
		hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
		Result result = null;
		try {
			//获取到待解析的图片
//            Bitmap bitmap = BitmapFactory.decodeFile(bitmapPath);
			Bitmap bitmap = getBitmapFromFile(bitmapPath, 800, 800);

			int lWidth = bitmap.getWidth();
			int lHeight = bitmap.getHeight();
			int[] lPixels = new int[lWidth * lHeight];
			bitmap.getPixels(lPixels, 0, lWidth, 0, 0, lWidth, lHeight);
			RGBLuminanceSource rgbLuminanceSource = new RGBLuminanceSource(lWidth, lHeight, lPixels);

			BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(rgbLuminanceSource));

			//初始化解析对象
			MultiFormatReader reader = new MultiFormatReader();
			//开始解析
			result = reader.decode(binaryBitmap, hints);
		} catch (NotFoundException e) {
			LogHelper.e("------1", e);
			e.printStackTrace();
		} catch (OutOfMemoryError e) {

		}

		return result;
	}

	/**
	 * 压缩图片
	 *
	 * @param filePath
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap getBitmapFromFile(String filePath, int width, int height) {
		if (null != filePath) {
			BitmapFactory.Options opts = null;
			if (width > 0 && height > 0) {

				opts = new BitmapFactory.Options(); //设置inJustDecodeBounds为true后，decodeFile并不分配空间，此时计算原始图片的长度和宽度
				opts.inJustDecodeBounds = true;
				BitmapFactory.decodeFile(filePath, opts);
				// 计算图片缩放比例
				final int minSideLength = Math.min(width, height);
				opts.inSampleSize = computeSampleSize(opts, minSideLength,
						width * height);
				//这里一定要将其设置回false，因为之前我们将其设置成了true
				opts.inJustDecodeBounds = false;
				opts.inInputShareable = true;
				opts.inPurgeable = true;
			}
			try {
				return BitmapFactory.decodeFile(filePath, opts);
			} catch (OutOfMemoryError e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static int computeSampleSize(BitmapFactory.Options options,
										int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);

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

	private static int computeInitialSampleSize(BitmapFactory.Options options,
												int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math
				.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
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
	 * 根据本地路径显示图片
	 *
	 * @param view 显示图片的控件
	 * @param path 文件路径
	 */
	public static void displayPic(ImageView view, String path) {

		int degree = GraphicHelper.readPictureDegree(path);

		Bitmap bitmap = null;
		try {
			bitmap = GraphicHelper.compressImage(path, 800);
			if (bitmap == null) {
				return;
			}
			bitmap = GraphicHelper.imageZoom(bitmap, 20);

		} catch (IOException e) {
		}

		Bitmap newbmp = GraphicHelper.rotateBitmapByDegree(bitmap, degree);
//        Bitmap bitmap = BitmapHelper.getBitmapFromFile(path, 200, 200);
		view.setImageBitmap(newbmp);
	}

	/**
	 * @param path    路径
	 * @param maxSize 图片的大小（kb）
	 */
	public static String compressPic(String path, int maxSize) {
		String newPath = FileHelper.getSaveBitmapPath();
		Bitmap bitmap = null;
		try {
			bitmap = GraphicHelper.compressImage(path, 800);
			if (bitmap == null) {
				return null;
			}
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
			int options = 100;
			bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
			// 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			while (baos.toByteArray().length / 1024 > maxSize) {
				// 重置baos即清空baos
				baos.reset();
				// 每次都减少10
				options -= 10;
				// 这里压缩options%，把压缩后的数据存放到baos中
				bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
			}

			File myFile = new File(newPath);
			if (!myFile.getParentFile().exists()) {
				myFile.getParentFile().mkdirs();
			}

			FileOutputStream fOut = null;
			fOut = new FileOutputStream(myFile);
			fOut.write(baos.toByteArray());
			fOut.flush();
			fOut.close();
		} catch (IOException e) {
		}
		return newPath;
	}
}
