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

public class get_linked_acct extends AbstractProcedure {

	public Integer execute(String recog_cd, String recog_id) throws SQLException, ProcedureException {		
		/*
		 * $Revision: 99 $ - Checks program type associated with an account - $RCSfile$
		 * 
		 *   (]$[) $RCSfile$:$Revision: 99 $ | CDATE=$Date: 2012-12-19 14:14:54 -0700 (Wed, 19 Dec 2012) $ ~
		 * 
		 *       Copyright (C) 2004 Choice Hotels International, Inc.
		 *                       All Rights Reserved
		 * 
		 * Returns the acct_id at the head of a string of linked accts. Null
		 * if no acct exists at all.
		 */		
		
		Integer head_acct_id;		
		Integer this_acct_id;		
		Integer linked_acct_id;		
		String acct_status;		
		
		//set debug file to '/tmp/get_linked_acct.trace';
		//trace on;
		
		head_acct_id = null;		
		this_acct_id = null;		
		linked_acct_id = null;		
		acct_status = null;		
		

		PreparedStatement pstmt1 = prepareStatement(
				  "select acct.acct_id, acct.linked_acct_id, acct.acct_status"
				+ " from acct"
				+ " where acct.recog_cd = ?"
				+ " and acct.recog_id = ?");
		
		if (recog_cd != null) {
			pstmt1.setString(1, recog_cd);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		if (recog_id != null) {
			pstmt1.setString(2, recog_id);
		}
		else {
			pstmt1.setNull(2, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		head_acct_id = rs1.getInt(1);
		linked_acct_id = rs1.getInt(2);
		acct_status = rs1.getString(3);
		pstmt1.close();		
		
		while (linked_acct_id != null && !acct_status.equals("A")) {			

			PreparedStatement pstmt2 = prepareStatement(
					  "select acct.acct_id, acct.linked_acct_id, acct.acct_status"
					+ " from acct"
					+ " where acct.acct_id = ?");
			
			if (linked_acct_id != null) {
				pstmt2.setInt(1, linked_acct_id);
			}
			else {
				pstmt2.setNull(1, Types.JAVA_OBJECT);
			}
			ResultSet rs2 = executeQuery(pstmt2);
			rs2.next();
			this_acct_id = rs2.getInt(1);
			linked_acct_id = rs2.getInt(2);
			acct_status = rs2.getString(3);
			pstmt2.close();			
			
			if (linked_acct_id == null && acct_status.equals("A")) {				
				head_acct_id = this_acct_id;
			}
		}		
		
		return head_acct_id;
	}

}