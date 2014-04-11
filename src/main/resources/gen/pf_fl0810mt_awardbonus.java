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

public class pf_fl0810mt_awardbonus extends AbstractProcedure {

	public String execute(Integer in_acct_id, Integer in_promo_id, String in_stay_type, Integer in_prop_id, Timestamp in_doa, Integer in_los, String in_rm_type, String in_srp_code, Double in_rm_revenue, Double in_fb_revenue, Double in_other_revenue, String in_curr_code, String in_res_source, Integer in_stay_id) throws SQLException, ProcedureException {		
		
		/*
		 * pf_fl0810mt_awardbonus.sql - If we get to this routine, then we have determined that the account on the stay
		 *                             has potentially qualified for the triple points bonus; i.e. they have registered
		 *                             for the offer, they are an elite Plat or Diamond account. In this procedure, we
		 *                             just need to check that the stay is at least the second stay after registration,
		 *                             and that the stay is not part of a multi-room in which the bonus has already been 
		 *                             awarded.
		 *    
		 * $Id: pf_fl0810mt_awardbonus.sql 99 2012-12-19 21:14:54Z michael_giroux $
		 *    
		 *        Copyright (C) 2010 Choice Hotels International, Inc.
		 */		
		
		Timestamp l_start_date;		
		Timestamp l_stop_date;		
		Integer l_stay_id;		
		Integer l_stay_id2;		
		String l_prop_cd;		
		Timestamp l_doa;		
		Timestamp l_dod;		
		Integer l_linked_id;		
		Integer l_trans_id;		
		Timestamp f_dod;		
		Integer n_id;		
		Integer n_link;		
		Integer l_acct_trans_id;		
		Integer l_ord;		
		Timestamp l_post_dtime;		
		Timestamp l_curr_post_dtime;		
		Integer l_already_awarded;		
		Double l_linked_amount;		
		Integer l_offer_id;		
		Timestamp l_offer_dtime;		
		Integer l_stay_count;		
		String l_false;		
		String l_recog_cd;		
		
		Integer sql_error;		
		Integer isam_error;		
		String error_data;		
		
		try {			
			
			l_start_date = null;			
			l_stop_date = null;			
			l_stay_id = null;			
			l_stay_id2 = null;			
			l_linked_id = null;			
			l_prop_cd = null;			
			l_doa = null;			
			l_dod = null;			
			l_trans_id = null;			
			f_dod = in_doa + in_los;			
			n_id = null;			
			n_link = null;			
			l_acct_trans_id = 0;			
			l_ord = 1;			
			l_post_dtime = null;			
			l_curr_post_dtime = null;			
			l_already_awarded = 0;			
			l_linked_amount = 0.0;			
			l_offer_id = null;			
			l_offer_dtime = null;			
			l_stay_count = 0;			
			l_false = "F";			// assume the worst
			l_recog_cd = null;			
			
			//set debug file to '/tmp/pf_fl0810mt_awardbonus.trace';
			//trace on;
			

			PreparedStatement pstmt1 = prepareStatement(
					  "select start_date, stop_date"
					+ " from promo p"
					+ " where p.promo_id = ?");
			
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
			
			// get basic info on account and offer

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
			

			PreparedStatement pstmt3 = prepareStatement(
					  "select o.offer_id"
					+ " from offer o"
					+ " where o.offer_cd = \"FL0810TR\""
					+ " and o.recog_cd = ?");
			
			if (l_recog_cd != null) {
				pstmt3.setString(1, l_recog_cd);
			}
			else {
				pstmt3.setNull(1, Types.JAVA_OBJECT);
			}
			ResultSet rs3 = executeQuery(pstmt3);
			rs3.next();
			l_offer_id = rs3.getInt(1);
			pstmt3.close();			
			

			PreparedStatement pstmt4 = prepareStatement(
					  "select ao.offer_dtime"
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
			
			if (l_offer_dtime == null) {				
				throw new ProcedureException(-746, 0, "pf_fl0810mt_awardbonus: unexpected null for offer_dtime");
			}			
			
			// Get a list of linked stays to consider

			Iterator<Object> it0 = find_linked_stays_by_promo(in_promo_id, l_start_date, l_stop_date, in_acct_id).iterator();
			l_stay_id = (Integer) it0.next();
			l_prop_cd = (String) it0.next();
			l_doa = (Timestamp) it0.next();
			l_dod = (Timestamp) it0.next();
			l_linked_id = (Integer) it0.next();
			l_trans_id = (Integer) it0.next();
			while (it0.hasNext()) {				
				

				PreparedStatement pstmt5 = prepareStatement(
						  "select post_dtime"
						+ " from stay"
						+ " where stay_id = ?");
				
				if (l_stay_id != null) {
					pstmt5.setInt(1, l_stay_id);
				}
				else {
					pstmt5.setNull(1, Types.JAVA_OBJECT);
				}
				ResultSet rs5 = executeQuery(pstmt5);
				rs5.next();
				l_post_dtime = rs5.getTimestamp(1);
				pstmt5.close();				
				

				PreparedStatement pstmt6 = prepareInsert(
						  "insert into recent_stay_trans_list_fl10 (stay_id, prop_cd, doa, dod, linked_id, acct_trans_id, ord, post_dtime)"
						+ " values (?, ?, ?, ?, nvl(?, ?), ?, ?, ?)");
				if (l_stay_id != null) {
					pstmt6.setInt(1, l_stay_id);
				}
				else {
					pstmt6.setNull(1, Types.JAVA_OBJECT);
				}
				if (l_prop_cd != null) {
					pstmt6.setString(2, l_prop_cd);
				}
				else {
					pstmt6.setNull(2, Types.JAVA_OBJECT);
				}
				if (l_doa != null) {
					pstmt6.setObject(3, l_doa);
				}
				else {
					pstmt6.setNull(3, Types.JAVA_OBJECT);
				}
				if (l_dod != null) {
					pstmt6.setObject(4, l_dod);
				}
				else {
					pstmt6.setNull(4, Types.JAVA_OBJECT);
				}
				if (l_linked_id != null) {
					pstmt6.setInt(5, l_linked_id);
				}
				else {
					pstmt6.setNull(5, Types.JAVA_OBJECT);
				}
				if (l_stay_id != null) {
					pstmt6.setInt(6, l_stay_id);
				}
				else {
					pstmt6.setNull(6, Types.JAVA_OBJECT);
				}
				if (l_trans_id != null) {
					pstmt6.setInt(7, l_trans_id);
				}
				else {
					pstmt6.setNull(7, Types.JAVA_OBJECT);
				}
				if (l_ord != null) {
					pstmt6.setInt(8, l_ord);
				}
				else {
					pstmt6.setNull(8, Types.JAVA_OBJECT);
				}
				if (l_post_dtime != null) {
					pstmt6.setObject(9, l_post_dtime);
				}
				else {
					pstmt6.setNull(9, Types.JAVA_OBJECT);
				}
				executeUpdate(pstmt6);
				pstmt6.close();				
				l_ord = l_ord + 1;
			}			
			
			// add current stay to list

			PreparedStatement pstmt7 = prepareStatement(
					  "select max(l.id)"
					+ " from recent_stay_trans_list_fl10 l"
					+ " where l.prop_cd = new get_prop_cd().execute(?)"
					+ " and ("(" || 
					+ ") || (" || 
					+ ")
					+ ")");
			
			if (in_prop_id != null) {
				pstmt7.setInt(1, in_prop_id);
			}
			else {
				pstmt7.setNull(1, Types.JAVA_OBJECT);
			}
			ResultSet rs6 = executeQuery(pstmt7);
			rs6.next();
			n_id = rs6.getInt(1);
			pstmt7.close();			
			
			if (n_id != null) {				

				PreparedStatement pstmt8 = prepareStatement(
						  "select nvl(l.linked_id, l.stay_id)"
						+ " from recent_stay_trans_list_fl10 l"
						+ " where l.id = ?");
				
				if (n_id != null) {
					pstmt8.setInt(1, n_id);
				}
				else {
					pstmt8.setNull(1, Types.JAVA_OBJECT);
				}
				ResultSet rs7 = executeQuery(pstmt8);
				rs7.next();
				n_link = rs7.getInt(1);
				pstmt8.close();
			}			
			else {				
				n_link = in_stay_id;
			}			
			

			PreparedStatement pstmt9 = prepareStatement(
					  "select post_dtime"
					+ " from stay"
					+ " where stay_id = ?");
			
			if (in_stay_id != null) {
				pstmt9.setInt(1, in_stay_id);
			}
			else {
				pstmt9.setNull(1, Types.JAVA_OBJECT);
			}
			ResultSet rs8 = executeQuery(pstmt9);
			rs8.next();
			l_post_dtime = rs8.getTimestamp(1);
			pstmt9.close();			

			PreparedStatement pstmt10 = prepareInsert(
					  "insert into recent_stay_trans_list_fl10 (stay_id, prop_cd, doa, dod, linked_id, ord, post_dtime)"
					+ " values (?, new get_prop_cd().execute(?), ?, ?, ?, ?, ?)");
			if (in_stay_id != null) {
				pstmt10.setInt(1, in_stay_id);
			}
			else {
				pstmt10.setNull(1, Types.JAVA_OBJECT);
			}
			if (in_prop_id != null) {
				pstmt10.setInt(2, in_prop_id);
			}
			else {
				pstmt10.setNull(2, Types.JAVA_OBJECT);
			}
			if (in_doa != null) {
				pstmt10.setObject(3, in_doa);
			}
			else {
				pstmt10.setNull(3, Types.JAVA_OBJECT);
			}
			if (f_dod != null) {
				pstmt10.setObject(4, f_dod);
			}
			else {
				pstmt10.setNull(4, Types.JAVA_OBJECT);
			}
			if (n_link != null) {
				pstmt10.setInt(5, n_link);
			}
			else {
				pstmt10.setNull(5, Types.JAVA_OBJECT);
			}
			if (l_ord != null) {
				pstmt10.setInt(6, l_ord);
			}
			else {
				pstmt10.setNull(6, Types.JAVA_OBJECT);
			}
			if (l_curr_post_dtime != null) {
				pstmt10.setObject(7, l_curr_post_dtime);
			}
			else {
				pstmt10.setNull(7, Types.JAVA_OBJECT);
			}
			executeUpdate(pstmt10);
			pstmt10.close();			
			
			// Get count of stays since registration occurred. There must be at least two 
			// in order to continue; i.e. the triple points for a stay does not kick in
			// until the second stay after registration.

			PreparedStatement pstmt11 = prepareStatement(
					  "select count(*)"
					+ " from recent_stay_trans_list_fl10"
					+ " where linked_id = stay_id"
					+ " and date(doa) >= date(?)");
			
			if (l_offer_dtime != null) {
				pstmt11.setObject(1, l_offer_dtime);
			}
			else {
				pstmt11.setNull(1, Types.JAVA_OBJECT);
			}
			ResultSet rs9 = executeQuery(pstmt11);
			rs9.next();
			l_stay_count = rs9.getInt(1);
			pstmt11.close();			
			
			if (l_stay_count <= 1) {				
				return l_false;
			}			
			
			// The stay at hand qualifies. However, see if it's linked to another with
			// the same doa (multi-room) and if the bonus has already been awarded on
			// another room. If so, then the stay at hand does not qualify.
			

			PreparedStatement pstmt12 = prepareStatement(
					  "select stay_id, acct_trans_id, doa"
					+ " from recent_stay_trans_list_fl10"
					+ " where linked_id = ?"
					+ " and stay_id != ?");
			
			if (n_link != null) {
				pstmt12.setInt(1, n_link);
			}
			else {
				pstmt12.setNull(1, Types.JAVA_OBJECT);
			}
			if (in_stay_id != null) {
				pstmt12.setInt(2, in_stay_id);
			}
			else {
				pstmt12.setNull(2, Types.JAVA_OBJECT);
			}
			ResultSet rs10 = executeQuery(pstmt12);
			while (rs10.next()) {
				l_stay_id = rs10.getInt(1);
				l_acct_trans_id = rs10.getInt(2);
				l_doa = rs10.getTimestamp(3);				
				
				if (l_doa.equals(in_doa)) {					
					l_linked_amount = 0;					

					PreparedStatement pstmt13 = prepareStatement(
							  "select ad.amount"
							+ " from acct_trans a, acct_trans_detail ad"
							+ " where a.acct_trans_id = ad.acct_trans_id"
							+ " and a.acct_trans_id = ?"
							+ " and a.rev_acct_trans_id is null"
							+ " and ad.promo_id = ?");
					
					if (l_acct_trans_id != null) {
						pstmt13.setInt(1, l_acct_trans_id);
					}
					else {
						pstmt13.setNull(1, Types.JAVA_OBJECT);
					}
					if (in_promo_id != null) {
						pstmt13.setInt(2, in_promo_id);
					}
					else {
						pstmt13.setNull(2, Types.JAVA_OBJECT);
					}
					ResultSet rs11 = executeQuery(pstmt13);
					rs11.next();
					l_linked_amount = rs11.getDouble(1);
					pstmt13.close();					
					
					if (l_linked_amount > 0) {						
						l_already_awarded = 1;						
						break;
					}
				}
			}
			pstmt12.close();			
			
			if (l_already_awarded.equals(1)) {				
				return l_false;
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
				throw new ProcedureException(sql_error, isam_error, error_data);
			}
		}
	}

}