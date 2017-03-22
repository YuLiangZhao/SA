package com.zbar.lib.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 图片简单处理工具类
 */
public class ImageUtil {
	
	/**
	 * 屏幕宽
	 * 
	 * @param context
	 * @return
	 */
	public static int getWidth(Context context) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		return dm.widthPixels;
	}

	/**
	 * 屏幕高
	 * 
	 * @param context
	 * @return
	 */
	public static int getHeight(Context context) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		return dm.heightPixels;
	}
	
	/**
	 * 解决小米、魅族等定制ROM
	 * @param context
	 * @param intent
	 * @return
	 */
	 public static Uri getUri(Context context , Intent intent) {
	        Uri uri = intent.getData();
	        String type = intent.getType();
	        if (uri.getScheme().equals("file") && (type.contains("image/"))) {  
	            String path = uri.getEncodedPath();
	            if (path != null) {  
	                path = Uri.decode(path);
	                ContentResolver cr = context.getContentResolver();
	                StringBuffer buff = new StringBuffer();
	                buff.append("(").append(Images.ImageColumns.DATA).append("=")
	                        .append("'" + path + "'").append(")");  
	                Cursor cur = cr.query(Images.Media.EXTERNAL_CONTENT_URI,
	                        new String[] { Images.ImageColumns._ID },
	                        buff.toString(), null, null);  
	                int index = 0;  
	                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {  
	                    index = cur.getColumnIndex(Images.ImageColumns._ID);
	                    // set _id value  
	                    index = cur.getInt(index);  
	                }  
	                if (index == 0) {  
	                    // do nothing  
	                } else {  
	                    Uri uri_temp = Uri
	                            .parse("content://media/external/images/media/"  
	                                    + index);  
	                    if (uri_temp != null) {  
	                        uri = uri_temp;  
	                        Log.i("urishi", uri.toString());
	                    }  
	                }  
	            }  
	        }  
	        return uri;  
	    }  
	
	/**
	 * 根据文件Uri获取路径
	 * 
	 * @param context
	 * @param uri
	 * @return
	 */
	public static String getFilePathByFileUri(Context context, Uri uri) {
		String filePath = null;
		Cursor cursor = context.getContentResolver().query(uri, null, null,
				null, null);
		if (cursor.moveToFirst()) {
			filePath = cursor.getString(cursor
					.getColumnIndex(MediaStore.Images.Media.DATA));
		}
		cursor.close();
		return filePath;
	}
	/**
	 * 根据图片原始路径获取图片
	 *
	 * */
    public static Bitmap getBitmap(String path, int width, int height){
		Bitmap bm = null;
		//先解析图片边框的大小
		BitmapFactory.Options ops = new BitmapFactory.Options();
		ops.inJustDecodeBounds = true;
		bm = BitmapFactory.decodeFile(path, ops);
		ops.inSampleSize = 1;
		int oHeight = ops.outHeight;
		int oWidth = ops.outWidth;
		//控制压缩比
		int contentHeight = 0;
		int contentWidth = 0;
		contentHeight = height; //自定义
		contentWidth = width;   //自定义
		if(((float)oHeight/contentHeight) < ((float)oWidth/contentWidth)){
			ops.inSampleSize = (int) Math.ceil((float)oWidth/contentWidth);
		}else{
			ops.inSampleSize = (int) Math.ceil((float)oHeight/contentHeight);
		}
		ops.inJustDecodeBounds = false;
		bm = BitmapFactory.decodeFile(path, ops);
		return bm;
	}
	/**
	 * 根据图片原始路径获取图片缩略图
	 * 
	 * @param imagePath 图片原始路径
	 * @param width		缩略图宽度
	 * @param height	缩略图高度
	 * @return
	 */
	public static Bitmap getImageThumbnail(String imagePath, int width, int height) {
		Bitmap bitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;//不加载直接获取Bitmap宽高
		// 获取这个图片的宽和高，注意此处的bitmap为null
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		if(bitmap == null){
		// 计算缩放比
		int h = options.outHeight;
		int w = options.outWidth;
		Log.i("test", "optionsH"+h+"optionsW"+w);
		int beWidth = w / width;
		int beHeight = h / height;
		int rate = 1;
		if (beWidth < beHeight) {
			rate = beWidth;
		} else {
			rate = beHeight;
		}
		if (rate <= 0) {//图片实际大小小于缩略图,不缩放
			rate = 1;
		}
		options.inSampleSize = rate;
		options.inJustDecodeBounds = false; 
		// 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		// 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
				ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		}
		return bitmap;
	}
    /**
     * 将URI转成Bitmap
     *
     * @param context
     * @param uri
     * @return
     */
    public static Bitmap getBitmapByUri(Context context, Uri uri) {
        //Log.i("ImageUtil",context.getPackageCodePath());
        if (context == null || uri == null) return null;

        Bitmap bitmap;
        try {
            bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }
	/**
	 * 通过uri获取图片并进行压缩
	 *
	 * @param uri
	 */
	public static Bitmap getBitmapFormUri(Activity ac, Uri uri) throws FileNotFoundException, IOException {
		InputStream input = ac.getContentResolver().openInputStream(uri);
		BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
		onlyBoundsOptions.inJustDecodeBounds = true;
		onlyBoundsOptions.inDither = true;//optional
		onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
		BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
		input.close();
		int originalWidth = onlyBoundsOptions.outWidth;
		int originalHeight = onlyBoundsOptions.outHeight;
		if ((originalWidth == -1) || (originalHeight == -1))
			return null;
		//图片分辨率以480x800为标准
		float hh = 800f;//这里设置高度为800f
		float ww = 480f;//这里设置宽度为480f
		//缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;//be=1表示不缩放
		if (originalWidth > originalHeight && originalWidth > ww) {//如果宽度大的话根据宽度固定大小缩放
			be = (int) (originalWidth / ww);
		} else if (originalWidth < originalHeight && originalHeight > hh) {//如果高度高的话根据宽度固定大小缩放
			be = (int) (originalHeight / hh);
		}
		if (be <= 0)
			be = 1;
		//比例压缩
		BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
		bitmapOptions.inSampleSize = be;//设置缩放比例
		bitmapOptions.inDither = true;//optional
		bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
		input = ac.getContentResolver().openInputStream(uri);
		Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
		input.close();

		return compressImage(bitmap);//再进行质量压缩
	}
	/**
	 * 质量压缩方法
	 *
	 * @param image
	 * @return
	 */
	public static Bitmap compressImage(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > 1024) {  //循环判断如果压缩后图片是否大于1024kb,大于继续压缩
            //Log.d("ImageUtil",baos.toByteArray().length / 1024 + "=||=options:"+options);
            baos.reset();//重置baos即清空baos
			//第一个参数 ：图片格式 ，第二个参数： 图片质量，100为最高，0为最差  ，第三个参数：保存压缩后的数据的流
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;//每次都减少10
		}
        //Log.i("ImageUtil",baos.toByteArray().length / 1024 + "=||=options:"+options);
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
		return bitmap;
	}
	/**
	 * 通过Uri获取文件
	 * @param ac
	 * @param uri
	 * @return
	 */
	public static File getFileFromMediaUri(Context ac, Uri uri) {
		if(uri.getScheme().toString().compareTo("content") == 0){
			ContentResolver cr = ac.getContentResolver();
			Cursor cursor = cr.query(uri, null, null, null, null);// 根据Uri从数据库中找
			if (cursor != null) {
				cursor.moveToFirst();
				String filePath = cursor.getString(cursor.getColumnIndex("_data"));// 获取图片路径
				cursor.close();
				if (filePath != null) {
					return new File(filePath);
				}
			}
		}else if(uri.getScheme().toString().compareTo("file") == 0){
			return new File(uri.toString().replace("file://",""));
		}
		return null;
	}
	/**
	 * 读取图片的旋转的角度
	 *
	 * @param path 图片绝对路径
	 * @return 图片的旋转角度
	 */
	public static int getBitmapDegree(String path) {
		int degree = 0;
		try {
			// 从指定路径下读取图片，并获取其EXIF信息
			ExifInterface exifInterface = new ExifInterface(path);
			// 获取图片的旋转信息
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
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
	//将Bitmap转换成图片保存JPG到sd卡
	public static String SaveBmp2JPG(Bitmap mBitmap,String fileName)  {
		File f = new File( Environment.getExternalStorageDirectory() + fileName + ".jpg");
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
		try {
			fOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
        String filePath = f.getAbsolutePath();
        return filePath;
	}
    /**
     * Save Bitmap to a file.保存图片到SD卡。
     *
     * @param bitmap
     * @param fileName
     * @return error message if the saving is failed. null if the saving is
     *         successful.
     * @throws IOException
     */
    public static Uri saveBitmapToFile(Bitmap bitmap, String fileName)
            throws IOException {
        BufferedOutputStream os = null;
        try {
            File file = new File(fileName);
            int end = fileName.lastIndexOf(File.separator);
            String fileNamePath = fileName.substring(0, end);
            File filePath = new File(fileNamePath);
            if (!filePath.exists()) {
                filePath.mkdirs();
            }
            file.createNewFile();
            os = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
            Uri uri = Uri.parse(file.getPath());
            return uri;
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    Log.e("ImageUtils_Error", e.getMessage(), e);
                }
            }
        }
    }
	/**
	 * 转换图片成圆形
	 *
	 * @param bitmap
	 *            传入Bitmap对象
	 * @return
	 */
	public Bitmap toRoundBitmap(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float roundPx;
		float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
		if (width <= height) {
			roundPx = width / 2;
			top = 0;
			bottom = width;
			left = 0;
			right = width;
			height = width;
			dst_left = 0;
			dst_top = 0;
			dst_right = width;
			dst_bottom = width;
		} else {
			roundPx = height / 2;
			float clip = (width - height) / 2;
			left = clip;
			right = width - clip;
			top = 0;
			bottom = height;
			width = height;
			dst_left = 0;
			dst_top = 0;
			dst_right = height;
			dst_bottom = height;
		}
		Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
		final Rect dst = new Rect((int) dst_left, (int) dst_top, (int) dst_right, (int) dst_bottom);
		final RectF rectF = new RectF(dst);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(bitmap, src, dst, paint);
		return output;
	}
	//转换图片成圆形2
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {

		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final Paint paint = new Paint();
		//保证是方形，并且从中心画
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		int w;
		int deltaX = 0;
		int deltaY = 0;
		if (width <= height) {
			w = width;
			deltaY = height - w;
		} else {
			w = height;
			deltaX = width - w;
		}
		final Rect rect = new Rect(deltaX, deltaY, w, w);
		final RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		//canvas.drawARGB(alpha, R, G, B);//取值均在0-255
		//圆形，所有只用一个

		int radius = (int) (Math.sqrt(w * w * 2.0d) / 2);
		canvas.drawRoundRect(rectF, radius, radius, paint);

		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}
}
