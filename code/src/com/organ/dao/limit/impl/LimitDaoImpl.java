package com.organ.dao.limit.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;

import com.organ.common.BaseDao;
import com.organ.dao.adm.PrivDao;
import com.organ.dao.adm.RoleDao;
import com.organ.dao.adm.RolePrivDao;
import com.organ.dao.limit.LimitDao;
import com.organ.dao.limit.RoleAppSecretDao;
import com.organ.model.AppSecret;
import com.organ.model.TPriv;
import com.organ.model.TRole;
import com.organ.model.TRoleAppSecret;
import com.organ.model.TRolePriv;
import com.organ.utils.PrivUrlNameUtil;

public class LimitDaoImpl extends BaseDao<TPriv, Long> implements LimitDao {
	
	RoleDao roleDao;
	PrivDao privDao;
	RolePrivDao rolePrivDao;
	RoleAppSecretDao roleappsecretDao;
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(LimitDaoImpl.class);

	public RoleDao getRoleDao() {
		return roleDao;
	}

	public void setRoleDao(RoleDao roleDao) {
		this.roleDao = roleDao;
	}

	public PrivDao getPrivDao() {
		return privDao;
	}

	public void setPrivDao(PrivDao privDao) {
		this.privDao = privDao;
	}

	public RolePrivDao getRolePrivDao() {
		return rolePrivDao;
	}

	public void setRolePrivDao(RolePrivDao rolePrivDao) {
		this.rolePrivDao = rolePrivDao;
	}

	public RoleAppSecretDao getRoleappsecretDao() {
		return roleappsecretDao;
	}

	public void setRoleappsecretDao(RoleAppSecretDao roleappsecretDao) {
		this.roleappsecretDao = roleappsecretDao;
	}

