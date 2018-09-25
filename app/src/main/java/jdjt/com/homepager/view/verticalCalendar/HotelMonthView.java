package jdjt.com.homepager.view.verticalCalendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.vondear.rxtool.RxImageTool;

/**
 * Created by xxd on 2018/9/13.
 * 酒店日历卡的实现类
 */

public class HotelMonthView extends MonthView {

    private Paint mDayPaint; // 画日期的画笔
    private Paint mSelectBgPaint; // 选中日期背景画笔
    private Paint mLinePaint;  // 画线条的画笔
    private Paint mExplainPaint;  // 说明文字的画笔

    private float mDivideLine; // 分割线高度
    private float mSpace; // 背景边距

    private float mDayBaseLine; // 日期的基线
    private float mExplainBaseLine; // 说明文字基线

    private OnCalendarOnClickListener onCalendarOnClickListener;

    public HotelMonthView(Context context) {
        super(context);
    }

    public HotelMonthView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setOnCalendarOnClickListener(OnCalendarOnClickListener onCalendarOnClickListener) {
        this.onCalendarOnClickListener = onCalendarOnClickListener;
    }

    @Override
    public void setCurrentDate(int year, int month, CalendarDelegate delegate) {
        super.setCurrentDate(year, month, delegate);
        init();
    }

    private void init() {
        mDivideLine = RxImageTool.dp2px(0.48f);
        mSpace = 5;

        mDayPaint = new Paint();
        mDayPaint.setAntiAlias(true);
        mDayPaint.setColor(Color.WHITE);
        mDayPaint.setTextSize(RxImageTool.dp2px(15.36f));

        mSelectBgPaint = new Paint();
        mSelectBgPaint.setAntiAlias(true);
        mSelectBgPaint.setColor(Color.parseColor("#F39800"));

        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setColor(Color.parseColor("#393A3C"));

        mExplainPaint = new Paint();
        mExplainPaint.setAntiAlias(true);
        mExplainPaint.setColor(Color.WHITE);
        mExplainPaint.setTextSize(RxImageTool.dp2px(13.44f));

        mDayBaseLine = mDelegate.getCalendarItemHeight() / 2 + getBaseLineHeight(mDayPaint);
        mExplainBaseLine = mDelegate.getCalendarItemHeight() * 5 / 6 + getBaseLineHeight(mDayPaint);
    }

    /**
     * 获取字体的宽
     *
     * @param text text
     * @return return
     */
    private float getTextWidth(Paint paint, String text) {
        return paint.measureText(text);
    }

    /**
     * 获取画笔的基线
     *
     * @param paint
     * @return
     */
    private float getBaseLineHeight(Paint paint) {
        Paint.FontMetrics metrics = paint.getFontMetrics();
        return (metrics.descent - metrics.ascent) / 2 - metrics.descent;
    }

