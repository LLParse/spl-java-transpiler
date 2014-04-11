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

public class get_cust_currency extends AbstractProcedure {

	public String execute(Integer acct_id, Integer cust_id) throws SQLException, ProcedureException {		
		
		/*
		 * $RCSfile$ - Get customer's unit of currency - $Revision: 99 $
		 * 
		 * (]$[) $RCSfile$:$Revision: 99 $ | CDATE=$Date: 2012-12-19 14:14:54 -0700 (Wed, 19 Dec 2012) $ ~
		 * 
		 *   Get the specified customer's home currency.
		 * 
		 *   The home currency is based on the currency of the country
		 *   of the customer's home address.  If a home address is not
		 *   present use the member's business address.  If a business
		 *   address is not present use whatever other address the member
		 *   has on record. If no address is found, the currency defaults to the 
		 *   currency of the associated program.
		 * 
		 *       Copyright (C) 2001 Choice Hotels International, Inc.
		 *                       All Rights Reserved
		 */		
		
		String curr_cd;		
		String addr_type;		
		String primary_addr;		
		String home_curr_cd;		
		String bus_curr_cd;		
		String other_curr_cd;		
		String recog_cd;		
		
		// Initialize defined variables to null
		curr_cd = null;		
		cust_id = null;		
		addr_type = null;		
		home_curr_cd = null;		
		bus_curr_cd = null;		
		other_curr_cd = null;		
		recog_cd = null;		
		
		// set debug file to 'test.trace';
		// trace on;
		
		// Get the primary customer associated with the account
		

		PreparedStatement pstmt1 = prepareStatement(
				  "select country.curr_cd, cust_addr.addr_type"
				+ " from cust, cust_addr, country"
				+ " where cust.cust_id = cust_addr.cust_id"
				+ " and cust_addr.country = country.country_cd"
				+ " and cust.cust_id = ?");
		
		if (cust_id != null) {
			pstmt1.setInt(1, cust_id);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		while (rs1.next()) {
			curr_cd = rs1.getString(1);
			addr_type = rs1.getString(2);			
			
			if (addr_type.equals("H")) {				
				home_curr_cd = curr_cd;
			}			
			else if (addr_type.equals("B")) {				
				bus_curr_cd = curr_cd;
			}			
			else {				
				other_curr_cd = curr_cd;
			}
		}
		pstmt1.close();		
		
		if (home_curr_cd != null) {			
			return home_curr_cd;
		}		
		else if (bus_curr_cd != null) {			
			return bus_curr_cd;
		}		
		else if (other_curr_cd != null) {			
			return other_curr_cd;
		}		
		else {			
			// currency not found use program currency
			recog_cd = new get_recog_cd().execute(acct_id);			

			PreparedStatement pstmt2 = prepareStatement(
					  "select recog_pgm.curr_cd"
					+ " from recog_pgm"
					+ " where recog_pgm.recog_cd = ?");
			
			if (recog_cd != null) {
				pstmt2.setString(1, recog_cd);
			}
			else {
				pstmt2.setNull(1, Types.JAVA_OBJECT);
			}
			ResultSet rs2 = executeQuery(pstmt2);
			rs2.next();
			curr_cd = rs2.getString(1);
			pstmt2.close();			
			return curr_cd;
		}
	}

}