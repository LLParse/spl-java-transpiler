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

public class get_recog_cd extends AbstractProcedure {

	public String execute(Integer acct_id) throws SQLException, ProcedureException {		
		
		/*
		 * Get the recog code corresponding to the specified account id.
		 */		
		
		String recog_cd;		
		
		recog_cd = null;		
		

		PreparedStatement pstmt1 = prepareStatement(
				  "select acct.recog_cd"
				+ " from acct"
				+ " where acct.acct_id = ?");
		
		if (acct_id != null) {
			pstmt1.setInt(1, acct_id);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		recog_cd = rs1.getString(1);
		pstmt1.close();		
		
		return recog_cd;
	}

}