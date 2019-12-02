package com.platform.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 自定义异常
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2016年10月27日 下午10:11:27
 */
public class ApiRRException extends RuntimeException {
	 private static final Log log = LogFactory.getLog(ApiRRException.class);
    private static final long serialVersionUID = 1L;

    private String errmsg;
    private int errno = 500;

    public ApiRRException(String errmsg) {
        super(errmsg);
        log.info("----------ApiRRException.errmsg------:"+errmsg);
        this.errmsg = errmsg;
    }

    public ApiRRException(String errmsg, Throwable e) {
        super(errmsg, e);
        log.info("----------ApiRRException.errmsg------:"+errmsg+"----------ApiRRException.Throwable------:"+e);
        this.errmsg = errmsg;
    }

    public ApiRRException(String errmsg, int errno) {
        super(errmsg);
        log.info("----------ApiRRException.errmsg------:"+errmsg+"----------ApiRRException.errno------:"+errno);
        this.errmsg = errmsg;
        this.errno = errno;
    }

    public ApiRRException(String errmsg, int errno, Throwable e) {
        super(errmsg, e);
        log.info("----------ApiRRException.errmsg------:"+errmsg+"----------ApiRRException.errno------:"+errno+"----------ApiRRException.Throwable------:"+e);
        this.errmsg = errmsg;
        this.errno = errno;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public int getErrno() {
        return errno;
    }

    public void setErrno(int errno) {
        this.errno = errno;
    }
}
