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

public class pf_is_employee extends AbstractProcedure {

	public String execute(Integer acct_id, Integer promo_id, String stay_type, Integer prop_id, Timestamp doa, Integer los, String rm_type, String srp_code, Double rm_revenue, Double fb_revenue, Double other_revenue, String curr_code, String res_source, Integer raf_reg_id) throws SQLException, ProcedureException {		
		
		/*
		 * Determine whether or not the primary customer for the specfied account
		 *   is an employee.
		 *    
		 *   $Id: pf_is_employee.sql 99 2012-12-19 21:14:54Z michael_giroux $
		 *    
		 *   Copyright (C) 2009 Choice Hotels International, Inc.
		 */		
		
		Integer ac;		
		
		ac = 0;		
		

		PreparedStatement pstmt1 = prepareStatement(
				  "select count(*)"
				+ " from cust_acct ca"
				+ "  inner join cust_attribute t on ca.cust_id = t.cust_id inner join attribute_value v on t.attribute_value_id = v.attribute_value_id"
				+ " and v.value = \"Enabled\" inner join attribute a on a.attribute_id = v.attribute_id"
				+ " and a.attribute_cd = \"Employee\""
				+ " where ca.acct_id = ?");
		
		if (acct_id != null) {
			pstmt1.setInt(1, acct_id);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		ac = rs1.getInt(1);
		pstmt1.close();		
		
		if (ac > 0) {			
			return "T";
		}		
		else {			
			return "F";
		}
	}

}