/* Generated on 02-25-2013 12:49:24 PM by SPLParser v0.9 */
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

public class pv_fnsp09ne extends AbstractProcedure {

	public Double execute(Integer acct_id, Integer cust_id, Integer promo_id, Integer criteria, Double value, Double stay_rev, String chain_cd, Integer los, Integer stay_id) throws SQLException, ProcedureException {		
		
		/*
		 * $Id: pv_fnsp09ne.sql 99 2012-12-19 21:14:54Z michael_giroux $
		 * 
		 *   Description: 
		 * 
		 *   Compute the amount of points to award a free night, up to 8000 
		 *   if the property either stay is Modscale. Up to 6000 if both are
		 *   economy. The last stay base points + base points of this stay
		 *   are taken into account.
		 * 
		 *       Copyright (C) 2009 Choice Hotels International, Inc.
		 *                       All Rights Reserved
		 */		
		
		Double amount;		
		Integer last_elig_tran;		
		Double last_stay_value;		
		Double this_stay_value;		
		Double residual;		
		String last_res_source;		
		String found_trans;		
		Timestamp signup_date;		
		Timestamp promo_start_date;		
		Timestamp promo_stop_date;		
		Integer l_stay_id;		
		String l_prop_cd;		
		Timestamp l_doa;		
		Timestamp l_dod;		
		Integer l_linked_id;		
		Integer l_trans_id;		
		Timestamp offer_dtime;		
		String last_chain;		
		
		amount = null;		
		last_elig_tran = null;		
		last_stay_value = 0.0;		
		this_stay_value = 0.0;		
		residual = 0.0;		
		last_res_source = null;		
		found_trans = "F";		
		signup_date = null;		
		promo_start_date = null;		
		promo_stop_date = null;		
		l_stay_id = null;		
		l_prop_cd = null;		
		l_doa = null;		
		l_dod = null;		
		l_linked_id = null;		
		l_trans_id = null;		
		offer_dtime = null;		
		last_chain = null;		
		
		//set debug file to '/tmp/pv_fnsp09ne.trace';
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
		
		// We'll use signup date later on.

		PreparedStatement pstmt2 = prepareStatement(
				  "select acct.signup_date"
				+ " from acct"
				+ " where acct.acct_id = ?");
		
		if (acct_id != null) {
			pstmt2.setInt(1, acct_id);
		}
		else {
			pstmt2.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs2 = executeQuery(pstmt2);
		rs2.next();
		signup_date = rs2.getTimestamp(1);
		pstmt2.close();		
		

		Iterator<Object> it0 = find_linked_stays_by_promo(promo_id, promo_start_date, promo_stop_date, acct_id).iterator();
		l_stay_id = (Integer) it0.next();
		l_prop_cd = (String) it0.next();
		l_doa = (Timestamp) it0.next();
		l_dod = (Timestamp) it0.next();
		l_linked_id = (Integer) it0.next();
		l_trans_id = (Integer) it0.next();
		while (it0.hasNext()) {			
			

			PreparedStatement pstmt3 = prepareInsert(
					  "insert into recent_stay_trans_list (stay_id, prop_cd, doa, dod, linked_id, acct_trans_id)"
					+ " values (?, ?, ?, ?, nvl(?, ?), ?)");
			if (l_stay_id != null) {
				pstmt3.setInt(1, l_stay_id);
			}
			else {
				pstmt3.setNull(1, Types.JAVA_OBJECT);
			}
			if (l_prop_cd != null) {
				pstmt3.setString(2, l_prop_cd);
			}
			else {
				pstmt3.setNull(2, Types.JAVA_OBJECT);
			}
			if (l_doa != null) {
				pstmt3.setObject(3, l_doa);
			}
			else {
				pstmt3.setNull(3, Types.JAVA_OBJECT);
			}
			if (l_dod != null) {
				pstmt3.setObject(4, l_dod);
			}
			else {
				pstmt3.setNull(4, Types.JAVA_OBJECT);
			}
			if (l_linked_id != null) {
				pstmt3.setInt(5, l_linked_id);
			}
			else {
				pstmt3.setNull(5, Types.JAVA_OBJECT);
			}
			if (l_stay_id != null) {
				pstmt3.setInt(6, l_stay_id);
			}
			else {
				pstmt3.setNull(6, Types.JAVA_OBJECT);
			}
			if (l_trans_id != null) {
				pstmt3.setInt(7, l_trans_id);
			}
			else {
				pstmt3.setNull(7, Types.JAVA_OBJECT);
			}
			executeUpdate(pstmt3);
			pstmt3.close();
		}		
		
		// Next locate the base amount for the last posted stay with valid res 
		// source OR signup_date = doa on stay. First hit gets selected regardless
		// of valid selections on linked ID.
		

		PreparedStatement pstmt4 = prepareStatement(
				  "select distinct linked_id"
				+ " from recent_stay_trans_list l"
				+ " where l.doa >= ?"
				+ " and l.doa <= ?"
				+ " order by linked_id desc");
		
		if (promo_start_date != null) {
			pstmt4.setObject(1, promo_start_date);
		}
		else {
			pstmt4.setNull(1, Types.JAVA_OBJECT);
		}
		if (promo_stop_date != null) {
			pstmt4.setObject(2, promo_stop_date);
		}
		else {
			pstmt4.setNull(2, Types.JAVA_OBJECT);
		}
		ResultSet rs3 = executeQuery(pstmt4);
		while (rs3.next()) {
			l_linked_id = rs3.getInt(1);			
			
			// Look at each stay linked together

			PreparedStatement pstmt5 = prepareStatement(
					  "select ?, acct_trans_id"
					+ " from recent_stay_trans_list ll"
					+ " where ll.linked_id = ?"
					+ " order by acct_trans_id desc");
			
			if (stay_id != null) {
				pstmt5.setInt(1, stay_id);
			}
			else {
				pstmt5.setNull(1, Types.JAVA_OBJECT);
			}
			if (l_linked_id != null) {
				pstmt5.setInt(2, l_linked_id);
			}
			else {
				pstmt5.setNull(2, Types.JAVA_OBJECT);
			}
			ResultSet rs4 = executeQuery(pstmt5);
			while (rs4.next()) {
				l_stay_id = rs4.getInt(1);
				l_trans_id = rs4.getInt(2);				
				

				PreparedStatement pstmt6 = prepareStatement(
						  "select s.res_source, s.doa"
						+ " from stay s"
						+ " where s.stay_id = ?");
				
				if (l_stay_id != null) {
					pstmt6.setInt(1, l_stay_id);
				}
				else {
					pstmt6.setNull(1, Types.JAVA_OBJECT);
				}
				ResultSet rs5 = executeQuery(pstmt6);
				rs5.next();
				last_res_source = rs5.getString(1);
				l_doa = rs5.getTimestamp(2);
				pstmt6.close();				
				
				if (last_res_source != null && (last_res_source.equals("C") || last_res_source.equals("N")) && found_trans.equals("F")) {					// found trans yet?
					last_elig_tran = l_trans_id;					
					found_trans = "T";
				}				
				else {					
					if (l_doa.equals(signup_date) && found_trans.equals("F")) {						// at least doa matches
						if (last_res_source != null) {							
							if ((!last_res_source.equals("C") && !last_res_source.equals("N"))) {								
								last_elig_tran = l_trans_id;								
								found_trans = "Y";
							}
						}						
						else {							
							last_elig_tran = l_trans_id;							
							found_trans = "Y";
						}
					}
				}
			}
			pstmt5.close();
		}
		pstmt4.close();		// cur1 looking at stays
		
		// Next determine the chain associated with the last transaction

		PreparedStatement pstmt7 = prepareStatement(
				  "select p.chain_id"
				+ " from stay s, prop p, acct_trans t"
				+ " where t.acct_trans_id = ?"
				+ " and s.stay_id = t.stay_id"
				+ " and p.prop_id = s.prop_id");
		
		if (last_elig_tran != null) {
			pstmt7.setInt(1, last_elig_tran);
		}
		else {
			pstmt7.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs6 = executeQuery(pstmt7);
		rs6.next();
		last_chain = rs6.getString(1);
		pstmt7.close();		
		
		// What was that last one worth?

		PreparedStatement pstmt8 = prepareStatement(
				  "select d.amount"
				+ " from acct_trans_detail d, promo p"
				+ " where d.acct_trans_id = ?"
				+ " and d.promo_id = p.promo_id"
				+ " and p.rule = \"A\"");
		
		if (last_elig_tran != null) {
			pstmt8.setInt(1, last_elig_tran);
		}
		else {
			pstmt8.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs7 = executeQuery(pstmt8);
		rs7.next();
		last_stay_value = rs7.getDouble(1);
		pstmt8.close();		
		
		// Compute the value of the current stay based on revenue and chain
		if ((chain_cd.equals("E") || chain_cd.equals("W") || chain_cd.equals("S") || chain_cd.equals("M"))) {			
			this_stay_value = 5.0 * (trunc(stay_rev, 0) / criteria);
		}		
		else {			
			this_stay_value = 10.0 * (trunc(stay_rev, 0) / criteria);
		}		
		
		if (this_stay_value.equals(0.0)) {			
			return 0;
		}		
		
		this_stay_value = this_stay_value + last_stay_value;		
		
		// Now see what they get, 2 economy/extended stay get 1 value,
		// a combo of mid and econ gets another
		if ((last_chain.equals("E") || last_chain.equals("W") || last_chain.equals("S") || last_chain.equals("M")) && (chain_cd.equals("E") || chain_cd.equals("W") || chain_cd.equals("S") || chain_cd.equals("M"))) {			
			residual = mod(this_stay_value, 6000);			
			
			if (residual < 1000.0) {				
				amount = 6000 - residual;
			}			
			else {				
				amount = 5000;
			}
		}		
		else {			
			residual = mod(this_stay_value, 8000);			
			
			if (residual < 3000.0) {				
				amount = 8000 - residual;
			}			
			else {				
				amount = 5000;
			}
		}		
		
		return amount;
	}

}