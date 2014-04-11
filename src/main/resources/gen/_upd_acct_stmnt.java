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

public class _upd_acct_stmnt extends AbstractProcedure {

	public void execute(Integer acct_id, String trans_type, Double amount, String stay_type, String rev_indicator) throws SQLException, ProcedureException {		
		
		/*
		 * $RCSfile$ - Update an account statement record - $Revision: 99 $
		 * 
		 * (]$[) $RCSfile$:$Revision: 99 $ | CDATE=$Date: 2012-12-19 14:14:54 -0700 (Wed, 19 Dec 2012) $ ~
		 * 
		 *  Creates/updates a members' account statement record. Statement ID is based
		 *  on current date.
		 * 
		 *  1. Get the most recent statement record for the account.
		 *  2. If non-existent, create one.
		 *  3. If found, but most recent is not in the current statement, create
		 *     current statement record and move totalizer values into it.
		 *  4. Update the current record with the transaction data.
		 */		
		
		Integer current_stmnt_id;		
		Integer last_stmnt_id;		
		String update_life;		
		
		Double beg_balance;		
		Double end_balance;		
		Double amt_earned;		
		Double amt_redeemed;		
		Double amt_adjusted;		
		Double amt_expire1;		
		Timestamp exp_date1;		
		Double amt_expire2;		
		Timestamp exp_date2;		
		Double amt_life_earned;		
		Integer exp_rec;		
		Double amount_exp;		
		Timestamp date_exp;		
		
		beg_balance = 0.0;		
		end_balance = 0.0;		
		amt_earned = 0.0;		
		amt_redeemed = 0.0;		
		amt_adjusted = 0.0;		
		amt_expire1 = 0.0;		
		exp_date1 = null;		
		amt_expire2 = 0.0;		
		exp_date2 = null;		
		amt_life_earned = 0.0;		
		exp_rec = null;		
		amount_exp = 0.0;		
		date_exp = null;		
		
		current_stmnt_id = null;		
		last_stmnt_id = null;		
		update_life = null;		
		
		// set debug file to '/tmp/_upd_acct_stmnt.trace';
		// trace on;
		
		// Get current statement record.
		current_stmnt_id = new get_stmnt_id().execute(new Timestamp(System.currentTimeMillis()), new get_recog_cd().execute(acct_id));		
		
		// Get statement ID of members' most recent statement.
		// If the member has no statement records just bypass the 
		// rollover process and begin with the current trans data only.

		PreparedStatement pstmt1 = prepareStatement(
				  "select max(acct_stmnt.stmnt_id)"
				+ " from acct_stmnt"
				+ " where acct_stmnt.acct_id = ?");
		
		if (acct_id != null) {
			pstmt1.setInt(1, acct_id);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		last_stmnt_id = rs1.getInt(1);
		pstmt1.close();		
		
		if (last_stmnt_id.equals(current_stmnt_id)) {			// get current values

			PreparedStatement pstmt2 = prepareStatement(
					  "select acct_stmnt.beg_balance, acct_stmnt.end_balance, acct_stmnt.amt_earned, acct_stmnt.amt_redeemed, acct_stmnt.amt_adjusted, acct_stmnt.amt_life_earned"
					+ " from acct_stmnt"
					+ " where acct_stmnt.acct_id = ?"
					+ " and acct_stmnt.stmnt_id = ?");
			
			if (acct_id != null) {
				pstmt2.setInt(1, acct_id);
			}
			else {
				pstmt2.setNull(1, Types.JAVA_OBJECT);
			}
			if (current_stmnt_id != null) {
				pstmt2.setInt(2, current_stmnt_id);
			}
			else {
				pstmt2.setNull(2, Types.JAVA_OBJECT);
			}
			ResultSet rs2 = executeQuery(pstmt2);
			rs2.next();
			beg_balance = rs2.getDouble(1);
			end_balance = rs2.getDouble(2);
			amt_earned = rs2.getDouble(3);
			amt_redeemed = rs2.getDouble(4);
			amt_adjusted = rs2.getDouble(5);
			amt_life_earned = rs2.getDouble(6);
			pstmt2.close();
		}		
		
		if (last_stmnt_id != null && !last_stmnt_id.equals(current_stmnt_id)) {			
			
			// New statement record for members account
			// Roll in latest figures into current statement. Note that 
			// the end balance is loaded into the beginning balance.
			

			PreparedStatement pstmt3 = prepareStatement(
					  "select acct_stmnt.end_balance, acct_stmnt.amt_life_earned"
					+ " from acct_stmnt"
					+ " where acct_stmnt.acct_id = ?"
					+ " and acct_stmnt.stmnt_id = ?");
			
			if (acct_id != null) {
				pstmt3.setInt(1, acct_id);
			}
			else {
				pstmt3.setNull(1, Types.JAVA_OBJECT);
			}
			if (last_stmnt_id != null) {
				pstmt3.setInt(2, last_stmnt_id);
			}
			else {
				pstmt3.setNull(2, Types.JAVA_OBJECT);
			}
			ResultSet rs3 = executeQuery(pstmt3);
			rs3.next();
			beg_balance = rs3.getDouble(1);
			amt_life_earned = rs3.getDouble(2);
			pstmt3.close();			
			
			// Initialize the rest of the record
			end_balance = beg_balance;
		}		
		
		// Next update the expiration fields. They are set equal to the values in the 
		// acct_exp table for the 2 most immenantly expiring records.
		exp_rec = 1;		
		

		PreparedStatement pstmt4 = prepareStatement(
				  "select a.amount, a.exp_date"
				+ " from acct_exp a"
				+ " where a.acct_id = ?"
				+ " and a.exp_date > today"
				+ " and a.amount > 0.0"
				+ " order by 2 ASC");
		
		if (acct_id != null) {
			pstmt4.setInt(1, acct_id);
		}
		else {
			pstmt4.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs4 = executeQuery(pstmt4);
		while (rs4.next()) {
			amount_exp = rs4.getDouble(1);
			date_exp = rs4.getTimestamp(2);			
			
			if (exp_rec.equals(1)) {				
				amt_expire1 = amount_exp;				
				exp_date1 = date_exp;				
				exp_rec = 2;
			}			
			else if (exp_rec.equals(2)) {				
				amt_expire2 = amount_exp;				
				exp_date2 = date_exp;				
				exp_rec = 3;
			}
		}
		pstmt4.close();		
		// Now set to null expiration data with zero points
		if (amt_expire1.equals(0)) {			
			amt_expire1 = null;			
			exp_date1 = null;
		}		
		if (amt_expire2.equals(0)) {			
			amt_expire2 = null;			
			exp_date2 = null;
		}		
		
		// end balance always changes by transaction amount
		end_balance = end_balance + amount;		
		
		// check if lifetime balance is adjusted
		update_life = new update_cumul().execute(trans_type, stay_type);		
		if (update_life.equals("Y")) {			
			amt_life_earned = amt_life_earned + amount;
		}		
		
		// Increment the totalizers based on transaction type
		if (trans_type.equals("S")) {			// a stay of some type
			amt_earned = amt_earned + amount;
		}		// end if a stay
		
		if (trans_type.equals("A")) {			// an adjustment
			amt_adjusted = amt_adjusted + amount;
		}		
		
		if (trans_type.equals("E")) {			// an expiration
			amt_adjusted = amt_adjusted + amount;
		}		
		
		if (trans_type.equals("R")) {			// a redemption
			amt_redeemed = amt_redeemed + amount;
		}		
		
		if (trans_type.equals("B")) {			// a bonus type
			amt_earned = amt_earned + amount;
		}		
		
		// Finally add/update acct_stmnt table
		if (last_stmnt_id.equals(current_stmnt_id)) {			// Update existing

			PreparedStatement pstmt5 = prepareStatement(
					  "update acct_stmnt"
					+ " set (beg_balance, end_balance, amt_earned, amt_redeemed, amt_adjusted, amt_expire1, exp_date1, amt_expire2, exp_date2, amt_life_earned, last_update_dtime) = (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, current)"
					+ " where acct_stmnt.acct_id = ?"
					+ " and acct_stmnt.stmnt_id = ?");
			
			if (beg_balance != null) {
				pstmt5.setDouble(1, beg_balance);
			}
			else {
				pstmt5.setNull(1, Types.JAVA_OBJECT);
			}
			if (end_balance != null) {
				pstmt5.setDouble(2, end_balance);
			}
			else {
				pstmt5.setNull(2, Types.JAVA_OBJECT);
			}
			if (amt_earned != null) {
				pstmt5.setDouble(3, amt_earned);
			}
			else {
				pstmt5.setNull(3, Types.JAVA_OBJECT);
			}
			if (amt_redeemed != null) {
				pstmt5.setDouble(4, amt_redeemed);
			}
			else {
				pstmt5.setNull(4, Types.JAVA_OBJECT);
			}
			if (amt_adjusted != null) {
				pstmt5.setDouble(5, amt_adjusted);
			}
			else {
				pstmt5.setNull(5, Types.JAVA_OBJECT);
			}
			if (amt_expire1 != null) {
				pstmt5.setDouble(6, amt_expire1);
			}
			else {
				pstmt5.setNull(6, Types.JAVA_OBJECT);
			}
			if (exp_date1 != null) {
				pstmt5.setObject(7, exp_date1);
			}
			else {
				pstmt5.setNull(7, Types.JAVA_OBJECT);
			}
			if (amt_expire2 != null) {
				pstmt5.setDouble(8, amt_expire2);
			}
			else {
				pstmt5.setNull(8, Types.JAVA_OBJECT);
			}
			if (exp_date2 != null) {
				pstmt5.setObject(9, exp_date2);
			}
			else {
				pstmt5.setNull(9, Types.JAVA_OBJECT);
			}
			if (amt_life_earned != null) {
				pstmt5.setDouble(10, amt_life_earned);
			}
			else {
				pstmt5.setNull(10, Types.JAVA_OBJECT);
			}
			if (acct_id != null) {
				pstmt5.setInt(11, acct_id);
			}
			else {
				pstmt5.setNull(11, Types.JAVA_OBJECT);
			}
			if (current_stmnt_id != null) {
				pstmt5.setInt(12, current_stmnt_id);
			}
			else {
				pstmt5.setNull(12, Types.JAVA_OBJECT);
			}
			executeUpdate(pstmt5);
			pstmt5.close();
		}		
		else {			

			PreparedStatement pstmt6 = prepareInsert(
					  "insert into acct_stmnt (acct_id, stmnt_id, beg_balance, end_balance, amt_earned, amt_redeemed, amt_adjusted, amt_expire1, exp_date1, amt_expire2, exp_date2, amt_life_earned, last_update_dtime)"
					+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, current)");
			if (acct_id != null) {
				pstmt6.setInt(1, acct_id);
			}
			else {
				pstmt6.setNull(1, Types.JAVA_OBJECT);
			}
			if (current_stmnt_id != null) {
				pstmt6.setInt(2, current_stmnt_id);
			}
			else {
				pstmt6.setNull(2, Types.JAVA_OBJECT);
			}
			if (beg_balance != null) {
				pstmt6.setDouble(3, beg_balance);
			}
			else {
				pstmt6.setNull(3, Types.JAVA_OBJECT);
			}
			if (end_balance != null) {
				pstmt6.setDouble(4, end_balance);
			}
			else {
				pstmt6.setNull(4, Types.JAVA_OBJECT);
			}
			if (amt_earned != null) {
				pstmt6.setDouble(5, amt_earned);
			}
			else {
				pstmt6.setNull(5, Types.JAVA_OBJECT);
			}
			if (amt_redeemed != null) {
				pstmt6.setDouble(6, amt_redeemed);
			}
			else {
				pstmt6.setNull(6, Types.JAVA_OBJECT);
			}
			if (amt_adjusted != null) {
				pstmt6.setDouble(7, amt_adjusted);
			}
			else {
				pstmt6.setNull(7, Types.JAVA_OBJECT);
			}
			if (amt_expire1 != null) {
				pstmt6.setDouble(8, amt_expire1);
			}
			else {
				pstmt6.setNull(8, Types.JAVA_OBJECT);
			}
			if (exp_date1 != null) {
				pstmt6.setObject(9, exp_date1);
			}
			else {
				pstmt6.setNull(9, Types.JAVA_OBJECT);
			}
			if (amt_expire2 != null) {
				pstmt6.setDouble(10, amt_expire2);
			}
			else {
				pstmt6.setNull(10, Types.JAVA_OBJECT);
			}
			if (exp_date2 != null) {
				pstmt6.setObject(11, exp_date2);
			}
			else {
				pstmt6.setNull(11, Types.JAVA_OBJECT);
			}
			if (amt_life_earned != null) {
				pstmt6.setDouble(12, amt_life_earned);
			}
			else {
				pstmt6.setNull(12, Types.JAVA_OBJECT);
			}
			executeUpdate(pstmt6);
			pstmt6.close();
		}		
		
		return;
	}

}