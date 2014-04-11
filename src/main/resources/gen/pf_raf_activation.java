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

public class pf_raf_activation extends AbstractProcedure {

	public String execute(Integer acct_id, Integer promo_id, String stay_type, Integer prop_id, Timestamp doa, Integer los, String rm_type, String srp_code, Double rm_revenue, Double fb_revenue, Double other_revenue, String curr_code, String res_source, Integer stay_id) throws SQLException, ProcedureException {		
		
		/*
		 * pf_raf_activation.sql - Check if the stay is valid for
		 * the RAF first stay award bonus.
		 *    
		 *           
		 * $Id: pf_raf_activation.sql 99 2012-12-19 21:14:54Z michael_giroux $
		 *    
		 *        Copyright (C) 2006 Choice Hotels International, Inc.
		 */		
		
		String answer;		
		Integer raf_reg_id;		
		
		raf_reg_id = null;		
		answer = null;		
		
		answer = new pf_firststayever().execute(acct_id, promo_id, stay_type, prop_id, doa, los, rm_type, srp_code, rm_revenue, fb_revenue, other_revenue, curr_code, res_source, stay_id);		
		
		if (!answer.equals("T")) {			
			return "F";
		}		
		
		// First stay, see if they were registered and enrolled

		PreparedStatement pstmt1 = prepareStatement(
				  "select raf_reg.raf_reg_id"
				+ " from raf_reg"
				+ " where raf_reg.stay_promo_id = ?"
				+ " and raf_reg.referred_acct_id = ?"
				+ " and raf_reg.reg_status = \"E\"");
		
		if (promo_id != null) {
			pstmt1.setInt(1, promo_id);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		if (acct_id != null) {
			pstmt1.setInt(2, acct_id);
		}
		else {
			pstmt1.setNull(2, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		raf_reg_id = rs1.getInt(1);
		pstmt1.close();		
		
		if (raf_reg_id == null) {			// not correctly registered.
			return "F";
		}		
		
		return "T";
	}

}