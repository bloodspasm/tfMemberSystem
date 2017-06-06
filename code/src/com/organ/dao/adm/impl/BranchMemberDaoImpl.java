package com.organ.dao.adm.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.organ.common.BaseDao;
import com.organ.dao.adm.BranchMemberDao;
import com.organ.model.TBranchMember;

public class BranchMemberDaoImpl extends BaseDao<TBranchMember, Integer>
		implements BranchMemberDao {

	@SuppressWarnings("unchecked")
	@Override
	public TBranchMember getBranchMemberByBranchPosition(Integer branchId,
			Integer positionId) {

		Criteria c = getCriteria();
		c.add(Restrictions.eq("branchId", branchId));
		c.add(Restrictions.eq("positionId", positionId));
		c.add(Restrictions.eq("isDel", "1"));

		List list = c.list();
		if (list.isEmpty()) {
			return null;
		}
		return (TBranchMember) list.get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TBranchMember> getBranchMemberByMember(Integer memberId) {

		Criteria c = getCriteria();
		c.add(Restrictions.eq("memberId", memberId));
		c.add(Restrictions.eq("isDel", "1"));

		List list = c.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TBranchMember> getBranchMemberByBranch(Integer branchId) {

		Criteria c = getCriteria();
		c.add(Restrictions.eq("branchId", branchId));
		c.add(Restrictions.eq("isDel", "1"));

		List list = c.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void selectMaster(Integer memberId) {

		Criteria c = getCriteria();
		c.add(Restrictions.eq("memberId", memberId));
		c.add(Restrictions.eq("isMaster", "0"));
		c.add(Restrictions.eq("isDel", "1"));
		c.addOrder(Order.asc("listorder"));

		List list = c.list();
		if (list.isEmpty())
			return;

		TBranchMember bm = (TBranchMember) list.get(0);
		bm.setIsMaster("1");
		update(bm);
	}

	@SuppressWarnings("unchecked")
	@Override
	public TBranchMember getBranchMemberByBranchMember(Integer branchId,
			Integer memberId) {

		Criteria c = getCriteria();
		c.add(Restrictions.eq("branchId", branchId));
		c.add(Restrictions.eq("memberId", memberId));
		c.add(Restrictions.eq("isDel", "1"));

		List list = c.list();
		if (list.isEmpty()) {
			return null;
		}
		return (TBranchMember) list.get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List getBranchMemberByMemberIds(String memberIds) {

		String hql = (new StringBuilder(
				"select BM.member_id mid, P.name pname, B.name bname, BM.is_master master, B.manager_id mnid  from t_branch_member BM left join t_branch B on B.id=BM.branch_id left join t_position P on BM.position_id=P.id where BM.isdel='1' and BM.member_id in (")
				.append(memberIds).append(")")).toString();
		return getSession().createSQLQuery(hql).list();
	}

	@Override
	public int updatePositionByUseId(int userIdInt, int positionId) {
		String hql = (new StringBuilder(
				"update TBranchMember t set t.positionId=").append(positionId)
				.append("where t.memberId=").append(userIdInt)).toString();
		return update(hql);
	}

	@Override
	public int delRelationByIds(String userids, String isLogic) {
		try {
			if (isLogic.equals("1")) {
				String hql = (new StringBuilder("update TBranchMember set isDel=0 where memberId in (").append(userids).append(")")).toString();
				return update(hql);
			} else {
				String hql = (new StringBuilder("delete from TBranchMember where memberId in (").append(userids).append(")")).toString();
				return delete(hql);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int getBranchMemberCountByPositionId(Integer id) {
		return count("from TBranchMember t where t.positionId=" + id + " and t.isDel='1'");
	}

	@Override
	public boolean getMasterMemberById(int memberId) {
		String hql = (new StringBuilder("from TBranchMember where memberId=").append(memberId).append(" and isMaster='1'").append(" and isDel='1'")).toString();
		List list = find(hql);
		
		if (list != null && list.size() > 0) {
			return true;
		}
		return false;
	}

	@Override
	public int getBranchMemberCountByMember(int memberId) {
		String hql = (new StringBuilder("from TBranchMember where memberId=").append(memberId).append(" and isDel='1'")).toString();
		int count = count(hql);
		return count;
	}

}