	/**
	 * 实现添的接口
	 */
	@Override
	public int updatePriv(int parentId, String name, String app) {
		// TODO Auto-generated method stub
		TPriv tPriv = new TPriv();
		tPriv.setParentId(parentId);
		tPriv.setName(name);
		tPriv.setApp(app);
		tPriv.setUrl(PrivUrlNameUtil.initUrlName(name));
		tPriv.setListorder(0); // 这个不能为空
		// tPriv.setCategory("0");
		tPriv.setGrouping("0");
		try {
			save(tPriv);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tPriv.getId();
	}

	@Override
	public int DeletePriv(int privId) {
		// TODO Auto-generated method stub
		try {
			String hql = "delete TPriv where id=" + privId;

			int result = delete(hql);

			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int editPriv(int priv_id, String pid, String name, String app) {
		// TODO Auto-generated method stub
		try {
			String hql = "update TPriv tp set tp.name= '" + name
					+ "',tp.parentId='" + pid + "',tp.category='" + pid
					+ "',tp.app='" + app + "',tp.url='"
					+ PrivUrlNameUtil.initUrlName(name) + "' where tp.id="
					+ priv_id;
			int result = update(hql);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 根据权限名称搜索
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List searchPriv(String Name, int pagesize, int pageindex) {
		try {
			int start = pageindex * pagesize;
			String hql;
			if (!StringUtils.isBlank(Name)) {
				hql="SELECT tmp.id,tmp.parent_id,tmp.name,tmp.category,tmp.url,tmp.app,tmp.parent_name FROM ("
					+"SELECT pr.id,pr.parent_id,pr.name,pr.category,pr.url,pr.app,t.name AS parent_name FROM t_priv pr "
					+"LEFT JOIN t_priv t ON pr.parent_id=t.id "
					+"WHERE pr.parent_id IN (SELECT  p.id FROM t_priv p WHERE p.parent_id IN (SELECT tp.id  FROM  t_priv tp WHERE tp.parent_id=0)) "
					+") tmp WHERE tmp.name like '%"+Name+"%'"+" or tmp.url like '%"+Name+"%' limit "+start+","+pagesize;
			} else {
				hql ="SELECT pr.id,pr.parent_id,pr.name,pr.category,pr.url,pr.app,t.name AS parent_name FROM t_priv pr "
					+"LEFT JOIN t_priv t ON pr.parent_id=t.id "
					+"WHERE pr.parent_id IN (SELECT  p.id FROM t_priv p WHERE p.parent_id IN (SELECT tp.id  FROM  t_priv tp WHERE tp.parent_id=0)) "
					+"limit "+ start + "," + pagesize;
			}
			SQLQuery query = this.getSession().createSQLQuery(hql);
			List list = query.list();
			if (list.size() > 0) {
				return list;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public int getCount() {
		try {
			int result = count("from TPriv");
			return result;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int getSearchCount(String name) {
		// TODO Auto-generated method stub
		String sql;
		try {
			if (!StringUtils.isBlank(name)) {
				sql = "select count(*) from (" + "select " + "tp.id,"
						+ "tp.parent_id," + "tp.NAME," + "tp.category,"
						+ "tp.url," + "tp.app "
						+ "from t_priv tp where tp.name like '%" + name + "%'"
						+ "or tp.url like '%" + name + "%') as tt";
			} else {
				sql = "select count(*) from (" + "select " + "tp.id,"
						+ "tp.parent_id," + "tp.NAME," + "tp.category,"
						+ "tp.url," + "tp.app " + "from t_priv tp) as tt";
			}
			int SearchCount = Integer.parseInt(this.getSession()
					.createSQLQuery(sql).list().get(0).toString());
			return SearchCount;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List getLimitbyRole(Integer roleId, String appName) {
		String sql = "select p.id, p.name, p.parent_id parentid, p.grouping, rp.role_id roleid, p.url url"
				+ " from t_priv p"
				+ " left join t_role_priv rp"
				+ " on p.id = rp.priv_id and rp.role_id ="
				+ roleId
				+ " where p.app = '"
				+ appName
				+ "' order by p.parent_id desc, p.listorder desc";
		return runSql(sql);
	}

	@Override
	public List getRoleList(String appname) {
		try {
			String sql = null;
			if (!StringUtils.isBlank(appname)) {
				sql = "select tr.id,tr.name,tra.role_id from t_role tr "
						+ " join t_role_appsecret tra on tr.id = tra.role_id "
						+ "join t_appsecret ta on ta.id=tra.appsecret_id "
						+ "where ta.appname = '" + appname + "'";
			} else {
				sql = "select tr.id,tr.name from t_role tr "
						+ " join t_role_appsecret tra on tr.id = tra.role_id "
						+ "join t_appsecret ta on ta.id=tra.appsecret_id ";
			}
			System.err.println(sql);
			return runSql(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List getPrivNamebytwo(String appName) {
		try {
			String sql = "select p.id,p.name from t_priv p  where"
					+ " p.parent_id IN (select tp.id from t_priv tp where tp.parent_id = 0) and p.app='"+appName+"'";
			return runSql(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Integer saveRolebyApp(Integer roleId,Integer appsecretId,String roleName, String privs,
			String appName) {
		TRole role = roleDao.get(roleId);
		if (role == null) {
			role = new TRole();
			TRoleAppSecret roleAppSecret = new TRoleAppSecret();
			role.setName(roleName);
			role.setListorder(roleDao.getMax("listorder", "from TRole") + 1);
			roleAppSecret.setAppsecretId(appsecretId);
			roleAppSecret.setRoleId(roleId);
			roleappsecretDao.save(roleAppSecret);
			roleDao.save(role);
		}
		rolePrivDao.delete("delete from TRolePriv where roleId = " + role.getId());
		String[] pa = privs.split(",");
		Integer i = pa.length;
		while(i-- > 0) {
			if (!"".equals(pa[i])) {
				TRolePriv rolePriv = new TRolePriv();
				rolePriv.setRoleId(role.getId());
				rolePriv.setPrivId(Integer.parseInt(pa[i]));
				rolePrivDao.save(rolePriv);
			}
		}
		return role.getId();
	}

}
