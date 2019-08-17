package com.zbar.lib.custom_views.imgview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;

import com.zbar.lib.R;


public class RoundCanvasView extends ImageView {
    public final static String TAG="HeadRoundCanvasView";
    /**需要设置圆角的源图片*/
    private Bitmap mBitmap;
    /**绘制圆角bitmap的画笔*/
    private Paint mPaint;
    /**绘制的矩形区域*/
    private Rect rect;
    /**绘制带有圆角的矩形区域*/
    private RectF rectF;
    /**圆角bitmap的圆角弧度*/
    private int roundRadius = 40;
    /**屏幕密度*/
    private DisplayMetrics displayMetrics;
    /**ImageView所占的宽*/
    private float width = 80;
    /**ImageView所占的高*/
    private float height = 80;
    /**边框颜色*/
    private int borderColor = -1;
    /**边框宽度*/
    private int borderWidth = 2;

    /**
     * 设置需要设置圆角的源图片, 并进行缩放
     * =>param bitmap 源图片
     * =>param isInvalidate 是否通知View组件重绘
     */
    public void setBitmap(Bitmap bitmap, boolean isInvalidate){
        //创建一个矩阵
        Matrix m = new Matrix();
        //设置bitmap缩放;当小于0时,bitmap缩小;当大于1时,方法
        m.setScale(width/mBitmap.getWidth(), height/mBitmap.getHeight());
        //产生新的位图
        this.mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), m, true);
        //判断是否通知view重绘
        if(isInvalidate){
            invalidate();
        }
    }

    public RoundCanvasView(Context context) {
        this(context, null);
    }

    public RoundCanvasView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundCanvasView(Context context, AttributeSet attrs,
                           int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //初始化
        init(context, attrs);

    }

    /***
     * 初始化操作
     */
    private void init(Context context, AttributeSet attrs) {
        //初始化画笔
        mPaint = new  Paint();
        //初始化颜色
        mPaint.setColor(Color.BLACK);
        //抗锯齿
        mPaint.setAntiAlias(true);
        //获得密度
        displayMetrics = getResources().getDisplayMetrics();

        if(attrs != null){
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RoundAngleImage);

            //在构造方法中,调用getWidth()和getHeight()方法,获取到的宽高都为0;
            //getWidth()和getHeight()方法在OnDraw方法中,都能正确返回;
            //所以把一部分初始化操作放到OnDraw方法中,比如获取ImageView的宽高.

            //得到ImageView的宽度
//			width = array.getDimension(R.styleable.RoundAngleImage_roundWidth, 80);
//			Log.i(TAG, "width = "+width);
//			width = getWidth();
//			Log.i(TAG, "getWidth() = "+getWidth());
//			width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width, displayMetrics);
            //得到ImageView的高度
//			height = array.getDimension(R.styleable.RoundAngleImage_roundHeight, 80);
//			Log.i(TAG, "height = "+height);
//			height = getHeight();
//			Log.i(TAG, "getHeight() = "+getHeight());
//			height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, height, displayMetrics);
//			Log.i(TAG, "height = "+height);
            //得到圆角弧度
            roundRadius = array.getInteger(R.styleable.RoundAngleImage_roundRadius, 40);
            Log.i(TAG, "roundRadius = "+roundRadius);
//			roundRadius = 40;
            //得到边框颜色
            borderColor = array.getColor(R.styleable.RoundAngleImage_roundBorderColor, Color.RED);
            Log.i(TAG, "borderColor = "+borderColor);
//			borderColor = Color.RED;
            //得到边框宽度
            borderWidth = array.getInteger(R.styleable.RoundAngleImage_roundBorderWidth, 2);
            Log.i(TAG, "borderWidth = "+borderWidth);
//			borderWidth = 2;
            //获得Bitmap位图
            Drawable drawable  = array.getDrawable(R.styleable.RoundAngleImage_roundImageSrc);
            mBitmap = ((BitmapDrawable)drawable).getBitmap();
//			mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_head_2).copy(Bitmap.Config.ARGB_8888, true);
            Log.i(TAG, "mBitmap = "+mBitmap.toString());
            //回收
            array.recycle();
        }else{
            //得到ImageView的宽度
//			width = 80;
//			width = getWidth();
//			width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width, displayMetrics);
            //得到ImageView的高度
//			height = 80;
//			height = getHeight();
//			height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, height, displayMetrics);
            //得到圆角弧度
            roundRadius = 40;
            //得到边框颜色
            borderColor = Color.RED;
            //得到边框宽度
            borderWidth = 2;
            //获得Bitmap位图
            mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.default_userhead).copy(Bitmap.Config.ARGB_8888, true);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//		Log.i(TAG, "getWidth() = "+getWidth());
