package com.platform.dao;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.platform.entity.BonusPointsVo;

/**
 * Dao
 *
 * @author softopensell
 * @email softopensell@outlook.com
 * @date 2019-04-29 10:46:58
 */
public interface ApiBonusPointsMapper extends BaseDao<BonusPointsVo> {
	public BonusPointsVo getUesrPoint(@Param("userId") Integer userId,@Param("bloodType") Integer bloodType);//bp.

	public int reduceBonusTeamInvitedPoints(@Param("userIds")Collection<Integer> userIds,@Param("incrPoint") BigDecimal incrPoint);
	public int addBonusTeamInvitedPoints(@Param("userIds")Collection<Integer> userIds,@Param("incrPoint") BigDecimal incrPoint);
	public int addBonusTeamInvitedSum(@Param("userIds")Collection<Integer> userIds,@Param("incrSum") Integer incrSum);
	public int reduceBonusTeamInvitedSum(@Param("userIds")Collection<Integer> userIds,@Param("incrSum") Integer incrSum);

	public List<BonusPointsVo> getUserPointByUserIds(@Param("userIds")Collection<Integer> userIds);

	public List<BonusPointsVo> findByUserRoleType(@Param("userRoles")List<Integer> userRoles);

	BonusPointsVo queryByUserIdAndBloodType(@Param("userId") Integer userId,@Param("bloodType") Integer bloodType);
	List<BonusPointsVo> queryByParentUserId(@Param("userId") Integer userId,@Param("bloodType") Integer bloodType);

	
}
