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

public class pv_fl0809f extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public Double execute(Integer in_acct_id, Integer in_cust_id, Integer in_promo_id, Integer in_criteria, Double in_value, Double in_stay_rev, String in_chain_cd, Integer in_los, Integer in_stay_id) throws SQLException, ProcedureException {
        /*
         * $Id: pv_fl0809f.sql 22906 2009-08-06 22:19:51Z rshepher $
         * 
         *   Description: 
         * 
         *   Compute the amount of points to award for Fall 2009 promo, 
         *   stay 3 consecutive nights; minumum 5,000, max 8,000.
         *   
         *       Copyright (C) 2009 Choice Hotels International, Inc.
         *                       All Rights Reserved
         */
        Double amount;
        Double l_sum_amounts;
        Double residual;
        String l_chain_group_cd;
        Double l_pts_dollar;
        Integer sql_error;
        Integer isam_error;
        String error_data;
        try {
            amount = 0.0;
            l_sum_amounts = 0.0;
            residual = 0.0;
            l_chain_group_cd = null;
            l_pts_dollar = 0.0;
            //set debug file to '/tmp/pv_fl0809f.trace';

            //trace on;

            PreparedStatement pstmt1 = prepareStatement(
                      "select cg.chain_group_cd"
                    + " from chain_group cg, chain_group_detail cgd"
                    + " where cg.chain_group_id = cgd.chain_group_id"
                    + " and cgd.chain_id = ?");
            
            if (in_chain_cd != null) {
                pstmt1.setString(1, in_chain_cd);
            }
            else {
                pstmt1.setNull(1, Types.JAVA_OBJECT);
            }
            ResultSet rs1 = executeQuery(pstmt1);
            rs1.next();
            l_chain_group_cd = rs1.getString(1);
            pstmt1.close();
            rs1.close();
            if ((l_chain_group_cd.equals("MS") || l_chain_group_cd.equals("US"))) {
                l_pts_dollar = 10.0;
            }
            else {
                l_pts_dollar = 5.0;
            }
            // get sum of the rows from the pv_fl0809f_temp table

            PreparedStatement pstmt2 = prepareStatement(
                      "select sum(q.amount)"
                    + " from pv_fl0809f_temp q");
            
            ResultSet rs2 = executeQuery(pstmt2);
            rs2.next();
            l_sum_amounts = rs2.getDouble(1);
            pstmt2.close();
            rs2.close();
            residual = mod(l_sum_amounts * l_pts_dollar, 8000);
            if (residual < 3000.0) {
                amount = 8000.0 - residual;
            }
            else {
                amount = 5000.0;
            }
            PreparedStatement pstmt3 = prepareStatement("drop table pv_fl0809f_temp");
            executeUpdate(pstmt3);
            pstmt3.close();
            return amount;
        }
        catch (SQLException e) {
            sql_error = e.getErrorCode();
            isam_error = 0;
            error_data = e.getMessage();
            {
                PreparedStatement pstmt4 = prepareStatement("drop table pv_fl0809f_temp");
                executeUpdate(pstmt4);
                pstmt4.close();
                throw new ProcedureException(sql_error, isam_error, error_data);
            }
        }
    }

}