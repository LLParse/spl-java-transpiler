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

public class get_stay_id extends AbstractProcedure {

	public Integer execute(Integer acct_trans_id) throws SQLException, ProcedureException {		
		
		/*
		 * Get the stay id associated with the specified transaction.
		 */		
		
		Integer stay_id;		
		
		stay_id = null;		
		

		PreparedStatement pstmt1 = prepareStatement(
				  "select stay.stay_id"
				+ " from stay"
				+ " where stay.acct_trans_id = ?");
		
		if (acct_trans_id != null) {
			pstmt1.setInt(1, acct_trans_id);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		stay_id = rs1.getInt(1);
		pstmt1.close();		
		
		return stay_id;
	}

}