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

public class sync_crs extends AbstractProcedure {

	public String execute(String trans_type, Integer acct_trans_id, String recog_cd, String external_pgm, String stay_type) throws SQLException, ProcedureException {		
		
		/*
		 * $RCSfile$ - Synchronize transaction with the CRS? - $Revision: 99 $
		 * 
		 * (]$[) $RCSfile$:$Revision: 99 $ | CDATE=$Date: 2012-12-19 14:14:54 -0700 (Wed, 19 Dec 2012) $ ~
		 * 
		 *   Returns 'Y' or 'N' depending on it the transaction award amount is to be
		 *   sent to the crs by inserting an entry in the acct_trans_out table. The
		 *   rules are as follows:
		 * 
		 *   Partner associated with transaction must be an internal partner.
		 * 
		 *   Trans_type 
		 *   
		 *   'S' - yes if associated stay_type is type 'N'ormal or 'F'olio
		 *   'R' - yes on all redemptions
		 *   'A' - yes on all adjustments, unless it was an SRD booking override where
		 *         CIS added points to the account to let the booking succeed.
		 *   'E' - yes on all account expirations
		 *   'V' - yes on all reversals
		 */		
		
		String sync;		
		String acct_status;		
		String visible;		
		String l_std_desc_cd;		
		
		sync = "N";		// Default to not sync CRS
		acct_status = null;		
		visible = null;		
		l_std_desc_cd = null;		
		

		PreparedStatement pstmt1 = prepareStatement(
				  "select r.externally_visible"
				+ " from recog_pgm r"
				+ " where r.recog_cd = ?");
		
		if (recog_cd != null) {
			pstmt1.setString(1, recog_cd);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		visible = rs1.getString(1);
		pstmt1.close();		
		
		
		// Only consider transactions associated with internal partners
		// and are externally visible                                                                                
		if (external_pgm.equals("N") && visible.equals("Y")) {			
			

			PreparedStatement pstmt2 = prepareStatement(
					  "select a.acct_status"
					+ " from acct a, acct_trans t"
					+ " where t.acct_trans_id = ?"
					+ " and a.acct_id = t.acct_id");
			
			if (acct_trans_id != null) {
				pstmt2.setInt(1, acct_trans_id);
			}
			else {
				pstmt2.setNull(1, Types.JAVA_OBJECT);
			}
			ResultSet rs2 = executeQuery(pstmt2);
			rs2.next();
			acct_status = rs2.getString(1);
			pstmt2.close();			
			
			if (trans_type.equals("S")) {				
				// Omit only transactions associated with non-qualifying stay.
				if (!stay_type.equals("X")) {					
					sync = "Y";
				}
			}			// trans_type is 'S'tay
			
			// Sync redemptions if not associated with a stay
			if (trans_type.equals("R") && stay_type == null) {				
				sync = "Y";
			}			
			
			if (trans_type.equals("A") || trans_type.equals("E") || trans_type.equals("B")) {				
				sync = "Y";				
				// see if this was an SRD points adjustment transaction, do not sync in that case
				if (trans_type.equals("A")) {					
					// get std_desc_cd based on the call ticket in the acct_trans record

					PreparedStatement pstmt3 = prepareStatement(
							  "select std_desc_cd"
							+ " from std_desc"
							+ " where std_desc_id = ("
								+ "select c.std_desc_id"
								+ " from cust_call c"
								+ " where c.cust_call_id = ("
									+ "select a.cust_call_id"
									+ " from acct_trans a"
									+ " where a.acct_trans_id = ?"
								+ ")"
							+ ")");
					
					if (acct_trans_id != null) {
						pstmt3.setInt(1, acct_trans_id);
					}
					else {
						pstmt3.setNull(1, Types.JAVA_OBJECT);
					}
					ResultSet rs3 = executeQuery(pstmt3);
					rs3.next();
					l_std_desc_cd = rs3.getString(1);
					pstmt3.close();					
					if (l_std_desc_cd != null && l_std_desc_cd.equals("SRDOVR")) {						
						sync = "N";
					}
				}
			}
		}		// End if internal partner
		
		return sync;
	}

}