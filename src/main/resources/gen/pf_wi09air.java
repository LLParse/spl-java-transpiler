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

public class pf_wi09air extends AbstractProcedure {

	public String execute(Integer in_acct_id, Integer in_promo_id, String in_stay_type, Integer in_prop_id, Timestamp in_doa, Integer in_los, String in_rm_type, String in_srp_code, Double in_rm_revenue, Double in_fb_revenue, Double in_other_revenue, String in_curr_code, String in_res_source, Integer in_stay_id) throws SQLException, ProcedureException {		
		
		/*
		 * pf_wi09air.sql - Check if stay is eligible for the Winter 2009 promo. Customer must have registered
		 *                  prior to DOA to be eligible. Stay must be at least the second stay within the
		 *                  promotion period.
		 *                  
		 *                  For airline stays, consecutive and concurrent stays must be examined.
		 *                  
		 *                  The stays in question are restricted to DOA falling within the promotion start 
		 *                  and end dates. Also the stays must have qualified for points.
		 *                         
		 * $Id: pf_wi09air.sql 99 2012-12-19 21:14:54Z michael_giroux $
		 *    
		 *        Copyright (C) 2009 Choice Hotels International, Inc.
		 *        
		 *        See if incoming acct_id has any WI09% like offers for other accounts - do this if this is an
		 *        enrollment stay; if so, auto register this account. If not auto register this and all of the
		 *        other accounts.
		 *        
		 *        select * from acct_offer where acct_id in 
		 *               (select acct_id from cust_acct where cust_id in (select cust_id from cust_acct where acct_id = in_acct_id))
		 *               and offer_id = 'XXXX' 
		 *               (or perhaps offer_id in ('WI09CP', 'WI09AIR', etc...)
		 */		
		
		Timestamp l_start_date;		
		Timestamp l_stop_date;		
		String l_recog_cd;		
		String l_recog_id;		
		Integer l_stay_id;		
		String l_prop_cd;		
		Timestamp l_doa;		
		Timestamp l_dod;		
		String l_res_source;		
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
		Integer l_stay_cnt;		
		Integer l_offer_id;		
		Timestamp l_offer_dtime;		
		
		Integer sql_error;		
		Integer isam_error;		
		String error_data;		
		
		try {			
			
			l_start_date = null;			
			l_stop_date = null;			
			l_recog_cd = null;			
			l_recog_id = null;			
			l_stay_id = null;			
			l_prop_cd = null;			
			l_doa = null;			
			l_dod = null;			
			l_res_source = null;			
			l_linked_id = null;			
			l_trans_id = null;			
			f_dod = null;			
			n_id = null;			
			n_link = null;			
			l_name = null;			
			l_signup_date = null;			
			l_qualifies = 0;			
			l_ord = 0;			
			l_linked_trans_id = null;			
			l_bonus_cnt = 0;			
			l_stay_cnt = 0;			
			l_offer_id = 0;			
			l_offer_dtime = null;			
			
			//set debug file to '/tmp/pf_wi09air.trace';
			//trace on;
			
			// Get the program code of the account

			PreparedStatement pstmt1 = prepareStatement(
					  "select a.recog_cd, a.recog_id, a.signup_date"
					+ " from acct a"
					+ " where a.acct_id = ?");
			
			if (in_acct_id != null) {
				pstmt1.setInt(1, in_acct_id);
			}
			else {
				pstmt1.setNull(1, Types.JAVA_OBJECT);
			}
			ResultSet rs1 = executeQuery(pstmt1);
			rs1.next();
			l_recog_cd = rs1.getString(1);
			l_recog_id = rs1.getString(2);
			l_signup_date = rs1.getTimestamp(3);
			pstmt1.close();			
			
			// Get the promo start and stop dates

			PreparedStatement pstmt2 = prepareStatement(
					  "select p.start_date, p.stop_date"
					+ " from promo p"
					+ " where p.recog_cd = ?"
					+ " and p.promo_id = ?");
			
			if (l_recog_cd != null) {
				pstmt2.setString(1, l_recog_cd);
			}
			else {
				pstmt2.setNull(1, Types.JAVA_OBJECT);
			}
			if (in_promo_id != null) {
				pstmt2.setInt(2, in_promo_id);
			}
			else {
				pstmt2.setNull(2, Types.JAVA_OBJECT);
			}
			ResultSet rs2 = executeQuery(pstmt2);
			rs2.next();
			l_start_date = rs2.getTimestamp(1);
			l_stop_date = rs2.getTimestamp(2);
			pstmt2.close();			
			
			if (l_start_date == null) {				
				return "F";
			}			
			
			if (in_doa < l_start_date || in_doa > l_stop_date) {				
				return "F";
			}			
			
			// get booking source, doa

			PreparedStatement pstmt3 = prepareStatement(
					  "select s.res_source, s.doa"
					+ " from stay s"
					+ " where s.stay_id = ?");
			
			if (in_stay_id != null) {
				pstmt3.setInt(1, in_stay_id);
			}
			else {
				pstmt3.setNull(1, Types.JAVA_OBJECT);
			}
			ResultSet rs3 = executeQuery(pstmt3);
			rs3.next();
			l_res_source = rs3.getString(1);
			l_doa = rs3.getTimestamp(2);
			pstmt3.close();			
			
			if (l_res_source == null || (!l_res_source.equals("C") && !l_res_source.equals("N"))) {				
				return "F";
			}			
			
			// see if they registered for offer before DOA

			PreparedStatement pstmt4 = prepareStatement(
					  "select o.offer_id"
					+ " from offer o"
					+ " where o.offer_cd = \"WI09AIR\""
					+ " and o.recog_cd = ?");
			
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
					  "select date(ao.offer_dtime)"
					+ " from acct_offer ao"
					+ " where ao.acct_id = ?"
					+ " and ao.offer_id = ?");
			
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
			
			// If no offer but stay is an enrollment stay, then register this
			// account and any other accounts tied to the customer.
			if (l_offer_dtime == null || l_offer_dtime > in_doa) {				
				if (l_offer_dtime == null && l_doa.equals(l_signup_date)) {					
					new pf_wi09airregister().execute(in_acct_id, "WI09AIR", l_recog_cd, l_recog_id);
				}				
				else {					
					return "F";
				}
			}			
			
			// get number of stays within the promo period

			PreparedStatement pstmt6 = prepareStatement(
					  "select count(*)"
					+ " from acct_trans a, stay s"
					+ " where a.acct_id = ?"
					+ " and a.stay_id = s.stay_id"
					+ " and s.stay_type in (\"N\", \"F\")"
					+ " and s.doa >= ?"
					+ " and s.doa <= ?"
					+ " and a.rev_acct_trans_id is null"
					+ " and new get_acct_trans_sum().execute(a.acct_trans_id) > 0");
			
			if (in_acct_id != null) {
				pstmt6.setInt(1, in_acct_id);
			}
			else {
				pstmt6.setNull(1, Types.JAVA_OBJECT);
			}
			if (l_start_date != null) {
				pstmt6.setObject(2, l_start_date);
			}
			else {
				pstmt6.setNull(2, Types.JAVA_OBJECT);
			}
			if (l_stop_date != null) {
				pstmt6.setObject(3, l_stop_date);
			}
			else {
				pstmt6.setNull(3, Types.JAVA_OBJECT);
			}
			ResultSet rs6 = executeQuery(pstmt6);
			rs6.next();
			l_stay_cnt = rs6.getInt(1);
			pstmt6.close();			
			
			// Get a list of linked air stays to consider

			Iterator<Object> it0 = find_linked_air_stays_by_promo(in_promo_id, l_start_date, l_stop_date, in_acct_id).iterator();
			l_stay_id = (Integer) it0.next();
			l_prop_cd = (String) it0.next();
			l_doa = (Timestamp) it0.next();
			l_dod = (Timestamp) it0.next();
			l_linked_id = (Integer) it0.next();
			l_trans_id = (Integer) it0.next();
			while (it0.hasNext()) {				
				

				PreparedStatement pstmt7 = prepareInsert(
						  "insert into recent_stay_trans_list_wi09air (stay_id, prop_cd, doa, dod, linked_id, acct_trans_id, ord)"
						+ " values (?, ?, ?, ?, nvl(?, ?), ?, ?)");
				if (l_stay_id != null) {
					pstmt7.setInt(1, l_stay_id);
				}
				else {
					pstmt7.setNull(1, Types.JAVA_OBJECT);
				}
				if (l_prop_cd != null) {
					pstmt7.setString(2, l_prop_cd);
				}
				else {
					pstmt7.setNull(2, Types.JAVA_OBJECT);
				}
				if (l_doa != null) {
					pstmt7.setObject(3, l_doa);
				}
				else {
					pstmt7.setNull(3, Types.JAVA_OBJECT);
				}
				if (l_dod != null) {
					pstmt7.setObject(4, l_dod);
				}
				else {
					pstmt7.setNull(4, Types.JAVA_OBJECT);
				}
				if (l_linked_id != null) {
					pstmt7.setInt(5, l_linked_id);
				}
				else {
					pstmt7.setNull(5, Types.JAVA_OBJECT);
				}
				if (l_stay_id != null) {
					pstmt7.setInt(6, l_stay_id);
				}
				else {
					pstmt7.setNull(6, Types.JAVA_OBJECT);
				}
				if (l_trans_id != null) {
					pstmt7.setInt(7, l_trans_id);
				}
				else {
					pstmt7.setNull(7, Types.JAVA_OBJECT);
				}
				if (l_ord != null) {
					pstmt7.setInt(8, l_ord);
				}
				else {
					pstmt7.setNull(8, Types.JAVA_OBJECT);
				}
				executeUpdate(pstmt7);
				pstmt7.close();				
				l_ord = l_ord + 1;
			}			
			
			// see if current stay is linked to any others

			PreparedStatement pstmt8 = prepareStatement(
					  "select max(l.id)"
					+ " from recent_stay_trans_list_wi09air l"
					+ " where l.prop_cd = new get_prop_cd().execute(?)"
					+ " and ("(" || 
					+ ") || (" || 
					+ ")
					+ ")");
			
			if (in_prop_id != null) {
				pstmt8.setInt(1, in_prop_id);
			}
			else {
				pstmt8.setNull(1, Types.JAVA_OBJECT);
			}
			ResultSet rs7 = executeQuery(pstmt8);
			rs7.next();
			n_id = rs7.getInt(1);
			pstmt8.close();			
			
			if (n_id != null) {				

				PreparedStatement pstmt9 = prepareStatement(
						  "select nvl(l.linked_id, l.stay_id)"
						+ " from recent_stay_trans_list_wi09air l"
						+ " where l.id = ?");
				
				if (n_id != null) {
					pstmt9.setInt(1, n_id);
				}
				else {
					pstmt9.setNull(1, Types.JAVA_OBJECT);
				}
				ResultSet rs8 = executeQuery(pstmt9);
				rs8.next();
				n_link = rs8.getInt(1);
				pstmt9.close();
			}			
			else {				
				n_link = in_stay_id;
			}			
			
			// if this stay not linked then check if it's at least the second stay
			if (n_link.equals(in_stay_id)) {				
				if (l_stay_cnt > 0) {					
					return "T";
				}				
				else {					
					return "F";
				}
			}			
			
			// There are linked stays. Need to determine if bonus has already been awarded. 
			// Only one bonus for consecutive stays or multi-room stays.
			l_qualifies = 1;			
			

			PreparedStatement pstmt10 = prepareStatement(
					  "select acct_trans_id"
					+ " from recent_stay_trans_list_wi09air"
					+ " where linked_id = ?");
			
			if (n_link != null) {
				pstmt10.setInt(1, n_link);
			}
			else {
				pstmt10.setNull(1, Types.JAVA_OBJECT);
			}
			ResultSet rs9 = executeQuery(pstmt10);
			while (rs9.next()) {
				l_linked_trans_id = rs9.getInt(1);				
				
				// see if bonus already awarded

				PreparedStatement pstmt11 = prepareStatement(
						  "select count(*)"
						+ " from acct_trans_detail d, promo p"
						+ " where d.acct_trans_id = ?"
						+ " and d.promo_id = ?"
						+ " and d.promo_id = p.promo_id"
						+ " and p.rule = \"S\"");
				
				if (l_linked_trans_id != null) {
					pstmt11.setInt(1, l_linked_trans_id);
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
				ResultSet rs10 = executeQuery(pstmt11);
				rs10.next();
				l_bonus_cnt = rs10.getInt(1);
				pstmt11.close();				
				
				// already awarded, can't give them another one
				if (l_bonus_cnt > 0) {					
					l_qualifies = 0;					
					break;
				}
			}
			pstmt10.close();			
			if (l_qualifies.equals(1) && l_stay_cnt > 0) {				
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