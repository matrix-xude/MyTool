package jdjt.com.homepager.domain.back;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xxd on 2018/9/28.
 */

public class BackBaseList<T> implements Serializable {

    private String state;
    private String code;
    private String msg;
    private List<T> data;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
