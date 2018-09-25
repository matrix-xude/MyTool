package jdjt.com.homepager.adapter;

import android.view.View;
import android.widget.TextView;

import com.taobao.library.BaseBannerAdapter;
import com.taobao.library.VerticalBannerView;
import com.vondear.rxtool.view.RxToast;

import java.util.List;

import jdjt.com.homepager.R;
import jdjt.com.homepager.domain.HomeVerticalBannerBean;
import jdjt.com.homepager.domain.SimpleString;

/**
 * Created by xxd on 2018/9/5.
 */

public class VerticalBannerAdapter extends BaseBannerAdapter<HomeVerticalBannerBean> {

    public VerticalBannerAdapter(List<HomeVerticalBannerBean> listData) {
        super(listData);
    }

    @Override
    public View getView(VerticalBannerView verticalBannerView) {
        return View.inflate(verticalBannerView.getContext(), R.layout.item_home_vertical_banner, null);
    }

    @Override
    public void setItem(View view, final HomeVerticalBannerBean bean) {
        TextView tv1 = view.findViewById(R.id.tv_item_home_vertical_banner_1);
        TextView tv2 = view.findViewById(R.id.tv_item_home_vertical_banner_2);
        tv1.setText(bean.getName1());
        tv2.setText(bean.getName2());
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxToast.normal(bean.getName1());
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxToast.normal(bean.getName2());
            }
        });
    }
}
