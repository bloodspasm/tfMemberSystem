package com.organ.action.sys;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.organ.common.BaseAction;
import com.organ.common.Constants;
import com.organ.common.Tips;
import com.organ.model.SessionUser;
import com.organ.model.TMember;
import com.organ.service.adm.PrivService;
import com.organ.service.member.MemberService;
import com.organ.utils.JSONUtils;
import com.organ.utils.MathUtils;
import com.organ.utils.PropertiesUtils;
import com.organ.utils.RongCloudUtils;
import com.organ.utils.StringUtils;
import com.organ.utils.TextHttpSender;
import com.organ.utils.TimeGenerator;

/**
 * 系统相关
 * @since jdk1.7
 * @author hao_dy
 *
 */
public class SystemAction extends BaseAction {
	
	private static final long serialVersionUID = -3901445181785461508L;
	private static final String LOGIN_ERROR_MESSAGE = "loginErrorMsg";
	private static final Logger logger = Logger.getLogger(SystemAction.class);
	
	/**
	 * 跳转登陆页面
	 * @return
	 * @throws Exception
	 */
	public String login() throws IOException, ServletException {
	
		if (getSessionUser() == null) {
			return "loginPage";
		} else {
			return "loginSuccess";
		}
	}
	
	/**
	 * 登陆验证
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public String afterLogin() throws IOException, ServletException {
		JSONObject result = new JSONObject();
		
		if (StringUtils.getInstance().isBlank(account)) {
			result.put("code", 0);
			result.put("text", Tips.NULLUSER.getText());
			returnToClient(result.toString());
			return "text";
		}

		/**
		 * 初始化
		 */
		/*if (account.equals("Administrator")) {
			int count = memberService.countMember();
			if (count > 1) {
				result.put("code", 0);
				result.put("text", Tips.NOTINIT.getText());
				returnToClient(result.toString());
				return "text";
			}
		}*/
		
		TMember member = memberService.searchSigleUser(account, userpwd);
		
		if(member == null) {
			result.put("code", 0);
			result.put("text", Tips.ERRORUSERORPWD.getText());
			returnToClient(result.toString());
			return "text";
		}
		
		logger.debug("The logining account is " + account);
		
		String userId = "" + member.getId();
		String name = member.getFullname();
		String token = null;
		String tokenMaxAge = PropertiesUtils.getStringByKey("db.tokenMaxAge");
		
		long tokenMaxAgeLong = 0;
		long firstTokenDate = 0;
		
		if (member.getCreatetokendate()!=null) {
			firstTokenDate = member.getCreatetokendate();
		}
		
		long now = TimeGenerator.getInstance().getUnixTime();
		
		if (tokenMaxAge != null && !"".equals(tokenMaxAge)) {
			tokenMaxAgeLong = Long.valueOf(tokenMaxAge);
		}
		
		if (StringUtils.getInstance().isBlank(member.getToken()) || (now - firstTokenDate) > tokenMaxAgeLong) {
			try {
				String domain = PropertiesUtils.getDomain();
				String uploadDir = PropertiesUtils.getUploadDir();
				String logo = member.getLogo();
				if(logo == null) logo = "PersonImg.png";
				
				String url = domain + uploadDir + logo;
				token = RongCloudUtils.getInstance().getToken(userId, name, url);
				memberService.updateUserTokenForId(userId, token);
			} catch (Exception e) {
				logger.error(e);
				e.printStackTrace();
			}
		} else {
			token = member.getToken();
		}
		
		logger.info(token);
		
		//设置用户session
		SessionUser su = new SessionUser();
		
		su.setId(member.getId());
		su.setAccount(member.getAccount());
		su.setFullname(member.getFullname());
		su.setToken(token);
		setSessionUser(su);
		
