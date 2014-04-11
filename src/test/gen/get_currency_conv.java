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

public class get_currency_conv extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public Double execute(String curr_cd) throws SQLException, ProcedureException {
        /*
         * Get the currency conversion rate for the specified currency.
         * 
         *   The conversion rate will be the most recent average rate.
         */
        Double conv_rate;
        conv_rate = null;
        PreparedStatement pstmt1 = prepareStatement(
                  "select avg_rate"
                + " from currency_conv"
                + " where currency_conv.curr_cd = ?"
                + " and currency_conv.conv_date = 
                    + "select max(currency_conv.conv_date)"
                    + " from currency_conv"
                    + " where currency_conv.curr_cd = ?");
            
            if (curr_cd != null) {
                pstmt1.setString(1, curr_cd);
            }
            else {
                pstmt1.setNull(1, Types.JAVA_OBJECT);
            }
            if (curr_cd != null) {
                pstmt1.setString(2, curr_cd);
            }
            else {
                pstmt1.setNull(2, Types.JAVA_OBJECT);
            }
            ResultSet rs1 = executeQuery(pstmt1);
            rs1.next();
            conv_rate = rs1.getDouble(1);
            rs1.close();
            return conv_rate;
        }
    
    }