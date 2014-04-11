/* Generated on 02-25-2013 12:49:25 PM by SPLParser v0.9 */
package com.choicehotels.gen;

import java.lang.reflect.Method;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class pf_sp2006_visa extends AbstractProcedure {

	public String execute(Integer acct_id, Integer promo_id, String stay_type, Integer prop_id, Timestamp doa, Integer los, String rm_type, String srp_code, Double rm_revenue, Double fb_revenue, Double other_revenue, String curr_code, String res_source, Integer stay_id) throws SQLException, ProcedureException {		
		
		/*
		 * 	pf_sp2006_visa.sql	- Master filter  for first part of Special Spring 2006 Promo
		 * 	(]$[) $RCSfile$:$Revision: 99 $ | CDATE=$Date: 2012-12-19 14:14:54 -0700 (Wed, 19 Dec 2012) $ ~
		 */		
		
		String inet_a;		
		String end_a;		
		String resv_a;		
		String visa_a;		
		Timestamp res_date;		
		Timestamp pb_date;		
		Timestamp doa;		
		String cc_code;		
		String stay_type;		
		String pay_meth;		
		
		res_date = null;		
		pb_date = null;		
		doa = null;		
		cc_code = null;		
		stay_type = null;		
		pay_meth = null;		
		inet_a = "F";		
		end_a = "F";		
		resv_a = "F";		
		visa_a = "F";		
		
		inet_a = new pf_internet_bking().execute(acct_id, promo_id, stay_type, prop_id, doa, los, rm_type, srp_code, rm_revenue, fb_revenue, other_revenue, curr_code, res_source, stay_id);		
		
		if (inet_a.equals("F")) {			
			return "F";
		}		
		
		end_a = new pf_complete_stay_by_promo_end().execute(acct_id, promo_id, stay_type, prop_id, doa, los, rm_type, srp_code, rm_revenue, fb_revenue, other_revenue, curr_code, res_source, stay_id);		
		
		if (end_a.equals("F")) {			
			return "F";
		}		
		

		PreparedStatement pstmt1 = prepareStatement(
				  "select p.start_date"
				+ " from promo p"
				+ " where p.promo_id = ?");
		
		if (promo_id != null) {
			pstmt1.setInt(1, promo_id);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		pb_date = rs1.getTimestamp(1);
		pstmt1.close();		
		

		PreparedStatement pstmt2 = prepareStatement(
				  "select s.reservation_date, s.doa, c.cc_code, s.stay_type, s.payment_meth"
				+ " from stay s, cust_ident c"
				+ " where s.stay_id = ?"
				+ " and s.cust_ident_id = c.cust_ident_id");
		
		if (stay_id != null) {
			pstmt2.setInt(1, stay_id);
		}
		else {
			pstmt2.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs2 = executeQuery(pstmt2);
		rs2.next();
		res_date = rs2.getTimestamp(1);
		doa = rs2.getTimestamp(2);
		cc_code = rs2.getString(3);
		stay_type = rs2.getString(4);
		pay_meth = rs2.getString(5);
		pstmt2.close();		
		
		if (res_date == null) {			
			return "F";
		}		
		
		if (res_date >= pb_date && res_date <= date("5/17/2006")) {			
			resv_a = "T";
		}		
		else {			
			return "F";
		}		
		
		if (stay_type.equals("N") && cc_code.equals("VI")) {			
			visa_a = "T";
		}		
		
		if (stay_type.equals("F") && pay_meth.equals("VI")) {			
			visa_a = "T";
		}		
		
		
		if (inet_a.equals("T") && end_a.equals("T") && resv_a.equals("T") && visa_a.equals("T")) {			
			return "T";
		}		
		else {			
			return "F";
		}
	}

}