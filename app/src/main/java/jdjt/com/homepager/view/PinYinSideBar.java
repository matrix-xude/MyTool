package jdjt.com.homepager.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.vondear.rxtool.RxImageTool;

import java.util.ArrayList;
import java.util.List;

import jdjt.com.homepager.R;

/**
 * Created by xxd on 2018/9/21.
 * 拼音侧面的索引控件，宽度、高度、间隔固定
 */

public class PinYinSideBar extends View {
    private float mLetterSpacingExtra;

    private OnTouchLetterListener mOnTouchLetterListener;
    private List<String> dataList;

    private int mFocusIndex;
    private Paint mPaint;
    private Paint mFocusPaint;
    private float mBaseLineHeight;

    public PinYinSideBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public PinYinSideBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PinYinSideBar(Context context) {
        this(context, null);
    }

    public void setOnTouchLetterListener(OnTouchLetterListener onTouchLetterListener) {
        this.mOnTouchLetterListener = onTouchLetterListener;
    }

    public void setDataList(@NonNull List<String> dataList) {
        this.dataList = dataList;
    }

    private void init(Context context, AttributeSet attrs) {
        mOnTouchLetterListener = getDummyListener();
        dataList = new ArrayList<>(0);
        mFocusIndex = -1;

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PinYinSideBar);
        float textSize = typedArray.getDimension(R.styleable.PinYinSideBar_letterSize, RxImageTool.dp2px(11.52f));
        int letterColor = typedArray.getColor(R.styleable.PinYinSideBar_letterColor,
                ContextCompat.getColor(getContext(), android.R.color.white));
        mLetterSpacingExtra = typedArray.getFloat(R.styleable.PinYinSideBar_letterSpacingExtra, 1.3f);
        int focusLetterColor = typedArray.getColor(R.styleable.PinYinSideBar_focusLetterColor,
                ContextCompat.getColor(getContext(), android.R.color.white));
        typedArray.recycle();

        mPaint = new Paint();
        mPaint.setTypeface(Typeface.DEFAULT_BOLD);
        mPaint.setAntiAlias(true);
        mPaint.setColor(letterColor);
        mPaint.setTextSize(textSize);

        mFocusPaint = new Paint();
        mFocusPaint.setTypeface(Typeface.DEFAULT_BOLD);
        mFocusPaint.setAntiAlias(true);
        mFocusPaint.setFakeBoldText(true);
        mFocusPaint.setTextSize(textSize);
        mFocusPaint.setColor(focusLetterColor);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    private int measureWidth(int widthMeasureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = getSuggestedMinWidth();
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    private int getSuggestedMinWidth() {
        String maxLengthTag = "";
        for (String tag : dataList) {
            if (maxLengthTag.length() < tag.length()) {
                maxLengthTag = tag;
            }
        }
        return (int) (mPaint.measureText(maxLengthTag) + 0.5);
    }

    private int measureHeight(int heightMeasureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(heightMeasureSpec);
        int specSize = MeasureSpec.getSize(heightMeasureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            Paint.FontMetrics fm = mPaint.getFontMetrics();
            float singleHeight = fm.bottom - fm.top;
            mBaseLineHeight = fm.bottom * mLetterSpacingExtra;
            result = (int) (dataList.size() * singleHeight * mLetterSpacingExtra);
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int height = getHeight();
        int width = getWidth();
        if (height == 0) {
            return;
        }
        int singleHeight = height / dataList.size();

        for (int i = 0; i < dataList.size(); i++) {
            float xPos = width / 2 - mPaint.measureText(dataList.get(i)) / 2;
            float yPos = singleHeight * (i + 1);
            if (i == mFocusIndex) {
                canvas.drawText(dataList.get(i), xPos, yPos - mBaseLineHeight, mFocusPaint);
            } else {
                canvas.drawText(dataList.get(i), xPos, yPos - mBaseLineHeight, mPaint);
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final float y = event.getY();
        final int formerFocusIndex = mFocusIndex;
        final OnTouchLetterListener listener = mOnTouchLetterListener;
        final int c = calculateOnClickItemNum(y);

        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mFocusIndex = -1;
                invalidate();
                listener.onTouchEnd(dataList.get(c));
                break;
            case MotionEvent.ACTION_DOWN:
                listener.onTouchStart(dataList.get(c));
            default:
                if (formerFocusIndex != c) {
                    if (c >= 0 && c < dataList.size()) {
                        listener.onTouchLetterChanged(dataList.get(c));
                        mFocusIndex = c;
                        invalidate();
                    }
                }
                break;
        }
        return true;
    }

    /**
     * @param yPos
     * @return the corresponding position in list
     */
    private int calculateOnClickItemNum(float yPos) {
        int result;
        result = (int) (yPos / getHeight() * dataList.size());
        if (result >= dataList.size()) {
            result = dataList.size() - 1;
        } else if (result < 0) {
            result = 0;
        }
        return result;
    }

    public interface OnTouchLetterListener {
        void onTouchLetterChanged(String s);

        void onTouchStart(String s);

        void onTouchEnd(String s);
    }

    private OnTouchLetterListener getDummyListener() {
        return new OnTouchLetterListener() {
            @Override
            public void onTouchLetterChanged(String s) {

            }

            @Override
            public void onTouchStart(String s) {

            }

            @Override
            public void onTouchEnd(String s) {

            }
        };
    }

}
