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

public class add_pcall_ticket extends AbstractProcedure {

	public Integer execute(Integer prop_id, String call_status, String severity, Integer std_desc_id, String problem_desc, String assoc_recog_cd, String assoc_recog_id, String recog_id, Integer entry_id, String notes, String contact_title, String contact_frst_name, String contact_last_name, String contact_phone, String contact_fax, String contact_email_addr) throws SQLException, ProcedureException {		
		
		/*
		 * (]$[) $RCSfile$:$Revision: 99 $ | CDATE=$Date: 2012-12-19 14:14:54 -0700 (Wed, 19 Dec 2012) $ ~
		 * 
		 *  Generate one each prop_call and prop_call_log records.
		 * 
		 *  This procedure is a wrapper for _add_pcall_ticket adding only BEGIN and 
		 *  COMMIT/ROLLBACK.
		 *  
		 * 	Copyright (C) 2002 Choice Hotels International, Inc.
		 *                       All Rights Reserved
		 */		
		
		Integer prop_call_id;		
		
		Integer sql_error;		
		Integer isam_error;		
		String error_data;		
		
		try {			
			
			// set debug file to '/tmp/add_call_ticket.trace';
			// trace on;
			
			prop_call_id = null;
			begin();			
			
			prop_call_id = new _add_pcall_ticket().execute(prop_id, call_status, severity, std_desc_id, problem_desc, assoc_recog_cd, assoc_recog_id, recog_id, entry_id, notes, contact_title, contact_frst_name, contact_last_name, contact_phone, contact_fax, contact_email_addr);
			commit();			
			
			// Return success
			return prop_call_id;			
			

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