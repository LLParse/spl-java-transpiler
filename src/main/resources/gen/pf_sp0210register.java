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

public class pf_sp0210register extends AbstractProcedure {

	public String execute(Integer in_acct_id, Integer in_promo_id, String in_stay_type, Integer in_prop_id, Timestamp in_doa, Integer in_los, String in_rm_type, String in_srp_code, Double in_rm_revenue, Double in_fb_revenue, Double in_other_revenue, String in_curr_code, String in_res_source, Integer in_stay_id) throws SQLException, ProcedureException {		
		
		/*
		 * pf_sp0210register.sql - Spring 2010 1+1 = Free: Check if account is elite platinum or 
		 *                         diamond on DOA and registered for Spring 2010 offer or if the
		 *                         stay at hand is an enrollment stay. If so, then check if bonus 
		 *                         should be awarded.
		 *    
		 * $Id: pf_sp0210register.sql 99 2012-12-19 21:14:54Z michael_giroux $
		 * 
		 *       Copyright (C) 2010 Choice Hotels International, Inc.
		 */		
		
		Timestamp l_start_date;		
		Timestamp l_stop_date;		
		String l_answer;		
		String l_name;		
		Integer l_acct_id;		
		String l_recog_cd;		
		Integer l_offer_id;		
		Timestamp l_offer_dtime;		
		String l_isenrollstay;		
		String l_platinum;		
		String l_diamond;		
		
		Integer sql_error;		
		Integer isam_error;		
		String error_data;		
		
		try {			
			
			l_start_date = null;			
			l_stop_date = null;			
			l_answer = null;			
			l_name = null;			
			l_acct_id = null;			
			l_recog_cd = null;			
			l_offer_id = null;			
			l_offer_dtime = null;			
			l_isenrollstay = null;			
			l_platinum = "F";			
			l_diamond = "F";			
			
			//set debug file to '/tmp/pf_sp0210register.trace';
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
			
			// if enrollment stay, proceed to check if bonus should be awarded
			l_isenrollstay = new pf_isenrollstay().execute(in_acct_id, in_doa);			
			if (l_isenrollstay.equals("T")) {				
				l_answer = pf_sp0210awardbonus(in_acct_id, in_promo_id, in_stay_type, in_prop_id, in_doa, in_los, in_rm_type, in_srp_code, in_rm_revenue, in_fb_revenue, in_other_revenue, in_curr_code, in_res_source, in_stay_id);				
				return l_answer;
			}			
			
			// check elite level and if Platinum/Diamond and registered, then see
			// if the stay triggers the bonus.
			l_platinum = new pf_isplatinum_ondoa().execute(in_acct_id, in_doa);			
			if (l_platinum.equals("F")) {				
				l_diamond = new pf_isdiamond_ondoa().execute(in_acct_id, in_doa);
			}			
			
			if (l_platinum.equals("F") && l_diamond.equals("F")) {				
				return "F";
			}			
			
			// We are either Platinum or Diamond so get info on the account

			PreparedStatement pstmt2 = prepareStatement(
					  "select a.recog_cd"
					+ " from acct a"
					+ " where a.acct_id = ?");
			
			if (in_acct_id != null) {
				pstmt2.setInt(1, in_acct_id);
			}
			else {
				pstmt2.setNull(1, Types.JAVA_OBJECT);
			}
			ResultSet rs2 = executeQuery(pstmt2);
			rs2.next();
			l_recog_cd = rs2.getString(1);
			pstmt2.close();			
			
			// see if registered for offer for promo, raise exception if no offer found
			// or multiple offers returned. There should only be one offer per recog_cd
			// for Spring 2010, but there could be multiple promo_acct_elig

			PreparedStatement pstmt3 = prepareStatement(
					  "select pae.offer_id"
					+ " from promo_acct_elig pae"
					+ " where pae.promo_id = ?"
					+ " and pae.recog_cd = ?"
					+ " and pae.offer_id is not null");
			
			if (in_promo_id != null) {
				pstmt3.setInt(1, in_promo_id);
			}
			else {
				pstmt3.setNull(1, Types.JAVA_OBJECT);
			}
			if (l_recog_cd != null) {
				pstmt3.setString(2, l_recog_cd);
			}
			else {
				pstmt3.setNull(2, Types.JAVA_OBJECT);
			}
			ResultSet rs3 = executeQuery(pstmt3);
			rs3.next();
			l_offer_id = rs3.getInt(1);
			pstmt3.close();			
			
			if (l_offer_id == null) {				
				throw new ProcedureException(-746, 0, "pf_sp0210register: no promo_acct-elig/offer found for Spring 2010 promo");
			}			
			
			// get date of registration for the account

			PreparedStatement pstmt4 = prepareStatement(
					  "select date(ao.offer_dtime)"
					+ " from acct_offer ao"
					+ " where ao.acct_id = ?"
					+ " and ao.offer_id = ?");
			
			if (in_acct_id != null) {
				pstmt4.setInt(1, in_acct_id);
			}
			else {
				pstmt4.setNull(1, Types.JAVA_OBJECT);
			}
			if (l_offer_id != null) {
				pstmt4.setInt(2, l_offer_id);
			}
			else {
				pstmt4.setNull(2, Types.JAVA_OBJECT);
			}
			ResultSet rs4 = executeQuery(pstmt4);
			rs4.next();
			l_offer_dtime = rs4.getTimestamp(1);
			pstmt4.close();			
			
			// check if they registered for this offer before the stay
			if (l_offer_dtime == null || l_offer_dtime > in_doa) {				
				return "F";
			}			
			
			// Platinum or Diamond and registered so check if bonus should be awarded
			
			l_answer = pf_sp0210awardbonus(in_acct_id, in_promo_id, in_stay_type, in_prop_id, in_doa, in_los, in_rm_type, in_srp_code, in_rm_revenue, in_fb_revenue, in_other_revenue, in_curr_code, in_res_source, in_stay_id);			
			
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