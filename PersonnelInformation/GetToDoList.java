package com.ysk.action.PersonnelOperate.PersonnelInformation;

import jcx.jform.hproc;

public class GetToDoList extends hproc {
	public String action(String value) throws Throwable {
		// 可自定HTML版本各欄位的預設值與按鈕的動作
		// 傳入值 value
		String html="<iframe width=500 height=500 src=\""+getToDoListURL()+"&target=hr_flow_main"+"\" name=ToDoList></iframe>";
		setValue("text2",html);
		//this.addScript("Ext.MessageBox.hide();");
		
		return value;
	}

	public String getInformation() {
		return "---------------getToDoList(getToDoList).html_action()----------------";
	}
}
