package jdjt.com.homepager.domain.back;

import java.util.List;

/**
 * Created by xxd on 2018/10/12.
 */

public class BackSearchHotel {

    private String pageNo;
    private String count;
    private List<BackHotel> listHotel;

    public String getPageNo() {
        return pageNo;
    }

    public void setPageNo(String pageNo) {
        this.pageNo = pageNo;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public List<BackHotel> getListHotel() {
        return listHotel;
    }

    public void setListHotel(List<BackHotel> listHotel) {
        this.listHotel = listHotel;
    }
}
