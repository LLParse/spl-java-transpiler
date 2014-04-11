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

public class pf_sp0111r5 extends AbstractProcedure {

	public String execute(Integer in_acct_id, Integer in_promo_id, String in_stay_type, Integer in_prop_id, Timestamp in_doa, Integer in_los, String in_rm_type, String in_srp_code, Double in_rm_revenue, Double in_fb_revenue, Double in_other_revenue, String in_curr_code, String in_res_source, Integer in_stay_id) throws SQLException, ProcedureException {		
		
		/*
		 * pf_sp0111r5.sql - Check if stay is eligible for the 'Spring 2011 500 bonus points' promotion.
		 *                   The filter targets elite Platinum and Diamond accounts. The rules are:
		 *                  
		 *                   - Must be Platinum or Diamond and must have registered for the offer on or
		 *                     before the DOA.
		 *                   - Must have been booked via ch.com.
		 *                   - Must be at least the second stay on or after registration date.
		 *                   - Must be GP. Programs AU, CE, CN, and MC are not eligible.
		 *    
		 * $Id: pf_sp0111r5.sql 35705 2011-01-17 17:53:44Z rshepher $
		 *    
		 *        Copyright (C) 2010 Choice Hotels International, Inc.
		 */		
		
		Timestamp start_date;		
		Timestamp stop_date;		
		String l_fullkey;		
		String l_value;		
		String l_platinum;		
		String l_diamond;		
		Timestamp l_offer_dtime;		
		String l_recog_cd;		
		String l_table_created;		
		Integer l_stay_id;		
		String l_prop_cd;		
		Timestamp l_doa;		
		Timestamp l_dod;		
		Integer l_linked_id;		
		Integer l_trans_id;		
		Timestamp f_dod;		
		Integer n_id;		
		Integer n_link;		
		Timestamp l_post_dtime;		
		Integer l_ord;		
		String l_debug;		
		Integer l_acct_trans_id;		
		Integer l_already_awarded;		
		Double l_linked_amount;		
		Integer l_howmanystays;		
		
		Integer sql_error;		
		Integer isam_error;		
		String error_data;		
		
		try {			
			
			start_date = null;			
			stop_date = null;			
			l_fullkey = "stored.procedure.debug.pf_sp0111r5";			
			l_value = "F";			
			l_platinum = "F";			
			l_diamond = "F";			
			l_offer_dtime = null;			
			l_recog_cd = null;			
			l_table_created = "F";			
			l_stay_id = null;			
			l_linked_id = null;			
			l_prop_cd = null;			
			l_doa = null;			
			l_dod = null;			
			l_trans_id = null;			
			f_dod = in_doa + in_los;			
			n_id = null;			
			n_link = null;			
			l_post_dtime = null;			
			l_ord = 1;			
			l_debug = "F";			
			l_acct_trans_id = 0;			
			l_already_awarded = 0;			
			l_linked_amount = 0.0;			
			l_howmanystays = 0;			
			
			// set up tracing based on app_config entry
			l_debug = new settrace().execute("pf_sp0111r5");			
			if (l_debug.equals("T")) {				
				setDebugFile("/tmp/pf_sp0111r5.trace");				
				trace("on");
			}			
			
			// sanity check just to make sure
			if (in_rm_revenue == null || in_rm_revenue.equals(0.0)) {				
				return "F";
			}			
			

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
			
			// Get the program code of the account

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
			
			// AU, CE, CN, and MC not eligible. These accounts probably never get here since there is
			// no promo record for these programs, but just in case....
			if (l_recog_cd.equals("AU") || l_recog_cd.equals("CE") || l_recog_cd.equals("CN") || l_recog_cd.equals("MC")) {				
				return "F";
			}			
			
			// check reservation source; must be Internet
			if (in_res_source == null || !in_res_source.equals("N")) {				
				return "F";
			}			
			
			// check elite level; must be Platinum or Diamond
			l_platinum = new pf_isplatinum_ondoa().execute(in_acct_id, in_doa);			
			if (l_platinum.equals("F")) {				
				l_diamond = new pf_isdiamond_ondoa().execute(in_acct_id, in_doa);
			}			
			
			if (!l_platinum.equals("T") && !l_diamond.equals("T")) {				
				return "F";
			}			
			
			// check they are registered for the offer on or before the DOA

			PreparedStatement pstmt3 = prepareStatement(
					  "select offer_dtime"
					+ " from acct_offer ao"
					+ " where ao.acct_id = ?"
					+ " and ao.offer_id = ("
						+ "select offer_id"
						+ " from offer"
						+ " where offer_cd = \"SP0111R\""
					+ ")");
			
			if (in_acct_id != null) {
				pstmt3.setInt(1, in_acct_id);
			}
			else {
				pstmt3.setNull(1, Types.JAVA_OBJECT);
			}
			ResultSet rs3 = executeQuery(pstmt3);
			rs3.next();
			l_offer_dtime = rs3.getTimestamp(1);
			pstmt3.close();			
			
			if (l_offer_dtime == null || l_offer_dtime > in_doa) {				
				return "F";
			}			
			
			l_table_created = "T";			
			
			// Get a list of linked stays to consider , regardless of promo since they
			// all count together in bonus determination

			Iterator<Object> it0 = find_linked_stays(in_acct_id, start_date, stop_date).iterator();
			l_stay_id = (Integer) it0.next();
			l_prop_cd = (String) it0.next();
			l_doa = (Timestamp) it0.next();
			l_dod = (Timestamp) it0.next();
			l_linked_id = (Integer) it0.next();
			l_trans_id = (Integer) it0.next();
			while (it0.hasNext()) {				
				

				PreparedStatement pstmt4 = prepareStatement(
						  "select post_dtime"
						+ " from stay"
						+ " where stay_id = ?");
				
				if (l_stay_id != null) {
					pstmt4.setInt(1, l_stay_id);
				}
				else {
					pstmt4.setNull(1, Types.JAVA_OBJECT);
				}
				ResultSet rs4 = executeQuery(pstmt4);
				rs4.next();
				l_post_dtime = rs4.getTimestamp(1);
				pstmt4.close();				
				

				PreparedStatement pstmt5 = prepareInsert(
						  "insert into recent_stay_trans_list_sp11r5 (stay_id, prop_cd, doa, dod, linked_id, acct_trans_id, ord, post_dtime)"
						+ " values (?, ?, ?, ?, nvl(?, ?), ?, ?, ?)");
				if (l_stay_id != null) {
					pstmt5.setInt(1, l_stay_id);
				}
				else {
					pstmt5.setNull(1, Types.JAVA_OBJECT);
				}
				if (l_prop_cd != null) {
					pstmt5.setString(2, l_prop_cd);
				}
				else {
					pstmt5.setNull(2, Types.JAVA_OBJECT);
				}
				if (l_doa != null) {
					pstmt5.setObject(3, l_doa);
				}
				else {
					pstmt5.setNull(3, Types.JAVA_OBJECT);
				}
				if (l_dod != null) {
					pstmt5.setObject(4, l_dod);
				}
				else {
					pstmt5.setNull(4, Types.JAVA_OBJECT);
				}
				if (l_linked_id != null) {
					pstmt5.setInt(5, l_linked_id);
				}
				else {
					pstmt5.setNull(5, Types.JAVA_OBJECT);
				}
				if (l_stay_id != null) {
					pstmt5.setInt(6, l_stay_id);
				}
				else {
					pstmt5.setNull(6, Types.JAVA_OBJECT);
				}
				if (l_trans_id != null) {
					pstmt5.setInt(7, l_trans_id);
				}
				else {
					pstmt5.setNull(7, Types.JAVA_OBJECT);
				}
				if (l_ord != null) {
					pstmt5.setInt(8, l_ord);
				}
				else {
					pstmt5.setNull(8, Types.JAVA_OBJECT);
				}
				if (l_post_dtime != null) {
					pstmt5.setObject(9, l_post_dtime);
				}
				else {
					pstmt5.setNull(9, Types.JAVA_OBJECT);
				}
				executeUpdate(pstmt5);
				pstmt5.close();				
				l_ord = l_ord + 1;
			}			
			
			// add current stay to list

			PreparedStatement pstmt6 = prepareStatement(
					  "select max(l.id)"
					+ " from recent_stay_trans_list_sp11r5 l"
					+ " where l.prop_cd = new get_prop_cd().execute(?)"
					+ " and ("(" || 
					+ ") || (" || 
					+ ")
					+ ")");
			
			if (in_prop_id != null) {
				pstmt6.setInt(1, in_prop_id);
			}
			else {
				pstmt6.setNull(1, Types.JAVA_OBJECT);
			}
			ResultSet rs5 = executeQuery(pstmt6);
			rs5.next();
			n_id = rs5.getInt(1);
			pstmt6.close();			
			
			if (n_id != null) {				

				PreparedStatement pstmt7 = prepareStatement(
						  "select nvl(l.linked_id, l.stay_id)"
						+ " from recent_stay_trans_list_sp11r5 l"
						+ " where l.id = ?");
				
				if (n_id != null) {
					pstmt7.setInt(1, n_id);
				}
				else {
					pstmt7.setNull(1, Types.JAVA_OBJECT);
				}
				ResultSet rs6 = executeQuery(pstmt7);
				rs6.next();
				n_link = rs6.getInt(1);
				pstmt7.close();
			}			
			else {				
				n_link = in_stay_id;
			}			
			

			PreparedStatement pstmt8 = prepareStatement(
					  "select post_dtime"
					+ " from stay"
					+ " where stay_id = ?");
			
			if (in_stay_id != null) {
				pstmt8.setInt(1, in_stay_id);
			}
			else {
				pstmt8.setNull(1, Types.JAVA_OBJECT);
			}
			ResultSet rs7 = executeQuery(pstmt8);
			rs7.next();
			l_post_dtime = rs7.getTimestamp(1);
			pstmt8.close();			

			PreparedStatement pstmt9 = prepareInsert(
					  "insert into recent_stay_trans_list_sp11r5 (stay_id, prop_cd, doa, dod, linked_id, ord, post_dtime)"
					+ " values (?, new get_prop_cd().execute(?), ?, ?, ?, ?, ?)");
			if (in_stay_id != null) {
				pstmt9.setInt(1, in_stay_id);
			}
			else {
				pstmt9.setNull(1, Types.JAVA_OBJECT);
			}
			if (in_prop_id != null) {
				pstmt9.setInt(2, in_prop_id);
			}
			else {
				pstmt9.setNull(2, Types.JAVA_OBJECT);
			}
			if (in_doa != null) {
				pstmt9.setObject(3, in_doa);
			}
			else {
				pstmt9.setNull(3, Types.JAVA_OBJECT);
			}
			if (f_dod != null) {
				pstmt9.setObject(4, f_dod);
			}
			else {
				pstmt9.setNull(4, Types.JAVA_OBJECT);
			}
			if (n_link != null) {
				pstmt9.setInt(5, n_link);
			}
			else {
				pstmt9.setNull(5, Types.JAVA_OBJECT);
			}
			if (l_ord != null) {
				pstmt9.setInt(6, l_ord);
			}
			else {
				pstmt9.setNull(6, Types.JAVA_OBJECT);
			}
			if (l_post_dtime != null) {
				pstmt9.setObject(7, l_post_dtime);
			}
			else {
				pstmt9.setNull(7, Types.JAVA_OBJECT);
			}
			executeUpdate(pstmt9);
			pstmt9.close();			
			
			// see if this stay is part of a multi-room stay by checking DOAs of the linked
			// stays with the DOA of stay at hand. If multiple rooms found and bonus has 
			// already been awarded, then stay is not eligible.
			

			PreparedStatement pstmt10 = prepareStatement(
					  "select stay_id, acct_trans_id, doa"
					+ " from recent_stay_trans_list_sp11r5"
					+ " where linked_id = ?"
					+ " and stay_id != ?"
					+ " order by post_dtime desc");
			
			if (n_link != null) {
				pstmt10.setInt(1, n_link);
			}
			else {
				pstmt10.setNull(1, Types.JAVA_OBJECT);
			}
			if (in_stay_id != null) {
				pstmt10.setInt(2, in_stay_id);
			}
			else {
				pstmt10.setNull(2, Types.JAVA_OBJECT);
			}
			ResultSet rs8 = executeQuery(pstmt10);
			while (rs8.next()) {
				l_stay_id = rs8.getInt(1);
				l_acct_trans_id = rs8.getInt(2);
				l_doa = rs8.getTimestamp(3);				
				
				if (l_doa.equals(in_doa)) {					
					// see if same doa stay has already been awarded the current, 500 point bonus
					l_linked_amount = 0;					

					PreparedStatement pstmt11 = prepareStatement(
							  "select ad.amount"
							+ " from acct_trans a, acct_trans_detail ad"
							+ " where a.acct_trans_id = ad.acct_trans_id"
							+ " and a.acct_trans_id = ?"
							+ " and a.rev_acct_trans_id is null"
							+ " and ad.promo_id = ?");
					
					if (l_acct_trans_id != null) {
						pstmt11.setInt(1, l_acct_trans_id);
					}
					else {
						pstmt11.setNull(1, Types.JAVA_OBJECT);
					}
					if (in_promo_id != null) {
						pstmt11.setInt(2, in_promo_id);
					}
					else {
						pstmt11.setNull(2, Types.JAVA_OBJECT);
					}
					ResultSet rs9 = executeQuery(pstmt11);
					rs9.next();
					l_linked_amount = rs9.getDouble(1);
					pstmt11.close();					
					if (l_linked_amount > 0) {						
						l_already_awarded = 1;						
						break;
					}
				}
			}
			pstmt10.close();			
			if (l_already_awarded.equals(1)) {				
				return "F";
			}			
			
			// check for at least two stays after registration

			PreparedStatement pstmt12 = prepareStatement(
					  "select count(*)"
					+ " from ("
						+ "select doa, count(*)"
						+ " from recent_stay_trans_list_sp11r5"
						+ " where doa >= ?"
						+ " group by 1"
					+ ")");
			
			if (l_offer_dtime != null) {
				pstmt12.setObject(1, l_offer_dtime);
			}
			else {
				pstmt12.setNull(1, Types.JAVA_OBJECT);
			}
			ResultSet rs10 = executeQuery(pstmt12);
			rs10.next();
			l_howmanystays = rs10.getInt(1);
			pstmt12.close();			
			
			if (l_howmanystays > 1) {				
				return "T";
			}			
			else {				
				return "F";
			}			
			

		}
		catch (SQLException e) {
			sql_error = e.getErrorCode();
			isam_error = 0;
			error_data = e.getMessage();
			{				
				if (l_table_created.equals("T")) {
				}				
				throw new ProcedureException(sql_error, isam_error, error_data);
			}
		}
	}

}