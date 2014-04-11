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

public class pf_iseconomy extends AbstractProcedure {

	public String execute(Integer in_prop_id) throws SQLException, ProcedureException {		
		
		/*
		 * pf_iseconomy.sql - Check if property is an economy brand
		 *    
		 * $Id: pf_iseconomy.sql 99 2012-12-19 21:14:54Z michael_giroux $
		 *    
		 *        Copyright (C) 2012 Choice Hotels International, Inc.
		 */		
		
		String l_chain_group_cd;		
		String l_answer;		
		String l_debug;		
		
		Integer sql_error;		
		Integer isam_error;		
		String error_data;		
		
		try {			
			
			l_chain_group_cd = null;			
			l_answer = "F";			
			l_debug = "F";			
			

			PreparedStatement pstmt1 = prepareStatement(
					  "select cg.chain_group_cd"
					+ " from chain_group cg, chain_group_detail cgd, prop p"
					+ " where cg.chain_group_id = cgd.chain_group_id"
					+ " and cgd.chain_id = p.chain_id"
					+ " and p.prop_id = ?");
			
			if (in_prop_id != null) {
				pstmt1.setInt(1, in_prop_id);
			}
			else {
				pstmt1.setNull(1, Types.JAVA_OBJECT);
			}
			ResultSet rs1 = executeQuery(pstmt1);
			rs1.next();
			l_chain_group_cd = rs1.getString(1);
			pstmt1.close();			
			
			if ((l_chain_group_cd.equals("EC") || l_chain_group_cd.equals("ES"))) {				
				l_answer = "T";
			}			
			
			return l_answer;			
			

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