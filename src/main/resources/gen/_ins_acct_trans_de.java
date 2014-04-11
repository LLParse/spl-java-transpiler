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

public class _ins_acct_trans_de extends AbstractProcedure {

	public Integer execute(Integer acct_trans_id, Integer ord, Double amount, Integer promo_id, String variance) throws SQLException, ProcedureException {		
		
		/*
		 * $RCSfile$ - Insert a valid acct_trans record - $Revision: 99 $
		 * 
		 * (]$[) $RCSfile$:$Revision: 99 $ | CDATE=$Date: 2012-12-19 14:14:54 -0700 (Wed, 19 Dec 2012) $ ~
		 * 
		 *       Copyright (C) 2001 Choice Hotels International, Inc.
		 *                       All Rights Reserved
		 * 
		 * This procedure inserts an acct_trans_detail record. Note that
		 * the ord values start at 1 and are contiguous. If null is passed
		 * the value becomes the next avail.
		 */		
		
		Integer detail_id;		
		detail_id = null;		
		
		//set debug file to '/tmp/_ins_acct_trans_de.trace';
		//trace on;
		
		// check which detail to add
		if (ord == null) {			
			ord = 1 + (
				+ "select max(acct_trans_detail.ord)"
				+ " from acct_trans_detail"
				+ " where acct_trans_detail.acct_trans_id.equals(acct_trans_id)");
		}		
		
		if (variance != "N" && variance != "Y") {			
			throw new ProcedureException(-746, 0, "_ins_acct_trans_de: variance indicator must be 'Y' or 'N'.");
		}		
		

		PreparedStatement pstmt1 = prepareInsert(
				  "insert into acct_trans_detail (acct_trans_id, ord, amount, promo_id, variance)"
				+ " values (?, ?, ?, ?, ?)");
		if (acct_trans_id != null) {
			pstmt1.setInt(1, acct_trans_id);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		if (ord != null) {
			pstmt1.setInt(2, ord);
		}
		else {
			pstmt1.setNull(2, Types.JAVA_OBJECT);
		}
		if (amount != null) {
			pstmt1.setDouble(3, amount);
		}
		else {
			pstmt1.setNull(3, Types.JAVA_OBJECT);
		}
		if (promo_id != null) {
			pstmt1.setInt(4, promo_id);
		}
		else {
			pstmt1.setNull(4, Types.JAVA_OBJECT);
		}
		if (variance != null) {
			pstmt1.setString(5, variance);
		}
		else {
			pstmt1.setNull(5, Types.JAVA_OBJECT);
		}
		executeUpdate(pstmt1);
		pstmt1.close();		
		
		//Get the serial value from the insert.
		detail_id = dbinfo("sqlca.sqlerrd1");		
		
		return detail_id;
	}

}