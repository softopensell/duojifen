package com.platform.dao;

import org.apache.ibatis.annotations.Param;

import com.platform.entity.TokenEntity;

/**
 * 用户Token
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2017-03-23 15:22:07
 */
public interface ApiTokenMapper extends BaseDao<TokenEntity> {

    TokenEntity queryByUserId(@Param("userId") Integer userId);

    TokenEntity queryByToken(@Param("token") String token);

}
