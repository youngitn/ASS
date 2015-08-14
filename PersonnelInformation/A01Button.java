package com.ysk.action.PersonnelOperate.PersonnelInformation;

import hr.common;

import java.util.Hashtable;
import java.util.*;
import com.ysk.dao.QanoteViewManager;

import jcx.db.talk;
import jcx.jform.hproc;
import jcx.util.convert;
import jcx.util.datetime;
//test  PreparedStatement;
import java.sql.*;
 //     
public class A01Button extends hproc {     
	public String action(String value) throws Throwable {
		//77888899999
		String Today = datetime.getToday("YYYYmmdd");
		String DATELIST = (String) get("SYSTEM.DATELIST");
		String CUST = (String) get("SYSTEM.CUST");
		if (CUST == null)
			CUST = "NONE"; 
		String VALIDATE = "VALIDATE1";
		if (CUST.trim().equals("CUS000000009")) {
			VALIDATE = "VALIDATE";
		}
		String ISNULL = (String) get("SYSTEM.ISNULL");
		if (ISNULL == null) 
			ISNULL = "";
		talk t = getTalk();
		String ButtonName = getName();
		String EMPID = getUser();
		// System.out.println("ButtonName==>"+ButtonName);
		if (ButtonName.trim().equals("button1")) {
			String CPNYID = "";
	     String  PS_sql = "select CPNYID from HRUSER where EMPID='"+EMPID+"'";
	    
	     String aa[][] = t.queryFromPool( PS_sql );
			if (aa.length != 0)
				CPNYID = aa[0][0];
		
//			String sql = "select MDATE,(select DEP_NAME from HRUSER_DEPT_BAS where DEP_NO=QANOTE.SDEPT),(select QAKNAME from QAKIND where QAKIND=QANOTE.QAKIND),QA," + VALIDATE
//					+ ",FILES,MTIME from QANOTE where (TYPE = 'A' or (TYPE = 'B' and SENDEMP like '%" + EMPID + "%')) and (QAMOTIF is null or QAMOTIF='' or QAMOTIF='" + CPNYID + "' ) and " + VALIDATE
//					+ " >= '" + convert.ToSql(Today.trim()) + "'  order by 1 desc,6 desc";
//
//			String ret[][] = t.queryFromPool(sql);
			String[][] ret = null;
			int fieldList[] = { QanoteViewManager.ID_MDATE, QanoteViewManager.ID_SDEPT_NAME, QanoteViewManager.ID_QAKIND_NAME, QanoteViewManager.ID_QA, QanoteViewManager.ID_VALIDATE1,
					QanoteViewManager.ID_FILES, QanoteViewManager.ID_MTIME };
			
			String where = " WHERE " + VALIDATE + " >= '" + convert.ToSql(Today.trim()) + "'  order by 1 desc,6 desc";
			
			QanoteViewManager viewDao = QanoteViewManager.getInstance();
			ret = viewDao.loadByWhereAsStringArray(where, fieldList);
			for (int i = 0; i < ret.length; i++) {
				ret[i][0] = common.sysToday(DATELIST, "ymd", ret[i][0]);
				ret[i][4] = common.sysToday(DATELIST, "ymd", ret[i][4]);
				ret[i][0] = convert.FormatedDate(ret[i][0].trim(), "/");
				ret[i][4] = convert.FormatedDate(ret[i][4].trim(), "/");
				if (ret[i][5].trim().length() != 0) {
					ret[i][5] = "<a href=\"" + getDownloadURL(ret[i][5].trim()) + "\"><FONT COLOR=BLUE>" + translate("下載") + "</FONT></a>";
				} else {
					ret[i][5] = "";//  
				}
			}
			setTableData("table1", ret);
		} else if (ButtonName.trim().equals("button2")) {
			String sql = "select a.EMPID,a.HECNAME,a.SEX,(select DEP_NAME from HRUSER_DEPT_BAS where DEP_NO=a.DEPT_NO),(select POS_NAME from POSITION where POSSIE=a.POSSIE),a.INADATE,'詳細資料' from HRUSER a where substring(a.INADATE,1,6)='"
					+ convert.ToSql(Today.trim().substring(0, 6)) + "' " + get("CPNYID.SUPERA") + " order by 6,4,5";

			
			String ret[][] = t.queryFromPool(sql);
			for (int i = 0; i < ret.length; i++) {
				if (ret[i][2].trim().equals("M"))
					ret[i][2] = translate("男");
				else if (ret[i][2].trim().equals("F"))
					ret[i][2] = translate("女");
				else
					ret[i][2] = "";
				if (DATELIST.trim().equals("A")) {
					ret[i][5] = convert.ac2roc(ret[i][5].trim());
				} 
				ret[i][5] = convert.FormatedDate(ret[i][5].trim(), "/"); 
			}
			setTableData("table1", ret); 
			// setVisible("table1",true);
		} else if (ButtonName.trim().equals("button3")) {
			message("測試測試測試778899");      
			String sql = "select (select COCNAME from COMPANY where CPNYID=a.CPNYID),isnull((select UTNAME from USERTYPE where UTYPE=a.UTYPE),''),a.EMPID, a.HECNAME, substring(a.BIRTHDAY,5,4),a.SEX,(select DEP_NAME from HRUSER_DEPT_BAS where DEP_NO=a.DEPT_NO),(select PNAME from PLACE where PLACE=a.PLACE) from HRUSER a ";
			
			
			PreparedStatement  PS_sql2 = t.getConnectionFromPool().prepareStatement(sql) ;
	     	PS_sql2.executeUpdate();
	     	ResultSet rs = PS_sql2.executeQuery();
	     	String[][] ret = new String[1000][1000];
			int i = 0 ;
	     	while(rs.next()){
				//String fname = rs.getString(4);
				for (int y = 1;y<=8;y++){
					ret[i][y-1] =	 rs.getString(y);
				}
				i++;
				//message("the fname is " + fname);
				}
	     	ResultSetMetaData rsmd = rs.getMetaData();
	     	int numcols = rsmd.getColumnCount();
	     	String statementText = PS_sql2.toString();
	     	//message( Integer.toString(numcols));
			//sql = statementText.substring( statementText.indexOf( ": " ) + 2 );
			//message("!!!"+sql);
	     	//ResultSet rs = PS_sql2.executeQuery();
	     	//String ret[][] = t.queryFromPool(PS_sql2.toString());
			//String ret[][] =rs.toArray();
			//String ret[][] = t.queryFromPool(sql);
			/*for (int i = 0; i < ret.length; i++) {
				ret[i][4] = ret[i][4].trim().substring(0, 2) + translate("月") + ret[i][4].trim().substring(2, 4) + translate("日");
				if (ret[i][5].trim().equals("M"))
					ret[i][5] = translate("男");
				else if (ret[i][5].trim().equals("F"))
					ret[i][5] = translate("女");
				else
					ret[i][5] = "";
			}*/
			/*List rowValues = new ArrayList();
			while (rs.next()) {
			    rowValues.add(rs.getString(1));
			} */ 
			setTableData("table1",ret);
		} else if (ButtonName.trim().equals("button4")) {
			
			String sql = "select b.LESS_NO,b.LESSNAME,a.SDATE,a.EDATE,a.TOT_HR from PQ22 a,PQ03 b,PQ22_FLOWC c where a.LESS_NO=b.LESS_NO and a.CPNYID=c.CPNYID and a.LESS_KEY=c.LESS_KEY and c.F_INP_STAT='確認' "
					+ get("CPNYID.SUPERA") + "";
			sql += " and substring(a.SDATE,1,6) like '" + Today.trim().substring(0, 6) + "%' order by 3,4";
			String ret[][] = t.queryFromPool(sql);
			for (int i = 0; i < ret.length; i++) {
				ret[i][2] = common.sysToday(DATELIST, "ymd", ret[i][2]);
				ret[i][3] = common.sysToday(DATELIST, "ymd", ret[i][3]);
				ret[i][2] = convert.FormatedDate(ret[i][2].trim(), "/");
				ret[i][3] = convert.FormatedDate(ret[i][3].trim(), "/");
			}
			setTableData("table1", ret);
		} else if (ButtonName.trim().equals("button5")) {
			
			// String
			// sql="select a.PDATE,b.DEP_NAME,c.POS_NAME,a.APPLY_P,a.NOTE from REQUIRE a,HRUSER_DEPT_BAS b,POSITION c,REQUIRE_FLOWC d where a.POSSIE=c.POSSIE and a.DEPT_NO=b.DEP_NO and a.CPNYID=d.CPNYID and a.PNO=d.PNO and d.F_INP_STAT='確認' and APPLY_P!=(select count(*) from REQUIRED where PNO=a.PNO and CYN='Y')";
			String sql = "select a.PDATE,b.DEP_NAME,c.POS_NAME,a.APPLY_P,a.NOTE from REQUIRE a,HRUSER_DEPT_BAS b,POSITION c,REQUIRE_FLOWC d where a.POSSIE=c.POSSIE and a.DEPT_NO=b.DEP_NO and a.CPNYID=d.CPNYID and a.PNO=d.PNO and d.F_INP_STAT='確認' and a.PNO in (select PNO from REQUIRED where isnull(CDATE,'"
					+ ISNULL + "')='" + ISNULL + "') " + get("CPNYID.SUPERA") + "";
			sql += " order by 1";
			String ret[][] = t.queryFromPool(sql);
			for (int i = 0; i < ret.length; i++) {
				ret[i][0] = common.sysToday(DATELIST, "ymd", ret[i][0]);
				ret[i][0] = convert.FormatedDate(ret[i][0].trim(), "/");
			}
			setTableData("table1", ret);
		} else if (ButtonName.trim().equals("button6xxx")) {
			String HRSYS[][] = t.queryFromPool("select HRADDR from HRSYS");
			String title1 = "";
			if (HRSYS.length != 0) {
				if (HRSYS[0][0].trim().length() != 0) {
					if (HRSYS[0][0].trim().toUpperCase().startsWith("HTTP"))
						title1 = "" + HRSYS[0][0].trim() + "";
					else
						title1 = "http://" + HRSYS[0][0].trim() + "";
				}
			}
			// setValue("PPD",translate("如欲查看應徵者詳細資料，請連結至eMaker之HR系統查看：")+"<br>"+title1);
			String LLTODAY = datetime.dateAdd(Today.trim(), "m", -1);
			String sql = "select PDATE,UIDENTID,HECNAME,BIRTHDAY,SEX,MARRY,(select POS_NAME from POSITION where POSSIE=RESUME.RESUME) from RESUME where 1=1";
			sql += " and RESUME in (select POSSIE from REQUIRE where DEPT_NO in (select DEP_NO from HRUSER_DEPT_BAS where DEP_CHIEF='" + EMPID + "')) and PDATE>='" + convert.ToSql(LLTODAY.trim())
					+ "'";
			sql += " order by 1";
			if (CUST.trim().equals("CUS000000006")) {
				sql = "select PDATE,UIDENTID,HECNAME,BIRTHDAY,SEX,MARRY,(select POS_NAME from POSITION where POSSIE=RESUME.RESUME) from SYN_RESUME where 1=1";
				sql += " and RESUME in (select POSSIE from REQUIRE where DEPT_NO in (select DEP_NO from HRUSER_DEPT_BAS where DEP_CHIEF='" + EMPID + "')) and PDATE>='" + convert.ToSql(LLTODAY.trim())
						+ "'";
				sql += " order by 1";
			}
			String ret[][] = t.queryFromPool(sql);
			for (int i = 0; i < ret.length; i++) {
				ret[i][0] = common.sysToday(DATELIST, "ymd", ret[i][0]);
				ret[i][3] = common.sysToday(DATELIST, "ymd", ret[i][3]);
				ret[i][0] = convert.FormatedDate(ret[i][0].trim(), "/");
				ret[i][3] = convert.FormatedDate(ret[i][3].trim(), "/");
				if (ret[i][4].trim().equals("M"))
					ret[i][4] = translate("男");
				else if (ret[i][4].trim().equals("F"))
					ret[i][4] = translate("女");
				else
					ret[i][4] = "";
				if (ret[i][5].trim().equals("A"))
					ret[i][5] = translate("已婚");
				else if (ret[i][5].trim().equals("B"))
					ret[i][5] = translate("未婚");
				else if (ret[i][5].trim().equals("C"))
					ret[i][5] = translate("離婚");
				else if (ret[i][5].trim().equals("D"))
					ret[i][5] = translate("寡居");
				else
					ret[i][5] = "";
			}
			setTableData("table1", ret);
		} else if (ButtonName.trim().equals("button7")) {
			String sql = "select POSSIE from HRUSER where ";
			if (!EMPID.equals(""))
				sql += " EMPID='" + EMPID + "' ";

			String s[][] = t.queryFromPool(sql);
			String POSNO = s[0][0];
			// POSNO="008";
			String P[] = getPOSSET(t, POSNO);
			// System.err.println("------ "+P[0]);
			setValue("P1", P[0]);
			setValue("P2", P[1]);
			setValue("P3", P[2]);
			setValue("P4", P[3]);
		}
		return value;
	}

