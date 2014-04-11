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

public class pf_sp2006_cobrand extends AbstractProcedure {

	public String execute(Integer acct_id, Integer promo_id, String stay_type, Integer prop_id, Timestamp doa, Integer los, String rm_type, String srp_code, Double rm_revenue, Double fb_revenue, Double other_revenue, String curr_code, String res_source, Integer stay_id) throws SQLException, ProcedureException {		
		
		/*
		 * 	pf_sp2006_cobrand.sql	- Master filter  for second part of Special Spring 2006 Promo
		 * 	(]$[) $RCSfile$:$Revision: 99 $ | CDATE=$Date: 2012-12-19 14:14:54 -0700 (Wed, 19 Dec 2012) $ ~
		 */		
		
		String answer;		
		Timestamp res_date;		
		Timestamp doa;		
		Timestamp pb_date;		
		
		answer = "F";		
		res_date = null;		
		doa = null;		
		pb_date = null;		
		
		answer = new pf_bofa_payment().execute(acct_id, promo_id, stay_type, prop_id, doa, los, rm_type, srp_code, rm_revenue, fb_revenue, other_revenue, curr_code, res_source, stay_id);		
		
		if (answer.equals("F")) {			
			return answer;
		}		
		
		answer = new pf_internet_bking().execute(acct_id, promo_id, stay_type, prop_id, doa, los, rm_type, srp_code, rm_revenue, fb_revenue, other_revenue, curr_code, res_source, stay_id);		
		
		if (answer.equals("F")) {			
			return answer;
		}		
		
		answer = new pf_complete_stay_by_promo_end().execute(acct_id, promo_id, stay_type, prop_id, doa, los, rm_type, srp_code, rm_revenue, fb_revenue, other_revenue, curr_code, res_source, stay_id);		
		
		if (answer.equals("F")) {			
			return answer;
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
				  "select s.reservation_date, s.doa"
				+ " from stay s"
				+ " where s.stay_id = ?");
		
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
		pstmt2.close();		
		
		if (res_date == null) {			
			return "F";
		}		
		
		if (res_date >= pb_date && res_date <= date("5/17/2006")) {			
			answer = "T";
		}		
		else {			
			return "F";
		}		
		
		return answer;
	}

}