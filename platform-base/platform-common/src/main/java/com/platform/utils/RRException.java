package com.platform.utils;

/**
 * 自定义异常
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2016年10月27日 下午10:11:27
 */
public class RRException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private String errmsg;
    private int errno = 500;

    public RRException(String msg) {
        super(msg);
        this.errmsg = msg;
    }

    public RRException(String msg, Throwable e) {
        super(msg, e);
        this.errmsg = msg;
    }

    public RRException(String msg, int code) {
        super(msg);
        this.errmsg = msg;
        this.errno = code;
    }

    public RRException(String msg, int code, Throwable e) {
        super(msg, e);
        this.errmsg = msg;
        this.errno = code;
    }

    public String getMsg() {
        return errmsg;
    }

    public void setMsg(String msg) {
        this.errmsg = msg;
    }

    public int getCode() {
        return errno;
    }

    public void setCode(int code) {
        this.errno = code;
    }


}
