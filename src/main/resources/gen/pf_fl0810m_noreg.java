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

public class pf_fl0810m_noreg extends AbstractProcedure {

	public String execute(Integer in_acct_id, Integer in_promo_id, String in_stay_type, Integer in_prop_id, Timestamp in_doa, Integer in_los, String in_rm_type, String in_srp_code, Double in_rm_revenue, Double in_fb_revenue, Double in_other_revenue, String in_curr_code, String in_res_source, Integer in_stay_id) throws SQLException, ProcedureException {		
		
		/*
		 * pf_fl0810m_noreg.sql - Fall 2010 promo filter for the midscale stay twice promotion. This filter processes the
		 *                        standard midscale promotion, FL0810M, where no registration for the FL0810R offer is assumed.
		 *                        However, if the account is registered for the midscale triple points promotion, FL0810MT, and
		 *                        the DOA is after the registration, then this filter should return false and let the triple points
		 *                        filter determine the bonus. But, we also need to check their elite level if they are registered for
		 *                        the triple points offer. If they are registered, but not Platinum or Diamond, then this filter should
		 *                        be utilized and the max awards is 4.
		 *                        
		 *                        If not registered for FL0810R,  the max number of awards allowed is 4.
		 *                        If registered for FL0810TR on DOA, but not elite Plat or Diamond on DOA, then they are considered
		 *                        unregistered and again, the max awards allowed is 4.
		 *                  
		 *                        Stay eligibility criteria for this promo are:
		 *    
		 *                        1) booked via ch.com, central res or GDS
		 *                        2) signup stay automatically qualifies
		 *                        3) Platinum or Diamond or CP Europe or CP Latin America can book through any channel
		 *                        If those criteria are met, then determine if this is the second qualifying stay before
		 *                        awarding the bonus. 
		 *    
		 * $Id: pf_fl0810m_noreg.sql 99 2012-12-19 21:14:54Z michael_giroux $
		 *    
		 *        Copyright (C) 2010 Choice Hotels International, Inc.
		 */		
		Timestamp l_start_date;		
		Timestamp l_stop_date;		
		String l_promo_cd;		
		String l_recog_cd;		
		Timestamp l_signup_date;		
		Integer l_offer_id;		
		Integer l_bonus_cnt;		
		String l_platinum;		
		String l_diamond;		
		String l_registered;		
		String l_enrollstay;		
		String l_eligible;		
		String l_answer;		
		Integer l_appl_group;		
		String l_chain_id;		
		String l_mkt_area;		
		String l_country;		
		String l_prop_country;		
		String l_prop_type;		
		String l_ioc_region;		
		String l_prop_class;		
		String l_prop_curr_cd;		
		String l_stay_type;		
		Integer l_prop_chk;		
		
		Integer sql_error;		
		Integer isam_error;		
		String error_data;		
		
		try {			
			
			l_start_date = null;			
			l_stop_date = null;			
			l_promo_cd = null;			
			l_recog_cd = null;			
			l_signup_date = null;			
			l_offer_id = null;			
			l_bonus_cnt = 0;			
			l_platinum = "F";			
			l_diamond = "F";			
			l_registered = "F";			
			l_enrollstay = "F";			
			l_eligible = "F";			
			l_answer = "F";			
			l_appl_group = 0;			
			l_chain_id = null;			
			l_mkt_area = null;			
			l_country = null;			
			l_prop_country = null;			
			l_prop_type = null;			
			l_ioc_region = null;			
			l_prop_class = null;			
			l_prop_curr_cd = null;			
			l_stay_type = null;			
			l_prop_chk = 0;			
			
			//set debug file to '/tmp/pf_fl0810m_noreg.trace';
			//trace on;
			
			// Check for valid doa

			PreparedStatement pstmt1 = prepareStatement(
					  "select promo.start_date, promo.stop_date, promo.promo_cd"
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
			l_promo_cd = rs1.getString(3);
			pstmt1.close();			
			
			if (in_doa < l_start_date || in_doa > l_stop_date) {				
				return l_answer;
			}			
			
			// need to check if the current stay property is valid before doing anything 
			// else. For example, a midscale stay can show up as the current stay and
			// it will get processed as a economy! 
			

			PreparedStatement pstmt2 = prepareStatement(
					  "select stay_type, prop_curr_cd"
					+ " from stay"
					+ " where stay_id = ?");
			
			if (in_stay_id != null) {
				pstmt2.setInt(1, in_stay_id);
			}
			else {
				pstmt2.setNull(1, Types.JAVA_OBJECT);
			}
			ResultSet rs2 = executeQuery(pstmt2);
			rs2.next();
			l_stay_type = rs2.getString(1);
			l_prop_curr_cd = rs2.getString(2);
			pstmt2.close();			
			

			PreparedStatement pstmt3 = prepareStatement(
					  "select chain_id, mkt_area, country, prop_type, ioc_region, prop_class, country"
					+ " from prop"
					+ " where prop_id = ?");
			
			if (in_prop_id != null) {
				pstmt3.setInt(1, in_prop_id);
			}
			else {
				pstmt3.setNull(1, Types.JAVA_OBJECT);
			}
			ResultSet rs3 = executeQuery(pstmt3);
			rs3.next();
			l_chain_id = rs3.getString(1);
			l_mkt_area = rs3.getString(2);
			l_country = rs3.getString(3);
			l_prop_type = rs3.getString(4);
			l_ioc_region = rs3.getString(5);
			l_prop_class = rs3.getString(6);
			l_prop_country = rs3.getString(7);
			pstmt3.close();			
			
			l_prop_chk = new chk_prop_partic().execute(in_acct_id, in_prop_id, in_doa, in_los, in_rm_type, in_srp_code, in_rm_revenue, in_fb_revenue, in_other_revenue, in_promo_id, l_recog_cd, l_prop_curr_cd, l_appl_group, l_chain_id, l_mkt_area, l_prop_country, l_prop_type, l_ioc_region, l_prop_class, in_stay_id, l_stay_type);			
			
			if (l_prop_chk.equals(0)) {				
				return l_answer;
			}			
			
			// get some account info

			PreparedStatement pstmt4 = prepareStatement(
					  "select a.recog_cd, a.signup_date"
					+ " from acct a"
					+ " where a.acct_id = ?");
			
			if (in_acct_id != null) {
				pstmt4.setInt(1, in_acct_id);
			}
			else {
				pstmt4.setNull(1, Types.JAVA_OBJECT);
			}
			ResultSet rs4 = executeQuery(pstmt4);
			rs4.next();
			l_recog_cd = rs4.getString(1);
			l_signup_date = rs4.getTimestamp(2);
			pstmt4.close();			
			
			// Check if account is Plat/Diam and registered for the triple points offer, FL0810TR, before the DOA
			// If so, then this is not the filter to process the stay.
			l_platinum = new pf_isplatinum_ondoa().execute(in_acct_id, in_doa);			
			if (l_platinum.equals("F")) {				
				l_diamond = new pf_isdiamond_ondoa().execute(in_acct_id, in_doa);
			}			
			if (l_platinum.equals("T") || l_diamond.equals("T")) {				

				PreparedStatement pstmt5 = prepareStatement(
						  "select ao.offer_id"
						+ " from acct_offer ao"
						+ " where ao.acct_id = ?"
						+ " and ao.offer_id = ("
							+ "select o.offer_id"
							+ " from offer o"
							+ " where o.offer_cd = \"FL0810TR\""
							+ " and o.recog_cd = ?"
						+ ")");
				
				if (in_acct_id != null) {
					pstmt5.setInt(1, in_acct_id);
				}
				else {
					pstmt5.setNull(1, Types.JAVA_OBJECT);
				}
				if (l_recog_cd != null) {
					pstmt5.setString(2, l_recog_cd);
				}
				else {
					pstmt5.setNull(2, Types.JAVA_OBJECT);
				}
				ResultSet rs5 = executeQuery(pstmt5);
				rs5.next();
				l_offer_id = rs5.getInt(1);
				pstmt5.close();				
				if (l_offer_id != null) {					
					l_registered = new pf_isregistered().execute(l_offer_id, in_acct_id, in_doa);					
					if (l_registered.equals("T")) {						
						return l_answer;
					}
				}
			}			
			
			// We now know that we are not dealing with a Plat or Diam account who is registered
			// for the triple points offer of the Fall promotion. Continue on with determining if
			// the stay meets the basic promo criteria.
			
			// centrally booked or GDS booking?
			if (in_res_source != null && (in_res_source.equals("N") || in_res_source.equals("C") || in_res_source.equals("G"))) {				
				l_eligible = "T";
			}			
			
			// Platinum/Diamond or CP Latin America or CP Europe?
			if (l_eligible.equals("F")) {				
				if (l_platinum.equals("T") || l_diamond.equals("T") || l_recog_cd.equals("MC") || l_recog_cd.equals("CE")) {					
					l_eligible = "T";
				}
			}			
			
			// Enrollment stay?
			if (l_eligible.equals("F")) {				
				l_enrollstay = new pf_isenrollstay().execute(in_acct_id, in_doa);				
				if (l_enrollstay.equals("T")) {					
					l_eligible = "T";
				}
			}			
			
			// No critera were met, so no soup for you!
			if (l_eligible.equals("F")) {				
				return l_answer;
			}			
			
			// Stay is eligible, but first check to see if the max number of bonuses has been
			// reached. If so, then stay doesn't qualify on this filter.
			

			PreparedStatement pstmt6 = prepareStatement(
					  "select count(*)"
					+ " from acct_trans a, acct_trans_detail ad"
					+ " where a.acct_trans_id = ad.acct_trans_id"
					+ " and a.acct_id = ?"
					+ " and a.rev_acct_trans_id is null"
					+ " and ad.promo_id = ?");
			
			if (in_acct_id != null) {
				pstmt6.setInt(1, in_acct_id);
			}
			else {
				pstmt6.setNull(1, Types.JAVA_OBJECT);
			}
			if (in_promo_id != null) {
				pstmt6.setInt(2, in_promo_id);
			}
			else {
				pstmt6.setNull(2, Types.JAVA_OBJECT);
			}
			ResultSet rs6 = executeQuery(pstmt6);
			rs6.next();
			l_bonus_cnt = rs6.getInt(1);
			pstmt6.close();			
			
			if (l_bonus_cnt > 3) {				
				return l_answer;
			}			
			
			// Not reached the bonus limit, see if this stay triggers the bonus
			l_answer = new pf_fl0810m_awardbonus().execute(in_acct_id, in_promo_id, in_stay_type, in_prop_id, in_doa, in_los, in_rm_type, in_srp_code, in_rm_revenue, in_fb_revenue, in_other_revenue, in_curr_code, in_res_source, in_stay_id);			
			
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