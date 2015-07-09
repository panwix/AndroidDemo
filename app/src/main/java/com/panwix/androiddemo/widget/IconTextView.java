package com.panwix.androiddemo.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Panwix on 15/7/9.
 */
public class IconTextView extends TextView {
    //设置命名空间
    private final String namespace = "http://com.panwix.mobile";

    //保存图像资源ID的变量
    private int resourceId = 0;
    private Bitmap bitmap;

    public IconTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        //获取资源ID的值
        resourceId = attrs.getAttributeResourceValue(namespace, "iconSrc", 0);
        if(resourceId > 0)
            //如果成功获得图像资源ID，装载这个图像资源并创建Bitmap对象
            bitmap = BitmapFactory.decodeResource(getResources(), resourceId);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(bitmap != null){
            //从原图上截取图像的区域
            Rect src = new Rect();

            //将截取的图像复制到bitmap上
            Rect target = new Rect();
            src.left = 0;
            src.top = 0;
            src.right = bitmap.getWidth();
            src.bottom = bitmap.getHeight();

            int textHeight = (int)getTextSize();
            target.left = 0;

            //计算图像绘制到目标区域的纵坐标
            target.top = (int)((getMeasuredHeight() - getTextSize())/2) + 1;
            target.bottom = target.top + textHeight;

            //为了保证图像不变形，根据图像高度重新计算图像的宽度
            target.right = (int)(textHeight*(bitmap.getWidth()/(float)bitmap.getHeight()));

            //开始绘制图像
            canvas.drawBitmap(bitmap, src, target, getPaint());

            //将TextView中的文本向右移动一定距离
            canvas.translate(target.right + 2, 0);

        }
        super.onDraw(canvas);
    }
}
