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

public class pf_wi1211 extends AbstractProcedure {

	public String execute(Integer in_acct_id, Integer in_promo_id, String in_stay_type, Integer in_prop_id, Timestamp in_doa, Integer in_los, String in_rm_type, String in_srp_code, Double in_rm_revenue, Double in_fb_revenue, Double in_other_revenue, String in_curr_code, String in_res_source, Integer in_stay_id) throws SQLException, ProcedureException {		
		
		/*
		 * pf_wi1211.sql - Winter 2011 promo filter for double points if criteria are met:
		 *                 1) Must be registered for WI1211O and have no alt_acct_id entry in the acct_offer table
		 *                 2) DOA must be on or after the registration date
		 *                 2) Booked via ch.com, 18004Choice, mobile, GDS
		 *                 3) Platinum/Diamond accounts can book from any source
		 *                 
		 *                 This filter is only entered via the WI1211D[E|M] promos if the account is registered.
		 *    
		 * $Id: pf_wi1211.sql 99 2012-12-19 21:14:54Z michael_giroux $
		 *    
		 *        Copyright (C) 2011 Choice Hotels International, Inc.
		 */		
		
		Timestamp l_start_date;		
		Timestamp l_stop_date;		
		Timestamp l_offer_dtime;		
		String l_recog_cd;		
		Timestamp l_signup_date;		
		Integer l_offer_id;		
		String l_platinum;		
		String l_diamond;		
		String l_eligible;		
		String l_debug;		
		
		Integer sql_error;		
		Integer isam_error;		
		String error_data;		
		
		try {			
			
			l_start_date = null;			
			l_stop_date = null;			
			l_offer_dtime = null;			
			l_recog_cd = null;			
			l_signup_date = null;			
			l_offer_id = null;			
			l_platinum = "F";			
			l_diamond = "F";			
			l_eligible = "F";			
			l_debug = "F";			
			
			// determine tracing based on app_config
			
			l_debug = new settrace().execute("pf_wi1211");			
			if (l_debug.equals("T")) {				
				setDebugFile("/tmp/pf_wi1211_" + dbinfo("sessionid") + ".trace");				
				trace("on");
			}			
			
			// get some account info

			PreparedStatement pstmt1 = prepareStatement(
					  "select a.recog_cd, a.signup_date"
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
			pstmt1.close();			
			
			// see if registered on or before DOA and alt_acct_id is null (i.e. no partner miles wanted)

			PreparedStatement pstmt2 = prepareStatement(
					  "select offer_dtime"
					+ " from acct_offer"
					+ " where acct_id = ?"
					+ " and offer_id = ("
						+ "select offer_id"
						+ " from offer"
						+ " where offer_cd = \"WI1211O\""
						+ " and recog_cd = ?"
					+ ")"
					+ " and alt_acct_id is null");
			
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
			l_offer_dtime = rs2.getTimestamp(1);
			pstmt2.close();			
			
			if (l_offer_dtime == null || l_offer_dtime > in_doa) {				
				return l_eligible;
			}			
			
			// see if reservation source meets criteria
			if (in_res_source != null && (in_res_source.equals("N") || in_res_source.equals("C") || in_res_source.equals("M") || in_res_source.equals("G"))) {				
				l_eligible = "T";
			}			
			else {				
				// see if enrollment stay at the hotel
				l_eligible = new pf_isenrollstay_v2().execute(in_acct_id, in_doa, in_doa + in_los);
			}			
			
			if (l_eligible.equals("F")) {				
				l_platinum = new pf_isplatinum_ondoa().execute(in_acct_id, in_doa);				
				if (l_platinum.equals("F")) {					
					l_diamond = new pf_isdiamond_ondoa().execute(in_acct_id, in_doa);
				}				
				
				// if Plat or Diamond on DOA, then eligible regardless of booking source
				if (l_platinum.equals("T") || l_diamond.equals("T")) {					
					l_eligible = "T";
				}
			}			
			
			if (l_eligible.equals("F")) {				
				return l_eligible;
			}			
			
			// all basic criteria have been met and it looks like bonus should be awarded 
			// but do final check on multiple rooms and already awarded bonus.
			
			l_eligible = new pf_wi1211_awardbonus().execute(in_acct_id, in_promo_id, in_stay_type, in_prop_id, in_doa, in_los, in_rm_type, in_srp_code, in_rm_revenue, in_fb_revenue, in_other_revenue, in_curr_code, in_res_source, in_stay_id, l_start_date, l_stop_date);			
			
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