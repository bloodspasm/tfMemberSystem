package com.organ.service.member.impl;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import jxl.write.WriteException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.organ.common.SysInterface;
import com.organ.common.Tips;
import com.organ.dao.adm.BranchDao;
import com.organ.dao.adm.BranchMemberDao;
import com.organ.dao.adm.MemberRoleDao;
import com.organ.dao.auth.UserValidDao;
import com.organ.dao.member.MemberDao;
import com.organ.dao.member.TextCodeDao;
import com.organ.dao.upload.CutLogoTempDao;
import com.organ.model.TBranch;
import com.organ.model.TMember;
import com.organ.model.TextCode;
import com.organ.service.member.MemberService;
import com.organ.utils.FileUtil;
import com.organ.utils.HttpRequest;
import com.organ.utils.JSONUtils;
import com.organ.utils.LogUtils;
import com.organ.utils.PasswordGenerator;
import com.organ.utils.PropertiesUtils;
import com.organ.utils.RongCloudUtils;
import com.organ.utils.StringUtils;
import com.organ.utils.TimeGenerator;
import com.organ.utils.XlsUtils;

public class MemberServiceImpl implements MemberService {

	private static final Logger logger = LogManager.getLogger(MemberServiceImpl.class);
	
	@Override
	public TMember searchSigleUser(String name, String password, int organId) {
		TMember memeber = null;

		logger.info("name:" + name + ",password: " + password);
		try {
			memeber = memberDao.searchSigleUserByOrgan(name, password, organId);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(LogUtils.getInstance().getErrorInfoFromException(e));
		}
		return memeber;
	}

	@Override
	public boolean updateUserPwdForAccount(String account, String newPwd, int organId) {
		boolean status = false;

		logger.info("name:" + account + ",newPwd: " + newPwd);
		try {
			status = memberDao.updateUserPwdForAccount(account, newPwd, organId);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(LogUtils.getInstance().getErrorInfoFromException(e));
		}

		return status;
	}

	@Override
	public boolean updateUserPwdForPhone(String phone, String newPwd) {
		boolean status = false;

		logger.info("phone:" + phone + ",newPwd: " + newPwd);
		try {
			status = memberDao.updateUserPwdForPhone(phone, newPwd);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(LogUtils.getInstance().getErrorInfoFromException(e));
		}

		return status;
	}

