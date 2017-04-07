package com.organ.action.appinfoconfig;

import java.io.InputStream;

import javax.servlet.ServletException;

import org.json.JSONException;
import org.json.JSONObject;

import com.organ.common.BaseAction;
import com.organ.dao.appinfoconfig.AppInfoConfigDao;
import com.organ.service.appinfoconfig.AppInfoConfigService;

/**
 * 应用信息配置
 * 
 * @author Lmy
 * 
 */
public class AppInfoConfigAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3357825063940018758L;
	private AppInfoConfigService appInfoConfigService;

	public void setAppInfoConfigService(
			AppInfoConfigService appInfoConfigService) {
		this.appInfoConfigService = appInfoConfigService;
	}

	public String getAppInfo() throws ServletException, JSONException {
		String pagesize = this.request.getParameter("pagesize");
		String pageindex = this.request.getParameter("pageindex");

		Integer intpagesize = pagesize == null ? null : Integer
				.parseInt(pagesize);
		Integer intpageindex = pageindex == null ? null : Integer
				.parseInt(pageindex);
		boolean flag = false;
		String result = null;
		try {
			result = appInfoConfigService.getAppInfo(intpagesize, intpageindex);

		} catch (Exception e) {
			e.printStackTrace();
		}
		/*
		 * JSONObject jsonObject = new JSONObject(); if (flag) {
		 * jsonObject.put("code", 1 + ""); jsonObject.put("text", "更新成功");
		 * }else{ jsonObject.put("code", 0 + ""); jsonObject.put("text",
		 * "更新失败"); } returnToClient(jsonObject.toString());
		 */
		returnToClient(result);
		return "text";
	}

	/**
	 * 添加应用信息
	 * 
	 * @return
	 * @throws ServletException
	 * @throws JSONException
	 */
	public String updateAppInfo() throws ServletException, JSONException {
		// int appId,String secert,String callbackurl,long apptime,String
		// appname,int isopen
		String appId = this.request.getParameter("appId");
		String secert = this.request.getParameter("secert");
		String callbackurl = this.request.getParameter("callbackurl");
		String appname = this.request.getParameter("appname");
		String isopen = this.request.getParameter("isopen");
		Integer intappId = appId == null ? null : Integer.parseInt(appId);
		Integer intisopen = isopen == null ? null : Integer.parseInt(isopen);
		boolean falg = false;
		try {
			String result = appInfoConfigService.updatePriv(intappId, secert,
					callbackurl, appname, intisopen);
			if ("".equals(result) && null == result) {
				falg = false;
			} else {
				falg = true;
			}
		} catch (Exception e) {
			falg = false;
			// TODO: handle exception
			e.printStackTrace();
		}
		JSONObject jsonObject = new JSONObject();
		if (falg) {
			jsonObject.put("code", 1 + "");
			jsonObject.put("text", "更新成功");

		} else {
			jsonObject.put("code", 0 + "");
			jsonObject.put("text", "更新失败");

		}
		returnToClient(jsonObject.toString());
		return "text";

	}

	/**
	 * 删除应用
	 * 
	 * @return
	 * @throws ServletException
	 * @throws JSONException
	 */
	public String DelApp() throws ServletException, JSONException {
		String id = this.request.getParameter("AppId");
		Integer intid = id == null ? null : Integer.parseInt(id);
		boolean falg = false;
		String result = null;
		try {
			result = appInfoConfigService.DelApp(intid);
		
			if ("".equals(result) && null == result) {
				falg = false;
			} else {
				falg = true;
			}
		} catch (Exception e) {
			falg = false;
			// TODO: handle exception
			e.printStackTrace();
		}
		JSONObject jsonObject = new JSONObject();
		if (falg) {
			jsonObject.put("code", 1 + "");
			jsonObject.put("text", "刪除成功");

		} else {
			jsonObject.put("code", 0 + "");
			jsonObject.put("text", "刪除失败");

		}
		returnToClient(jsonObject.toString());
		return "text";
	}
	
	public String EditApp() throws ServletException,JSONException {
		String id = this.request.getParameter("id");
		String appId = this.request.getParameter("appId");
		String secert = this.request.getParameter("secert");
		String callbackurl = this.request.getParameter("callbackurl");
		String apptime = this.request.getParameter("apptime");
		String appname = this.request.getParameter("appname");
		String isopen = this.request.getParameter("isopen");
		Integer intappId = appId == null ? null : Integer.parseInt(appId);
		Integer intisopen = isopen == null ? null : Integer.parseInt(isopen);
		Long longapptime = apptime == null ? null : Long.parseLong(apptime);
		Integer intid = id == null ? null : Integer.parseInt(id);
		boolean flag = false;
		try {
			String result = appInfoConfigService.EditApp(intid, intappId, secert, callbackurl, longapptime, appname, intisopen);
			if ("".equals(result) && null == result) {
				flag = false;
			} else {
				flag = true;
			}
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}
		JSONObject jsonObject = new JSONObject();
		if (flag) {
			jsonObject.put("code", 1 + "");
			jsonObject.put("text", "编辑应用信息成功");

		} else {
			jsonObject.put("code", 0 + "");
			jsonObject.put("text", "编辑应用信息失败");

		}
		returnToClient(jsonObject.toString());
		return "text";
	}
	
	public String SearchAppName() throws ServletException,JSONException{
		String pagesize = this.request.getParameter("pagesize");
		String pageindex = this.request.getParameter("pageindex");
		String AppName = this.request.getParameter("AppName");
		Integer intpagesize = pagesize == null ? null : Integer
				.parseInt(pagesize);
		Integer intpageindex = pageindex == null ? null : Integer
				.parseInt(pageindex);
		String result = appInfoConfigService.SearchApp(AppName, intpagesize, intpageindex);
		returnToClient(result);
		return "text";
	}

}
