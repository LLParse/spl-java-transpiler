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

public class reverse_trans extends AbstractProcedure {

	public Integer execute(Integer acct_trans_id, Integer entry_id, Integer std_desc_id, String notes) throws SQLException, ProcedureException {		
		/*
		 * (]$[) $RCSfile$:$Revision: 99 $ | CDATE=$Date: 2012-12-19 14:14:54 -0700 (Wed, 19 Dec 2012) $ ~
		 * 
		 *       Copyright (C) 2002 Choice Hotels International, Inc.
		 * 		    All Rights Reserved
		 * 
		 *   Accepts one acct_trans_id, and member call log info. If valid,
		 *   attempts to reverse the transaction by calling the procedure
		 *   _reverse_trans(). If an associated transaction exists, it is
		 *   reversed as well.
		 * 
		 *   Returns the transaction ID of the reversing transaction if successful,
		 *   and an exception if not.
		 */		
		// local working variables
		Integer rev_acct_trans_id;		
		Integer asc_acct_trans_id;		
		
		Integer sql_error;		
		Integer isam_error;		
		String error_data;		
		
		try {
			begin();			
			
			// initialize variables
			rev_acct_trans_id = null;			
			asc_acct_trans_id = null;			
			
			// set debug file to '/tmp/reverse_trans.trace';
			// trace on;
			
			// Get the associated transaction, if any

			PreparedStatement pstmt1 = prepareStatement(
					  "select acct_trans.asc_acct_trans_id"
					+ " from acct_trans"
					+ " where acct_trans.acct_trans_id = ?");
			
			if (acct_trans_id != null) {
				pstmt1.setInt(1, acct_trans_id);
			}
			else {
				pstmt1.setNull(1, Types.JAVA_OBJECT);
			}
			ResultSet rs1 = executeQuery(pstmt1);
			rs1.next();
			asc_acct_trans_id = rs1.getInt(1);
			pstmt1.close();			
			
			// If an associated transaction exists, reverse it first
			if (asc_acct_trans_id != null) {				
				rev_acct_trans_id = new _reverse_trans().execute(asc_acct_trans_id, entry_id, std_desc_id, notes);				
				
				rev_acct_trans_id = null;
			}			
			
			// Reverse the transaction
			rev_acct_trans_id = new _reverse_trans().execute(acct_trans_id, entry_id, std_desc_id, notes);
			commit();			
			
			return rev_acct_trans_id;			
			

		}
		catch (SQLException e) {
			sql_error = e.getErrorCode();
			isam_error = 0;
			error_data = e.getMessage();
			{
				rollback();				
				throw new ProcedureException(sql_error, isam_error, error_data);
			}
		}
	}

}