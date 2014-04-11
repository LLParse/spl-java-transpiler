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

public class _add_call_ticket extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public Integer execute(Integer cust_id, Integer acct_id, String call_status, String severity, Integer std_desc_id, String problem_desc, Integer prop_id, Integer entry_id, String notes) throws SQLException, ProcedureException {
        /*
         * (]$[) $RCSfile$:$Revision: 6848 $ | CDATE=$Date: 2005-05-19 02:21:48 -0700 (Thu, 19 May 2005) $ ~
         *  
         *  Generate one each cust_call and cust_call_log records.
         *  
         *  Assumption is that 'BEGIN WORK' is called prior to this
         *  procedure and 'ROLLBACK/COMMIT WORK' after depending on result. Returns
         *  ticket_nbr on success. 
         * 
         * 	Copyright (C) 2001 Choice Hotels International, Inc.
         *                       All Rights Reserved
         */
        Integer cust_call_id;
        cust_call_id = null;
        // set debug file to '/tmp/_add_call_ticket.trace';

        // trace on;

        // Generate the call ticket first.

        PreparedStatement pstmt1 = prepareInsert(
                  "insert into cust_call (cust_call_id, cust_id, acct_id, call_dtime, call_status, severity, std_desc_id, problem_desc, prop_id, entry_id, last_update_dtime, last_update_id)"
                + " values (0, ?, ?, current, ?, ?, ?, ?, ?, ?, current, ?)");
        if (cust_id != null) {
            pstmt1.setInt(1, cust_id);
        }
        else {
            pstmt1.setNull(1, Types.JAVA_OBJECT);
        }
        if (acct_id != null) {
            pstmt1.setInt(2, acct_id);
        }
        else {
            pstmt1.setNull(2, Types.JAVA_OBJECT);
        }
        if (call_status != null) {
            pstmt1.setString(3, call_status);
        }
        else {
            pstmt1.setNull(3, Types.JAVA_OBJECT);
        }
        if (severity != null) {
            pstmt1.setString(4, severity);
        }
        else {
            pstmt1.setNull(4, Types.JAVA_OBJECT);
        }
        if (std_desc_id != null) {
            pstmt1.setInt(5, std_desc_id);
        }
        else {
            pstmt1.setNull(5, Types.JAVA_OBJECT);
        }
        if (problem_desc != null) {
            pstmt1.setString(6, problem_desc);
        }
        else {
            pstmt1.setNull(6, Types.JAVA_OBJECT);
        }
        if (prop_id != null) {
            pstmt1.setInt(7, prop_id);
        }
        else {
            pstmt1.setNull(7, Types.JAVA_OBJECT);
        }
        if (entry_id != null) {
            pstmt1.setInt(8, entry_id);
        }
        else {
            pstmt1.setNull(8, Types.JAVA_OBJECT);
        }
        if (entry_id != null) {
            pstmt1.setInt(9, entry_id);
        }
        else {
            pstmt1.setNull(9, Types.JAVA_OBJECT);
        }
        executeUpdate(pstmt1);
        pstmt1.close();
        cust_call_id = dbinfo("sqlca.sqlerrd1");
        PreparedStatement pstmt2 = prepareInsert(
                  "insert into cust_call_log (cust_call_id, ord, notes, entry_dtime, entry_id)"
                + " values (?, 1, ?, current, ?)");
        if (cust_call_id != null) {
            pstmt2.setInt(1, cust_call_id);
        }
        else {
            pstmt2.setNull(1, Types.JAVA_OBJECT);
        }
        if (notes != null) {
            pstmt2.setString(2, notes);
        }
        else {
            pstmt2.setNull(2, Types.JAVA_OBJECT);
        }
        if (entry_id != null) {
            pstmt2.setInt(3, entry_id);
        }
        else {
            pstmt2.setNull(3, Types.JAVA_OBJECT);
        }
        executeUpdate(pstmt2);
        pstmt2.close();
        // Return success

        return cust_call_id;
    }

}