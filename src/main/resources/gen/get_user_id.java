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

public class get_user_id extends AbstractProcedure {

	public Integer execute(String user_name) throws SQLException, ProcedureException {		
		
		/*
		 * Get the user id corresponding to the specified user name.
		 */		
		
		Integer user_id;		
		
		user_id = null;		
		
		user_name = upper(user_name);		
		
		//lookup the user_id by user_name

		PreparedStatement pstmt1 = prepareStatement(
				  "select user.user_id"
				+ " from user"
				+ " where user.user_name = ?");
		
		if (user_name != null) {
			pstmt1.setString(1, user_name);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		user_id = rs1.getInt(1);
		pstmt1.close();		
		
		//return the user_id
		return user_id;
	}

}