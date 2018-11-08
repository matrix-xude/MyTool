package jdjt.com.homepager.view.verticalCalendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;

/**
 * Created by xxd on 2018/9/13.
 */

public abstract class MonthView extends View implements View.OnClickListener {

    /**
     * 配置文件
     */
    CalendarDelegate mDelegate;

    /**
     * 日历项
     */
    List<CalendarData> mItems;

    /**
     * 当前日历卡年份
     */
    private int mYear;

    /**
     * 当前日历卡月份
     */
    private int mMonth;


    /**
     * 日历的行数
     */
    private int mLineCount;

    /**
     * 日历高度
     */
    private int mHeight;

    /**
     * 每一项的高度
     */
    protected int mItemHeight;

    /**
     * 每一项的宽度
     */
    protected int mItemWidth;


    /**
     * 点击的x、y坐标
     */
    float mX, mY;

    /**
     * 是否点击
     */
    boolean isClick = true;

    public MonthView(Context context) {
        this(context, null);
    }

    public MonthView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mLineCount == 0)
            return;
        onPreDraw(canvas);
        mItemWidth = getWidth() / 7;
        int d = 0;
        for (int i = 0; i < mLineCount; i++) {
            for (int j = 0; j < 7; j++) {
                CalendarData calendar = mItems.get(d);
                draw(canvas, calendar, i, j, d);
                ++d;
            }
        }
    }

    protected void onPreDraw(Canvas canvas) {

    }

    /**
     * 开始绘制
     *
     * @param canvas   canvas
     * @param calendar 对应日历
     * @param i        i
     * @param j        j
     * @param d        d
     */
    private void draw(Canvas canvas, CalendarData calendar, int i, int j, int d) {
        int x = j * mItemWidth;
        int y = i * mItemHeight;
        onDrawText(canvas, calendar, x, y);
    }

    /**
     * 初始化日期
     *
     * @param year  year
     * @param month month
     */
    public void setCurrentDate(int year, int month, CalendarDelegate delegate) {
        mYear = year;
        mMonth = month;
        mDelegate = delegate;
        mItemHeight = mDelegate.getCalendarItemHeight();
        mHeight = CalendarUtil.getMonthViewHeight(year, month, mItemHeight, mDelegate.getWeekStart());
        initCalendar();
    }

    /**
     * 初始化日历
     */
    @SuppressLint("WrongConstant")
    private void initCalendar() {

        int nextDiff = CalendarUtil.getMonthEndDiff(mYear, mMonth, mDelegate.getWeekStart());
        int preDiff = CalendarUtil.getMonthViewStartDiff(mYear, mMonth, mDelegate.getWeekStart());
        int monthDayCount = CalendarUtil.getMonthDaysCount(mYear, mMonth);

        mLineCount = (preDiff + monthDayCount + nextDiff) / 7;
        mItems = CalendarUtil.initCalendarForMonthView(mYear, mMonth, mDelegate.getCurrentDay(), mDelegate.getWeekStart());

        setOnClickListener(this);
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mLineCount != 0) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(mHeight, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public int getLineCount() {
        return mLineCount;
    }

    public int getItemWidth() {
        return mItemWidth;
    }

    public int getItemHeight() {
        return mItemHeight;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getPointerCount() > 1)
            return false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mX = event.getX();
                mY = event.getY();
                isClick = true;
                break;
            case MotionEvent.ACTION_MOVE:
                float mDY;
                if (isClick) {
                    mDY = event.getY() - mY;
                    isClick = Math.abs(mDY) <= 50;
                }
                break;
            case MotionEvent.ACTION_UP:
                mX = event.getX();
                mY = event.getY();
                break;
        }
        return super.onTouchEvent(event);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onClick(View v) {
        if (!isClick) {
            return;
        }
        CalendarData calendarData = getIndex();
        if (calendarData == null) {
            return;
        }
        onCalendarClick(calendarData);
    }

    /**
     * 获取点击选中的日期
     *
     * @return return
     */
    protected CalendarData getIndex() {
        int indexX = (int) mX / mItemWidth;
        if (indexX >= 7) {
            indexX = 6;
        }
        int indexY = (int) mY / mItemHeight;
        int position = indexY * 7 + indexX;// 选择项
        if (position >= 0 && position < mItems.size())
            return mItems.get(position);
        return null;
    }

    /**
     * 绘制日历文本
     *
     * @param canvas       canvas
     * @param calendarData 日历calendar
     * @param x            日历Card x起点坐标
     * @param y            日历Card y起点坐标
     */
    protected abstract void onDrawText(Canvas canvas, CalendarData calendarData, int x, int y);

    /**
     * 点击日历事件
     *
     * @param calendarData
     */
    protected abstract void onCalendarClick(CalendarData calendarData);
}
