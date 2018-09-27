package jdjt.com.homepager.http;

/**
 * 新接口中返回code不为200，后者body为null,抛出此异常和信息
 * 使调用者知道由此类异常存在
 */
public class CodeException extends Exception {

    public CodeException() {
    }

    public CodeException(String message) {
        super(message);
    }

    public CodeException(String message, Throwable cause) {
        super(message, cause);
    }

    public CodeException(Throwable cause) {
        super(cause);
    }

    public CodeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
