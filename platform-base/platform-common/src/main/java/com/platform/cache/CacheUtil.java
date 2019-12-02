package com.platform.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;

import com.platform.dao.SysMacroDao;
import com.platform.entity.SysMacroEntity;
import com.platform.utils.SpringContextUtils;
import com.platform.utils.StringUtils;

/**
 * 作者: @author Harmon <br>
 * 时间: 2017-08-16 10:14<br>
 * 描述: CacheUtil <br>
 */
public class CacheUtil implements InitializingBean {
	 protected Logger logger = Logger.getLogger(getClass());
    public  void init() {
        SysMacroDao macroDao = SpringContextUtils.getBean(SysMacroDao.class);
        if (null != macroDao) {
            J2CacheUtils.put("macroList", macroDao.queryList(new HashMap<String, Object>()));
            logger.info("--------macroList-------");
        }
    }
    @Override
    public void afterPropertiesSet() throws Exception {
        init();
    }

    /**
     * 根据字典标识获取字典中文
     *
     * @param preName 父级name
     * @param value   字典value
     * @return
     */
    public static String getCodeName(String preName, String value) {
        String name = "";
        Long parentId = 0L;
        List<SysMacroEntity> sysMacroEntityList = (List<SysMacroEntity>) J2CacheUtils.get("macroList");
        if (!StringUtils.isNullOrEmpty(sysMacroEntityList)) {
            for (SysMacroEntity macroEntity : sysMacroEntityList) {
                if (preName.equals(macroEntity.getName())) {
                    parentId = macroEntity.getId();
                }
            }
            for (SysMacroEntity macroEntity : sysMacroEntityList) {
                if (value.equals(macroEntity.getValue()) && parentId.equals(macroEntity.getParentId())) {
                    name = macroEntity.getName();
                }
            }
        }
        return name;
    }
    /**
     * 获取
     * @param preCode
     * @param value 
     * @return codeName
     */
    public static String getNameByCode(String preCode, String value) {
        String name = "";
        Long parentId = 0L;
        List<SysMacroEntity> sysMacroEntityList = (List<SysMacroEntity>) J2CacheUtils.get("macroList");

        if (!StringUtils.isNullOrEmpty(sysMacroEntityList)) {
            for (SysMacroEntity macroEntity : sysMacroEntityList) {
                if (preCode.equals(macroEntity.getValue())) {
                    parentId = macroEntity.getId();
                }
            }
            for (SysMacroEntity macroEntity : sysMacroEntityList) {
                if (value.equals(macroEntity.getValue()) && parentId.equals(macroEntity.getParentId())) {
                    name = macroEntity.getName();
                }
            }
        }
        return name;
    }
    
    /**
     * 根据字典标识获取字典中文
     *
     * @param preCode 父级code
     * @param code   字典value
     * @return code value
     */
    public static String getCodeValueByCode(String preCode, String code) {
        String codeValue = "";
        Long parentId = 0L;
        List<SysMacroEntity> sysMacroEntityList = (List<SysMacroEntity>) J2CacheUtils.get("macroList");
        if (!StringUtils.isNullOrEmpty(sysMacroEntityList)) {
            for (SysMacroEntity macroEntity : sysMacroEntityList) {
                if (preCode.equals(macroEntity.getValue())) {
                    parentId = macroEntity.getId();
                }
            }
            for (SysMacroEntity macroEntity : sysMacroEntityList) {
                if (code.equals(macroEntity.getName()) && parentId.equals(macroEntity.getParentId())) {
                	codeValue = macroEntity.getValue();
                }
            }
        }
        return codeValue;
    }
    
    /**
     * 根据字典类别代码获取字典列表
     *
     * @param preCode 父级code
     * @return
     */
    public static List<SysMacroEntity> getListByPreCode(String preCode) {
    	List<SysMacroEntity> list=new ArrayList<SysMacroEntity>();
        Long parentId = 0L;
        List<SysMacroEntity> sysMacroEntityList = (List<SysMacroEntity>) J2CacheUtils.get("macroList");
        if (!StringUtils.isNullOrEmpty(sysMacroEntityList)) {
            for (SysMacroEntity macroEntity : sysMacroEntityList) {
                if (preCode.equals(macroEntity.getValue())) {
                    parentId = macroEntity.getId();
                }
            }
            for (SysMacroEntity macroEntity : sysMacroEntityList) {
                if (parentId.equals(macroEntity.getParentId())) {
                	list.add(macroEntity);
                }
            }
        }
        return list;
    }

}