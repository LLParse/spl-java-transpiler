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

public class pf_wi09airregister extends AbstractProcedure {

	public void execute(Integer in_acct_id, String offer_cd, String in_recog_cd, String in_recog_id) throws SQLException, ProcedureException {		
		
		/*
		 * pf_wi09register.sql - Register the input airline account for the input Winter 2009 offer code passed.
		 *                       If other accounts for this customer are not registered, then register them also.
		 *                       If other accounts are already registered, then this account must have been created
		 *                       after the other accounts were registered, so we are just bringing this account
		 *                       in line with the other registered accounts.
		 *                       
		 *                       May want to put this code back in pf_wi09air.sql .....
		 * 
		 * $Id: pf_wi09airregister.sql 99 2012-12-19 21:14:54Z michael_giroux $
		 *     
		 *     Copyright (C) 2009 Choice Hotels International, Inc.
		 */		
		
		Integer l_offer_id;		
		Integer l_accounts_count;		
		Integer l_acct_offer_count;		
		String l_recog_cd;		
		String l_recog_id;		
		
		Integer sql_error;		
		Integer isam_error;		
		String error_data;		
		
		try {			
			
			l_offer_id = 0;			
			l_accounts_count = 0;			
			l_acct_offer_count = 0;			
			l_recog_cd = null;			
			l_recog_id = null;			
			
			// set debug file to '/tmp/pf_wi09airregister.trace';
			// trace on;
			
			// get a count of other accounts for this customer. Should always be at least
			// two? this account and a GP account....??

			PreparedStatement pstmt1 = prepareStatement(
					  "select count(*)"
					+ " from cust_acct c1"
					+ " where c1.cust_id in ("
						+ "select c2.cust_id"
						+ " from cust_acct c2"
						+ " where c2.acct_id = ?"
					+ ")");
			
			if (in_acct_id != null) {
				pstmt1.setInt(1, in_acct_id);
			}
			else {
				pstmt1.setNull(1, Types.JAVA_OBJECT);
			}
			ResultSet rs1 = executeQuery(pstmt1);
			rs1.next();
			l_accounts_count = rs1.getInt(1);
			pstmt1.close();			
			
			// see if there are any other accounts registered for this offer

			PreparedStatement pstmt2 = prepareStatement(
					  "select count(*)"
					+ " from acct_offer ao"
					+ " where ao.acct_id in ("
						+ "select c1.acct_id"
						+ " from cust_acct c1"
						+ " where c1.cust_id in ("
							+ "select c2.cust_id"
							+ " from cust_acct c2"
							+ " where c2.acct_id = ?"
						+ ")"
					+ ")"
					+ " and ao.offer_id in ("
						+ "select o.offer_id"
						+ " from offer o"
						+ " where o.offer_cd in (\"WI09EXST\", \"WI09NEW\")"
					+ ")");
			
			if (in_acct_id != null) {
				pstmt2.setInt(1, in_acct_id);
			}
			else {
				pstmt2.setNull(1, Types.JAVA_OBJECT);
			}
			ResultSet rs2 = executeQuery(pstmt2);
			rs2.next();
			l_acct_offer_count = rs2.getInt(1);
			pstmt2.close();			
			
			// add this account to the acct_offer table
			new add_acct_offer().execute(l_recog_cd, offer_cd, l_recog_id);			
			
			if (l_acct_offer_count > 0) {				
				return;
			}			
			
			// register those other accounts
			

			PreparedStatement pstmt3 = prepareStatement(
					  "select a.recog_cd, a.recog_id"
					+ " from acct a"
					+ " where a.acct_id in ("
						+ "select ca.acct_id"
						+ " from cust_acct ca"
						+ " where ca.cust_id in ("
							+ "select caa.cust_id"
							+ " from cust_acct caa"
							+ " where caa.acct_id = ?"
						+ ")"
					+ ")");
			
			if (in_acct_id != null) {
				pstmt3.setInt(1, in_acct_id);
			}
			else {
				pstmt3.setNull(1, Types.JAVA_OBJECT);
			}
			ResultSet rs3 = executeQuery(pstmt3);
			while (rs3.next()) {
				l_recog_cd = rs3.getString(1);
				l_recog_id = rs3.getString(2);				
				
				new add_acct_offer().execute(l_recog_cd, "WI09AIR", l_recog_id);
			}
			pstmt3.close();			
			

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