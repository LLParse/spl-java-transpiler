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

public class _upd_acct_exp extends AbstractProcedure {

	public void execute(Integer acct_id_in, Double amount_in, Integer acct_trans_id_in, Timestamp exp_date_in) throws SQLException, ProcedureException {		
		/*
		 * $RCSfile$ - Update the amount of amount expiring from an account - $Revision: 99 $
		 * 
		 *         (]$[) $RCSfile$:$Revision: 99 $ | CDATE=$Date: 2012-12-19 14:14:54 -0700 (Wed, 19 Dec 2012) $ ~
		 * 
		 *               Copyright (C) 2001 Choice Hotels International, Inc.
		 *                               All Rights Reserved
		 */		
		
		Integer l_balance;		
		Integer l_exp_bal;		
		Integer l_exp_temp_bal;		
		String l_aging_type;		
		Integer l_orig_trans_id;		
		Integer l_restore_cnt;		
		Timestamp t_exp_date;		
		Double t_amount;		
		Double amount_required;		
		Integer l_acct_exp_temp_id;		
		Integer l_tmp_amount;		
		
		l_balance = 0;		
		l_exp_bal = 0;		
		l_exp_temp_bal = 0;		
		l_aging_type = null;		
		l_orig_trans_id = null;		
		l_restore_cnt = 0;		
		t_exp_date = null;		
		t_amount = null;		
		amount_required = 0.0;		
		l_acct_exp_temp_id = 0;		
		l_tmp_amount = 0;		
		
		// set debug file to '/tmp/_upd_acct_exp.trace';
		// trace on;
		
		// don't bother w/ zero point transactions
		if (amount_in.equals(0)) {			
			return;
		}		
		

		PreparedStatement pstmt1 = prepareStatement(
				  "select sum(amount)"
				+ " from acct_exp"
				+ " where acct_id = ?");
		
		if (acct_id_in != null) {
			pstmt1.setInt(1, acct_id_in);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		l_exp_bal = rs1.getInt(1);
		pstmt1.close();		
		

		PreparedStatement pstmt2 = prepareStatement(
				  "select ("sum(amount)
				+ ") * -1"
				+ " from acct_exp_temp"
				+ " where acct_id = ?"
				+ " and amount < 0.0"
				+ " and ins_dtime > current - 8hour");
		
		if (acct_id_in != null) {
			pstmt2.setInt(1, acct_id_in);
		}
		else {
			pstmt2.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs2 = executeQuery(pstmt2);
		rs2.next();
		l_exp_temp_bal = rs2.getInt(1);
		pstmt2.close();		
		
		l_balance = l_exp_bal + l_exp_temp_bal;		
		
		// no sense going through the motions if we are consuming more points than the account has
		if (amount_in > l_balance) {			
			throw new ProcedureException(-746, 0, "_upd_acct_exp: Unable to consume value [" + amount_in + "] from account. Balance: " + l_balance);
		}		
		
		// determine type of transaction we are dealing with
		// accumulate   == points earned [stay, partner {cobrand, erac}, adjustment(+)]
		// consume      == srd booking, award redemption, adjustment(-)
		// restore      == srd cancel, award redemption reversed, adjustment(+/-) reversed, stay reversed
		
		new get_trans_aging_type().execute(acct_trans_id_in, amount_in);		
		
		if (l_aging_type.equals("restore")) {			
			l_restore_cnt = new restore_points_aging().execute(l_orig_trans_id, acct_trans_id_in, acct_id_in);			
			
			if (l_restore_cnt > 0) {				
				return;
			}
		}		
		
		if (amount_in > 0.0) {			// Increment the account.

			PreparedStatement pstmt3 = prepareStatement(
					  "update acct_exp"
					+ " set amount = amount + ?"
					+ " where acct_id = ?"
					+ " and exp_date = ?");
			
			if (amount_in != null) {
				pstmt3.setDouble(1, amount_in);
			}
			else {
				pstmt3.setNull(1, Types.JAVA_OBJECT);
			}
			if (acct_id_in != null) {
				pstmt3.setInt(2, acct_id_in);
			}
			else {
				pstmt3.setNull(2, Types.JAVA_OBJECT);
			}
			if (exp_date_in != null) {
				pstmt3.setObject(3, exp_date_in);
			}
			else {
				pstmt3.setNull(3, Types.JAVA_OBJECT);
			}
			executeUpdate(pstmt3);
			pstmt3.close();			
			
			// If the update count was not 1, the record does not exist so create it
			if (!dbinfo("sqlca.sqlerrd2").equals(1)) {				

				PreparedStatement pstmt4 = prepareInsert(
						  "insert into acct_exp (acct_id, exp_date, amount)"
						+ " values (?, ?, ?)");
				if (acct_id_in != null) {
					pstmt4.setInt(1, acct_id_in);
				}
				else {
					pstmt4.setNull(1, Types.JAVA_OBJECT);
				}
				if (exp_date_in != null) {
					pstmt4.setObject(2, exp_date_in);
				}
				else {
					pstmt4.setNull(2, Types.JAVA_OBJECT);
				}
				if (amount_in != null) {
					pstmt4.setDouble(3, amount_in);
				}
				else {
					pstmt4.setNull(3, Types.JAVA_OBJECT);
				}
				executeUpdate(pstmt4);
				pstmt4.close();
			}			
			

			PreparedStatement pstmt5 = prepareInsert(
					  "insert into acct_trans_aging (acct_trans_id, acct_id, exp_date, amount)"
					+ " values (?, ?, ?, ?)");
			if (acct_trans_id_in != null) {
				pstmt5.setInt(1, acct_trans_id_in);
			}
			else {
				pstmt5.setNull(1, Types.JAVA_OBJECT);
			}
			if (acct_id_in != null) {
				pstmt5.setInt(2, acct_id_in);
			}
			else {
				pstmt5.setNull(2, Types.JAVA_OBJECT);
			}
			if (exp_date_in != null) {
				pstmt5.setObject(3, exp_date_in);
			}
			else {
				pstmt5.setNull(3, Types.JAVA_OBJECT);
			}
			if (amount_in != null) {
				pstmt5.setDouble(4, amount_in);
			}
			else {
				pstmt5.setNull(4, Types.JAVA_OBJECT);
			}
			executeUpdate(pstmt5);
			pstmt5.close();
		}		
		else {			// Decrement account
			
			amount_required = amount_in;			
			
			if (l_aging_type.equals("redemption")) {				
				

				PreparedStatement pstmt6 = prepareStatement(
						  "select acct_exp_temp_id, amount * -1"
						+ " from acct_exp_temp"
						+ " where acct_id = ?"
						+ " and amount < 0.0"
						+ " and ins_dtime > current - 8hour"
						+ " order by ins_dtime");
				
				if (acct_id_in != null) {
					pstmt6.setInt(1, acct_id_in);
				}
				else {
					pstmt6.setNull(1, Types.JAVA_OBJECT);
				}
				ResultSet rs3 = executeQuery(pstmt6);
				while (rs3.next()) {
					l_acct_exp_temp_id = rs3.getInt(1);
					l_tmp_amount = rs3.getInt(2);					
					
					if (amount_required.equals(0.0)) {						
						break;
					}					
					
					if (l_tmp_amount < amount_required) {						
						amount_required = amount_required - l_tmp_amount;						
						

						PreparedStatement pstmt7 = prepareStatement(
								  "update acct_exp_temp"
								+ " set amount = 0.0"
								+ " where acct_exp_temp_id = ?");
						
						if (l_acct_exp_temp_id != null) {
							pstmt7.setInt(1, l_acct_exp_temp_id);
						}
						else {
							pstmt7.setNull(1, Types.JAVA_OBJECT);
						}
						executeUpdate(pstmt7);
						pstmt7.close();						
						

						PreparedStatement pstmt8 = prepareInsert(
								  "insert into acct_trans_aging (acct_trans_id, acct_id, exp_date, amount)"
								+ " values (?, ?, mdy(12, 31, year(current) - 1), ?)");
						if (acct_trans_id_in != null) {
							pstmt8.setInt(1, acct_trans_id_in);
						}
						else {
							pstmt8.setNull(1, Types.JAVA_OBJECT);
						}
						if (acct_id_in != null) {
							pstmt8.setInt(2, acct_id_in);
						}
						else {
							pstmt8.setNull(2, Types.JAVA_OBJECT);
						}
						if (l_tmp_amount != null) {
							pstmt8.setInt(3, l_tmp_amount);
						}
						else {
							pstmt8.setNull(3, Types.JAVA_OBJECT);
						}
						executeUpdate(pstmt8);
						pstmt8.close();
					}					
					else {						

						PreparedStatement pstmt9 = prepareStatement(
								  "update acct_exp_temp"
								+ " set amount = amount + ?"
								+ " where acct_exp_temp_id = ?");
						
						if (amount_required != null) {
							pstmt9.setDouble(1, amount_required);
						}
						else {
							pstmt9.setNull(1, Types.JAVA_OBJECT);
						}
						if (l_acct_exp_temp_id != null) {
							pstmt9.setInt(2, l_acct_exp_temp_id);
						}
						else {
							pstmt9.setNull(2, Types.JAVA_OBJECT);
						}
						executeUpdate(pstmt9);
						pstmt9.close();						
						

						PreparedStatement pstmt10 = prepareInsert(
								  "insert into acct_trans_aging (acct_trans_id, acct_id, exp_date, amount)"
								+ " values (?, ?, mdy(12, 31, year(current) - 1), ?)");
						if (acct_trans_id_in != null) {
							pstmt10.setInt(1, acct_trans_id_in);
						}
						else {
							pstmt10.setNull(1, Types.JAVA_OBJECT);
						}
						if (acct_id_in != null) {
							pstmt10.setInt(2, acct_id_in);
						}
						else {
							pstmt10.setNull(2, Types.JAVA_OBJECT);
						}
						if (amount_required != null) {
							pstmt10.setDouble(3, amount_required);
						}
						else {
							pstmt10.setNull(3, Types.JAVA_OBJECT);
						}
						executeUpdate(pstmt10);
						pstmt10.close();						
						
						amount_required = 0.0;
					}
				}
				pstmt6.close();
			}			
			
			// lock the acct_exp records for this acct
			//update  acct_exp
			//set     acct_exp.amount  = acct_exp.amount
			//where   acct_id = acct_id;
			
			

			PreparedStatement pstmt11 = prepareStatement(
					  "select exp_date, amount"
					+ " from acct_exp"
					+ " where acct_id = ?"
					+ " and amount > 0.0"
					+ " order by exp_date");
			
			if (acct_id_in != null) {
				pstmt11.setInt(1, acct_id_in);
			}
			else {
				pstmt11.setNull(1, Types.JAVA_OBJECT);
			}
			ResultSet rs4 = executeQuery(pstmt11);
			while (rs4.next()) {
				t_exp_date = rs4.getTimestamp(1);
				t_amount = rs4.getDouble(2);				
				
				if (amount_required.equals(0.0)) {					
					break;
				}				
				
				if (t_amount < amount_required) {					
					amount_required = amount_required - t_amount;					
					

					PreparedStatement pstmt12 = prepareStatement(
							  "update acct_exp"
							+ " set amount = 0.0"
							+ " where acct_id = ?"
							+ " and exp_date = ?");
					
					if (acct_id_in != null) {
						pstmt12.setInt(1, acct_id_in);
					}
					else {
						pstmt12.setNull(1, Types.JAVA_OBJECT);
					}
					if (t_exp_date != null) {
						pstmt12.setObject(2, t_exp_date);
					}
					else {
						pstmt12.setNull(2, Types.JAVA_OBJECT);
					}
					executeUpdate(pstmt12);
					pstmt12.close();					
					

					PreparedStatement pstmt13 = prepareInsert(
							  "insert into acct_trans_aging (acct_trans_id, acct_id, exp_date, amount)"
							+ " values (?, ?, ?, ?)");
					if (acct_trans_id_in != null) {
						pstmt13.setInt(1, acct_trans_id_in);
					}
					else {
						pstmt13.setNull(1, Types.JAVA_OBJECT);
					}
					if (acct_id_in != null) {
						pstmt13.setInt(2, acct_id_in);
					}
					else {
						pstmt13.setNull(2, Types.JAVA_OBJECT);
					}
					if (t_exp_date != null) {
						pstmt13.setObject(3, t_exp_date);
					}
					else {
						pstmt13.setNull(3, Types.JAVA_OBJECT);
					}
					if (t_amount != null) {
						pstmt13.setDouble(4, t_amount);
					}
					else {
						pstmt13.setNull(4, Types.JAVA_OBJECT);
					}
					executeUpdate(pstmt13);
					pstmt13.close();
				}				
				else {					

					PreparedStatement pstmt14 = prepareStatement(
							  "update acct_exp"
							+ " set amount = amount - ?"
							+ " where acct_id = ?"
							+ " and exp_date = ?");
					
					if (amount_required != null) {
						pstmt14.setDouble(1, amount_required);
					}
					else {
						pstmt14.setNull(1, Types.JAVA_OBJECT);
					}
					if (acct_id_in != null) {
						pstmt14.setInt(2, acct_id_in);
					}
					else {
						pstmt14.setNull(2, Types.JAVA_OBJECT);
					}
					if (t_exp_date != null) {
						pstmt14.setObject(3, t_exp_date);
					}
					else {
						pstmt14.setNull(3, Types.JAVA_OBJECT);
					}
					executeUpdate(pstmt14);
					pstmt14.close();					
					

					PreparedStatement pstmt15 = prepareInsert(
							  "insert into acct_trans_aging (acct_trans_id, acct_id, exp_date, amount)"
							+ " values (?, ?, ?, ?)");
					if (acct_trans_id_in != null) {
						pstmt15.setInt(1, acct_trans_id_in);
					}
					else {
						pstmt15.setNull(1, Types.JAVA_OBJECT);
					}
					if (acct_id_in != null) {
						pstmt15.setInt(2, acct_id_in);
					}
					else {
						pstmt15.setNull(2, Types.JAVA_OBJECT);
					}
					if (t_exp_date != null) {
						pstmt15.setObject(3, t_exp_date);
					}
					else {
						pstmt15.setNull(3, Types.JAVA_OBJECT);
					}
					if (amount_required != null) {
						pstmt15.setDouble(4, amount_required);
					}
					else {
						pstmt15.setNull(4, Types.JAVA_OBJECT);
					}
					executeUpdate(pstmt15);
					pstmt15.close();					
					
					amount_required = 0.0;
				}
			}
			pstmt11.close();			
			
			if (!amount_required.equals(0.0)) {				
				throw new ProcedureException(-746, 0, "_upd_acct_exp: Unable to consume value [" + amount_in + "] from account. Balance: " + l_balance);
			}
		}
	}

}