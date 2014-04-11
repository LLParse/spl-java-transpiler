/* Generated on 03-01-2013 12:10:56 PM by SPLParser v0.9 */
package com.choicehotels.cis.spl.gen;

import java.lang.reflect.Method;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.joda.time.DateTime;import org.joda.time.LocalDate;

import com.choicehotels.cis.spl.AbstractProcedure;
import com.choicehotels.cis.spl.ProcedureException;

public class get_rev_status extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public Integer execute(Integer acct_trans_id) throws SQLException, ProcedureException {
        /*
         * (]$[) $RCSfile$:$Revision: 9979 $ | CDATE=$Date: 2006-07-21 13:40:55 -0700 (Fri, 21 Jul 2006) $ ~
         * 
         * 
         *       Copyright (C) 2002 Choice Hotels International, Inc.
         * 		    All Rights Reserved
         * 
         *   get_rev_status accepts one acct_trans_id and determines if reversal is 
         *   possible. If reversable 0 is returned. Otherwise a non-zero error code
         *   specifying the reason is set. The procedure rev_response( error_code)
         *   translates the result code into an english phrase.
         * 
         *   The following transactions may not be reversed:
         * 
         *   1. Transactions that have already been reversed.
         *   2. Transactions associated with an external partner that have been 
         *      resubmitted.
         *   3. Transactions for external partners that have been processed 
         *      (trans_status = 'A').
         *   4. Transactions associated with external partners that have been 
         *      submitted (trans_status = 'S').
         *   5. If transaction is a stay it must be 'N'ormal or 'F'olio. Also if the
         *      transaction is associated with a redemption stay it may not be 
         *      reversed as well.
         *   6. If transaction is a redemption it must not be fulfilled (awarded).
         *   7. Along with the above rules only transactions of type 'S', 'A', 'B'
         *      or 'R' are considered.
         *   8. Any reversal that would take the account negative.
         *   9. Any adjustment transaction associated with a cobranded call ticket.
         *   10. Any transaction affiliated with an account that is not active.
         */
        // local working variables

        Integer rev_acct_trans_id;
        Integer rsub_acct_trans_id;
        Integer acct_id;
        String trans_type;
        String stay_type;
        DateTime fulfillment_date;
        String external_pgm;
        String trans_status;
        Integer rev_error;
        Double trans_amount;
        Double acct_balance;
        Integer balance;
        Integer orig_stay_id;
        Integer redemption_id;
        Integer cust_call_id;
        String desc_class;
        String acct_status;
        // initialize variables

        rev_acct_trans_id = null;
        rsub_acct_trans_id = null;
        acct_id = null;
        trans_type = null;
        stay_type = null;
        fulfillment_date = null;
        external_pgm = null;
        trans_status = null;
        rev_error = 0;
        trans_amount = 0.0;
        acct_balance = 0.0;
        balance = null;
        orig_stay_id = null;
        redemption_id = null;
        cust_call_id = null;
        desc_class = null;
        acct_status = null;
        //set debug file to '/tmp/get_rev_status.trace';

        //trace on;

        // Get the acct_id and transaction type from the original transaction

        PreparedStatement pstmt1 = prepareStatement(
                  "select a.acct_id, a.trans_type, a.rev_acct_trans_id, a.rsub_acct_trans_id, a.trans_status, a.stay_id, a.redemption_id, a.cust_call_id, ac.acct_status"
                + " from acct_trans a, acct ac"
                + " where a.acct_trans_id = ?"
                + " and a.acct_id = ac.acct_id");
        
        if (acct_trans_id != null) {
            pstmt1.setInt(1, acct_trans_id);
        }
        else {
            pstmt1.setNull(1, Types.JAVA_OBJECT);
        }
        ResultSet rs1 = executeQuery(pstmt1);
        rs1.next();
        acct_id = rs1.getInt(1);
        trans_type = rs1.getString(2);
        rev_acct_trans_id = rs1.getInt(3);
        rsub_acct_trans_id = rs1.getInt(4);
        trans_status = rs1.getString(5);
        orig_stay_id = rs1.getInt(6);
        redemption_id = rs1.getInt(7);
        cust_call_id = rs1.getInt(8);
        acct_status = rs1.getString(9);
        pstmt1.close();
        rs1.close();
        // We can only reverse a transaction one time.

