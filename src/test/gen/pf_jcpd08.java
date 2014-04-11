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

public class pf_jcpd08 extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public String execute(Integer acct_id, Integer promo_id, String stay_type, Integer prop_id, DateTime doa, Integer los, String rm_type, String srp_code, Double rm_revenue, Double fb_revenue, Double other_revenue, String curr_code, String res_source, Integer stay_id) throws SQLException, ProcedureException {
        /*
         * 
pf_jcpd08.sql - Check account sign up date falls between promotion start/stop dates

(]$[) $RCSfile$:$Revision: $ | CDATE=$Date:  $ ~

       Copyright (C) 2008 Choice Hotels International, Inc.
     All Rights Reserved

         */
        DateTime signup_date;
        DateTime start_date;
        DateTime stop_date;
        signup_date = null;
        start_date = null;
        stop_date = "01/31/09";
        // end sign up date not same as promo stop date

        //set debug file to '/tmp/pf_jcpd08.trace';

        //trace on;

        PreparedStatement pstmt1 = prepareStatement(
                  "select acct.signup_date"
                + " from acct"
                + " where acct.acct_id = ?");
        
        if (acct_id != null) {
            pstmt1.setInt(1, acct_id);
        }
        else {
            pstmt1.setNull(1, Types.JAVA_OBJECT);
        }
        ResultSet rs1 = executeQuery(pstmt1);
        rs1.next();
        signup_date = new DateTime(rs1.getTimestamp(1).getTime());
        pstmt1.close();
        rs1.close();
        PreparedStatement pstmt2 = prepareStatement(
                  "select promo.start_date"
                + " from promo"
                + " where promo.promo_id = ?");
        
        if (promo_id != null) {
            pstmt2.setInt(1, promo_id);
        }
        else {
            pstmt2.setNull(1, Types.JAVA_OBJECT);
        }
        ResultSet rs2 = executeQuery(pstmt2);
        rs2.next();
        start_date = new DateTime(rs2.getTimestamp(1).getTime());
        pstmt2.close();
        rs2.close();
        if ((signup_date.isAfter(start_date) || signup_date.isEqual(start_date)) && (signup_date.isBefore(stop_date) || signup_date.isEqual(stop_date))) {
            return "T";
        }
        return "F";
    }

}