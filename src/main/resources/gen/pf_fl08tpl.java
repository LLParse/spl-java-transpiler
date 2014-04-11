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

public class pf_fl08tpl extends AbstractProcedure {

	public String execute(Integer acct_id, Integer promo_id, String stay_type, Integer prop_id, Timestamp doa, Integer los, String rm_type, String srp_code, Double rm_revenue, Double fb_revenue, Double other_revenue, String curr_code, String res_source, Integer stay_id) throws SQLException, ProcedureException {		
		
		
		/*
		 * pf_fl08tpl.sql - Checks if stay qualifies for Fall 2008 Midscale triple point promotion.
		 *                  It is called by the wrapper functions pf_fl08et and pf_fl08numt and
		 *                  does this:
		 *                  DOA falls within promotion period.
		 *                  After first stay.
		 *                  IS booked via internet or 800-4CHOICE
		 *                  Not a duplicate stay (same Date of dep as another stay).
		 * 
		 * (]$[) $RCSfile$:$Revision: 19481 $ | CDATE=$Date: 2008-10-13 12:36:18 -0700 (Mon, 13 Oct 2008) $ ~
		 * 
		 *        Copyright (C) 2005 Choice Hotels International, Inc.
		 *      All Rights Reserved
		 */		
		
		Integer bonus_cnt;		
		Double qual_stay_amt;		
		Timestamp start_date;		
		Timestamp stop_date;		
		
		bonus_cnt = null;		
		qual_stay_amt = 0.0;		
		start_date = null;		
		stop_date = null;		
		
		//set debug file to '/tmp/pf_fl08tpl.trace';
		//trace on;
		
		// Check criteria doing the easy/fast ones first..
		
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
		
		
		// Check the res_source to see if it IS set to 'N'-internet or 'C'-800-4CHOICE.
		if (res_source == null) {			
			return "F";
		}		
		if (res_source.equals("W") || res_source.equals("G") || res_source.equals("I") || res_source.equals("O")) {			
			return "F";
		}		
		
		// Is this after at least 1 point eligible stay in promotion period?

		PreparedStatement pstmt2 = prepareStatement(
				  "select sum(d.amount)"
				+ " from stay s, acct_trans t, acct_trans_detail d, prop p, chain c"
				+ " where s.doa >= ?"
				+ " and s.doa <= ?"
				+ " and s.stay_type in (\"N\", \"F\")"
				+ " and s.stay_id = t.stay_id"
				+ " and s.prop_id = p.prop_id"
				+ " and p.chain_id = c.chain_id"
				+ " and t.acct_trans_id = d.acct_trans_id"
				+ " and t.acct_id = ?"
				+ " and t.rev_acct_trans_id is null"
				+ " and c.chain_id in ("				

					+ "select chain_id"
					+ " from promo_prop_partic"
					+ " where promo_prop_partic.promo_id = ?"
					+ " and promo_prop_partic.chain_group_id is null"					
					

					+ " union "
					+ "select g.chain_id"
					+ " from promo_prop_partic c"
					+ "  inner join chain_group_detail g on c.chain_group_id = g.chain_group_id"
					+ " where c.promo_id = ?"
					+ " and c.chain_group_id is not null"
				+ ")");
		
		if (start_date != null) {
			pstmt2.setObject(1, start_date);
		}
		else {
			pstmt2.setNull(1, Types.JAVA_OBJECT);
		}
		if (stop_date != null) {
			pstmt2.setObject(2, stop_date);
		}
		else {
			pstmt2.setNull(2, Types.JAVA_OBJECT);
		}
		if (acct_id != null) {
			pstmt2.setInt(3, acct_id);
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
		if (promo_id != null) {
			pstmt2.setInt(5, promo_id);
		}
		else {
			pstmt2.setNull(5, Types.JAVA_OBJECT);
		}
		ResultSet rs2 = executeQuery(pstmt2);
		rs2.next();
		qual_stay_amt = rs2.getDouble(1);
		pstmt2.close();		
		
		if (qual_stay_amt == null) {			
			return "F";
		}		
		
		if (qual_stay_amt <= 0.0) {			
			return "F";
		}		
		
		// Now see if this is a duplicate stay/same departure date and promo awarded

		PreparedStatement pstmt3 = prepareStatement(
				  "select count(*)"
				+ " from stay s, acct_trans t, acct_trans_detail d"
				+ " where s.doa + s.los = ? + ?"
				+ " and s.prop_id = ?"
				+ " and s.stay_id = t.stay_id"
				+ " and t.acct_trans_id = d.acct_trans_id"
				+ " and d.promo_id = ?"
				+ " and t.acct_id = ?"
				+ " and t.rev_acct_trans_id is null");
		
		if (doa != null) {
			pstmt3.setObject(1, doa);
		}
		else {
			pstmt3.setNull(1, Types.JAVA_OBJECT);
		}
		if (los != null) {
			pstmt3.setInt(2, los);
		}
		else {
			pstmt3.setNull(2, Types.JAVA_OBJECT);
		}
		if (prop_id != null) {
			pstmt3.setInt(3, prop_id);
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
		if (acct_id != null) {
			pstmt3.setInt(5, acct_id);
		}
		else {
			pstmt3.setNull(5, Types.JAVA_OBJECT);
		}
		ResultSet rs3 = executeQuery(pstmt3);
		rs3.next();
		bonus_cnt = rs3.getInt(1);
		pstmt3.close();		
		
		if (bonus_cnt > 0) {			
			return "F";
		}		
		
		return "T";
	}

}