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

public class pf_vanilla_revenue_noreg extends AbstractProcedure {

	public String execute(Integer in_acct_id, Integer in_promo_id, String in_stay_type, Integer in_prop_id, Timestamp in_doa, Integer in_los, String in_rm_type, String in_srp_code, Double in_rm_revenue, Double in_fb_revenue, Double in_other_revenue, String in_curr_code, String in_res_source, Integer in_stay_id) throws SQLException, ProcedureException {		
		
		/*
		 * Promo filter for a generic, standard, vanilla flavored revenue type promotion. 
		 *          The promotions using this filter will be configured by Marketing using the Promo Config Tool
		 *          or can be loaded with the batch CampaignImport loader program using an xml input file.
		 *                      
		 *          The standard, unchanging requirements for a stay to be awarded a bonus with this filter are:
		 *    
		 *              1) There are no registration requirements - pretty concrete fact
		 *              2) There are no booking source requirements - but there could be if so desired. This include reservation dates/windows
		 *                 that they may want to specify.
		 *                 NOTE: 1 and 2 are typically for the International programs; i.e. Europe, Australasia, Latin America
		 *              3) There may be a prerequisite stay requirement: promo.prerequisite_stays is checked to see if 'n' number of stays 
		 *                 must have been completed before bonuses are awarded.
		 *              
		 *              NOTE: If the promotion has a max_amount or bonus cap limit, that check if deferred to the promo_value
		 *                    handler. All promotions in the campaign with a promo.class = 'RVCAP' must be examined to get a sum of points 
		 *                    across all promotions.
		 *    
		 *        $Id: pf_vanilla_revenue_noreg.sql 99 2012-12-19 21:14:54Z michael_giroux $
		 *    
		 *        Copyright (C) 2012 Choice Hotels International, Inc.
		 */		
		
		// define local variables to be used in the procedure
		Timestamp l_start_date;		
		Timestamp l_stop_date;		
		Timestamp l_doa;		
		Timestamp l_doa2;		
		String l_recog_cd;		
		Integer l_link;		
		String l_enrollstay;		
		String l_eligible;		
		String l_answer;		
		String l_debug;		
		String l_temp_exists;		
		String l_promo_cd;		
		String l_check_consecutive;		
		Integer l_stay_count;		
		Integer l_linked_id;		
		Integer l_link_count;		
		Integer l_max_amount;		
		Integer l_prerequisite_stays;		
		Integer l_stay_id;		
		String l_srp_code;		
		Double l_points;		
		Integer l_bonus_cnt;		
		Integer l_acct_trans_id;		
		
		// debug variables for dumping out the temporary recent_stay_list table if debug trace is turned on
		Integer l_dbg_stay_id;		
		String l_dbg_prop_cd;		
		Timestamp l_dbg_doa;		
		Timestamp l_dbg_dod;		
		Integer l_dbg_linked_id;		
		Integer l_dbg_acct_trans_id;		
		Integer l_dbg_ord;		
		Timestamp l_dbg_post_dtime;		
		String l_dbg_res_source;		
		Double l_dbg_points;		
		
		// variables for thrown exceptions
		Integer sql_error;		
		Integer isam_error;		
		String error_data;		
		
		try {			
			
			// initialize all local variables
			l_start_date = null;			
			l_stop_date = null;			
			l_doa = null;			
			l_doa2 = null;			
			l_recog_cd = null;			
			l_link = null;			
			l_enrollstay = "F";			
			l_eligible = "F";			
			l_answer = "F";			
			l_debug = "F";			
			l_temp_exists = "F";			
			l_check_consecutive = "F";			
			l_stay_count = 0;			
			l_linked_id = 0;			
			l_link_count = 0;			
			l_max_amount = 0;			
			l_prerequisite_stays = 0;			
			l_stay_id = null;			
			l_srp_code = null;			
			l_points = 0.0;			
			l_bonus_cnt = null;			
			l_acct_trans_id = null;			
			
			l_dbg_stay_id = null;			
			l_dbg_prop_cd = null;			
			l_dbg_doa = null;			
			l_dbg_dod = null;			
			l_dbg_linked_id = null;			
			l_dbg_acct_trans_id = null;			
			l_dbg_ord = null;			
			l_dbg_post_dtime = null;			
			l_dbg_res_source = null;			
			l_dbg_points = null;			
			
			// set up tracing based on app_config entry
			l_debug = new settrace().execute("pf_vanilla_revenue_noreg");			
			if (l_debug.equals("T")) {				
				setDebugFile("/tmp/pf_vanilla_revenue_noreg_" + dbinfo("sessionid") + ".trace");				
				trace("on");
			}			
			
			// Retrieve some promo info

			PreparedStatement pstmt1 = prepareStatement(
					  "select p.start_date, p.stop_date, p.recog_cd, p.max_amount, p.prerequisite_stays, p.promo_cd"
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
			l_recog_cd = rs1.getString(3);
			l_max_amount = rs1.getInt(4);
			l_prerequisite_stays = rs1.getInt(5);
			l_promo_cd = rs1.getString(6);
			pstmt1.close();			
			
			// see if any booking requirements were met
			//call pf_revenue_booking(in_acct_id, in_doa, in_los, in_res_source, in_stay_id, l_promo_cd) 
			//   returning l_answer, l_check_consecutive;
			
			//if (l_answer = 'F') then
			//   return l_answer; -- failed so get out
			//end if;
			
			// if we have a number of stays required before we begin awarding a bonus, then we need to check
			// for consecutiveness to make sure the prerequisite number of stays is met. For example, if 
			// prerequisite_stays is 1, and we have check-in/check-outs on 3/1, 3/2, 3/3 at the samp property, then those 3 stays
			// are really on one logical stay so a bonus would not be awarded until, for example, a 3/6 stay.
			if (l_prerequisite_stays != null) {				
				l_link = new pf_load_recent_stay_list().execute(in_acct_id, in_stay_id, in_promo_id, in_prop_id, in_doa, in_doa + in_los, l_start_date, l_stop_date);				
				l_temp_exists = "T";				
				
				if (l_debug.equals("T")) {					
					

					PreparedStatement pstmt2 = prepareStatement(
							  "select stay_id, prop_cd, doa, dod, linked_id, acct_trans_id, ord, post_dtime, res_source, points"
							+ " from recent_stay_list");
					
					ResultSet rs2 = executeQuery(pstmt2);
					while (rs2.next()) {
						l_dbg_stay_id = rs2.getInt(1);
						l_dbg_prop_cd = rs2.getString(2);
						l_dbg_doa = rs2.getTimestamp(3);
						l_dbg_dod = rs2.getTimestamp(4);
						l_dbg_linked_id = rs2.getInt(5);
						l_dbg_acct_trans_id = rs2.getInt(6);
						l_dbg_ord = rs2.getInt(7);
						l_dbg_post_dtime = rs2.getTimestamp(8);
						l_dbg_res_source = rs2.getString(9);
						l_dbg_points = rs2.getDouble(10);
					}
					pstmt2.close();
				}				
				
				// count how many stays there have been. There is no registration requirements but we have
				// to consider SRD stays
				

				PreparedStatement pstmt3 = prepareStatement(
						  "select stay_id, linked_id, doa"
						+ " from recent_stay_list"
						+ " where stay_id = linked_id"
						+ " order by doa");
				
				ResultSet rs3 = executeQuery(pstmt3);
				while (rs3.next()) {
					l_stay_id = rs3.getInt(1);
					l_linked_id = rs3.getInt(2);
					l_doa = rs3.getTimestamp(3);					
					

					PreparedStatement pstmt4 = prepareStatement(
							  "select s.srp_code"
							+ " from stay s"
							+ " where s.stay_id = ?");
					
					if (l_stay_id != null) {
						pstmt4.setInt(1, l_stay_id);
					}
					else {
						pstmt4.setNull(1, Types.JAVA_OBJECT);
					}
					ResultSet rs4 = executeQuery(pstmt4);
					rs4.next();
					l_srp_code = rs4.getString(1);
					pstmt4.close();					
					
					if (l_srp_code != null && l_srp_code.equals("SRD")) {						
						//see if any non-SRD stays linked to this SRD stay
						

						PreparedStatement pstmt5 = prepareStatement(
								  "select doa, points"
								+ " from recent_stay_list"
								+ " where linked_id = ?");
						
						if (l_stay_id != null) {
							pstmt5.setInt(1, l_stay_id);
						}
						else {
							pstmt5.setNull(1, Types.JAVA_OBJECT);
						}
						ResultSet rs5 = executeQuery(pstmt5);
						while (rs5.next()) {
							l_doa2 = rs5.getTimestamp(1);
							l_points = rs5.getDouble(2);							
							
							if (l_points > 0) {								
								l_stay_count = l_stay_count + 1;								
								break;
							}
						}
						pstmt5.close();
					}					
					else {						
						l_stay_count = l_stay_count + 1;						
						continue;
					}
				}
				pstmt3.close();				
				
				if (l_stay_count > l_prerequisite_stays) {					
					l_answer = "T";
				}
			}			
			
			// make check for stay with same doa as input stay and already awarded a bonus
			if (l_answer.equals("T")) {				
				l_doa = null;				
				l_stay_id = null;				
				

				PreparedStatement pstmt6 = prepareStatement(
						  "select stay_id, acct_trans_id, doa"
						+ " from recent_stay_list"
						+ " where linked_id = ?"
						+ " and stay_id != ?");
				
				if (l_link != null) {
					pstmt6.setInt(1, l_link);
				}
				else {
					pstmt6.setNull(1, Types.JAVA_OBJECT);
				}
				if (in_stay_id != null) {
					pstmt6.setInt(2, in_stay_id);
				}
				else {
					pstmt6.setNull(2, Types.JAVA_OBJECT);
				}
				ResultSet rs6 = executeQuery(pstmt6);
				while (rs6.next()) {
					l_stay_id = rs6.getInt(1);
					l_acct_trans_id = rs6.getInt(2);
					l_doa = rs6.getTimestamp(3);					
					l_bonus_cnt = 0;					
					if (l_doa.equals(in_doa)) {						

						PreparedStatement pstmt7 = prepareStatement(
								  "select count(*)"
								+ " from acct_trans a"
								+ "  inner join acct_trans_detail ad on a.acct_trans_id = ad.acct_trans_id"
								+ " where a.acct_trans_id = ?"
								+ " and a.rev_acct_trans_id is null"
								+ " and a.acct_id = ?"
								+ " and ad.promo_id = ?");
						
						if (l_acct_trans_id != null) {
							pstmt7.setInt(1, l_acct_trans_id);
						}
						else {
							pstmt7.setNull(1, Types.JAVA_OBJECT);
						}
						if (in_acct_id != null) {
							pstmt7.setInt(2, in_acct_id);
						}
						else {
							pstmt7.setNull(2, Types.JAVA_OBJECT);
						}
						if (in_promo_id != null) {
							pstmt7.setInt(3, in_promo_id);
						}
						else {
							pstmt7.setNull(3, Types.JAVA_OBJECT);
						}
						ResultSet rs7 = executeQuery(pstmt7);
						rs7.next();
						l_bonus_cnt = rs7.getInt(1);
						pstmt7.close();
					}					
					if (l_bonus_cnt > 0) {						
						l_answer = "F";						
						break;
					}
				}
				pstmt6.close();
			}			
			
			// if we've not passed above tests, then get out, dropping temp table if it was created
			if (l_answer.equals("F")) {				
				if (l_temp_exists.equals("T")) {
				}				
				return l_answer;
			}			
			
			// Stay appears eligible, but we need to see if consecutiveness needs to be
			// checked and if so, is the stay consecutive with any other stays that may have been 
			// awarded the bonus before giving a final thumbs up.
			if (l_check_consecutive.equals("T")) {				
				if (l_temp_exists.equals("F")) {					
					l_link = new pf_load_recent_stay_list().execute(in_acct_id, in_stay_id, in_promo_id, in_prop_id, in_doa, in_doa + in_los, l_start_date, l_stop_date);					
					l_temp_exists = "T";
				}				
				l_answer = new pf_vanilla_revenue_awardbonus().execute(in_acct_id, in_promo_id, in_stay_type, in_prop_id, in_doa, in_los, in_rm_type, in_srp_code, in_rm_revenue, in_fb_revenue, in_other_revenue, in_curr_code, in_res_source, in_stay_id, l_link);
			}			
			
			if (l_temp_exists.equals("T")) {
			}			
			
			return l_answer;			
			

		}
		catch (SQLException e) {
			sql_error = e.getErrorCode();
			isam_error = 0;
			error_data = e.getMessage();
			{				
				if (l_temp_exists.equals("T")) {
				}				
				throw new ProcedureException(sql_error, isam_error, error_data);
			}
		}
	}

}