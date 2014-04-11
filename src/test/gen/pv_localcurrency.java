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

public class pv_localcurrency extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public Double execute(Integer acct_id, Integer cust_id, Integer promo_id, Integer criteria, Double value, Double stay_rev, String chain_cd, Integer los, Integer stay_id) throws SQLException, ProcedureException {
        /*
         * $Id: pv_localcurrency.sql 20931 2009-02-26 22:48:48Z rshepher $
         * 
         *   Description: 
         * 
         *   Compute the number of points to award based on the eligible stay revenue of the
         *   local currency, not the stay_rev which has been converted to USD. The value and
         *   criteria parameters are used along with the elig_revenue_pc to calculate the
         *   final amount awarded; i.e value * revenue / criteria. For examples:
         *   
         *   Promo for Air New Zealand Airpoints program for a stay in New Zealand is 1 point for 
         *   every 20 New Zealand Dollars(NZD). Eligible stay revenue is 100 NZD. Amount awarded 
         *   is 1 * 100 / 20 = 5 points.
         *   
         *   Promo for Velocity Rewards program for a stay in Australia is 3 points per
         *   Australian Dollar (AUD). Eligible stay revenue is 100 AUD. Amount awarded is
         *   3 * 100 / 1 = 300 points.
         * 
         *       Copyright (C) 2009 Choice Hotels International, Inc.
         *                       All Rights Reserved
         */
        Double l_amount;
        Double l_elig_revenue_pc;
        l_amount = null;
        l_elig_revenue_pc = 0.0;
        //set debug file to '/tmp/pv_localcurrency.trace';

        //trace on;

        // Get local currency eligible revenue from stay table

        PreparedStatement pstmt1 = prepareStatement(
                  "select elig_revenue_pc"
                + " from stay"
                + " where stay.stay_id = ?");
        
        if (stay_id != null) {
            pstmt1.setInt(1, stay_id);
        }
        else {
            pstmt1.setNull(1, Types.JAVA_OBJECT);
        }
        ResultSet rs1 = executeQuery(pstmt1);
        rs1.next();
        l_elig_revenue_pc = rs1.getDouble(1);
        pstmt1.close();
        rs1.close();
        l_amount = value * trunc(l_elig_revenue_pc, 0) / criteria.doubleValue();
        return l_amount;
    }

}