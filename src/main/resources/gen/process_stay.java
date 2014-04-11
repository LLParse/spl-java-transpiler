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

public class process_stay extends AbstractProcedure {

	public Collection<Object> execute(Integer acct_id, Integer stay_id, Integer cust_call_id, Double amount, Integer orig_trans_id) throws SQLException, ProcedureException {		
		
		/*
		 * (]$[) $RCSfile$:$Revision: 99 $ | CDATE=$Date: 2012-12-19 14:14:54 -0700 (Wed, 19 Dec 2012) $ ~
		 * 
		 *  Process one stay record by generating the associated transaction
		 *  Returns acct_trans_id and amount on success.
		 * 
		 *         Copyright (C) 2003 Choice Hotels International, Inc.
		 *                       All Rights Reserved
		 */		
		
		// Data from new records
		Integer acct_trans_id;		
		
		// Working variables
		
		String stay_type;		
		Integer cust_id;		
		String pp_name;		
		Integer sql_error;		
		Integer isam_error;		
		String error_data;		
		
		
		// Initialize all defined variables.
		acct_trans_id = null;		
		stay_type = null;		
		cust_id = null;		
		pp_name = null;		
		sql_error = null;		
		isam_error = null;		
		error_data = null;		
		
		//set debug file to '/tmp/process_stay.trace';
		//trace on;           
		
		//----------------------------------------------------------
		// First validate stay_id, completion of customer, and type 
		//----------------------------------------------------------

		PreparedStatement pstmt1 = prepareStatement(
				  "select s.stay_type, s.cust_id"
				+ " from stay s, cust c"
				+ " where s.stay_id = ?"
				+ " and s.cust_id = c.cust_id");
		
		if (stay_id != null) {
			pstmt1.setInt(1, stay_id);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		stay_type = rs1.getString(1);
		cust_id = rs1.getInt(2);
		pstmt1.close();		
		
		if (stay_type == null) {			
			throw new ProcedureException(-746, 0, "process_stay: the stay is invalid");
		}		
		
		if ((!stay_type.equals("N") && !stay_type.equals("F") && !stay_type.equals("R") && !stay_type.equals("X"))) {			
			throw new ProcedureException(-746, 0, "process_stay: stay_type must be 'N', 'F', 'R' or 'X'");
		}		
		
		// Call the promotion pre-processors.
		

		PreparedStatement pstmt2 = prepareStatement(
				  "select pp.name"
				+ " from promo_proc pp"
				+ " where pp.trigger = \"STAY\""
				+ " and pp.type = \"PRE\""
				+ " and pp.start_date <= today"
				+ " and pp.stop_date >= today");
		
		ResultSet rs2 = executeQuery(pstmt2);
		while (rs2.next()) {
			pp_name = rs2.getString(1);			
			
			
			// TODO spot check reflection call
			try {
				Class<?> clazz = Class.forName("process_stay");
				Method method = clazz.getDeclaredMethod(pp_name, Integer.class, Integer.class);
				method.invoke(acct_id, cust_id);
			}
			catch (Exception e) {
				e.printStackTrace();
			};
		}
		pstmt2.close();		
		
		// Process the stay event.
		new _proc_stay_event().execute(acct_id, stay_id, cust_call_id, amount);		
		
		// Check if this new stay is a resubmitted stay and if so, link them.
		if (orig_trans_id != null) {			
			orig_trans_id = new link_rsub_trans().execute(orig_trans_id, acct_trans_id);
		}		
		
		if (amount > 0) {			
			// Call the promotion post-processors.
			

			PreparedStatement pstmt3 = prepareStatement(
					  "select pp.name"
					+ " from promo_proc pp"
					+ " where pp.trigger = \"STAY\""
					+ " and pp.type = \"POST\""
					+ " and pp.start_date <= today"
					+ " and pp.stop_date >= today");
			
			ResultSet rs3 = executeQuery(pstmt3);
			while (rs3.next()) {
				pp_name = rs3.getString(1);				
				
				
				// TODO spot check reflection call
				try {
					Class<?> clazz = Class.forName("process_stay");
					Method method = clazz.getDeclaredMethod(pp_name, Integer.class, Integer.class);
					method.invoke(acct_id, cust_id);
				}
				catch (Exception e) {
					e.printStackTrace();
				};
			}
			pstmt3.close();
		}		
		
		return new ArrayList<Object>(Arrays.<Object>asList(acct_trans_id, amount));
	}

}