		/*
		//2.设置权限
		SessionPrivilege sp = new SessionPrivilege();
		ArrayList<JSONObject> ja = new ArrayList<JSONObject>();
		
		if (!account.equals("Administrator")) {
			List privList = privService.getRoleIdForId(member.getId());
			
			if (privList != null) {
				Iterator it = privList.iterator();
				
				while(it.hasNext()) {
					Object[] o = (Object[])it.next();
					JSONObject js = new JSONObject();
					js.put("privid", o[0]);
					js.put("priurl", o[1]);
					ja.add(js);
				}
			} 
		} else {
			ja = privService.getInitLoginPriv();
		}
	
		sp.setPrivilige(ja);
		setSessionAttribute(Constants.ATTRIBUTE_NAME_OF_SESSIONPRIVILEGE, sp);*/
		JSONObject text = JSONUtils.getInstance().modelToJSONObj(member);
		
		text.remove("password");
		text.remove("createtokendate");
		text.remove("groupmax");
		text.remove("groupuse");
		text.put("token", token);
		//text.put("priv", JSONUtils.getInstance().modelToJSONObj(sp));
		
		result.put("code", 1);
		result.put("text", text.toString());
		
		returnToClient(result.toString());  
		
		return "text";
	}

	/**
	 * 登出
	 * @return
	 * @throws Exception
	 */
	public String logOut() throws IOException, ServletException
	{
		request.getSession().removeAttribute(Constants.ATTRIBUTE_NAME_OF_SESSIONUSER);
		request.getSession().invalidate();
		return "loginPage";
	}
	
	/**
	 * 跳转修改密码页(仅web端使用)
	 * @return
	 * @throws Exception
	 */
	public String fogetPassword() throws ServletException {
		//request.getRequestDispatcher("/page/web/forgotpassword.jsp").forward(request, response);
		
		return "forgetpwd";
	}
	
	/**
	 * 中转短信平台
	 * @return
	 * @throws Exception
	 */
	public String requestText() throws IOException, ServletException {

		JSONObject text = new JSONObject();
		
		if (!StringUtils.getInstance().isBlank(phone)) {
			String dbCode = memberService.getTextCode(phone);
			String endText = PropertiesUtils.getStringByKey("code.endtext");
			String code = "";
			String context = "";
			
			if (dbCode == null || dbCode.equals("-1")) {
				int codeBit = StringUtils.getInstance().strToInt(PropertiesUtils.getStringByKey("code.bit"));
				code = String.valueOf(MathUtils.getInstance().getRandomSpecBit(codeBit));
				context = code + endText;
				memberService.saveTextCode(phone, code);
			} else {
				context = dbCode + endText;
			}
			
			//发送短信代码
			String sendText = TextHttpSender.getInstance().sendText(phone, context);
			
			if ("0".equals(sendText)) {
				text.put("code", 1);
				text.put("text", Tips.SENDTEXTS.getText());
			} else {
				text.put("code", 0);
				text.put("text", Tips.SENDERR.getText());
			}
		} else {
			text.put("code", 0);
			text.put("text", Tips.NULLPHONE.getText());
		}
		
		returnToClient(text.toString());
		
		return "text";
	}

	
	/**
	 * 验证短信(仅web端使用)
	 * @return
	 */
	public String testText() throws ServletException {
		
		JSONObject text = new JSONObject();
		
		if (StringUtils.getInstance().isBlank(phone)) {
			text.put("code", -1);
			text.put("text", Tips.NULLPHONE.getText());
		} else if (StringUtils.getInstance().isBlank(textcode)) {
			text.put("code", -1);
			text.put("text", Tips.NULLTEXTS.getText());
		} else {
			String dbCode = memberService.getTextCode(phone);
			
			if (dbCode != null && !dbCode.equals("-1") && dbCode.equals(textcode)) {
				text.put("code", 1);
				text.put("text", Tips.TRUETEXTS.getText());
			} else {
				text.put("code", 0);
				text.put("text", Tips.FAIL.getText());
			}
		}
		
		returnToClient(text.toString());
		
		return "text";
	}
	
	/**
	 * 验证旧密码
	 * @return
	 * @throws ServletException
	 */
	public String valideOldPwd() throws ServletException {
		JSONObject text = new JSONObject();
		
		if (!StringUtils.getInstance().isBlank(oldpwd)) {				//登陆后修改密码
			boolean validOldPwd = memberService.valideOldPwd(account, oldpwd);
			if (!validOldPwd) {
				text.put("code", 0);
				text.put("text", Tips.WRONGOLDPWD.getText());
			} else {
				text.put("code", 1);
				text.put("text", Tips.OK.getText());
			}
		} else { 
			text.put("code", 0);
			text.put("text", Tips.WRONGOLDPWD.getText());
		}
		
		returnToClient(text.toString());
		
		return "text";
	}
	
	/**
	 * 修改新密码
	 * @return
	 */
	public String newPassword() throws ServletException {
		JSONObject text = new JSONObject();
		
		System.out.println("newPassword: " + account);
		if (StringUtils.getInstance().isBlank(account)) {
			text.put("code", "0");
			text.put("text", Tips.NULLUSER.getText());
			returnToClient(text.toString());
			return "text";
		}
		
		boolean status = true;
		int flag = 0;
				
		if (!StringUtils.getInstance().isBlank(oldpwd)) {				//登陆后修改密码
			boolean validOldPwd = memberService.valideOldPwd(account, oldpwd);
			flag = 1;		//后台修改
			if (!validOldPwd) {
				text.put("code", -1);
				text.put("text", Tips.WRONGOLDPWD.getText());
				status = false;
			}
		} else { //忘记密码修改密码 app端(web端这里不传textcode,)
			if (!StringUtils.getInstance().isBlank(textcode)) {
				if (textcode == null || "".equals(textcode)) {
					text.put("code", -1);
					text.put("text", Tips.NULLTEXTS.getText());
					status = false;
				} else {
					String dbCode = memberService.getTextCode(phone);
					
					if (dbCode != null && !dbCode.equals("-1") && dbCode.equals(textcode)) {
						text.put("code", 1);
						text.put("text", Tips.TRUETEXTS.getText());
					} else {
						text.put("code", 0);
						text.put("text", Tips.FAIL.getText());
					}
				}
			}
		}
		
		if (status) {
			if (!newpwd.equals(comparepwd)) {
				request.setAttribute(LOGIN_ERROR_MESSAGE, Tips.FALSECOMPAREPWD.getText());
				return "fogetpwd";
			}
			
			boolean updateState = false;
			
			if (flag == 1) {
				updateState = memberService.updateUserPwdForAccount(account, newpwd);
			} else {
				updateState = memberService.updateUserPwdForPhone(phone, newpwd);
			}
			
			if (updateState == true) {
				text.put("code", "1");
				text.put("text", Tips.CHANGEPWDSUC.getText());
			} else {
				text.put("code", "0");
				text.put("text", Tips.CHANGEPWDFAIL.getText());
			}
		}
		returnToClient(text.toString());
		
		return "text";
	}
	
	/**
	 * 跳转组织信息
	 * @return
	 * @throws ServletException
	 */
	public String organInfo() throws ServletException {
		return "organInfo";
	}
	
	/**
	 * 群组管理 
	 * @return
	 * @throws ServletException
	 */
	public String groupManager() throws ServletException {
		return "groupManager";
	}
	
	/**
	 * 跳转组织结构
	 * @return
	 * @throws ServletException
	 */
	public String organFrame() throws ServletException {
		return "organFrame";
	}
	
	/**
	 * 高级设置 
	 * @return
	 * @throws ServletException
	 */
	public String highset() throws ServletException {
		return "highset";
	}
	
	private MemberService memberService;
	private PrivService privService;
	
	public void setMemberService(MemberService memberService) {
		this.memberService = memberService;
	}

	public void setPrivService(PrivService privService) {
		this.privService = privService;
	}

	private String account;
	private String userpwd;
	private String oldpwd;
	private String newpwd;
	private String textcode;
	private String comparepwd;
	private String dataSource;
	private String phone;
	private String token;
	private String organ;

	public void setOrgan(String organ) {
		this.organ = organ;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public void setUserpwd(String userpwd) {
		this.userpwd = userpwd;
	}

	public void setOldpwd(String oldpwd) {
		this.oldpwd = oldpwd;
	}

	public void setNewpwd(String newpwd) {
		this.newpwd = newpwd;
	}

	public void setTextcode(String textcode) {
		this.textcode = textcode;
	}

	public void setComparepwd(String comparepwd) {
		this.comparepwd = comparepwd;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}
