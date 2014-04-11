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

public class get_mbr_summary extends AbstractProcedure {

	public Collection<Object> execute(String recog_cd, String recog_id) throws SQLException, ProcedureException {		//point balance
		
		/*
		 * (]$[) $RCSfile$:$Revision: 99 $ | CDATE=$Date: 2012-12-19 14:14:54 -0700 (Wed, 19 Dec 2012) $ ~
		 * 
		 *   get_mbr_summary -  get member summary information
		 * 
		 *       Copyright (C) 2002 Choice Hotels International, Inc.
		 *                       All Rights Reserved
		 */		
		
		String acct_status;		
		String alt_acct_num;		
		String frst_name;		
		String mid_initial;		
		String last_name;		
		String addr_type;		
		String addr_1;		
		String addr_2;		
		String city;		
		String state;		
		String country;		
		String zip;		
		String phone;		
		Integer acct_bal;		
		
		//  set debug file to '/tmp/get_mbr_summary.trace';
		//  trace on;
		
		acct_status = null;		
		alt_acct_num = null;		
		frst_name = null;		
		mid_initial = null;		
		last_name = null;		
		addr_type = null;		
		addr_1 = null;		
		addr_2 = null;		
		city = null;		
		state = null;		
		country = null;		
		zip = null;		
		phone = null;		
		acct_bal = null;		
		
		new get_acct_summary().execute(recog_cd, recog_id);		
		
		return new ArrayList<Object>(Arrays.<Object>asList(acct_status, alt_acct_num, frst_name, mid_initial, last_name, addr_type, addr_1, addr_2, city, state, country, zip, phone, acct_bal));
	}

}