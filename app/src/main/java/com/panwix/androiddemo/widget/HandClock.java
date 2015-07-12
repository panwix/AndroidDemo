package com.panwix.androiddemo.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import java.util.Calendar;

/**
 * Created by Panwix on 15/7/12.
 */
public class HandClock extends View implements Runnable{
    private int clockImageResourceId;
    private Bitmap bitmap;
    private float scale;
    private float handCenterWidthScale;
    private float handCenterHeightScale;
    private int minuteHandSize;
    private int hourHandSize;
    private Handler handler = new Handler();
    //设置命名空间
    private final String namespace = "http://com.panwix.mobile";
    @Override
    public void run() {
        //重新绘制View
        invalidate();
        //重新设置0定时器，在60秒后调用run方法
        handler.postDelayed(this, 60*1000);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //根据图像的实际大小等比例设置View的大小
        setMeasuredDimension((int)(bitmap.getWidth()*scale), (int)(bitmap.getHeight()*scale));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        Rect src = new Rect();
        Rect target = new Rect();
        src.left = 0;
        src.top = 0;
        src.right = bitmap.getWidth();
        src.bottom = bitmap.getHeight();
        target.left = 0;
        target.top = 0;
        target.right = (int)(src.right*scale);
        target.bottom = (int)(src.bottom*scale);
        //画表盘图像
        canvas.drawBitmap(bitmap, src, target, paint);
        //计算表盘中心点的横坐标
        float centerX = bitmap.getWidth()*scale*handCenterWidthScale;
        //计算表盘中心点的纵坐标
        float centerY = bitmap.getHeight()*scale*handCenterHeightScale;
        //画表盘中心点
        canvas.drawCircle(centerX, centerY, 5, paint);
        //设置分针为3个像素
        paint.setStrokeWidth(8);
        Calendar calendar = Calendar.getInstance();
        int currentMinute = calendar.get(Calendar.MINUTE);
        int currentHour = calendar.get(Calendar.HOUR);
        //计算时针和分针的角度
        double minuteRadian = Math.toRadians(360-((currentMinute*6)-90)%360);
        double hourRadian = Math.toRadians((360-((currentHour*30)-90))%360 - (30*currentMinute/60));
        //在表盘上画分针
        canvas.drawLine(centerX, centerY, (int)(centerX+minuteHandSize*Math.cos(minuteRadian)),
                (int)(centerY - minuteHandSize*Math.sin(minuteRadian)),paint);
        //设置时针为4个像素
        paint.setStrokeWidth(10);
        //在表盘上画时针
        canvas.drawLine(centerX, centerY, (int)(centerX+hourHandSize*Math.cos(hourRadian)),
                (int)(centerY - hourHandSize*Math.sin(hourRadian)),paint);
    }

    public HandClock(Context context, AttributeSet attrs) {
        super(context, attrs);
        clockImageResourceId = attrs.getAttributeResourceValue(namespace, "clockImageSrc", 0);
        if(clockImageResourceId>0)
            bitmap = BitmapFactory.decodeResource(getResources(), clockImageResourceId);
        scale = attrs.getAttributeFloatValue(namespace, "scale", 1);
        handCenterWidthScale = attrs.getAttributeFloatValue(namespace, "handCenterWidthScale",
                bitmap.getWidth()/2);
        handCenterHeightScale = attrs.getAttributeFloatValue(namespace, "handCenterHeightScale",
                bitmap.getHeight()/2);
        //在读取时针和分针长度后，将其值按图像的缩放比例进行缩放
        minuteHandSize = (int)(attrs.getAttributeIntValue(namespace, "minuteHandSize", 0)*scale);
        hourHandSize = (int)(attrs.getAttributeIntValue(namespace, "hourHandSize", 0)*scale);
        int currentSecond = Calendar.getInstance().get(Calendar.SECOND);
        //当秒针在12点方向时执行run方法
        handler.postDelayed(this, (60-currentSecond)*1000);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //删除回调对象
        handler.removeCallbacks(this);
    }
}