	public String getInformation() {
		return "---------------button1(\u3000\u3000\u3000\u3000\u516c\u544a\u6b04).html_action()----------------";
	}

	public static String[] getPOSSET(talk t, String kPOSNO) throws Exception {
		String[][] r1 = null;
		String sql = "";

		// 找出跟職位說明書(主表)
		String[] array = new String[] { "POSNO", "FUNCT", "DEPT_NO", "PLACE", "MANAGER", "AGENT", "VASSVL", "TDEPT1", "TDEPT2", "PAYRATE", "WSYSTEM", "TEAMSAFE", "FRINGE", "SPECH", "POSNOTE", "A1",
				"A2", "A3", "A4", "B1", "B2", "B3", "B4", "C1", "C2", "C3", "C4", "D1", "D2", "D3", "D4" };
		sql = "select a.POSNO,(select FUNCT from POSSET where POSNO = a.POSNO),b.DEP_CODE+' '+b.DEP_NAME,a.PLACE,"
				+ "c.POSSIE+' '+c.POS_NAME,d.POSNO+' '+d.FUNCT,e.POSSIE+' '+e.POS_NAME,f.POSNO+' '+f.FUNCT,"
				+ "g.POSNO+' '+g.FUNCT,a.PAYRATE,a.WSYSTEM,a.TEAMSAFE,a.FRINGE,a.SPECH,a.POSNOTE,A1,A2,A3,A4,B1,B2,B3,B4,C1,C2,C3,C4,D1,D2,D3,D4 " + "from POSTNOTE a "
				+ "left join HRUSER_DEPT_BAS b on a.DEPT_NO = b.DEP_NO " + "left join POSITION c on a.MANAGER = c.POSSIE " + "left join POSSET d on a.AGENT = d.POSNO "
				+ "left join POSITION e on a.VASSVL = c.POSSIE " + "left join POSSET f on a.TDEPT1 = f.POSNO " + "left join POSSET g on a.TDEPT2 = g.POSNO " + "where 1 = 1 " + "and a.POSNO = '"
				+ convert.ToSql(kPOSNO) + "' ";
		r1 = t.queryFromPool(sql);

		if (r1.length == 0)
			return new String[] { "尚未設定工作說明書", "", "", "" };
		// 整理資料
		Hashtable htPOSTNOTE = new Hashtable();
		for (int i = 0; i < r1.length; i++) {
			for (int j = 0; j < array.length; j++)
				htPOSTNOTE.put(array[j], r1[i][j].trim());
		}

		// 具備能力
		sql = "select b.FUNCNAME , c.ABINAME , a.EXPLAIN , a.SCORE " + "from POSTND1 a " + "left join FUNCTKIND b on a.FUNCKIND = b.FUNCKIND "
				+ "left join ABILITY c on a.FUNCKIND = c.FUNCKIND and a.ABI = c.ABI " + "where 1 = 1 " + "and a.POSNO = '" + convert.ToSql(kPOSNO) + "' " + "order by  1 , 2 ";
		r1 = t.queryFromPool(sql);

		// 整理資料
		Hashtable htPOSTND1 = new Hashtable();
		htPOSTND1.put(kPOSNO, r1);

		// 工作職掌
		sql = "select a.JOBWORK , a.JOBRESP , a.RATETYPE , a.WORKRATE " + "from POSND2 a " + "where 1 = 1 " + "and a.POSNO = '" + convert.ToSql(kPOSNO) + "' " + "order by 1 ";
		r1 = t.queryFromPool(sql);

		// 整理資料
		Hashtable htPOSTND2 = new Hashtable();
		htPOSTND2.put(kPOSNO, r1);

		// 晉昇資格
		sql = "select a.LESSON , a.LESSNAME " + "from POSND3 a " + "where 1 = 1 " + "and a.POSNO = '" + convert.ToSql(kPOSNO) + "' " + "order by 1 ";
		r1 = t.queryFromPool(sql);

		// 整理資料
		Hashtable htPOSTND3 = new Hashtable();
		htPOSTND3.put(kPOSNO, r1);

		// 任用資格
		sql = "select a.QNAME , a.QDESCRIPTION " + "from POSND4 a " + "where 1 = 1 " + "and a.POSNO = '" + convert.ToSql(kPOSNO) + "' " + "order by 1 ";
		r1 = t.queryFromPool(sql);

		// 整理資料
		Hashtable htPOSTND4 = new Hashtable();
		htPOSTND4.put(kPOSNO, r1);

		// 取得 html 畫面
		array = new String[] { "", "", "", "" };
		array[0] = getPOSSET1(htPOSTNOTE, htPOSTND2, kPOSNO);
		array[1] = getPOSSET2(htPOSTNOTE, htPOSTND1, kPOSNO);
		array[2] = getPOSSET3(htPOSTNOTE, htPOSTND4, kPOSNO);
		array[3] = getPOSSET4(htPOSTNOTE, htPOSTND4, kPOSNO);

		return array;
	}

