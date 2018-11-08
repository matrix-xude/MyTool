package jdjt.com.homepager.domain.back;

/**
 * Created by xxd on 2018/11/6.
 * V客会体验卡预约信息酒店核销码
 */

public class BackVExperienceInfoLevel2 {

    private String verificationCode; // 核销码code
    private String verificationId; // 核销码Id
    private String valid; // 使用标识（0：未使用 1：已使用）

    private boolean check; // 是否已经勾选，本地使用

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public String getVerificationId() {
        return verificationId;
    }

    public void setVerificationId(String verificationId) {
        this.verificationId = verificationId;
    }

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
