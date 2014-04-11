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

public class pf_wi1211_awardbonus extends AbstractProcedure {

	public String execute(Integer in_acct_id, Integer in_promo_id, String in_stay_type, Integer in_prop_id, Timestamp in_doa, Integer in_los, String in_rm_type, String in_srp_code, Double in_rm_revenue, Double in_fb_revenue, Double in_other_revenue, String in_curr_code, String in_res_source, Integer in_stay_id, Timestamp in_start_date, Timestamp in_stop_date) throws SQLException, ProcedureException {		
		
		/*
		 * pf_wi1211_awardbonus.sql - If this routine is called, then the stay being processed meets the
		 *                           basic Winter 2011 criteria. Now see if the bonus should be awarded; i.e.
		 *                           we only need to check to see if any of the consecutive stays have been awarded
		 *                           an airline bonus. Perhaps registered for Points+Miles, then switched to 
		 *                           just be awarded Double Points. If this stay is part of a consecutive stay
		 *                           group, and one of those stays has been awarded a partner bonus, then do not
		 *                           award the double points.
		 *                                                            
		 * $Id: pf_wi1211_awardbonus.sql 99 2012-12-19 21:14:54Z michael_giroux $
		 *    
		 *        Copyright (C) 2011 Choice Hotels International, Inc.
		 */		
		
		//  promo_id's for all the participating partners
		Integer l_aa_promo_id;		
		Integer l_ac_promo_id;		
		Integer l_as_promo_id;		
		Integer l_at_promo_id;		
		Integer l_co_promo_id;		
		Integer l_dl_promo_id;		
		Integer l_fs_promo_id;		
		Integer l_sw_promo_id;		
		Integer l_ua_promo_id;		
		Integer l_us_promo_id;		
		
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
			
			l_aa_promo_id = 0;			
			l_ac_promo_id = 0;			
			l_as_promo_id = 0;			
			l_at_promo_id = 0;			
			l_co_promo_id = 0;			
			l_dl_promo_id = 0;			
			l_fs_promo_id = 0;			
			l_sw_promo_id = 0;			
			l_ua_promo_id = 0;			
			l_us_promo_id = 0;			
			
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
			
			// determine tracing based on app_config
			
			l_debug = new settrace().execute("pf_wi1211_awardbonus");			
			if (l_debug.equals("T")) {				
				setDebugFile("/tmp/pf_wi1211_awardbonus_" + dbinfo("sessionid") + ".trace");				
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
			
			// get all partner promo_id's
			

			PreparedStatement pstmt3 = prepareStatement(
					  "select promo_id"
					+ " from promo"
					+ " where promo_cd = \"WI1211AA\"");
			
			ResultSet rs3 = executeQuery(pstmt3);
			rs3.next();
			l_aa_promo_id = rs3.getInt(1);
			pstmt3.close();			
			

			PreparedStatement pstmt4 = prepareStatement(
					  "select promo_id"
					+ " from promo"
					+ " where promo_cd = \"WI1211AC\"");
			
			ResultSet rs4 = executeQuery(pstmt4);
			rs4.next();
			l_ac_promo_id = rs4.getInt(1);
			pstmt4.close();			
			

			PreparedStatement pstmt5 = prepareStatement(
					  "select promo_id"
					+ " from promo"
					+ " where promo_cd = \"WI1211AS\"");
			
			ResultSet rs5 = executeQuery(pstmt5);
			rs5.next();
			l_as_promo_id = rs5.getInt(1);
			pstmt5.close();			
			

			PreparedStatement pstmt6 = prepareStatement(
					  "select promo_id"
					+ " from promo"
					+ " where promo_cd = \"WI1211AT\"");
			
			ResultSet rs6 = executeQuery(pstmt6);
			rs6.next();
			l_at_promo_id = rs6.getInt(1);
			pstmt6.close();			
			

			PreparedStatement pstmt7 = prepareStatement(
					  "select promo_id"
					+ " from promo"
					+ " where promo_cd = \"WI1211CO\"");
			
			ResultSet rs7 = executeQuery(pstmt7);
			rs7.next();
			l_co_promo_id = rs7.getInt(1);
			pstmt7.close();			
			

			PreparedStatement pstmt8 = prepareStatement(
					  "select promo_id"
					+ " from promo"
					+ " where promo_cd = \"WI1211DL\"");
			
			ResultSet rs8 = executeQuery(pstmt8);
			rs8.next();
			l_dl_promo_id = rs8.getInt(1);
			pstmt8.close();			
			

			PreparedStatement pstmt9 = prepareStatement(
					  "select promo_id"
					+ " from promo"
					+ " where promo_cd = \"WI1211FS\"");
			
			ResultSet rs9 = executeQuery(pstmt9);
			rs9.next();
			l_fs_promo_id = rs9.getInt(1);
			pstmt9.close();			
			

			PreparedStatement pstmt10 = prepareStatement(
					  "select promo_id"
					+ " from promo"
					+ " where promo_cd = \"WI1211SW\"");
			
			ResultSet rs10 = executeQuery(pstmt10);
			rs10.next();
			l_sw_promo_id = rs10.getInt(1);
			pstmt10.close();			
			

			PreparedStatement pstmt11 = prepareStatement(
					  "select promo_id"
					+ " from promo"
					+ " where promo_cd = \"WI1211UA\"");
			
			ResultSet rs11 = executeQuery(pstmt11);
			rs11.next();
			l_ua_promo_id = rs11.getInt(1);
			pstmt11.close();			
			

			PreparedStatement pstmt12 = prepareStatement(
					  "select promo_id"
					+ " from promo"
					+ " where promo_cd = \"WI1211US\"");
			
			ResultSet rs12 = executeQuery(pstmt12);
			rs12.next();
			l_us_promo_id = rs12.getInt(1);
			pstmt12.close();			
			
			// Get a list of linked stays to consider

			Iterator<Object> it0 = find_linked_stays_by_promo(in_promo_id, l_start_date, l_stop_date, in_acct_id).iterator();
			l_stay_id = (Integer) it0.next();
			l_prop_cd = (String) it0.next();
			l_doa = (Timestamp) it0.next();
			l_dod = (Timestamp) it0.next();
			l_linked_id = (Integer) it0.next();
			l_trans_id = (Integer) it0.next();
			while (it0.hasNext()) {				
				

				PreparedStatement pstmt13 = prepareStatement(
						  "select post_dtime"
						+ " from stay"
						+ " where stay_id = ?");
				
				if (l_stay_id != null) {
					pstmt13.setInt(1, l_stay_id);
				}
				else {
					pstmt13.setNull(1, Types.JAVA_OBJECT);
				}
				ResultSet rs13 = executeQuery(pstmt13);
				rs13.next();
				l_post_dtime = rs13.getTimestamp(1);
				pstmt13.close();				
				

				PreparedStatement pstmt14 = prepareInsert(
						  "insert into recent_stay_trans_list_wi1211 (stay_id, prop_cd, doa, dod, linked_id, acct_trans_id, ord, post_dtime)"
						+ " values (?, ?, ?, ?, nvl(?, ?), ?, ?, ?)");
				if (l_stay_id != null) {
					pstmt14.setInt(1, l_stay_id);
				}
				else {
					pstmt14.setNull(1, Types.JAVA_OBJECT);
				}
				if (l_prop_cd != null) {
					pstmt14.setString(2, l_prop_cd);
				}
				else {
					pstmt14.setNull(2, Types.JAVA_OBJECT);
				}
				if (l_doa != null) {
					pstmt14.setObject(3, l_doa);
				}
				else {
					pstmt14.setNull(3, Types.JAVA_OBJECT);
				}
				if (l_dod != null) {
					pstmt14.setObject(4, l_dod);
				}
				else {
					pstmt14.setNull(4, Types.JAVA_OBJECT);
				}
				if (l_linked_id != null) {
					pstmt14.setInt(5, l_linked_id);
				}
				else {
					pstmt14.setNull(5, Types.JAVA_OBJECT);
				}
				if (l_stay_id != null) {
					pstmt14.setInt(6, l_stay_id);
				}
				else {
					pstmt14.setNull(6, Types.JAVA_OBJECT);
				}
				if (l_trans_id != null) {
					pstmt14.setInt(7, l_trans_id);
				}
				else {
					pstmt14.setNull(7, Types.JAVA_OBJECT);
				}
				if (l_ord != null) {
					pstmt14.setInt(8, l_ord);
				}
				else {
					pstmt14.setNull(8, Types.JAVA_OBJECT);
				}
				if (l_post_dtime != null) {
					pstmt14.setObject(9, l_post_dtime);
				}
				else {
					pstmt14.setNull(9, Types.JAVA_OBJECT);
				}
				executeUpdate(pstmt14);
				pstmt14.close();				
				l_ord = l_ord + 1;
			}			
			
			// add current stay to list

			PreparedStatement pstmt15 = prepareStatement(
					  "select max(l.id)"
					+ " from recent_stay_trans_list_wi1211 l"
					+ " where l.prop_cd = new get_prop_cd().execute(?)"
					+ " and ("(" || 
					+ ") || (" || 
					+ ")
					+ ")");
			
			if (in_prop_id != null) {
				pstmt15.setInt(1, in_prop_id);
			}
			else {
				pstmt15.setNull(1, Types.JAVA_OBJECT);
			}
			ResultSet rs14 = executeQuery(pstmt15);
			rs14.next();
			n_id = rs14.getInt(1);
			pstmt15.close();			
			
			if (n_id != null) {				

				PreparedStatement pstmt16 = prepareStatement(
						  "select nvl(l.linked_id, l.stay_id)"
						+ " from recent_stay_trans_list_wi1211 l"
						+ " where l.id = ?");
				
				if (n_id != null) {
					pstmt16.setInt(1, n_id);
				}
				else {
					pstmt16.setNull(1, Types.JAVA_OBJECT);
				}
				ResultSet rs15 = executeQuery(pstmt16);
				rs15.next();
				n_link = rs15.getInt(1);
				pstmt16.close();
			}			
			else {				
				n_link = in_stay_id;
			}			
			

			PreparedStatement pstmt17 = prepareStatement(
					  "select post_dtime"
					+ " from stay"
					+ " where stay_id = ?");
			
			if (in_stay_id != null) {
				pstmt17.setInt(1, in_stay_id);
			}
			else {
				pstmt17.setNull(1, Types.JAVA_OBJECT);
			}
			ResultSet rs16 = executeQuery(pstmt17);
			rs16.next();
			l_post_dtime = rs16.getTimestamp(1);
			pstmt17.close();			

			PreparedStatement pstmt18 = prepareInsert(
					  "insert into recent_stay_trans_list_wi1211 (stay_id, prop_cd, doa, dod, linked_id, ord, post_dtime)"
					+ " values (?, new get_prop_cd().execute(?), ?, ?, ?, ?, ?)");
			if (in_stay_id != null) {
				pstmt18.setInt(1, in_stay_id);
			}
			else {
				pstmt18.setNull(1, Types.JAVA_OBJECT);
			}
			if (in_prop_id != null) {
				pstmt18.setInt(2, in_prop_id);
			}
			else {
				pstmt18.setNull(2, Types.JAVA_OBJECT);
			}
			if (in_doa != null) {
				pstmt18.setObject(3, in_doa);
			}
			else {
				pstmt18.setNull(3, Types.JAVA_OBJECT);
			}
			if (f_dod != null) {
				pstmt18.setObject(4, f_dod);
			}
			else {
				pstmt18.setNull(4, Types.JAVA_OBJECT);
			}
			if (n_link != null) {
				pstmt18.setInt(5, n_link);
			}
			else {
				pstmt18.setNull(5, Types.JAVA_OBJECT);
			}
			if (l_ord != null) {
				pstmt18.setInt(6, l_ord);
			}
			else {
				pstmt18.setNull(6, Types.JAVA_OBJECT);
			}
			if (l_curr_post_dtime != null) {
				pstmt18.setObject(7, l_curr_post_dtime);
			}
			else {
				pstmt18.setNull(7, Types.JAVA_OBJECT);
			}
			executeUpdate(pstmt18);
			pstmt18.close();			
			
			// see if this stay is part of any consecutive stay and already
			// awarded an airline bonus. If so, we can not award the double points
			

			PreparedStatement pstmt19 = prepareStatement(
					  "select stay_id, acct_trans_id, doa"
					+ " from recent_stay_trans_list_wi1211"
					+ " where linked_id = ?"
					+ " and stay_id != ?"
					+ " order by post_dtime desc");
			
			if (n_link != null) {
				pstmt19.setInt(1, n_link);
			}
			else {
				pstmt19.setNull(1, Types.JAVA_OBJECT);
			}
			if (in_stay_id != null) {
				pstmt19.setInt(2, in_stay_id);
			}
			else {
				pstmt19.setNull(2, Types.JAVA_OBJECT);
			}
			ResultSet rs17 = executeQuery(pstmt19);
			while (rs17.next()) {
				l_stay_id = rs17.getInt(1);
				l_acct_trans_id = rs17.getInt(2);
				l_doa = rs17.getTimestamp(3);				
				
				// not worried about multiple rooms yet
				// if (l_doa = in_doa) then
				// see if same doa stay has already been awarded another winter bonus
				l_linked_amount = 0;				

				PreparedStatement pstmt20 = prepareStatement(
						  "select ad.amount"
						+ " from acct_trans a, acct_trans_detail ad"
						+ " where a.acct_trans_id = ad.acct_trans_id"
						+ " and a.stay_id = ?"
						+ " and a.rev_acct_trans_id is null"
						+ " and ad.promo_id in (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
				
				if (l_stay_id != null) {
					pstmt20.setInt(1, l_stay_id);
				}
				else {
					pstmt20.setNull(1, Types.JAVA_OBJECT);
				}
				if (l_aa_promo_id != null) {
					pstmt20.setInt(2, l_aa_promo_id);
				}
				else {
					pstmt20.setNull(2, Types.JAVA_OBJECT);
				}
				if (l_ac_promo_id != null) {
					pstmt20.setInt(3, l_ac_promo_id);
				}
				else {
					pstmt20.setNull(3, Types.JAVA_OBJECT);
				}
				if (l_as_promo_id != null) {
					pstmt20.setInt(4, l_as_promo_id);
				}
				else {
					pstmt20.setNull(4, Types.JAVA_OBJECT);
				}
				if (l_at_promo_id != null) {
					pstmt20.setInt(5, l_at_promo_id);
				}
				else {
					pstmt20.setNull(5, Types.JAVA_OBJECT);
				}
				if (l_co_promo_id != null) {
					pstmt20.setInt(6, l_co_promo_id);
				}
				else {
					pstmt20.setNull(6, Types.JAVA_OBJECT);
				}
				if (l_dl_promo_id != null) {
					pstmt20.setInt(7, l_dl_promo_id);
				}
				else {
					pstmt20.setNull(7, Types.JAVA_OBJECT);
				}
				if (l_fs_promo_id != null) {
					pstmt20.setInt(8, l_fs_promo_id);
				}
				else {
					pstmt20.setNull(8, Types.JAVA_OBJECT);
				}
				if (l_sw_promo_id != null) {
					pstmt20.setInt(9, l_sw_promo_id);
				}
				else {
					pstmt20.setNull(9, Types.JAVA_OBJECT);
				}
				if (l_ua_promo_id != null) {
					pstmt20.setInt(10, l_ua_promo_id);
				}
				else {
					pstmt20.setNull(10, Types.JAVA_OBJECT);
				}
				if (l_us_promo_id != null) {
					pstmt20.setInt(11, l_us_promo_id);
				}
				else {
					pstmt20.setNull(11, Types.JAVA_OBJECT);
				}
				ResultSet rs18 = executeQuery(pstmt20);
				rs18.next();
				l_linked_amount = rs18.getDouble(1);
				pstmt20.close();				
				if (l_linked_amount > 0) {					
					l_already_awarded = "T";					
					break;
				}
			}
			pstmt19.close();			
			
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

				PreparedStatement pstmt21 = prepareStatement(
						  "delete from t_acct_trans_contrib"
						+ " where promo_id = ?");
				if (in_promo_id != null) {
					pstmt21.setInt(1, in_promo_id);
				}
				else {
					pstmt21.setNull(1, Types.JAVA_OBJECT);
				}
				executeUpdate(pstmt21);
				pstmt21.close();				
				throw new ProcedureException(sql_error, isam_error, error_data);
			}
		}
	}

}