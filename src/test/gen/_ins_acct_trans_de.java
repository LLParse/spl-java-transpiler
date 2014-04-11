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

public class _ins_acct_trans_de extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public Integer execute(Integer acct_trans_id, Integer ord, Double amount, Integer promo_id, String variance) throws SQLException, ProcedureException {
        /*
         * $RCSfile$ - Insert a valid acct_trans record - $Revision: 5804 $
         * 
         * (]$[) $RCSfile$:$Revision: 5804 $ | CDATE=$Date: 2004-12-13 04:10:28 -0700 (Mon, 13 Dec 2004) $ ~
         * 
         *       Copyright (C) 2001 Choice Hotels International, Inc.
         *                       All Rights Reserved
         * 
         * This procedure inserts an acct_trans_detail record. Note that
         * the ord values start at 1 and are contiguous. If null is passed
         * the value becomes the next avail.
         */
        Integer detail_id;
        detail_id = null;
        //set debug file to '/tmp/_ins_acct_trans_de.trace';

        //trace on;

        // check which detail to add

        if (ord == null) {
            
            PreparedStatement pstmt1 = prepareStatement(
                      "select max(acct_trans_detail.ord)"
                    + " from acct_trans_detail"
                    + " where acct_trans_detail.acct_trans_id = ?
                    Integer tmp1 = rs0.getInt(1);
                    ord = 1 + tmp1;
                }
                if (? <> \"N\""
                + " and ? <> \"Y\") {
                    throw new ProcedureException(-746, 0, \"_ins_acct_trans_de: variance indicator must be 'Y' or 'N'.\");
                }
                PreparedStatement pstmt2 = prepareInsert(
                          "insert into acct_trans_detail (?, ?, ?, ?, ?)"
                        + " values (?, ?, ?, ?, ?)");
                if (acct_trans_id != null) {
                    pstmt2.setInt(1, acct_trans_id);
                }
                else {
                    pstmt2.setNull(1, Types.JAVA_OBJECT);
                }
                if (ord != null) {
                    pstmt2.setInt(2, ord);
                }
                else {
                    pstmt2.setNull(2, Types.JAVA_OBJECT);
                }
                if (amount != null) {
                    pstmt2.setDouble(3, amount);
                }
                else {
                    pstmt2.setNull(3, Types.JAVA_OBJECT);
                }
                if (promo_id != null) {
                    pstmt2.setInt(4, promo_id);
                }
                else {
                    pstmt2.setNull(4, Types.JAVA_OBJECT);
                }
                if (variance != null) {
                    pstmt2.setString(5, variance);
                }
                else {
                    pstmt2.setNull(5, Types.JAVA_OBJECT);
                }
                if (acct_trans_id != null) {
                    pstmt2.setInt(6, acct_trans_id);
                }
                else {
                    pstmt2.setNull(6, Types.JAVA_OBJECT);
                }
                if (ord != null) {
                    pstmt2.setInt(7, ord);
                }
                else {
                    pstmt2.setNull(7, Types.JAVA_OBJECT);
                }
                if (amount != null) {
                    pstmt2.setDouble(8, amount);
                }
                else {
                    pstmt2.setNull(8, Types.JAVA_OBJECT);
                }
                if (promo_id != null) {
                    pstmt2.setInt(9, promo_id);
                }
                else {
                    pstmt2.setNull(9, Types.JAVA_OBJECT);
                }
                if (variance != null) {
                    pstmt2.setString(10, variance);
                }
                else {
                    pstmt2.setNull(10, Types.JAVA_OBJECT);
                }
                executeUpdate(pstmt2);
                pstmt2.close();
                //Get the serial value from the insert.

                detail_id = dbinfo("sqlca.sqlerrd1");
                return detail_id;
            }
        
        }