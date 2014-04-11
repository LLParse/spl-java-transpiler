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

public class pf_su0510awardbonus extends AbstractProcedure {

	public String execute(Integer in_acct_id, Integer in_promo_id, String in_stay_type, Integer in_prop_id, Timestamp in_doa, Integer in_los, String in_rm_type, String in_srp_code, Double in_rm_revenue, Double in_fb_revenue, Double in_other_revenue, String in_curr_code, String in_res_source, Integer in_stay_id) throws SQLException, ProcedureException {		
		
		/*
		 * pf_su0510awardbonus.sql - If this routine is called, then the stay being processed meets the
		 *                           Summer 2010 criteria. Now see if the bonus should be awarded; i.e.
		 *                           - are there two stays that meet the criteria; the current one being
		 *                             looked at and one previous. 
		 *                           - non-elite and Gold only earn bonus 2 times
		 *                           - elite Plat or Diamond earn bonus 4 times
		 *                           If so, insert the point values into a temp table to be used by the 
		 *                           pv_su0510 routine to calculate the bonus points awarded.
		 *    
		 * $Id: pf_su0510awardbonus.sql 99 2012-12-19 21:14:54Z michael_giroux $
		 *    
		 *        Copyright (C) 2010 Choice Hotels International, Inc.
		 */		
		
		Integer has_bonus_cnt;		
		Timestamp start_date;		
		Timestamp stop_date;		
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
		
		Integer sql_error;		
		Integer isam_error;		
		String error_data;		
		
		try {			
			
			has_bonus_cnt = 0;			
			start_date = null;			
			stop_date = null;			
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
			l_flag = 0;			
			
			//set debug file to '/tmp/pf_su0510awardbonus.trace';
			//trace on;
			
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
			

			PreparedStatement pstmt3 = prepareStatement(
					  "select o.offer_id"
					+ " from offer o"
					+ " where o.offer_cd = \"SU0510\"");
			
			ResultSet rs3 = executeQuery(pstmt3);
			rs3.next();
			l_offer_id = rs3.getInt(1);
			pstmt3.close();			
			
			// check elite level at DOA
			l_platinum = new pf_isplatinum_ondoa().execute(in_acct_id, in_doa);			
			if (l_platinum.equals("F")) {				
				l_diamond = new pf_isdiamond_ondoa().execute(in_acct_id, in_doa);
			}			
			if (l_platinum.equals("T") || l_diamond.equals("T")) {				
				// If Plat/Diamond are registered on or before DOA, then their max bonuses
				// is increased from 2 to 4.
				l_registered = new pf_isregistered().execute(l_offer_id, in_acct_id, in_doa);				
				if (l_registered.equals("T")) {					
					l_max_bonuses = 4;
				}
			}			
			
			// See if they have exceeded the max number of bonuses allowed for the promo.
			// Non-elite and Gold max allowed is 2, registered Platinum and Diamond is 4

			PreparedStatement pstmt4 = prepareStatement(
					  "select count(*)"
					+ " from acct_trans a, acct_trans_detail ad"
					+ " where a.acct_trans_id = ad.acct_trans_id"
					+ " and a.acct_id = ?"
					+ " and a.rev_acct_trans_id is null"
					+ " and ad.promo_id = ?");
			
			if (in_acct_id != null) {
				pstmt4.setInt(1, in_acct_id);
			}
			else {
				pstmt4.setNull(1, Types.JAVA_OBJECT);
			}
			if (in_promo_id != null) {
				pstmt4.setInt(2, in_promo_id);
			}
			else {
				pstmt4.setNull(2, Types.JAVA_OBJECT);
			}
			ResultSet rs4 = executeQuery(pstmt4);
			rs4.next();
			has_bonus_cnt = rs4.getInt(1);
			pstmt4.close();			
			if (has_bonus_cnt >= l_max_bonuses) {				
				return "F";
			}			
			
			// The stay being processed does not have an acct_trans_detail record
			// stored yet. So, we need to calculate its points based on rates,
			// property type, currency, etc.. Gather up that info then calculate
			// the number of points for the stay.
			

			PreparedStatement pstmt5 = prepareStatement(
					  "select s.prop_curr_rate, s.earning_curr_rate, s.post_dtime"
					+ " from stay s"
					+ " where s.stay_id = ?");
			
			if (in_stay_id != null) {
				pstmt5.setInt(1, in_stay_id);
			}
			else {
				pstmt5.setNull(1, Types.JAVA_OBJECT);
			}
			ResultSet rs5 = executeQuery(pstmt5);
			rs5.next();
			l_prop_curr_rate = rs5.getDouble(1);
			l_earn_curr_rate = rs5.getDouble(2);
			l_curr_post_dtime = rs5.getTimestamp(3);
			pstmt5.close();			
			

			PreparedStatement pstmt6 = prepareStatement(
					  "select cg.chain_group_cd"
					+ " from chain_group cg, chain_group_detail cgd, prop p"
					+ " where cg.chain_group_id = cgd.chain_group_id"
					+ " and cgd.chain_id = p.chain_id"
					+ " and p.prop_id = ?");
			
			if (in_prop_id != null) {
				pstmt6.setInt(1, in_prop_id);
			}
			else {
				pstmt6.setNull(1, Types.JAVA_OBJECT);
			}
			ResultSet rs6 = executeQuery(pstmt6);
			rs6.next();
			l_chain_group_cd = rs6.getString(1);
			pstmt6.close();			
			
			l_current_amount = trunc(new convert_currency().execute(l_prop_curr_rate, l_earn_curr_rate, in_rm_revenue));			
			if ((l_chain_group_cd.equals("MS") || l_chain_group_cd.equals("US"))) {				
				l_pts_dollar = 10.0;
			}			
			else {				
				l_pts_dollar = 5.0;
			}			
			
			l_current_points = l_current_amount * l_pts_dollar;			
			
			// Get a list of linked stays to consider

			Iterator<Object> it0 = find_linked_stays_by_promo(in_promo_id, start_date, stop_date, in_acct_id).iterator();
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
						  "insert into recent_stay_trans_list_su10 (stay_id, prop_cd, doa, dod, linked_id, acct_trans_id, ord, post_dtime)"
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
					+ " from recent_stay_trans_list_su10 l"
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
						+ " from recent_stay_trans_list_su10 l"
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
					  "insert into recent_stay_trans_list_su10 (stay_id, prop_cd, doa, dod, linked_id, ord, post_dtime)"
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
			
			// Examine each unique stay to determine if it should be excluded from eligibility
			// Rules are:
			//        Enrollment stay is valid regardless of booking source.
			//        Central booking is valid
			//        Platinum and Diamond at DOA are eligible regardless of booking source
			
			// We need to determine up front how many bonus eligible stays we are
			// dealing with in the temp table that has been constructed. We can't 
			// do this procedurally as we process. It just won't work.
			l_elig_stay_count = 0;			
			

			PreparedStatement pstmt13 = prepareStatement(
					  "select stay_id"
					+ " from recent_stay_trans_list_su10"
					+ " where linked_id = stay_id");
			
			ResultSet rs11 = executeQuery(pstmt13);
			while (rs11.next()) {
				l_stay_id = rs11.getInt(1);				
				
				l_is_elig = new pf_su0510_stay_eligible().execute(in_acct_id, l_stay_id, in_promo_id, l_signup_date, l_recog_cd, l_flag);				
				if (l_is_elig.equals("T")) {					
					l_elig_stay_count = l_elig_stay_count + 1;
				}
			}
			pstmt13.close();			
			l_flag = 1;			// set for all other calls to pf..stay_eligible
			
			// if there are not enough eligible, no need to go on
			if (trunc(l_elig_stay_count / 2) <= has_bonus_cnt) {				
				return "F";
			}			
			
			
			
			// see if stay at hand is linked with others. If it is, check that bonus has
			// not already been awarded for one of the linked stays. We will not roll
			// up points from the other linked stays.
			

			PreparedStatement pstmt14 = prepareStatement(
					  "select stay_id, acct_trans_id, doa"
					+ " from recent_stay_trans_list_su10"
					+ " where linked_id = ?"
					+ " and stay_id != ?");
			
			if (n_link != null) {
				pstmt14.setInt(1, n_link);
			}
			else {
				pstmt14.setNull(1, Types.JAVA_OBJECT);
			}
			if (in_stay_id != null) {
				pstmt14.setInt(2, in_stay_id);
			}
			else {
				pstmt14.setNull(2, Types.JAVA_OBJECT);
			}
			ResultSet rs12 = executeQuery(pstmt14);
			while (rs12.next()) {
				l_stay_id = rs12.getInt(1);
				l_acct_trans_id = rs12.getInt(2);
				l_doa = rs12.getTimestamp(3);				
				
				// we only want to count one room of a multi-room stay, in this
				// case, we already have l_current_points from stay at hand
				if (l_doa.equals(in_doa)) {					
					continue;
				}				
				
				// see if linked stay has already been awarded the bonus
				l_linked_amount = 0;				

				PreparedStatement pstmt15 = prepareStatement(
						  "select ad.amount"
						+ " from acct_trans a, acct_trans_detail ad"
						+ " where a.acct_trans_id = ad.acct_trans_id"
						+ " and a.acct_trans_id = ?"
						+ " and a.rev_acct_trans_id is null"
						+ " and ad.promo_id = ?");
				
				if (l_acct_trans_id != null) {
					pstmt15.setInt(1, l_acct_trans_id);
				}
				else {
					pstmt15.setNull(1, Types.JAVA_OBJECT);
				}
				if (in_promo_id != null) {
					pstmt15.setInt(2, in_promo_id);
				}
				else {
					pstmt15.setNull(2, Types.JAVA_OBJECT);
				}
				ResultSet rs13 = executeQuery(pstmt15);
				rs13.next();
				l_linked_amount = rs13.getDouble(1);
				pstmt15.close();				
				
				if (l_linked_amount > 0) {					
					l_already_awarded = 1;					
					break;
				}
			}
			pstmt14.close();			
			
			if (l_already_awarded.equals(1)) {				
				return "F";
			}			
			
			// go ahead and insert points for current stay
			// this may include the stay at hand plus any stay it is linked to
			

			PreparedStatement pstmt16 = prepareInsert(
					  "insert into qualified_stays_pv_su10 (amount)"
					+ " values (?)");
			if (l_current_points != null) {
				pstmt16.setDouble(1, l_current_points);
			}
			else {
				pstmt16.setNull(1, Types.JAVA_OBJECT);
			}
			executeUpdate(pstmt16);
			pstmt16.close();			
			
			l_total_qualified = l_total_qualified + 1;			
			
			// This cur1 cursor processes each stay that is not linked. We will
			// see if each one qualifies. If it does, we collect the amount of 
			// points for it. Then, we need to check any linked stays using 
			// another cursor to see if their amount of points should be added
			// to the current cur1 stay we are processing.
			

			PreparedStatement pstmt17 = prepareStatement(
					  "select stay_id, linked_id, acct_trans_id, doa"
					+ " from recent_stay_trans_list_su10"
					+ " where stay_id = linked_id"
					+ " and stay_id != ?"
					+ " order by post_dtime desc");
			
			if (n_link != null) {
				pstmt17.setInt(1, n_link);
			}
			else {
				pstmt17.setNull(1, Types.JAVA_OBJECT);
			}
			ResultSet rs14 = executeQuery(pstmt17);
			while (rs14.next()) {
				l_stay_id = rs14.getInt(1);
				l_linked_id = rs14.getInt(2);
				l_acct_trans_id = rs14.getInt(3);
				l_doa = rs14.getTimestamp(4);				
				
				if (l_stay_id.equals(in_stay_id)) {					
					continue;
				}				
				
				l_total_points = 0.0;				
				// Determine if the stay qualifies for the bonus.
				// get booking source, doa, prop_id for stay
				l_is_elig = new pf_su0510_stay_eligible().execute(in_acct_id, l_stay_id, in_promo_id, l_signup_date, l_recog_cd, l_flag);				
				// Get the number of points for the stay from the acct_trans_detail record.
				l_current_points = 0.0;				
				if (l_is_elig.equals("T") && l_acct_trans_id != null) {					
					// see if bonus awarded for this stay

					PreparedStatement pstmt18 = prepareStatement(
							  "select count(*)"
							+ " from acct_trans a, acct_trans_detail d"
							+ " where a.acct_trans_id = d.acct_trans_id"
							+ " and a.acct_trans_id = ?"
							+ " and a.rev_acct_trans_id is null"
							+ " and d.promo_id = ?");
					
					if (l_acct_trans_id != null) {
						pstmt18.setInt(1, l_acct_trans_id);
					}
					else {
						pstmt18.setNull(1, Types.JAVA_OBJECT);
					}
					if (in_promo_id != null) {
						pstmt18.setInt(2, in_promo_id);
					}
					else {
						pstmt18.setNull(2, Types.JAVA_OBJECT);
					}
					ResultSet rs15 = executeQuery(pstmt18);
					rs15.next();
					l_already_awarded = rs15.getInt(1);
					pstmt18.close();					
					if (l_already_awarded.equals(0) || l_already_awarded == null) {						

						PreparedStatement pstmt19 = prepareStatement(
								  "select d.amount"
								+ " from acct_trans a, acct_trans_detail d, promo p"
								+ " where d.acct_trans_id = ?"
								+ " and a.acct_trans_id = d.acct_trans_id"
								+ " and d.promo_id = p.promo_id"
								+ " and a.rev_acct_trans_id is null"
								+ " and p.rule = \"A\"");
						
						if (l_acct_trans_id != null) {
							pstmt19.setInt(1, l_acct_trans_id);
						}
						else {
							pstmt19.setNull(1, Types.JAVA_OBJECT);
						}
						ResultSet rs16 = executeQuery(pstmt19);
						rs16.next();
						l_current_points = rs16.getDouble(1);
						pstmt19.close();						
						if (l_current_points != null) {							
							l_total_points = l_total_points + l_current_points;
						}						
						// add entry to temp acct_trans_contrib table

						PreparedStatement pstmt20 = prepareInsert(
								  "insert into t_acct_trans_contrib (promo_id, contrib_stay_id, contrib_acct_trans_id)"
								+ " values (?, ?, ?)");
						if (in_promo_id != null) {
							pstmt20.setInt(1, in_promo_id);
						}
						else {
							pstmt20.setNull(1, Types.JAVA_OBJECT);
						}
						if (l_stay_id != null) {
							pstmt20.setInt(2, l_stay_id);
						}
						else {
							pstmt20.setNull(2, Types.JAVA_OBJECT);
						}
						if (l_acct_trans_id != null) {
							pstmt20.setInt(3, l_acct_trans_id);
						}
						else {
							pstmt20.setNull(3, Types.JAVA_OBJECT);
						}
						executeUpdate(pstmt20);
						pstmt20.close();
					}
				}				
				
				// if we have a qualified stay, insert into table for pv_ code. Since this is a
				// Stay Twice promotion, the stay at hand qualified. Then if we have points
				// from the cursors, we have our stays to award the bonus, so get out!
				if (l_total_points > 0.0) {					

					PreparedStatement pstmt21 = prepareInsert(
							  "insert into qualified_stays_pv_su10 (amount)"
							+ " values (?)");
					if (l_total_points != null) {
						pstmt21.setInt(1, l_total_points);
					}
					else {
						pstmt21.setNull(1, Types.JAVA_OBJECT);
					}
					executeUpdate(pstmt21);
					pstmt21.close();					
					l_total_qualified = l_total_qualified + 1;					
					break;
				}
			}
			pstmt17.close();			
			
			if (l_total_qualified < 2) {				
				// perhaps current stay qualified but no others so no bonus for you!
				// delete anything we stored in the temp acct_trans_contrib table

				PreparedStatement pstmt22 = prepareStatement(
						  "delete from t_acct_trans_contrib"
						+ " where promo_id = ?");
				if (in_promo_id != null) {
					pstmt22.setInt(1, in_promo_id);
				}
				else {
					pstmt22.setNull(1, Types.JAVA_OBJECT);
				}
				executeUpdate(pstmt22);
				pstmt22.close();				
				return "F";
			}			
			
			if (l_total_qualified > 2) {				
				throw new ProcedureException(-746, 0, "pf_su0510awardbonus: unexpected number of rows written for qualified_stays_pv_su10 table");
			}			
			
			// add entry for current stay to temporary acct_trans_contrib

			PreparedStatement pstmt23 = prepareInsert(
					  "insert into t_acct_trans_contrib (promo_id, contrib_stay_id)"
					+ " values (?, ?)");
			if (in_promo_id != null) {
				pstmt23.setInt(1, in_promo_id);
			}
			else {
				pstmt23.setNull(1, Types.JAVA_OBJECT);
			}
			if (in_stay_id != null) {
				pstmt23.setInt(2, in_stay_id);
			}
			else {
				pstmt23.setNull(2, Types.JAVA_OBJECT);
			}
			executeUpdate(pstmt23);
			pstmt23.close();			
			return "T";			
			

		}
		catch (SQLException e) {
			sql_error = e.getErrorCode();
			isam_error = 0;
			error_data = e.getMessage();
			{				

				PreparedStatement pstmt24 = prepareStatement(
						  "delete from t_acct_trans_contrib"
						+ " where promo_id = ?");
				if (in_promo_id != null) {
					pstmt24.setInt(1, in_promo_id);
				}
				else {
					pstmt24.setNull(1, Types.JAVA_OBJECT);
				}
				executeUpdate(pstmt24);
				pstmt24.close();				
				throw new ProcedureException(sql_error, isam_error, error_data);
			}
		}
	}

}