	public static String getPOSSET1(Hashtable htPOSTNOTE, Hashtable htPOSTND2, String POSNO) {
		String[][] r1 = (String[][]) htPOSTND2.get(POSNO);
		String html = "";
		if (htPOSTNOTE.size() == 0)
			return html;
		if (htPOSTND2.size() == 0)
			r1 = new String[0][0];
		html = ""
				// + "<head> "
				// + "<meta http-equiv=\"Content-Language\" content=\"zh-tw\"> "
				// +
				// "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=big5\"> "
				// + "<title>職位說明書</title> "
				// + "</head> "
				// + "<body> "
				+ "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"799\" height=\"545\"> " + "	<!-- MSTableType=\"layout\" --> " + "	<tr> "
				+ "		<td valign=\"top\" height=\"545\" width=\"799\"> " + "		<!-- MSCellType=\"ContentBody\" --> " + "		<table cellpadding=\"0\" cellspacing=\"0\" width=\"799\" height=\"544\"> "
				+ "			<!-- MSTableType=\"layout\" --> " + "			<tr> " + "				<td height=\"150\" valign=\"top\"> " + "				<table border=\"1\" width=\"100%\" id=\"table5\" height=\"146\"> "
				+ "					<tr> " + "						<td rowspan=\"3\" width=\"73\">　</td> " + "						<td width=\"120\" height=\"44\">　</td> " + "						<td rowspan=\"3\" width=\"297\"> "
				+ "						<p align=\"center\" ><font size=\"4\">　</font> " + "						<p align=\"center\" ><font size=\"4\">工作說明書</font> "
				+ "                        <p align=\"center\"><font size=\"4\">"
				+ htPOSTNOTE.get("AGENT")
				+ "</font></td> "
				+ "						<td rowspan=\"2\" width=\"57\"> "
				+ "						<p align=\"center\">表單</p> "
				+ "						<p align=\"center\">編號</td> "
				+ "						<td rowspan=\"2\">　</td> "
				+ "                    </tr> "
				+ " 					<tr> "
				+ "						<td rowspan=\"2\" width=\"120\">　</td> "
				+ "					</tr> "
				+ "					<tr> "
				+ "						<td height=\"46\" width=\"57\"> "
				+ "						<p align=\"center\">版次</td> "
				+ "						<td height=\"46\">　</td> "
				+ "					</tr> "
				+ "				</table> "
				+ "				</td> "
				+ "			</tr> "
				+ "			<tr> "
				+ "				<td height=\"198\" valign=\"top\"> "
				+ "				<table border=\"1\" width=\"100%\" id=\"table6\" height=\"194\"> "
				+ "					<tr> "
				+ "						<td colspan=\"4\">一、工作鑑別(Job Identufucation)</td> "
				+ "					</tr> "
				+ "					<tr> "
				+ "						<td width=\"15%\" align=\"center\">職稱編號</td> "
				+ "						<td width=\"33%\">"
				+ htPOSTNOTE.get("POSNO").toString()
				+ "</td> "
				+ "						<td width=\"16%\" align=\"center\">單位名稱</td> "
				+ "						<td width=\"33%\">"
				+ htPOSTNOTE.get("DEPT_NO").toString()
				+ "</td> "
				+ "					</tr> "
				+ "					<tr> "
				+ "						<td width=\"15%\" align=\"center\">職位名稱</td> "
				+ "						<td width=\"33%\">"
				+ htPOSTNOTE.get("FUNCT").toString()
				+ "</td> "
				+ "						<td width=\"16%\" align=\"center\">工作地點</td> "
				+ "						<td width=\"33%\">"
				+ htPOSTNOTE.get("PLACE").toString()
				+ "</td> "
				+ "					</tr> "
				+ "					<tr> "
				+ "						<td width=\"15%\" align=\"center\">職責概述</td> "
				+ "						<td width=\"85%\" colspan=3>"
				+ htPOSTNOTE.get("POSNOTE").toString()
				+ "</td> "
				+ "					</tr> "
				+ "				</table> "
				+ "				</td> "
				+ "			</tr> "
				+ "			<tr> "
				+ "				<td valign=\"top\" height=\"196\" width=\"799\"> "
				+ "				<table border=\"1\" width=\"100%\" id=\"table7\" height=\"130\"> "
				+ "					<tr> "
				+ "						<td colspan=\"4\" height=\"31\">二、工作職掌(Job Responsibility)</td> "
				+ "					</tr> "
				+ "					<tr> "
				+ "						<td width=\"15%\" align=\"center\" height=\"30\">工作職責</td> "
				+ "						<td width=\"33%\" align=\"center\" height=\"30\">職責評估標準</td> "
				+ "						<td width=\"16%\" align=\"center\" height=\"30\">權重(%)</td> " + "						<td width=\"33%\" align=\"center\" height=\"30\">時間比重(%)</td> " + "					</tr> ";
		for (int i = 0; i < r1.length; i++) {
			html += "					<tr> " + "						<td width=\"15%\" align=\"center\" height=\"29\">" + r1[i][0].trim() + "</td> " + "						<td width=\"33%\" align=\"center\" height=\"29\">" + r1[i][1].trim()
					+ "</td> " + "						<td width=\"16%\" align=\"center\" height=\"29\">" + r1[i][2].trim() + "</td> " + "						<td width=\"33%\" align=\"center\" height=\"29\">" + r1[i][3].trim()
					+ "</td> " + "					</tr> ";
		}
		html += "				</table> " + "				</td> " + "			</tr> " + "		</table> " + "		</td> " + "	</tr> " + "</table> "
		// + "</body> "
				+ " ";
		return html;
	}

