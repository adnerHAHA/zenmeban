package com.fkl.music.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adner on 2016/10/15.
 */
public class LrcView extends TextView {
    private float width;
    private float high;
    private Paint currentPaint;
    private Paint NotCurrentPaint;
    private float TextHigh = 50;
    private float TextSize = 30;
    private  int index = 0;
    static List<String> mSendEntities=new ArrayList<String>();

    public  void setSententceEntities(List<String> mSendEntities){
        this.mSendEntities=mSendEntities;
    }
    public LrcView(Context context) {
        super(context);
        init();
    }
    public LrcView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        init();
    }
    public LrcView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        init();
    }


    private void init() {
        // TODO Auto-generated method stub
        setFocusable(true);

        // 高亮部分
        currentPaint = new Paint();
        currentPaint.setAntiAlias(true);
        currentPaint.setTextAlign(Paint.Align.CENTER);

        // 非高亮部分
        NotCurrentPaint = new Paint();
        NotCurrentPaint.setAntiAlias(true);
        NotCurrentPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (canvas == null) {
            return;
        }
        if (index==-1){
            return;
        }

        currentPaint.setColor(Color.argb(248, 251, 248, 100));
        NotCurrentPaint.setColor(Color.argb(140, 255, 255, 255));

        currentPaint.setTextSize(45);
        currentPaint.setTypeface(Typeface.SERIF);

        NotCurrentPaint.setTextSize(TextSize);
        NotCurrentPaint.setTypeface(Typeface.DEFAULT);

        try {
            setText("");
            canvas.drawText(mSendEntities.get(index).toString(), width / 2,
                    high / 2, currentPaint);

            float tempY = high / 2;
            // 画出本句之前的句子
            for (int i = index - 1; i >= 0; i--) {
                // 向上推移
                tempY = tempY - TextHigh;

                canvas.drawText(mSendEntities.get(i).toString(), width / 2,
                        tempY, NotCurrentPaint);
            }
            tempY = high / 2;
            // 画出本句之后的句子
            for (int i = index + 1; i < mSendEntities.size(); i++) {
                // 往下推移
                tempY = tempY + TextHigh;
                canvas.drawText(mSendEntities.get(i).toString(), width / 2,
                        tempY, NotCurrentPaint);
            }
        } catch (Exception e) {
            setText("...木有歌词文件,赶紧去下载...");
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.width = w;
        this.high = h;
    }
    public void SetIndex(int index) {
        this.index = index;
    }
}
