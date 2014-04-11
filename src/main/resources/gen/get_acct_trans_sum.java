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

public class get_acct_trans_sum extends AbstractProcedure {

	public Double execute(Integer acct_trans_id) throws SQLException, ProcedureException {		
		/*
		 * $RCSfile$ - Get the sum of an account transaction - $Revision: 99 $
		 * 
		 * (]$[) $RCSfile$:$Revision: 99 $ | CDATE=$Date: 2012-12-19 14:14:54 -0700 (Wed, 19 Dec 2012) $ ~
		 * 
		 *       Copyright (C) 2000 Choice Hotels International, Inc.
		 * 		    All Rights Reserved
		 * 
		 *   This procedure sums the detail records for one account transaction and 
		 *   returns the amount.
		 */		
		Double amount;		
		
		amount = 0.0;		
		
		// set debug file to '/tmp/get_acct_trans_sum.trace';
		// trace on;
		
		// Sum the transaction details to get total amount of transaction

		PreparedStatement pstmt1 = prepareStatement(
				  "select sum(acct_trans_detail.amount)"
				+ " from acct_trans_detail"
				+ " where acct_trans_detail.acct_trans_id = ?");
		
		if (acct_trans_id != null) {
			pstmt1.setInt(1, acct_trans_id);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		amount = rs1.getDouble(1);
		pstmt1.close();		
		
		return amount;
	}

}