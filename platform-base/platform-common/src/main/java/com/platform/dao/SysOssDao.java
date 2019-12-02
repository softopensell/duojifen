package com.platform.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.platform.entity.SysOssEntity;

/**
 * 文件上传Dao
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2017-03-25 12:13:26
 */
public interface SysOssDao extends BaseDao<SysOssEntity> {

	List<SysOssEntity> queryByFromTypeAndFromNo(@Param("ossFormType") Integer ossFormType,@Param("ossFormNo") String ossFormNo);
	SysOssEntity queryByUrl(@Param("url") String url);

}
