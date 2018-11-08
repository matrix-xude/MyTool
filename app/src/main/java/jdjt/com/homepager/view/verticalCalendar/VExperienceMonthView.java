package jdjt.com.homepager.view.verticalCalendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.vondear.rxtool.RxImageTool;

import java.util.Calendar;
import java.util.Map;

import jdjt.com.homepager.domain.VExperienceCalendarData;
import jdjt.com.homepager.util.ToastUtil;

/**
 * Created by xxd on 2018/9/13.
 * V客会体验卡的月份图，不能选择当日的3日之内
 */

public class VExperienceMonthView extends MonthView {

    private Paint mDayPaint; // 画日期的画笔
    private Paint mSelectBgPaint; // 选中日期背景画笔
    private Paint mLinePaint;  // 画线条的画笔
    private Paint mExplainPaint;  // 说明文字的画笔

    private float mDivideLine; // 分割线高度
    private float mSpace; // 背景边距

    private float mDayBaseLine; // 日期的基线
    private float mExplainBaseLine; // 说明文字基线

    private int mNotChoiceDay = 3; // 不能选择的日期

    private OnRangeSelectedListener onRangeSelectedListener;
    private VExperienceCalendarData vExperienceCalendarData;
    private Map<String, Boolean> notBookingMap; // 预定日历中，不能预定的日期

    public VExperienceMonthView(Context context) {
        super(context);
    }

    public VExperienceMonthView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setCurrentDate(int year, int month, CalendarDelegate delegate, VExperienceCalendarData vExperienceCalendarData) {
        this.vExperienceCalendarData = vExperienceCalendarData;
        notBookingMap = vExperienceCalendarData.getNotBookingMap();
        super.setCurrentDate(year, month, delegate);
        init();
    }

    public void setOnRangeSelectedListener(OnRangeSelectedListener onRangeSelectedListener) {
        this.onRangeSelectedListener = onRangeSelectedListener;
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
        mExplainBaseLine = mDelegate.getCalendarItemHeight() * 3 / 4 + getBaseLineHeight(mDayPaint);
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
            if (CalendarUtil.getDayCountBetweenBothCalendar(mDelegate.getCurrentDay(), calendarData) < mNotChoiceDay) {
                mDayPaint.setColor(Color.parseColor("#505050")); // 今天后的3天内不能预定
            } else {
                if (notBookingMap != null && notBookingMap.get(calendarData.toString()) != null) { // 后台不能预定的日期
                    mDayPaint.setColor(Color.parseColor("#505050"));
                } else {
                    mDayPaint.setColor(Color.WHITE);
                }
            }
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
        if (CalendarUtil.getDayCountBetweenBothCalendar(mDelegate.getCurrentDay(), calendarData) < mNotChoiceDay) {
            return; // 今天后的3天内不能预定
        }

        if (mDelegate.mCurrentStartDate != null && mDelegate.mCurrentEndDate != null) {  // 已经点击了2个日期,重新选择
            if (notBookingMap != null && notBookingMap.get(calendarData.toString()) != null) { // 后台不能预定的日期
                return;
            }
            mDelegate.mCurrentStartDate = calendarData;
            mDelegate.mCurrentEndDate = null;
            ToastUtil.showToast(getContext(), "请选择离店日期");
            if (onRangeSelectedListener != null) {
                onRangeSelectedListener.onStartSelected(mDelegate.mCurrentStartDate);
            }
        } else if (mDelegate.mCurrentStartDate != null) {  // 已经选择了入店
            int compareTo = calendarData.compareTo(mDelegate.mCurrentStartDate);
            if (compareTo == 0) { // 点击了当前入店日
                return;
            } else if (compareTo < 0) {  // 点击了入店之前的日期
                if (notBookingMap != null && notBookingMap.get(calendarData.toString()) != null) { // 后台不能预定的日期
                    return;
                }
                mDelegate.mCurrentStartDate = calendarData;
            } else { // 点击了入店之后的日期
                int roomCount = vExperienceCalendarData.getRoomCount();
                int dayCount = CalendarUtil.getDayCountBetweenBothCalendar(mDelegate.mCurrentStartDate, calendarData);
                if (roomCount * dayCount > vExperienceCalendarData.getUsefulCodeSize()) {
                    ToastUtil.showToast(getContext(), "核销码不足");
                    return;
                }
                if (notBookingMap != null) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(CalendarUtil.getDate("yyyy-MM-dd", mDelegate.mCurrentStartDate.toString()));
                    for (int i = dayCount - 1; i > 0; i--) {
                        calendar.add(Calendar.DAY_OF_MONTH, 1);
                        String stringDate = CalendarUtil.getStringDate(calendar);
                        if (notBookingMap.get(stringDate) != null) {  // [开始、结束)日期中间不能包含不能选的日期
                            ToastUtil.showToast(getContext(), "所选入离日期中包含无法预定日期");
                            return;
                        }
                    }
                }
                mDelegate.mCurrentEndDate = calendarData;
                if (onRangeSelectedListener != null) {
                    onRangeSelectedListener.onRangeSelected(mDelegate.mCurrentStartDate, mDelegate.mCurrentEndDate);
                }
            }
        } else {  // 没有选择
            if (notBookingMap != null && notBookingMap.get(calendarData.toString()) != null) { // 后台不能预定的日期
                return;
            }
            mDelegate.mCurrentStartDate = calendarData;
            if (onRangeSelectedListener != null) {
                onRangeSelectedListener.onStartSelected(mDelegate.mCurrentStartDate);
            }
            ToastUtil.showToast(getContext(), "请选择离店日期");
        }

        invalidate();
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

    public interface OnRangeSelectedListener {
        // 选择了一个范围
        void onRangeSelected(CalendarData start, CalendarData end);

        // 选择了入住时间，（第一次选择，选了2个时间后再次选择）
        void onStartSelected(CalendarData start);
    }

}
