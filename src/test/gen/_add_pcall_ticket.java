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

public class _add_pcall_ticket extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public Integer execute(Integer prop_id, String call_status, String severity, Integer std_desc_id, String problem_desc, String assoc_recog_cd, String assoc_recog_id, String recog_cd, Integer entry_id, String notes, String contact_title, String contact_frst_name, String contact_last_name, String contact_phone, String contact_fax, String contact_email_addr) throws SQLException, ProcedureException {
        /*
         * (]$[) $RCSfile$:$Revision: 8487 $ | CDATE=$Date: 2005-11-08 09:40:41 -0700 (Tue, 08 Nov 2005) $ ~
         *  
         *  Generate one each prop_call and prop_call_log records.
         *  
         *  Assumption is that 'BEGIN WORK' is called prior to this
         *  procedure and 'ROLLBACK/COMMIT WORK' after depending on result. Returns
         *  ticket_nbr on success. 
         * 
         * 	Copyright (C) 2002 Choice Hotels International, Inc.
         *                       All Rights Reserved
         */
        Integer prop_call_id;
        prop_call_id = null;
        // set debug file to '/tmp/_add_pcall_ticket.trace';

        // trace on;

        // Generate the call ticket first.

        PreparedStatement pstmt1 = prepareInsert(
                  "insert into prop_call (prop_call_id, prop_id, call_dtime, call_status, severity, std_desc_id, problem, assoc_recog_cd, assoc_recog_id, recog_cd, entry_id, last_update_dtime, last_update_id)"
                + " values (0, ?, current, ?, ?, ?, ?, ?, ?, ?, ?, current, ?)");
        if (prop_id != null) {
            pstmt1.setInt(1, prop_id);
        }
        else {
            pstmt1.setNull(1, Types.JAVA_OBJECT);
        }
        if (call_status != null) {
            pstmt1.setString(2, call_status);
        }
        else {
            pstmt1.setNull(2, Types.JAVA_OBJECT);
        }
        if (severity != null) {
            pstmt1.setString(3, severity);
        }
        else {
            pstmt1.setNull(3, Types.JAVA_OBJECT);
        }
        if (std_desc_id != null) {
            pstmt1.setInt(4, std_desc_id);
        }
        else {
            pstmt1.setNull(4, Types.JAVA_OBJECT);
        }
        if (problem_desc != null) {
            pstmt1.setString(5, problem_desc);
        }
        else {
            pstmt1.setNull(5, Types.JAVA_OBJECT);
        }
        if (assoc_recog_cd != null) {
            pstmt1.setString(6, assoc_recog_cd);
        }
        else {
            pstmt1.setNull(6, Types.JAVA_OBJECT);
        }
        if (assoc_recog_id != null) {
            pstmt1.setString(7, assoc_recog_id);
        }
        else {
            pstmt1.setNull(7, Types.JAVA_OBJECT);
        }
        if (recog_cd != null) {
            pstmt1.setString(8, recog_cd);
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
        if (entry_id != null) {
            pstmt1.setInt(10, entry_id);
        }
        else {
            pstmt1.setNull(10, Types.JAVA_OBJECT);
        }
        executeUpdate(pstmt1);
        pstmt1.close();
        prop_call_id = dbinfo("sqlca.sqlerrd1");
        PreparedStatement pstmt2 = prepareInsert(
                  "insert into prop_call_log (prop_call_id, ord, notes, contact_title, contact_frst_name, contact_last_name, contact_phone, contact_fax, contact_email_addr, entry_dtime, entry_id)"
                + " values (?, 1, ?, ?, ?, ?, ?, ?, ?, current, ?)");
        if (prop_call_id != null) {
            pstmt2.setInt(1, prop_call_id);
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
        if (contact_title != null) {
            pstmt2.setString(3, contact_title);
        }
        else {
            pstmt2.setNull(3, Types.JAVA_OBJECT);
        }
        if (contact_frst_name != null) {
            pstmt2.setString(4, contact_frst_name);
        }
        else {
            pstmt2.setNull(4, Types.JAVA_OBJECT);
        }
        if (contact_last_name != null) {
            pstmt2.setString(5, contact_last_name);
        }
        else {
            pstmt2.setNull(5, Types.JAVA_OBJECT);
        }
        if (contact_phone != null) {
            pstmt2.setString(6, contact_phone);
        }
        else {
            pstmt2.setNull(6, Types.JAVA_OBJECT);
        }
        if (contact_fax != null) {
            pstmt2.setString(7, contact_fax);
        }
        else {
            pstmt2.setNull(7, Types.JAVA_OBJECT);
        }
        if (contact_email_addr != null) {
            pstmt2.setString(8, contact_email_addr);
        }
        else {
            pstmt2.setNull(8, Types.JAVA_OBJECT);
        }
        if (entry_id != null) {
            pstmt2.setInt(9, entry_id);
        }
        else {
            pstmt2.setNull(9, Types.JAVA_OBJECT);
        }
        executeUpdate(pstmt2);
        pstmt2.close();
        // Return success

        return prop_call_id;
    }

}