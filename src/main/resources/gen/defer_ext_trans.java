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

public class defer_ext_trans extends AbstractProcedure {

	public Integer execute(Integer acct_trans_id, Integer std_desc_id, Integer entry_id) throws SQLException, ProcedureException {		
		
		/*
		 * (]$[) $RCSfile$:$Revision: 99 $ | CDATE=$Date: 2012-12-19 14:14:54 -0700 (Wed, 19 Dec 2012) $ ~
		 * 
		 *   Update the par record with a std_desc_id to indicate a deferred status.
		 * 
		 * 	Copyright (C) 2002 Choice Hotels International, Inc.
		 *                       All Rights Reserved
		 */		
		
		Integer par_id;		
		String request_status;		
		Integer par_tran_id;		
		
		Integer sql_error;		
		Integer isam_error;		
		String error_data;		
		String error_msg;		
		
		par_id = null;		
		request_status = null;		
		par_tran_id = null;		
		
		// Initialize defined variables to null
		
		// set debug file to '/tmp/defer_ext_trans.trace';
		// trace on;
		
		//----------------------------------------------------------
		// First determine that the member is at an 'A'ctive status
		//----------------------------------------------------------

		PreparedStatement pstmt1 = prepareStatement(
				  "select p.par_id, p.request_status, p.acct_trans_id"
				+ " from par p"
				+ " where p.acct_trans_id = ?");
		
		if (acct_trans_id != null) {
			pstmt1.setInt(1, acct_trans_id);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		par_id = rs1.getInt(1);
		request_status = rs1.getString(2);
		par_tran_id = rs1.getInt(3);
		pstmt1.close();		
		
		if (par_id == null) {			
			throw new ProcedureException(-746, 0, "defer_ext_trans: Par record not found.");
		}		
		
		if (!request_status.equals("R")) {			
			throw new ProcedureException(-746, 0, "defer_ext_trans: Par record is not 'R'ejected.");
		}		
		

		PreparedStatement pstmt2 = prepareStatement(
				  "update par"
				+ " set par.std_desc_id = ?, par.last_update_id = ?, par.last_update_dtime = current"
				+ " where par.acct_trans_id = ?");
		
		if (std_desc_id != null) {
			pstmt2.setInt(1, std_desc_id);
		}
		else {
			pstmt2.setNull(1, Types.JAVA_OBJECT);
		}
		if (entry_id != null) {
			pstmt2.setInt(2, entry_id);
		}
		else {
			pstmt2.setNull(2, Types.JAVA_OBJECT);
		}
		if (acct_trans_id != null) {
			pstmt2.setInt(3, acct_trans_id);
		}
		else {
			pstmt2.setNull(3, Types.JAVA_OBJECT);
		}
		executeUpdate(pstmt2);
		pstmt2.close();		
		
		return par_id;
	}

}