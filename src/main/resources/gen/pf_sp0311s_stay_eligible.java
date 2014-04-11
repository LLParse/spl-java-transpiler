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

public class pf_sp0311s_stay_eligible extends AbstractProcedure {

	public Collection<Object> execute(Integer in_acct_id, Integer in_stay_id, Integer in_promo_id, Timestamp in_signup_date, String in_recog_cd) throws SQLException, ProcedureException {		
		
		/*
		 * $Id: pf_sp0311s_stay_eligible.sql 99 2012-12-19 21:14:54Z michael_giroux $
		 * 
		 *   Description: 
		 * 
		 *   This procedure is used by the pf_sp0311s_awardbonus procedure to determine if
		 *   a stay being considered as part of the bonus calculation meets the promo 
		 *   requirements. It is really the initial qualification logic of the
		 *   pf_sp0311s filter, but we are looking at past stays that meet the promo criteria for 
		 *   eligibility.
		 *   
		 *   The procedure looks at basic promo requirements for the stay, but also looks to see
		 *   if we are dealing with an economy property with a length of stay of at least 2 nights.
		 *   This results in two extra parameters being returned; 1 indicating the stay is for an
		 *   economy property and one that indicates if it meets the 2 night minimum.
		 *   
		 *       Copyright (C) 2011 Choice Hotels International, Inc.
		 *                       All Rights Reserved
		 */		
		
		String l_res_source;		
		Timestamp l_doa;		
		String l_isplat;		
		String l_isdiamond;		
		Integer l_prop_id;		
		Integer l_los;		
		String l_basic_eligible;		
		String l_econo_eligible;		
		String l_is_econo;		
		String l_chain_group_cd;		
		String l_debug;		
		String l_enrollstay;		
		String l_alreadybonus;		
		Integer l_act_acct_trans_id;		
		Integer l_ord;		
		Integer l_promo_id;		
		Integer l_rev_acct_trans_id;		
		
		Integer sql_error;		
		Integer isam_error;		
		String error_data;		
		
		try {			
			
			l_res_source = null;			
			l_doa = null;			
			l_isplat = null;			
			l_isdiamond = null;			
			l_prop_id = null;			
			l_los = null;			
			l_basic_eligible = "F";			
			l_econo_eligible = "F";			
			l_is_econo = "F";			
			l_chain_group_cd = null;			
			l_debug = "F";			
			l_enrollstay = "F";			
			l_alreadybonus = "F";			
			l_act_acct_trans_id = null;			
			l_ord = null;			
			l_promo_id = null;			
			l_rev_acct_trans_id = null;			
			
			// set up tracing based on app_config entry
			l_debug = new settrace().execute("pf_sp0311s_stay_eligible");			
			if (l_debug.equals("T")) {				
				setDebugFile("/tmp/pf_sp0311s_stay_eligible.trace");				
				trace("on");
			}			
			
			// see if stay being examined has already participated in a bonus for this promo 
			// and that the txn has not been reversed.
			// one stay should only ever be a part of one stay twice, but I'll code it to
			// handle multiple (cursor) and qualify it based on in_promo_id.
			

			PreparedStatement pstmt1 = prepareStatement(
					  "select acct_trans_id"
					+ " from acct_trans_contrib"
					+ " where contrib_stay_id = ?");
			
			if (in_stay_id != null) {
				pstmt1.setInt(1, in_stay_id);
			}
			else {
				pstmt1.setNull(1, Types.JAVA_OBJECT);
			}
			ResultSet rs1 = executeQuery(pstmt1);
			while (rs1.next()) {
				l_act_acct_trans_id = rs1.getInt(1);				
				

				PreparedStatement pstmt2 = prepareStatement(
						  "select a.rev_acct_trans_id"
						+ " from acct_trans a, acct_trans_detail ad"
						+ " where a.acct_trans_id = ad.acct_trans_id"
						+ " and a.acct_trans_id = ?"
						+ " and ad.promo_id = ?");
				
				if (l_act_acct_trans_id != null) {
					pstmt2.setInt(1, l_act_acct_trans_id);
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
				l_rev_acct_trans_id = rs2.getInt(1);
				pstmt2.close();				
				
				if (l_rev_acct_trans_id == null) {					
					l_alreadybonus = "T";					
					break;
				}
			}
			pstmt1.close();			
			
			if (l_alreadybonus.equals("T")) {				
				return new ArrayList<Object>(Arrays.<Object>asList(l_basic_eligible, l_is_econo, l_econo_eligible));
			}			
			

			PreparedStatement pstmt3 = prepareStatement(
					  "select res_source, doa, los, prop_id"
					+ " from stay"
					+ " where stay_id = ?");
			
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
			l_los = rs3.getInt(3);
			l_prop_id = rs3.getInt(4);
			pstmt3.close();			
			
			// central booking qualifies
			if (l_res_source != null && (l_res_source.equals("C") || l_res_source.equals("N") || l_res_source.equals("M"))) {				
				l_basic_eligible = "T";
			}			
			
			// enrollment stay qualifies
			l_enrollstay = new pf_isenrollstay_v2().execute(in_acct_id, l_doa, l_doa + l_los);			
			if (l_enrollstay.equals("T")) {				
				l_basic_eligible = "T";
			}			
			
			// platinum or diamond on doa of stay qualifies
			l_isplat = new pf_isplatinum_ondoa().execute(in_acct_id, l_doa);			
			if (l_isplat.equals("F")) {				
				l_isdiamond = new pf_isdiamond_ondoa().execute(in_acct_id, l_doa);
			}			
			if (l_isplat.equals("T") || l_isdiamond.equals("T")) {				
				l_basic_eligible = "T";
			}			
			

			PreparedStatement pstmt4 = prepareStatement(
					  "select cg.chain_group_cd"
					+ " from chain_group cg, chain_group_detail cgd, prop p"
					+ " where cg.chain_group_id = cgd.chain_group_id"
					+ " and cgd.chain_id = p.chain_id"
					+ " and p.prop_id = ?");
			
			if (l_prop_id != null) {
				pstmt4.setInt(1, l_prop_id);
			}
			else {
				pstmt4.setNull(1, Types.JAVA_OBJECT);
			}
			ResultSet rs4 = executeQuery(pstmt4);
			rs4.next();
			l_chain_group_cd = rs4.getString(1);
			pstmt4.close();			
			
			if ((l_chain_group_cd.equals("EC") || l_chain_group_cd.equals("ES"))) {				
				l_is_econo = "T";				
				if (l_los > 1) {					
					l_econo_eligible = "T";
				}
			}			
			
			// Possible return values;
			// T, T, T => basic eligible, economy stay, requirement of 2 nights minimum met
			// T, T, F => basic eligible, economy stay, less than 2 night minimum
			// T, F, F => basic eligible, midscale stay, N/A
			// F, F, F => basic not eligible, N/A, N/A
			
			return new ArrayList<Object>(Arrays.<Object>asList(l_basic_eligible, l_is_econo, l_econo_eligible));			
			

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