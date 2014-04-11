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

public class pf_fl0810mt extends AbstractProcedure {

	public String execute(Integer in_acct_id, Integer in_promo_id, String in_stay_type, Integer in_prop_id, Timestamp in_doa, Integer in_los, String in_rm_type, String in_srp_code, Double in_rm_revenue, Double in_fb_revenue, Double in_other_revenue, String in_curr_code, String in_res_source, Integer in_stay_id) throws SQLException, ProcedureException {		
		
		/*
		 * pf_fl0810mt.sql - Fall 2010 promo filter for midscale the promo to allow triple points for
		 *                   Plat/Diam registered accounts. Even though the input account is registered, we
		 *                   need to ensure that the account is at the elite Platinum or Diamond level (since
		 *                   we allow anyone/account to register because they could reach elite Plat or Diamond
		 *                   level at some point during the promotion) and
		 *                   that the DOA is after the registration date before we give out the triple points.
		 *    
		 * $Id: pf_fl0810mt.sql 99 2012-12-19 21:14:54Z michael_giroux $
		 *    
		 *        Copyright (C) 2010 Choice Hotels International, Inc.
		 */		
		Timestamp l_start_date;		
		Timestamp l_stop_date;		
		String l_promo_cd;		
		String l_recog_cd;		
		Timestamp l_signup_date;		
		Integer l_offer_id;		
		String l_platinum;		
		String l_diamond;		
		String l_registered;		
		String l_eligible;		
		String l_false;		
		String l_answer;		
		Integer l_appl_group;		
		String l_chain_id;		
		String l_mkt_area;		
		String l_country;		
		String l_prop_country;		
		String l_prop_type;		
		String l_ioc_region;		
		String l_prop_class;		
		String l_prop_curr_cd;		
		String l_stay_type;		
		Integer l_prop_chk;		
		
		Integer sql_error;		
		Integer isam_error;		
		String error_data;		
		
		try {			
			
			l_start_date = null;			
			l_stop_date = null;			
			l_promo_cd = null;			
			l_recog_cd = null;			
			l_signup_date = null;			
			l_offer_id = null;			
			l_platinum = "F";			
			l_diamond = "F";			
			l_registered = "F";			
			l_eligible = "F";			
			l_false = "F";			
			l_answer = null;			
			l_appl_group = 0;			
			l_chain_id = null;			
			l_mkt_area = null;			
			l_country = null;			
			l_prop_country = null;			
			l_prop_type = null;			
			l_ioc_region = null;			
			l_prop_class = null;			
			l_prop_curr_cd = null;			
			l_stay_type = null;			
			l_prop_chk = 0;			
			
			//set debug file to '/tmp/pf_fl0810mt.trace';
			//trace on;
			
			// Check for valid doa

			PreparedStatement pstmt1 = prepareStatement(
					  "select promo.start_date, promo.stop_date, promo.promo_cd"
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
			l_promo_cd = rs1.getString(3);
			pstmt1.close();			
			
			if (in_doa < l_start_date || in_doa > l_stop_date) {				
				return l_false;
			}			
			
			// need to check if the current stay property is valid before doing anything 
			// else. For example, a midscale stay can show up as the current stay and
			// it will get processed as a economy! 
			

			PreparedStatement pstmt2 = prepareStatement(
					  "select stay_type, prop_curr_cd"
					+ " from stay"
					+ " where stay_id = ?");
			
			if (in_stay_id != null) {
				pstmt2.setInt(1, in_stay_id);
			}
			else {
				pstmt2.setNull(1, Types.JAVA_OBJECT);
			}
			ResultSet rs2 = executeQuery(pstmt2);
			rs2.next();
			l_stay_type = rs2.getString(1);
			l_prop_curr_cd = rs2.getString(2);
			pstmt2.close();			
			

			PreparedStatement pstmt3 = prepareStatement(
					  "select chain_id, mkt_area, country, prop_type, ioc_region, prop_class, country"
					+ " from prop"
					+ " where prop_id = ?");
			
			if (in_prop_id != null) {
				pstmt3.setInt(1, in_prop_id);
			}
			else {
				pstmt3.setNull(1, Types.JAVA_OBJECT);
			}
			ResultSet rs3 = executeQuery(pstmt3);
			rs3.next();
			l_chain_id = rs3.getString(1);
			l_mkt_area = rs3.getString(2);
			l_country = rs3.getString(3);
			l_prop_type = rs3.getString(4);
			l_ioc_region = rs3.getString(5);
			l_prop_class = rs3.getString(6);
			l_prop_country = rs3.getString(7);
			pstmt3.close();			
			
			l_prop_chk = new chk_prop_partic().execute(in_acct_id, in_prop_id, in_doa, in_los, in_rm_type, in_srp_code, in_rm_revenue, in_fb_revenue, in_other_revenue, in_promo_id, l_recog_cd, l_prop_curr_cd, l_appl_group, l_chain_id, l_mkt_area, l_prop_country, l_prop_type, l_ioc_region, l_prop_class, in_stay_id, l_stay_type);			
			
			if (l_prop_chk.equals(0)) {				
				return l_answer;
			}			
			
			// get some account info

			PreparedStatement pstmt4 = prepareStatement(
					  "select a.recog_cd, a.signup_date"
					+ " from acct a"
					+ " where a.acct_id = ?");
			
			if (in_acct_id != null) {
				pstmt4.setInt(1, in_acct_id);
			}
			else {
				pstmt4.setNull(1, Types.JAVA_OBJECT);
			}
			ResultSet rs4 = executeQuery(pstmt4);
			rs4.next();
			l_recog_cd = rs4.getString(1);
			l_signup_date = rs4.getTimestamp(2);
			pstmt4.close();			
			
			l_platinum = new pf_isplatinum_ondoa().execute(in_acct_id, in_doa);			
			if (l_platinum.equals("F")) {				
				l_diamond = new pf_isdiamond_ondoa().execute(in_acct_id, in_doa);
			}			
			
			// if not at least Plat or Diamond on DOA, then not eligible for this offer/promo
			if (l_platinum.equals("F") && l_diamond.equals("F")) {				
				return l_false;
			}			
			
			// see if they were registered prior to the DOA

			PreparedStatement pstmt5 = prepareStatement(
					  "select o.offer_id"
					+ " from offer o"
					+ " where o.offer_cd = \"FL0810TR\""
					+ " and o.recog_cd = ?");
			
			if (l_recog_cd != null) {
				pstmt5.setString(1, l_recog_cd);
			}
			else {
				pstmt5.setNull(1, Types.JAVA_OBJECT);
			}
			ResultSet rs5 = executeQuery(pstmt5);
			rs5.next();
			l_offer_id = rs5.getInt(1);
			pstmt5.close();			
			l_registered = new pf_isregistered().execute(l_offer_id, in_acct_id, in_doa);			
			if (l_registered.equals("F")) {				
				return l_false;
			}			
			
			// If we are here, then we have a Plat or Diamond stay registered for the triple points offer. 
			// Additionally, it doesn't matter how the stay was booked, as Plat/Diamond can book through
			// any channel. 
			// Now, see if we should award the triple points for the stay. Must be at least the second 
			// stay after registering and bonus must not have been awarded previously for the stay; i.e.
			// do the multi-room stay check.
			
			l_answer = new pf_fl0810mt_awardbonus().execute(in_acct_id, in_promo_id, in_stay_type, in_prop_id, in_doa, in_los, in_rm_type, in_srp_code, in_rm_revenue, in_fb_revenue, in_other_revenue, in_curr_code, in_res_source, in_stay_id);			
			
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