//		Log.i(TAG, "getHeight() = "+getHeight());
        //得到ImageView的宽度
        width = getWidth();
        //得到ImageView的高度
        height = getHeight();
        //创建一个矩形
        rect = new Rect(0, 0, (int)width, (int)height);
        //通过上面创建的矩形,生成一个圆角矩形
        rectF = new RectF(rect);
        //将获得的位图进行处理
        setBitmap(mBitmap, false);


        //获得圆角的Bitmap位图
        Bitmap roundBitmap = getCroppedBitmap(mBitmap);
        //将圆角位图绘制到Canvas上面
        canvas.drawBitmap(roundBitmap, 0, 0, null);

        //回收bitmap;
        //使用bitmap.recycle()方法,可能会报java.lang.RuntimeException: Canvas: trying to use a recycled bitmap android.graphics.Bitmap@420e97f0异常
        //改为下面这种方式就不会出现这样的问题了
        if(roundBitmap != null && !roundBitmap.isRecycled()){
            roundBitmap = null;
        }
        if(mBitmap != null && !mBitmap.isRecycled()){
            mBitmap = null;
        }

        //创建边框RectF
        RectF borderRectF = getBorderLineRectF(canvas);
        //创建边框画笔
        Paint borderPaint = getBorderPaint(borderColor, borderWidth);
        //绘制边框
        canvas.drawRoundRect(borderRectF, roundRadius, roundRadius, borderPaint);
    }

    /***
     * 利用Bitmap进行缓冲,将圆角图片绘制到bitmap中
     * =>param bmp 源位图
     * =>return 圆角的Bitmap图片
     */
    private Bitmap getCroppedBitmap(Bitmap bmp){
        //通过ImageView的大小生成位图
        Bitmap output = Bitmap.createBitmap((int)width, (int)height, Bitmap.Config.ARGB_8888);
        //创建一个画布,将位图传入,表示在此位图上面绘制
        Canvas canvas2 = new Canvas(output);
        //相当于清屏的作用
        canvas2.drawARGB(0, 0, 0, 0);
        //绘制圆角的矩形
        canvas2.drawRoundRect(rectF, roundRadius, roundRadius, mPaint);
        //设置画笔的Xfermode属性SRC_IN
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        //将位图绘制到矩形上面
        canvas2.drawBitmap(bmp, rect, rect, mPaint);
        //返回,通过设置Xfermode,最后得到的是圆角的Bitmap位图
        return output;
    }


    /***
     * ImageView边框的RectF
     * =>param canvas 画布

     * =>return
     */
    private RectF getBorderLineRectF(Canvas canvas){
        //通过画布得到ImageView所占用的矩形区域
        Rect borderRect = canvas.getClipBounds();
        //设置边框的宽度
        borderRect.left++;
        borderRect.top++;
        borderRect.right--;
        borderRect.bottom--;
        //创建圆角矩形
        RectF borderRectF = new RectF(borderRect);
        return borderRectF;
    }


    /**
     * ImageView边框的Paint
     * =>return 返回Paint
     */
    private Paint getBorderPaint(int borderColor, int borderWidth){
        //画笔
        Paint borderPaint = new Paint();
        //颜色
        borderPaint.setColor(borderColor);
        //style属性
        borderPaint.setStyle(Paint.Style.STROKE);
        //画笔的宽度
        borderPaint.setStrokeWidth(borderWidth);
        return borderPaint;
    }




}