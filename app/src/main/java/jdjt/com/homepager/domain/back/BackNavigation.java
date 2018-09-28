package jdjt.com.homepager.domain.back;

import java.util.List;

/**
 * Created by xxd on 2018/9/28.
 * 首页最上面的导航
 */

public class BackNavigation {

    private String navigationTypeId;  //导航分类Id
    private String navigationTypeName;  //导航分类名称
    private String backgroundType;  //背景类型（1-背景；2-色值）
    private String typeContent;  //背景信息（图片地址或者色值）
    private String seq;  //顺序
    private List<BackNavigationLevel> navigationParams;

    public String getNavigationTypeId() {
        return navigationTypeId;
    }

    public void setNavigationTypeId(String navigationTypeId) {
        this.navigationTypeId = navigationTypeId;
    }

    public String getNavigationTypeName() {
        return navigationTypeName;
    }

    public void setNavigationTypeName(String navigationTypeName) {
        this.navigationTypeName = navigationTypeName;
    }

    public String getBackgroundType() {
        return backgroundType;
    }

    public void setBackgroundType(String backgroundType) {
        this.backgroundType = backgroundType;
    }

    public String getTypeContent() {
        return typeContent;
    }

    public void setTypeContent(String typeContent) {
        this.typeContent = typeContent;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public List<BackNavigationLevel> getNavigationParams() {
        return navigationParams;
    }

    public void setNavigationParams(List<BackNavigationLevel> navigationParams) {
        this.navigationParams = navigationParams;
    }
}
