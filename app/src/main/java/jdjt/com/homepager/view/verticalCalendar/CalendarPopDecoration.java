package jdjt.com.homepager.view.verticalCalendar;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.vondear.rxtool.RxImageTool;

import java.util.List;

/**
 * Created by xxd on 2018/9/5.
 */

public class CalendarPopDecoration extends RecyclerView.ItemDecoration {

    private Paint mPaint;
    private CalendarDelegate delegate;
    private List<CalendarRecyclerView.MonthBean> dataList;

    private String text;
    private final String HINT = "请选择离店日期";

    public CalendarPopDecoration(CalendarDelegate delegate, List<CalendarRecyclerView.MonthBean> dataList, int color) {
        this.delegate = delegate;
        this.dataList = dataList;

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextSize(RxImageTool.dp2px(13.44f));
        mPaint.setColor(color);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);

        if (delegate.mCurrentStartDate == null) // 没有入店日期
            return;

        CalendarData targetData;
        if (delegate.mCurrentEndDate != null) {
            text = CalendarUtil.getDayCountBetweenBothCalendar(delegate.mCurrentStartDate, delegate.mCurrentEndDate) + "晚";
            targetData = delegate.mCurrentEndDate;
        } else {
            text = HINT;
            targetData = delegate.mCurrentStartDate;
        }

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(view);
            if (targetData.getYear() == dataList.get(position).getYear() &&
                    targetData.getMonth() == dataList.get(position).getMonth()) {  // 同月份

                int index = targetData.getIndex();
                int row = index / 7; // 第几行，第几列，从0开始计算
                int line = index % 7;

                int itemWidth = (view.getRight() - view.getLeft()) / 7; // 每个item宽度
                int itemHeight = delegate.getCalendarItemHeight(); // 每个item高度
                int headHeight = RxImageTool.dp2px(30); // 年月日高度

                float centerX = itemWidth * (line + 0.5f);
                float centerY = view.getTop() + headHeight + itemHeight * row;

                float arrowWidth = RxImageTool.dp2px(10); // px
                float arrowHeight = RxImageTool.dp2px(15); //px

                mPaint.setColor(Color.parseColor("#6c6d6e"));
                Path path = new Path();  // 画箭头
                path.moveTo(centerX, centerY);
                path.rLineTo(arrowWidth / 2, -arrowHeight);
                path.rLineTo(-arrowWidth, 0);
                path.close();
                c.drawPath(path, mPaint);

                float textHeight = RxImageTool.dp2px(25);
                float textWidth = mPaint.measureText(text);
                float textPadding = RxImageTool.dp2px(10); // 文字左右的边距

                float baseLineHeight = getBaseLineHeight(mPaint);
                // 画文字框
                if (textWidth / 2 + textPadding > centerX) {  // 左边顶住了边框
                    c.drawRect(0, centerY - arrowHeight - textHeight, textWidth + 2 * textPadding, centerY - arrowHeight, mPaint);
                    mPaint.setColor(Color.WHITE);
                    c.drawText(text, textPadding, centerY - arrowHeight - textHeight / 2 + baseLineHeight, mPaint);
                } else if (textWidth / 2 + textPadding > (view.getRight() - centerX)) {  // 右边顶住了框
                    c.drawRect(view.getRight() - textWidth - 2 * textPadding, centerY - arrowHeight - textHeight, view.getRight(), centerY - arrowHeight, mPaint);
                    mPaint.setColor(Color.WHITE);
                    c.drawText(text, view.getRight() - textWidth - textPadding, centerY - arrowHeight - textHeight / 2 + baseLineHeight, mPaint);
                } else {  // 中间
                    c.drawRect(centerX - textWidth / 2 - textPadding, centerY - arrowHeight - textHeight, centerX + textWidth / 2 + textPadding, centerY - arrowHeight, mPaint);
                    mPaint.setColor(Color.WHITE);
                    c.drawText(text, centerX - textWidth / 2, centerY - arrowHeight - textHeight / 2 + baseLineHeight, mPaint);
                }

            }
        }
    }

    private float getBaseLineHeight(Paint paint) {
        Paint.FontMetrics metrics = paint.getFontMetrics();
        return (metrics.descent - metrics.ascent) / 2 - metrics.descent;
    }
}
