package jdjt.com.homepager.view.verticalCalendar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import jdjt.com.homepager.R;

/**
 * Created by xxd on 2018/9/17.
 */

public class HotelCalendarView extends RelativeLayout {

    private CalendarRecyclerView calendar_recycler_view;

    private final String SIMPLE_FORMAT = "yyyy-MM-dd";

    public HotelCalendarView(Context context) {
        super(context);
    }

    public HotelCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.hotel_calender, this, true);
        calendar_recycler_view = findViewById(R.id.calendar_recycler_view);
    }

    // 显示默认的今年到明年一个月
    public void show() {
        show(null);
    }

    public void show(Builder builder) {
        CalendarDelegate delegate = new CalendarDelegate();
        if (builder != null) {
            if (builder.preStartDate != null && builder.preEndDate != null) {
                delegate.mPreStartDate = CalendarUtil.getCalendarData(builder.preStartDate);
                delegate.mPreEndDate = CalendarUtil.getCalendarData(builder.preEndDate);
            }
            if (builder.minDate != null && builder.maxDate != null) {
                CalendarData minData = CalendarUtil.getCalendarData(builder.minDate);
                CalendarData maxData = CalendarUtil.getCalendarData(builder.maxDate);
                delegate.setRange(minData.getYear(), minData.getMonth(), minData.getDay(),
                        maxData.getYear(), maxData.getMonth(), maxData.getDay());
            }
        }
        calendar_recycler_view.setUp(delegate);
    }

    private SelectedListener selectedListener;

    public void setSelectedListener(final SelectedListener selectedListener) {
        this.selectedListener = selectedListener;
        calendar_recycler_view.setOnRangeSelectedListener(new CalendarRecyclerView.OnRangeSelectedListener() {
            @Override
            public void onRangeSelected(CalendarData start, CalendarData end) {
                if (selectedListener != null) {
                    selectedListener.selected(start, end);
                }
            }
        });
    }

    public interface SelectedListener {
        void selected(CalendarData start, CalendarData end);
    }

    public static class Builder {
        String minDate;
        String maxDate;
        String preStartDate;
        String preEndDate;

        /**
         * 以下必须全部标准格式 "yyyy-MM-dd"
         *
         * @param minDate
         * @return
         */
        public Builder minDate(String minDate) {
            this.minDate = minDate;
            return this;
        }

        public Builder preStartDate(String preStartDate) {
            this.preStartDate = preStartDate;
            return this;
        }

        public Builder preEndDate(String preEndDate) {
            this.preEndDate = preEndDate;
            return this;
        }

        public Builder maxDate(String maxDate) {
            this.maxDate = maxDate;
            return this;
        }
    }

}
