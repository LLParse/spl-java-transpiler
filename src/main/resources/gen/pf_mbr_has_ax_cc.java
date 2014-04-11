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

public class pf_mbr_has_ax_cc extends AbstractProcedure {

	public String execute(Integer mbr_id, Integer promo_id, String stay_type, Integer prop_id, Timestamp doa, Integer los, String rm_type, String srp_code, Double rm_revenue, Double fb_revenue, Double other_revenue, String curr_code) throws SQLException, ProcedureException {		
		
		Integer ax_cc_count;		
		ax_cc_count = null;		
		

		PreparedStatement pstmt1 = prepareStatement(
				  "select count(*)"
				+ " from mbr_cc"
				+ " where mbr_cc.mbr_id = ?"
				+ " and mbr_cc.cc_cd = \"AX\""
				+ " and mbr_cc.cc_encrypted_id != null");
		
		if (mbr_id != null) {
			pstmt1.setInt(1, mbr_id);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		ax_cc_count = rs1.getInt(1);
		pstmt1.close();		
		
		if (ax_cc_count.equals(0)) {			
			return "F";
		}		
		else {			
			return "T";
		}
	}

}