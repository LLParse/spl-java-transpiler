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

public class pf_firststayever extends AbstractProcedure {

	public String execute(Integer acct_id, Integer promo_id, String stay_type, Integer prop_id, Timestamp doa, Integer los, String rm_type, String srp_code, Double rm_revenue, Double fb_revenue, Double other_revenue, String curr_code, String res_source, Integer stay_id) throws SQLException, ProcedureException {		
		
		/*
		 * 	(]$[) $RCSfile$:$Revision: 99 $ | CDATE=$Date: 2012-12-19 14:14:54 -0700 (Wed, 19 Dec 2012) $ ~
		 * 
		 * 	determine whether the member has stayed ever before?
		 * 
		 *        Copyright (C) 2000 Choice Hotels International, Inc.
		 */		
		Integer stay_count;		
		stay_count = null;		
		

		PreparedStatement pstmt1 = prepareStatement(
				  "select count(*)"
				+ " from acct_trans a, stay s, acct_trans_detail ad"
				+ " where a.stay_id = s.stay_id"
				+ " and a.trans_type = \"S\""
				+ " and a.acct_trans_id = ad.acct_trans_id"
				+ " and ad.amount != 0"
				+ " and a.rev_acct_trans_id is null"
				+ " and s.stay_type in (\"N\", \"F\")"
				+ " and s.stay_status = \"S\""
				+ " and a.acct_id = ?");
		
		if (acct_id != null) {
			pstmt1.setInt(1, acct_id);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		stay_count = rs1.getInt(1);
		pstmt1.close();		
		
		if (stay_count > 0) {			
			return "F";
		}		
		else {			
			return "T";
		}
	}

}