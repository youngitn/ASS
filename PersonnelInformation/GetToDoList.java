package com.ysk.action.PersonnelOperate.PersonnelInformation;

import jcx.jform.hproc;

public class GetToDoList extends hproc {
	public String action(String value) throws Throwable {
		// �i�۩wHTML�����U��쪺�w�]�ȻP���s���ʧ@
		// �ǤJ�� value
		String html="<iframe width=500 height=500 src=\""+getToDoListURL()+"&target=hr_flow_main"+"\" name=ToDoList></iframe>";
		setValue("text2",html);
		//this.addScript("Ext.MessageBox.hide();");
		
		return value;
	}

	public String getInformation() {
		return "---------------getToDoList(getToDoList).html_action()----------------";
	}
}
