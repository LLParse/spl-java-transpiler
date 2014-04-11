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

public class pf_std_booking_rules extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public String execute(Integer in_acct_id, DateTime in_doa, Integer in_los, String in_res_source) throws SQLException, ProcedureException {
        /*
         * $Id: pf_std_booking_rules.sql 52307 2012-06-18 23:30:58Z rick_shepherd $
         * 
         *   Description: 
         * 
         *   For the stay provided, see if it meets the "standard" promotion booking source rules. These seem
         *   to be pretty standard over the last several promotions. However, change is always possible and
         *   different versions of this procedure may be needed in the future.
         *   
         *   *****************************************************************************************************
         *   NOTE: This procedure is now being used by the pf_vanilla_stay_twice* filters as the standard stay
         *         twice book rules, so if this procedure needs to change for some other promotion that calls it, 
         *         you will need to create a new booking rule procedure with rules suited for the "vanilla" stay
         *         twice filters.
         *   *****************************************************************************************************
         *   
         *   The rules are:
         *   
         *   - reservation source is either Internet(N), CRS(C), GDS(G), or Mobile(M)
         *   - Platinum or Diamond can book via any source
         *   - Enrollment any time during the stay qualifies 
         *   
         *       Copyright (C) 2012 Choice Hotels International, Inc.
         *                       All Rights Reserved
         */
        String l_answer;
        Integer sql_error;
        Integer isam_error;
        String error_data;
        try {
            l_answer = "F";
            // check if centrally booked

            if (in_res_source != null && (in_res_source.equals("N") || in_res_source.equals("C") || in_res_source.equals("G") || in_res_source.equals("M"))) {
                l_answer = "T";
            }
            // if not centrally booked, see if at platinum or diamond elite level

            if (l_answer.equals("F")) {
                l_answer = (String) new pf_isplatinum_ondoa().execute(in_acct_id, in_doa);
                if (l_answer.equals("F")) {
                    l_answer = (String) new pf_isdiamond_ondoa().execute(in_acct_id, in_doa);
                }
            }
            // if still not eligible, see if they enrolled during the stay

            if (l_answer.equals("F")) {
                l_answer = (String) new pf_isenrollstay_v2().execute(in_acct_id, in_doa, in_doa.plusDays(in_los));
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