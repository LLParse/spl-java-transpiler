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

public class pv_vanilla_stay_twice extends AbstractProcedure {

	public Double execute(Integer in_acct_id, Integer in_cust_id, Integer in_promo_id, Integer in_criteria, Double in_value, Double in_stay_rev, String in_chain_cd, Integer in_los, Integer in_stay_id) throws SQLException, ProcedureException {		
		
		/*
		 * Compute the amount of points to award for vanilla stay twice promo. The minumum and maximum promo values are specified in the promo_value table.
		 *   A temp table containing rows for the stays being awarded will be available for this function to sum and determine the points to award.
		 *                          
		 *   $Id: pv_vanilla_stay_twice.sql 99 2012-12-19 21:14:54Z michael_giroux $
		 *    
		 *   Copyright (C) 2012 Choice Hotels International, Inc.
		 */		
		
		Double l_amount;		
		Double l_sum_points;		
		Double l_min_value;		
		Double l_max_value;		
		Double l_residual;		
		Double l_diff;		
		String l_debug;		
		
		Integer sql_error;		
		Integer isam_error;		
		String error_data;		
		
		try {			
			
			l_amount = 0.0;			
			l_sum_points = 0.0;			
			l_min_value = 0.0;			
			l_max_value = 0.0;			
			l_residual = 0.0;			
			l_diff = 0.0;			
			l_debug = "F";			
			
			// set up tracing based on app_config entry
			// call settrace('pv_vanilla_stay_twice') returning l_debug;
			// if (l_debug = 'T') then
			//   set debug file to '/tmp/pv_vanilla_stay_twice_' || dbinfo('sessionid') || '.trace';
			//   trace on;
			// end if;
			

			PreparedStatement pstmt1 = prepareStatement(
					  "select sum(amount)"
					+ " from qualified_stays_vanilla_stay_twice");
			
			ResultSet rs1 = executeQuery(pstmt1);
			rs1.next();
			l_sum_points = rs1.getDouble(1);
			pstmt1.close();			
			
			if (l_sum_points == null) {				
				throw new ProcedureException(-746, 0, "pv_vanilla_stay_twice: qualified_stays_vanilla_stay_twice table contained no points");
			}			
			

			PreparedStatement pstmt2 = prepareStatement(
					  "select min_value, max_value"
					+ " from promo_value"
					+ " where promo_id = ?");
			
			if (in_promo_id != null) {
				pstmt2.setInt(1, in_promo_id);
			}
			else {
				pstmt2.setNull(1, Types.JAVA_OBJECT);
			}
			ResultSet rs2 = executeQuery(pstmt2);
			rs2.next();
			l_min_value = rs2.getDouble(1);
			l_max_value = rs2.getDouble(2);
			pstmt2.close();			
			
			l_diff = l_max_value - l_min_value;			
			
			l_residual = mod(l_sum_points, l_max_value);			
			
			if (l_residual < l_diff) {				
				l_amount = l_max_value - l_residual;
			}			
			else {				
				l_amount = l_min_value;
			}			
			
			return l_amount;			
			

		}
		catch (SQLException e) {
			sql_error = e.getErrorCode();
			isam_error = 0;
			error_data = e.getMessage();
			{				
				throw new ProcedureException(sql_error, isam_error, error_data);
			}
		}
	}

}