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

public class _reset_par extends AbstractProcedure {

	public void execute(Integer par_id, Integer entry_id) throws SQLException, ProcedureException {		
		
		/*
		 * $RCSfile$- Update a par record - $Revision: 99 $
		 * 
		 * (]$[) $RCSfile$:$Revision: 99 $ | CDATE=$Date: 2012-12-19 14:14:54 -0700 (Wed, 19 Dec 2012) $ ~
		 * 
		 *       Copyright (C) 2003 Choice Hotels International, Inc.
		 *                       All Rights Reserved
		 * 
		 * This procedure resets the status of a partner activity request (par) record to 
		 * 'P'ending.  All transmission timestamps are set to null.
		 * 
		 * This procedure does its work inside a transaction to ensure data integrity.
		 */		
		
		Integer my_par_id;		
		
		my_par_id = null;		
		
		//set debug file to '/tmp/_reset_par.trace';
		//trace on;
		
		// Check that the par record exists.

		PreparedStatement pstmt1 = prepareStatement(
				  "select p.par_id"
				+ " from par p"
				+ " where p.par_id = ?");
		
		if (par_id != null) {
			pstmt1.setInt(1, par_id);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		my_par_id = rs1.getInt(1);
		pstmt1.close();		
		
		if (my_par_id == null) {			
			throw new ProcedureException(-746, 0, "_reset_par: No par record to update.");
		}		
		

		PreparedStatement pstmt2 = prepareStatement(
				  "update par"
				+ " set par.rcv_dtime = null, par.xmit_dtime = null, par.par_result_msg_id = null, par.last_update_dtime = current, par.last_update_id = ?, par.request_status = \"P\""
				+ " where par.par_id = ?");
		
		if (entry_id != null) {
			pstmt2.setInt(1, entry_id);
		}
		else {
			pstmt2.setNull(1, Types.JAVA_OBJECT);
		}
		if (par_id != null) {
			pstmt2.setInt(2, par_id);
		}
		else {
			pstmt2.setNull(2, Types.JAVA_OBJECT);
		}
		executeUpdate(pstmt2);
		pstmt2.close();
	}

}