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

public class _apply_trans extends AbstractProcedure {

	public void execute(Integer acct_id, String external_pgm, Integer acct_trans_id, String trans_type, Double amount, String stay_type) throws SQLException, ProcedureException {		
		/*
		 * $RCSfile$- Apply amount from one acct_trans record to an account - $Revision: 99 $
		 * 
		 * (]$[) $RCSfile$:$Revision: 99 $ | CDATE=$Date: 2012-12-19 14:14:54 -0700 (Wed, 19 Dec 2012) $ ~
		 * 
		 *       Copyright (C) 2001 Choice Hotels International, Inc.
		 *                       All Rights Reserved
		 * 
		 * Apply amount from a transaction to a customers account. If external partner
		 * do nothing. If an internal customer, update the statement info as well as
		 * account expiration. If negative, consume the amount. This procedure must
		 * be called inside a transaction to ensure data integrity.
		 */		
		
		String sync;		
		Timestamp exp_date;		
		String recog_cd;		
		
		sync = null;		
		exp_date = null;		
		recog_cd = new get_recog_cd().execute(acct_id);		
		
		// set debug file to '/tmp/_apply_trans.trace';
		// trace on;
		
		// Apply amount to account based on partner type
		if (external_pgm.equals("N") && !recog_cd.equals("CS")) {			
			exp_date = new get_exp_date().execute(new get_recog_cd().execute(acct_id), new Timestamp(System.currentTimeMillis()));			
			
			new _upd_acct_exp().execute(acct_id, amount, acct_trans_id, exp_date);			
			
			new _upd_acct_stmnt().execute(acct_id, trans_type, amount, stay_type, "N");			
			
			sync = new sync_crs().execute(trans_type, acct_trans_id, recog_cd, external_pgm, stay_type);			
			if (sync.equals("Y")) {				
				new _ins_acct_trans_ou().execute(acct_trans_id, acct_id, trans_type, amount);
			}
		}		
		
		// Finally update the transaction status to 'A'pplied

		PreparedStatement pstmt1 = prepareStatement(
				  "update acct_trans"
				+ " set acct_trans.trans_status = \"A\", acct_trans.final_dtime = current"
				+ " where acct_trans.acct_trans_id = ?");
		
		if (acct_trans_id != null) {
			pstmt1.setInt(1, acct_trans_id);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		executeUpdate(pstmt1);
		pstmt1.close();
	}

}