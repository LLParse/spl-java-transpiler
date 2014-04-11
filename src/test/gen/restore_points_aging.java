/* Generated on 03-01-2013 12:10:57 PM by SPLParser v0.9 */
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

public class restore_points_aging extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public Integer execute(Integer orig_acct_trans_id, Integer restore_acct_trans_id, Integer acct_id_in) throws SQLException, ProcedureException {
        /*
         * $RCSfile$ - Restores points from temporary expiration bucket - $Revision: 25751 $
         * 
         *         (]$[) $RCSfile$:$Revision: 25751 $ | CDATE=$Date: 2010-02-02 16:45:49 -0700 (Tue, 02 Feb 2010) $ ~
         * 
         *               Copyright (C) 2001 Choice Hotels International, Inc.
         *                               All Rights Reserved
         */
        Integer l_amount;
        Integer l_exp_amount;
        DateTime l_exp_date;
        Integer l_cnt;
        Integer l_aging_count;
        Integer l_acct_id;
        l_amount = null;
        l_exp_amount = null;
        l_exp_date = null;
        l_cnt = 0;
        l_aging_count = 0;
        l_acct_id = null;
        // set debug file to '/tmp/restore_points_aging.trace';

        // trace on;

        PreparedStatement pstmt1 = prepareStatement(
                  "select count(*)"
                + " from acct_trans_aging ta"
                + " where ta.acct_trans_id = ?");
        
        if (orig_acct_trans_id != null) {
            pstmt1.setInt(1, orig_acct_trans_id);
        }
        else {
            pstmt1.setNull(1, Types.JAVA_OBJECT);
        }
        ResultSet rs1 = executeQuery(pstmt1);
        rs1.next();
        l_aging_count = rs1.getInt(1);
        pstmt1.close();
        rs1.close();
        if (l_aging_count < 1) {
            return 0;
        }
        // if the exp buckets can't be restored, then fall back to 

        // the original logic of just decrementing the oldest bucket first

        PreparedStatement pstmt2 = prepareStatement(
                  "select amount, exp_date, acct_id"
                + " from acct_trans_aging ta"
                + " where ta.acct_trans_id = ?"
                + " order by exp_date desc");
        
        if (orig_acct_trans_id != null) {
            pstmt2.setInt(1, orig_acct_trans_id);
        }
        else {
            pstmt2.setNull(1, Types.JAVA_OBJECT);
        }
        ResultSet rs2 = executeQuery(pstmt2);
        while (rs2.next()) {
            l_amount = rs2.getInt(1);
            l_exp_date = new DateTime(rs2.getTimestamp(2).getTime());
            l_acct_id = rs2.getInt(3);
            // not concerned about negative amounts at this point

            if (l_amount < 0.0) {
                continue;
            }
            PreparedStatement pstmt3 = prepareStatement(
                      "select amount"
                    + " from acct_exp"
                    + " where acct_id = ?"
                    + " and exp_date = ?");
            
            if (l_acct_id != null) {
                pstmt3.setInt(1, l_acct_id);
            }
            else {
                pstmt3.setNull(1, Types.JAVA_OBJECT);
            }
            if (l_exp_date != null) {
                pstmt3.setObject(2, l_exp_date);
            }
            else {
                pstmt3.setNull(2, Types.JAVA_OBJECT);
            }
            ResultSet rs3 = executeQuery(pstmt3);
            rs3.next();
            l_exp_amount = rs3.getInt(1);
            pstmt3.close();
            rs3.close();
            if (l_amount * -1 < l_exp_amount) {
                return 0;
            }
        }
        pstmt2.close();
        rs2.close();
        PreparedStatement pstmt4 = prepareStatement(
                  "select amount, exp_date, acct_id"
                + " from acct_trans_aging ta"
                + " where ta.acct_trans_id = ?"
                + " order by exp_date desc");
        
        if (orig_acct_trans_id != null) {
            pstmt4.setInt(1, orig_acct_trans_id);
        }
        else {
            pstmt4.setNull(1, Types.JAVA_OBJECT);
        }
        ResultSet rs4 = executeQuery(pstmt4);
        while (rs4.next()) {
            l_amount = rs4.getInt(1);
            l_exp_date = new DateTime(rs4.getTimestamp(2).getTime());
            l_acct_id = rs4.getInt(3);
            if (new LocalDate().toDateTimeAtStartOfDay().isAfter(l_exp_date)) {
                PreparedStatement pstmt5 = prepareInsert(
                          "insert into acct_exp_temp (acct_id, ins_dtime, amount)"
                        + " values (?, current, ?)");
                if (l_acct_id != null) {
                    pstmt5.setInt(1, l_acct_id);
                }
                else {
                    pstmt5.setNull(1, Types.JAVA_OBJECT);
                }
                if (l_amount != null) {
                    pstmt5.setInt(2, l_amount);
                }
                else {
                    pstmt5.setNull(2, Types.JAVA_OBJECT);
                }
                executeUpdate(pstmt5);
                pstmt5.close();
            }
            else {
                PreparedStatement pstmt6 = prepareStatement(
                          "update acct_exp"
                        + " set amount = amount + ? * -1"
                        + " where acct_id = ?"
                        + " and exp_date = ?");
                
                if (l_amount != null) {
                    pstmt6.setInt(1, l_amount);
                }
                else {
                    pstmt6.setNull(1, Types.JAVA_OBJECT);
                }
                if (acct_id_in != null) {
                    pstmt6.setInt(2, acct_id_in);
                }
                else {
                    pstmt6.setNull(2, Types.JAVA_OBJECT);
                }
                if (l_exp_date != null) {
                    pstmt6.setObject(3, l_exp_date);
                }
                else {
                    pstmt6.setNull(3, Types.JAVA_OBJECT);
                }
                executeUpdate(pstmt6);
                pstmt6.close();
                PreparedStatement pstmt7 = prepareInsert(
                          "insert into acct_trans_aging (acct_trans_id, acct_id, exp_date, amount)"
                        + " values (?, ?, ?, -?)");
                if (restore_acct_trans_id != null) {
                    pstmt7.setInt(1, restore_acct_trans_id);
                }
                else {
                    pstmt7.setNull(1, Types.JAVA_OBJECT);
                }
                if (acct_id_in != null) {
                    pstmt7.setInt(2, acct_id_in);
                }
                else {
                    pstmt7.setNull(2, Types.JAVA_OBJECT);
                }
                if (l_exp_date != null) {
                    pstmt7.setObject(3, l_exp_date);
                }
                else {
                    pstmt7.setNull(3, Types.JAVA_OBJECT);
                }
                if (l_amount != null) {
                    pstmt7.setInt(4, l_amount);
                }
                else {
                    pstmt7.setNull(4, Types.JAVA_OBJECT);
                }
                executeUpdate(pstmt7);
                pstmt7.close();
            }
        }
        pstmt4.close();
        rs4.close();
        return l_aging_count;
    }

}