	@Override
	public String getOneOfMember(String userId) {

		JSONObject jo = new JSONObject();

		logger.info("userID: " + userId);
		try {
			int userIdInt = StringUtils.getInstance().strToInt(userId);

			Object[] member = memberDao.getOneOfMember(userIdInt);

			if (member == null) {
				jo.put("code", 0);
				jo.put("text", Tips.NULLUSER.getText());
			} else {
				for (int i = 0; i < member.length; i++) {
					jo.put("id", isBlank(member[0]));
					jo.put("account", isBlank(member[1]));
					jo.put("name", isBlank(member[2]));
					jo.put("logo", isBlank(member[3]));
					jo.put("telephone", isBlank(member[4]));
					jo.put("email", isBlank(member[5]));
					jo.put("address", isBlank(member[6]));
					jo.put("token", isBlank(member[7]));
					jo.put("sex", isBlank(member[8]));
					jo.put("birthday", isBlank(member[9]));
					jo.put("workno", isBlank(member[10]));
					jo.put("mobile", isBlank(member[11]));
					jo.put("intro", isBlank(member[12]));
					jo.put("branchid", isBlank(member[13]));
					jo.put("branchname", isBlank(member[14]));
					jo.put("positionid", isBlank(member[15]));
					jo.put("positionname", isBlank(member[16]));
					jo.put("organid", isBlank(member[17]));
					jo.put("organname", isBlank(member[18]));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(LogUtils.getInstance().getErrorInfoFromException(e));
		}
		return jo.toString();
	}

	@Override
	public int updateUserTokenForId(String userId, String token) {
		int row = 0;

		logger.info("userID: " + userId + ",token: " + token);
		
		try {
			row = memberDao.updateUserTokenForId(userId, token);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(LogUtils.getInstance().getErrorInfoFromException(e));
		}

		return row;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String searchUser(String account, int organId) {
		JSONArray ja = new JSONArray();

		logger.info("account: " + account + ",organId: " + organId);
		try {
			account =  java.net.URLDecoder.decode(account, "utf-8");
			List members = memberDao.searchUser(account, organId);

			if (members == null) {
				JSONObject jo = new JSONObject();
				jo.put("code", 0);
				jo.put("text", Tips.NULLUSER.getText());
			} else {
				for (int i = 0; i < members.size(); i++) {
					Object[] member = (Object[]) members.get(i);
					JSONObject jo = new JSONObject();
					if (String.valueOf(member[15]).equals("1")) {
						jo.put("id", isBlank(member[0]));
						jo.put("account", isBlank(member[1]));
						jo.put("name", isBlank(member[2]));
						jo.put("logo", isBlank(member[3]));
						jo.put("telephone", isBlank(member[4]));
						jo.put("email", isBlank(member[5]));
						jo.put("address", isBlank(member[6]));
						jo.put("birthday", isBlank(member[7]));
						jo.put("workno", isBlank(member[8]));
						jo.put("mobile", isBlank(member[9]));
						jo.put("intro", isBlank(member[10]));
						jo.put("sex", isBlank(member[11]));
						jo.put("branchname", isBlank(member[12]));
						jo.put("positionname", isBlank(member[13]));
						jo.put("organname", isBlank(member[14]));
						ja.add(jo);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(LogUtils.getInstance().getErrorInfoFromException(e));
		}
		return ja.toString();
	}

	@Override
	public boolean valideOldPwd(String account, String oldPwd, int organId) {
		boolean b = false;

		try {
			b = memberDao.valideOldPwd(account, oldPwd, organId);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(LogUtils.getInstance().getErrorInfoFromException(e));
		}
		return b;
	}

	@Override
	public String getTextCode(String phone) {
		String code = null;

		logger.info("phone: " + phone);
		try {
			TextCode tc = textCodeDao.getTextCode(phone);

			if (tc != null) {
				long now = TimeGenerator.getInstance().getUnixTime();
				long createTime = tc.getCreateTime();
				long valideTime = StringUtils.getInstance().strToLong(
						PropertiesUtils.getStringByKey("code.validetime"));

				if ((now - createTime) >= valideTime) {
					code = "-1";
				} else {
					code = tc.getTextCode();
				}
			} else {
				code = "-1";
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(LogUtils.getInstance().getErrorInfoFromException(e));
		}

		return code;
	}

	@Override
	public void saveTextCode(String phone, String code) {
		
		logger.info("phone: " + phone + ",code=" + code);
		
		try {
			textCodeDao.deleteTextCode(phone);

			TextCode stc = new TextCode();
			stc.setPhoneNum(phone);
			stc.setTextCode(code);
			stc.setCreateTime(TimeGenerator.getInstance().getUnixTime());
			textCodeDao.saveTextCode(stc);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(LogUtils.getInstance().getErrorInfoFromException(e));
		}
	}

	@Override
	public String updateMemberInfoForWeb(String userId, String position,
			String fullName, String sign) {

		logger.info("userId: " + userId + ",position=" + position + ",fullName: " + fullName + ",sign:" + sign);
		
		JSONObject jo = new JSONObject();

		if (StringUtils.getInstance().isBlank(userId)) {
			jo.put("code", -1);
			jo.put("text", Tips.WRONGPARAMS.getText());
		} else {
			try {
				int userIdInt = StringUtils.getInstance().strToInt(userId);
				int ret = memberDao.updateMemeberInfoForWeb(userIdInt,
						fullName, sign);

				if (ret > 0) {

					if (!StringUtils.getInstance().isBlank(position)) {
						int positionId = StringUtils.getInstance().strToInt(
								position);
						branchMemberDao.updatePositionByUseId(userIdInt,
								positionId);
					}
					jo.put("code", 1);
					jo.put("text", Tips.OK.getText());
				} else {
					jo.put("code", 0);
					jo.put("text", Tips.FAIL.getText());
				}

			} catch (Exception e) {
				e.printStackTrace();
				logger.error(LogUtils.getInstance().getErrorInfoFromException(e));
			}
		}

		return jo.toString();
	}

	@Override
	@Deprecated
	public boolean updateUserPwd(String account, String newPwd) {
		boolean status = false;

		try {
			String md5Pwd = PasswordGenerator.getInstance().getMD5Str(newPwd);

			status = memberDao.updateUserPwd(account, md5Pwd);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(LogUtils.getInstance().getErrorInfoFromException(e));
		}

		return status;
	}

	@Override
	public String updateMemberForApp(String userId, String email,
			String mobile, String phone, String address) {
		
		logger.info("userId: " + userId + ",email=" + email + ",mobile: " + mobile + ",phone:" + phone + ",address:"+address);
		JSONObject jo = new JSONObject();

		if (StringUtils.getInstance().isBlank(userId)) {
			jo.put("code", -1);
			jo.put("text", Tips.WRONGPARAMS.getText());
		} else {
			try {
				int userIdInt = StringUtils.getInstance().strToInt(userId);
				int ret = memberDao.updateMemeberInfoForApp(userIdInt, email,
						mobile, phone, address);

				if (ret > 0) {
					jo.put("code", 1);
					jo.put("text", Tips.OK.getText());
				} else {
					jo.put("code", 0);
					jo.put("text", Tips.FAIL.getText());
				}

			} catch (Exception e) {
				e.printStackTrace();
				logger.error(LogUtils.getInstance().getErrorInfoFromException(e));
			}
		}

		return jo.toString();
	}

	@Override
	public TMember getMemberByToken(String token) {

		logger.info("token: " + token);
		try {
			if (!StringUtils.getInstance().isBlank(token)) {
				TMember member = memberDao.getMemberByToken(token);

				if (member != null) {
					String tokenMaxAge = PropertiesUtils
							.getStringByKey("db.tokenMaxAge");

					long tokenMaxAgeLong = 0;
					long now = TimeGenerator.getInstance().getUnixTime();
					long firstTokenDate = member.getCreatetokendate();

					if (tokenMaxAge != null && !"".equals(tokenMaxAge)) {
						tokenMaxAgeLong = Long.valueOf(tokenMaxAge);
					}

					if ((now - firstTokenDate) <= tokenMaxAgeLong
							|| tokenMaxAgeLong == 0) {
						return member;
					}
				} else {
					logger.info("member is null");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(LogUtils.getInstance().getErrorInfoFromException(e));
		}
		return null;
	}

	@Override
	public String getAllMemberInfo(int organId) {
		JSONObject jo = new JSONObject();

		try {
			List<TMember> memberList = memberDao.getAllMemberInfo(organId);

			if (memberList != null && memberList.size() > 0) {
				int memberLen = memberList.size();

				JSONArray ja = new JSONArray();

				for (int i = 0; i < memberLen; i++) {
					TMember tms = memberList.get(i);
					JSONObject text = JSONUtils.getInstance().modelToJSONObj(
							tms);
					ja.add(text);
				}
				jo.put("code", 1);
				jo.put("text", ja.toString());
			} else {
				logger.warn("memberList is null");
				jo.put("code", 0);
				jo.put("text", Tips.NULLGROUPMEMBER.getText());
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(LogUtils.getInstance().getErrorInfoFromException(e));
		}

		return jo.toString();
	}

	@Override
	public String getAllMemberOnLineStatus(int organId, String userIds) {
		JSONObject jo = new JSONObject();

		logger.info("userIds: " + userIds);
		try {
			ArrayList<String> idList = null;

			if (StringUtils.getInstance().isBlank(userIds)) {
				idList = new ArrayList<String>();
				List<TMember> memberList = memberDao.getAllMemberInfo(organId);
				if (memberList != null) {
					int memberLen = memberList.size();
					for (int i = 0; i < memberLen; i++) {
						TMember tms = memberList.get(i);
						String id = tms.getId() + "";
						idList.add(id);
					}
				}
			} else {
				userIds = StringUtils.getInstance().replaceChar(userIds, "\"",
						"");
				userIds = StringUtils.getInstance().replaceChar(userIds, "[",
						"");
				userIds = StringUtils.getInstance().replaceChar(userIds, "]",
						"");

				String[] userIdses = userIds.split(",");

				idList = new ArrayList<String>(Arrays.asList(userIdses));
			}

			JSONObject ja = new JSONObject();
			// 各成员在线状态
			logger.info("idList: " + idList.toString());
			for (int i = 0; i < idList.size(); i++) {
				String id = idList.get(i);
				String status = RongCloudUtils.getInstance().checkOnLine(id);
				ja.put(id, status == null ? 0 : status);
			}
			jo.put("code", 1);
			jo.put("text", ja.toString());

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(LogUtils.getInstance().getErrorInfoFromException(e));
		}
		return jo.toString();
	}

	@Override
	public String getMultipleMemberForAccounts(String mulMemberStr, int organId) {
		String[] mulMemberStrs = null;
		String ret = null;
		JSONObject jo = new JSONObject();
		
		logger.info("mulMemberStr: " + mulMemberStr);
		
		if (!StringUtils.getInstance().isBlank(mulMemberStr)) {
			mulMemberStr = StringUtils.getInstance().replaceChar(mulMemberStr, "]", "");
			mulMemberStr = StringUtils.getInstance().replaceChar(mulMemberStr, "[", "");
			mulMemberStr = StringUtils.getInstance().replaceChar(mulMemberStr, "\"", "");
			mulMemberStrs = mulMemberStr.split(",");
			List<TMember> memberList = memberDao.getMultipleMemberForAccounts(mulMemberStrs, organId);
			JSONArray ja = new JSONArray();
			
			if (memberList != null) {
				int len = memberList.size();
				
				for (int i = 0; i < len; i++) {
					JSONObject j = new JSONObject();
					TMember t = memberList.get(i);
					j.put("id", t.getId());
					j.put("name", t.getFullname());
					j.put("account", t.getAccount());
					ja.add(j);
				}
				jo.put("code", 1);
				jo.put("text", ja.toString());
			} else {
				jo.put("code", 0);
				jo.put("text", Tips.NULLGROUPMEMBER.getText());
			}
		} else {
			jo.put("code", 0);
			jo.put("text", Tips.NULLUSER.getText());
		}
		ret = jo.toString();
		return ret;
	}
	
	@Override
	public int countMember(int organId) {
		try {
			int count = memberDao.getMemberCount(organId);
			return count;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(LogUtils.getInstance().getErrorInfoFromException(e));
		}

		return 0;
	}

	@Override
	public String getMemberIdsByAccount(String names, int organId) {
		String code = "0";
		String text = null;
		JSONObject ret = new JSONObject();
		
		logger.info("names: " + names);
		try {
			if (StringUtils.getInstance().isBlank(names)) {
				text = Tips.WRONGPARAMS.getText();
			} else {
				String[] namesArr = StringUtils.getInstance().strToArray(names);
				List list = memberDao.getMemberIdsByAccount(namesArr, organId);
				
				if (list != null) {
					code = "1";
					text = list.toString();
				} else {
					text = Tips.NULLID.getText();
				}
			}
			
			ret.put("code", code);
			ret.put("text", text);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(LogUtils.getInstance().getErrorInfoFromException(e));
		}
		
		return ret.toString();
	}
	
	@Override
	public String isUsedPic(String userId, String picName) {
		JSONObject jo = new JSONObject();
		
		logger.info("picName: " + picName);
		
		try {
			int userIdInt = Integer.parseInt(userId);
			boolean used = memberDao.isUsedPic(userIdInt, picName);
			jo.put("code", 1);
			jo.put("text", used);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(LogUtils.getInstance().getErrorInfoFromException(e));
		}
		
		return jo.toString();
	}
	
	@Override
	public String getMemberIdForAccount(String account, int organId) {
		JSONObject jo = new JSONObject();
		
		logger.info("account: " + account);
		try {
			int id = memberDao.getMemberIdForAccount(account, organId);
			jo.put("code", 1);
			jo.put("text", id);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(LogUtils.getInstance().getErrorInfoFromException(e));
		}
		return jo.toString();
	}
	
	@Override
	public String getMultipleMemberForIds(String ids) {
		JSONObject jo = new JSONObject();
		
		logger.info("ids: " + ids);
		try {
			String[] idArr = StringUtils.getInstance().strToArray(ids);
			int len = idArr.length;
			Integer[] idIntArr = new Integer[len];
			
			for(int i = 0; i < len; i++) {
				idIntArr[i] = Integer.parseInt(idArr[i]);
			}
			
			List<TMember> list = memberDao.getMultipleMemberForIds(idIntArr);
			List<JSONObject> lj = new ArrayList<JSONObject>();
			
 			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					JSONObject tm = JSONUtils.getInstance().modelToJSONObj(list.get(i));
					tm.remove("password");
					lj.add(tm);
				}
 			} else {
 				logger.warn("list is null");
 			}
 			
			if (list != null) {
				jo.put("code", 1);
				jo.put("text", lj.toString());
			} else {
				jo.put("code", 0);
				jo.put("text", Tips.FAIL.getText());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(LogUtils.getInstance().getErrorInfoFromException(e));
		}
		return jo.toString();
	}
	
	@Override
	public String getMemberForId(String userId) {
		JSONObject jo = new JSONObject();
		
		logger.info("userId: " + userId);
		try {
			int userIdInt = Integer.parseInt(userId);
			TMember tm = memberDao.getMemberForId(userIdInt);
			if (tm != null) {
				JSONObject j = JSONUtils.getInstance().modelToJSONObj(tm);
				j.remove("password");
				jo.put("code", 1);
				jo.put("text", j.toString());
			} else {
				jo.put("code", 0);
				jo.put("text", Tips.FAIL.getText());
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(LogUtils.getInstance().getErrorInfoFromException(e));
		}
		
		return jo.toString();
	}
	
	@Override
	public String getLimitMemberIds(String mapMax, int organId) {
		JSONObject jo = new JSONObject();
		
		logger.info("mapMax: " + mapMax);
		try {
			int mapMaxInt = Integer.parseInt(mapMax);
			List<TMember> tm = memberDao.getLimitMemberIds(mapMaxInt, organId);
			List<JSONObject> ret = new ArrayList<JSONObject>();
			
			if (tm != null) {
				for(int i = 0; i < tm.size(); i++) {
					ret.add(JSONUtils.getInstance().modelToJSONObj(tm.get(i)));
				}
				jo.put("code", 1);
				jo.put("text", ret.toString());
			} else {
				jo.put("code", 0);
				jo.put("text", Tips.FAIL.getText());
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(LogUtils.getInstance().getErrorInfoFromException(e));
		}
		
		return jo.toString();
	}
	

	@Override
	public String getMemberParam(String id, String ps) {
		JSONObject jo = new JSONObject();
		String code = "0";
		String text = null;
		
		logger.info("id: " + id + ", ps: " + ps);
		
		try {
			if (!StringUtils.getInstance().isBlank(id) && !StringUtils.getInstance().isBlank(ps)) {
				id = StringUtils.getInstance().replaceChar(id, "[", "");
				id = StringUtils.getInstance().replaceChar(id, "]", "");
				id = StringUtils.getInstance().replaceChar(id, "\"", "");
				String[] pss = StringUtils.getInstance().strToArray(ps);

				List memList = memberDao.getMemberParam(id, pss);
				JSONArray ja = new JSONArray();
				
				if (memList != null) {
					code = "1";
					for(int i = 0; i < memList.size(); i++) {
						JSONObject t = new JSONObject();
						Object[] o = (Object[]) memList.get(i);
						t.put("userID", o[0]);
						for(int k = 1; k < pss.length; k++) {
							t.put(pss[k], o[k] == null ? "" : o[k]);
						}
						ja.add(t);
					}
					text = ja.toString();
				} else {
					logger.warn("memList is null");
					text = Tips.FAIL.getText();
				}
			}
			
			jo.put("code", code);
			jo.put("text", text);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(LogUtils.getInstance().getErrorInfoFromException(e));
		}
		
		return jo.toString();
	}

	@Override
	public TMember getSuperAdmin(String account, String userpwd, int organId) {
		TMember memeber = null;

		logger.info("account: " + account + ", userpwd: " + userpwd);
		
		try {
			memeber = memberDao.getSuperAdmin(account, userpwd, organId);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(LogUtils.getInstance().getErrorInfoFromException(e));
		}
		return memeber;
	}
	
	@Override
	public String logicDelMemberByUserIds(String userids) {
		JSONObject jo = new JSONObject();
		logger.info("userids: " + userids);
		
		try {
			if (StringUtils.getInstance().isBlank(userids)) {
				jo.put("code", 0);
				jo.put("text", Tips.WRONGPARAMS.getText());
			} else {
				userids = StringUtils.getInstance().replaceChar(userids, "\"", "");
				userids = StringUtils.getInstance().replaceChar(userids, "[", "");
				userids = StringUtils.getInstance().replaceChar(userids, "]", "");
				String[] idArr = userids.split(",");
				List<String> alDelIds = new ArrayList<String>();
				
				String isLogic = PropertiesUtils.getStringByKey("del.logic");
				isLogic = StringUtils.getInstance().isBlank(isLogic) ? "1" : isLogic;
				
				int ret = memberDao.logicDelMemberByUserIds(userids, isLogic);
				Integer[] alIds = null;
				StringBuilder delIds = new StringBuilder();
				
				if (ret != idArr.length) {
					List<String> noDelIds = memberDao.getNotDelIds(userids, isLogic);
					
					if (noDelIds != null) {
						for(int i = 0; i < idArr.length; i++) {
							if (!noDelIds.contains(idArr[i])) {
								alDelIds.add(idArr[i]);
							}
						}
					}

					alIds = new Integer[alDelIds.size()];
			
					for (int i = 0; i < alDelIds.size(); i++) {
						alIds[i] = Integer.parseInt(alDelIds.get(i));
						delIds.append(alDelIds.get(i));
						if (i < alDelIds.size() - 1) {
							delIds.append(",");
						}
					}
					userids = delIds.toString();
				} else {
					alIds = new Integer[idArr.length];
					for (int i = 0; i < idArr.length; i++) {
						alIds[i] = Integer.parseInt(idArr[i]);
					}
				}
			
				int ret1 = branchMemberDao.delRelationByIds(userids, isLogic);
				
				//更新管理者
				if (ret1 > 0) {
					List<TBranch> branchIds = branchDao.getBranchByMangerId(alIds);
					StringBuilder bids = new StringBuilder();
					if (branchIds != null) {
						int len = branchIds.size();
						for(int i = 0; i < len; i++) {
							bids.append(branchIds.get(i).getId());
							if (i < len - 1) {
								bids.append(",");
							}
						}
						branchDao.update("update TBranch set managerId=0 where id in (" + bids.toString()+ ")");
					}
				}
				
				int ret2 = memberRoleDao.deleteRelationByIds(userids, isLogic);
				int ret3 = userValidDao.deleteRelationByIds(userids, isLogic);
				int ret4 = cutLogoTempDao.deleteRelationByIds(userids, isLogic);
					
				JSONObject params = new JSONObject();
				params.put("userIds", userids);
				params.put("isLogic", isLogic);

				String protocol = PropertiesUtils.getStringByKey("im.protocol");
				String host = PropertiesUtils.getStringByKey("im.host");
				String sys = PropertiesUtils.getStringByKey("im.sys");
				String urlStr = protocol + "://" + host + "/" + sys + "/";

				String result = HttpRequest.getInstance().sendPost(
						SysInterface.DELBYMEMIDS.getName(), params, urlStr, host);
				
				jo.put("code", 1);
				jo.put("text", Tips.OK.getText());
			}
		} catch(Exception e) {
			logger.error(LogUtils.getInstance().getErrorInfoFromException(e));
			e.printStackTrace();
		}
		return jo.toString();
	}
	

	@Override
	public String exportsMember(int organId, String realPath) {
		List organ = branchDao.getOrgan(organId);
		
		if (organ != null && organ.size() > 0) {
			Object[] o = (Object[]) organ.get(0);
			String organCode = String.valueOf(o[2]);
			List<TMember> memberList = memberDao.getAllMemberInfo(organId);
			
			if (memberList != null && memberList.size() > 0) {
				ArrayList<String[]> exportMemList = new ArrayList<String[]>();
				
				exportMemList.add(new String[]{"0", "0", o[1] + "-成员表"});
				//生成标题
				exportMemList.add(new String[]{"0", "1", "ID"});
				exportMemList.add(new String[]{"1", "1", "账号"});
				exportMemList.add(new String[]{"2", "1", "姓名"});
				exportMemList.add(new String[]{"3", "1", "拼音"});
				exportMemList.add(new String[]{"4", "1", "工号"});
				exportMemList.add(new String[]{"5", "1", "性别"});
				exportMemList.add(new String[]{"6", "1", "生日"});
				exportMemList.add(new String[]{"7", "1", "头像"});
				exportMemList.add(new String[]{"8", "1", "Email"});
				exportMemList.add(new String[]{"9", "1", "手机"});
				exportMemList.add(new String[]{"10", "1", "电话"});
				exportMemList.add(new String[]{"11", "1", "地址"});
				exportMemList.add(new String[]{"12", "1", "描述"});
				
				int line = 2;
				
				for(int i = 0; i < memberList.size(); i++) {
					TMember tb = memberList.get(i);
					String lineStr = String.valueOf(line);
					exportMemList.add(new String[]{"0", lineStr, String.valueOf(tb.getId())});
					exportMemList.add(new String[]{"1", lineStr, String.valueOf(tb.getAccount())});
					exportMemList.add(new String[]{"2", lineStr, tb.getFullname()});
					exportMemList.add(new String[]{"3", lineStr, tb.getAllpinyin()});
					exportMemList.add(new String[]{"4", lineStr, tb.getWorkno()});
					exportMemList.add(new String[]{"5", lineStr, tb.getSex().equals(1) ? "男" : "女"});
					exportMemList.add(new String[]{"6", lineStr, tb.getBirthday()});
					exportMemList.add(new String[]{"7", lineStr, tb.getLogo()});
					exportMemList.add(new String[]{"8", lineStr, tb.getEmail()});
					exportMemList.add(new String[]{"9", lineStr, tb.getMobile()});
					exportMemList.add(new String[]{"10", lineStr, tb.getTelephone()});
					exportMemList.add(new String[]{"11", lineStr, tb.getAddress()});
					exportMemList.add(new String[]{"12", lineStr, tb.getIntro()});
					line++;
				}
				
				String fileName = "exports" + PropertiesUtils.getStringByKey("dir.seperate") + organCode + "-Member-" + TimeGenerator.getInstance().formatNow("yyyyMMddhh") + ".xls";
				String fileAllName = realPath + fileName;
				
				try {
					OutputStream os = new FileOutputStream(fileAllName);
					XlsUtils.getInstance().createTitleExcel("成员数据", 13, exportMemList, os);
					if (FileUtil.isExists(fileAllName)) {
						return fileName;
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (WriteException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	@Override
	public String exportsMember2(int organId, String realPath) {
		List organ = branchDao.getOrgan(organId);
		
		if (organ != null && organ.size() > 0) {
			Object[] o = (Object[]) organ.get(0);
			String organCode = String.valueOf(o[2]);
			List<TMember> memberList = memberDao.getExportsMember(organId);
			JSONArray ja = new JSONArray();
			
			if (memberList != null && memberList.size() > 0) {
				int len = memberList.size();
				StringBuilder sb = new StringBuilder();
				
				for(int i = 0; i < len; i++) {
					TMember t = memberList.get(i);
					JSONObject jo = new JSONObject();
					jo.put("mobile", isBlank(t.getMobile()));
					jo.put("name", isBlank(t.getFullname()));
					jo.put("workno", isBlank(t.getWorkno()));
					jo.put("sex", StringUtils.getInstance().isBlank(t.getSex()) ? "" : (t.getSex().equals("1") ? "男" : "女"));
					jo.put("telephone", isBlank(t.getTelephone()));
					jo.put("email", isBlank(t.getEmail()));
					jo.put("id", t.getId());
					ja.add(jo);
					sb.append(t.getId());
					if (i < len - 1) {
						sb.append(",");
					}
				}
				
				List bmList = branchMemberDao.getBranchMemberByMemberIds(sb.toString());
				
				//组合职位，部门
				if (bmList != null && bmList.size() > 0) {
					int bmLen = bmList.size();
					Map<String, Object[]> bmMap = new HashMap<String, Object[]>();
					
					//去除重复
					for(int i = 0; i < bmLen; i++) {
						Object[] jo = (Object[])bmList.get(i);
						String id = String.valueOf(jo[0]);
						if(!bmMap.containsKey(id)) {
							bmMap.put(id, jo);
						} else {
							if (String.valueOf(jo[3]).equals("1")) {
								bmMap.remove(id);
								bmMap.put(id, jo);
							}
						}
					}
					
					for (int i = 0; i < len; i++) {
						JSONObject jo = ja.getJSONObject(i);
						for(Entry<String, Object[]> m: bmMap.entrySet()){
							String id = m.getKey();
							Object[] bmJo = m.getValue();
							if (jo.getString("id").equals(id)) {
								String bname = String.valueOf(bmJo[2]);
								String pname = String.valueOf(bmJo[1]);
								String master = String.valueOf(bmJo[4]);
								
								bname = isBlank(bname);
								pname = isBlank(pname);
								master = StringUtils.getInstance().isBlank("master") ? "0" : master;
								
								jo.put("bname", bname);
								jo.put("pname", pname);
								jo.put("master", master);
								bmMap.remove(id);
								break;
							}
						}
					}
					for(int i = 0; i < len; i++) {
						JSONObject jo = ja.getJSONObject(i);
						for(int j = 0; j < len; j++) {
							JSONObject jo1 = ja.getJSONObject(j);
							if (jo.getString("master").equals(jo1.getString("id"))) {
								jo.remove("master");
								jo.put("master", jo1.getString("name"));
								break;
							}
						}
					}
				}
				
				ArrayList<String[]> exportMemList = new ArrayList<String[]>();
				exportMemList.add(new String[]{"0", "0", "手机"});
				exportMemList.add(new String[]{"1", "0", "姓名"});
				exportMemList.add(new String[]{"2", "0", "工号"});
				exportMemList.add(new String[]{"3", "0", "性别"});
				exportMemList.add(new String[]{"4", "0", "属性部门"});
				exportMemList.add(new String[]{"5", "0", "部门领导"});
				exportMemList.add(new String[]{"6", "0", "职务"});
				exportMemList.add(new String[]{"7", "0", "座机"});
				exportMemList.add(new String[]{"8", "0", "邮箱"});
				
				int line = 1;
				
				for(int i = 0; i < ja.size(); i++) {
					JSONObject jo = ja.getJSONObject(i);
					String lineStr = String.valueOf(line);
					exportMemList.add(new String[]{"0", lineStr, jo.getString("mobile")});
					exportMemList.add(new String[]{"1", lineStr, jo.getString("name")});
					exportMemList.add(new String[]{"2", lineStr, jo.getString("workno")});
					exportMemList.add(new String[]{"3", lineStr, jo.getString("sex")});
					exportMemList.add(new String[]{"4", lineStr, jo.getString("bname")});
					exportMemList.add(new String[]{"5", lineStr, jo.getString("master")});
					exportMemList.add(new String[]{"6", lineStr, jo.getString("pname")});
					exportMemList.add(new String[]{"7", lineStr, jo.getString("telephone")});
					exportMemList.add(new String[]{"8", lineStr, jo.getString("email")});
					line++;
				}
				
				String fileName = "exports" + PropertiesUtils.getStringByKey("dir.seperate") + organCode + "-Member-" + TimeGenerator.getInstance().formatNow("yyyyMMddhh") + ".xls";
				String fileAllName = realPath + fileName;
				
				try {
					OutputStream os = new FileOutputStream(fileAllName);
					XlsUtils.getInstance().createSimpleExcel("成员数据", exportMemList, os);
					if (FileUtil.isExists(fileAllName)) {
						return fileName;
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (WriteException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return null;
	}
	
	@Override
	public void updateBySql(int organId, String address) {
		memberDao.update("update TMember set address='" + address + "' where organId=" + organId);
	}
	
	private String isBlank(Object o) {
		return (o == null || o.equals("null")) ? "" : o + "";
	}

	private TextCodeDao textCodeDao;
	private MemberDao memberDao;
	private BranchMemberDao branchMemberDao;
	private MemberRoleDao memberRoleDao;
	private UserValidDao userValidDao;
	private CutLogoTempDao cutLogoTempDao;
	private BranchDao branchDao;

	public void setBranchMemberDao(BranchMemberDao branchMemberDao) {
		this.branchMemberDao = branchMemberDao;
	}

	public void setMemberDao(MemberDao memberDao) {
		this.memberDao = memberDao;
	}

	public TextCodeDao getTextCodeDao() {
		return textCodeDao;
	}

	public void setTextCodeDao(TextCodeDao textCodeDao) {
		this.textCodeDao = textCodeDao;
	}

	public MemberDao getMemberDao() {
		return memberDao;
	}

	public MemberRoleDao getMemberRoleDao() {
		return memberRoleDao;
	}

	public void setMemberRoleDao(MemberRoleDao memberRoleDao) {
		this.memberRoleDao = memberRoleDao;
	}

	public UserValidDao getUserValidDao() {
		return userValidDao;
	}

	public void setUserValidDao(UserValidDao userValidDao) {
		this.userValidDao = userValidDao;
	}

	public BranchMemberDao getBranchMemberDao() {
		return branchMemberDao;
	}

	public void setCutLogoTempDao(CutLogoTempDao cutLogoTempDao) {
		this.cutLogoTempDao = cutLogoTempDao;
	}

	public void setBranchDao(BranchDao branchDao) {
		this.branchDao = branchDao;
	}

}
