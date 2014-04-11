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

public class pv_free_night extends AbstractProcedure {

	public Double execute(Integer acct_id, Integer cust_id, Integer promo_id, Integer criteria, Double value, Double stay_rev, String chain_cd, Integer los, Integer stay_id) throws SQLException, ProcedureException {		
		
		/*
		 * (]$[) $RCSfile$:$Revision: 99 $ | CDATE=$Date: 2012-12-19 14:14:54 -0700 (Wed, 19 Dec 2012) $ ~
		 * 
		 *   Description: 
		 * 
		 *   Compute the number of points to award to guarantee a free night worth 
		 *   6000 points taking into account the revenue of this stay and the revenue
		 *   of the most recent point eligable stay.
		 * 
		 *       Copyright (C) 2003 Choice Hotels International, Inc.
		 *                       All Rights Reserved
		 */		
		
		Double amount;		
		Integer last_elig_tran_id;		
		Double last_stay_value;		
		Double this_stay_value;		
		Double residual;		
		Timestamp promo_start_date;		
		Timestamp promo_stop_date;		
		
		amount = null;		
		last_elig_tran_id = null;		
		last_stay_value = 0.0;		
		this_stay_value = 0.0;		
		residual = 0.0;		
		promo_start_date = null;		
		promo_stop_date = null;		
		
		//set debug file to '/tmp/pv_free_night.trace';
		//trace on;
		
		// First get the date range in question

		PreparedStatement pstmt1 = prepareStatement(
				  "select start_date, stop_date"
				+ " from promo"
				+ " where promo.promo_id = ?");
		
		if (promo_id != null) {
			pstmt1.setInt(1, promo_id);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		promo_start_date = rs1.getTimestamp(1);
		promo_stop_date = rs1.getTimestamp(2);
		pstmt1.close();		
		
		// Next locate the last point eligable stay transaction id
		// within the promotion period

		PreparedStatement pstmt2 = prepareStatement(
				  "select max(a.acct_trans_id)"
				+ " from stay s, acct_trans a, prop p"
				+ " where s.stay_id = a.stay_id"
				+ " and s.stay_type in (\"N\", \"F\")"
				+ " and s.prop_id = p.prop_id"
				+ " and p.country = \"US\""
				+ " and new get_acct_trans_sum().execute(a.acct_trans_id) > 0"
				+ " and a.acct_id = ?"
				+ " and a.rev_acct_trans_id is null"
				+ " and s.doa >= ?"
				+ " and s.doa <= ?");
		
		if (acct_id != null) {
			pstmt2.setInt(1, acct_id);
		}
		else {
			pstmt2.setNull(1, Types.JAVA_OBJECT);
		}
		if (promo_start_date != null) {
			pstmt2.setObject(2, promo_start_date);
		}
		else {
			pstmt2.setNull(2, Types.JAVA_OBJECT);
		}
		if (promo_stop_date != null) {
			pstmt2.setObject(3, promo_stop_date);
		}
		else {
			pstmt2.setNull(3, Types.JAVA_OBJECT);
		}
		ResultSet rs2 = executeQuery(pstmt2);
		rs2.next();
		last_elig_tran_id = rs2.getInt(1);
		pstmt2.close();		
		
		// If found, get its' base point value, otherwise set to zero
		if (last_elig_tran_id != null) {			

			PreparedStatement pstmt3 = prepareStatement(
					  "select acct_trans_detail.amount"
					+ " from acct_trans_detail"
					+ " where acct_trans_detail.acct_trans_id = ?"
					+ " and acct_trans_detail.ord = 1");
			
			if (last_elig_tran_id != null) {
				pstmt3.setInt(1, last_elig_tran_id);
			}
			else {
				pstmt3.setNull(1, Types.JAVA_OBJECT);
			}
			ResultSet rs3 = executeQuery(pstmt3);
			rs3.next();
			last_stay_value = rs3.getDouble(1);
			pstmt3.close();
		}		
		
		if (last_stay_value == null) {			
			last_stay_value = 0.0;
		}		
		
		// Compute the value of the current stay based on revenue
		
		this_stay_value = value * (trunc(stay_rev, 0) / criteria);		
		
		if (this_stay_value.equals(0.0)) {			
			return 0;
		}		
		
		residual = mod(this_stay_value + last_stay_value, 6000);		
		
		if (residual < 1000.0) {			
			amount = 6000 - residual;
		}		
		else {			
			amount = 5000;
		}		
		
		return amount;
	}

}