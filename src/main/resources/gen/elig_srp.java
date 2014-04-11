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

public class elig_srp extends AbstractProcedure {

	public String execute(String recog_cd, String srp_code) throws SQLException, ProcedureException {		
		
		/*
		 * (]$[) $RCSfile$:$Revision: 99 $ | CDATE=$Date: 2012-12-19 14:14:54 -0700 (Wed, 19 Dec 2012) $ ~
		 *   The SRP logic is as follows:
		 *     null = eligible
		 *     present but not in recog_srp = not eligible,
		 *     present and in recog_srp and eligible ind is 'N' = not eligible
		 *     present and in recog_srp and eligible ind is 'Y' = eligible
		 * 
		 *   
		 *       Copyright (C) 2003 Choice Hotels International, Inc.
		 *                       All Rights Reserved
		 */		
		
		String eligible;		
		eligible = null;		
		
		return "Y";		// SRP Check disabled..
		
		if (srp_code == null) {			
			return "Y";
		}		
		

		PreparedStatement pstmt1 = prepareStatement(
				  "select s.reward_eligible"
				+ " from recog_srp s"
				+ " where s.recog_cd = ?"
				+ " and s.srp_code = upper(?)");
		
		if (recog_cd != null) {
			pstmt1.setString(1, recog_cd);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		if (srp_code != null) {
			pstmt1.setString(2, srp_code);
		}
		else {
			pstmt1.setNull(2, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		eligible = rs1.getString(1);
		pstmt1.close();		
		
		if (dbinfo("sqlca.sqlerrd2").equals(0)) {			
			return "N";
		}		
		
		if (eligible.equals("N")) {			
			return "N";
		}		
		
		return "Y";
	}

}