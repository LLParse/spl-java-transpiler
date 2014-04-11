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

public class get_par_status extends AbstractProcedure {

	public String execute(Integer par_id) throws SQLException, ProcedureException {		
		
		/*
		 * Get the status of the specified partner activity request.
		 */		
		
		String request_status;		
		
		request_status = null;		
		

		PreparedStatement pstmt1 = prepareStatement(
				  "select par.request_status"
				+ " from par"
				+ " where par.par_id = ?");
		
		if (par_id != null) {
			pstmt1.setInt(1, par_id);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		request_status = rs1.getString(1);
		pstmt1.close();		
		
		if (request_status == null) {			
			throw new ProcedureException(-746, 0, "get_par_status: Partner activity request not found.");
		}		
		
		return request_status;
	}

}