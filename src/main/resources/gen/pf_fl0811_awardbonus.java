/* Generated on 02-25-2013 12:49:24 PM by SPLParser v0.9 */
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

public class pf_fl0811_awardbonus extends AbstractProcedure {

	public String execute(Integer in_acct_id, Integer in_promo_id, String in_stay_type, Integer in_prop_id, Timestamp in_doa, Integer in_los, String in_rm_type, String in_srp_code, Double in_rm_revenue, Double in_fb_revenue, Double in_other_revenue, String in_curr_code, String in_res_source, Integer in_stay_id, Timestamp in_start_date, Timestamp in_stop_date) throws SQLException, ProcedureException {		
		
		/*
		 * pf_fl0811_awardbonus.sql - If this routine is called, then the stay being processed meets the
		 *                           basic Fall 2011 criteria. Now see if the bonus should be awarded; i.e.
		 *                           is there a multi-room stay involved that has already been awarded the 
		 *                           bonus.
		 *                                                            
		 * $Id: pf_fl0811_awardbonus.sql 99 2012-12-19 21:14:54Z michael_giroux $
		 *    
		 *        Copyright (C) 2011 Choice Hotels International, Inc.
		 */		
		
		Timestamp l_start_date;		
		Timestamp l_stop_date;		
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
		Timestamp l_signup_date;		
		Integer l_acct_trans_id;		
		Integer l_ord;		
		Timestamp l_post_dtime;		
		Timestamp l_curr_post_dtime;		
		Integer l_linked_same_doa;		
		String l_debug;		
		String l_already_awarded;		
		Integer l_promo_id_mq;		
		Integer l_promo_id_eq;		
		Integer l_promo_id_md;		
		Integer l_promo_id_ed;		
		Double l_linked_amount;		
		
		Integer l_dbg_stay_id;		
		String l_dbg_prop_cd;		
		Timestamp l_dbg_doa;		
		Timestamp l_dbg_dod;		
		Integer l_dbg_linked_id;		
		Integer l_dbg_acct_trans_id;		
		Integer l_dbg_ord;		
		Timestamp l_dbg_post_dtime;		
		
		Integer sql_error;		
		Integer isam_error;		
		String error_data;		
		
		try {			
			
			l_start_date = null;			
			l_stop_date = null;			
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
			l_signup_date = null;			
			l_acct_trans_id = 0;			
			l_ord = 1;			
			l_post_dtime = null;			
			l_curr_post_dtime = null;			
			l_linked_same_doa = 0;			
			l_debug = "F";			
			l_already_awarded = "F";			
			l_promo_id_mq = null;			
			l_promo_id_eq = null;			
			l_promo_id_md = null;			
			l_promo_id_ed = null;			
			l_linked_amount = 0.0;			
			
			l_dbg_stay_id = null;			
			l_dbg_prop_cd = null;			
			l_dbg_doa = null;			
			l_dbg_dod = null;			
			l_dbg_linked_id = null;			
			l_dbg_acct_trans_id = null;			
			l_dbg_ord = null;			
			l_dbg_post_dtime = null;			
			
			// set up tracing based on app_config entry
			l_debug = new settrace().execute("pf_fl0811_awardbonus");			
			if (l_debug.equals("T")) {				
				setDebugFile("/tmp/pf_fl0811_awardbonus_" + dbinfo("sessionid") + ".trace");				
				trace("on");
			}			
			
			// get promo dates if not passed in
			if (in_start_date == null || in_stop_date == null) {				

				PreparedStatement pstmt1 = prepareStatement(
						  "select start_date, stop_date"
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
				pstmt1.close();
			}			
			else {				
				l_start_date = in_start_date;				
				l_stop_date = in_stop_date;
			}			
			
			// Get the program code of the account

			PreparedStatement pstmt2 = prepareStatement(
					  "select a.recog_cd, a.signup_date"
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
			l_signup_date = rs2.getTimestamp(2);
			pstmt2.close();			
			
			// get promo_id's for all the promos so we can check to see if
			// a bonus  has already been awarded for a multi-room stay. It 
			// could have been awarded for any of them.

			PreparedStatement pstmt3 = prepareStatement(
					  "select promo_id"
					+ " from promo"
					+ " where promo_cd = \"FL0811MQ\""
					+ " and recog_cd = ?");
			
			if (l_recog_cd != null) {
				pstmt3.setString(1, l_recog_cd);
			}
			else {
				pstmt3.setNull(1, Types.JAVA_OBJECT);
			}
			ResultSet rs3 = executeQuery(pstmt3);
			rs3.next();
			l_promo_id_mq = rs3.getInt(1);
			pstmt3.close();			

			PreparedStatement pstmt4 = prepareStatement(
					  "select promo_id"
					+ " from promo"
					+ " where promo_cd = \"FL0811EQ\""
					+ " and recog_cd = ?");
			
			if (l_recog_cd != null) {
				pstmt4.setString(1, l_recog_cd);
			}
			else {
				pstmt4.setNull(1, Types.JAVA_OBJECT);
			}
			ResultSet rs4 = executeQuery(pstmt4);
			rs4.next();
			l_promo_id_eq = rs4.getInt(1);
			pstmt4.close();			

			PreparedStatement pstmt5 = prepareStatement(
					  "select promo_id"
					+ " from promo"
					+ " where promo_cd = \"FL0811MD\""
					+ " and recog_cd = ?");
			
			if (l_recog_cd != null) {
				pstmt5.setString(1, l_recog_cd);
			}
			else {
				pstmt5.setNull(1, Types.JAVA_OBJECT);
			}
			ResultSet rs5 = executeQuery(pstmt5);
			rs5.next();
			l_promo_id_md = rs5.getInt(1);
			pstmt5.close();			

			PreparedStatement pstmt6 = prepareStatement(
					  "select promo_id"
					+ " from promo"
					+ " where promo_cd = \"FL0811ED\""
					+ " and recog_cd = ?");
			
			if (l_recog_cd != null) {
				pstmt6.setString(1, l_recog_cd);
			}
			else {
				pstmt6.setNull(1, Types.JAVA_OBJECT);
			}
			ResultSet rs6 = executeQuery(pstmt6);
			rs6.next();
			l_promo_id_ed = rs6.getInt(1);
			pstmt6.close();			
			
			// Get a list of linked stays to consider

			Iterator<Object> it0 = find_linked_stays_by_promo(in_promo_id, l_start_date, l_stop_date, in_acct_id).iterator();
			l_stay_id = (Integer) it0.next();
			l_prop_cd = (String) it0.next();
			l_doa = (Timestamp) it0.next();
			l_dod = (Timestamp) it0.next();
			l_linked_id = (Integer) it0.next();
			l_trans_id = (Integer) it0.next();
			while (it0.hasNext()) {				
				

				PreparedStatement pstmt7 = prepareStatement(
						  "select post_dtime"
						+ " from stay"
						+ " where stay_id = ?");
				
				if (l_stay_id != null) {
					pstmt7.setInt(1, l_stay_id);
				}
				else {
					pstmt7.setNull(1, Types.JAVA_OBJECT);
				}
				ResultSet rs7 = executeQuery(pstmt7);
				rs7.next();
				l_post_dtime = rs7.getTimestamp(1);
				pstmt7.close();				
				

				PreparedStatement pstmt8 = prepareInsert(
						  "insert into recent_stay_trans_list_fl0811 (stay_id, prop_cd, doa, dod, linked_id, acct_trans_id, ord, post_dtime)"
						+ " values (?, ?, ?, ?, nvl(?, ?), ?, ?, ?)");
				if (l_stay_id != null) {
					pstmt8.setInt(1, l_stay_id);
				}
				else {
					pstmt8.setNull(1, Types.JAVA_OBJECT);
				}
				if (l_prop_cd != null) {
					pstmt8.setString(2, l_prop_cd);
				}
				else {
					pstmt8.setNull(2, Types.JAVA_OBJECT);
				}
				if (l_doa != null) {
					pstmt8.setObject(3, l_doa);
				}
				else {
					pstmt8.setNull(3, Types.JAVA_OBJECT);
				}
				if (l_dod != null) {
					pstmt8.setObject(4, l_dod);
				}
				else {
					pstmt8.setNull(4, Types.JAVA_OBJECT);
				}
				if (l_linked_id != null) {
					pstmt8.setInt(5, l_linked_id);
				}
				else {
					pstmt8.setNull(5, Types.JAVA_OBJECT);
				}
				if (l_stay_id != null) {
					pstmt8.setInt(6, l_stay_id);
				}
				else {
					pstmt8.setNull(6, Types.JAVA_OBJECT);
				}
				if (l_trans_id != null) {
					pstmt8.setInt(7, l_trans_id);
				}
				else {
					pstmt8.setNull(7, Types.JAVA_OBJECT);
				}
				if (l_ord != null) {
					pstmt8.setInt(8, l_ord);
				}
				else {
					pstmt8.setNull(8, Types.JAVA_OBJECT);
				}
				if (l_post_dtime != null) {
					pstmt8.setObject(9, l_post_dtime);
				}
				else {
					pstmt8.setNull(9, Types.JAVA_OBJECT);
				}
				executeUpdate(pstmt8);
				pstmt8.close();				
				l_ord = l_ord + 1;
			}			
			
			// add current stay to list

			PreparedStatement pstmt9 = prepareStatement(
					  "select max(l.id)"
					+ " from recent_stay_trans_list_fl0811 l"
					+ " where l.prop_cd = new get_prop_cd().execute(?)"
					+ " and ("(" || 
					+ ") || (" || 
					+ ")
					+ ")");
			
			if (in_prop_id != null) {
				pstmt9.setInt(1, in_prop_id);
			}
			else {
				pstmt9.setNull(1, Types.JAVA_OBJECT);
			}
			ResultSet rs8 = executeQuery(pstmt9);
			rs8.next();
			n_id = rs8.getInt(1);
			pstmt9.close();			
			
			if (n_id != null) {				

				PreparedStatement pstmt10 = prepareStatement(
						  "select nvl(l.linked_id, l.stay_id)"
						+ " from recent_stay_trans_list_fl0811 l"
						+ " where l.id = ?");
				
				if (n_id != null) {
					pstmt10.setInt(1, n_id);
				}
				else {
					pstmt10.setNull(1, Types.JAVA_OBJECT);
				}
				ResultSet rs9 = executeQuery(pstmt10);
				rs9.next();
				n_link = rs9.getInt(1);
				pstmt10.close();
			}			
			else {				
				n_link = in_stay_id;
			}			
			

			PreparedStatement pstmt11 = prepareStatement(
					  "select post_dtime"
					+ " from stay"
					+ " where stay_id = ?");
			
			if (in_stay_id != null) {
				pstmt11.setInt(1, in_stay_id);
			}
			else {
				pstmt11.setNull(1, Types.JAVA_OBJECT);
			}
			ResultSet rs10 = executeQuery(pstmt11);
			rs10.next();
			l_post_dtime = rs10.getTimestamp(1);
			pstmt11.close();			

			PreparedStatement pstmt12 = prepareInsert(
					  "insert into recent_stay_trans_list_fl0811 (stay_id, prop_cd, doa, dod, linked_id, ord, post_dtime)"
					+ " values (?, new get_prop_cd().execute(?), ?, ?, ?, ?, ?)");
			if (in_stay_id != null) {
				pstmt12.setInt(1, in_stay_id);
			}
			else {
				pstmt12.setNull(1, Types.JAVA_OBJECT);
			}
			if (in_prop_id != null) {
				pstmt12.setInt(2, in_prop_id);
			}
			else {
				pstmt12.setNull(2, Types.JAVA_OBJECT);
			}
			if (in_doa != null) {
				pstmt12.setObject(3, in_doa);
			}
			else {
				pstmt12.setNull(3, Types.JAVA_OBJECT);
			}
			if (f_dod != null) {
				pstmt12.setObject(4, f_dod);
			}
			else {
				pstmt12.setNull(4, Types.JAVA_OBJECT);
			}
			if (n_link != null) {
				pstmt12.setInt(5, n_link);
			}
			else {
				pstmt12.setNull(5, Types.JAVA_OBJECT);
			}
			if (l_ord != null) {
				pstmt12.setInt(6, l_ord);
			}
			else {
				pstmt12.setNull(6, Types.JAVA_OBJECT);
			}
			if (l_curr_post_dtime != null) {
				pstmt12.setObject(7, l_curr_post_dtime);
			}
			else {
				pstmt12.setNull(7, Types.JAVA_OBJECT);
			}
			executeUpdate(pstmt12);
			pstmt12.close();			
			
			// see if this stay is part of a multi-room stay by checking DOAs of the linked
			// stays with the DOA of stay at hand. If multiple rooms found and bonus has 
			// already been awarded, then stay is not eligible.
			

			PreparedStatement pstmt13 = prepareStatement(
					  "select stay_id, acct_trans_id, doa"
					+ " from recent_stay_trans_list_fl0811"
					+ " where linked_id = ?"
					+ " and stay_id != ?"
					+ " order by post_dtime desc");
			
			if (n_link != null) {
				pstmt13.setInt(1, n_link);
			}
			else {
				pstmt13.setNull(1, Types.JAVA_OBJECT);
			}
			if (in_stay_id != null) {
				pstmt13.setInt(2, in_stay_id);
			}
			else {
				pstmt13.setNull(2, Types.JAVA_OBJECT);
			}
			ResultSet rs11 = executeQuery(pstmt13);
			while (rs11.next()) {
				l_stay_id = rs11.getInt(1);
				l_acct_trans_id = rs11.getInt(2);
				l_doa = rs11.getTimestamp(3);				
				
				if (l_doa.equals(in_doa)) {					
					// see if same doa stay has already been awarded another fall bonus
					l_linked_amount = 0;					

					PreparedStatement pstmt14 = prepareStatement(
							  "select ad.amount"
							+ " from acct_trans a, acct_trans_detail ad"
							+ " where a.acct_trans_id = ad.acct_trans_id"
							+ " and a.acct_trans_id = ?"
							+ " and a.rev_acct_trans_id is null"
							+ " and ad.promo_id in (?, ?, ?, ?)");
					
					if (l_acct_trans_id != null) {
						pstmt14.setInt(1, l_acct_trans_id);
					}
					else {
						pstmt14.setNull(1, Types.JAVA_OBJECT);
					}
					if (l_promo_id_mq != null) {
						pstmt14.setInt(2, l_promo_id_mq);
					}
					else {
						pstmt14.setNull(2, Types.JAVA_OBJECT);
					}
					if (l_promo_id_eq != null) {
						pstmt14.setInt(3, l_promo_id_eq);
					}
					else {
						pstmt14.setNull(3, Types.JAVA_OBJECT);
					}
					if (l_promo_id_md != null) {
						pstmt14.setInt(4, l_promo_id_md);
					}
					else {
						pstmt14.setNull(4, Types.JAVA_OBJECT);
					}
					if (l_promo_id_ed != null) {
						pstmt14.setInt(5, l_promo_id_ed);
					}
					else {
						pstmt14.setNull(5, Types.JAVA_OBJECT);
					}
					ResultSet rs12 = executeQuery(pstmt14);
					rs12.next();
					l_linked_amount = rs12.getDouble(1);
					pstmt14.close();					
					if (l_linked_amount > 0) {						
						l_already_awarded = "T";						
						break;
					}
				}
			}
			pstmt13.close();			
			
			if (l_already_awarded.equals("T")) {				
				return "F";
			}			
			return "T";			
			

		}
		catch (SQLException e) {
			sql_error = e.getErrorCode();
			isam_error = 0;
			error_data = e.getMessage();
			{				

				PreparedStatement pstmt15 = prepareStatement(
						  "delete from t_acct_trans_contrib"
						+ " where promo_id = ?");
				if (in_promo_id != null) {
					pstmt15.setInt(1, in_promo_id);
				}
				else {
					pstmt15.setNull(1, Types.JAVA_OBJECT);
				}
				executeUpdate(pstmt15);
				pstmt15.close();				
				throw new ProcedureException(sql_error, isam_error, error_data);
			}
		}
	}

}