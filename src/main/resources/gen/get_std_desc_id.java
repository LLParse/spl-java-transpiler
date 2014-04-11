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

public class get_std_desc_id extends AbstractProcedure {

	public Integer execute(String recog_cd, String clazz, String subclass, String std_desc_cd) throws SQLException, ProcedureException {		
		
		/*
		 * Get the standard description id of the specified standard
		 *   description.
		 */		
		
		Integer std_desc_id;		
		
		std_desc_id = null;		
		

		PreparedStatement pstmt1 = prepareStatement(
				  "select std_desc.std_desc_id"
				+ " from std_desc"
				+ " where std_desc.recog_cd = ?"
				+ " and std_desc.class = ?"
				+ " and std_desc.subclass = ?"
				+ " and std_desc.std_desc_cd = ?");
		
		if (recog_cd != null) {
			pstmt1.setString(1, recog_cd);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		if (clazz != null) {
			pstmt1.setString(2, clazz);
		}
		else {
			pstmt1.setNull(2, Types.JAVA_OBJECT);
		}
		if (subclass != null) {
			pstmt1.setString(3, subclass);
		}
		else {
			pstmt1.setNull(3, Types.JAVA_OBJECT);
		}
		if (std_desc_cd != null) {
			pstmt1.setString(4, std_desc_cd);
		}
		else {
			pstmt1.setNull(4, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		std_desc_id = rs1.getInt(1);
		pstmt1.close();		
		
		return std_desc_id;
	}

}