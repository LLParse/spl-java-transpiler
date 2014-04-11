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

public class link_rsub_tckts extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public Integer execute(Integer orig_acct_trans_id, Integer new_acct_trans_id) throws SQLException, ProcedureException {
        /*
         * (]$[) $RCSfile$:$Revision: 5804 $ | CDATE=$Date: 2004-12-13 04:10:28 -0700 (Mon, 13 Dec 2004) $ ~
         * 
         *       Copyright (C) 2002 Choice Hotels International, Inc.
         * 		    All Rights Reserved
         * 
         * 
         *   Link call ticket from orig transaction to new, resubmitted transaction.
         */
        Integer cust_call_id;
        Integer cust_id;
        Integer acct_id;
        DateTime call_dtime;
        String call_status;
        String severity;
        Integer std_desc_id;
        String problem_desc;
        Integer prop_id;
        Integer entry_id;
        DateTime last_update_dtime;
        Integer last_update_id;
        Integer orig_cust_call_id;
        Integer ord;
        String notes;
        cust_call_id = null;
        cust_id = null;
        acct_id = null;
        call_dtime = null;
        call_status = null;
        severity = null;
        std_desc_id = null;
        problem_desc = null;
        prop_id = null;
        entry_id = null;
        last_update_dtime = null;
        last_update_id = null;
        orig_cust_call_id = null;
        ord = null;
        notes = null;
        //set debug file to '/tmp/link_rsub_tckts.trace';

        //trace on;

        // First make a copy of the old ticket.

        PreparedStatement pstmt1 = prepareStatement(
                  "select c.cust_call_id, c.cust_id, c.acct_id, c.call_dtime, c.call_status, c.severity, c.std_desc_id, c.problem_desc, c.prop_id, c.entry_id, c.last_update_dtime, c.last_update_id"
                + " from cust_call c"
                + " where c.acct_trans_id = ?");
        
        if (orig_acct_trans_id != null) {
            pstmt1.setInt(1, orig_acct_trans_id);
        }
        else {
            pstmt1.setNull(1, Types.JAVA_OBJECT);
        }
        ResultSet rs1 = executeQuery(pstmt1);
        rs1.next();
        orig_cust_call_id = rs1.getInt(1);
        cust_id = rs1.getInt(2);
        acct_id = rs1.getInt(3);
        call_dtime = new DateTime(rs1.getTimestamp(4).getTime());
        call_status = rs1.getString(5);
        severity = rs1.getString(6);
        std_desc_id = rs1.getInt(7);
        problem_desc = rs1.getString(8);
        prop_id = rs1.getInt(9);
        entry_id = rs1.getInt(10);
        last_update_dtime = new DateTime(rs1.getTimestamp(11).getTime());
        last_update_id = rs1.getInt(12);
        pstmt1.close();
        rs1.close();
        // There had better be one

        if (cust_id == null) {
            throw new ProcedureException(-746, 0, "link_rsub_tckts: Original transaction has no call ticket");
        }
        PreparedStatement pstmt2 = prepareInsert(
                  "insert into cust_call (cust_call_id, cust_id, acct_id, call_dtime, call_status, severity, std_desc_id, problem_desc, acct_trans_id, prop_id, entry_id, last_update_dtime, last_update_id)"
                + " values (0, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        if (cust_id != null) {
            pstmt2.setInt(1, cust_id);
        }
        else {
            pstmt2.setNull(1, Types.JAVA_OBJECT);
        }
        if (acct_id != null) {
            pstmt2.setInt(2, acct_id);
        }
        else {
            pstmt2.setNull(2, Types.JAVA_OBJECT);
        }
        if (call_dtime != null) {
            pstmt2.setObject(3, call_dtime);
        }
        else {
            pstmt2.setNull(3, Types.JAVA_OBJECT);
        }
        if (call_status != null) {
            pstmt2.setString(4, call_status);
        }
        else {
            pstmt2.setNull(4, Types.JAVA_OBJECT);
        }
        if (severity != null) {
            pstmt2.setString(5, severity);
        }
        else {
            pstmt2.setNull(5, Types.JAVA_OBJECT);
        }
        if (std_desc_id != null) {
            pstmt2.setInt(6, std_desc_id);
        }
        else {
            pstmt2.setNull(6, Types.JAVA_OBJECT);
        }
        if (problem_desc != null) {
            pstmt2.setString(7, problem_desc);
        }
        else {
            pstmt2.setNull(7, Types.JAVA_OBJECT);
        }
        if (new_acct_trans_id != null) {
            pstmt2.setInt(8, new_acct_trans_id);
        }
        else {
            pstmt2.setNull(8, Types.JAVA_OBJECT);
        }
        if (prop_id != null) {
            pstmt2.setInt(9, prop_id);
        }
        else {
            pstmt2.setNull(9, Types.JAVA_OBJECT);
        }
        if (entry_id != null) {
            pstmt2.setInt(10, entry_id);
        }
        else {
            pstmt2.setNull(10, Types.JAVA_OBJECT);
        }
        if (last_update_dtime != null) {
            pstmt2.setObject(11, last_update_dtime);
        }
        else {
            pstmt2.setNull(11, Types.JAVA_OBJECT);
        }
        if (last_update_id != null) {
            pstmt2.setInt(12, last_update_id);
        }
        else {
            pstmt2.setNull(12, Types.JAVA_OBJECT);
        }
        executeUpdate(pstmt2);
        pstmt2.close();
        //Get the serial value from the insert.

        cust_call_id = dbinfo("sqlca.sqlerrd1");
        // Finally copy all the cust_call_log records over.

        PreparedStatement pstmt3 = prepareStatement(
                  "select cl.ord, cl.notes, cl.entry_id, cl.entry_dtime"
                + " from cust_call_log cl"
                + " where cl.cust_call_id = ?");
        
        if (orig_cust_call_id != null) {
            pstmt3.setInt(1, orig_cust_call_id);
        }
        else {
            pstmt3.setNull(1, Types.JAVA_OBJECT);
        }
        ResultSet rs2 = executeQuery(pstmt3);
        while (rs2.next()) {
            ord = rs2.getInt(1);
            notes = rs2.getString(2);
            entry_id = rs2.getInt(3);
            last_update_dtime = new DateTime(rs2.getTimestamp(4).getTime());
            PreparedStatement pstmt4 = prepareInsert(
                      "insert into cust_call_log (cust_call_id, ord, notes, entry_dtime, entry_id)"
                    + " values (?, ?, ?, ?, ?)");
            if (cust_call_id != null) {
                pstmt4.setInt(1, cust_call_id);
            }
            else {
                pstmt4.setNull(1, Types.JAVA_OBJECT);
            }
            if (ord != null) {
                pstmt4.setInt(2, ord);
            }
            else {
                pstmt4.setNull(2, Types.JAVA_OBJECT);
            }
            if (notes != null) {
                pstmt4.setString(3, notes);
            }
            else {
                pstmt4.setNull(3, Types.JAVA_OBJECT);
            }
            if (last_update_dtime != null) {
                pstmt4.setObject(4, last_update_dtime);
            }
            else {
                pstmt4.setNull(4, Types.JAVA_OBJECT);
            }
            if (entry_id != null) {
                pstmt4.setInt(5, entry_id);
            }
            else {
                pstmt4.setNull(5, Types.JAVA_OBJECT);
            }
            executeUpdate(pstmt4);
            pstmt4.close();
        }
        pstmt3.close();
        rs2.close();
        return cust_call_id;
    }

}