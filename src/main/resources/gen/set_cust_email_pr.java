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

public class set_cust_email_pr extends AbstractProcedure {

	public Integer execute(String recog_cd, String recog_id, String email_addr, String send_email, String user_name) throws SQLException, ProcedureException {		
		
		/*
		 * (]$[) $RCSfile$:$Revision: 99 $ | CDATE=$Date: 2012-12-19 14:14:54 -0700 (Wed, 19 Dec 2012) $ ~
		 * 
		 *  Set the contact preference on a customer's email address.
		 * 
		 * 	Copyright (C) 2002 Choice Hotels International, Inc.
		 *                       All Rights Reserved
		 */		
		
		Integer acct_id;		
		Integer user_id;		
		Integer update_count;		
		
		// set debug file to '/tmp/set_cust_email_pr.trace';
		// trace on;
		
		acct_id = null;		
		user_id = null;		
		
		// validate  send_email
		if (!send_email.equals("Y") && !send_email.equals("N")) {			
			throw new ProcedureException(-746, 0, "set_cust_email_pr: send_email must be 'Y' or 'N'");
		}		
		
		// validate and encode the recog_cd and recog_id
		acct_id = new get_acct_id().execute(recog_cd, recog_id);		
		if (acct_id == null) {			
			throw new ProcedureException(-746, 0, "set_cust_email_pr: recog_cd, recog_id is not known");
		}		
		
		user_id = new get_user_id().execute(user_name);		
		if (user_id == null) {			
			throw new ProcedureException(-746, 0, "set_cust_email_pr: user_name is not known");
		}		
		
		// set the preference

		PreparedStatement pstmt1 = prepareStatement(
				  "update cust_email"
				+ " set (send_email, last_update_dtime, last_update_id) = (?, current, ?)"
				+ " where cust_email.cust_id in ("
					+ "select ca.cust_id"
					+ " from cust_acct ca"
					+ " where ca.acct_id = ?"
				+ ")"
				+ " and upper(cust_email.email_addr) = upper(?)");
		
		if (send_email != null) {
			pstmt1.setString(1, send_email);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		if (user_id != null) {
			pstmt1.setInt(2, user_id);
		}
		else {
			pstmt1.setNull(2, Types.JAVA_OBJECT);
		}
		if (acct_id != null) {
			pstmt1.setInt(3, acct_id);
		}
		else {
			pstmt1.setNull(3, Types.JAVA_OBJECT);
		}
		if (email_addr != null) {
			pstmt1.setString(4, email_addr);
		}
		else {
			pstmt1.setNull(4, Types.JAVA_OBJECT);
		}
		executeUpdate(pstmt1);
		pstmt1.close();		
		
		update_count = dbinfo("sqlca.sqlerrd2");		
		
		return update_count;
	}

}