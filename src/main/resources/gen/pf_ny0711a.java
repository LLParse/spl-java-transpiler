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

public class pf_ny0711a extends AbstractProcedure {

	public String execute(Integer in_acct_id, Integer in_promo_id, String in_stay_type, Integer in_prop_id, Timestamp in_doa, Integer in_los, String in_rm_type, String in_srp_code, Double in_rm_revenue, Double in_fb_revenue, Double in_other_revenue, String in_curr_code, String in_res_source, Integer in_stay_id) throws SQLException, ProcedureException {		
		
		/*
		 * pf_ny0711a.sql - New York on Sale Summer promotion. This filter checks:
		 *                  1) if the account has the designated application group code
		 *                  2) the booking source is ch.com or mobile
		 *                  3) reservation made between 7/21/2011 and 9/30/2011
		 *                  
		 *    
		 * $Id: pf_ny0711a.sql 99 2012-12-19 21:14:54Z michael_giroux $
		 *    
		 *        Copyright (C) 2011 Choice Hotels International, Inc.
		 */		
		
		Timestamp l_start_date;		
		Timestamp l_stop_date;		
		String l_debug;		
		Integer l_acct_appl_group_id;		
		Integer l_appl_group_id;		
		Timestamp l_reservation_date;		
		String l_answer;		
		String l_resv_start_date;		
		String l_resv_stop_date;		
		
		Integer sql_error;		
		Integer isam_error;		
		String error_data;		
		
		try {			
			
			l_start_date = null;			
			l_stop_date = null;			
			l_debug = "F";			
			l_acct_appl_group_id = null;			
			l_appl_group_id = null;			
			l_reservation_date = null;			
			l_answer = "F";			
			l_resv_start_date = null;			
			l_resv_stop_date = null;			
			
			// set up tracing based on app_config entry
			l_debug = new settrace().execute("pf_ny0711a");			
			if (l_debug.equals("T")) {				
				setDebugFile("/tmp/pf_ny0711a_" + dbinfo("sessionid") + ".trace");				
				trace("on");
			}			
			
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
				return l_answer;
			}			
			
			// get account info

			PreparedStatement pstmt2 = prepareStatement(
					  "select appl_group_id"
					+ " from acct"
					+ " where acct_id = ?");
			
			if (in_acct_id != null) {
				pstmt2.setInt(1, in_acct_id);
			}
			else {
				pstmt2.setNull(1, Types.JAVA_OBJECT);
			}
			ResultSet rs2 = executeQuery(pstmt2);
			rs2.next();
			l_acct_appl_group_id = rs2.getInt(1);
			pstmt2.close();			
			
			// get the NY On Sale Appl Group Code

			PreparedStatement pstmt3 = prepareStatement(
					  "select appl_group_id"
					+ " from appl_group"
					+ " where appl_group_cd = \"NY0711A\"");
			
			ResultSet rs3 = executeQuery(pstmt3);
			rs3.next();
			l_appl_group_id = rs3.getInt(1);
			pstmt3.close();			
			
			// appl group code check
			if (l_acct_appl_group_id == null || !l_acct_appl_group_id.equals(l_appl_group_id)) {				
				return l_answer;
			}			
			
			// booking source check
			if (in_res_source == null || (!in_res_source.equals("N") && !in_res_source.equals("M"))) {				
				return l_answer;
			}			
			
			// reservation date check within subset of promo date. In this
			// case, promo is 7/21/2011 -> 12/31/2011 but reservation date
			// has to be between 7/21/2011 and 9/30/2011.

			PreparedStatement pstmt4 = prepareStatement(
					  "select reservation_date"
					+ " from stay"
					+ " where stay_id = ?");
			
			if (in_stay_id != null) {
				pstmt4.setInt(1, in_stay_id);
			}
			else {
				pstmt4.setNull(1, Types.JAVA_OBJECT);
			}
			ResultSet rs4 = executeQuery(pstmt4);
			rs4.next();
			l_reservation_date = rs4.getTimestamp(1);
			pstmt4.close();			
			
			// get the reservation control dates from the app_config table

			PreparedStatement pstmt5 = prepareStatement(
					  "select value"
					+ " from app_config"
					+ " where key = \"promo.ny0711.reservation_start_date \"");
			
			ResultSet rs5 = executeQuery(pstmt5);
			rs5.next();
			l_resv_start_date = rs5.getString(1);
			pstmt5.close();			
			

			PreparedStatement pstmt6 = prepareStatement(
					  "select value"
					+ " from app_config"
					+ " where key = \"promo.ny0711.reservation_stop_date \"");
			
			ResultSet rs6 = executeQuery(pstmt6);
			rs6.next();
			l_resv_stop_date = rs6.getString(1);
			pstmt6.close();			
			
			
			if (l_reservation_date == null || l_reservation_date < date(l_resv_start_date) || l_reservation_date > date(l_resv_stop_date)) {				
				return l_answer;
			}			
			
			// all the basic criteria has passed, now check if another room has already received the bonus
			l_answer = new pf_ny0711_awardbonus().execute(in_acct_id, in_promo_id, in_stay_type, in_prop_id, in_doa, in_los, in_rm_type, in_srp_code, in_rm_revenue, in_fb_revenue, in_other_revenue, in_curr_code, in_res_source, in_stay_id);			
			
			return l_answer;			
			

		}
		catch (SQLException e) {
			sql_error = e.getErrorCode();
			isam_error = 0;
			error_data = e.getMessage();
			{				
				throw new ProcedureException(sql_error, isam_error, error_data);
			}
		}
	}

}