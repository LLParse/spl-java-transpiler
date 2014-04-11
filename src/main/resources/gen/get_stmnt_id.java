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

public class get_stmnt_id extends AbstractProcedure {

	public Integer execute(Timestamp stmnt_date, String recog_cd) throws SQLException, ProcedureException {		
		/*
		 * $RCSfile$ - Get the statement ID for a given date - $Revision: 99 $
		 * 
		 * (]$[) $RCSfile$:$Revision: 99 $ | CDATE=$Date: 2012-12-19 14:14:54 -0700 (Wed, 19 Dec 2012) $ ~
		 * 
		 *       Copyright (C) 2001 Choice Hotels International, Inc.
		 *                       All Rights Reserved
		 * 
		 * This procedure returns the statement ID for a given date.
		 */		
		
		Integer stmnt_id;		
		
		stmnt_id = null;		
		

		PreparedStatement pstmt1 = prepareStatement(
				  "select stmnt_cycle.stmnt_id"
				+ " from stmnt_cycle"
				+ " where stmnt_cycle.recog_cd = ?"
				+ " and ");
		
		if (recog_cd != null) {
			pstmt1.setString(1, recog_cd);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		stmnt_id = rs1.getInt(1);
		pstmt1.close();		
		
		if (stmnt_id == null) {			
			throw new ProcedureException(-746, 0, "get_stmnt_id: No statement cycle defined for given date");
		}		
		
		return stmnt_id;
	}

}