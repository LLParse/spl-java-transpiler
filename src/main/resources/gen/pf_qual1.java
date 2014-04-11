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

public class pf_qual1 extends AbstractProcedure {

	public String execute(Integer mbr_id, String promo_code, String stay_type, String prop, Timestamp doa, Integer los, String rm_type, String srp_code, Double rm_revenue, String curr_code, Double rm_revenue_usd) throws SQLException, ProcedureException {		
		
		Integer chk_promo_cd;		
		Integer chk_offer_cd;		
		chk_promo_cd = null;
		chk_offer_cd = null;		
		
		//update statistics for procedure pf_qual1;
		
		
		
		setDebugFile("/tmp/pf_qual1.trace");		
		
		// Check if the member has been awarded activation bonus already. 
		
		//trace 'mbr_id = ' || mbr_id;

		PreparedStatement pstmt1 = prepareStatement(
				  "select count(*)"
				+ " from mbr_bonus mb"
				+ " where mb.promo_cd = \"QUAL1\""
				+ " and mb.mbr_id = ?");
		
		if (mbr_id != null) {
			pstmt1.setInt(1, mbr_id);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		chk_promo_cd = rs1.getInt(1);
		pstmt1.close();		
		
		//trace 'chk_promo_cd = ' || chk_promo_cd;
		if (chk_promo_cd > 0) {			
			return "F";
		}		
		else {			
			return "T";
		}
	}

}