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

public class pf_fl0811_enroll extends AbstractProcedure {

	public String execute(Integer in_acct_id, Integer in_promo_id, String in_stay_type, Integer in_prop_id, Timestamp in_doa, Integer in_los, String in_rm_type, String in_srp_code, Double in_rm_revenue, Double in_fb_revenue, Double in_other_revenue, String in_curr_code, String in_res_source, Integer in_stay_id) throws SQLException, ProcedureException {		
		
		/*
		 * pf_fl0811_enroll.sql - Fall 2011 promo filter for quadruple points for accounts not registered
		 * 
		 *                       1) This is for checking enrollment eligibility
		 *                 
		 *                       This filter is only entered if there is no acct_offer entry and only for
		 *                       the quadruple promos
		 *    
		 * $Id: pf_fl0811_enroll.sql 99 2012-12-19 21:14:54Z michael_giroux $
		 *    
		 *        Copyright (C) 2011 Choice Hotels International, Inc.
		 */		
		
		Timestamp l_start_date;		
		Timestamp l_stop_date;		
		String l_recog_cd;		
		Timestamp l_signup_date;		
		String l_platinum;		
		String l_diamond;		
		String l_registered;		
		String l_eligible;		
		String l_debug;		
		String l_appl_source;		
		Integer l_offer_id;		
		Integer l_count;		
		
		
		Integer sql_error;		
		Integer isam_error;		
		String error_data;		
		
		try {			
			
			l_start_date = null;			
			l_stop_date = null;			
			l_recog_cd = null;			
			l_signup_date = null;			
			l_platinum = "F";			
			l_diamond = "F";			
			l_registered = "F";			
			l_eligible = "F";			
			l_debug = "F";			
			l_appl_source = "F";			
			l_offer_id = null;			
			l_count = 0;			
			
			
			// set up tracing based on app_config entry
			l_debug = new settrace().execute("pf_fl0811_enroll");			
			if (l_debug.equals("T")) {				
				setDebugFile("/tmp/pf_fl0811_enroll_" + dbinfo("sessionid") + ".trace");				
				trace("on");
			}			
			
			// get some account info

			PreparedStatement pstmt1 = prepareStatement(
					  "select a.recog_cd, a.signup_date, a.appl_source"
					+ " from acct a"
					+ " where a.acct_id = ?");
			
			if (in_acct_id != null) {
				pstmt1.setInt(1, in_acct_id);
			}
			else {
				pstmt1.setNull(1, Types.JAVA_OBJECT);
			}
			ResultSet rs1 = executeQuery(pstmt1);
			rs1.next();
			l_recog_cd = rs1.getString(1);
			l_signup_date = rs1.getTimestamp(2);
			l_appl_source = rs1.getString(3);
			pstmt1.close();			
			
			// check if registered and if so, get out since this filter is for
			// non-registered accounts

			PreparedStatement pstmt2 = prepareStatement(
					  "select count(*)"
					+ " from acct_offer"
					+ " where acct_id = ?"
					+ " and offer_id = ("
						+ "select offer_id"
						+ " from offer"
						+ " where offer_cd = \"FL0811O\""
						+ " and recog_cd = ?"
					+ ")");
			
			if (in_acct_id != null) {
				pstmt2.setInt(1, in_acct_id);
			}
			else {
				pstmt2.setNull(1, Types.JAVA_OBJECT);
			}
			if (l_recog_cd != null) {
				pstmt2.setString(2, l_recog_cd);
			}
			else {
				pstmt2.setNull(2, Types.JAVA_OBJECT);
			}
			ResultSet rs2 = executeQuery(pstmt2);
			rs2.next();
			l_count = rs2.getInt(1);
			pstmt2.close();			
			
			if (l_count > 0) {				
				return l_eligible;
			}			
			
			// see if enrollment stay at the hotel
			l_eligible = new pf_isenrollstay_v2().execute(in_acct_id, in_doa, in_doa + in_los);			
			
			// was stay booked centrally and enroll at hotel or through WinAI during promotion
			if (in_res_source != null && (in_res_source.equals("N") || in_res_source.equals("C") || in_res_source.equals("M"))) {				
				if (l_appl_source != null && (l_appl_source.equals("H") || l_appl_source.equals("W"))) {					
					// get some promo info

					PreparedStatement pstmt3 = prepareStatement(
							  "select start_date, stop_date"
							+ " from promo"
							+ " where promo_id = ?");
					
					if (in_promo_id != null) {
						pstmt3.setInt(1, in_promo_id);
					}
					else {
						pstmt3.setNull(1, Types.JAVA_OBJECT);
					}
					ResultSet rs3 = executeQuery(pstmt3);
					rs3.next();
					l_start_date = rs3.getTimestamp(1);
					l_stop_date = rs3.getTimestamp(2);
					pstmt3.close();					
					if (l_signup_date >= l_start_date && l_signup_date <= l_stop_date) {						
						l_eligible = "T";
					}
				}
			}			
			
			if (l_eligible.equals("F")) {				
				return l_eligible;
			}			
			
			// all basic criteria have been met and it looks like bonus should be awarded 
			// but do final check on multiple rooms and already awarded bonus.
			l_eligible = new pf_fl0811_awardbonus().execute(in_acct_id, in_promo_id, in_stay_type, in_prop_id, in_doa, in_los, in_rm_type, in_srp_code, in_rm_revenue, in_fb_revenue, in_other_revenue, in_curr_code, in_res_source, in_stay_id, l_start_date, l_stop_date);			
			
			return l_eligible;			
			

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