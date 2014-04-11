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

public class pf_ny0711_awardbonus extends AbstractProcedure {

	public String execute(Integer in_acct_id, Integer in_promo_id, String in_stay_type, Integer in_prop_id, Timestamp in_doa, Integer in_los, String in_rm_type, String in_srp_code, Double in_rm_revenue, Double in_fb_revenue, Double in_other_revenue, String in_curr_code, String in_res_source, Integer in_stay_id) throws SQLException, ProcedureException {		
		
		/*
		 * pf_ny0711_awardbonus.sql - Called to see if the bonus has already been awarded for another room
		 *                           
		 *    
		 * $Id: pf_ny0711_awardbonus.sql 99 2012-12-19 21:14:54Z michael_giroux $
		 *    
		 *        Copyright (C) 2011 Choice Hotels International, Inc.
		 */		
		
		Timestamp l_start_date;		
		Timestamp l_stop_date;		
		Integer l_stay_id;		
		String l_prop_cd;		
		Timestamp l_doa;		
		Timestamp l_dod;		
		Integer l_linked_id;		
		Integer l_trans_id;		
		Timestamp f_dod;		
		Integer n_id;		
		Integer n_link;		
		Integer l_ord;		
		Timestamp l_post_dtime;		
		String l_elig;		
		String l_debug;		
		Integer l_dod2;		
		Integer l_bonus_cnt;		
		
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
			l_stay_id = null;			
			l_linked_id = null;			
			l_prop_cd = null;			
			l_doa = null;			
			l_dod = null;			
			l_trans_id = null;			
			f_dod = in_doa + in_los;			
			n_id = null;			
			n_link = null;			
			l_ord = 1;			
			l_post_dtime = null;			
			l_elig = "T";			
			l_debug = "F";			
			l_dod2 = null;			
			l_bonus_cnt = 0;			
			
			l_dbg_stay_id = null;			
			l_dbg_prop_cd = null;			
			l_dbg_doa = null;			
			l_dbg_dod = null;			
			l_dbg_linked_id = null;			
			l_dbg_acct_trans_id = null;			
			l_dbg_ord = null;			
			l_dbg_post_dtime = null;			
			
			// set up tracing based on app_config entry
			l_debug = new settrace().execute("pf_ny0711_awardbonus");			
			if (l_debug.equals("T")) {				
				setDebugFile("/tmp/pf_ny0711_awardbonus_" + dbinfo("sessionid") + ".trace");				
				trace("on");
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
			l_start_date = rs1.getTimestamp(1);
			l_stop_date = rs1.getTimestamp(2);
			pstmt1.close();			
			
			
			// Get a list of linked stays to consider

			Iterator<Object> it0 = find_linked_stays_by_promo(in_promo_id, l_start_date, l_stop_date, in_acct_id).iterator();
			l_stay_id = (Integer) it0.next();
			l_prop_cd = (String) it0.next();
			l_doa = (Timestamp) it0.next();
			l_dod = (Timestamp) it0.next();
			l_linked_id = (Integer) it0.next();
			l_trans_id = (Integer) it0.next();
			while (it0.hasNext()) {				
				

				PreparedStatement pstmt2 = prepareStatement(
						  "select post_dtime"
						+ " from stay"
						+ " where stay_id = ?");
				
				if (l_stay_id != null) {
					pstmt2.setInt(1, l_stay_id);
				}
				else {
					pstmt2.setNull(1, Types.JAVA_OBJECT);
				}
				ResultSet rs2 = executeQuery(pstmt2);
				rs2.next();
				l_post_dtime = rs2.getTimestamp(1);
				pstmt2.close();				
				

				PreparedStatement pstmt3 = prepareInsert(
						  "insert into recent_stay_trans_list_ny0711 (stay_id, prop_cd, doa, dod, linked_id, acct_trans_id, ord, post_dtime)"
						+ " values (?, ?, ?, ?, nvl(?, ?), ?, ?, ?)");
				if (l_stay_id != null) {
					pstmt3.setInt(1, l_stay_id);
				}
				else {
					pstmt3.setNull(1, Types.JAVA_OBJECT);
				}
				if (l_prop_cd != null) {
					pstmt3.setString(2, l_prop_cd);
				}
				else {
					pstmt3.setNull(2, Types.JAVA_OBJECT);
				}
				if (l_doa != null) {
					pstmt3.setObject(3, l_doa);
				}
				else {
					pstmt3.setNull(3, Types.JAVA_OBJECT);
				}
				if (l_dod != null) {
					pstmt3.setObject(4, l_dod);
				}
				else {
					pstmt3.setNull(4, Types.JAVA_OBJECT);
				}
				if (l_linked_id != null) {
					pstmt3.setInt(5, l_linked_id);
				}
				else {
					pstmt3.setNull(5, Types.JAVA_OBJECT);
				}
				if (l_stay_id != null) {
					pstmt3.setInt(6, l_stay_id);
				}
				else {
					pstmt3.setNull(6, Types.JAVA_OBJECT);
				}
				if (l_trans_id != null) {
					pstmt3.setInt(7, l_trans_id);
				}
				else {
					pstmt3.setNull(7, Types.JAVA_OBJECT);
				}
				if (l_ord != null) {
					pstmt3.setInt(8, l_ord);
				}
				else {
					pstmt3.setNull(8, Types.JAVA_OBJECT);
				}
				if (l_post_dtime != null) {
					pstmt3.setObject(9, l_post_dtime);
				}
				else {
					pstmt3.setNull(9, Types.JAVA_OBJECT);
				}
				executeUpdate(pstmt3);
				pstmt3.close();				
				l_ord = l_ord + 1;
			}			
			
			// add current stay to list

			PreparedStatement pstmt4 = prepareStatement(
					  "select max(l.id)"
					+ " from recent_stay_trans_list_ny0711 l"
					+ " where l.prop_cd = new get_prop_cd().execute(?)"
					+ " and ("(" || 
					+ ") || (" || 
					+ ")
					+ ")");
			
			if (in_prop_id != null) {
				pstmt4.setInt(1, in_prop_id);
			}
			else {
				pstmt4.setNull(1, Types.JAVA_OBJECT);
			}
			ResultSet rs3 = executeQuery(pstmt4);
			rs3.next();
			n_id = rs3.getInt(1);
			pstmt4.close();			
			
			if (n_id != null) {				

				PreparedStatement pstmt5 = prepareStatement(
						  "select nvl(l.linked_id, l.stay_id)"
						+ " from recent_stay_trans_list_ny0711 l"
						+ " where l.id = ?");
				
				if (n_id != null) {
					pstmt5.setInt(1, n_id);
				}
				else {
					pstmt5.setNull(1, Types.JAVA_OBJECT);
				}
				ResultSet rs4 = executeQuery(pstmt5);
				rs4.next();
				n_link = rs4.getInt(1);
				pstmt5.close();
			}			
			else {				
				n_link = in_stay_id;
			}			
			

			PreparedStatement pstmt6 = prepareStatement(
					  "select post_dtime"
					+ " from stay"
					+ " where stay_id = ?");
			
			if (in_stay_id != null) {
				pstmt6.setInt(1, in_stay_id);
			}
			else {
				pstmt6.setNull(1, Types.JAVA_OBJECT);
			}
			ResultSet rs5 = executeQuery(pstmt6);
			rs5.next();
			l_post_dtime = rs5.getTimestamp(1);
			pstmt6.close();			

			PreparedStatement pstmt7 = prepareInsert(
					  "insert into recent_stay_trans_list_ny0711 (stay_id, prop_cd, doa, dod, linked_id, ord, post_dtime)"
					+ " values (?, new get_prop_cd().execute(?), ?, ?, ?, ?, ?)");
			if (in_stay_id != null) {
				pstmt7.setInt(1, in_stay_id);
			}
			else {
				pstmt7.setNull(1, Types.JAVA_OBJECT);
			}
			if (in_prop_id != null) {
				pstmt7.setInt(2, in_prop_id);
			}
			else {
				pstmt7.setNull(2, Types.JAVA_OBJECT);
			}
			if (in_doa != null) {
				pstmt7.setObject(3, in_doa);
			}
			else {
				pstmt7.setNull(3, Types.JAVA_OBJECT);
			}
			if (f_dod != null) {
				pstmt7.setObject(4, f_dod);
			}
			else {
				pstmt7.setNull(4, Types.JAVA_OBJECT);
			}
			if (n_link != null) {
				pstmt7.setInt(5, n_link);
			}
			else {
				pstmt7.setNull(5, Types.JAVA_OBJECT);
			}
			if (l_ord != null) {
				pstmt7.setInt(6, l_ord);
			}
			else {
				pstmt7.setNull(6, Types.JAVA_OBJECT);
			}
			if (l_post_dtime != null) {
				pstmt7.setObject(7, l_post_dtime);
			}
			else {
				pstmt7.setNull(7, Types.JAVA_OBJECT);
			}
			executeUpdate(pstmt7);
			pstmt7.close();			
			
			// Stay is eligible and may trigger a bonus, but first check to see if this stay is a multi-room
			// stay and we are only awarding one bonus for multiple rooms
			
			if (l_debug.equals("T")) {				
				

				PreparedStatement pstmt8 = prepareStatement(
						  "select stay_id, prop_cd, doa, dod, linked_id, acct_trans_id, ord, post_dtime"
						+ " from recent_stay_trans_list_ny0711");
				
				ResultSet rs6 = executeQuery(pstmt8);
				while (rs6.next()) {
					l_dbg_stay_id = rs6.getInt(1);
					l_dbg_prop_cd = rs6.getString(2);
					l_dbg_doa = rs6.getTimestamp(3);
					l_dbg_dod = rs6.getTimestamp(4);
					l_dbg_linked_id = rs6.getInt(5);
					l_dbg_acct_trans_id = rs6.getInt(6);
					l_dbg_ord = rs6.getInt(7);
					l_dbg_post_dtime = rs6.getTimestamp(8);
				}
				pstmt8.close();
			}			
			
			
			// See if the stay at hand is part of a stay set that has already been awarded an ny0711p bonus.
			

			PreparedStatement pstmt9 = prepareStatement(
					  "select stay_id, doa, dod"
					+ " from recent_stay_trans_list_ny0711"
					+ " where linked_id = ?"
					+ " and stay_id != ?");
			
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
				l_doa = rs7.getTimestamp(2);
				l_dod2 = rs7.getInt(3);				
				
				if (l_doa.equals(in_doa) || (in_doa + in_los.equals(l_dod2))) {					

					PreparedStatement pstmt10 = prepareStatement(
							  "select count(*)"
							+ " from acct_trans a, acct_trans_detail ad"
							+ " where a.stay_id = ?"
							+ " and a.rev_acct_trans_id is null"
							+ " and ad.promo_id = ?");
					
					if (l_stay_id != null) {
						pstmt10.setInt(1, l_stay_id);
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
					l_bonus_cnt = rs8.getInt(1);
					pstmt10.close();					
					if (l_bonus_cnt.equals(0)) {						
						continue;
					}					
					l_elig = "F";					
					break;
				}
			}
			pstmt9.close();			
			return l_elig;			
			

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