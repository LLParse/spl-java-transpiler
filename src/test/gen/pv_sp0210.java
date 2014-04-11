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

public class pv_sp0210 extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public Double execute(Integer in_acct_id, Integer in_cust_id, Integer in_promo_id, Integer in_criteria, Double in_value, Double in_stay_rev, String in_chain_cd, Integer in_los, Integer in_stay_id) throws SQLException, ProcedureException {
        /*
         * $Id: pv_sp0210.sql 25075 2010-01-15 22:32:02Z rshepher $
         * 
         *   Description: 
         * 
         *   Compute the amount of points to award for Spring 2010 promo, 
         *   1 + 1 = FREE; minumum 5,000, max 8,000. A temp table containing
         *   2 rows for the 2 stays being awarded will be available for this function
         *   to sum and determine the points to award.
         *   
         *       Copyright (C) 2010 Choice Hotels International, Inc.
         *                       All Rights Reserved
         */
        Double l_amount;
        Double l_sum_points;
        Double l_residual;
        Integer l_row_count;
        Integer sql_error;
        Integer isam_error;
        String error_data;
        try {
            l_amount = 0.0;
            l_sum_points = 0.0;
            l_residual = 0.0;
            l_row_count = 0;
            //set debug file to '/tmp/pv_sp0210.trace';

            //trace on;

            PreparedStatement pstmt1 = prepareStatement(
                      "select count(*), sum(amount)"
                    + " from qualified_stays_pv_sp10");
            
            ResultSet rs1 = executeQuery(pstmt1);
            rs1.next();
            l_row_count = rs1.getInt(1);
            l_sum_points = rs1.getDouble(2);
            pstmt1.close();
            rs1.close();
            if (!l_row_count.equals(2) || l_sum_points == null) {
                throw new ProcedureException(-746, 0, "pv_sp0210: qualified_stays_pv_sp10 table count error");
            }
            l_residual = mod(l_sum_points, 8000);
            if (l_residual < 3000.0) {
                l_amount = 8000.0 - l_residual;
            }
            else {
                l_amount = 5000.0;
            }
            PreparedStatement pstmt2 = prepareStatement("drop table qualified_stays_pv_sp10");
            executeUpdate(pstmt2);
            pstmt2.close();
            return l_amount;
        }
        catch (SQLException e) {
            sql_error = e.getErrorCode();
            isam_error = 0;
            error_data = e.getMessage();
            {
                PreparedStatement pstmt3 = prepareStatement("drop table qualified_stays_pv_sp10");
                executeUpdate(pstmt3);
                pstmt3.close();
                throw new ProcedureException(sql_error, isam_error, error_data);
            }
        }
    }

}