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

public class get_seq_nbr extends AbstractProcedure {

	public Integer execute(String seq_nbr_cd) throws SQLException, ProcedureException {		
		
		/*
		 * Get the next sequence number for the specified code.
		 */		
		
		Integer current_val;		
		Integer next_val;		
		Integer min_val;		
		Integer max_val;		
		Integer error_val;		
		String rollover;		
		
		current_val = null;		
		next_val = null;		
		min_val = null;		
		max_val = null;		
		error_val = null;		
		rollover = null;		
		

		PreparedStatement pstmt1 = prepareStatement(
				  "select seq_nbr_cntrl.next_val, seq_nbr_cntrl.min_val, seq_nbr_cntrl.max_val, seq_nbr_cntrl.error_val, seq_nbr_cntrl.rollover"
				+ " from seq_nbr_cntrl"
				+ " where seq_nbr_cntrl.seq_nbr_cd = ?");
		
		if (seq_nbr_cd != null) {
			pstmt1.setString(1, seq_nbr_cd);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		next_val = rs1.getInt(1);
		min_val = rs1.getInt(2);
		max_val = rs1.getInt(3);
		error_val = rs1.getInt(4);
		rollover = rs1.getString(5);
		pstmt1.close();		
		
		if (next_val.equals(error_val)) {			
			return error_val;
		}		
		
		current_val = next_val;		
		
		next_val = next_val + 1;		
		if (next_val > max_val) {			
			if (rollover.equals("Y")) {				
				next_val = min_val;
			}			
			else {				
				next_val = error_val;
			}
		}		
		

		PreparedStatement pstmt2 = prepareStatement(
				  "update seq_nbr_cntrl"
				+ " set seq_nbr_cntrl.next_val = ?"
				+ " where seq_nbr_cntrl.seq_nbr_cd = ?");
		
		if (next_val != null) {
			pstmt2.setInt(1, next_val);
		}
		else {
			pstmt2.setNull(1, Types.JAVA_OBJECT);
		}
		if (seq_nbr_cd != null) {
			pstmt2.setString(2, seq_nbr_cd);
		}
		else {
			pstmt2.setNull(2, Types.JAVA_OBJECT);
		}
		executeUpdate(pstmt2);
		pstmt2.close();		
		
		return current_val;
	}

}