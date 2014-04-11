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

public class get_appl_group_id extends AbstractProcedure {

	public Integer execute(String recog_cd, String appl_group_cd) throws SQLException, ProcedureException {		
		
		/*
		 * Get the application group id corresponding to the
		 *   specified application group code for the specified 
		 *   program.
		 */		
		
		Integer appl_group_id;		
		
		appl_group_id = null;		
		
		//lookup the appl_group_id by recog_cd and appl_group_cd

		PreparedStatement pstmt1 = prepareStatement(
				  "select appl_group.appl_group_id"
				+ " from appl_group"
				+ " where appl_group.recog_cd = ?"
				+ " and appl_group.appl_group_cd = ?");
		
		if (recog_cd != null) {
			pstmt1.setString(1, recog_cd);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		if (appl_group_cd != null) {
			pstmt1.setString(2, appl_group_cd);
		}
		else {
			pstmt1.setNull(2, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		appl_group_id = rs1.getInt(1);
		pstmt1.close();		
		
		//return the application group id
		return appl_group_id;
	}

}