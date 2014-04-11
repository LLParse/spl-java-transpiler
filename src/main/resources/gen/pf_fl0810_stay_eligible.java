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

public class pf_fl0810_stay_eligible extends AbstractProcedure {

	public String execute(Integer in_acct_id, Integer in_stay_id, Integer in_promo_id, Timestamp in_signup_date, String in_recog_cd, Integer in_flag) throws SQLException, ProcedureException {		
		
		/*
		 * $Id: pf_fl0810_stay_eligible.sql 99 2012-12-19 21:14:54Z michael_giroux $
		 * 
		 *   Description: 
		 * 
		 *   This procedure is used by the pf_fl0810m_awardbonus procedure to determine if
		 *   a stay being considered as part of the bonus calculation meets the promo 
		 *   requirements. We are looking at past stays that meet the promo criteria for 
		 *   eligibility. The procedure has two functions. One is to simply determine if
		 *   an existing stay is eligible based on promo requirements. The other is to also 
		 *   determine if the stay has contributed to a previous bonus. This second function
		 *   is used so a past stay is not chosen more than one time when rolling up points
		 *   for the bonus by querying the acct_trans_contrib table.
		 *   
		 *       Copyright (C) 2010 Choice Hotels International, Inc.
		 *                       All Rights Reserved
		 */		
		
		String l_res_source;		
		Timestamp l_doa;		
		String l_isplat;		
		String l_isdiamond;		
		Integer l_offer_id;		
		Timestamp l_offer_date;		
		Integer l_count;		
		String l_eligible;		
		String l_recog_cd;		
		
		Integer sql_error;		
		Integer isam_error;		
		String error_data;		
		
		try {			
			
			l_res_source = null;			
			l_doa = null;			
			l_isplat = null;			
			l_isdiamond = null;			
			l_offer_id = null;			
			l_offer_date = null;			
			l_count = 0;			
			l_eligible = "F";			
			l_recog_cd = null;			
			
			//set debug file to '/tmp/pf_fl0810_stay_eligible.trace';
			//trace on;
			

			PreparedStatement pstmt1 = prepareStatement(
					  "select recog_cd"
					+ " from acct"
					+ " where acct_id = ?");
			
			if (in_acct_id != null) {
				pstmt1.setInt(1, in_acct_id);
			}
			else {
				pstmt1.setNull(1, Types.JAVA_OBJECT);
			}
			ResultSet rs1 = executeQuery(pstmt1);
			rs1.next();
			l_recog_cd = rs1.getString(1);
			pstmt1.close();			
			

			PreparedStatement pstmt2 = prepareStatement(
					  "select res_source, doa"
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
			l_res_source = rs2.getString(1);
			l_doa = rs2.getTimestamp(2);
			pstmt2.close();			
			
			// central booking qualifies
			if (l_res_source != null && (l_res_source.equals("C") || l_res_source.equals("N") || l_res_source.equals("G"))) {				
				l_eligible = "T";
			}			
			
			// enrollment stay qualifies
			if (l_eligible.equals("F") && l_doa.equals(in_signup_date)) {				
				l_eligible = "T";
			}			
			
			// platinum or diamond on doa of stay, or CP Mexico or CP Europe qualifies
			if (l_eligible.equals("F")) {				
				l_isplat = new pf_isplatinum_ondoa().execute(in_acct_id, l_doa);				
				if (l_isplat.equals("F")) {					
					l_isdiamond = new pf_isdiamond_ondoa().execute(in_acct_id, l_doa);
				}				
				if (l_isplat.equals("T") || l_isdiamond.equals("T") || l_recog_cd.equals("MC") || l_recog_cd.equals("CE")) {					
					l_eligible = "T";
				}
			}			
			
			// if it met no criteria, then return false
			if (l_eligible.equals("F")) {				
				return "F";
			}			
			
			// has the stay already contributed to a previous award bonus? For this
			// we need to look at the acct_trans_contrib table to see if the stay
			// is present for this promo
			if (in_flag.equals(1)) {				

				PreparedStatement pstmt3 = prepareStatement(
						  "select count(*)"
						+ " from acct_trans a, acct_trans_contrib atc, acct_trans_detail atd"
						+ " where a.acct_trans_id = atc.acct_trans_id"
						+ " and a.acct_trans_id = atd.acct_trans_id"
						+ " and atc.acct_trans_id = atd.acct_trans_id"
						+ " and atc.contrib_stay_id = ?"
						+ " and a.rev_acct_trans_id is null"
						+ " and atd.promo_id = ?");
				
				if (in_stay_id != null) {
					pstmt3.setInt(1, in_stay_id);
				}
				else {
					pstmt3.setNull(1, Types.JAVA_OBJECT);
				}
				if (in_promo_id != null) {
					pstmt3.setInt(2, in_promo_id);
				}
				else {
					pstmt3.setNull(2, Types.JAVA_OBJECT);
				}
				ResultSet rs3 = executeQuery(pstmt3);
				rs3.next();
				l_count = rs3.getInt(1);
				pstmt3.close();
			}			
			
			if (l_count > 0) {				
				return "F";
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