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

public class pf_sp0210booking extends AbstractProcedure {

	public String execute(Integer in_acct_id, Integer in_promo_id, String in_stay_type, Integer in_prop_id, Timestamp in_doa, Integer in_los, String in_rm_type, String in_srp_code, Double in_rm_revenue, Double in_fb_revenue, Double in_other_revenue, String in_curr_code, String in_res_source, Integer in_stay_id) throws SQLException, ProcedureException {		
		
		/*
		 * pf_sp0210booking.sql - Check if the stay was booked via ch.com or central reservations
		 *                        or was an enrollment stay. If so, then call the routine to check
		 *                        if a bonus should be awarded.
		 *    
		 * $Id: pf_sp0210booking.sql 25075 2010-01-15 22:32:02Z rshepher $
		 * 
		 *       Copyright (C) 2010 Choice Hotels International, Inc.
		 */		
		String l_answer;		
		Timestamp l_start_date;		
		Timestamp l_stop_date;		
		String l_isenrollstay;		
		
		l_answer = null;		
		l_start_date = null;		
		l_stop_date = null;		
		l_isenrollstay = null;		
		
		//set debug file to '/tmp/pf_sp0210booking.trace';
		//trace on;
		
		// Check for valid doa

		PreparedStatement pstmt1 = prepareStatement(
				  "select promo.start_date, promo.stop_date"
				+ " from promo"
				+ " where promo.promo_id = ?");
		
		if (in_promo_id != null) {
			pstmt1.setInt(1, in_promo_id);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		l_start_date = rs1.getTimestamp(1);
		l_stop_date = rs1.getTimestamp(2);
		pstmt1.close();		
		
		if (in_doa < l_start_date || in_doa > l_stop_date) {			
			return "F";
		}		
		
		l_isenrollstay = new pf_isenrollstay().execute(in_acct_id, in_doa);		
		
		// Check if booked internet, central res, or is an enrollment stay
		if (l_isenrollstay.equals("F")) {			
			if (in_res_source == null || (!in_res_source.equals("C") && !in_res_source.equals("N"))) {				
				return "F";
			}
		}		
		
		// Got an eligible stay, check if bonus should be awarded
		l_answer = pf_sp0210awardbonus(in_acct_id, in_promo_id, in_stay_type, in_prop_id, in_doa, in_los, in_rm_type, in_srp_code, in_rm_revenue, in_fb_revenue, in_other_revenue, in_curr_code, in_res_source, in_stay_id);		
		
		return l_answer;
	}

}