package com.platform.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platform.dao.ShopDao;
import com.platform.dao.UserDao;
import com.platform.entity.ShopEntity;
import com.platform.entity.SysUserEntity;
import com.platform.entity.UserEntity;
import com.platform.service.ShopService;
import com.platform.service.SysUserService;
import com.platform.util.ShopConstant;
import com.platform.utils.ShiroUtils;

/**
 * Service实现类
 *
 * @author softopensell
 * @email softopensell_javadream@163.com
 * @date 2019-06-28 23:37:18
 */
@Service("shopService")
public class ShopServiceImpl implements ShopService {
    @Autowired
    private ShopDao shopDao;
    @Autowired
    private SysUserService sysUserService;
    
    @Autowired
    private UserDao userDao;

    @Override
    public ShopEntity queryObject(Integer id) {
        return shopDao.queryObject(id);
    }

    @Override
    public List<ShopEntity> queryList(Map<String, Object> map) {
        return shopDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return shopDao.queryTotal(map);
    }

    @Override
    public int save(ShopEntity shop) {
        return shopDao.save(shop);
    }

    @Override
    public int update(ShopEntity shop) {
        return shopDao.update(shop);
    }

    @Override
    public int delete(Integer id) {
        return shopDao.delete(id);
    }

    @Override
    public int deleteBatch(Integer[] ids) {
        return shopDao.deleteBatch(ids);
    }

	@Override
	public void saveAudit(ShopEntity shop) {
		ShopEntity qshop = shopDao.queryObject(shop.getId());
		if(qshop!=null) {
			qshop.setAuditUserId(ShiroUtils.getUserEntity().getUserId().intValue());
			qshop.setAuditStatu(shop.getAuditStatu());
			qshop.setAuditTime(new Date());
			qshop.setAuditOpinion(shop.getAuditOpinion());
			if(shop.getAuditStatu()==ShopConstant.SHOP_AUDIT_STATUS_SUCCESS) {
				UserEntity createUser = userDao.queryObject(qshop.getCreateUserId());
				if(createUser!=null) {
					SysUserEntity sysUser=new SysUserEntity();
					sysUser.setUsername(createUser.getUserName());
					sysUser.setMobile(createUser.getMobile());
					sysUser.setStatus(1);
					sysUser.setCreateUserId(ShiroUtils.getUserEntity().getUserId());
					List<Long> roleIdList = new ArrayList<Long>();
					roleIdList.add(ShopConstant.SHOP_ROLE_ID);
					sysUser.setRoleIdList(roleIdList);
					sysUserService.save(sysUser);
					qshop.setSystemUserId(sysUser.getUserId().intValue());
				}
			}
			shopDao.update(qshop);
		}
	}
}
