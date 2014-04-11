/* Generated on 02-25-2013 12:49:24 PM by SPLParser v0.9 */
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

public class expire_acct_value extends AbstractProcedure {

	public Integer execute(Integer acct_id, Timestamp exp_date) throws SQLException, ProcedureException {		
		
		/*
		 * (]$[) $RCSfile$:$Revision: 99 $ | CDATE=$Date: 2012-12-19 14:14:54 -0700 (Wed, 19 Dec 2012) $ ~
		 * 
		 *  Expire value for one account.
		 *  Assumption is that 'BEGIN WORK' is called prior to this
		 *  procedure and 'ROLLBACK/COMMIT WORK' after depending on result. Returns
		 *  points_trans_id on success.
		 * 
		 * 	Copyright (C) 2002 Choice Hotels International, Inc.
		 *                       All Rights Reserved
		 */		
		
		Double amount;		
		Integer acct_trans_id;		
		Integer detail_id;		
		
		amount = null;		
		acct_trans_id = null;		
		detail_id = null;		
		
		// set debug file to '/tmp/expire_acct_value.trace';
		// trace on;
		
		//----------------------------------------------------------
		// determine how many points to expire
		//----------------------------------------------------------
		

		PreparedStatement pstmt1 = prepareStatement(
				  "select sum(acct_exp.amount)"
				+ " from acct_exp"
				+ " where acct_exp.acct_id = ?"
				+ " and acct_exp.exp_date = ?");
		
		if (acct_id != null) {
			pstmt1.setInt(1, acct_id);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		if (exp_date != null) {
			pstmt1.setObject(2, exp_date);
		}
		else {
			pstmt1.setNull(2, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		amount = rs1.getDouble(1);
		pstmt1.close();		
		
		//----------------------------------------------------------
		// if there is nothing to do return now
		//----------------------------------------------------------
		if (amount == null || amount.equals(0.0)) {			
			return 0;
		}		
		
		//----------------------------------------------------------
		// post the transaction
		//----------------------------------------------------------
		amount = amount * -1;		// Invert the value
		acct_trans_id = new _post_trans().execute(acct_id, null, "E", null, null, null, amount, null);		
		detail_id = new _ins_acct_trans_de().execute(acct_trans_id, 1, amount, null, "N");		
		
		// Return success
		return acct_trans_id;
	}

}