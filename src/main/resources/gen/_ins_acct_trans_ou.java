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

public class _ins_acct_trans_ou extends AbstractProcedure {

	public void execute(Integer acct_trans_id, Integer acct_id, String trans_type, Double amount) throws SQLException, ProcedureException {		
		
		/*
		 * $RCSfile$ - Add an acct_trans_out record for an account to submit to CRS - $Revision: 99 $
		 * 
		 * (]$[) $RCSfile$:$Revision: 99 $ | CDATE=$Date: 2012-12-19 14:14:54 -0700 (Wed, 19 Dec 2012) $ ~
		 * 
		 *       Copyright (C) 2001 Choice Hotels International, Inc.
		 *                       All Rights Reserved
		 * 
		 * This procedure adds one acct_trans_out record for an account. To queue for 
		 * transmission to the CRS
		 */		
		
		//set debug file to '/tmp/_ins_acct_trans_ou.trace';
		//trace on;
		

		PreparedStatement pstmt1 = prepareInsert(
				  "insert into acct_trans_out (acct_trans_id, acct_id, final_dtime, trans_type, amount, retry_count)"
				+ " values (?, ?, current, ?, ?, 0)");
		if (acct_trans_id != null) {
			pstmt1.setInt(1, acct_trans_id);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		if (acct_id != null) {
			pstmt1.setInt(2, acct_id);
		}
		else {
			pstmt1.setNull(2, Types.JAVA_OBJECT);
		}
		if (trans_type != null) {
			pstmt1.setString(3, trans_type);
		}
		else {
			pstmt1.setNull(3, Types.JAVA_OBJECT);
		}
		if (amount != null) {
			pstmt1.setDouble(4, amount);
		}
		else {
			pstmt1.setNull(4, Types.JAVA_OBJECT);
		}
		executeUpdate(pstmt1);
		pstmt1.close();
	}

}