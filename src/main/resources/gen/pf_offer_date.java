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

public class pf_offer_date extends AbstractProcedure {

	public String execute(Integer acct_id, Integer promo_id, String stay_type, Integer prop_id, Timestamp doa, Integer los, String rm_type, String srp_code, Double rm_revenue, Double fb_revenue, Double other_revenue, String curr_code, String res_source, Integer stay_id) throws SQLException, ProcedureException {		
		
		/*
		 * 	pf_offer_date.sql	- Check that acct_offer date is before the DOA
		 * 	(]$[) $RCSfile$:$Revision: 99 $ | CDATE=$Date: 2012-12-19 14:14:54 -0700 (Wed, 19 Dec 2012) $ ~
		 */		
		
		Timestamp offer_date;		
		
		offer_date = null;		
		
		// there are potentially multiple offers so just pick
		// the earliest one

		PreparedStatement pstmt1 = prepareStatement(
				  "select date(min(a.offer_dtime))"
				+ " from promo_acct_elig p, acct_offer a"
				+ " where p.promo_id = ?"
				+ " and a.acct_id = ?"
				+ " and p.offer_id = a.offer_id");
		
		if (promo_id != null) {
			pstmt1.setInt(1, promo_id);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		if (acct_id != null) {
			pstmt1.setInt(2, acct_id);
		}
		else {
			pstmt1.setNull(2, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		offer_date = rs1.getTimestamp(1);
		pstmt1.close();		
		
		if (offer_date != null) {			
			if (offer_date <= doa) {				
				return "T";
			}
		}		
		
		return "F";
	}

}