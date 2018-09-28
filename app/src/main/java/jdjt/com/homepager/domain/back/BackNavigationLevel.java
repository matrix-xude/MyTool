package jdjt.com.homepager.domain.back;

/**
 * Created by xxd on 2018/9/28.
 * 首页最上面的导航
 */

public class BackNavigationLevel {

    private String paramTitle;  //分类title（显示用）
    private String paramCode;  //关联分类code,上一级的
    private String paramName;  //关联分类名称,上一级的
    private String seq;  //顺序

    public String getParamTitle() {
        return paramTitle;
    }

    public void setParamTitle(String paramTitle) {
        this.paramTitle = paramTitle;
    }

    public String getParamCode() {
        return paramCode;
    }

    public void setParamCode(String paramCode) {
        this.paramCode = paramCode;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }
}
