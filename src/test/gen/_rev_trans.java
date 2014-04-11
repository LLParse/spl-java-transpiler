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

public class _rev_trans extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public Integer execute(Integer acct_trans_id, Integer cust_call_id, String force_rev) throws SQLException, ProcedureException {
        /*
         * (]$[) $RCSfile$:$Revision: 12170 $ | CDATE=$Date: 2007-06-28 08:43:03 -0700 (Thu, 28 Jun 2007) $ ~
         * 
         *       Copyright (C) 2002 Choice Hotels International, Inc.
         *                     All Rights Reserved
         * 
         *   _rev_trans accepts one acct_trans_id and, if valid attempts to reverse the 
         *   transaction by creating an equal one with opposite point value. Depending 
         *   on partner type, the account will be updated accordingly:
         * 
         *   External partners will merely attempt to dequeue the transaction with _del_par
         *   verifying the transmission status of the transaction.
         * 
         *   Internal partners will update the acct_stmnt and acct_exp tables by
         *   removing the original transaction values.
         *    
         *   Returns the new transaction ID if successful, an exception if not.
         */
        //Data from the acct_trans record

        Integer acct_id;
        Double amount;
        Integer rev_acct_trans_id;
        String trans_type;
        Integer detail_id;
        String external_pgm;
        String stay_type;
        DateTime trans_date;
        Integer orig_stay_id;
        Integer orig_cust_call_id;
        Integer orig_redemption_id;
        Integer rev_status;
        Integer upd_count;
        //initialize variables

        acct_id = null;
        amount = null;
        rev_acct_trans_id = null;
        trans_type = null;
        detail_id = null;
        external_pgm = null;
        stay_type = null;
        trans_date = null;
        orig_stay_id = null;
        orig_cust_call_id = null;
        orig_redemption_id = null;
        rev_status = 0;
        upd_count = null;
        // set debug file to '/tmp/_rev_trans.trace';

        // trace on;

        if (!force_rev.equals("Y")) {
            rev_status = new get_rev_status().execute(acct_trans_id);
            if (rev_status > 0) {
                throw new ProcedureException(-746, 0, "_rev_trans: Transaction is not reversable.");
            }
        }
        // Get amount of original transaction

        amount = new get_acct_trans_sum().execute(acct_trans_id);
        PreparedStatement pstmt1 = prepareStatement(
                  "select acct_trans.acct_id, acct_trans.rev_acct_trans_id, acct_trans.trans_type, acct_trans.init_dtime, stay_id, ?, redemption_id"
                + " from acct_trans"
                + " where acct_trans.acct_trans_id = ?");
        
        if (cust_call_id != null) {
            pstmt1.setInt(1, cust_call_id);
        }
        else {
            pstmt1.setNull(1, Types.JAVA_OBJECT);
        }
        if (acct_trans_id != null) {
            pstmt1.setInt(2, acct_trans_id);
        }
        else {
            pstmt1.setNull(2, Types.JAVA_OBJECT);
        }
        ResultSet rs1 = executeQuery(pstmt1);
        rs1.next();
        acct_id = rs1.getInt(1);
        rev_acct_trans_id = rs1.getInt(2);
        trans_type = rs1.getString(3);
        trans_date = new DateTime(rs1.getTimestamp(4).getTime());
        orig_stay_id = rs1.getInt(5);
        orig_cust_call_id = rs1.getInt(6);
        orig_redemption_id = rs1.getInt(7);
        pstmt1.close();
        rs1.close();
        // Invert the amount

        amount = -amount;
        //Generate the reverse transaction and detail records

        rev_acct_trans_id = new _ins_acct_trans().execute(acct_id, "V", "P", null, cust_call_id, null);
        detail_id = new _ins_acct_trans_de().execute(rev_acct_trans_id, 1, amount, null, "N");
        //Update the original transaction with the reverse transaction id.

        PreparedStatement pstmt2 = prepareStatement(
                  "update acct_trans"
                + " set acct_trans.rev_acct_trans_id = ?"
                + " where acct_trans.acct_trans_id = ?"
                + " and acct_trans.rev_acct_trans_id is null");
        
        if (rev_acct_trans_id != null) {
            pstmt2.setInt(1, rev_acct_trans_id);
        }
        else {
            pstmt2.setNull(1, Types.JAVA_OBJECT);
        }
        if (acct_trans_id != null) {
            pstmt2.setInt(2, acct_trans_id);
        }
        else {
            pstmt2.setNull(2, Types.JAVA_OBJECT);
        }
        executeUpdate(pstmt2);
        pstmt2.close();
        upd_count = dbinfo("sqlca.sqlerrd2");
        if (!upd_count.equals(1)) {
            throw new ProcedureException(-746, 0, "_rev_trans: Transaction is already reversed.");
        }
        // Update the account partner queue depending on program type

        external_pgm = new get_pgm_type().execute(acct_id);
        if (external_pgm.equals("Y")) {
            // Externals don't get queued.

            new _del_par().execute(acct_trans_id);
            PreparedStatement pstmt3 = prepareStatement(
                      "update acct_trans"
                    + " set acct_trans.final_dtime = current, acct_trans.trans_status = \"A\""
                    + " where acct_trans.acct_trans_id = ?");
            
            if (acct_trans_id != null) {
                pstmt3.setInt(1, acct_trans_id);
            }
            else {
                pstmt3.setNull(1, Types.JAVA_OBJECT);
            }
            executeUpdate(pstmt3);
            pstmt3.close();
        }
        else {
            if (trans_type.equals("S")) {
                PreparedStatement pstmt4 = prepareStatement(
                          "select stay.stay_type"
                        + " from stay"
                        + " where stay.stay_id = ?");
                
                if (orig_stay_id != null) {
                    pstmt4.setInt(1, orig_stay_id);
                }
                else {
                    pstmt4.setNull(1, Types.JAVA_OBJECT);
                }
                ResultSet rs2 = executeQuery(pstmt4);
                rs2.next();
                stay_type = rs2.getString(1);
                pstmt4.close();
                rs2.close();
            }
        }
        // Finalize all reversing transactions regardless of program.

        new _apply_trans().execute(acct_id, external_pgm, rev_acct_trans_id, trans_type, amount, stay_type);
        return rev_acct_trans_id;
    }

}