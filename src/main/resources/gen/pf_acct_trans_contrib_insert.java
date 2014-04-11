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

public class pf_acct_trans_contrib_insert extends AbstractProcedure {

	public String execute(Integer in_stay_id, Integer in_promo_id, Integer in_link) throws SQLException, ProcedureException {		
		
		/*
		 * $Id: pf_acct_trans_contrib_insert.sql 99 2012-12-19 21:14:54Z michael_giroux $
		 * 
		 *   Description: 
		 * 
		 *   Determine if stay is consecutive with another stay that has already been awarded a bonus
		 *   for the given promotion. The recent_stay_list temp table has already been created and 
		 *   populated with stays for the particular account and promotion. If an transaction is found awarding
		 *   the bonus for the promotion, insert an entry (non-contributing stay type) into the temp 
		 *   acct_trans_contrib table so it will be persisted at the end of the transaction. The t_acct_trans_contrib
		 *   table has been created at a higher level in the stored procedure hierarchy.
		 *   
		 *       Copyright (C) 2012 Choice Hotels International, Inc.
		 *                       All Rights Reserved
		 */		
		
		Integer l_stay_id;		
		Integer l_acct_trans_id;		
		Integer l_ord;		
		Integer l_rev_acct_trans_id;		
		Integer l_act_acct_trans_id;		
		String l_act_insert_needed;		
		String l_answer;		
		
		String l_debug;		
		
		Integer sql_error;		
		Integer isam_error;		
		String error_data;		
		
		try {			
			
			l_debug = "F";			
			l_stay_id = null;			
			l_acct_trans_id = null;			
			l_act_acct_trans_id = null;			
			l_ord = null;			
			l_rev_acct_trans_id = null;			
			l_act_insert_needed = "F";			
			l_answer = "F";			
			
			

			PreparedStatement pstmt1 = prepareStatement(
					  "select stay_id, acct_trans_id"
					+ " from recent_stay_list"
					+ " where linked_id = ?"
					+ " and stay_id != ?");
			
			if (in_link != null) {
				pstmt1.setInt(1, in_link);
			}
			else {
				pstmt1.setNull(1, Types.JAVA_OBJECT);
			}
			if (in_stay_id != null) {
				pstmt1.setInt(2, in_stay_id);
			}
			else {
				pstmt1.setNull(2, Types.JAVA_OBJECT);
			}
			ResultSet rs1 = executeQuery(pstmt1);
			while (rs1.next()) {
				l_stay_id = rs1.getInt(1);
				l_acct_trans_id = rs1.getInt(2);				
				
				l_acct_trans_id = null;
				l_ord = null;
				l_act_acct_trans_id = null;				
				

				PreparedStatement pstmt2 = prepareStatement(
						  "select acct_trans_id, ord"
						+ " from acct_trans_contrib"
						+ " where contrib_stay_id = ?");
				
				if (l_stay_id != null) {
					pstmt2.setInt(1, l_stay_id);
				}
				else {
					pstmt2.setNull(1, Types.JAVA_OBJECT);
				}
				ResultSet rs2 = executeQuery(pstmt2);
				while (rs2.next()) {
					l_act_acct_trans_id = rs2.getInt(1);
					l_ord = rs2.getInt(2);					
					
					// see if this is triggering and reversed

					PreparedStatement pstmt3 = prepareStatement(
							  "select rev_acct_trans_id"
							+ " from acct_trans"
							+ " where acct_trans_id = ?");
					
					if (l_act_acct_trans_id != null) {
						pstmt3.setInt(1, l_act_acct_trans_id);
					}
					else {
						pstmt3.setNull(1, Types.JAVA_OBJECT);
					}
					ResultSet rs3 = executeQuery(pstmt3);
					rs3.next();
					l_rev_acct_trans_id = rs3.getInt(1);
					pstmt3.close();					
					if (l_rev_acct_trans_id == null) {						
						l_act_insert_needed = "Y";						// bonus active so add stay at hand to acct_trans_contrib
						break;
					}
				}
				pstmt2.close();				
				if (l_act_insert_needed.equals("Y")) {					
					break;
				}
			}
			pstmt1.close();			
			
			if (l_act_insert_needed.equals("Y")) {				
				// linked stay with bonus still active so add stay at hand to acct_trans_contrib 
				// to prevent it from being used in future bonus calculations. Be sure to indicate
				// it did not contribute to the bonus calculation, bonus_contrib = 'N'

				PreparedStatement pstmt4 = prepareInsert(
						  "insert into t_acct_trans_contrib (acct_trans_id, promo_id, ord, contrib_stay_id, bonus_contrib)"
						+ " values (?, ?, ?, ?, \"N\")");
				if (l_act_acct_trans_id != null) {
					pstmt4.setInt(1, l_act_acct_trans_id);
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
				if (l_ord != null) {
					pstmt4.setInt(3, l_ord);
				}
				else {
					pstmt4.setNull(3, Types.JAVA_OBJECT);
				}
				if (in_stay_id != null) {
					pstmt4.setInt(4, in_stay_id);
				}
				else {
					pstmt4.setNull(4, Types.JAVA_OBJECT);
				}
				executeUpdate(pstmt4);
				pstmt4.close();				
				l_answer = "T";
			}			
			
			return l_answer;			
			

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