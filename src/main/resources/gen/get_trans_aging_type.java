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

public class get_trans_aging_type extends AbstractProcedure {

	public Collection<Object> execute(Integer acct_trans_id_in, Double amount_in) throws SQLException, ProcedureException {		
		
		/*
		 * $RCSfile$ - Determine points aging type - $Revision: 99 $
		 * 
		 *               Copyright (C) 2001 Choice Hotels International, Inc.
		 *                               All Rights Reserved
		 */		
		
		Integer l_redemption_id;		
		String l_trans_type;		
		String l_stay_status;		
		Integer l_rest_acct_trans_id;		
		
		l_redemption_id = null;		
		l_trans_type = null;		
		l_stay_status = null;		
		l_rest_acct_trans_id = null;		
		

		PreparedStatement pstmt1 = prepareStatement(
				  "select redemption_id, trans_type, stay_status"
				+ " from acct_trans a,  outer (stay s)"
				+ " where acct_trans_id = ?"
				+ " and a.stay_id = s.stay_id");
		
		if (acct_trans_id_in != null) {
			pstmt1.setInt(1, acct_trans_id_in);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		l_redemption_id = rs1.getInt(1);
		l_trans_type = rs1.getString(2);
		l_stay_status = rs1.getString(3);
		pstmt1.close();		
		
		if (l_trans_type.equals("V")) {			

			PreparedStatement pstmt2 = prepareStatement(
					  "select acct_trans_id"
					+ " from acct_trans"
					+ " where rev_acct_trans_id = ?");
			
			if (acct_trans_id_in != null) {
				pstmt2.setInt(1, acct_trans_id_in);
			}
			else {
				pstmt2.setNull(1, Types.JAVA_OBJECT);
			}
			ResultSet rs2 = executeQuery(pstmt2);
			rs2.next();
			l_rest_acct_trans_id = rs2.getInt(1);
			pstmt2.close();			
			
			return new ArrayList<Object>(Arrays.<Object>asList("restore", l_rest_acct_trans_id));
		}		
		
		if (l_stay_status.equals("X") && l_trans_type.equals("R")) {			

			PreparedStatement pstmt3 = prepareStatement(
					  "select a0.acct_trans_id"
					+ " from acct_trans a0"
					+ " where a0.stay_id = ("					

						+ "select max(s_srd.stay_id)"
						+ " from stay s_cancel"
						+ "  inner join acct_trans a_cancel on s_cancel.stay_id = a_cancel.stay_id, stay s_srd"
						+ "  inner join acct_trans a_srd on s_srd.stay_id = a_srd.stay_id"
						+ " where s_srd.stay_id < s_cancel.stay_id"
						+ " and s_srd.crs_conf_nbr = s_cancel.crs_conf_nbr"
						+ " and s_srd.doa = s_cancel.doa"
						+ " and a_cancel.acct_trans_id = ?"
						+ " and s_srd.stay_type = \"R\""
						+ " and s_srd.stay_status = \"S\""
					+ ")");
			
			if (acct_trans_id_in != null) {
				pstmt3.setInt(1, acct_trans_id_in);
			}
			else {
				pstmt3.setNull(1, Types.JAVA_OBJECT);
			}
			ResultSet rs3 = executeQuery(pstmt3);
			rs3.next();
			l_rest_acct_trans_id = rs3.getInt(1);
			pstmt3.close();			
			
			if (l_rest_acct_trans_id == null) {				
				return new ArrayList<Object>(Arrays.<Object>asList("redemption", null));
			}			
			
			return new ArrayList<Object>(Arrays.<Object>asList("restore", l_rest_acct_trans_id));
		}		
		
		if (l_trans_type.equals("R")) {			
			return new ArrayList<Object>(Arrays.<Object>asList("redemption", null));
		}		
		
		if (amount_in < 0.0) {			
			return new ArrayList<Object>(Arrays.<Object>asList("consume", null));
		}		
		
		if (amount_in > 0.0) {			
			return new ArrayList<Object>(Arrays.<Object>asList("accumulate", null));
		}
	}

}