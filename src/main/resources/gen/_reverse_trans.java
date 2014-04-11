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

public class _reverse_trans extends AbstractProcedure {

	public Integer execute(Integer acct_trans_id, Integer entry_id, Integer std_desc_id, String notes) throws SQLException, ProcedureException {		
		/*
		 * $Id: _reverse_trans.sql 99 2012-12-19 21:14:54Z michael_giroux $
		 * 
		 *       Copyright (C) 2002 Choice Hotels International, Inc.
		 * 		    All Rights Reserved
		 * 
		 *   Accepts one acct_trans_id, and member call log info. If valid, 
		 *   attempts to reverse the transaction by calling the procedure _rev_trans().
		 * 
		 *   Returns the new transaction ID if successful, an exception if not.
		 * 
		 *   The following transactions may not be reversed:
		 * 
		 *   1. Transactions that have already been reversed.
		 *   2. Transactions associated with an external partner that have been 
		 *      resubmitted, (rsub_acct_trans_id is not null).
		 *   3. Transactions associated with external partners that are at a status
		 *      of 'S'ubmitted, or 'A'pplied.
		 *   4. If transaction is a stay it may not be a redemption stay.
		 *   5. If transaction is a redemption it must not be fulfilled (awarded).
		 */		
		// local working variables
		Integer rev_acct_trans_id;		
		Integer acct_id;		
		Integer ticket_nbr;		
		Double amount;		
		String problem_desc;		
		Integer rev_status;		
		String rev_message;		
		
		// initialize variables
		rev_acct_trans_id = null;		
		acct_id = null;		
		ticket_nbr = null;		
		amount = null;		
		problem_desc = null;		
		rev_status = null;		
		rev_message = null;		
		
		// set debug file to '/tmp/_reverse_trans.trace';
		// trace on;
		
		// First check if it is a candidate for reversal
		rev_status = new get_rev_status().execute(acct_trans_id);		
		if (!rev_status.equals(0)) {			
			rev_message = "Unable to reverse transaction id " + acct_trans_id + ", rev_status=" + rev_status;			
			throw new ProcedureException(-746, 0, rev_message);
		}		
		
		// Get total amount of original transaction
		amount = new get_acct_trans_sum().execute(acct_trans_id);		
		
		// Get the acct_id and transaction type from the original transaction

		PreparedStatement pstmt1 = prepareStatement(
				  "select acct_trans.acct_id, acct_trans.rev_acct_trans_id, \"Reversed transaction. Original transaction (TransID \" || acct_trans.acct_trans_id || \") was a \" || decode(acct_trans.trans_type, \"S\", \"Stay\", \"R\", \"Redemption\", \"A\", \"Adjustment\", \"B\", \"Bonus\", \"E\", \"Expiration\", \"V\", \"Reversal\") || \" transaction posted on \" || acct_trans.init_dtime || \" for \" || ? || \".\""
				+ " from acct_trans"
				+ " where acct_trans.acct_trans_id = ?");
		
		if (amount != null) {
			pstmt1.setDouble(1, amount);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		if (acct_trans_id != null) {
			pstmt1.setInt(2, acct_trans_id);
		}
		else {
			pstmt1.setNull(2, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		acct_id = rs1.getInt(1);
		rev_acct_trans_id = rs1.getInt(2);
		problem_desc = rs1.getString(3);
		pstmt1.close();		
		
		
		// Insert the mbr_call and mbr_call_log records.
		ticket_nbr = new _add_call_ticket().execute(new get_pri_cust_id().execute(acct_id), acct_id, "C", "L", std_desc_id, problem_desc, null, entry_id, notes);		
		
		// Next reverse the transaction itself...
		rev_acct_trans_id = new _rev_trans().execute(acct_trans_id, ticket_nbr, "N");		
		
		return rev_acct_trans_id;
	}

}