    @Override
    protected void onDrawText(Canvas canvas, CalendarData calendarData, int x, int y) {

        if (!calendarData.isCurrentMonth()) // 不是当前月
            return;

        int itemWidth = getItemWidth();

        boolean currentSelected = mDelegate.mCurrentStartDate != null; // 是否已经选择了
        boolean preSelected = mDelegate.mPreStartDate != null && mDelegate.mPreEndDate != null; // 之前是否已经选择了

        // 画背景
        if (mDelegate.mCurrentStartDate != null && mDelegate.mCurrentEndDate != null) {  // 已经点击了2个日期
            if (CalendarUtil.isCalendarInRange(calendarData, mDelegate.mCurrentStartDate, mDelegate.mCurrentEndDate)) {  // 当前日期在范围选择在范围内的
                if (calendarData.compareTo(mDelegate.mCurrentStartDate) == 0 || calendarData.compareTo(mDelegate.mCurrentEndDate) == 0) {
                    mSelectBgPaint.setColor(Color.parseColor("#F39800"));
                } else {
                    mSelectBgPaint.setColor(Color.parseColor("#A8A8A8"));
                }
                canvas.drawRect(x + mSpace, y + mSpace, x + itemWidth - mSpace, y + getItemHeight() - mSpace, mSelectBgPaint);
            }
        } else if (mDelegate.mCurrentStartDate != null) {  // 已经选择了入店
            if (calendarData.equals(mDelegate.mCurrentStartDate)) {
                mSelectBgPaint.setColor(Color.parseColor("#F39800"));
                canvas.drawRect(x + mSpace, y + mSpace, x + itemWidth - mSpace, y + getItemHeight() - mSpace, mSelectBgPaint);
            }
        } else {  // 没有选择，看看有没有预选的背景
            if (preSelected) {
                if (CalendarUtil.isCalendarInRange(calendarData, mDelegate.mPreStartDate, mDelegate.mPreEndDate)) { // 范围内
                    if (calendarData.compareTo(mDelegate.mPreStartDate) == 0 || calendarData.compareTo(mDelegate.mPreEndDate) == 0) {
                        mSelectBgPaint.setColor(Color.parseColor("#F39800"));
                    } else {
                        mSelectBgPaint.setColor(Color.parseColor("#A8A8A8"));
                    }
                    canvas.drawRect(x + mSpace, y + mSpace, x + itemWidth - mSpace, y + getItemHeight() - mSpace, mSelectBgPaint);
                }
            }
        }

        // 画日期
        if (CalendarUtil.isCalendarInRange(calendarData, mDelegate) && calendarData.compareTo(mDelegate.getCurrentDay()) >= 0) {
            mDayPaint.setColor(Color.WHITE);
        } else {
            mDayPaint.setColor(Color.parseColor("#505050"));
        }
        String dayText;
        if (calendarData.equals(mDelegate.getCurrentDay()))
            dayText = "今天";
        else
            dayText = calendarData.getDay() + "";
        canvas.drawText(dayText, x + (itemWidth - getTextWidth(mDayPaint, dayText)) / 2, y + mDayBaseLine, mDayPaint);

        // 画说明文字
        if (currentSelected) {  // 如果当前已经选择过
            if (mDelegate.mCurrentStartDate != null && calendarData.equals(mDelegate.mCurrentStartDate)) {
                canvas.drawText("入住", x + (itemWidth - getTextWidth(mExplainPaint, "入住")) / 2, y + mExplainBaseLine, mExplainPaint);
            } else if (mDelegate.mCurrentEndDate != null && calendarData.equals(mDelegate.mCurrentEndDate)) {
                canvas.drawText("离店", x + (itemWidth - getTextWidth(mExplainPaint, "离店")) / 2, y + mExplainBaseLine, mExplainPaint);
            }
        } else if (preSelected) { // 之前选择过
            if (calendarData.equals(mDelegate.mPreStartDate)) {
                canvas.drawText("入住", x + (itemWidth - getTextWidth(mExplainPaint, "入住")) / 2, y + mExplainBaseLine, mExplainPaint);
            } else if (calendarData.equals(mDelegate.mPreEndDate)) {
                canvas.drawText("离店", x + (itemWidth - getTextWidth(mExplainPaint, "离店")) / 2, y + mExplainBaseLine, mExplainPaint);
            }
        }
    }

    @Override
    protected void onCalendarClick(CalendarData calendarData) {
        if (mDelegate == null)
            return;
        if (!calendarData.isCurrentMonth()) // 空白部分
            return;
        if (calendarData.compareTo(mDelegate.getCurrentDay()) < 0) // 点进今天之前
            return;
        if (!CalendarUtil.isCalendarInRange(calendarData, mDelegate)) // 点击不在范围内的
            return;

        if (onCalendarOnClickListener != null) {
            onCalendarOnClickListener.onCalendarClick(calendarData);
        }

    }

    @Override
    protected void onPreDraw(Canvas canvas) {
        super.onPreDraw(canvas);
        for (int i = 1; i < getLineCount(); i++) {
            float top = getItemHeight() * i - mDivideLine / 2;
            float bottom = getItemHeight() * i + mDivideLine / 2;
            float left = 0;
            float right = getWidth();
            canvas.drawRect(left, top, right, bottom, mLinePaint);
        }
    }

    interface OnCalendarOnClickListener {
        void onCalendarClick(CalendarData calendarData);
    }
}
