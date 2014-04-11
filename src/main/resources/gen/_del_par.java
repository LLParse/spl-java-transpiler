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

public class _del_par extends AbstractProcedure {

	public void execute(Integer acct_trans_id) throws SQLException, ProcedureException {		
		/*
		 * $RCSfile$ - Delete one par record - $Revision: 99 $
		 * 
		 * (]$[) $RCSfile$:$Revision: 99 $ | CDATE=$Date: 2012-12-19 14:14:54 -0700 (Wed, 19 Dec 2012) $ ~
		 * 
		 *       Copyright (C) 2001 Choice Hotels International, Inc.
		 *                       All Rights Reserved
		 * 
		 * This procedure deletes one partner activity request (par) record.
		 * The record must not be at a status of  'S'ubmitted.
		 * 
		 * This procedure must be called inside a transaction to ensure data integrity.
		 */		
		
		String request_status;		
		request_status = null;		
		
		//set debug file to '/tmp/_del_par.trace';
		//trace on;
		
		// First retrieve and check the request status

		PreparedStatement pstmt1 = prepareStatement(
				  "select par.request_status"
				+ " from par"
				+ " where par.acct_trans_id = ?");
		
		if (acct_trans_id != null) {
			pstmt1.setInt(1, acct_trans_id);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		request_status = rs1.getString(1);
		pstmt1.close();		
		
		if (request_status == null) {			
			throw new ProcedureException(-746, 0, "_del_par: Unable to delete, transaction not found in queue.");
		}		
		
		if (request_status.equals("S")) {			
			throw new ProcedureException(-746, 0, "_del_par: Unable to delete, transaction has been submitted.");
		}		
		
		// Delete it

		PreparedStatement pstmt2 = prepareStatement(
				  "delete from par"
				+ " where par.acct_trans_id = ?");
		if (acct_trans_id != null) {
			pstmt2.setInt(1, acct_trans_id);
		}
		else {
			pstmt2.setNull(1, Types.JAVA_OBJECT);
		}
		executeUpdate(pstmt2);
		pstmt2.close();
	}

}