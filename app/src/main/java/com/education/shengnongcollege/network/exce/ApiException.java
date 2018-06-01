package com.education.shengnongcollege.network.exce;

/**
 * Created by IntelliJ IDEA.
 * User: xiaolong
 */
public class ApiException extends RuntimeException {

    public static final int KEY_ERROR = 311;
    public static final int WRONG_PASSWORD = 101;
    private int errorCode;
    private String errorMsg;

    public ApiException(int errorCode, String errorMsg) {
        super(errorMsg != null ? errorMsg : "网络错误");
        this.errorCode = errorCode;
        if (errorMsg != null) {
            this.errorMsg = errorMsg;
        } else {
            this.errorMsg = "网络错误";
        }

    }

    @Override
    public String getMessage() {
        return errorMsg;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    /**
     * 由于服务器传递过来的错误信息直接给用户看的话，用户未必能够理解
     * 需要根据错误码对错误信息进行一个转换，在显示给用户
     *
     * @param code
     * @return
     */
    private static String getApiExceptionMessage(int code) {
        String message;
        switch (code) {
            case KEY_ERROR:
                message = "该用户不存在";
                break;
            case WRONG_PASSWORD:
                message = "密码错误";
                break;
            default:
                message = "未知错误";

        }
        return message;
    }
}