	public static String getPOSSET2(Hashtable htPOSTNOTE, Hashtable htPOSTND1, String POSNO) {
		String[][] r1 = (String[][]) htPOSTND1.get(POSNO);
		String html = "";
		if (htPOSTNOTE.size() == 0)
			return html;
		if (htPOSTND1.size() == 0)
			r1 = new String[0][0];
		html = " "
				// + "<head> "
				// + "<meta http-equiv=\"Content-Language\" content=\"zh-tw\"> "
				// +
				// "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=big5\"> "
				// + "<title>職位說明書</title> "
				// + "</head> "
				// + "<body> "
				+ "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"799\" height=\"545\"> " + "	<!-- MSTableType=\"layout\" --> " + "	<tr> "
				+ "		<td valign=\"top\" height=\"545\" width=\"799\"> " + "		<!-- MSCellType=\"ContentBody\" --> " + " 		<table cellpadding=\"0\" cellspacing=\"0\" width=\"799\" height=\"544\"> "
				+ "			<!-- MSTableType=\"layout\" --> " + "			<tr> " + "				<td height=\"214\" valign=\"top\"> " + "				<table border=\"1\" width=\"100%\" id=\"table8\" height=\"203\"> "
				+ "					<tr> " + "						<td colspan=\"5\">三、組織與關係(Organizational Relations)</td> " + "					</tr> "
				/*
				 * + "					<tr> " +
				 * "						<td width=\"16%\" align=\"center\">直屬主管</td> " +
				 * "						<td width=\"32%\" align=\"center\">"
				 * +htPOSTNOTE.get("MANAGER").toString()+"</td> " +
				 * "						<td width=\"16%\" align=\"center\">下屬職稱</td> " +
				 * "						<td width=\"32%\" align=\"center\">"
				 * +htPOSTNOTE.get("VASSVL").toString()+"</td> " + "					</tr> "
				 * + "					<tr> " +
				 * "						<td width=\"16%\" align=\"center\">職務代理人</td> " +
				 * "						<td width=\"32%\" align=\"center\">"
				 * +htPOSTNOTE.get("AGENT").toString()+"</td> " +
				 * "						<td width=\"16%\" align=\"center\">　</td> " +
				 * "						<td width=\"32%\" align=\"center\">　</td> " +
				 * "					</tr> " + "					<tr> " +
				 * "						<td width=\"16%\" align=\"center\">調任職位本單位</td> " +
				 * "						<td width=\"32%\" align=\"center\">"
				 * +htPOSTNOTE.get("TDEPT1").toString()+"</td> " +
				 * "						<td width=\"16%\" align=\"center\">調任職位他單位</td> " +
				 * "						<td width=\"32%\" align=\"center\">"
				 * +htPOSTNOTE.get("TDEPT2").toString()+"</td> " + "					</tr> "
				 */
				+ "					<tr> " + "						<th width=\"20%\" align=\"center\"></td> " + "						<th width=\"20%\" align=\"center\">對象</td> " + "						<th width=\"20%\" align=\"center\">方式</td> "
				+ "						<th width=\"20%\" align=\"center\">頻度</td> " + "						<th width=\"20%\" align=\"center\">內容</td> " + "					</tr> " + "					<tr> "
				+ "						<td width=\"20%\" align=\"center\">向上</td> " + "						<td width=\"20%\" align=\"center\">"
				+ htPOSTNOTE.get("A1").toString()
				+ "</td> "
				+ "						<td width=\"20%\" align=\"center\">"
				+ htPOSTNOTE.get("A2").toString()
				+ "</td> "
				+ "						<td width=\"20%\" align=\"center\">"
				+ htPOSTNOTE.get("A3").toString()
				+ "</td> "
				+ "						<td width=\"20%\" align=\"center\">"
				+ htPOSTNOTE.get("A4").toString()
				+ "</td> "
				+ "					</tr> "
				+ "					<tr> "
				+ "						<td width=\"20%\" align=\"center\">平行</td> "
				+ "						<td width=\"20%\" align=\"center\">"
				+ htPOSTNOTE.get("B1").toString()
				+ "</td> "
				+ "						<td width=\"20%\" align=\"center\">"
				+ htPOSTNOTE.get("B2").toString()
				+ "</td> "
				+ "						<td width=\"20%\" align=\"center\">"
				+ htPOSTNOTE.get("B3").toString()
				+ "</td> "
				+ "						<td width=\"20%\" align=\"center\">"
				+ htPOSTNOTE.get("B4").toString()
				+ "</td> "
				+ "					</tr> "
				+ "					<tr> "
				+ "						<td width=\"20%\" align=\"center\">對下</td> "
				+ "						<td width=\"20%\" align=\"center\">"
				+ htPOSTNOTE.get("C1").toString()
				+ "</td> "
				+ "						<td width=\"20%\" align=\"center\">"
				+ htPOSTNOTE.get("C2").toString()
				+ "</td> "
				+ "						<td width=\"20%\" align=\"center\">"
				+ htPOSTNOTE.get("C3").toString()
				+ "</td> "
				+ "						<td width=\"20%\" align=\"center\">"
				+ htPOSTNOTE.get("C4").toString()
				+ "</td> "
				+ "					</tr> "
				+ "					<tr> "
				+ "						<td width=\"20%\" align=\"center\">對外</td> "
				+ "						<td width=\"20%\" align=\"center\">"
				+ htPOSTNOTE.get("D1").toString()
				+ "</td> "
				+ "						<td width=\"20%\" align=\"center\">"
				+ htPOSTNOTE.get("D2").toString()
				+ "</td> "
				+ "						<td width=\"20%\" align=\"center\">"
				+ htPOSTNOTE.get("D3").toString()
				+ "</td> "
				+ "						<td width=\"20%\" align=\"center\">"
				+ htPOSTNOTE.get("D4").toString()
				+ "</td> "
				+ "					</tr> "
				+ "				</table> "
				+ "				</td> "
				+ "			</tr> "
				+ "			<tr> "
				+ "				<td valign=\"top\" height=\"330\" width=\"799\"> "
				+ "				<table border=\"1\" width=\"100%\" id=\"table7\" height=\"130\"> "
				+ "					<tr> "
				+ "						<td colspan=\"4\" height=\"31\">四、具備能力</td> "
				+ "					</tr> "
				+ "					<tr> "
				+ "						<td width=\"15%\" align=\"center\" height=\"30\">職能類別</td> "
				+ "						<td width=\"15%\" align=\"center\" height=\"30\">能力主題</td> "
				+ "						<td width=\"50%\" align=\"center\" height=\"30\">說明</td> "
				+ "						<td width=\"17%\" align=\"center\" height=\"30\">能力分數或等級</td> " + "					</tr> ";
		for (int i = 0; i < r1.length; i++) {
			html += "					<tr> " + "						<td width=\"15%\" align=\"center\" height=\"29\">" + r1[i][0].trim() + "</td> " + "						<td width=\"15%\" align=\"center\" height=\"29\">" + r1[i][1].trim()
					+ "</td> " + "						<td width=\"50%\" align=\"center\" height=\"29\">" + r1[i][2].trim() + "</td> " + "						<td width=\"17%\" align=\"center\" height=\"29\">" + r1[i][3].trim()
					+ "</td> " + "					</tr> ";
		}
		html += "				</table> " + "				</td> " + "			</tr> " + "		</table> " + "		</td> " + "	</tr> " + "</table> "
		// + "</body> "
				+ " ";
		return html;
	}

