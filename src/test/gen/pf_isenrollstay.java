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

public class pf_isenrollstay extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public String execute(Integer in_acct_id, DateTime in_doa) throws SQLException, ProcedureException {
        /*
         * pf_isenrollstay.sql - Check if the stay at hand is an enrollment stay from the hotel.
         *    
         * $Id: pf_isenrollstay.sql 36195 2011-02-03 01:41:47Z rshepher $
         * 
         *       Copyright (C) 2010 Choice Hotels International, Inc.
         */
        DateTime l_signup_date;
        String l_answer;
        String l_appl_source;
        String l_debug;
        Integer sql_error;
        Integer isam_error;
        String error_data;
        try {
            l_signup_date = null;
            l_answer = "F";
            l_appl_source = null;
            l_debug = "F";
            // set up tracing based on app_config entry

            l_debug = (String) new settrace().execute("pf_isenrollstay");
            if (l_debug.equals("T")) {
                setDebugFile("/tmp/pf_isenrollstay.trace");
                trace("on");
            }
            // Get sign up date and signup location for the account

            PreparedStatement pstmt1 = prepareStatement(
                      "select a.signup_date, a.appl_source"
                    + " from acct a"
                    + " where a.acct_id = ?");
            
            if (in_acct_id != null) {
                pstmt1.setInt(1, in_acct_id);
            }
            else {
                pstmt1.setNull(1, Types.JAVA_OBJECT);
            }
            ResultSet rs1 = executeQuery(pstmt1);
            rs1.next();
            l_signup_date = new DateTime(rs1.getTimestamp(1).getTime());
            l_appl_source = rs1.getString(2);
            pstmt1.close();
            rs1.close();
            // if hotel signed up account on DOA, then is enrollment stay

            if (l_signup_date.isEqual(in_doa) && l_appl_source != null && l_appl_source.equals("H")) {
                l_answer = "T";
            }
            return l_answer;
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