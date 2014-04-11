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

public class sync_acct extends AbstractProcedure {

	public void execute(Integer acct_id, String acct_status, String action) throws SQLException, ProcedureException {		
		
		String external_pgm;		
		String recog_cd;		
		Integer current_count;		
		
		current_count = 0;		
		external_pgm = null;		
		recog_cd = new get_recog_cd().execute(acct_id);		
		

		PreparedStatement pstmt1 = prepareStatement(
				  "select p.external_pgm"
				+ " from recog_pgm p"
				+ " where p.recog_cd = ?");
		
		if (recog_cd != null) {
			pstmt1.setString(1, recog_cd);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		external_pgm = rs1.getString(1);
		pstmt1.close();		
		
		if (external_pgm.equals("Y") || recog_cd.equals("EC")) {			
			return;
		}		
		
		if (action.equals("N") && !acct_status.equals("A")) {			
			return;
		}		
		
		if (action.equals("U") && (!acct_status.equals("A") && !acct_status.equals("I") && !acct_status.equals("T"))) {			
			return;
		}		
		
		if (action.equals("D") && (!acct_status.equals("I") && !acct_status.equals("T"))) {			
			return;
		}		
		

		PreparedStatement pstmt2 = prepareStatement(
				  "select count(*)"
				+ " from acct_sync_out aso"
				+ " where aso.acct_id = ?"
				+ " and aso.send_dtime <= current");
		
		if (acct_id != null) {
			pstmt2.setInt(1, acct_id);
		}
		else {
			pstmt2.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs2 = executeQuery(pstmt2);
		rs2.next();
		current_count = rs2.getInt(1);
		pstmt2.close();		
		
		if (current_count > 0) {			
			return;
		}		
		

		PreparedStatement pstmt3 = prepareInsert(
				  "insert into acct_sync_out (sync_id, acct_id, action, event_dtime, send_dtime)"
				+ " values (0, ?, ?, current, current)");
		if (acct_id != null) {
			pstmt3.setInt(1, acct_id);
		}
		else {
			pstmt3.setNull(1, Types.JAVA_OBJECT);
		}
		if (action != null) {
			pstmt3.setString(2, action);
		}
		else {
			pstmt3.setNull(2, Types.JAVA_OBJECT);
		}
		executeUpdate(pstmt3);
		pstmt3.close();
	}

}