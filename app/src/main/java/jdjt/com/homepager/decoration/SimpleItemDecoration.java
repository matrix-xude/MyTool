package jdjt.com.homepager.decoration;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.vondear.rxtool.RxImageTool;

/**
 * Created by xxd on 2018/9/5.
 */

public class SimpleItemDecoration extends RecyclerView.ItemDecoration {

    private int divide; // 间隔,px
    private Paint mPaint;

    public SimpleItemDecoration(int divideSpace, int color) {
        this.divide = RxImageTool.dp2px(divideSpace);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(color);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        int position = parent.getChildAdapterPosition(view);
        if (position > 0) { // 非第一行
            outRect.top = divide;
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(view);
            if (position > 0) {
                int left = view.getLeft();
                int right = view.getRight();
                int top = view.getTop() - divide;
                int bottom = view.getTop();
                c.drawRect(left, top, right, bottom, mPaint);
            }
        }
    }
}
