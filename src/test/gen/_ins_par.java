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

public class _ins_par extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public Integer execute(Integer acct_id, Integer cust_id, String trans_type, Integer acct_trans_id) throws SQLException, ProcedureException {
        /*
         * $RCSfile$ - Insert a par record - $Revision: 5804 $
         * 
         * (]$[) $RCSfile$:$Revision: 5804 $ | CDATE=$Date: 2004-12-13 04:10:28 -0700 (Mon, 13 Dec 2004) $ ~
         * 
         *       Copyright (C) 2001 Choice Hotels International, Inc.
         *                       All Rights Reserved
         * 
         * This procedure collects customer information and inserts a pending partner
         * activity request record. The transaction type must not be type 'E' expiring, 
         * or 'V' reversing. If the transaction already exists in the par table with any
         * status, the insert is denied and an exception is raised.
         * 
         * Note it is not an error if any part of the customer name is missing. It will be
         * the application that processes these records for specific partners that will
         * determine that. If required data is missing they will reject the par record.
         * Some partners may not care about the name info.
         * 
         * This procedure must be called inside a transaction to ensure data integrity.
         */
        Integer par_id;
        String cust_frst_name;
        String cust_mid_initial;
        String cust_last_name;
        String recog_cd;
        String recog_id;
        par_id = null;
        cust_frst_name = null;
        cust_mid_initial = null;
        cust_last_name = null;
        recog_cd = null;
        recog_id = null;
        //set debug file to '/tmp/_ins_par.trace';

        //trace on;

        // First get the partner ID (recog_cd) and partner customer ID (recog_id)

        recog_cd = new get_recog_cd().execute(acct_id);
        recog_id = new get_recog_id().execute(acct_id);
        if (recog_cd == null) {
            throw new ProcedureException(-746, 0, "_ins_par: Invalid partner program code, 'recog_cd'.");
        }
        if (recog_id == null) {
            throw new ProcedureException(-746, 0, "_ins_par: Invalid partner customer number 'recog_id'.");
        }
        // Next check that the transaction is not already in queue.

        PreparedStatement pstmt1 = prepareStatement(
                  "select p.par_id"
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
        pstmt1.close();
        rs1.close();
        if (par_id != null) {
            throw new ProcedureException(-746, 0, "_ins_par: Transaction already queued.");
        }
        // Make sure the transaction type is valid for submission

        if (trans_type.equals("E") || trans_type.equals("V")) {
            throw new ProcedureException(-746, 0, "_ins_par: Transaction type invalid.");
        }
        // Next retrieve the customer info

        PreparedStatement pstmt2 = prepareStatement(
                  "select c.frst_name, c.mid_initial, c.last_name"
                + " from cust c"
                + " where c.cust_id = ?");
        
        if (cust_id != null) {
            pstmt2.setInt(1, cust_id);
        }
        else {
            pstmt2.setNull(1, Types.JAVA_OBJECT);
        }
        ResultSet rs2 = executeQuery(pstmt2);
        rs2.next();
        cust_frst_name = rs2.getString(1);
        cust_mid_initial = rs2.getString(2);
        cust_last_name = rs2.getString(3);
        pstmt2.close();
        rs2.close();
        // Create the record. 

        PreparedStatement pstmt3 = prepareInsert(
                  "insert into par (par_id, recog_cd, request_dtime, request_status, cust_acct_nbr, cust_frst_name, cust_mid_initial, cust_last_name, acct_trans_id)"
                + " values (0, ?, current, \"P\", ?, ?, ?, ?, ?)");
        if (recog_cd != null) {
            pstmt3.setString(1, recog_cd);
        }
        else {
            pstmt3.setNull(1, Types.JAVA_OBJECT);
        }
        if (recog_id != null) {
            pstmt3.setString(2, recog_id);
        }
        else {
            pstmt3.setNull(2, Types.JAVA_OBJECT);
        }
        if (cust_frst_name != null) {
            pstmt3.setString(3, cust_frst_name);
        }
        else {
            pstmt3.setNull(3, Types.JAVA_OBJECT);
        }
        if (cust_mid_initial != null) {
            pstmt3.setString(4, cust_mid_initial);
        }
        else {
            pstmt3.setNull(4, Types.JAVA_OBJECT);
        }
        if (cust_last_name != null) {
            pstmt3.setString(5, cust_last_name);
        }
        else {
            pstmt3.setNull(5, Types.JAVA_OBJECT);
        }
        if (acct_trans_id != null) {
            pstmt3.setInt(6, acct_trans_id);
        }
        else {
            pstmt3.setNull(6, Types.JAVA_OBJECT);
        }
        executeUpdate(pstmt3);
        pstmt3.close();
        return dbinfo("sqlca.sqlerrd1");
    }

}