        if (rev_acct_trans_id != null) {
            return 1;
        }
        // Resubmitted transactions may not be reversed.

        if (rsub_acct_trans_id != null) {
            rev_error = 2;
        }
        // Next check for partner type. If external the transaction must not have

        // been applied or submitted for approval.

        external_pgm = new get_pgm_type().execute(acct_id);
        if (external_pgm.equals("Y")) {
            if (trans_status.equals("A")) {
                rev_error = 3;
            }
            if (trans_status.equals("S")) {
                rev_error = 4;
            }
        }
        // If the transaction is a stay, check that it is a normal or folio type only

        if (trans_type.equals("S")) {
            PreparedStatement pstmt2 = prepareStatement(
                      "select stay.stay_type"
                    + " from stay"
                    + " where stay.stay_id = ?");
            
            if (orig_stay_id != null) {
                pstmt2.setInt(1, orig_stay_id);
            }
            else {
                pstmt2.setNull(1, Types.JAVA_OBJECT);
            }
            ResultSet rs2 = executeQuery(pstmt2);
            rs2.next();
            stay_type = rs2.getString(1);
            pstmt2.close();
            rs2.close();
            if (stay_type != null) {
                if (!stay_type.equals("N") && !stay_type.equals("F")) {
                    rev_error = 5;
                }
            }
        }
        // If the transaction is a non-stay redemption check that it has not been fulfilled.

        if (trans_type.equals("R") && redemption_id != null) {
            PreparedStatement pstmt3 = prepareStatement(
                      "select redemption.fulfillment_date"
                    + " from redemption"
                    + " where redemption.redemption_id = ?");
            
            if (redemption_id != null) {
                pstmt3.setInt(1, redemption_id);
            }
            else {
                pstmt3.setNull(1, Types.JAVA_OBJECT);
            }
            ResultSet rs3 = executeQuery(pstmt3);
            rs3.next();
            fulfillment_date = new DateTime(rs3.getTimestamp(1).getTime());
            pstmt3.close();
            rs3.close();
            if (fulfillment_date != null) {
                rev_error = 6;
            }
        }
        // If the transaction is a stay redemption it can't be reversed

        if (trans_type.equals("R") && orig_stay_id != null) {
            rev_error = 5;
        }
        // Transaction must be a valid type

        if (!trans_type.equals("S") && !trans_type.equals("B") && !trans_type.equals("A") && !trans_type.equals("R")) {
            rev_error = 7;
        }
        // Make sure that the reversal does not put the acct balance to negative

        if (external_pgm.equals("N")) {
            if (rev_error.equals(0)) {
                trans_amount = new get_acct_trans_sum().execute(acct_trans_id);
                balance = new get_acct_bal().execute(acct_id);
                acct_balance = balance.doubleValue();
                if (acct_balance - trans_amount < 0.0) {
                    rev_error = 8;
                }
            }
        }
        // Check adjustments associated with a cobranded VISA or

        // ANY externally awarded point adjustment

        if (trans_type.equals("A")) {
            PreparedStatement pstmt4 = prepareStatement(
                      "select s.class"
                    + " from std_desc s, cust_call c"
                    + " where c.cust_call_id = ?"
                    + " and c.std_desc_id = s.std_desc_id");
            
            if (cust_call_id != null) {
                pstmt4.setInt(1, cust_call_id);
            }
            else {
                pstmt4.setNull(1, Types.JAVA_OBJECT);
            }
            ResultSet rs4 = executeQuery(pstmt4);
            rs4.next();
            desc_class = rs4.getString(1);
            pstmt4.close();
            rs4.close();
            if (desc_class.equals("CobrandTxn")) {
                rev_error = 9;
            }
            if (desc_class.equals("PartnerTxn")) {
                rev_error = 9;
            }
        }
        // Check the account status itself.

        if (!acct_status.equals("A")) {
            rev_error = 10;
        }
        return rev_error;
    }

}