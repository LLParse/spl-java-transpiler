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

public class get_redemption_id extends AbstractProcedure {

	public Integer execute(Integer acct_trans_id) throws SQLException, ProcedureException {		
		
		/*
		 * Get the redemption id associated with the specified transaction.
		 */		
		
		Integer redemption_id;		
		
		redemption_id = null;		
		

		PreparedStatement pstmt1 = prepareStatement(
				  "select redemption.redemption_id"
				+ " from redemption"
				+ " where redemption.acct_trans_id = ?");
		
		if (acct_trans_id != null) {
			pstmt1.setInt(1, acct_trans_id);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		redemption_id = rs1.getInt(1);
		pstmt1.close();		
		
		return redemption_id;
	}

}