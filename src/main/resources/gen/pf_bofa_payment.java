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

public class pf_bofa_payment extends AbstractProcedure {

	public String execute(Integer acct_id, Integer promo_id, String stay_type, Integer prop_id, Timestamp doa, Integer los, String rm_type, String srp_code, Double rm_revenue, Double fb_revenue, Double other_revenue, String curr_code, String res_source, Integer stay_id) throws SQLException, ProcedureException {		
		
		/*
		 * 	pf_bofa_payment.sql 	- Checks if payment method is a cobranded credit card.
		 * 							IdentUpdate sets the cobrand_ptr_id on the cust_ident 
		 * 							record if payment was made using a cobrand credit card.
		 * 
		 *     Copyright (C) 2005 Choice Hotels International, Inc.
		 *     (]$[) $RCSfile$:$Revision: 99 $ | CDATE=$Date: 2012-12-19 14:14:54 -0700 (Wed, 19 Dec 2012) $ ~
		 */		
		
		Integer bofa;		
		bofa = null;		
		

		PreparedStatement pstmt1 = prepareStatement(
				  "select i.cobrand_ptr_id"
				+ " from stay s, cust_ident i"
				+ " where s.cust_ident_id = i.cust_ident_id"
				+ " and s.stay_id = ?");
		
		if (stay_id != null) {
			pstmt1.setInt(1, stay_id);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		bofa = rs1.getInt(1);
		pstmt1.close();		
		
		if (bofa == null) {			
			return "F";
		}		
		
		return "T";
	}

}