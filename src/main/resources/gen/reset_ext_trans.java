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

public class reset_ext_trans extends AbstractProcedure {

	public Collection<Object> execute(Integer par_id, Integer entry_id) throws SQLException, ProcedureException {		
		
		/*
		 * (]$[) $RCSfile$:$Revision: 99 $ | CDATE=$Date: 2012-12-19 14:14:54 -0700 (Wed, 19 Dec 2012) $ ~
		 * 
		 *   Reset a partner activity request to 'P'ending.
		 * 
		 * 	Copyright (C) 2003 Choice Hotels International, Inc.
		 *                       All Rights Reserved
		 */		
		
		Integer error_flag;		
		
		
		Integer sql_error;		
		Integer isam_error;		
		String error_data;		
		String error_msg;		
		
		try {
			begin();			
			
			// Initialize defined variables to null
			error_flag = 1;			// default to success, 0 = failure
			
			sql_error = null;			
			isam_error = null;			
			error_data = null;			// no error message
			error_msg = null;			
			
			// set debug file to '/tmp/reset_ext_trans.trace';
			// trace on;
			
			// update the par table.
			new _reset_par().execute(par_id, entry_id);
			commit();			
			
			return new ArrayList<Object>(Arrays.<Object>asList(error_flag, error_msg));			
			

		}
		catch (SQLException e) {
			sql_error = e.getErrorCode();
			isam_error = 0;
			error_data = e.getMessage();
			{
				rollback();				
				error_flag = 0;				// fail the attempt
				error_msg = "SQL: " + sql_error + " ISAM: " + isam_error + " MSG: " + error_data;
			}
		}
	}

}