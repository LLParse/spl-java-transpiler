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

public class _dequeue_acct extends AbstractProcedure {

	public Integer execute(Integer acct_id, Integer cust_call_id, Integer entry_id) throws SQLException, ProcedureException {		
		/*
		 * $RCSfile$ - Removes all pending par records for an account - $Revision: 99 $
		 * 
		 * (]$[) $RCSfile$:$Revision: 99 $ | CDATE=$Date: 2012-12-19 14:14:54 -0700 (Wed, 19 Dec 2012) $ ~
		 * 
		 *       Copyright (C) 2001 Choice Hotels International, Inc.
		 *                       All Rights Reserved
		 * 
		 * This procedure removes all pending par records for a given account and
		 * sets their associated transactions to a 'R'ejected status.
		 */		
		
		Integer delete_count;		
		Integer acct_trans_id;		
		
		delete_count = 0;		
		acct_trans_id = null;		
		
		

		PreparedStatement pstmt1 = prepareStatement(
				  "select acct_trans.acct_trans_id"
				+ " from acct_trans, par"
				+ " where acct_trans.acct_id = ?"
				+ " and acct_trans.trans_status = \"P\""
				+ " and acct_trans.acct_trans_id = par.acct_trans_id");
		
		if (acct_id != null) {
			pstmt1.setInt(1, acct_id);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		while (rs1.next()) {
			acct_trans_id = rs1.getInt(1);			
			
			// Remove from queue
			new _del_par().execute(acct_trans_id);			
			
			// Reject the transaction

			PreparedStatement pstmt2 = prepareStatement(
					  "update acct_trans"
					+ " set acct_trans.trans_status = \"R\""
					+ " where acct_trans.acct_trans_id = ?");
			
			if (acct_trans_id != null) {
				pstmt2.setInt(1, acct_trans_id);
			}
			else {
				pstmt2.setNull(1, Types.JAVA_OBJECT);
			}
			executeUpdate(pstmt2);
			pstmt2.close();			
			
			delete_count = delete_count + 1;
		}
		pstmt1.close();		
		
		return delete_count;
	}

}