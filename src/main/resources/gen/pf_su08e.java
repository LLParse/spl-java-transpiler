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

public class pf_su08e extends AbstractProcedure {

	public String execute(Integer acct_id, Integer promo_id, String stay_type, Integer prop_id, Timestamp doa, Integer los, String rm_type, String srp_code, Double rm_revenue, Double fb_revenue, Double other_revenue, String curr_code, String res_source, Integer stay_id) throws SQLException, ProcedureException {		
		
		/*
		 * pf_su08e.sql -       Check if stay is eligible for the
		 *                      Summer 2008 economy and extended stay promotion
		 *                      where non-consecutive stays earn 1000 bonus points.
		 *           
		 * $Id: pf_su08e.sql 99 2012-12-19 21:14:54Z michael_giroux $
		 *    
		 *        Copyright (C) 2008 Choice Hotels International, Inc.
		 */		
		Integer no_bonus_cnt;		
		Integer has_bonus_cnt;		
		Integer des_bonus_cnt;		
		Integer stay_cnt;		
		Timestamp start_date;		
		Timestamp stop_date;		
		String recog_cd;		
		Integer check_promo_id;		
		Integer l_stay_id;		
		String l_prop_cd;		
		Timestamp l_doa;		
		Timestamp l_dod;		
		Integer l_linked_id;		
		Integer l_trans_id;		
		Timestamp f_dod;		
		Integer n_id;		
		Integer n_link;		
		Timestamp offer_dtime;		
		
		no_bonus_cnt = 0;		
		has_bonus_cnt = 0;		
		des_bonus_cnt = 0;		
		stay_cnt = 0;		
		start_date = null;		
		stop_date = null;		
		recog_cd = null;		
		check_promo_id = null;		
		l_stay_id = null;		
		l_prop_cd = null;		
		l_doa = null;		
		l_dod = null;		
		l_linked_id = null;		
		l_trans_id = null;		
		f_dod = doa + los;		
		n_id = null;		
		n_link = null;		
		offer_dtime = null;		
		
		//set debug file to '/tmp/pf_su08e.trace';
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
		
		
		// Check the res_source to see if it's set to 'N' for internet.
		if (res_source == null) {			
			return "F";
		}		
		if (!res_source.equals("N")) {			
			return "F";
		}		
		
		// Get the program code of the account

		PreparedStatement pstmt2 = prepareStatement(
				  "select acct.recog_cd"
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
		recog_cd = rs2.getString(1);
		pstmt2.close();		
		
		// Get the promo ID for counting awarded bonuses
		// NOTE: This MUST be adjusted for each new filter!

		PreparedStatement pstmt3 = prepareStatement(
				  "select promo.promo_id"
				+ " from promo"
				+ " where promo.recog_cd = ?"
				+ " and promo.promo_cd = \"SU08E\"");
		
		if (recog_cd != null) {
			pstmt3.setString(1, recog_cd);
		}
		else {
			pstmt3.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs3 = executeQuery(pstmt3);
		rs3.next();
		check_promo_id = rs3.getInt(1);
		pstmt3.close();		
		

		Iterator<Object> it0 = find_linked_stays_by_promo(promo_id, start_date, stop_date, acct_id).iterator();
		l_stay_id = (Integer) it0.next();
		l_prop_cd = (String) it0.next();
		l_doa = (Timestamp) it0.next();
		l_dod = (Timestamp) it0.next();
		l_linked_id = (Integer) it0.next();
		l_trans_id = (Integer) it0.next();
		while (it0.hasNext()) {			
			

			PreparedStatement pstmt4 = prepareInsert(
					  "insert into recent_stay_trans_list (stay_id, prop_cd, doa, dod, linked_id, acct_trans_id)"
					+ " values (?, ?, ?, ?, nvl(?, ?), ?)");
			if (l_stay_id != null) {
				pstmt4.setInt(1, l_stay_id);
			}
			else {
				pstmt4.setNull(1, Types.JAVA_OBJECT);
			}
			if (l_prop_cd != null) {
				pstmt4.setString(2, l_prop_cd);
			}
			else {
				pstmt4.setNull(2, Types.JAVA_OBJECT);
			}
			if (l_doa != null) {
				pstmt4.setObject(3, l_doa);
			}
			else {
				pstmt4.setNull(3, Types.JAVA_OBJECT);
			}
			if (l_dod != null) {
				pstmt4.setObject(4, l_dod);
			}
			else {
				pstmt4.setNull(4, Types.JAVA_OBJECT);
			}
			if (l_linked_id != null) {
				pstmt4.setInt(5, l_linked_id);
			}
			else {
				pstmt4.setNull(5, Types.JAVA_OBJECT);
			}
			if (l_stay_id != null) {
				pstmt4.setInt(6, l_stay_id);
			}
			else {
				pstmt4.setNull(6, Types.JAVA_OBJECT);
			}
			if (l_trans_id != null) {
				pstmt4.setInt(7, l_trans_id);
			}
			else {
				pstmt4.setNull(7, Types.JAVA_OBJECT);
			}
			executeUpdate(pstmt4);
			pstmt4.close();
		}		
		
		// See if current stay is linked (consecutive) with any stays already in our list

		PreparedStatement pstmt5 = prepareStatement(
				  "select max(l.id)"
				+ " from recent_stay_trans_list l"
				+ " where l.prop_cd = new get_prop_cd().execute(?)"
				+ " and ("(" || 
				+ ") || (" || 
				+ ")
				+ ")");
		
		if (prop_id != null) {
			pstmt5.setInt(1, prop_id);
		}
		else {
			pstmt5.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs4 = executeQuery(pstmt5);
		rs4.next();
		n_id = rs4.getInt(1);
		pstmt5.close();		
		
		if (n_id != null) {			

			PreparedStatement pstmt6 = prepareStatement(
					  "select nvl(l.linked_id, l.stay_id)"
					+ " from recent_stay_trans_list l"
					+ " where l.id = ?");
			
			if (n_id != null) {
				pstmt6.setInt(1, n_id);
			}
			else {
				pstmt6.setNull(1, Types.JAVA_OBJECT);
			}
			ResultSet rs5 = executeQuery(pstmt6);
			rs5.next();
			n_link = rs5.getInt(1);
			pstmt6.close();
		}		
		else {			
			n_link = stay_id;
		}		
		

		PreparedStatement pstmt7 = prepareInsert(
				  "insert into recent_stay_trans_list (stay_id, prop_cd, doa, dod, linked_id)"
				+ " values (?, new get_prop_cd().execute(?), ?, ?, ?)");
		if (stay_id != null) {
			pstmt7.setInt(1, stay_id);
		}
		else {
			pstmt7.setNull(1, Types.JAVA_OBJECT);
		}
		if (prop_id != null) {
			pstmt7.setInt(2, prop_id);
		}
		else {
			pstmt7.setNull(2, Types.JAVA_OBJECT);
		}
		if (doa != null) {
			pstmt7.setObject(3, doa);
		}
		else {
			pstmt7.setNull(3, Types.JAVA_OBJECT);
		}
		if (f_dod != null) {
			pstmt7.setObject(4, f_dod);
		}
		else {
			pstmt7.setNull(4, Types.JAVA_OBJECT);
		}
		if (n_link != null) {
			pstmt7.setInt(5, n_link);
		}
		else {
			pstmt7.setNull(5, Types.JAVA_OBJECT);
		}
		executeUpdate(pstmt7);
		pstmt7.close();		
		
		if (n_link.equals(stay_id)) {			
			return "T";
		}		// stay linked to itself
		else {			
			return "F";
		}
	}

}