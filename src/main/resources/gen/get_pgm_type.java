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

public class get_pgm_type extends AbstractProcedure {

	public String execute(Integer acct_id) throws SQLException, ProcedureException {		
		/*
		 * $RCSfile$ - Checks program type associated with an account - $Revision: 99 $
		 * 
		 * (]$[) $RCSfile$:$Revision: 99 $ | CDATE=$Date: 2012-12-19 14:14:54 -0700 (Wed, 19 Dec 2012) $ ~
		 * 
		 *       Copyright (C) 2001 Choice Hotels International, Inc.
		 *                       All Rights Reserved
		 * 
		 * This procedure returns the type of partner associated with an account. 
		 * Returns 'Y' if partner is external, 'N' if internal.
		 */		
		
		String external_pgm;		
		
		external_pgm = null;		
		
		// Determine partner type associated with this account.

		PreparedStatement pstmt1 = prepareStatement(
				  "select recog_pgm.external_pgm"
				+ " from recog_pgm"
				+ " where recog_pgm.recog_cd = new get_recog_cd().execute(?)");
		
		if (acct_id != null) {
			pstmt1.setInt(1, acct_id);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		external_pgm = rs1.getString(1);
		pstmt1.close();		
		
		if (external_pgm == null) {			
			throw new ProcedureException(-746, 0, "get_pgm_type: Unable to determine partner type");
		}		
		
		return external_pgm;
	}

}