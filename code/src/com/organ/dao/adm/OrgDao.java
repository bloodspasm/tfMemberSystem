package com.organ.dao.adm;

import java.util.List;

import com.organ.common.IBaseDao;
import com.organ.model.TOrgan;

public interface OrgDao extends IBaseDao<TOrgan, Integer> {

	public List getProvince(); 
	public List getCity(Integer provinceId); 
	public List getDistrict(Integer cityId);
	public List getInward();
	public List getIndustry();
	public List getSubdustry(Integer industryId);
	public TOrgan getInfo(Integer orgId);
	public List getInfos(String soStr);
	public List<TOrgan> getList();
	public TOrgan getOrganByCode(String organCode);
	public int getMaxNumber();
}