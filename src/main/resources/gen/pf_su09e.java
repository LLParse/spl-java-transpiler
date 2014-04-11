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

public class pf_su09e extends AbstractProcedure {

	public String execute(Integer in_acct_id, Integer in_promo_id, String in_stay_type, Integer in_prop_id, Timestamp in_doa, Integer in_los, String in_rm_type, String in_srp_code, Double in_rm_revenue, Double in_fb_revenue, Double in_other_revenue, String in_curr_code, String in_res_source, Integer in_stay_id) throws SQLException, ProcedureException {		
		
		/*
		 * pf_su09e.sql - Check if stay is eligible for the Summer 2009 economy and extended stay promotion
		 *                where stays earn double points. 
		 *                      
		 *                Rules are:
		 *                     - Must be booked via ch.com or central res
		 *                     - unless: Platinum or Diamond can book via any channel
		 *                     - unless: Enrollment stay is eligible regardless of booking channel or elite status
		 *           
		 * $Id: pf_su09e.sql 99 2012-12-19 21:14:54Z michael_giroux $
		 *    
		 *        Copyright (C) 2009 Choice Hotels International, Inc.
		 */		
		Integer no_bonus_cnt;		
		Integer has_bonus_cnt;		
		Integer des_bonus_cnt;		
		Integer stay_cnt;		
		Timestamp start_date;		
		Timestamp stop_date;		
		String l_recog_cd;		
		Integer check_promo_id;		
		Integer l_stay_id;		
		String l_prop_cd;		
		Timestamp l_doa;		
		Timestamp l_dod;		
		Integer l_linked_id;		
		Integer l_trans_id;		
		String l_name;		
		Timestamp f_dod;		
		Integer n_id;		
		Integer n_link;		
		Timestamp offer_dtime;		
		String l_res_source;		
		Timestamp l_signup_date;		
		
		no_bonus_cnt = 0;		
		has_bonus_cnt = 0;		
		des_bonus_cnt = 0;		
		stay_cnt = 0;		
		start_date = null;		
		stop_date = null;		
		l_recog_cd = null;		
		check_promo_id = null;		
		l_stay_id = null;		
		l_prop_cd = null;		
		l_doa = null;		
		l_dod = null;		
		l_linked_id = null;		
		l_trans_id = null;		
		l_name = null;		
		f_dod = in_doa + in_los;		
		n_id = null;		
		n_link = null;		
		offer_dtime = null;		
		l_res_source = null;		
		l_signup_date = null;		
		
		//set debug file to '/tmp/pf_su09e.trace';
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
		start_date = rs1.getTimestamp(1);
		stop_date = rs1.getTimestamp(2);
		pstmt1.close();		
		
		if (in_doa < start_date || in_doa > stop_date) {			
			return "F";
		}		
		
		// Get the program code of the account

		PreparedStatement pstmt2 = prepareStatement(
				  "select acct.recog_cd, acct.signup_date"
				+ " from acct"
				+ " where acct.acct_id = ?");
		
		if (in_acct_id != null) {
			pstmt2.setInt(1, in_acct_id);
		}
		else {
			pstmt2.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs2 = executeQuery(pstmt2);
		rs2.next();
		l_recog_cd = rs2.getString(1);
		l_signup_date = rs2.getTimestamp(2);
		pstmt2.close();		
		
		// Get promo id

		PreparedStatement pstmt3 = prepareStatement(
				  "select promo.promo_id"
				+ " from promo"
				+ " where promo.recog_cd = ?"
				+ " and promo.promo_cd = \"SU09E\"");
		
		if (l_recog_cd != null) {
			pstmt3.setString(1, l_recog_cd);
		}
		else {
			pstmt3.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs3 = executeQuery(pstmt3);
		rs3.next();
		check_promo_id = rs3.getInt(1);
		pstmt3.close();		
		
		// Get elite level at time of doa

		PreparedStatement pstmt4 = prepareStatement(
				  "select elvl.name"
				+ " from acct_elite_level_hist aelh, elite_level elvl"
				+ " where aelh.acct_id = ?"
				+ " and aelh.elite_level_id = elvl.elite_level_id"
				+ " and aelh.acct_elite_level_hist_id = ("				

					+ "select max(els.acct_elite_level_hist_id)"
					+ " from acct_elite_level_hist els"
					+ "  inner join elite_level es on els.elite_level_id = es.elite_level_id"
					+ " where els.acct_id = ?"
					+ " and els.date_acquired <= ?"
					+ " and year(es.eff_year) = year(?)"
				+ ")");
		
		if (in_acct_id != null) {
			pstmt4.setInt(1, in_acct_id);
		}
		else {
			pstmt4.setNull(1, Types.JAVA_OBJECT);
		}
		if (in_acct_id != null) {
			pstmt4.setInt(2, in_acct_id);
		}
		else {
			pstmt4.setNull(2, Types.JAVA_OBJECT);
		}
		if (in_doa != null) {
			pstmt4.setObject(3, in_doa);
		}
		else {
			pstmt4.setNull(3, Types.JAVA_OBJECT);
		}
		if (in_doa != null) {
			pstmt4.setObject(4, in_doa);
		}
		else {
			pstmt4.setNull(4, Types.JAVA_OBJECT);
		}
		ResultSet rs4 = executeQuery(pstmt4);
		rs4.next();
		l_name = rs4.getString(1);
		pstmt4.close();		
		
		// enrollment stay eligible regardless of booking source
		if (l_signup_date.equals(in_doa)) {			
			return "T";
		}		
		
		// Platinum or Diamond are eligible regardless of booking source
		if (l_name != null) {			
			if (l_name.equals("Platinum") || l_name.equals("Diamond")) {				
				return "T";
			}
		}		
		
		// non-elite or Gold must book through ch.com or central res
		if (l_name == null || l_name.equals("Gold")) {			
			if (in_res_source == null || (!in_res_source.equals("C") && !in_res_source.equals("N"))) {				
				return "F";
			}
		}		
		
		// non-elite or Gold, booked through ch.com or central res
		return "T";
	}

}