	public static String getPOSSET3(Hashtable htPOSTNOTE, Hashtable htPOSTND4, String POSNO) {
		String[][] r1 = (String[][]) htPOSTND4.get(POSNO);
		String html = "";
		if (htPOSTNOTE == null)
			return html;
		if (htPOSTND4.size() == 0)
			r1 = new String[0][0];
		html = "" + "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"799\" height=\"545\"> " + "	<!-- MSTableType=\"layout\" --> " + "	<tr> "
				+ "		<td valign=\"top\" height=\"545\" width=\"799\"> " + "		<!-- MSCellType=\"ContentBody\" --> " + "		<table cellpadding=\"0\" cellspacing=\"0\" width=\"799\" height=\"544\"> "
				+ "			<!-- MSTableType=\"layout\" --> " + "			<tr> " + "				<td height=\"214\" valign=\"top\"> " + "				<table border=\"1\" width=\"100%\" id=\"table8\" height=\"96\"> "
				+ "					<tr> " + "						<td colspan=\"2\">五、任用資格</td> " + "					</tr> " + "					<tr> " + "						<td width=\"50%\" align=\"center\">資格項目</td> "
				+ " 						<td width=\"48%\" align=\"center\">合格標準</td> " + "					</tr> ";
		for (int i = 0; i < r1.length; i++) {
			html += "					<tr> " + "						<td width=\"50%\" align=\"center\">" + r1[i][0].trim() + "</td> " + "						<td width=\"48%\" align=\"center\">" + r1[i][1].trim() + "</td> " + "					</tr> ";
		}
		html += "				</table> " + "				</td> " + "			</tr> " + "			<tr> " + "				<td valign=\"top\" height=\"330\" width=\"799\"> "
				+ "				<table border=\"1\" width=\"100%\" id=\"table7\" height=\"130\"> " + "					<tr> " + "						<td colspan=\"4\" height=\"31\">六、職位報酬(Compensation)</td> " + "					</tr> "
				+ "					<tr> " + "						<td width=\"17%\" align=\"center\" height=\"30\">薪等薪級</td> " + "						<td width=\"33%\" align=\"center\" height=\"30\">"
				+ htPOSTNOTE.get("PAYRATE").toString()
				+ "</td> "
				+ "						<td width=\"17%\" align=\"center\" height=\"30\">出勤制度</td> "
				+ "						<td width=\"30%\" align=\"center\" height=\"30\">"
				+ htPOSTNOTE.get("WSYSTEM").toString()
				+ "</td> "
				+ "					</tr> "
				+ "					<tr> "
				+ "						<td width=\"17%\" align=\"center\" height=\"29\">團體保險</td> "
				+ "						<td width=\"33%\" align=\"center\" height=\"29\">"
				+ htPOSTNOTE.get("TEAMSAFE").toString()
				+ "</td> "
				+ "						<td width=\"17%\" align=\"center\" height=\"29\">其他福利</td> "
				+ "						<td width=\"30%\" align=\"center\" height=\"29\">"
				+ htPOSTNOTE.get("FRINGE").toString()
				+ "</td> "
				+ "					</tr> "
				+ "					<tr> "
				+ "						<td width=\"17%\" align=\"center\">特殊福利</td> "
				+ "						<td width=\"81%\" align=\"center\" colspan=\"3\">"
				+ htPOSTNOTE.get("SPECH").toString()
				+ "</td> "
				+ "					</tr> "
				+ "				</table> " + "				</td> " + "			</tr> " + "		</table> " + "		</td> " + "	</tr> " + "</table> " + " ";
		return html;
	}

