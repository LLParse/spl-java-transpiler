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

public class apply_bonus extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public Integer execute(String recog_cd, String offer_cd) throws SQLException, ProcedureException {
        /*
         * (]$[) $RCSfile$:$Revision: 8487 $ | CDATE=$Date: 2005-11-08 09:40:41 -0700 (Tue, 08 Nov 2005) $ ~
         *   This procedure scans the acct_offer table for accounts offered the given 
         *   promotion. For each account that matches, call add_bonus_event to award 
         *   the amount.
         * 
         * 	  Copyright (C) 2001 Choice Hotels International, Inc.
         * 			All Rights Reserved
         */
        Integer offer_id;
        Integer acct_id;
        Integer cust_id;
        Integer promo_id;
        Integer result;
        Double result2;
        String promo_cd;
        Integer sql_error;
        Integer isam_error;
        String error_data;
        try {
            acct_id = null;
            cust_id = null;
            offer_id = null;
            promo_id = null;
            result = null;
            promo_cd = null;
            // set debug file to '/tmp/apply_bonus.trace';

            // trace on;

            offer_id = new get_offer_id().execute(recog_cd, offer_cd);
            if (offer_id == null) {
                throw new ProcedureException(-746, 0, "apply_bonus: Invalid offer code");
            }
            setHoldability(java.sql.ResultSet.HOLD_CURSORS_OVER_COMMIT);
            PreparedStatement pstmt1 = prepareStatement(
                      "select acct_offer.acct_id, acct_offer.offer_id"
                    + " from acct_offer"
                    + " where acct_offer.offer_id = ?");
            
            if (offer_id != null) {
                pstmt1.setInt(1, offer_id);
            }
            else {
                pstmt1.setNull(1, Types.JAVA_OBJECT);
            }
            ResultSet rs1 = executeQuery(pstmt1);
            while (rs1.next()) {
                acct_id = rs1.getInt(1);
                offer_id = rs1.getInt(2);
                PreparedStatement pstmt2 = prepareStatement(
                          "select promo_acct_elig.promo_id"
                        + " from promo_acct_elig"
                        + " where promo_acct_elig.offer_id = ?");
                
                if (offer_id != null) {
                    pstmt2.setInt(1, offer_id);
                }
                else {
                    pstmt2.setNull(1, Types.JAVA_OBJECT);
                }
                ResultSet rs2 = executeQuery(pstmt2);
                rs2.next();
                promo_id = rs2.getInt(1);
                pstmt2.close();
                rs2.close();
                PreparedStatement pstmt3 = prepareStatement(
                          "select promo.promo_cd"
                        + " from promo"
                        + " where promo.promo_id = ?");
                
                if (promo_id != null) {
                    pstmt3.setInt(1, promo_id);
                }
                else {
                    pstmt3.setNull(1, Types.JAVA_OBJECT);
                }
                ResultSet rs3 = executeQuery(pstmt3);
                rs3.next();
                promo_cd = rs3.getString(1);
                pstmt3.close();
                rs3.close();
                // Find the primary customer id for this account.

                cust_id = new get_pri_cust_id().execute(acct_id);
                // Do the work...

                Iterator<Object> it0 = new add_bonus_event().execute(acct_id, cust_id, promo_cd, "F", null).iterator();
                result = (Integer) it0.next();
                result2 = (Double) it0.next();
            }
            pstmt1.close();
            rs1.close();
            setHoldability(java.sql.ResultSet.CLOSE_CURSORS_AT_COMMIT);
            return result;
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