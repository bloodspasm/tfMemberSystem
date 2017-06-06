package com.organ.dao.adm;

import java.util.List;

import com.organ.common.IBaseDao;
import com.organ.model.TPriv;

public interface PrivDao extends IBaseDao<TPriv, Integer> {

	/**
	 * 根据url获取权限
	 * @param url
	 * @param organId 
	 * @return
	 */
	public List<TPriv> getPrivByUrl(String[] url, int organId);
	public List<TPriv> getPrivByOrganAndApp(String app, int organId);

	public int getMaxPrivId();

}