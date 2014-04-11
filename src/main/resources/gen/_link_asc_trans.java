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

public class _link_asc_trans extends AbstractProcedure {

	public void execute(Integer f_acct_trans_id, Integer s_acct_trans_id) throws SQLException, ProcedureException {		
		
		/*
		 * (]$[) $RCSfile$:$Revision: 99 $ | CDATE=$Date: 2012-12-19 14:14:54 -0700 (Wed, 19 Dec 2012) $ ~
		 * 
		 *       Copyright (C) 2002 Choice Hotels International, Inc.
		 * 		    All Rights Reserved
		 * 
		 *   Link two transactions together.
		 */		
		
		Integer asc_acct_trans_id;		
		
		//set debug file to '/tmp/_link_asc_trans.trace';
		//trace on;
		
		//
		// Check the first transaction to make sure it hasn't already been linked.
		//
		asc_acct_trans_id = null;		
		

		PreparedStatement pstmt1 = prepareStatement(
				  "select acct_trans.asc_acct_trans_id"
				+ " from acct_trans"
				+ " where acct_trans.acct_trans_id = ?");
		
		if (f_acct_trans_id != null) {
			pstmt1.setInt(1, f_acct_trans_id);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		asc_acct_trans_id = rs1.getInt(1);
		pstmt1.close();		
		
		if (asc_acct_trans_id != null) {			
			if (!asc_acct_trans_id.equals(s_acct_trans_id)) {				
				throw new ProcedureException(-746, 0, "_link_asc_trans: transaction already linked");
			}
		}		
		
		//
		// Check the second transaction to make sure it hasn't already been linked.
		//
		asc_acct_trans_id = null;		
		

		PreparedStatement pstmt2 = prepareStatement(
				  "select acct_trans.asc_acct_trans_id"
				+ " from acct_trans"
				+ " where acct_trans.acct_trans_id = ?");
		
		if (s_acct_trans_id != null) {
			pstmt2.setInt(1, s_acct_trans_id);
		}
		else {
			pstmt2.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs2 = executeQuery(pstmt2);
		rs2.next();
		asc_acct_trans_id = rs2.getInt(1);
		pstmt2.close();		
		
		if (asc_acct_trans_id != null) {			
			if (!asc_acct_trans_id.equals(f_acct_trans_id)) {				
				throw new ProcedureException(-746, 0, "_link_asc_trans: transaction already linked");
			}
		}		
		
		//
		// Upate the transaction linking the first transaction to the second.
		//

		PreparedStatement pstmt3 = prepareStatement(
				  "update acct_trans"
				+ " set acct_trans.asc_acct_trans_id = ?"
				+ " where acct_trans.acct_trans_id = ?");
		
		if (s_acct_trans_id != null) {
			pstmt3.setInt(1, s_acct_trans_id);
		}
		else {
			pstmt3.setNull(1, Types.JAVA_OBJECT);
		}
		if (f_acct_trans_id != null) {
			pstmt3.setInt(2, f_acct_trans_id);
		}
		else {
			pstmt3.setNull(2, Types.JAVA_OBJECT);
		}
		executeUpdate(pstmt3);
		pstmt3.close();		
		
		//
		// Upate the transaction linking the second transaction to the first.
		//

		PreparedStatement pstmt4 = prepareStatement(
				  "update acct_trans"
				+ " set acct_trans.asc_acct_trans_id = ?"
				+ " where acct_trans.acct_trans_id = ?");
		
		if (f_acct_trans_id != null) {
			pstmt4.setInt(1, f_acct_trans_id);
		}
		else {
			pstmt4.setNull(1, Types.JAVA_OBJECT);
		}
		if (s_acct_trans_id != null) {
			pstmt4.setInt(2, s_acct_trans_id);
		}
		else {
			pstmt4.setNull(2, Types.JAVA_OBJECT);
		}
		executeUpdate(pstmt4);
		pstmt4.close();
	}

}