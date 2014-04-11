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

public class _upd_par extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public void execute(Integer acct_trans_id, String new_status, Integer par_result_msg_id, DateTime upd_dtime) throws SQLException, ProcedureException {
        /*
         * $RCSfile$ - Update a par record - $Revision: 18040 $
         * 
         * (]$[) $RCSfile$:$Revision: 18040 $ | CDATE=$Date: 2008-06-30 13:58:02 -0700 (Mon, 30 Jun 2008) $ ~
         * 
         *       Copyright (C) 2001 Choice Hotels International, Inc.
         *                       All Rights Reserved
         * 
         * This procedure updates the status of a partner activity request (par) record. 
         * Set the appropriate timers based on status setting. Status may only be
         * changed  as follows:
         * 
         * 'P'ending to 'S'ubmitted
         * 'S'ubmitted to either 'A'ccepted, or 'R'rejected 
         * 
         * Queue records do not revert to their initial or 'P'ending status. After 
         * at an accepted or rejected state, the only operation to perform is a delete.
         * 
         * This procedure does its work inside a transaction to ensure data integrity.
         */
        Integer par_id;
        String request_status;
        par_id = null;
        request_status = null;
        //set debug file to '/tmp/_upd_par.trace';

        //trace on;

        // Check that the transaction is already queuedand at a state for change.

        PreparedStatement pstmt1 = prepareStatement(
                  "select p.par_id, p.request_status"
                + " from par p"
                + " where p.acct_trans_id = ?");
        
        if (acct_trans_id != null) {
            pstmt1.setInt(1, acct_trans_id);
        }
        else {
            pstmt1.setNull(1, Types.JAVA_OBJECT);
        }
        ResultSet rs1 = executeQuery(pstmt1);
        rs1.next();
        par_id = rs1.getInt(1);
        request_status = rs1.getString(2);
        pstmt1.close();
        rs1.close();
        if (par_id == null) {
            throw new ProcedureException(-746, 0, "_upd_par: No par record to update.");
        }
        if (request_status.equals("R")) {
            throw new ProcedureException(-746, 0, "_upd_par: Status invalid for change.");
        }
        // Perform sanity checks

        if (new_status.equals("S") && !request_status.equals("P")) {
            throw new ProcedureException(-746, 0, "_upd_par: Status must be 'P'ending to 'S'ubmit.");
        }
        if (new_status.equals("A") && !request_status.equals("S")) {
            throw new ProcedureException(-746, 0, "_upd_par: Status must be 'S'ubmitted to change to 'A'ccept or 'R'eject.");
        }
        // Transaction is being submitted, set xmit_dtime

        if (new_status.equals("S")) {
            PreparedStatement pstmt2 = prepareStatement(
                      "update par"
                    + " set par.xmit_dtime = current, par.request_status = ?, par.last_update_dtime = current"
                    + " where par.acct_trans_id = ?");
            
            if (new_status != null) {
                pstmt2.setString(1, new_status);
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
        }
        // Transaction is being rejected set rcv_dtime and reason code.

        if (new_status.equals("R")) {
            PreparedStatement pstmt3 = prepareStatement(
                      "update par"
                    + " set par.rcv_dtime = ?, par.request_status = ?, par.par_result_msg_id = ?, par.last_update_dtime = current"
                    + " where par.acct_trans_id = ?");
            
            if (upd_dtime != null) {
                pstmt3.setObject(1, upd_dtime);
            }
            else {
                pstmt3.setNull(1, Types.JAVA_OBJECT);
            }
            if (new_status != null) {
                pstmt3.setString(2, new_status);
            }
            else {
                pstmt3.setNull(2, Types.JAVA_OBJECT);
            }
            if (par_result_msg_id != null) {
                pstmt3.setInt(3, par_result_msg_id);
            }
            else {
                pstmt3.setNull(3, Types.JAVA_OBJECT);
            }
            if (acct_trans_id != null) {
                pstmt3.setInt(4, acct_trans_id);
            }
            else {
                pstmt3.setNull(4, Types.JAVA_OBJECT);
            }
            executeUpdate(pstmt3);
            pstmt3.close();
        }
        // Transaction was accepted set rcv_dtime

        if (new_status.equals("A")) {
            PreparedStatement pstmt4 = prepareStatement(
                      "update par"
                    + " set par.rcv_dtime = ?, par.request_status = ?, par.last_update_dtime = current"
                    + " where par.acct_trans_id = ?");
            
            if (upd_dtime != null) {
                pstmt4.setObject(1, upd_dtime);
            }
            else {
                pstmt4.setNull(1, Types.JAVA_OBJECT);
            }
            if (new_status != null) {
                pstmt4.setString(2, new_status);
            }
            else {
                pstmt4.setNull(2, Types.JAVA_OBJECT);
            }
            if (acct_trans_id != null) {
                pstmt4.setInt(3, acct_trans_id);
            }
            else {
                pstmt4.setNull(3, Types.JAVA_OBJECT);
            }
            executeUpdate(pstmt4);
            pstmt4.close();
        }
    }

}