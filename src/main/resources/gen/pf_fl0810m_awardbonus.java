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

public class pf_fl0810m_awardbonus extends AbstractProcedure {

	public String execute(Integer in_acct_id, Integer in_promo_id, String in_stay_type, Integer in_prop_id, Timestamp in_doa, Integer in_los, String in_rm_type, String in_srp_code, Double in_rm_revenue, Double in_fb_revenue, Double in_other_revenue, String in_curr_code, String in_res_source, Integer in_stay_id) throws SQLException, ProcedureException {		
		
		/*
		 * pf_fl0810m_awardbonus.sql - If this routine is called, then the stay being processed meets the
		 *                             Fall 2010 criteria. Now see if the bonus should be awarded; i.e.
		 *                           - are there two stays that meet the criteria; the current one being
		 *                             looked at and one previous. 
		 *                           - non-elite and Gold only earn bonus 4 times
		 *                           - elite Plat or Diamond who register for this offer earn an unlimited number of bonuses 
		 *                           If stay triggers bonus, then insert the point values into a temp table to be used by the 
		 *                           pv_fl0810 routine to calculate the bonus points awarded.
		 *    
		 * $Id: pf_fl0810m_awardbonus.sql 99 2012-12-19 21:14:54Z michael_giroux $
		 *    
		 *        Copyright (C) 2010 Choice Hotels International, Inc.
		 */		
		
		Integer has_bonus_cnt;		
		Timestamp l_start_date;		
		Timestamp l_stop_date;		
		String l_recog_cd;		
		Integer l_stay_id;		
		Integer l_stay_id2;		
		String l_prop_cd;		
		Timestamp l_doa;		
		Timestamp l_doa2;		
		Timestamp l_dod;		
		Integer l_linked_id;		
		Integer l_trans_id;		
		Timestamp f_dod;		
		Integer n_id;		
		Integer n_link;		
		Timestamp l_signup_date;		
		Integer l_acct_trans_id;		
		String l_linked_elig;		
		Double l_current_amount;		
		Integer l_total_qualified;		
		Integer l_ord;		
		Timestamp l_post_dtime;		
		Timestamp l_curr_post_dtime;		
		Double l_prop_curr_rate;		
		Double l_earn_curr_rate;		
		String l_chain_group_cd;		
		Double l_current_points;		
		Integer l_already_awarded;		
		Double l_linked_amount;		
		Integer l_bonus_count;		
		Integer l_offer_id;		
		Integer l_total_points;		
		Double l_pts_dollar;		
		Integer l_elig_stay_count;		
		Timestamp l_offer_date;		
		String l_is_elig;		
		String l_platinum;		
		String l_diamond;		
		Integer l_max_bonuses;		
		String l_registered;		
		Integer l_flag;		
		Integer l_linked_already_partic;		
		
		Integer sql_error;		
		Integer isam_error;		
		String error_data;		
		
		try {			
			
			has_bonus_cnt = 0;			
			l_start_date = null;			
			l_stop_date = null;			
			l_recog_cd = null;			
			l_stay_id = null;			
			l_stay_id2 = null;			
			l_linked_id = null;			
			l_prop_cd = null;			
			l_doa = null;			
			l_doa2 = null;			
			l_dod = null;			
			l_trans_id = null;			
			f_dod = in_doa + in_los;			
			n_id = null;			
			n_link = null;			
			l_signup_date = null;			
			l_acct_trans_id = 0;			
			l_linked_elig = null;			
			l_current_amount = 0.0;			
			l_total_qualified = 0;			
			l_ord = 1;			
			l_post_dtime = null;			
			l_curr_post_dtime = null;			
			l_prop_curr_rate = 0.0;			
			l_earn_curr_rate = 0.0;			
			l_chain_group_cd = null;			
			l_current_points = 0.0;			
			l_already_awarded = 0;			
			l_linked_amount = 0.0;			
			l_bonus_count = 0;			
			l_offer_id = null;			
			l_total_points = 0;			
			l_pts_dollar = 0.0;			
			l_elig_stay_count = 0;			
			l_offer_date = null;			
			l_is_elig = null;			
			l_platinum = null;			
			l_diamond = null;			
			l_max_bonuses = 2;			
			l_registered = null;			
			l_flag = 1;			
			l_linked_already_partic = 0;			
			
			//set debug file to '/tmp/pf_fl0810m_awardbonus.trace';
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
			
			// Get the program code and signup date for the account

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
			
			if (in_rm_revenue == null || in_rm_revenue.equals(0.0)) {				
				return "F";
			}			
			
			// The stay being processed does not have an acct_trans_detail record
			// stored yet. So, we need to calculate its points based on rates,
			// property type, currency, etc.. Gather up that info then calculate
			// the number of points for the stay.
			

			PreparedStatement pstmt3 = prepareStatement(
					  "select s.prop_curr_rate, s.earning_curr_rate, s.post_dtime"
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
			l_prop_curr_rate = rs3.getDouble(1);
			l_earn_curr_rate = rs3.getDouble(2);
			l_curr_post_dtime = rs3.getTimestamp(3);
			pstmt3.close();			
			
			l_current_amount = trunc(new convert_currency().execute(l_prop_curr_rate, l_earn_curr_rate, in_rm_revenue));			
			l_pts_dollar = 10.0;			// mid scale promo so 10 pts per dollar
			l_current_points = l_current_amount * l_pts_dollar;			
			
			// Get a list of linked stays to consider

			Iterator<Object> it0 = find_linked_stays_by_promo(in_promo_id, l_start_date, l_stop_date, in_acct_id).iterator();
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
						  "insert into recent_stay_trans_list_fl10m (stay_id, prop_cd, doa, dod, linked_id, acct_trans_id, ord, post_dtime)"
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
					+ " from recent_stay_trans_list_fl10m l"
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
						+ " from recent_stay_trans_list_fl10m l"
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
			

			PreparedStatement pstmt8 = prepareInsert(
					  "insert into recent_stay_trans_list_fl10m (stay_id, prop_cd, doa, dod, linked_id, ord, post_dtime)"
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
			if (l_curr_post_dtime != null) {
				pstmt8.setObject(7, l_curr_post_dtime);
			}
			else {
				pstmt8.setNull(7, Types.JAVA_OBJECT);
			}
			executeUpdate(pstmt8);
			pstmt8.close();			
			
			// Examine each unique stay to determine if it should be excluded from eligibility
			// Rules are:
			//        Enrollment stay is valid regardless of booking source.
			//        Central booking is valid
			//        Platinum, Diamond, CP MC, CP CE are eligible regardless of booking source
			
			// see if stay at hand is linked with others. If it is, check that bonus has
			// not already been awarded for one of the linked stays. Roll up any points from
			// the linked stays.
			

			PreparedStatement pstmt9 = prepareStatement(
					  "select stay_id, acct_trans_id, doa"
					+ " from recent_stay_trans_list_fl10m"
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
				l_acct_trans_id = rs7.getInt(2);
				l_doa = rs7.getTimestamp(3);				
				
				// see if linked stay has already participated in a bonus award; look at acct_trans_contrib
				// table since the linked stay may have just been part of the bonus and its acct_trans
				// entries will not have any bonus indication
				l_bonus_count = 0;				

				PreparedStatement pstmt10 = prepareStatement(
						  "select count(*)"
						+ " from acct_trans a, acct_trans_contrib atc, acct_trans_detail atd"
						+ " where a.acct_trans_id = atc.acct_trans_id"
						+ " and a.acct_trans_id = atd.acct_trans_id"
						+ " and atc.acct_trans_id = atd.acct_trans_id"
						+ " and atc.contrib_stay_id = ?"
						+ " and a.rev_acct_trans_id is null"
						+ " and atd.promo_id = ?");
				
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
				l_bonus_count = rs8.getInt(1);
				pstmt10.close();				
				
				if (l_bonus_count > 0) {					
					l_already_awarded = 1;					
					break;
				}				
				// roll up any linked points into the current stay's points

				PreparedStatement pstmt11 = prepareStatement(
						  "select d.amount"
						+ " from acct_trans a, acct_trans_detail d, promo p"
						+ " where d.acct_trans_id = ?"
						+ " and a.acct_trans_id = d.acct_trans_id"
						+ " and d.promo_id = p.promo_id"
						+ " and a.rev_acct_trans_id is null"
						+ " and p.rule = \"A\"");
				
				if (l_acct_trans_id != null) {
					pstmt11.setInt(1, l_acct_trans_id);
				}
				else {
					pstmt11.setNull(1, Types.JAVA_OBJECT);
				}
				ResultSet rs9 = executeQuery(pstmt11);
				rs9.next();
				l_linked_amount = rs9.getDouble(1);
				pstmt11.close();				
				if (l_linked_amount != null && l_linked_amount > 0.0) {					
					l_current_points = l_current_points + l_linked_amount;
				}
			}
			pstmt9.close();			
			
			if (l_already_awarded.equals(1)) {				
				return "F";
			}			
			
			// go ahead and insert points for current stay
			// this may include points from the stay at hand plus any points from stays it is linked to

			PreparedStatement pstmt12 = prepareInsert(
					  "insert into qualified_stays_pv_fl10 (amount)"
					+ " values (?)");
			if (l_current_points != null) {
				pstmt12.setDouble(1, l_current_points);
			}
			else {
				pstmt12.setNull(1, Types.JAVA_OBJECT);
			}
			executeUpdate(pstmt12);
			pstmt12.close();			
			
			l_total_qualified = l_total_qualified + 1;			
			
			// This cur1 cursor processes each stay that is not linked. We will
			// see if each one qualifies. If it does, we collect the amount of 
			// points for it. Then, we need to check any linked stays using 
			// another cursor to see if their amount of points should be added
			// to the current cur1 stay we are processing.
			

			PreparedStatement pstmt13 = prepareStatement(
					  "select stay_id, linked_id, acct_trans_id, doa"
					+ " from recent_stay_trans_list_fl10m"
					+ " where stay_id = linked_id"
					+ " and stay_id != ?"
					+ " order by post_dtime desc");
			
			if (n_link != null) {
				pstmt13.setInt(1, n_link);
			}
			else {
				pstmt13.setNull(1, Types.JAVA_OBJECT);
			}
			ResultSet rs10 = executeQuery(pstmt13);
			while (rs10.next()) {
				l_stay_id = rs10.getInt(1);
				l_linked_id = rs10.getInt(2);
				l_acct_trans_id = rs10.getInt(3);
				l_doa = rs10.getTimestamp(4);				
				
				if (l_stay_id.equals(in_stay_id)) {					
					continue;
				}				
				
				// **** new logic needed ****
				// we have to immediately see if the stay being examined is linked to anything else
				// that might have participated in a bonus award; e.g. stay 1, stay 2(bonus), stay 1a, stay 3. In this 
				// case, stay 1a is linked to stay 1 which has already participated in a bonus. So, stay 1a should
				// not be eligible to pair up with stay 3 to trigger another bonus....ugh!
				
				
				l_total_points = 0.0;				
				// Determine if the stay qualifies for the bonus. Note, with l_flag set to 1, this procedure
				// call also looks at the acct_trans_contrib table to see if the stay being examined has been
				// used for a previous bonus award. This replaces the old technique of counting number of
				// eligible stays and number of bonuses received to see if bonus should be awarded.
				l_is_elig = new pf_fl0810_stay_eligible().execute(in_acct_id, l_stay_id, in_promo_id, l_signup_date, l_recog_cd, l_flag);				
				l_current_points = 0.0;				
				if (l_is_elig.equals("T") && l_acct_trans_id != null) {					
					// Get the number of points for the stay from the acct_trans_detail record.

					PreparedStatement pstmt14 = prepareStatement(
							  "select d.amount"
							+ " from acct_trans a, acct_trans_detail d, promo p"
							+ " where d.acct_trans_id = ?"
							+ " and a.acct_trans_id = d.acct_trans_id"
							+ " and d.promo_id = p.promo_id"
							+ " and a.rev_acct_trans_id is null"
							+ " and p.rule = \"A\"");
					
					if (l_acct_trans_id != null) {
						pstmt14.setInt(1, l_acct_trans_id);
					}
					else {
						pstmt14.setNull(1, Types.JAVA_OBJECT);
					}
					ResultSet rs11 = executeQuery(pstmt14);
					rs11.next();
					l_current_points = rs11.getDouble(1);
					pstmt14.close();					
					if (l_current_points != null) {						
						l_total_points = l_total_points + l_current_points;
					}					
					// add entry to temp acct_trans_contrib table

					PreparedStatement pstmt15 = prepareInsert(
							  "insert into t_acct_trans_contrib (promo_id, contrib_stay_id, contrib_acct_trans_id)"
							+ " values (?, ?, ?)");
					if (in_promo_id != null) {
						pstmt15.setInt(1, in_promo_id);
					}
					else {
						pstmt15.setNull(1, Types.JAVA_OBJECT);
					}
					if (l_stay_id != null) {
						pstmt15.setInt(2, l_stay_id);
					}
					else {
						pstmt15.setNull(2, Types.JAVA_OBJECT);
					}
					if (l_acct_trans_id != null) {
						pstmt15.setInt(3, l_acct_trans_id);
					}
					else {
						pstmt15.setNull(3, Types.JAVA_OBJECT);
					}
					executeUpdate(pstmt15);
					pstmt15.close();
				}				
				else {					
					continue;
				}				
				// Now look at potential linked stays; i.e. stays where the linked_stay_id 
				// equals the stay_id being processed in cursor cur1. If any are found,
				// see if each is eligible, and accumulate for the "stay".       
				

				PreparedStatement pstmt16 = prepareStatement(
						  "select stay_id, acct_trans_id"
						+ " from recent_stay_trans_list_fl10m"
						+ " where linked_id = ?"
						+ " and stay_id != ?");
				
				if (l_stay_id != null) {
					pstmt16.setInt(1, l_stay_id);
				}
				else {
					pstmt16.setNull(1, Types.JAVA_OBJECT);
				}
				if (l_stay_id != null) {
					pstmt16.setInt(2, l_stay_id);
				}
				else {
					pstmt16.setNull(2, Types.JAVA_OBJECT);
				}
				ResultSet rs12 = executeQuery(pstmt16);
				while (rs12.next()) {
					l_stay_id2 = rs12.getInt(1);
					l_trans_id = rs12.getInt(2);					
					
					// Now see if this linked stay is eligible. Go through the same
					// checks as the cur1 stay. If any of these are eligible, add
					// their point amount to the current running total.
					
					// First, check that the stay it's linked to has not already participated in a bonus
					// for the promo. Case where the linked stay comes in on a folio, after the
					// parent stay has participated in a bonus award. Ugly ugly ugly. So, you have
					// stay 1, stay 2 (bonus), stay 1a, stay 3. Stay 3 should not pair up with stay 1a.

					PreparedStatement pstmt17 = prepareStatement(
							  "select count(*)"
							+ " from acct_trans a, acct_trans_contrib atc"
							+ " where a.acct_trans_id = atc.acct_trans_id"
							+ " and atc.contrib_stay_id = ?"
							+ " and atc.acct_trans_id = ?"
							+ " and a.rev_acct_trans_id is null");
					
					if (l_stay_id != null) {
						pstmt17.setInt(1, l_stay_id);
					}
					else {
						pstmt17.setNull(1, Types.JAVA_OBJECT);
					}
					if (l_acct_trans_id != null) {
						pstmt17.setInt(2, l_acct_trans_id);
					}
					else {
						pstmt17.setNull(2, Types.JAVA_OBJECT);
					}
					ResultSet rs13 = executeQuery(pstmt17);
					rs13.next();
					l_linked_already_partic = rs13.getInt(1);
					pstmt17.close();					
					
					if (l_linked_already_partic != null && l_linked_already_partic > 0) {						
						continue;
					}					
					
					l_linked_elig = new pf_fl0810_stay_eligible().execute(in_acct_id, l_stay_id2, in_promo_id, l_signup_date, l_recog_cd, l_flag);					
					l_current_points = 0.0;					
					if (l_linked_elig.equals("T") && l_trans_id != null) {						

						PreparedStatement pstmt18 = prepareStatement(
								  "select d.amount"
								+ " from acct_trans a, acct_trans_detail d, promo p"
								+ " where d.acct_trans_id = ?"
								+ " and a.acct_trans_id = d.acct_trans_id"
								+ " and d.promo_id = p.promo_id"
								+ " and a.rev_acct_trans_id is null"
								+ " and p.rule = \"A\"");
						
						if (l_trans_id != null) {
							pstmt18.setInt(1, l_trans_id);
						}
						else {
							pstmt18.setNull(1, Types.JAVA_OBJECT);
						}
						ResultSet rs14 = executeQuery(pstmt18);
						rs14.next();
						l_current_points = rs14.getDouble(1);
						pstmt18.close();						
						if (l_current_points != null) {							
							l_total_points = l_total_points + l_current_points;
						}						
						// add entry to temp acct_trans_contrib table

						PreparedStatement pstmt19 = prepareInsert(
								  "insert into t_acct_trans_contrib (promo_id, contrib_stay_id, contrib_acct_trans_id)"
								+ " values (?, ?, ?)");
						if (in_promo_id != null) {
							pstmt19.setInt(1, in_promo_id);
						}
						else {
							pstmt19.setNull(1, Types.JAVA_OBJECT);
						}
						if (l_stay_id2 != null) {
							pstmt19.setInt(2, l_stay_id2);
						}
						else {
							pstmt19.setNull(2, Types.JAVA_OBJECT);
						}
						if (l_trans_id != null) {
							pstmt19.setInt(3, l_trans_id);
						}
						else {
							pstmt19.setNull(3, Types.JAVA_OBJECT);
						}
						executeUpdate(pstmt19);
						pstmt19.close();
					}
				}
				pstmt16.close();				
				
				// if we have a qualified stay, insert into table for pv_ code. Since this is a
				// Stay Twice promotion, the stay at hand qualified. Then if we have points
				// from the cursors, we have our stays to award the bonus, so get out!
				if (l_total_points > 0.0) {					

					PreparedStatement pstmt20 = prepareInsert(
							  "insert into qualified_stays_pv_fl10 (amount)"
							+ " values (?)");
					if (l_total_points != null) {
						pstmt20.setInt(1, l_total_points);
					}
					else {
						pstmt20.setNull(1, Types.JAVA_OBJECT);
					}
					executeUpdate(pstmt20);
					pstmt20.close();					
					l_total_qualified = l_total_qualified + 1;					
					break;
				}
			}
			pstmt13.close();			
			
			if (l_total_qualified < 2) {				
				// perhaps current stay qualified but no others so no bonus for you!
				// delete anything we stored in the temp acct_trans_contrib table

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
				return "F";
			}			
			
			if (l_total_qualified > 2) {				
				throw new ProcedureException(-746, 0, "pf_fl0810m_awardbonus: unexpected number of rows written for qualified_stays_pv_fl10 table");
			}			
			
			// add entry for current stay to temporary acct_trans_contrib

			PreparedStatement pstmt22 = prepareInsert(
					  "insert into t_acct_trans_contrib (promo_id, contrib_stay_id)"
					+ " values (?, ?)");
			if (in_promo_id != null) {
				pstmt22.setInt(1, in_promo_id);
			}
			else {
				pstmt22.setNull(1, Types.JAVA_OBJECT);
			}
			if (in_stay_id != null) {
				pstmt22.setInt(2, in_stay_id);
			}
			else {
				pstmt22.setNull(2, Types.JAVA_OBJECT);
			}
			executeUpdate(pstmt22);
			pstmt22.close();			
			
			// CE accounts have a pv_flat comp method so we need to drop
			// the table holding the points since pv_fl0810 will not be called
			// for CE accounts
			if (l_recog_cd.equals("CE")) {
			}			
			
			return "T";			
			

		}
		catch (SQLException e) {
			sql_error = e.getErrorCode();
			isam_error = 0;
			error_data = e.getMessage();
			{				

				PreparedStatement pstmt23 = prepareStatement(
						  "delete from t_acct_trans_contrib"
						+ " where promo_id = ?");
				if (in_promo_id != null) {
					pstmt23.setInt(1, in_promo_id);
				}
				else {
					pstmt23.setNull(1, Types.JAVA_OBJECT);
				}
				executeUpdate(pstmt23);
				pstmt23.close();				
				throw new ProcedureException(sql_error, isam_error, error_data);
			}
		}
	}

}