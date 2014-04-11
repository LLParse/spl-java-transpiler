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

public class check_filter extends AbstractProcedure {

	public String execute(Integer filter_id, Integer acct_id, Integer promo_id, String stay_type, Integer prop_id, Timestamp doa, Integer los, String rm_type, String srp_code, String curr_code, Double rm_revenue, Double fb_revenue, Double other_revenue, String res_source, Integer filter_data) throws SQLException, ProcedureException {		
		/*
		 * promotional filter launcher 
		 *  (]$[) $RCSfile$:$Revision: 99 $ | CDATE=$Date: 2012-12-19 14:14:54 -0700 (Wed, 19 Dec 2012) $ ~
		 * 
		 *   Given a filter ID execute the associated stored procedure.  Return
		 *   the result character 'T' or 'F'.
		 * 
		 *       Copyright (C) 2000,2005 Choice Hotels International, Inc.
		 * 		    All Rights Reserved
		 */		
		
		String proc_name;		
		String answer;		
		
		// set debug file to '/tmp/check_filter.trace';
		// trace on;
		
		proc_name = null;		
		answer = "F";		
		

		PreparedStatement pstmt1 = prepareStatement(
				  "select p.name"
				+ " from promo_filter p"
				+ " where p.filter_id = ?");
		
		if (filter_id != null) {
			pstmt1.setInt(1, filter_id);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		proc_name = rs1.getString(1);
		pstmt1.close();		
		
		if (proc_name == null) {			
			throw new ProcedureException(-746, 0, "check_filter: Unknown filter ID");
		}		
		
		
		// TODO spot check reflection call
		try {
			Class<?> clazz = Class.forName("check_filter");
			Method method = clazz.getDeclaredMethod(proc_name, Integer.class, Integer.class, String.class, Integer.class, Timestamp.class, Integer.class, String.class, String.class, Double.class, Double.class, Double.class, String.class, String.class, Integer.class);
			answer = (String) method.invoke(acct_id, promo_id, stay_type, prop_id, doa, los, rm_type, srp_code, rm_revenue, fb_revenue, other_revenue, curr_code, res_source, filter_data);
		}
		catch (Exception e) {
			e.printStackTrace();
		};		
		
		// Return the result...
		return answer;
	}

}