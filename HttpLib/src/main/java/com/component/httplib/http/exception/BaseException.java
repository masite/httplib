package com.component.httplib.http.exception;

public class BaseException extends Exception {
    public static final int UN_KNOWN_ERROR = 1000;
    public static final int ANALYTIC_SERVER_DATA_ERROR = 1001;
    public static final int JSON_SYNTAX_DATA_ERROR = 1006;
    public static final int NETWORK_ERROR = 1007;
    public static final int NET_TIME_OUT_ERROR = 1004;
    public static final int NET_CONNECT_ERROR = 1003;
    public static final int NET_HOST_ERROR = 1005;
    private String msg;
    private String code;

    public BaseException(Throwable cause, String code, String msg) {
        super(cause);
        this.code = code;
        this.msg = msg;
    }

    public BaseException(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public BaseException(Throwable cause, String code) {
        super(cause);
        this.code = code;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
