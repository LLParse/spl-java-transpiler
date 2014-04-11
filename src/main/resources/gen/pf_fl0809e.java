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

public class pf_fl0809e extends AbstractProcedure {

	public String execute(Integer in_acct_id, Integer in_promo_id, String in_stay_type, Integer in_prop_id, Timestamp in_doa, Integer in_los, String in_rm_type, String in_srp_code, Double in_rm_revenue, Double in_fb_revenue, Double in_other_revenue, String in_curr_code, String in_res_source, Integer in_stay_id) throws SQLException, ProcedureException {		
		
		/*
		 * pf_fl0809e.sql - Check if stay is eligible for the '1000 pts for completed stay' promo. This
		 *                  promo applies to Elite members only and they must have registered for the
		 *                  offer, FL0809E. Elite Gold members must book via ch.com or central res. 
		 *                  Consecutive stays or multiple room stays receive only one award bonus.
		 *                  
		 *                  The stays in question are restricted to DOA falling within the promotion start 
		 *                  and end dates. Also the stays must have qualified for points.
		 *                         
		 * $Id: pf_fl0809e.sql 99 2012-12-19 21:14:54Z michael_giroux $
		 *    
		 *        Copyright (C) 2009 Choice Hotels International, Inc.
		 */		
		
		Timestamp start_date;		
		Timestamp stop_date;		
		String l_recog_cd;		
		Integer fl0809e_promo_id;		
		Integer l_stay_id;		
		String l_prop_cd;		
		Timestamp l_doa;		
		Timestamp l_dod;		
		Integer l_linked_id;		
		Integer l_trans_id;		
		Timestamp f_dod;		
		Integer n_id;		
		Integer n_link;		
		String l_name;		
		Timestamp l_signup_date;		
		Integer l_qualifies;		
		Integer l_ord;		
		Integer l_linked_trans_id;		
		Integer l_bonus_cnt;		
		Integer l_offer_id;		
		Timestamp l_offer_dtime;		
		
		Integer sql_error;		
		Integer isam_error;		
		String error_data;		
		
		try {			
			
			start_date = null;			
			stop_date = null;			
			l_recog_cd = null;			
			fl0809e_promo_id = null;			
			l_stay_id = null;			
			l_prop_cd = null;			
			l_doa = null;			
			l_dod = null;			
			l_linked_id = null;			
			l_trans_id = null;			
			f_dod = null;			
			n_id = null;			
			n_link = null;			
			l_name = null;			
			l_signup_date = null;			
			l_qualifies = 1;			// assume stay qualifies
			l_ord = 0;			
			l_linked_trans_id = null;			
			l_bonus_cnt = 0;			
			l_offer_id = 0;			
			l_offer_dtime = null;			
			
			//set debug file to '/tmp/pf_fl0809e.trace';
			//trace on;
			
			// Get the program code of the account

			PreparedStatement pstmt1 = prepareStatement(
					  "select acct.recog_cd, acct.signup_date"
					+ " from acct"
					+ " where acct.acct_id = ?");
			
			if (in_acct_id != null) {
				pstmt1.setInt(1, in_acct_id);
			}
			else {
				pstmt1.setNull(1, Types.JAVA_OBJECT);
			}
			ResultSet rs1 = executeQuery(pstmt1);
			rs1.next();
			l_recog_cd = rs1.getString(1);
			l_signup_date = rs1.getTimestamp(2);
			pstmt1.close();			
			
			// Get the promo ID for counting awarded bonuses

			PreparedStatement pstmt2 = prepareStatement(
					  "select promo.promo_id, promo.start_date, promo.stop_date"
					+ " from promo"
					+ " where promo.recog_cd = ?"
					+ " and promo.promo_cd = \"FL0809E\"");
			
			if (l_recog_cd != null) {
				pstmt2.setString(1, l_recog_cd);
			}
			else {
				pstmt2.setNull(1, Types.JAVA_OBJECT);
			}
			ResultSet rs2 = executeQuery(pstmt2);
			rs2.next();
			fl0809e_promo_id = rs2.getInt(1);
			start_date = rs2.getTimestamp(2);
			stop_date = rs2.getTimestamp(3);
			pstmt2.close();			
			
			if (fl0809e_promo_id == null) {				
				return "F";
			}			
			
			if (in_doa < start_date || in_doa > stop_date) {				
				return "F";
			}			
			
			// see if the account is elite status on or before doa

			PreparedStatement pstmt3 = prepareStatement(
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
				pstmt3.setInt(1, in_acct_id);
			}
			else {
				pstmt3.setNull(1, Types.JAVA_OBJECT);
			}
			if (in_acct_id != null) {
				pstmt3.setInt(2, in_acct_id);
			}
			else {
				pstmt3.setNull(2, Types.JAVA_OBJECT);
			}
			if (in_doa != null) {
				pstmt3.setObject(3, in_doa);
			}
			else {
				pstmt3.setNull(3, Types.JAVA_OBJECT);
			}
			if (in_doa != null) {
				pstmt3.setObject(4, in_doa);
			}
			else {
				pstmt3.setNull(4, Types.JAVA_OBJECT);
			}
			ResultSet rs3 = executeQuery(pstmt3);
			rs3.next();
			l_name = rs3.getString(1);
			pstmt3.close();			
			
			if (l_name == null) {				
				return "F";
			}			
			
			// Account is elite. If Gold, must have booled via ch.com or central res
			if (l_name.equals("Gold") && (in_res_source == null || (!in_res_source.equals("C") && !in_res_source.equals("N")))) {				
				return "F";
			}			
			
			// see if they registered for offer before DOA
			// get the offer_id for the promo

			PreparedStatement pstmt4 = prepareStatement(
					  "select offer_id"
					+ " from offer"
					+ " where offer_cd = \"FL0809E\""
					+ " and recog_cd = ?");
			
			if (l_recog_cd != null) {
				pstmt4.setString(1, l_recog_cd);
			}
			else {
				pstmt4.setNull(1, Types.JAVA_OBJECT);
			}
			ResultSet rs4 = executeQuery(pstmt4);
			rs4.next();
			l_offer_id = rs4.getInt(1);
			pstmt4.close();			
			
			// get date of offer/registration if any

			PreparedStatement pstmt5 = prepareStatement(
					  "select date(offer_dtime)"
					+ " from acct_offer"
					+ " where acct_id = ?"
					+ " and offer_id = ?");
			
			if (in_acct_id != null) {
				pstmt5.setInt(1, in_acct_id);
			}
			else {
				pstmt5.setNull(1, Types.JAVA_OBJECT);
			}
			if (l_offer_id != null) {
				pstmt5.setInt(2, l_offer_id);
			}
			else {
				pstmt5.setNull(2, Types.JAVA_OBJECT);
			}
			ResultSet rs5 = executeQuery(pstmt5);
			rs5.next();
			l_offer_dtime = rs5.getTimestamp(1);
			pstmt5.close();			
			
			if (l_offer_dtime == null || l_offer_dtime > in_doa) {				
				return "F";
			}			
			
			// Get a list of linked stays to consider

			Iterator<Object> it0 = find_linked_stays_by_promo(in_promo_id, start_date, stop_date, in_acct_id).iterator();
			l_stay_id = (Integer) it0.next();
			l_prop_cd = (String) it0.next();
			l_doa = (Timestamp) it0.next();
			l_dod = (Timestamp) it0.next();
			l_linked_id = (Integer) it0.next();
			l_trans_id = (Integer) it0.next();
			while (it0.hasNext()) {				
				

				PreparedStatement pstmt6 = prepareInsert(
						  "insert into recent_stay_trans_list_fl0809e (stay_id, prop_cd, doa, dod, linked_id, acct_trans_id, ord)"
						+ " values (?, ?, ?, ?, nvl(?, ?), ?, ?)");
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
				executeUpdate(pstmt6);
				pstmt6.close();				
				l_ord = l_ord + 1;
			}			
			
			// see if current stay is linked to any others

			PreparedStatement pstmt7 = prepareStatement(
					  "select max(l.id)"
					+ " from recent_stay_trans_list_fl0809e l"
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
						+ " from recent_stay_trans_list_fl0809e l"
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
			
			// if this stay not linked then it qualifies since we did
			// not get out earlier
			if (n_link.equals(in_stay_id)) {				
				return "T";
			}			
			
			// There are linked stays. Need to determine if bonus has already been awarded. 
			// Only one bonus for consecutive stays or multi-room stays.
			

			PreparedStatement pstmt9 = prepareStatement(
					  "select acct_trans_id"
					+ " from recent_stay_trans_list_fl0809e"
					+ " where linked_id = ?");
			
			if (n_link != null) {
				pstmt9.setInt(1, n_link);
			}
			else {
				pstmt9.setNull(1, Types.JAVA_OBJECT);
			}
			ResultSet rs8 = executeQuery(pstmt9);
			while (rs8.next()) {
				l_linked_trans_id = rs8.getInt(1);				
				
				// see if bonus already awarded

				PreparedStatement pstmt10 = prepareStatement(
						  "select count(*)"
						+ " from acct_trans_detail d, promo p"
						+ " where d.acct_trans_id = ?"
						+ " and d.promo_id = ?"
						+ " and d.promo_id = p.promo_id"
						+ " and p.rule = \"S\"");
				
				if (l_linked_trans_id != null) {
					pstmt10.setInt(1, l_linked_trans_id);
				}
				else {
					pstmt10.setNull(1, Types.JAVA_OBJECT);
				}
				if (fl0809e_promo_id != null) {
					pstmt10.setInt(2, fl0809e_promo_id);
				}
				else {
					pstmt10.setNull(2, Types.JAVA_OBJECT);
				}
				ResultSet rs9 = executeQuery(pstmt10);
				rs9.next();
				l_bonus_cnt = rs9.getInt(1);
				pstmt10.close();				
				
				// already awarded, can't give them another one
				if (l_bonus_cnt > 0) {					
					l_qualifies = 0;					
					break;
				}
			}
			pstmt9.close();			
			if (l_qualifies.equals(1)) {				
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
				throw new ProcedureException(sql_error, isam_error, error_data);
			}
		}
	}

}