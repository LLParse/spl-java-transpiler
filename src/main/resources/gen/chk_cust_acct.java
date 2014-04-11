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

public class chk_cust_acct extends AbstractProcedure {

	public String execute(Integer acct_id, Integer cust_id) throws SQLException, ProcedureException {		
		/*
		 * chk_cust_acct.sql - Validate customer account relationship 1.2
		 * 
		 * (]$[) $RCSfile$:$Revision: 99 $ | CDATE=$Date: 2012-12-19 14:14:54 -0700 (Wed, 19 Dec 2012) $ ~
		 * 
		 *       Copyright (C) 2002 Choice Hotels International, Inc.
		 *                       All Rights Reserved
		 * 
		 * This procedure validates customer to account by checking for existence of 
		 * a cust_acct record and whether the cust_acct link is 'A'ctive.
		 * Returns 'Y' if valid, 'N' if not.
		 */		
		
		String active;		
		
		active = null;		
		
		// Determine partner type associated with this account.

		PreparedStatement pstmt1 = prepareStatement(
				  "select cust_acct.active"
				+ " from cust_acct"
				+ " where cust_acct.acct_id = ?"
				+ " and cust_acct.cust_id = ?");
		
		if (acct_id != null) {
			pstmt1.setInt(1, acct_id);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		if (cust_id != null) {
			pstmt1.setInt(2, cust_id);
		}
		else {
			pstmt1.setNull(2, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		active = rs1.getString(1);
		pstmt1.close();		
		
		if (active == null) {			
			return "N";
		}		
		
		return active;
	}

}