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

public class _submit_trans extends AbstractProcedure {

	public void execute(Integer acct_trans_id) throws SQLException, ProcedureException {		
		/*
		 * $RCSfile$ - Set an acct_trans record to submitted status - $Revision: 99 $
		 * 
		 * (]$[) $RCSfile$:$Revision: 99 $ | CDATE=$Date: 2012-12-19 14:14:54 -0700 (Wed, 19 Dec 2012) $ ~
		 * 
		 *       Copyright (C) 2002 Choice Hotels International, Inc.
		 *                       All Rights Reserved
		 * 
		 * This procedure sets the status of an acct_trans record to 'S'ubmitted. If the 
		 * transaction is associated with an external partner the par record is updated
		 * as well.
		 * 
		 * This procedure must be called inside a transaction to ensure data integrity.
		 */		
		
		Integer acct_id;		
		String external_pgm;		
		String trans_status;		
		
		acct_id = null;		
		external_pgm = null;		
		trans_status = null;		
		
		// set debug file to '/tmp/_submit_trans.trace';
		// trace on;
		
		// Lookup the transaction

		PreparedStatement pstmt1 = prepareStatement(
				  "select acct_trans.acct_id, acct_trans.trans_status"
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
		acct_id = rs1.getInt(1);
		trans_status = rs1.getString(2);
		pstmt1.close();		
		
		// Did we find the transaction?
		if (dbinfo("sqlca.sqlerrd2").equals(0)) {			
			throw new ProcedureException(-746, 0, "_submit_trans: Invalid transaction ID.");
		}		
		
		// Determine partner type associated with this transaction.
		external_pgm = new get_pgm_type().execute(acct_id);		
		
		// Check the state
		if (!trans_status.equals("P")) {			
			throw new ProcedureException(-746, 0, "_submit_trans: Transaction must be in the pending state.");
		}		
		
		// Update the transaction

		PreparedStatement pstmt2 = prepareStatement(
				  "update acct_trans"
				+ " set acct_trans.trans_status = \"S\""
				+ " where acct_trans.acct_trans_id = ?");
		
		if (acct_trans_id != null) {
			pstmt2.setInt(1, acct_trans_id);
		}
		else {
			pstmt2.setNull(1, Types.JAVA_OBJECT);
		}
		executeUpdate(pstmt2);
		pstmt2.close();		
		
		// If external partner update the par table
		if (external_pgm.equals("Y")) {			
			new _upd_par().execute(acct_trans_id, "S", null, new Timestamp(System.currentTimeMillis()));
		}
	}

}