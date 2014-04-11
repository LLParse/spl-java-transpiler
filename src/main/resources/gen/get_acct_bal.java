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

public class get_acct_bal extends AbstractProcedure {

	public Integer execute(Integer acct_id) throws SQLException, ProcedureException {		
		/*
		 * $RCSfile$ - Get the balance of an account - $Revision: 99 $
		 * 
		 * (]$[) $RCSfile$:$Revision: 99 $ | CDATE=$Date: 2012-12-19 14:14:54 -0700 (Wed, 19 Dec 2012) $ ~
		 * 
		 *       Copyright (C) 2002 Choice Hotels International, Inc.
		 * 		    All Rights Reserved
		 * 
		 *   Retrieve the account balance for a given account.
		 */		
		Integer last_stmnt_id;		
		Integer balance;		
		
		last_stmnt_id = null;		
		balance = null;		
		
		// set debug file to '/tmp/get_acct_bal.trace';
		// trace on;
		

		PreparedStatement pstmt1 = prepareStatement(
				  "select max(s.stmnt_id)"
				+ " from acct_stmnt s"
				+ " where s.acct_id = ?");
		
		if (acct_id != null) {
			pstmt1.setInt(1, acct_id);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		last_stmnt_id = rs1.getInt(1);
		pstmt1.close();		
		
		if (last_stmnt_id == null) {			
			return 0;
		}		
		

		PreparedStatement pstmt2 = prepareStatement(
				  "select s.end_balance"
				+ " from acct_stmnt s"
				+ " where s.acct_id = ?"
				+ " and s.stmnt_id = ?");
		
		if (acct_id != null) {
			pstmt2.setInt(1, acct_id);
		}
		else {
			pstmt2.setNull(1, Types.JAVA_OBJECT);
		}
		if (last_stmnt_id != null) {
			pstmt2.setInt(2, last_stmnt_id);
		}
		else {
			pstmt2.setNull(2, Types.JAVA_OBJECT);
		}
		ResultSet rs2 = executeQuery(pstmt2);
		rs2.next();
		balance = rs2.getInt(1);
		pstmt2.close();		
		
		if (balance == null) {			
			return 0;
		}		
		
		return balance;
	}

}