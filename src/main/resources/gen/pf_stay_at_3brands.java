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

public class pf_stay_at_3brands extends AbstractProcedure {

	public String execute(Integer acct_id, Integer promo_id, String stay_type, Integer prop_id, Timestamp doa, Integer los, String rm_type, String srp_code, Double rm_revenue, Double fb_revenue, Double other_revenue, String curr_code, String res_source, Integer stay_id) throws SQLException, ProcedureException {		
		
		
		String chain_id;		
		Integer chain_c;		
		Integer chain_q;		
		Integer chain_r;		
		Integer chain_z;		
		Integer awarded_3brands;		
		Timestamp sdate;		
		Timestamp edate;		
		Integer cust_id;		
		
		chain_id = null;
		sdate = null;
		edate = null;		
		chain_c = 0;		
		chain_q = 0;		
		chain_r = 0;		
		chain_z = 0;		
		awarded_3brands = 0;		
		cust_id = null;		
		
		//set debug file to '/tmp/pf_stay_at_3brands.trace';
		
		// First get the cust associated with stay
		cust_id = new get_pri_cust_id().execute(acct_id);		
		
		// Check if the member has been awarded 3 brand promo         
		
		// Increment the brand for this stay 
		// Obtain chain_id for this stay 

		PreparedStatement pstmt1 = prepareStatement(
				  "select p.chain_id"
				+ " from prop p"
				+ " where p.prop_id = ?");
		
		if (prop_id != null) {
			pstmt1.setInt(1, prop_id);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		chain_id = rs1.getString(1);
		pstmt1.close();		// incoming prop
		
		// Obtain start and end dates for this promo 

		PreparedStatement pstmt2 = prepareStatement(
				  "select pm.start_date, pm.stop_date"
				+ " from promo pm"
				+ " where pm.promo_id = ?");
		
		if (promo_id != null) {
			pstmt2.setInt(1, promo_id);
		}
		else {
			pstmt2.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs2 = executeQuery(pstmt2);
		rs2.next();
		sdate = rs2.getTimestamp(1);
		edate = rs2.getTimestamp(2);
		pstmt2.close();		// incoming promo
		
		
		//trace 'chain_id = ' || chain_id;
		
		if (chain_id.equals("C")) {			
			chain_c = 1;
		}		
		else if (chain_id.equals("Q")) {			
			chain_q = 1;
		}		
		else if (chain_id.equals("R")) {			
			chain_r = 1;
		}		
		else if (chain_id.equals("Z")) {			
			chain_z = 1;
		}		
		else {			
			return "F";
		}		
		
		
		// Obtain the number of distinct brands the member has stayed so far
		

		PreparedStatement pstmt3 = prepareStatement(
				  "select distinct p.chain_id"
				+ " from stay s, prop p"
				+ " where s.prop_id = p.prop_id"
				+ " and p.chain_id in (\"C\", \"Q\", \"R\", \"Z\")"
				+ " and s.stay_type in (\"N\", \"F\")"
				+ " and s.stay_status = \"S\""
				+ " and ("s.doa + s.los
				+ ") >= ?"
				+ " and s.doa <= ?"
				+ " and s.rm_revenue_pc > 0"
				+ " and s.cust_id = ?");
		
		if (sdate != null) {
			pstmt3.setObject(1, sdate);
		}
		else {
			pstmt3.setNull(1, Types.JAVA_OBJECT);
		}
		if (edate != null) {
			pstmt3.setObject(2, edate);
		}
		else {
			pstmt3.setNull(2, Types.JAVA_OBJECT);
		}
		if (cust_id != null) {
			pstmt3.setInt(3, cust_id);
		}
		else {
			pstmt3.setNull(3, Types.JAVA_OBJECT);
		}
		ResultSet rs3 = executeQuery(pstmt3);
		while (rs3.next()) {
			chain_id = rs3.getString(1);			
			
			//trace 'chain_id = ' || chain_id;
			
			if (chain_id.equals("C") && chain_c.equals(0)) {				
				chain_c = 1;
			}			
			if (chain_id.equals("Q") && chain_q.equals(0)) {				
				chain_q = 1;
			}			
			if (chain_id.equals("R") && chain_r.equals(0)) {				
				chain_r = 1;
			}			
			if (chain_id.equals("Z") && chain_z.equals(0)) {				
				chain_z = 1;
			}
		}
		pstmt3.close();		
		
		//trace 'chain_c = ' || chain_c;
		//trace 'chain_q = ' || chain_q;
		//trace 'chain_r = ' || chain_r;
		//trace 'chain_z = ' || chain_z;
		
		if (chain_c + chain_q + chain_r + chain_z >= 3) {			
			return "T";
		}		
		else {			
			return "F";
		}
	}

}