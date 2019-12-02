package com.platform.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.platform.entity.SysUserEntity;
import com.platform.utils.ShiroUtils;

/**
 * Controller公共组件
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2016年11月9日 下午9:42:26
 */
public abstract class AbstractController {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected SysUserEntity getUser() {
        return ShiroUtils.getUserEntity();
    }

    protected Long getUserId() {
        return getUser().getUserId();
    }

    protected Long getDeptId() {
        return getUser().getDeptId();
    }
    protected boolean isAdmin() {
    	SysUserEntity user=getUser();
    	if(user.getUserId().equals(1l)) {
        	return true;
        }
        return false;
    }
}
