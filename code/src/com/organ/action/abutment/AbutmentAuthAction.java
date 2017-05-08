package com.organ.action.abutment;

import javax.servlet.ServletException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.sf.json.JSONObject;


import com.googlecode.sslplugin.annotation.Secured;
import com.organ.common.BaseAction;
import com.organ.common.Tips;
import com.organ.service.auth.AppSecretService;
import com.organ.utils.JSONUtils;
import com.organ.utils.LogUtils;


public class AbutmentAuthAction extends BaseAction {

	private static final long serialVersionUID = 6187999207496183515L;
	private static final Logger logger = LogManager.getLogger(AbutmentAuthAction.class);
	
	
	public String validAppIdAndSecretAb() throws ServletException {
		String result = null;
		
		try {
			String params = getRequestDataByStream();
			logger.info(params);
			
			JSONObject jo = new JSONObject();
			boolean s = true;
			
			if (params == null) {
				s = false;
			} else {
				JSONObject p = JSONUtils.getInstance().stringToObj(params);
				if (!validParams(p)) {
					s = false;
				} else {
					String appId = p.getString("appId");
					String secret = p.getString("secret");
					result = appSecretService.getAppSecretByAppIdAndSecret(appId, secret);
				}
			}
			if (!s) {
				jo.put("code", 0);
				jo.put("text", Tips.WRONGPARAMS.getText());
				result = jo.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(LogUtils.getInstance().getErrorInfoFromException(e));
		}
		logger.info(result);
		returnToClient(result);
		return "text";
	}
	
	private AppSecretService appSecretService;

	public void setAppSecretService(AppSecretService appSecretService) {
		this.appSecretService = appSecretService;
	}
	
}