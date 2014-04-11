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

public class pf_acct_trans_contrib_chk extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public Integer execute(Integer in_stay_id, Integer in_promo_id) throws SQLException, ProcedureException {
        /*
         * $Id: pf_acct_trans_contrib_chk.sql 47496 2012-01-24 23:39:00Z rshepher $
         * 
         *   Description: 
         * 
         *   Determine if stay has already participated in a bonus for the promo_id provided
         *   
         *   
         *       Copyright (C) 2012 Choice Hotels International, Inc.
         *                       All Rights Reserved
         */
        Integer l_act_acct_trans_id;
        Integer l_bonus_cnt;
        Integer sql_error;
        Integer isam_error;
        String error_data;
        try {
            l_act_acct_trans_id = null;
            l_bonus_cnt = 0;
            PreparedStatement pstmt1 = prepareStatement(
                      "select acct_trans_id"
                    + " from acct_trans_contrib"
                    + " where contrib_stay_id = ?");
            
            if (in_stay_id != null) {
                pstmt1.setInt(1, in_stay_id);
            }
            else {
                pstmt1.setNull(1, Types.JAVA_OBJECT);
            }
            ResultSet rs1 = executeQuery(pstmt1);
            while (rs1.next()) {
                l_act_acct_trans_id = rs1.getInt(1);
                if (l_act_acct_trans_id != null) {
                    PreparedStatement pstmt2 = prepareStatement(
                              "select count(*)"
                            + " from acct_trans a, acct_trans_detail ad"
                            + " where a.acct_trans_id = ad.acct_trans_id"
                            + " and a.acct_trans_id = ?"
                            + " and ad.promo_id = ?"
                            + " and a.rev_acct_trans_id is null");
                    
                    if (l_act_acct_trans_id != null) {
                        pstmt2.setInt(1, l_act_acct_trans_id);
                    }
                    else {
                        pstmt2.setNull(1, Types.JAVA_OBJECT);
                    }
                    if (in_promo_id != null) {
                        pstmt2.setInt(2, in_promo_id);
                    }
                    else {
                        pstmt2.setNull(2, Types.JAVA_OBJECT);
                    }
                    ResultSet rs2 = executeQuery(pstmt2);
                    rs2.next();
                    l_bonus_cnt = rs2.getInt(1);
                    pstmt2.close();
                    rs2.close();
                }
                if (l_bonus_cnt > 0) {
                    break;
                }
            }
            pstmt1.close();
            rs1.close();
            return l_bonus_cnt;
        }
        catch (SQLException e) {
            sql_error = e.getErrorCode();
            isam_error = 0;
            error_data = e.getMessage();
            {
                throw new ProcedureException(sql_error, isam_error, error_data);
            }
        }
    }

}