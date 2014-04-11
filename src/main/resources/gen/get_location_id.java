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

public class get_location_id extends AbstractProcedure {

	public Integer execute(String location_name) throws SQLException, ProcedureException {		
		
		/*
		 * Get the location id corresponding to the specified location name.
		 */		
		
		Integer location_id;		
		
		location_id = null;		
		
		location_name = upper(location_name);		
		
		//lookup the location_id by location_name

		PreparedStatement pstmt1 = prepareStatement(
				  "select location.location_id"
				+ " from location"
				+ " where location.location_name = ?");
		
		if (location_name != null) {
			pstmt1.setString(1, location_name);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		location_id = rs1.getInt(1);
		pstmt1.close();		
		
		//return the location_id
		return location_id;
	}

}