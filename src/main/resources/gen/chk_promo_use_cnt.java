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

public class chk_promo_use_cnt extends AbstractProcedure {

	public Integer execute(Integer promo_id, Integer acct_id) throws SQLException, ProcedureException {		
		
		/*
		 * (]$[) $RCSfile$:$Revision: 99 $ | CDATE=$Date: 2012-12-19 14:14:54 -0700 (Wed, 19 Dec 2012) $ ~
		 *   chk_promo_use_cnt counts the number of times a promo
		 *   has been used, ignoring promotion usage that has since
		 *   been reversed.
		 * 
		 * 
		 * 	  Copyright (C) 2003 Choice Hotels International, Inc.
		 * 			All Rights Reserved
		 */		
		
		Integer count;		
		count = 0;		
		

		PreparedStatement pstmt1 = prepareStatement(
				  "select count(*)"
				+ " from acct_trans_detail ad, acct_trans a"
				+ " where a.acct_id = ?"
				+ " and a.acct_trans_id = ad.acct_trans_id"
				+ " and a.rev_acct_trans_id is null"
				+ " and a.rsub_acct_trans_id is null"
				+ " and ad.promo_id = ?");
		
		if (acct_id != null) {
			pstmt1.setInt(1, acct_id);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		if (promo_id != null) {
			pstmt1.setInt(2, promo_id);
		}
		else {
			pstmt1.setNull(2, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		count = rs1.getInt(1);
		pstmt1.close();		
		
		return count;
	}

}