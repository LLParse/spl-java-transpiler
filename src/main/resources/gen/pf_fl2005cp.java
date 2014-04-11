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

public class pf_fl2005cp extends AbstractProcedure {

	public String execute(Integer acct_id, Integer promo_id, String stay_type, Integer prop_id, Timestamp doa, Integer los, String rm_type, String srp_code, Double rm_revenue, Double fb_revenue, Double other_revenue, String curr_code, String res_source, Integer stay_id) throws SQLException, ProcedureException {		
		
		/*
		 * pf_fl2005cp.sql -    Check if stay is eligable for the
		 *                      'Stay 2 times and earn 1 free night'
		 *                      promotion. The stays in question are
		 *                      restricted to DOA falling within the
		 *                      promotion start and end dates. Also the
		 *                      stays must have qualified for points.
		 *  
		 *         
		 * $Id: pf_fl2005cp.sql 99 2012-12-19 21:14:54Z michael_giroux $
		 *  
		 *        Copyright (C) 2005 Choice Hotels International, Inc.
		 */		
		Integer no_bonus_cnt;		
		Integer has_bonus_cnt;		
		Timestamp start_date;		
		Timestamp stop_date;		
		
		no_bonus_cnt = 0;		
		has_bonus_cnt = 0;		
		start_date = null;		
		stop_date = null;		
		
		//set debug file to '/tmp/pf_fl2005cp.trace';
		//trace on;
		
		// Check for valid doa

		PreparedStatement pstmt1 = prepareStatement(
				  "select promo.start_date, promo.stop_date"
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
		start_date = rs1.getTimestamp(1);
		stop_date = rs1.getTimestamp(2);
		pstmt1.close();		
		
		if (doa < start_date || doa > stop_date) {			
			return "F";
		}		
		
		// non reversed stays with promotion

		PreparedStatement pstmt2 = prepareStatement(
				  "select count(*)"
				+ " from stay s, acct_trans a, prop p"
				+ " where a.acct_id = ?"
				+ " and a.stay_id = s.stay_id"
				+ " and s.stay_type in (\"N\", \"F\")"
				+ " and s.doa >= ?"
				+ " and s.doa <= ?"
				+ " and p.prop_id = s.prop_id"
				+ " and p.country = \"US\""
				+ " and a.rev_acct_trans_id is null"
				+ " and new get_acct_trans_sum().execute(a.acct_trans_id) > 0"
				+ " and a.acct_trans_id in ("
					+ "select ad.acct_trans_id"
					+ " from acct_trans_detail ad"
					+ " where ad.acct_trans_id = a.acct_trans_id"
					+ " and ad.promo_id in ("
						+ "select ?"
						+ " from promo"
						+ " where promo_cd = \"FL2005CP\""
					+ ")"
				+ ")");
		
		if (acct_id != null) {
			pstmt2.setInt(1, acct_id);
		}
		else {
			pstmt2.setNull(1, Types.JAVA_OBJECT);
		}
		if (start_date != null) {
			pstmt2.setObject(2, start_date);
		}
		else {
			pstmt2.setNull(2, Types.JAVA_OBJECT);
		}
		if (stop_date != null) {
			pstmt2.setObject(3, stop_date);
		}
		else {
			pstmt2.setNull(3, Types.JAVA_OBJECT);
		}
		if (promo_id != null) {
			pstmt2.setInt(4, promo_id);
		}
		else {
			pstmt2.setNull(4, Types.JAVA_OBJECT);
		}
		ResultSet rs2 = executeQuery(pstmt2);
		rs2.next();
		has_bonus_cnt = rs2.getInt(1);
		pstmt2.close();		
		
		// non reversed stays without promotion

		PreparedStatement pstmt3 = prepareStatement(
				  "select count(*)"
				+ " from stay s, acct_trans a, prop p"
				+ " where a.acct_id = ?"
				+ " and a.stay_id = s.stay_id"
				+ " and s.stay_type in (\"N\", \"F\")"
				+ " and s.doa >= ?"
				+ " and s.doa <= ?"
				+ " and p.prop_id = s.prop_id"
				+ " and p.country = \"US\""
				+ " and a.rev_acct_trans_id is null"
				+ " and new get_acct_trans_sum().execute(a.acct_trans_id) > 0"
				+ " and a.acct_trans_id not in ("
					+ "select ad.acct_trans_id"
					+ " from acct_trans_detail ad"
					+ " where ad.acct_trans_id = a.acct_trans_id"
					+ " and ad.promo_id in ("
						+ "select ?"
						+ " from promo"
						+ " where promo_cd = \"FL2005CP\""
					+ ")"
				+ ")");
		
		if (acct_id != null) {
			pstmt3.setInt(1, acct_id);
		}
		else {
			pstmt3.setNull(1, Types.JAVA_OBJECT);
		}
		if (start_date != null) {
			pstmt3.setObject(2, start_date);
		}
		else {
			pstmt3.setNull(2, Types.JAVA_OBJECT);
		}
		if (stop_date != null) {
			pstmt3.setObject(3, stop_date);
		}
		else {
			pstmt3.setNull(3, Types.JAVA_OBJECT);
		}
		if (promo_id != null) {
			pstmt3.setInt(4, promo_id);
		}
		else {
			pstmt3.setNull(4, Types.JAVA_OBJECT);
		}
		ResultSet rs3 = executeQuery(pstmt3);
		rs3.next();
		no_bonus_cnt = rs3.getInt(1);
		pstmt3.close();		
		
		if (has_bonus_cnt - no_bonus_cnt >= 0) {			
			return "F";
		}		
		
		return "T";
	}

}