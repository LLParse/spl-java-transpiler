/* Generated on 02-25-2013 12:49:24 PM by SPLParser v0.9 */
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

public class pf_F99PC extends AbstractProcedure {

	public String execute(Integer mbr_id, String promo_code, String stay_type, String prop, Timestamp doa, Integer los, String rm_type, String srp_code, Double rm_revenue, String curr_code, Double rm_revenue_usd) throws SQLException, ProcedureException {		
		
		Integer chk_promo_cd;		
		Integer chk_offer_cd;		
		//update statistics for procedure pf_F99PC;
		
		
		
		//set debug file to '/tmp/pf_F99PC.trace';
		
		// Check if the member has been awarded activation bonus already. 
		
		//trace 'mbr_id = ' || mbr_id;

		PreparedStatement pstmt1 = prepareStatement(
				  "select count(*)"
				+ " from mbr_bonus mb"
				+ " where mb.promo_cd = \"F99PC\""
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
		
		
		// Determine if the member has been offered the promo  F99PC
		// Obtain required parameters
		

		PreparedStatement pstmt2 = prepareStatement(
				  "select count(*)"
				+ " from mbr_offer"
				+ " where mbr_offer.mbr_id = ?"
				+ " and mbr_offer.offer_cd in (\"F99PC1\", \"F99PC2\", \"F99PC3\", \"F99PC4\")");
		
		if (mbr_id != null) {
			pstmt2.setInt(1, mbr_id);
		}
		else {
			pstmt2.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs2 = executeQuery(pstmt2);
		rs2.next();
		chk_offer_cd = rs2.getInt(1);
		pstmt2.close();		
		
		//trace 'chk_offer_cd = ' || chk_offer_cd;
		
		if (chk_offer_cd <= 0) {			
			return "F";
		}		
		else {			
			return "T";
		}
	}

}