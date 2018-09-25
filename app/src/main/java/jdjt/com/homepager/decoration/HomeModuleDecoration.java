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

public class HomeModuleDecoration extends RecyclerView.ItemDecoration {

    private int totalRow;  // 总行数
    private int rank; // 总列数
    private int divide; // 间隔,px
    private Paint mPaint;

    public HomeModuleDecoration(int totalRow, int rank, int divideHeight, int color) {
        this.totalRow = totalRow;
        this.rank = rank;
        this.divide = RxImageTool.dp2px(divideHeight);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(color);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        outRect.left = divide;
        int position = parent.getChildAdapterPosition(view);
        int currentRow = position / rank + 1; // 当前行数
        if (currentRow < totalRow) {
            outRect.bottom = divide;
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);

        int count = parent.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = parent.getChildAt(i);

            int left = view.getLeft() - divide;
            int right = view.getLeft();
            int top = view.getTop();
            int bottom = view.getBottom();
            c.drawRect(left, top, right, bottom, mPaint);

            int position = parent.getChildAdapterPosition(view);
            int currentRow = position / rank + 1; // 当前行数
            if (currentRow < totalRow) {
                int left2 = view.getLeft() - divide;
                int right2 = view.getRight();
                int top2 = view.getBottom();
                int bottom2 = view.getBottom() + divide;
                c.drawRect(left2, top2, right2, bottom2, mPaint);
            }
        }
    }
}
