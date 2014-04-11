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

public class cust_has_cc extends AbstractProcedure {

	public String execute(Integer cust_id, String cc_cd) throws SQLException, ProcedureException {		
		
		Integer cc_count;		
		
		/*
		 * $RCSfile$ - does a member have the specified credit card - $Revision: 99 $
		 * 
		 * (]$[) $RCSfile$:$Revision: 99 $ | CDATE=$Date: 2012-12-19 14:14:54 -0700 (Wed, 19 Dec 2012) $ ~
		 * 
		 *       Copyright (C) 2000 Choice Hotels International, Inc.
		 *                       All Rights Reserved
		 */		
		
		cc_count = 0;		
		

		PreparedStatement pstmt1 = prepareStatement(
				  "select count(*)"
				+ " from cust_cc"
				+ " where cust_cc.cust_id = ?"
				+ " and cust_cc.cc_cd = ?"
				+ " and cust_cc.cc_encrypted_id is not null");
		
		if (cust_id != null) {
			pstmt1.setInt(1, cust_id);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		if (cc_cd != null) {
			pstmt1.setString(2, cc_cd);
		}
		else {
			pstmt1.setNull(2, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		cc_count = rs1.getInt(1);
		pstmt1.close();		
		
		if (cc_count.equals(0)) {			
			return "F";
		}		
		else {			
			return "T";
		}
	}

}