	public static String getPOSSET4(Hashtable htPOSTNOTE, Hashtable htPOSTND3, String POSNO) {
		String[][] r1 = (String[][]) htPOSTND3.get(POSNO);
		String html = "";
		if (htPOSTNOTE.size() == 0)
			return html;
		if (htPOSTND3.size() == 0)
			r1 = new String[0][0];
		html = "" + "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"799\" height=\"545\"> " + "	<!-- MSTableType=\"layout\" --> " + "	<tr> "
				+ "		<td valign=\"top\" height=\"545\" width=\"799\"> " + "		<!-- MSCellType=\"ContentBody\" --> " + "		<table cellpadding=\"0\" cellspacing=\"0\" width=\"799\" height=\"544\"> "
				+ "			<!-- MSTableType=\"layout\" --> " + "			<tr> " + "				<td valign=\"top\" height=\"544\"> " + "				<table border=\"1\" width=\"100%\" id=\"table8\" height=\"96\"> "
				+ "					<tr> " + "						<td colspan=\"2\">七、晉升資格</td> " + "					</tr> " + "					<tr> " + "						<td width=\"50%\" align=\"center\">資格項目</td> "
				+ "						<td width=\"48%\" align=\"center\">合格標準</td> " + "					</tr> ";
		for (int i = 0; i < r1.length; i++) {
			html += "					<tr> " + "						<td width=\"50%\" align=\"center\">" + r1[i][0].trim() + "</td> " + "						<td width=\"48%\" align=\"center\">" + r1[i][1].trim() + "</td> " + "					</tr> ";
		}
		html += "				</table> " + "				</td> " + "			</tr> " + "			<tr> " + "				<td height=\"1\" width=\"799\"></td> " + "			</tr> " + "		</table> " + "		</td> " + "	</tr> " + "</table> "
		// + "</body> "
				+ "";
		return html; 
	}
     
}
