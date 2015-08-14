package com.ysk.action.PersonnelOperate.PersonnelInformation;

import jcx.db.talk;
import jcx.jform.hproc;
import jcx.util.convert;
import jcx.util.datetime;
import jcx.util.operation;

public class user_info extends hproc{
	public String action(String value)throws Throwable{
		String CUST = (String)get("SYSTEM.CUST");
		if(CUST==null) CUST = "NONE";
		if(CUST.trim().equals("CUS000000007")){
			setVisible("text13",false);
			setVisible("text15",false);
			setVisible("YSPECHR",false);
			setVisible("Y_AMT",false);
			setVisible("text17",false);
			setVisible("R_AMT",false);
			setVisible("text5",false);
			setVisible("CUR",false);
		}
		String ISNULL = (String)get("SYSTEM.ISNULL");
		if(ISNULL==null) ISNULL = "";
		String EMPID=getUser();
		String CPNYID="";
		//                            0                        1                                          2      3      4      5    6       7
		String sql="select HECNAME,(select DEP_NAME from HRUSER_DEPT_BAS where DEP_NO=HRUSER.DEPT_NO) DEPT_NO,INADATE,PHOTO,CPNYID,UTYPE,(select FUNCT from POSSET where POSNO=HRUSER.POSNO) from HRUSER where ";
		if(!EMPID.equals("")) sql+=" EMPID='"+EMPID+"' ";
		talk t=getTalk();
		String s[][]=t.queryFromPool(sql);
		setValue("EMPID",EMPID);
		setValue("HECNAME",s[0][0]);
		setValue("DEPT_NO",s[0][1]);
		String INADATE=s[0][2];
		String AIC_INADATE=s[0][2];
		String UTYPE=s[0][5];
		if(s[0][3].equals("")) s[0][3]="unknow.gif";
		setValue("PHOTO",s[0][3]);
		setValue("POSNO",s[0][6]);
		CPNYID=s[0][4];

		setValue("INADATE",convert.FormatedDate(INADATE,"/"));
		String UNIT09[][] = t.queryFromPool("select UNIT,SET5 from HKIND where HCODE='09'");
		String U09 = "A";
		if(UNIT09.length!=0){
			U09 = UNIT09[0][0].trim();
		}
		String UNIT15[][] = t.queryFromPool("select UNIT,SET5 from HKIND where HCODE='15'");
		String U15 = "A";
		if(UNIT15.length!=0){
			U15 = UNIT15[0][0].trim();
		}
		String YSPECHR = "0";
		String YNRESTHR = "0";
		String Y_AMT = "0";
		String OVERHOUR = "0";
		String NOTREST = "0";
		String R_AMT = "0";
		String TODAY = datetime.getToday("YYYYmmdd");
		sql = "select isnull(YSPECHR,0),isnull(YNRESTHR,0),isnull((case when EDATE1<'"+TODAY+"' then 0 else NRESTHR end),0),isnull(NNRESTHR,0),isnull(YRESTHR,0),isnull((case when EDATE1<'"+TODAY+"' then 0 else NRESTRHR end),0) from YSPECR "
//		+ " where YY = '"+TODAY.substring(0,4)+"' and EMPID = '"+convert.ToSql(EMPID.trim())+"' and CPNYID = '"+convert.ToSql(CPNYID.trim())+"'";
//		+ " where YY = '"+TODAY.substring(0,4)+"' and EMPID = '"+convert.ToSql(EMPID.trim())+"' ";
		+ " where isnull(SDATE,'')<='"+TODAY+"' and isnull(EDATE,'')>='"+TODAY+"' and EMPID = '"+convert.ToSql(EMPID.trim())+"' ";
		String UNVCS = "0";
		String ret[][] = t.queryFromPool(sql);
		if ( ret.length != 0 ) {
			UNVCS = operation.floatAdd(ret[0][4].trim(),ret[0][5].trim(),1);
			YSPECHR = ret[0][0].trim();
			YNRESTHR = ret[0][1].trim();
//			YSPECHR = operation.floatAdd(YSPECHR,"0",1);
			YSPECHR = operation.floatAdd(YSPECHR,ret[0][2].trim(),1);
			YNRESTHR = operation.floatAdd(YNRESTHR,ret[0][3].trim(),1);
		}

		/*
		sql = "select PNO,REPRICE,(select sum(cast(AMT as numeric(9,2))) from REST_LOG where PNO1=OVERTIME.PNO) from OVERTIME where CPNYID='"+CPNYID+"' and EMPID='"+EMPID+"' and RESERVE_DAY>='"+TODAY+"' ";
//		+ " where YYMM <= '"+TODAY.substring(0,6)+"' and EMPID = '"+convert.ToSql(EMPID.trim())+"' and CPNYID = '"+convert.ToSql(CPNYID.trim())+"' and KEEPYM>='"+TODAY.substring(0,6)+"'";
		ret = t.queryFromPool(sql);
		if ( ret.length != 0 ) {
			OVERHOUR = ret[0][0].trim();
			NOTREST = ret[0][1].trim();
		}
		*/


		
		String sqlw1 = "";
		String sqlw2 = "";
		String sqlw3 = "";
		sqlw1 = "select sum(isnull(AMT,0)) from VACATION "
			+ " where EMPID = '"+convert.ToSql(EMPID.trim())+"' and CPNYID = '"+convert.ToSql(CPNYID.trim())+"' and SDATE like '"+TODAY.substring(0,4)+"%' and HCODE ='09' and isnull(APPRDATE,'"+ISNULL+"') = '"+ISNULL+"'"
			+ " group by HCODE order by HCODE";	

/*
		sqlw2 = "select sum(isnull(AMT,0)) from VACATION "
			+ " where EMPID = '"+convert.ToSql(EMPID.trim())+"' and CPNYID = '"+convert.ToSql(CPNYID.trim())+"' and SDATE like '"+TODAY.substring(0,4)+"%' and HCODE ='15' and isnull(APPRDATE,'"+ISNULL+"') = '"+ISNULL+"'"
			+ " group by HCODE order by HCODE";	
		sqlw3="select sum(isnull(NOTREST,0)) from REST where CPNYID='"+convert.ToSql(CPNYID.trim())+"' and EMPID='"+convert.ToSql(EMPID.trim())+"' and KEEPYM='"+TODAY.substring(0,6)+"' ";
*/
		String CUR = "0.0";
		String retw1[][] = t.queryFromPool(sqlw1);
//		String retw2[][] = t.queryFromPool(sqlw2);
//		String retw3[][] = t.queryFromPool(sqlw3);
//		if(retw3.length!=0){
//			CUR = retw3[0][0].trim();
//			if(CUR.trim().length()==0) CUR = "0.0";
//		}
		if(retw1.length!=0){
			UNVCS = operation.floatAdd(UNVCS.trim(),retw1[0][0].trim(),1);
//			Y_AMT = operation.floatSubtract(YNRESTHR,retw1[0][0].trim(),1);
		}
		Y_AMT = operation.floatSubtract(YSPECHR,UNVCS.trim(),1);
//		if(retw2.length!=0) R_AMT = operation.floatSubtract(NOTREST,retw2[0][0].trim(),1);
//		else R_AMT = NOTREST;
		String UNI09 = "小時";
		if(U09.trim().equals("B")) UNI09 = "天";
		String UNI15 = "小時";
		if(U15.trim().equals("B")) UNI15 = "天";
//		setValue("CUR",CUR+UNI15);
		setValue("YSPECHR",YSPECHR+UNI09);
		setValue("Y_AMT",Y_AMT+UNI09);
//		setValue("R_AMT",R_AMT+UNI15);
		
		//960320 cooper
		//String html="<iframe width=550 height=200 src=\""+getToDoListURL()+"\" name=ToDoList></iframe>";
		//String html="<iframe width=250 height=240 src=\""+getToDoListURL()+"&target=hr_flow_main"+"\" name=ToDoList></iframe>";
		//setValue("text2",html);
		return value;
	}
	public String getInformation(){
		return "---------------FORM_STATUS(FORM_STATUS).html_action()----------------";
	}
}
