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

public class pf_complete_stay_by_promo_end extends AbstractProcedure {

	public String execute(Integer acct_id, Integer promo_id, String stay_type, Integer prop_id, Timestamp doa, Integer los, String rm_type, String srp_code, Double rm_revenue, Double fb_revenue, Double other_revenue, String curr_code, String res_source, Integer stay_id) throws SQLException, ProcedureException {		
		
		/*
		 * 	pf_complete_stay_by_promo_end.sql 	Checks that date of departure occurs before promo ends.
		 * 
		 *     Copyright (C) 2005 Choice Hotels International, Inc.
		 *     (]$[) $RCSfile$:$Revision: 99 $ | CDATE=$Date: 2012-12-19 14:14:54 -0700 (Wed, 19 Dec 2012) $ ~
		 */		
		
		Timestamp end_date;		
		end_date = null;		
		

		PreparedStatement pstmt1 = prepareStatement(
				  "select stop_date"
				+ " from promo p"
				+ " where p.promo_id = ?");
		
		if (promo_id != null) {
			pstmt1.setInt(1, promo_id);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		end_date = rs1.getTimestamp(1);
		pstmt1.close();		
		
		if (doa + los > end_date) {			
			return "F";
		}		
		else {			
			return "T";
		}
	}

}