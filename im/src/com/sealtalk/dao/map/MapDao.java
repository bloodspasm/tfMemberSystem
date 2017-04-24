package com.sealtalk.dao.map;

import java.util.List;

import javax.ws.rs.DELETE;

import com.sealtalk.common.IBaseDao;
import com.sealtalk.model.TMap;

public interface MapDao extends IBaseDao<TMap, Long> {

	/**
	 * 获取单人坐标
	 * @param targetId
	 * @return
	 */
	@Deprecated
	public Object[] getLocation(int targetId);

	/**
	 * 获取群成员坐标
	 * @param targetIdInt
	 * @return
	 */
	@Deprecated
	public List<Object[]> getLocationForMultId(String targetIdInt);

	/**
	 * 提交个人位置
	 * @param tm
	 */
	public void saveLocation(TMap tm);

	/**
	 * 获取坐标模型
	 * @param userId
	 * @return
	 */
	public TMap getLaLongtitudeForUserId(int userId);

	/**
	 * 更新坐标
	 * @param userId
	 * @param latitude
	 * @param longtitude
	 * @param now 
	 */
	public void updateLocation(int userId, String latitude, String longtitude, long now);

	/**
	 * 获取多个人的坐标
	 * @param ids
	 * @return
	 */
	public List<TMap> getMapByIds(Integer[] ids);

}