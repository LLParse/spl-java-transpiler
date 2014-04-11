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

public class pf_isenrollstay_v2 extends AbstractProcedure {

	public String execute(Integer in_acct_id, Timestamp in_doa, Timestamp in_dod) throws SQLException, ProcedureException {		
		
		/*
		 * pf_isenrollstay_v2.sql - Version 2 of checking if stay is an enrollment stay. Added criteria
		 *                          are: signup date is any time during the stay, not just on the DOA and
		 *                          appl_source for the account is H for a hotel signup.
		 *    
		 * $Id: pf_isenrollstay_v2.sql 99 2012-12-19 21:14:54Z michael_giroux $
		 * 
		 *       Copyright (C) 2011 Choice Hotels International, Inc.
		 */		
		Timestamp l_signup_date;		
		String l_answer;		
		String l_appl_source;		
		String l_debug;		
		
		Integer sql_error;		
		Integer isam_error;		
		String error_data;		
		
		try {			
			
			l_signup_date = null;			
			l_answer = "F";			
			l_appl_source = null;			
			l_debug = "F";			
			
			// set up tracing based on app_config entry
			l_debug = new settrace().execute("pf_isenrollstay_v2");			
			if (l_debug.equals("T")) {				
				setDebugFile("/tmp/pf_isenrollstay_v2.trace");				
				trace("on");
			}			
			
			// Get sign up date and signup location for the account

			PreparedStatement pstmt1 = prepareStatement(
					  "select a.signup_date, a.appl_source"
					+ " from acct a"
					+ " where a.acct_id = ?");
			
			if (in_acct_id != null) {
				pstmt1.setInt(1, in_acct_id);
			}
			else {
				pstmt1.setNull(1, Types.JAVA_OBJECT);
			}
			ResultSet rs1 = executeQuery(pstmt1);
			rs1.next();
			l_signup_date = rs1.getTimestamp(1);
			l_appl_source = rs1.getString(2);
			pstmt1.close();			
			
			// if hotel signed up account on DOA, then is enrollment stay
			if ((l_signup_date >= in_doa && l_signup_date <= in_dod) && l_appl_source != null && l_appl_source.equals("H")) {				
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