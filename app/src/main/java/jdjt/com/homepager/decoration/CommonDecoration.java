package jdjt.com.homepager.decoration;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.vondear.rxtool.RxImageTool;

import java.util.List;

/**
 * Created by xxd on 2018/9/25.
 */

public class CommonDecoration extends RecyclerView.ItemDecoration {

    private int dividePx; // 间隔，px
    private int lineCount; // 列数
    private Paint mPaint; // 画笔


    public CommonDecoration(int dividePx) {
        this(dividePx, 1, Color.TRANSPARENT);
    }

    public CommonDecoration(int dividePx, int lineCount, int color) {
        this.dividePx = dividePx;
        this.lineCount = lineCount;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(color);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildAdapterPosition(view);
        if (position / lineCount != 0) { // 不是第一行
            outRect.top = dividePx;
        }
        if (position % lineCount != 0) { // 不是第一列
            outRect.left = dividePx;
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(view);
            if (position / lineCount != 0) { // 不是第一行
                int left = view.getLeft();
                int right = view.getRight();
                int top = view.getTop() - dividePx;
                int bottom = view.getTop();
                c.drawRect(left, top, right, bottom, mPaint);
            }
            if (position % lineCount != 0) { // 不是第一列
                int left = view.getLeft() - dividePx;
                int right = view.getLeft();
                int top = view.getTop();
                if (position / lineCount != 0) {
                    top = view.getTop() - dividePx;
                }
                int bottom = view.getBottom();
                c.drawRect(left, top, right, bottom, mPaint);
            }
        }
    }
}
