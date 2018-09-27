package jdjt.com.homepager.http;

/**
 * Created by xxd on 2017/8/15.
 * 封装服务器返回的字段ERR类型异常，用于区分网络异常等
 */

public class ERRException extends RuntimeException {

    public ERRException() {
    }

    public ERRException(String message) {
        super(message);
    }

    public ERRException(String message, Throwable cause) {
        super(message, cause);
    }

    public ERRException(Throwable cause) {
        super(cause);
    }

    public ERRException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
