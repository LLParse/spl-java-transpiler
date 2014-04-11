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

public class pv_su09m extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public Double execute(Integer in_acct_id, Integer in_cust_id, Integer in_promo_id, Integer in_criteria, Double in_value, Double in_stay_rev, String in_chain_cd, Integer in_los, Integer in_stay_id) throws SQLException, ProcedureException {
        /*
         * $Id: pv_su09m.sql 22868 2009-08-05 19:01:21Z eelliot $
         * 
         *   Description: 
         * 
         *   Compute the amount of points to award for summer 2009 promo, 
         *   stay 3 nights, get enough points for $50 cash card; minumum
         *   10,000, max 14,000.
         *   
         *       Copyright (C) 2009 Choice Hotels International, Inc.
         *                       All Rights Reserved
         */
        Double amount;
        Double l_sum_amounts;
        Double residual;
        Integer sql_error;
        Integer isam_error;
        String error_data;
        try {
            amount = 0.0;
            l_sum_amounts = 0.0;
            residual = 0.0;
            //set debug file to '/tmp/pv_su09m.trace';

            //trace on;

            // get sum of the first three rows from the qualified_stays_pv_su09 table

            // these are the ones qualifying for the bonus

            PreparedStatement pstmt1 = prepareStatement(
                      "select sum(q.amount)"
                    + " from qualified_stays_pv_su09 q"
                    + " where id in (1, 2, 3)");
            
            ResultSet rs1 = executeQuery(pstmt1);
            rs1.next();
            l_sum_amounts = rs1.getDouble(1);
            pstmt1.close();
            rs1.close();
            residual = mod(l_sum_amounts, 14000);
            if (residual < 4000.0) {
                amount = 14000.0 - residual;
            }
            else {
                amount = 10000.0;
            }
            PreparedStatement pstmt2 = prepareStatement("drop table qualified_stays_pv_su09");
            executeUpdate(pstmt2);
            pstmt2.close();
            return amount;
        }
        catch (SQLException e) {
            sql_error = e.getErrorCode();
            isam_error = 0;
            error_data = e.getMessage();
            {
                PreparedStatement pstmt3 = prepareStatement("drop table qualified_stays_pv_su09");
                executeUpdate(pstmt3);
                pstmt3.close();
                throw new ProcedureException(sql_error, isam_error, error_data);
            }
        }
    }

}