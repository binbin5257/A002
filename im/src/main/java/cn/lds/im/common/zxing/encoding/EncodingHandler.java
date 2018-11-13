package cn.lds.im.common.zxing.encoding;

import java.util.Hashtable;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
/**
 * @author Ryan Tang
 *
 */
public final class EncodingHandler {
	private static final int BLACK = 0xff000000;
	
	/**
	 * 根据内容生成二维码数据
	 * @param content 二维码文字内容[为了信息安全性，一般都要先进行数据加密]
	 * @param widthAndHeight 二维码照片宽度高度
	 * @return
	 */
	public static Bitmap createQRCode(String content,int widthAndHeight) throws WriterException {
		// 根据内容生成二维码数据
		BitMatrix matrix = createQRCode(content, widthAndHeight, widthAndHeight);
		
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		int[] pixels = new int[width * height];
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (matrix.get(x, y)) {
					pixels[y * width + x] = BLACK;
				}
			}
		}
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}
	
	/**
	 * 根据内容生成二维码数据
	 * @param content 二维码文字内容
	 * @param width 二维码照片宽度
	 * @param height 二维码照片高度
	 * @return
	 */
	public static BitMatrix createQRCode(String content, int width, int height){
		Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();   
		//设置字符编码
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");  
        // 指定纠错等级
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        BitMatrix matrix = null;  
        try {  
            matrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints); 
        } catch (WriterException e) {  
            e.printStackTrace();  
        }
        return matrix;
	}
	
	/**
	 * 将照片logo添加到二维码中间
	 * @param image 生成的二维码照片对象
	 * @param imagePath 照片保存路径
	 * @param logoPath logo照片路径
	 * @param formate 照片格式
	 */
	public static Bitmap overlapImage(Bitmap background, Bitmap forground) {
		if (background == null) {  
		    return null;  
		}  
 
		int bgWidth = background.getWidth();  
		int bgHeight = background.getHeight();  
		int forWidth = bgWidth/5;  
		int forHeight = bgHeight/5;  
		forground = resizeBitmap(forground, forWidth);  
		  
		Bitmap newBitmap = Bitmap.createBitmap(bgWidth, bgHeight, Bitmap.Config.ARGB_8888);  
		Canvas cv = new Canvas(newBitmap);  
		cv.drawBitmap(background, 0, 0, null);  
		cv.drawBitmap(forground, (bgWidth - forWidth) / 2, (bgHeight - forHeight) / 2, null);// 在src的右下角画入水印  
		cv.save(Canvas.ALL_SAVE_FLAG);  
		cv.restore();  
		return newBitmap;
	}
	
	public static Bitmap resizeBitmap(Bitmap bitmap, int newWidth) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int newHeight = newWidth;
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        // matrix.postRotate(45);
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
                matrix, true);
        // bitmap.recycle();
        return resizedBitmap;
    }
}
