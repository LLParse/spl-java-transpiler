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

public class add_redeem_award extends AbstractProcedure {

	public Collection<Object> execute(Integer acct_id, Integer cust_id, Integer partner_acct_id, Integer entry_id, Integer award_id, String express_mail, Integer orig_acct_trans_id, Double redeem_amt_in, Integer promo_id_in) throws SQLException, ProcedureException {		
		
		/*
		 * $Id: add_redeem_award.sql 99 2012-12-19 21:14:54Z michael_giroux $
		 * 
		 *  Process an award redemption transaction. 
		 *  Returns acct_trans_id and redemption_id on success.
		 * 
		 * 	Copyright (C) 2002 Choice Hotels International, Inc.
		 *                       All Rights Reserved
		 */		
		
		// Data from new records/calculations
		Integer int_acct_trans_id;		
		Integer int_detail_id;		
		Integer int_redemption_id;		
		Integer ext_acct_trans_id;		
		Integer ext_detail_id;		
		Integer ext_redemption_id;		
		String external_pgm;		
		Double redeem_amt;		
		Double partner_award_amt;		
		String partner_recog_cd;		
		String express_mail_ind;		
		String orig_award_cd;		
		String notes;		
		Integer std_desc_id;		
		Integer rev_acct_trans_id;		
		Integer asc_acct_trans_id;		
		String val_cust_acct;		
		Integer dummy;		//throw-away value
		
		//- account table
		String acct_status;		
		String recog_cd;		
		
		Integer sql_error;		
		Integer isam_error;		
		String error_data;		
		
		// Initialize variables
		int_acct_trans_id = null;		
		int_detail_id = null;		
		int_redemption_id = null;		
		ext_acct_trans_id = null;		
		ext_detail_id = null;		
		ext_redemption_id = null;		
		external_pgm = null;		
		acct_status = null;		
		recog_cd = null;		
		redeem_amt = null;		
		partner_award_amt = null;		
		partner_recog_cd = null;		
		express_mail_ind = null;		
		orig_award_cd = null;		
		notes = null;		
		std_desc_id = null;		
		rev_acct_trans_id = null;		
		asc_acct_trans_id = null;		
		val_cust_acct = null;		
		
		//set debug file to '/tmp/add_redeem_award.trace';
		//trace on;
		
		//----------------------------------------------------------
		// First determine that the member is at an 'A'ctive status
		//----------------------------------------------------------
		acct_status = new get_acct_status().execute(acct_id);		
		if (acct_status != "A") {			
			throw new ProcedureException(-746, 0, "add_redeem_award: Account status is not 'A'ctive.");
		}		
		
		recog_cd = new get_recog_cd().execute(acct_id);		
		//----------------------------------------------------------
		// Next convert CRS property and get members' currrency
		//----------------------------------------------------------
		// Validate customer and account relationship
		val_cust_acct = new chk_cust_acct().execute(acct_id, cust_id);		
		if (val_cust_acct.equals("N")) {			
			throw new ProcedureException(-746, 0, "add_redeem_award: Customer not linked to account.");
		}		
		
		//----------------------------------------------------------
		// Redemption can only be made for internal partners.
		//----------------------------------------------------------
		external_pgm = new get_pgm_type().execute(acct_id);		
		if (!external_pgm.equals("N")) {			
			throw new ProcedureException(-746, 0, "add_redeem_award: Redemption valid only for internal accounts.");
		}		
		
		//----------------------------------------------------------
		// Validate the express mail indicator.
		//----------------------------------------------------------
		if (!express_mail.equals("Y") && !express_mail.equals("N")) {			
			throw new ProcedureException(-746, 0, "add_redeem_award: Express_mail must be set to 'Y' or 'N'");
		}		
		
		//----------------------------------------------------------
		// Look up the award info.
		//----------------------------------------------------------

		PreparedStatement pstmt1 = prepareStatement(
				  "select award.redeem_amt, award.partner_award_amt, recog_pgm.recog_cd, award.express_mail"
				+ " from award,  outer (recog_pgm)"
				+ " where award.award_id = ?"
				+ " and recog_pgm.partner_id = award.partner_id");
		
		if (award_id != null) {
			pstmt1.setInt(1, award_id);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		redeem_amt = rs1.getDouble(1);
		partner_award_amt = rs1.getDouble(2);
		partner_recog_cd = rs1.getString(3);
		express_mail_ind = rs1.getString(4);
		pstmt1.close();		
		
		//----------------------------------------------------------
		// Validate the parnter account id.
		//----------------------------------------------------------
		if (partner_acct_id != null) {			
			if (!new get_recog_cd().execute(partner_acct_id).equals(partner_recog_cd)) {				
				throw new ProcedureException(-746, 0, "add_redeem_award: partner account does not match award partner");
			}
		}		
		
		//----------------------------------------------------------
		// Check if express mail is available for award.  If not but 
		// the request is yes, reject the redemption.
		//----------------------------------------------------------
		if (express_mail_ind.equals("N") && express_mail.equals("Y")) {			
			throw new ProcedureException(-746, 0, "add_redeem_award: Express_mail is not available for selected award");
		}		
		
		//----------------------------------------------------------
		// The redemption is valid, check if it is a resubmitted one.
		// If so, the original transaction set needs to be reversed.
		//----------------------------------------------------------
		if (orig_acct_trans_id != null) {			
			// We have to reverse the original to restore points to
			// apply to the new redemption transaction.

			PreparedStatement pstmt2 = prepareStatement(
					  "select aw.award_cd, a.asc_acct_trans_id"
					+ " from award aw, redemption r, acct_trans a"
					+ " where a.acct_trans_id = ?"
					+ " and a.redemption_id = r.redemption_id"
					+ " and r.award_id = aw.award_id");
			
			if (orig_acct_trans_id != null) {
				pstmt2.setInt(1, orig_acct_trans_id);
			}
			else {
				pstmt2.setNull(1, Types.JAVA_OBJECT);
			}
			ResultSet rs2 = executeQuery(pstmt2);
			rs2.next();
			orig_award_cd = rs2.getString(1);
			asc_acct_trans_id = rs2.getInt(2);
			pstmt2.close();			
			
			// First the resubmitted  one.
			notes = "Resubmission of award: " + orig_award_cd;			
			std_desc_id = new get_std_desc_id().execute(partner_recog_cd, "MemberCall", "CallTicket", "RESUBAW");			
			rev_acct_trans_id = new _reverse_trans().execute(orig_acct_trans_id, entry_id, std_desc_id, notes);			
			
			// Next the internal transaction to restore points.
			notes = "Reinstatement of points for resubmitted award: " + orig_award_cd;			
			std_desc_id = new get_std_desc_id().execute(recog_cd, "MemberCall", "CallTicket", "RESUBAW");			
			rev_acct_trans_id = new _reverse_trans().execute(asc_acct_trans_id, entry_id, std_desc_id, notes);
		}		
		
		//----------------------------------------------------------
		// Post the transaction to the internal program.
		//----------------------------------------------------------
		int_redemption_id = new _add_redemption().execute(acct_id, cust_id, entry_id, entry_id, null, award_id, express_mail, promo_id_in);		
		
		int_acct_trans_id = new _post_trans().execute(acct_id, cust_id, "R", null, null, int_redemption_id, redeem_amt_in, null);		
		
		int_detail_id = new _ins_acct_trans_de().execute(int_acct_trans_id, 1, redeem_amt_in, null, "N");		
		
		
		//----------------------------------------------------------
		// If the award is a partner award post the transaction to
		// the external partner program and link the internal and
		// external transactions.
		//----------------------------------------------------------
		if (partner_acct_id != null) {			
			
			ext_redemption_id = new _add_redemption().execute(partner_acct_id, cust_id, entry_id, entry_id, null, award_id, express_mail, promo_id_in);			
			
			ext_acct_trans_id = new _post_trans().execute(partner_acct_id, cust_id, "R", null, null, ext_redemption_id, partner_award_amt, null);			
			
			ext_detail_id = new _ins_acct_trans_de().execute(ext_acct_trans_id, 1, partner_award_amt, null, "N");			
			
			new _link_asc_trans().execute(int_acct_trans_id, ext_acct_trans_id);			
			
			if (orig_acct_trans_id != null) {				
				dummy = new link_rsub_trans().execute(orig_acct_trans_id, ext_acct_trans_id);
			}
		}		
		
		// Return success
		return new ArrayList<Object>(Arrays.<Object>asList(int_acct_trans_id, int_redemption_id));
	}

}