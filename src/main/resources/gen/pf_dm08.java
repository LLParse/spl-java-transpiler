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

public class pf_dm08 extends AbstractProcedure {

	public String execute(Integer acct_id, Integer promo_id, String stay_type, Integer prop_id, Timestamp doa, Integer los, String rm_type, String srp_code, Double rm_revenue, Double fb_revenue, Double other_revenue, String curr_code, String res_source, Integer stay_id) throws SQLException, ProcedureException {		
		
		/*
		 * 	pf_dm08.sql	- Check that acct_offer date is before date of reservation but not
		 *     more than 48 hours after. 
		 * 	(]$[) $RCSfile$:$Revision: 99 $ | CDATE=$Date: 2012-12-19 14:14:54 -0700 (Wed, 19 Dec 2012) $ ~
		 */		
		
		Timestamp offer_date;		
		Timestamp res_date;		
		
		offer_date = null;		
		res_date = null;		
		
		//set debug file to '/tmp/pf_dm08.trace';
		//trace on;
		
		// get the reservation date

		PreparedStatement pstmt1 = prepareStatement(
				  "select reservation_date"
				+ " from stay"
				+ " where stay.stay_id = ?");
		
		if (stay_id != null) {
			pstmt1.setInt(1, stay_id);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		res_date = rs1.getTimestamp(1);
		pstmt1.close();		
		
		if (res_date == null) {			
			return "F";
		}		
		
		// there are potentially multiple offers so just pick
		// the earliest one

		PreparedStatement pstmt2 = prepareStatement(
				  "select date(min(a.offer_dtime))"
				+ " from promo_acct_elig p, acct_offer a"
				+ " where p.promo_id = ?"
				+ " and a.acct_id = ?"
				+ " and p.offer_id = a.offer_id");
		
		if (promo_id != null) {
			pstmt2.setInt(1, promo_id);
		}
		else {
			pstmt2.setNull(1, Types.JAVA_OBJECT);
		}
		if (acct_id != null) {
			pstmt2.setInt(2, acct_id);
		}
		else {
			pstmt2.setNull(2, Types.JAVA_OBJECT);
		}
		ResultSet rs2 = executeQuery(pstmt2);
		rs2.next();
		offer_date = rs2.getTimestamp(1);
		pstmt2.close();		
		
		if (offer_date != null) {			
			if (offer_date > res_date) {				
				return "F";
			}			
			if ((offer_date + 48hour) < res_date) {				
				return ("F");
			}
		}		
		
		return "T";
	}

}