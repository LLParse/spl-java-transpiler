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

public class pf_junitoverride extends AbstractProcedure {

	public String execute(Integer in_acct_id, Integer in_promo_id, String in_stay_type, Integer in_prop_id, Timestamp in_doa, Integer in_los, String in_rm_type, String in_srp_code, Double in_rm_revenue, Double in_fb_revenue, Double in_other_revenue, String in_curr_code, String in_res_source, Integer in_stay_id) throws SQLException, ProcedureException {		
		
		
		/*
		 * pf_junitoverride.sql - A promotion filter for the special junit override promotion. Always returns
		 *                        false so bonus is never awarded. This allows any stay to be eligible for an 
		 *                        override. Used for Selenium/JUNIT testing.
		 * 
		 * $Id: pf_junitoverride.sql 99 2012-12-19 21:14:54Z michael_giroux $
		 * 
		 *      Copyright (C) 2012 Choice Hotels International, Inc.
		 *      All Rights Reserved
		 */		
		
		
		return "F";
	}

}