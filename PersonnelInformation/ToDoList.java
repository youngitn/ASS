package com.ysk.action.PersonnelOperate.PersonnelInformation;

import jcx.jform.hproc;

public class ToDoList extends hproc {
	public String action(String value) throws Throwable {
		// 可自定HTML版本各欄位的預設值與按鈕的動作
		// 傳入值 value
		this.addScript("importScript('/js/YSKOA001.js')");
		return value;
	}

	public String getInformation() {
		return "---------------text2(\u6b63\u5728\u53d6\u5f97\u5f85\u7c3d\u6838\u8cc7\u6599...).html_action()----------------";
	}
}
