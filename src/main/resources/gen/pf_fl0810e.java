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

public class pf_fl0810e extends AbstractProcedure {

	public String execute(Integer in_acct_id, Integer in_promo_id, String in_stay_type, Integer in_prop_id, Timestamp in_doa, Integer in_los, String in_rm_type, String in_srp_code, Double in_rm_revenue, Double in_fb_revenue, Double in_other_revenue, String in_curr_code, String in_res_source, Integer in_stay_id) throws SQLException, ProcedureException {		
		
		/*
		 * pf_fl0810e.sql - Check if stay is eligible for the Fall 2010 economy and extended stay promotion
		 *                  where stays earn double points. 
		 *                      
		 *                  Rules are:
		 *                     - Must be booked via ch.com or central res
		 *                     - unless: Platinum or Diamond can book via any channel
		 *                     - unless: Enrollment stay is eligible regardless of booking channel or elite status
		 *                     - unless: CP Mexico and CP Europe accounts can book via any channel
		 *           
		 * $Id: pf_fl0810e.sql 99 2012-12-19 21:14:54Z michael_giroux $
		 *    
		 *        Copyright (C) 2010 Choice Hotels International, Inc.
		 */		
		
		Timestamp l_start_date;		
		Timestamp l_stop_date;		
		Timestamp l_signup_date;		
		String l_promo_cd;		
		String l_recog_cd;		
		String l_answer;		
		String l_platinum;		
		String l_diamond;		
		String l_eligible;		
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
		
		l_start_date = null;		
		l_stop_date = null;		
		l_signup_date = null;		
		l_promo_cd = null;		
		l_recog_cd = null;		
		l_answer = "F";		// assume the worst
		l_platinum = null;		
		l_diamond = null;		
		l_eligible = "F";		
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
		
		//set debug file to '/tmp/pf_fl0810e.trace';
		//trace on;
		
		// Check for valid doa

		PreparedStatement pstmt1 = prepareStatement(
				  "select start_date, stop_date, promo_cd"
				+ " from promo"
				+ " where promo_id = ?");
		
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
			return l_answer;
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
			return "F";
		}		
		
		
		// centrally booked stay is eligible
		if (in_res_source != null && (in_res_source.equals("C") || in_res_source.equals("N"))) {			
			l_eligible = "T";
		}		
		
		// check elite level and if Platinum/Diamond then stay is eligible
		if (l_eligible.equals("F")) {			
			l_platinum = new pf_isplatinum_ondoa().execute(in_acct_id, in_doa);			
			if (l_platinum.equals("F")) {				
				l_diamond = new pf_isdiamond_ondoa().execute(in_acct_id, in_doa);
			}			
			if (l_platinum.equals("T") || l_diamond.equals("T")) {				
				l_eligible = "T";
			}
		}		
		
		// if still not eligible, check for signup stay or CP Latin America, CP Europe accounts
		if (l_eligible.equals("F")) {			

			PreparedStatement pstmt4 = prepareStatement(
					  "select recog_cd, signup_date"
					+ " from acct"
					+ " where acct_id = ?");
			
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
			if (l_signup_date.equals(in_doa) || l_recog_cd.equals("MC") || l_recog_cd.equals("CE")) {				
				l_eligible = "T";
			}
		}		
		
		// basic criteria not met so deny
		if (l_eligible.equals("F")) {			
			return l_answer;
		}		
		
		// basic checks passed, now check for multi room, consecutive stay criteria
		l_answer = new pf_fl0810e_awardbonus().execute(in_acct_id, in_promo_id, in_stay_type, in_prop_id, in_doa, in_los, in_rm_type, in_srp_code, in_rm_revenue, in_fb_revenue, in_other_revenue, in_curr_code, in_res_source, in_stay_id);		
		
		return l_answer;
	}

}