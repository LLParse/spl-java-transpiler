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

public class pf_sp0111 extends AbstractProcedure {

	public String execute(Integer in_acct_id, Integer in_promo_id, String in_stay_type, Integer in_prop_id, Timestamp in_doa, Integer in_los, String in_rm_type, String in_srp_code, Double in_rm_revenue, Double in_fb_revenue, Double in_other_revenue, String in_curr_code, String in_res_source, Integer in_stay_id) throws SQLException, ProcedureException {		
		
		/*
		 * pf_sp0111.sql - Check if stay is eligible for the 'Spring 2011 Triple Points' promotion.
		 *                 The filter applies to both midscale and economy. The rules are:
		 *                 
		 *                 - If non-elite or Gold, must book centrally through ch.com or CRS.
		 *                 - If Plat/Diamond, can book through any channel.
		 *                 - If CP Mexico or CP Europe, can book through any channel.
		 *                 - Must be at least the second stay.
		 *                 Other filters related to Spring 2011 will determine whether accounts have gone 
		 *                 through registration to earn those awards.
		 *    
		 * $Id: pf_sp0111.sql 39481 2011-06-02 00:13:17Z rshepher $
		 *    
		 *        Copyright (C) 2010 Choice Hotels International, Inc.
		 */		
		Timestamp start_date;		
		Timestamp stop_date;		
		String l_recog_cd;		
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
		String l_eligible;		
		String l_platinum;		
		String l_diamond;		
		String l_table_created;		
		String l_debug;		
		Integer l_acct_trans_id;		
		Integer l_already_awarded;		
		Double l_linked_amount;		
		Integer l_double_points_promo_id;		
		Integer l_howmanystays;		
		
		Integer sql_error;		
		Integer isam_error;		
		String error_data;		
		
		try {			
			
			start_date = null;			
			stop_date = null;			
			l_recog_cd = null;			
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
			l_eligible = "F";			
			l_platinum = "F";			
			l_diamond = "F";			
			l_table_created = "F";			
			l_debug = "F";			
			l_acct_trans_id = 0;			
			l_already_awarded = 0;			
			l_linked_amount = 0.0;			
			l_double_points_promo_id = 0;			
			l_howmanystays = 0;			
			
			// set up tracing based on app_config entry
			l_debug = new settrace().execute("pf_sp0111");			
			if (l_debug.equals("T")) {				
				setDebugFile("/tmp/pf_sp0111.trace");				
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
			
			// if booked centrally, then the stay is eligible for further checking
			// else, look at the elite level and program code for possible eligibility
			if (in_res_source != null && (in_res_source.equals("C") || in_res_source.equals("N") || in_res_source.equals("M"))) {				
				l_eligible = "T";
			}			
			else {				
				l_platinum = new pf_isplatinum_ondoa().execute(in_acct_id, in_doa);				
				if (l_platinum.equals("F")) {					
					l_diamond = new pf_isdiamond_ondoa().execute(in_acct_id, in_doa);
				}				
				if (l_platinum.equals("T") || l_diamond.equals("T")) {					
					l_eligible = "T";
				}				
				else {					
					if (l_recog_cd.equals("CE") || l_recog_cd.equals("MC")) {						
						l_eligible = "T";
					}
				}
			}			
			
			if (l_eligible.equals("F")) {				
				return "F";
			}			
			
			l_table_created = "T";			
			
			// Get a list of linked stays to consider, regardless of promo since midscale and
			// economy all count together in bonus determination

			Iterator<Object> it0 = find_linked_stays(in_acct_id, start_date, stop_date).iterator();
			l_stay_id = (Integer) it0.next();
			l_prop_cd = (String) it0.next();
			l_doa = (Timestamp) it0.next();
			l_dod = (Timestamp) it0.next();
			l_linked_id = (Integer) it0.next();
			l_trans_id = (Integer) it0.next();
			while (it0.hasNext()) {				
				

				PreparedStatement pstmt3 = prepareStatement(
						  "select post_dtime"
						+ " from stay"
						+ " where stay_id = ?");
				
				if (l_stay_id != null) {
					pstmt3.setInt(1, l_stay_id);
				}
				else {
					pstmt3.setNull(1, Types.JAVA_OBJECT);
				}
				ResultSet rs3 = executeQuery(pstmt3);
				rs3.next();
				l_post_dtime = rs3.getTimestamp(1);
				pstmt3.close();				
				

				PreparedStatement pstmt4 = prepareInsert(
						  "insert into recent_stay_trans_list_sp11 (stay_id, prop_cd, doa, dod, linked_id, acct_trans_id, ord, post_dtime)"
						+ " values (?, ?, ?, ?, nvl(?, ?), ?, ?, ?)");
				if (l_stay_id != null) {
					pstmt4.setInt(1, l_stay_id);
				}
				else {
					pstmt4.setNull(1, Types.JAVA_OBJECT);
				}
				if (l_prop_cd != null) {
					pstmt4.setString(2, l_prop_cd);
				}
				else {
					pstmt4.setNull(2, Types.JAVA_OBJECT);
				}
				if (l_doa != null) {
					pstmt4.setObject(3, l_doa);
				}
				else {
					pstmt4.setNull(3, Types.JAVA_OBJECT);
				}
				if (l_dod != null) {
					pstmt4.setObject(4, l_dod);
				}
				else {
					pstmt4.setNull(4, Types.JAVA_OBJECT);
				}
				if (l_linked_id != null) {
					pstmt4.setInt(5, l_linked_id);
				}
				else {
					pstmt4.setNull(5, Types.JAVA_OBJECT);
				}
				if (l_stay_id != null) {
					pstmt4.setInt(6, l_stay_id);
				}
				else {
					pstmt4.setNull(6, Types.JAVA_OBJECT);
				}
				if (l_trans_id != null) {
					pstmt4.setInt(7, l_trans_id);
				}
				else {
					pstmt4.setNull(7, Types.JAVA_OBJECT);
				}
				if (l_ord != null) {
					pstmt4.setInt(8, l_ord);
				}
				else {
					pstmt4.setNull(8, Types.JAVA_OBJECT);
				}
				if (l_post_dtime != null) {
					pstmt4.setObject(9, l_post_dtime);
				}
				else {
					pstmt4.setNull(9, Types.JAVA_OBJECT);
				}
				executeUpdate(pstmt4);
				pstmt4.close();				
				l_ord = l_ord + 1;
			}			
			
			// add current stay to list

			PreparedStatement pstmt5 = prepareStatement(
					  "select max(l.id)"
					+ " from recent_stay_trans_list_sp11 l"
					+ " where l.prop_cd = new get_prop_cd().execute(?)"
					+ " and ("(" || 
					+ ") || (" || 
					+ ")
					+ ")");
			
			if (in_prop_id != null) {
				pstmt5.setInt(1, in_prop_id);
			}
			else {
				pstmt5.setNull(1, Types.JAVA_OBJECT);
			}
			ResultSet rs4 = executeQuery(pstmt5);
			rs4.next();
			n_id = rs4.getInt(1);
			pstmt5.close();			
			
			if (n_id != null) {				

				PreparedStatement pstmt6 = prepareStatement(
						  "select nvl(l.linked_id, l.stay_id)"
						+ " from recent_stay_trans_list_sp11 l"
						+ " where l.id = ?");
				
				if (n_id != null) {
					pstmt6.setInt(1, n_id);
				}
				else {
					pstmt6.setNull(1, Types.JAVA_OBJECT);
				}
				ResultSet rs5 = executeQuery(pstmt6);
				rs5.next();
				n_link = rs5.getInt(1);
				pstmt6.close();
			}			
			else {				
				n_link = in_stay_id;
			}			
			

			PreparedStatement pstmt7 = prepareStatement(
					  "select post_dtime"
					+ " from stay"
					+ " where stay_id = ?");
			
			if (in_stay_id != null) {
				pstmt7.setInt(1, in_stay_id);
			}
			else {
				pstmt7.setNull(1, Types.JAVA_OBJECT);
			}
			ResultSet rs6 = executeQuery(pstmt7);
			rs6.next();
			l_post_dtime = rs6.getTimestamp(1);
			pstmt7.close();			

			PreparedStatement pstmt8 = prepareInsert(
					  "insert into recent_stay_trans_list_sp11 (stay_id, prop_cd, doa, dod, linked_id, ord, post_dtime)"
					+ " values (?, new get_prop_cd().execute(?), ?, ?, ?, ?, ?)");
			if (in_stay_id != null) {
				pstmt8.setInt(1, in_stay_id);
			}
			else {
				pstmt8.setNull(1, Types.JAVA_OBJECT);
			}
			if (in_prop_id != null) {
				pstmt8.setInt(2, in_prop_id);
			}
			else {
				pstmt8.setNull(2, Types.JAVA_OBJECT);
			}
			if (in_doa != null) {
				pstmt8.setObject(3, in_doa);
			}
			else {
				pstmt8.setNull(3, Types.JAVA_OBJECT);
			}
			if (f_dod != null) {
				pstmt8.setObject(4, f_dod);
			}
			else {
				pstmt8.setNull(4, Types.JAVA_OBJECT);
			}
			if (n_link != null) {
				pstmt8.setInt(5, n_link);
			}
			else {
				pstmt8.setNull(5, Types.JAVA_OBJECT);
			}
			if (l_ord != null) {
				pstmt8.setInt(6, l_ord);
			}
			else {
				pstmt8.setNull(6, Types.JAVA_OBJECT);
			}
			if (l_post_dtime != null) {
				pstmt8.setObject(7, l_post_dtime);
			}
			else {
				pstmt8.setNull(7, Types.JAVA_OBJECT);
			}
			executeUpdate(pstmt8);
			pstmt8.close();			
			
			// see if this stay is part of a multi-room stay by checking DOAs of the linked
			// stays with the DOA of stay at hand. If multiple rooms found and bonus has 
			// already been awarded, then stay is not eligible.
			

			PreparedStatement pstmt9 = prepareStatement(
					  "select stay_id, acct_trans_id, doa"
					+ " from recent_stay_trans_list_sp11"
					+ " where linked_id = ?"
					+ " and stay_id != ?"
					+ " order by post_dtime desc");
			
			if (n_link != null) {
				pstmt9.setInt(1, n_link);
			}
			else {
				pstmt9.setNull(1, Types.JAVA_OBJECT);
			}
			if (in_stay_id != null) {
				pstmt9.setInt(2, in_stay_id);
			}
			else {
				pstmt9.setNull(2, Types.JAVA_OBJECT);
			}
			ResultSet rs7 = executeQuery(pstmt9);
			while (rs7.next()) {
				l_stay_id = rs7.getInt(1);
				l_acct_trans_id = rs7.getInt(2);
				l_doa = rs7.getTimestamp(3);				
				
				if (l_doa.equals(in_doa)) {					
					// see if same doa stay has already been awarded the triple point bonus
					l_linked_amount = 0;					

					PreparedStatement pstmt10 = prepareStatement(
							  "select ad.amount"
							+ " from acct_trans a, acct_trans_detail ad"
							+ " where a.acct_trans_id = ad.acct_trans_id"
							+ " and a.acct_trans_id = ?"
							+ " and a.rev_acct_trans_id is null"
							+ " and ad.promo_id = ?");
					
					if (l_acct_trans_id != null) {
						pstmt10.setInt(1, l_acct_trans_id);
					}
					else {
						pstmt10.setNull(1, Types.JAVA_OBJECT);
					}
					if (in_promo_id != null) {
						pstmt10.setInt(2, in_promo_id);
					}
					else {
						pstmt10.setNull(2, Types.JAVA_OBJECT);
					}
					ResultSet rs8 = executeQuery(pstmt10);
					rs8.next();
					l_linked_amount = rs8.getDouble(1);
					pstmt10.close();					
					if (l_linked_amount > 0) {						
						l_already_awarded = 1;						
						break;
					}					
					// if GP, make sure the same doa stay did not receive the double points bonus
					if (l_recog_cd.equals("GP")) {						

						PreparedStatement pstmt11 = prepareStatement(
								  "select p.promo_id"
								+ " from promo p"
								+ " where p.recog_cd = \"GP\""
								+ " and p.promo_cd = \"SP0111DM\"");
						
						ResultSet rs9 = executeQuery(pstmt11);
						rs9.next();
						l_double_points_promo_id = rs9.getInt(1);
						pstmt11.close();						
						l_linked_amount = 0;						

						PreparedStatement pstmt12 = prepareStatement(
								  "select ad.amount"
								+ " from acct_trans a, acct_trans_detail ad"
								+ " where a.acct_trans_id = ad.acct_trans_id"
								+ " and a.acct_trans_id = ?"
								+ " and a.rev_acct_trans_id is null"
								+ " and ad.promo_id = ?");
						
						if (l_acct_trans_id != null) {
							pstmt12.setInt(1, l_acct_trans_id);
						}
						else {
							pstmt12.setNull(1, Types.JAVA_OBJECT);
						}
						if (l_double_points_promo_id != null) {
							pstmt12.setInt(2, l_double_points_promo_id);
						}
						else {
							pstmt12.setNull(2, Types.JAVA_OBJECT);
						}
						ResultSet rs10 = executeQuery(pstmt12);
						rs10.next();
						l_linked_amount = rs10.getDouble(1);
						pstmt12.close();						
						if (l_linked_amount > 0) {							
							l_already_awarded = 1;							
							break;
						}
					}
				}
			}
			pstmt9.close();			
			if (l_already_awarded.equals(1)) {				
				return "F";
			}			
			
			// see if they have at least two stays, as bonus doesn't start until second stay
			// the stay at hand has been inserted into the temp table.

			PreparedStatement pstmt13 = prepareStatement(
					  "select count(*)"
					+ " from ("
						+ "select doa, count(*)"
						+ " from recent_stay_trans_list_sp11"
						+ " group by 1"
					+ ")");
			
			ResultSet rs11 = executeQuery(pstmt13);
			rs11.next();
			l_howmanystays = rs11.getInt(1);
			pstmt13.close();			
			
			if (l_howmanystays < 2) {				
				return "F";
			}			
			else {				
				return "T";
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