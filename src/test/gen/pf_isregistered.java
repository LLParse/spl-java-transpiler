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

public class pf_isregistered extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public String execute(Integer in_offer_id, Integer in_acct_id, DateTime in_doa) throws SQLException, ProcedureException {
        /*
         * pf_isregistered.sql - Check if the account is registered for the offer prior to the DOA. 
         *    
         * $Id: pf_isregistered.sql 27972 2010-04-16 23:29:13Z rshepher $
         * 
         *       Copyright (C) 2010 Choice Hotels International, Inc.
         */
        String l_answer;
        Integer l_offer_id;
        DateTime l_offer_dtime;
        Integer sql_error;
        Integer isam_error;
        String error_data;
        try {
            l_answer = "T";
            // assume the best

            l_offer_id = null;
            l_offer_dtime = null;
            //set debug file to '/tmp/pf_isregistered.trace';

            //trace on;

            // get date of registration for the account

            PreparedStatement pstmt1 = prepareStatement(
                      "select date(ao.offer_dtime)"
                    + " from acct_offer ao"
                    + " where ao.acct_id = ?"
                    + " and ao.offer_id = ?");
            
            if (in_acct_id != null) {
                pstmt1.setInt(1, in_acct_id);
            }
            else {
                pstmt1.setNull(1, Types.JAVA_OBJECT);
            }
            if (in_offer_id != null) {
                pstmt1.setInt(2, in_offer_id);
            }
            else {
                pstmt1.setNull(2, Types.JAVA_OBJECT);
            }
            ResultSet rs1 = executeQuery(pstmt1);
            rs1.next();
            l_offer_dtime = new DateTime(rs1.getTimestamp(1).getTime());
            pstmt1.close();
            rs1.close();
            // check if they registered for this offer before the stay

            if (l_offer_dtime == null || l_offer_dtime.isAfter(in_doa)) {
                l_answer = "F";
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