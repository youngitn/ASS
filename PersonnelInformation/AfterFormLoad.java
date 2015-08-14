package com.ysk.action.PersonnelOperate.PersonnelInformation;

import com.ysk.form.BaseForm; 
import com.ysk.service.ModuleAfterFormLoadService;
import com.ysk.util.Log;
import com.ysk.util.LogUtil;   
   
public class AfterFormLoad extends ModuleAfterFormLoadService {
	
	Log log = LogUtil.getLog(this.getClass());
	
	public String action(String value) throws Throwable {
		// 可自定HTML版本各欄位的預設值與按鈕的動作
		// 傳入值 value
		this.addScript("showLoginUserName('" +  this.getName(this.getUser()) +"');");
//		this.configureTopMenu();
//		this.configureLeftMenu();
		
		String fromLink = this.getName().trim();
		//log.methodDebug(BaseForm.FROM_LINK_PARAMETER + " = " + fromLink, Thread.currentThread().getStackTrace());
		String buttonid = this.getParameter(BaseForm.BUTTON_ID_PARAMETER);
		//log.methodDebug(BaseForm.BUTTON_ID_PARAMETER + " = " + buttonid, Thread.currentThread().getStackTrace());
		if ("button1".equals(buttonid)) {
			this.importCss("/css/extGrid.css");
		}
		return value;
	}

	public String getInformation() {
		return "---------------A1.\u500b\u4eba\u8cc7\u8a0a\u5340.loadPropram()----------------";
	}
}
