package com.platform.validator;

import org.apache.commons.lang.StringUtils;

import com.platform.utils.ApiRRException;

/**
 * 数据校验
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2017-03-23 15:50
 */
public abstract class ApiAssert {

    public static void isBlank(String str, String message) {
        if (StringUtils.isBlank(str)) {
            throw new ApiRRException(message);
        }
    }

    public static void isNull(Object object, String message) {
        if (object == null) {
            throw new ApiRRException(message);
        }
    }
}
