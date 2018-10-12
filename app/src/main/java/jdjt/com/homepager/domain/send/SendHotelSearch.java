package jdjt.com.homepager.domain.send;

import java.util.List;

/**
 * Created by xxd on 2018/10/12.
 * 酒店条件查询的入参
 */

public class SendHotelSearch {

    private String pageNo;
    private String pageCount;
    private String serchKey;
    private String startDate;
    private String endDate;
    private List<SendHotelSearchLevel1> paramTypeList;

    public String getPageNo() {
        return pageNo;
    }

    public void setPageNo(String pageNo) {
        this.pageNo = pageNo;
    }

    public String getPageCount() {
        return pageCount;
    }

    public void setPageCount(String pageCount) {
        this.pageCount = pageCount;
    }

    public String getSerchKey() {
        return serchKey;
    }

    public void setSerchKey(String serchKey) {
        this.serchKey = serchKey;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public List<SendHotelSearchLevel1> getParamTypeList() {
        return paramTypeList;
    }

    public void setParamTypeList(List<SendHotelSearchLevel1> paramTypeList) {
        this.paramTypeList = paramTypeList;
    }
}
