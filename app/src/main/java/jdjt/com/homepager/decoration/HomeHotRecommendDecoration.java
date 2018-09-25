package jdjt.com.homepager.decoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.vondear.rxtool.RxImageTool;

/**
 * Created by xxd on 2018/9/5.
 */

public class HomeHotRecommendDecoration extends RecyclerView.ItemDecoration {

    private int totalRow;  // 总行数
    private int rank; // 总列数
    private int divide; // 间隔,px
//    private Paint mPaint;

    public HomeHotRecommendDecoration(int totalRow, int rank, int divideSpace, int color) {
        this.totalRow = totalRow;
        this.rank = rank;
        this.divide = RxImageTool.dp2px(divideSpace);
//        mPaint = new Paint();
//        mPaint.setAntiAlias(true);
//        mPaint.setColor(color);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        int position = parent.getChildAdapterPosition(view);
        int currentRow = position / rank + 1; // 当前行数
        if (currentRow > 1) { // 非第一行
            outRect.top = divide;
        }
        if (position % rank != 0) { // 非第一列
            outRect.left = divide;
        }
    }

}
