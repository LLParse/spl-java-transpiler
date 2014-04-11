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

public class reject_ext_trans extends AbstractProcedure {

	public Collection<Object> execute(Integer par_id, Integer par_result_msg_id, Timestamp rej_dtime) throws SQLException, ProcedureException {		
		
		/*
		 * (]$[) $RCSfile$:$Revision: 99 $ | CDATE=$Date: 2012-12-19 14:14:54 -0700 (Wed, 19 Dec 2012) $ ~
		 * 
		 *   Update the par and acct_trans tables to note that the transaction
		 *   has been rejected to an external partner.
		 * 
		 * 	Copyright (C) 2002 Choice Hotels International, Inc.
		 *                       All Rights Reserved
		 */		
		
		Integer acct_id;		
		Integer acct_trans_id;		
		String acct_status;		
		Integer error_flag;		
		
		
		Integer sql_error;		
		Integer isam_error;		
		String error_data;		
		String error_msg;		
		
		try {			
			
			// Initialize defined variables to null
			acct_id = null;			
			acct_trans_id = null;			
			acct_status = null;			
			error_flag = 1;			// default to success, 0 = failure
			
			sql_error = null;			
			isam_error = null;			
			error_data = null;			// no error message
			error_msg = null;			
			
			// set debug file to '/tmp/reject_ext_trans.trace';
			// trace on;
			
			//----------------------------------------------------------
			// First determine that the member is at an 'A'ctive status
			//----------------------------------------------------------

			PreparedStatement pstmt1 = prepareStatement(
					  "select a.acct_id, a.acct_trans_id"
					+ " from acct_trans a, par p"
					+ " where a.acct_trans_id = p.acct_trans_id"
					+ " and p.par_id = ?");
			
			if (par_id != null) {
				pstmt1.setInt(1, par_id);
			}
			else {
				pstmt1.setNull(1, Types.JAVA_OBJECT);
			}
			ResultSet rs1 = executeQuery(pstmt1);
			rs1.next();
			acct_id = rs1.getInt(1);
			acct_trans_id = rs1.getInt(2);
			pstmt1.close();			
			
			if (acct_id == null) {				
				throw new ProcedureException(-746, 0, "reject_ext_trans: Account not found.");
			}			
			
			// Next check the date, if none given use current
			if (rej_dtime == null) {				
				rej_dtime = new Timestamp(System.currentTimeMillis());
			}			
			
			new _reject_trans().execute(acct_trans_id, par_result_msg_id, rej_dtime);			
			
			return new ArrayList<Object>(Arrays.<Object>asList(error_flag, error_msg));			
			

		}
		catch (SQLException e) {
			sql_error = e.getErrorCode();
			isam_error = 0;
			error_data = e.getMessage();
			{				
				error_flag = 0;				// fail the attempt
				error_msg = "SQL: " + sql_error + " ISAM: " + isam_error + " MSG: " + error_data;
			}			
			return new ArrayList<Object>(Arrays.<Object>asList(error_flag, error_msg));
		}
	}

}