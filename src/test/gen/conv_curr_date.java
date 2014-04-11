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

public class conv_curr_date extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public Double execute(String from_curr_cd, String to_curr_cd, DateTime conv_date, Double amount) throws SQLException, ProcedureException {
        /*
         * (]$[) $RCSfile$:$Revision: 7872 $ | CDATE=$Date: 2005-09-16 08:17:58 -0700 (Fri, 16 Sep 2005) $ ~
         * 
         *   Convert the specified amount from one currency to another.
         * 
         *       Copyright (C) 2001 Choice Hotels International, Inc.
         *                       All Rights Reserved
         */
        Double from_conv_rate;
        Double to_conv_rate;
        Double new_amount;
        //set debug file to '/tmp/conv_curr_date.trace';

        //trace on;

        from_conv_rate = null;
        to_conv_rate = null;
        new_amount = null;
        // lookup the conversion rate to convert from

        PreparedStatement pstmt1 = prepareStatement(
                  "select avg_rate"
                + " from currency_conv"
                + " where currency_conv.curr_cd = ?"
                + " and currency_conv.conv_date = 
                    + "select max(currency_conv.conv_date)"
                    + " from currency_conv"
                    + " where currency_conv.curr_cd = ?"
                    + " and currency_conv.conv_date < ?");
            
            if (from_curr_cd != null) {
                pstmt1.setString(1, from_curr_cd);
            }
            else {
                pstmt1.setNull(1, Types.JAVA_OBJECT);
            }
            if (from_curr_cd != null) {
                pstmt1.setString(2, from_curr_cd);
            }
            else {
                pstmt1.setNull(2, Types.JAVA_OBJECT);
            }
            if (conv_date != null) {
                pstmt1.setObject(3, conv_date);
            }
            else {
                pstmt1.setNull(3, Types.JAVA_OBJECT);
            }
            ResultSet rs1 = executeQuery(pstmt1);
            rs1.next();
            from_conv_rate = rs1.getDouble(1);
            rs1.close();
            if (from_conv_rate <= 0.0) {
                from_conv_rate = null;
            }
            // lookup the conversion rate to convert to

            PreparedStatement pstmt2 = prepareStatement(
                      "select avg_rate"
                    + " from currency_conv"
                    + " where currency_conv.curr_cd = ?"
                    + " and currency_conv.conv_date = 
                        + "select max(currency_conv.conv_date)"
                        + " from currency_conv"
                        + " where currency_conv.curr_cd = ?"
                        + " and currency_conv.conv_date < ?");
                
                if (to_curr_cd != null) {
                    pstmt2.setString(1, to_curr_cd);
                }
                else {
                    pstmt2.setNull(1, Types.JAVA_OBJECT);
                }
                if (to_curr_cd != null) {
                    pstmt2.setString(2, to_curr_cd);
                }
                else {
                    pstmt2.setNull(2, Types.JAVA_OBJECT);
                }
                if (conv_date != null) {
                    pstmt2.setObject(3, conv_date);
                }
                else {
                    pstmt2.setNull(3, Types.JAVA_OBJECT);
                }
                ResultSet rs2 = executeQuery(pstmt2);
                rs2.next();
                to_conv_rate = rs2.getDouble(1);
                rs2.close();
                if (to_conv_rate <= 0.0) {
                    to_conv_rate = null;
                }
                // convert amount checking against divide by zero.

                if (from_conv_rate != null && to_conv_rate != null) {
                    if (!from_conv_rate.equals(0.0)) {
                        new_amount = amount * to_conv_rate / from_conv_rate;
                    }
                }
                return new_amount;
            }
        
        }