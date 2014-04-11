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

public class get_user_std_desc extends AbstractProcedure {

	public String execute(Integer std_desc_id) throws SQLException, ProcedureException {		
		
		/*
		 * Get the user description corresponding to the specified
		 *   standard description id.
		 */		
		
		String user_desc;		
		
		user_desc = null;		
		

		PreparedStatement pstmt1 = prepareStatement(
				  "select std_desc.user_desc"
				+ " from std_desc"
				+ " where std_desc.std_desc_id = ?");
		
		if (std_desc_id != null) {
			pstmt1.setInt(1, std_desc_id);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		user_desc = rs1.getString(1);
		pstmt1.close();		
		
		return user_desc;
	}

}