package jdjt.com.homepager.view.verticalCalendar;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import jdjt.com.homepager.R;
import jdjt.com.homepager.decoration.CalendarPopDecoration;
import jdjt.com.homepager.view.commonRecyclerView.CommonAdapterRecycler;
import jdjt.com.homepager.view.commonRecyclerView.CommonViewHolderRecycler;

/**
 * Created by xxd on 2018/9/14.
 * 垂直滚动的日历控件
 */

public class CalendarRecyclerView extends RecyclerView {

    private int mMonthCount;  // 总共展示的月份

    private CalendarDelegate mDelegate;

    public CalendarRecyclerView(Context context) {
        super(context);
    }

    public CalendarRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 初始化
     *
     * @param delegate
     */
    public void setUp(CalendarDelegate delegate) {
        this.mDelegate = delegate;
        init();
    }

    private void init() {
        mMonthCount = 12 * (mDelegate.getMaxYear() - mDelegate.getMinYear())
                - mDelegate.getMinYearMonth() + 1 + mDelegate.getMaxYearMonth();
        List<MonthBean> dataList = getDataList();

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        setLayoutManager(layoutManager);
        setAdapter(new CommonAdapterRecycler<MonthBean>(getContext(), R.layout.item_calendar_recycler, dataList) {
            @Override
            public void convert(CommonViewHolderRecycler holder, MonthBean monthBean, int position) {
                holder.setText(R.id.tv_item_calendar_recycler_month, monthBean.getYear() + "年" + monthBean.getMonth() + "月");
                HotelMonthView hotelMonthView = holder.getView(R.id.hotel_month_view_calendar_recycler);
                hotelMonthView.setCurrentDate(monthBean.getYear(), monthBean.getMonth(), monthBean.getDelegate());
                int lineCount = hotelMonthView.getLineCount();
                int itemHeight = hotelMonthView.getItemHeight();
                ViewGroup.LayoutParams layoutParams = hotelMonthView.getLayoutParams();
                layoutParams.height = lineCount * itemHeight;
                hotelMonthView.setLayoutParams(layoutParams);
                hotelMonthView.setOnCalendarOnClickListener(new HotelMonthView.OnCalendarOnClickListener() {
                    @Override
                    public void onCalendarClick(CalendarData calendarData) {
                        if (mDelegate.mCurrentStartDate != null && mDelegate.mCurrentEndDate != null) {  // 已经点击了2个日期
                            return;
                        } else if (mDelegate.mCurrentStartDate != null) {  // 已经选择了入店
                            int compareTo = calendarData.compareTo(mDelegate.mCurrentStartDate);
                            if (compareTo == 0) { // 点击了当前入店日
                                return;
                            } else if (compareTo < 0) {  // 点击了入店之前的日期
                                mDelegate.mCurrentStartDate = calendarData;
                            } else { // 点击了入店之后的日期
                                mDelegate.mCurrentEndDate = calendarData;
                                if(onRangeSelectedListener != null){
                                    onRangeSelectedListener.onRangeSelected(mDelegate.mCurrentStartDate,mDelegate.mCurrentEndDate);
                                }
                            }
                        } else {  // 没有选择
                            mDelegate.mCurrentStartDate = calendarData;
                        }
                        notifyDataSetChanged();
                    }
                });
            }
        });
        addItemDecoration(new CalendarPopDecoration(mDelegate, dataList, Color.BLUE));

    }

    private OnRangeSelectedListener onRangeSelectedListener;

    public void setOnRangeSelectedListener(OnRangeSelectedListener onRangeSelectedListener) {
        this.onRangeSelectedListener = onRangeSelectedListener;
    }

    interface OnRangeSelectedListener {
        void onRangeSelected(CalendarData start, CalendarData end);
    }

    private List<MonthBean> getDataList() {
        int year = mDelegate.getMinYear();
        int month = mDelegate.getMinYearMonth();
        List<MonthBean> list = new ArrayList<>();
        for (int i = 0; i < mMonthCount; i++) {
            if (month > 12) {
                year++;
                month -= 12;
            }
            MonthBean bean = new MonthBean();
            bean.setYear(year);
            bean.setMonth(month);
            bean.setDelegate(mDelegate);
            list.add(bean);
            month++;
        }
        return list;
    }

    public class MonthBean {
        private int year;
        private int month;
        private CalendarDelegate delegate;

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public int getMonth() {
            return month;
        }

        public void setMonth(int month) {
            this.month = month;
        }

        public CalendarDelegate getDelegate() {
            return delegate;
        }

        public void setDelegate(CalendarDelegate delegate) {
            this.delegate = delegate;
        }
    }


}
