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

public class pv_fn8000consecutive extends AbstractProcedure {

	public Double execute(Integer acct_id, Integer cust_id, Integer promo_id, Integer criteria, Double value, Double stay_rev, String chain_cd, Integer los, Integer stay_id) throws SQLException, ProcedureException {		
		
		/*
		 * $Id: pv_fn8000consecutive.sql 99 2012-12-19 21:14:54Z michael_giroux $
		 * 
		 *   Description: 
		 * 
		 *   Compute the number of points to award to guarantee a free night worth 
		 *   8000 points taking into account the base points of this stay and the base points
		 *   of the most recent point eligable stay.
		 *   Note: Minimum points awarded is 5000, down from 6500
		 * 
		 *       Copyright (C) 2005 Choice Hotels International, Inc.
		 *                       All Rights Reserved
		 */		
		
		Double amount;		
		Integer last_elig_tran_id;		
		Double last_stay_value;		
		Double this_stay_value;		
		Double residual;		
		Timestamp promo_start_date;		
		Timestamp promo_stop_date;		
		Integer l_stay_id;		
		String l_prop_cd;		
		Timestamp l_doa;		
		Timestamp l_dod;		
		Integer l_linked_id;		
		Integer l_trans_id;		
		Timestamp offer_dtime;		
		
		amount = null;		
		last_elig_tran_id = null;		
		last_stay_value = 0.0;		
		this_stay_value = 0.0;		
		residual = 0.0;		
		promo_start_date = null;		
		promo_stop_date = null;		
		l_stay_id = null;		
		l_prop_cd = null;		
		l_doa = null;		
		l_dod = null;		
		l_linked_id = null;		
		l_trans_id = null;		
		offer_dtime = null;		
		
		//set debug file to '/tmp/pv_fn8000fl06.trace';
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
		

		Iterator<Object> it0 = find_linked_stays_by_promo(promo_id, promo_start_date, promo_stop_date, acct_id).iterator();
		l_stay_id = (Integer) it0.next();
		l_prop_cd = (String) it0.next();
		l_doa = (Timestamp) it0.next();
		l_dod = (Timestamp) it0.next();
		l_linked_id = (Integer) it0.next();
		l_trans_id = (Integer) it0.next();
		while (it0.hasNext()) {			
			

			PreparedStatement pstmt2 = prepareInsert(
					  "insert into recent_stay_trans_list (stay_id, prop_cd, doa, dod, linked_id, acct_trans_id)"
					+ " values (?, ?, ?, ?, nvl(?, ?), ?)");
			if (l_stay_id != null) {
				pstmt2.setInt(1, l_stay_id);
			}
			else {
				pstmt2.setNull(1, Types.JAVA_OBJECT);
			}
			if (l_prop_cd != null) {
				pstmt2.setString(2, l_prop_cd);
			}
			else {
				pstmt2.setNull(2, Types.JAVA_OBJECT);
			}
			if (l_doa != null) {
				pstmt2.setObject(3, l_doa);
			}
			else {
				pstmt2.setNull(3, Types.JAVA_OBJECT);
			}
			if (l_dod != null) {
				pstmt2.setObject(4, l_dod);
			}
			else {
				pstmt2.setNull(4, Types.JAVA_OBJECT);
			}
			if (l_linked_id != null) {
				pstmt2.setInt(5, l_linked_id);
			}
			else {
				pstmt2.setNull(5, Types.JAVA_OBJECT);
			}
			if (l_stay_id != null) {
				pstmt2.setInt(6, l_stay_id);
			}
			else {
				pstmt2.setNull(6, Types.JAVA_OBJECT);
			}
			if (l_trans_id != null) {
				pstmt2.setInt(7, l_trans_id);
			}
			else {
				pstmt2.setNull(7, Types.JAVA_OBJECT);
			}
			executeUpdate(pstmt2);
			pstmt2.close();
		}		
		
		// Next locate the last point eligable stay transaction id
		// within the promotion period
		

		PreparedStatement pstmt3 = prepareStatement(
				  "select sum(d.amount)"
				+ " from recent_stay_trans_list a, acct_trans_detail d, promo p"
				+ " where a.linked_id = ("				

					+ "select l.linked_id"
					+ " from recent_stay_trans_list l"
					+ " where l.acct_trans_id = ("					

						+ "select max(l2.acct_trans_id)"
						+ " from recent_stay_trans_list l2"
						+ " where l2.stay_id = l2.linked_id"
					+ ")"
				+ ")"
				+ " and a.doa >= ?"
				+ " and a.doa <= ?"
				+ " and a.acct_trans_id = d.acct_trans_id"
				+ " and d.promo_id = p.promo_id"
				+ " and p.rule = \"A\"");
		
		if (promo_start_date != null) {
			pstmt3.setObject(1, promo_start_date);
		}
		else {
			pstmt3.setNull(1, Types.JAVA_OBJECT);
		}
		if (promo_stop_date != null) {
			pstmt3.setObject(2, promo_stop_date);
		}
		else {
			pstmt3.setNull(2, Types.JAVA_OBJECT);
		}
		ResultSet rs2 = executeQuery(pstmt3);
		rs2.next();
		last_stay_value = rs2.getDouble(1);
		pstmt3.close();		
		
		if (last_stay_value == null) {			
			last_stay_value = 0.0;
		}		
		
		// Compute the value of the current stay based on revenue
		// Convert value to 5.0 if chain is 'S'uburban since current stay is 5 points/dollar
		if (chain_cd.equals("S")) {			
			value = 5.0;
		}		
		
		this_stay_value = value * (trunc(stay_rev, 0) / criteria);		
		
		if (this_stay_value.equals(0.0)) {			
			return 0;
		}		
		
		residual = mod(this_stay_value + last_stay_value, 8000);		
		
		if (residual < 3000.0) {			
			amount = 8000 - residual;
		}		
		else {			
			amount = 5000;
		}		
		
		return amount;
	}

}