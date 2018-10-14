package jdjt.com.homepager.decoration;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.vondear.rxtool.RxImageTool;

import java.util.List;

import jdjt.com.homepager.domain.PinyinItem;

/**
 * Created by xxd on 2018/9/5.
 */

public class PinYinDecoration extends RecyclerView.ItemDecoration {

    private Paint mPaint;
    private int mTitleHeight; // 拼音首字母的头高度
    private int mDivide; // 普通字母分割线
    private List<PinyinItem> dataList;

    public PinYinDecoration(List<PinyinItem> dataList) {
        this.dataList = dataList;
        mTitleHeight = RxImageTool.dp2px(36);
        mDivide = RxImageTool.dp2px(1);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(RxImageTool.dp2px(12.48f));
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        int position = parent.getChildAdapterPosition(view);
        if (position >= 0) {
            PinyinItem item = dataList.get(position);
            if (position == 0) { // 第一行，并且是拼音
                if (item.getType() == 0)
                    outRect.top = mTitleHeight;
            } else {
                PinyinItem lastItem = dataList.get(position - 1); // 上一个条目
                if (item.getType() == 0) {
                    // 上一个条目不是拼音 or 上一个条目拼音首字母不同
                    if (lastItem.getType() != 0 || !lastItem.getLetter().equals(item.getLetter()))
                        outRect.top = mTitleHeight;
                    else
                        outRect.top = mDivide;
                }
            }
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(view);
            if (position >= 0) {
                PinyinItem item = dataList.get(position);
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
                if (position == 0) { // 第一行，并且是拼音
                    if (item.getType() == 0)
                        drawTitle(c, view, layoutParams, position);
                } else {
                    PinyinItem lastItem = dataList.get(position - 1); // 上一个条目
                    // 上一个条目不是拼音 or 上一个条目拼音首字母不同
                    if (lastItem.getType() != 0 || !lastItem.getLetter().equals(item.getLetter()))
                        drawTitle(c, view, layoutParams, position);
                    else{
                        mPaint.setColor(Color.parseColor("#393A3C"));
                        c.drawRect(view.getPaddingStart(), view.getTop(), view.getRight() - view.getPaddingEnd(), view.getTop() + mDivide, mPaint);
                    }
                }
            }
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        View view = parent.getChildAt(0);
        int position = parent.getChildAdapterPosition(view);
        if (position >= 0) {
            PinyinItem item = dataList.get(position);
            if (item.getType() != 0) // 不是字母条目，不需要悬停
                return;
            boolean saveFlag = false;
            if (position + 1 < dataList.size()) { // 非最后一个条目
                PinyinItem nextItem = dataList.get(position + 1);
                if (nextItem.getType() != 0 || !nextItem.getLetter().equals(item.getLetter())) { // 需要位移canvas
                    if (view.getHeight() + view.getTop() < mTitleHeight) {  // 可见高度小于title高度
                        saveFlag = true;
                        c.save();
                        c.translate(0, view.getHeight() + view.getTop() - mTitleHeight);
                    }
                }
            }
            mPaint.setColor(Color.parseColor("#202123"));
            c.drawRect(0, parent.getPaddingTop(), view.getRight(), parent.getPaddingTop() + mTitleHeight, mPaint);
            mPaint.setColor(Color.WHITE);
            Rect mBounds = new Rect();
            mPaint.getTextBounds(item.getLetter(), 0, item.getLetter().length(), mBounds);
            c.drawText(item.getLetter(), view.getPaddingStart(),
                    parent.getPaddingTop() + mTitleHeight - (mTitleHeight / 2 - mBounds.height() / 2), mPaint);
            if (saveFlag)
                c.restore();
        }
    }

    private void drawTitle(Canvas c, View child, RecyclerView.LayoutParams params, int position) {
        mPaint.setColor(Color.parseColor("#202123"));
        c.drawRect(0, child.getTop() - params.topMargin - mTitleHeight, child.getRight(), child.getTop() - params.topMargin, mPaint);
        mPaint.setColor(Color.WHITE);
        Rect mBounds = new Rect();
        mPaint.getTextBounds(dataList.get(position).getLetter(), 0, dataList.get(position).getLetter().length(), mBounds);
        c.drawText(dataList.get(position).getLetter(), child.getPaddingStart(),
                child.getTop() - params.topMargin - (mTitleHeight / 2 - mBounds.height() / 2), mPaint);
    }
}
