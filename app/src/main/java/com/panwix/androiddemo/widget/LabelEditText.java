package com.panwix.androiddemo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.panwix.androiddemo.R;

/**
 * Created by Panwix on 15/7/11.
 */
public class LabelEditText extends LinearLayout {
    private TextView textView;
    private String labelText;
    private int labelFontSize;
    private String labelPosition;
    private final String namespace = "http://com.panwix.mobile";


    public LabelEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        //读取labelText属性值
        int resourceId = attrs.getAttributeResourceValue(namespace, "labelText", 0);

        //resourceId为0，可能时字符串
        if(resourceId == 0)
            labelText = attrs.getAttributeValue(namespace, "labelText");
        else
            labelText = getResources().getString(resourceId);

        //未取到labelText属性值，则抛出异常
        if(labelText == null)
            throw new RuntimeException("必须设置labelText属性值");

        //获取labelFontSize属性值
        resourceId = attrs.getAttributeResourceValue(namespace, "labelFontSize", 0);
        if(resourceId == 0)
            labelFontSize = attrs.getAttributeIntValue(namespace, "labelFontSize", 14);
        else
            labelFontSize = getResources().getInteger(resourceId);

        //获取labelPosition属性值
        resourceId = attrs.getAttributeResourceValue(namespace, "labelPosition", 0);
        if(resourceId == 0)
            labelPosition = attrs.getAttributeValue(namespace, "labelPosition");
        else
            labelPosition = getResources().getString(resourceId);
        if(labelPosition == null)
            labelPosition = "left";

        //获得LAYOUT_INFLATER_SERVIC服务
        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) context.getSystemService(infService);

        if("left".equals(labelPosition))
            li.inflate(R.layout.labeledittext_horizontal, this);
        else if("top".equals(labelPosition))
            li.inflate(R.layout.labeledittext_vertical, this);
        else
            throw new RuntimeException("labelPosition属性只有left或top");

        textView = (TextView)findViewById(R.id.textview);
        textView.setTextSize((float)labelFontSize);
        textView.setText(labelText);
    }
}
