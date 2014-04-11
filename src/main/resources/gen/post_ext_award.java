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

public class post_ext_award extends AbstractProcedure {

	public Collection<Object> execute(String recog_cd, String recog_id, String frst_name, String last_name, Double amount, String post_desc, String prop_cd) throws SQLException, ProcedureException {		
		
		/*
		 * (]$[) $RCSfile$:$Revision: 99 $ | CDATE=$Date: 2012-12-19 14:14:54 -0700 (Wed, 19 Dec 2012) $ ~
		 * 
		 *  Process external partner award to an account
		 *  Returns acct_trans_id on success.
		 * 
		 * 
		 * 	Copyright (C) 2002 Choice Hotels International, Inc.
		 *                       All Rights Reserved
		 */		
		
		// Data from new records/calculations
		Integer cust_id;		
		Integer acct_id;		
		String curr_frst;		
		String curr_mid;		
		String curr_last;		
		String acct_ind;		
		String cust_ind;		
		String addr_result;		
		Integer acct_trans_id;		
		Integer std_desc_id;		
		Integer prop_id;		
		Integer entry_id;		
		Integer detail_id;		
		Integer cust_call_id;		
		String notes;		
		String trans_unit_cd;		
		Integer par_id;		
		
		//- member table
		String acct_status;		
		
		Integer sql_error;		
		Integer isam_error;		
		String error_data;		
		Integer error_flag;		
		String error_msg;		
		
		// Initialize defined variables to null
		cust_id = null;		
		acct_id = null;		
		curr_frst = null;		
		curr_mid = null;		
		curr_last = null;		
		acct_ind = null;		
		cust_ind = null;		
		addr_result = null;		
		acct_trans_id = null;		
		detail_id = null;		
		std_desc_id = null;		
		prop_id = null;		
		entry_id = null;		
		cust_call_id = null;		
		notes = null;		
		acct_status = null;		
		trans_unit_cd = null;		
		par_id = null;		
		error_flag = 1;		// default to success, 0 = failure
		error_msg = null;		
		isam_error = null;		
		sql_error = null;		
		
		//set debug file to '/tmp/post_award.trace';
		//trace on;
		//----------------------------------------------------------
		// First get/create the acct_id and cust_id from input.
		//----------------------------------------------------------
		entry_id = new get_user_id().execute("AUTOCREATE");		
		
		create_ext_cust(recog_cd, recog_id, frst_name, null, last_name, null, null, null, null, null, null, null, null, null, null, null, null, entry_id);		
		
		
		// Get the account unit of measure for log entry
		trans_unit_cd = new get_program_units().execute(acct_id);		
		
		notes = "Account balance adjusted by " + amount + "  " + trans_unit_cd;		
		std_desc_id = new get_std_desc_id().execute(new get_recog_cd().execute(acct_id), "MemberCall", "PointAdjustment", "GOODWILL");		
		prop_id = new get_prop_id().execute(prop_cd);		
		entry_id = new get_user_id().execute("AUTOCREATE");		
		
		// Next add the mbr_call and mbr_call_log records
		cust_call_id = new _add_call_ticket().execute(cust_id, acct_id, "C", "L", std_desc_id, post_desc, prop_id, entry_id, notes);		
		
		
		//----------------------------------------------------------
		// generate the transaction and detail 
		//----------------------------------------------------------
		
		acct_trans_id = new _post_trans().execute(acct_id, cust_id, "A", null, cust_call_id, null, amount, null);		
		detail_id = new _ins_acct_trans_de().execute(acct_trans_id, 1, amount, null, "N");		
		
		
		// Finally apply the transaction amount to account indicating it has
		// been processed.

		PreparedStatement pstmt1 = prepareStatement(
				  "update acct_trans"
				+ " set acct_trans.trans_status = \"A\", acct_trans.final_dtime = current"
				+ " where acct_trans.acct_trans_id = ?");
		
		if (acct_trans_id != null) {
			pstmt1.setInt(1, acct_trans_id);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		executeUpdate(pstmt1);
		pstmt1.close();		
		

		PreparedStatement pstmt2 = prepareStatement(
				  "update par"
				+ " set par.request_status = \"A\""
				+ " where par.acct_trans_id = ?");
		
		if (acct_trans_id != null) {
			pstmt2.setInt(1, acct_trans_id);
		}
		else {
			pstmt2.setNull(1, Types.JAVA_OBJECT);
		}
		executeUpdate(pstmt2);
		pstmt2.close();		
		

		PreparedStatement pstmt3 = prepareStatement(
				  "select p.par_id"
				+ " from par p"
				+ " where p.acct_trans_id = ?");
		
		if (acct_trans_id != null) {
			pstmt3.setInt(1, acct_trans_id);
		}
		else {
			pstmt3.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt3);
		rs1.next();
		par_id = rs1.getInt(1);
		pstmt3.close();		
		
		return new ArrayList<Object>(Arrays.<Object>asList(par_id, error_flag, error_msg));
	}

}