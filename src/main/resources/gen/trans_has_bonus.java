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

public class trans_has_bonus extends AbstractProcedure {

	public String execute(Integer acct_trans_id) throws SQLException, ProcedureException {		
		/*
		 * trans_has_bonus.sql - Determine if transaction has bonus detail item
		 * 
		 * (]$[) $RCSfile$:$Revision: 99 $ | CDATE=$Date: 2012-12-19 14:14:54 -0700 (Wed, 19 Dec 2012) $ ~
		 * 
		 * 
		 *       Copyright (C) 2001 Choice Hotels International, Inc.
		 * 		    All Rights Reserved
		 * 
		 *   This procedure checks if a transaction has a promotion associated that is
		 *   not of type 'A'lways.
		 */		
		
		Integer has_bonus;		
		
		has_bonus = null;		
		
		// set debug file to '/tmp/trans_has_bonus.trace';
		// trace on;
		

		PreparedStatement pstmt1 = prepareStatement(
				  "select count(*)"
				+ " from acct_trans_detail, promo"
				+ " where acct_trans_detail.acct_trans_id = ?"
				+ " and promo.promo_id = acct_trans_detail.promo_id"
				+ " and promo.rule != \"A\"");
		
		if (acct_trans_id != null) {
			pstmt1.setInt(1, acct_trans_id);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		has_bonus = rs1.getInt(1);
		pstmt1.close();		
		
		if (has_bonus > 0) {			
			return "Y";
		}		
		
		return "N";
	}

}