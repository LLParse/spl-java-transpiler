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

public class get_prop_id_date extends AbstractProcedure {

	public Integer execute(String prop_cd, Timestamp eff_date) throws SQLException, ProcedureException {		
		
		/*
		 * Get the most current property id of the specified property code
		 *   for the specified date.
		 */		
		
		Integer prop_id;		
		
		prop_id = null;		
		

		PreparedStatement pstmt1 = prepareStatement(
				  "select prop.prop_id"
				+ " from prop"
				+ " where prop.prop_cd = ?"
				+ " and prop.eff_date = ("
					+ "select max(prop.eff_date)"
					+ " from prop"
					+ " where prop.prop_cd = ?"
					+ " and prop.eff_date <= ?"
				+ ")");
		
		if (prop_cd != null) {
			pstmt1.setString(1, prop_cd);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		if (prop_cd != null) {
			pstmt1.setString(2, prop_cd);
		}
		else {
			pstmt1.setNull(2, Types.JAVA_OBJECT);
		}
		if (eff_date != null) {
			pstmt1.setObject(3, eff_date);
		}
		else {
			pstmt1.setNull(3, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		prop_id = rs1.getInt(1);
		pstmt1.close();		
		
		return prop_id;
	}

}