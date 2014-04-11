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

public class get_base_points extends AbstractProcedure {

	public Double execute(Integer in_acct_trans_id) throws SQLException, ProcedureException {		
		
		Double l_points;		
		String l_debug;		
		
		/*
		 * get_base_points - simple, but oft used routine to get the base points for a specific,
		 *                   non-reversed transaction
		 * 
		 * $Id: get_base_points.sql 99 2012-12-19 21:14:54Z michael_giroux $
		 * 
		 *       Copyright (C) 2011 Choice Hotels International, Inc.
		 */		
		
		l_points = null;		
		l_debug = "F";		
		
		// set up tracing based on app_config entry
		l_debug = new settrace().execute("get_base_points");		
		if (l_debug.equals("T")) {			
			setDebugFile("/tmp/get_base_points.trace");			
			trace("on");
		}		
		

		PreparedStatement pstmt1 = prepareStatement(
				  "select d.amount"
				+ " from acct_trans a, acct_trans_detail d, promo p"
				+ " where d.acct_trans_id = ?"
				+ " and a.acct_trans_id = d.acct_trans_id"
				+ " and d.promo_id = p.promo_id"
				+ " and a.rev_acct_trans_id is null"
				+ " and p.rule = \"A\"");
		
		if (in_acct_trans_id != null) {
			pstmt1.setInt(1, in_acct_trans_id);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		l_points = rs1.getDouble(1);
		pstmt1.close();		
		
		return l_points;
	}

}