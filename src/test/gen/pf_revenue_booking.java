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

public class pf_revenue_booking extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public Collection<Object> execute(Integer in_acct_id, DateTime in_doa, Integer in_los, String in_res_source, Integer in_stay_id, String in_promo_cd) throws SQLException, ProcedureException {
        /*
         * Code that checks the booking rules for a vanilla revenue promotion. Can be called from the
         *        registration filter or the non-registration filter.
         *    
         *        $Id: pf_revenue_booking.sql 56841 2012-10-18 22:46:09Z rick_shepherd $
         *    
         *        Copyright (C) 2012 Choice Hotels International, Inc.
         */
        // define local variables to be used in the procedure

        DateTime l_start_date;
        DateTime l_stop_date;
        String l_answer;
        String l_debug;
        String l_impl;
        String l_operator;
        String l_param_list;
        String l_booking_sources;
        DateTime l_reservation_date;
        Integer l_reservation_window;
        String l_reservation_dates;
        String l_check_consecutive;
        String l_res_source_found;
        Integer l_reservation_int;
        Integer l_i;
        // variables for thrown exceptions

        Integer sql_error;
        Integer isam_error;
        String error_data;
        try {
            // initialize all local variables

            l_start_date = null;
            l_stop_date = null;
            l_answer = "F";
            l_debug = "F";
            l_reservation_date = null;
            l_booking_sources = null;
            l_reservation_date = null;
            l_reservation_window = null;
            l_reservation_dates = null;
            l_check_consecutive = "F";
            l_impl = null;
            l_operator = null;
            l_param_list = null;
            l_res_source_found = "F";
            l_i = 1;
            // set up tracing based on app_config entry

            l_debug = (String) new settrace().execute("pf_revenue_booking");
            if (l_debug.equals("T")) {
                setDebugFile("/tmp/pf_revenue_booking_" + dbinfo("sessionid") + ".trace");
                trace("on");
            }
            // get the booking requirements stored in the promo_filter_item table

            PreparedStatement pstmt1 = prepareStatement(
                      "select pfi.impl, pfi.operator, pfi.param_list"
                    + " from promo_filter pf"
                    + "  inner join promo_filter_item pfi on pf.filter_id = pfi.filter_id"
                    + " where pf.name = \"pf_revenue_booking_\" || ?");
            
            if (in_promo_cd != null) {
                pstmt1.setString(1, in_promo_cd);
            }
            else {
                pstmt1.setNull(1, Types.JAVA_OBJECT);
            }
            ResultSet rs1 = executeQuery(pstmt1);
            while (rs1.next()) {
                l_impl = rs1.getString(1);
                l_operator = rs1.getString(2);
                l_param_list = rs1.getString(3);
                if (l_impl.equals("BookingSourceFilter")) {
                    l_booking_sources = l_param_list;
                }
                if (l_impl.equals("ReservationDateFilter")) {
                    l_reservation_window = Integer.parseInt(l_param_list);
                }
                if (l_impl.equals("ReservationWindowFilter")) {
                    l_reservation_dates = l_param_list;
                }
                if (l_impl.equals("BonusNotAwardedFilter")) {
                    l_check_consecutive = "T";
                }
            }
            pstmt1.close();
            rs1.close();
            // if there are booking requirements, then assume Plat/Diamond members don't have them so

            // check the elite level

            if (l_booking_sources != null) {
                l_answer = (String) new pf_isplatinum_ondoa().execute(in_acct_id, in_doa);
                if (l_answer.equals("F")) {
                    l_answer = (String) new pf_isdiamond_ondoa().execute(in_acct_id, in_doa);
                }
            }
            // if we did not get an elite hit, check booking source requirements

            if (l_answer.equals("F")) {
                // see if it's an enrollment stay. If so, it qualifies regardless of booking source (if any required)

                l_answer = (String) new pf_isenrollstay_v2().execute(in_acct_id, in_doa, in_doa.plusDays(in_los));
            }
            // if not an enrollment stay or proper elite level, see if any booking requirements are met

            if (l_answer.equals("F")) {
                // check if we have booking source requirements and compare with the input reservation source variable

                if (l_booking_sources != null) {
                    while (l_i <= length(l_booking_sources)) {
                        if (substr(l_booking_sources, l_i, 1).equals(in_res_source)) {
                            l_answer = "T";
                            break;
                        }
                        l_i = l_i + 2;
                        if (l_i > 25) {
                            break;
                        }
                    }
                }
                else {
                    l_answer = "T";
                }
            }
            if (l_answer.equals("F")) {
                return new ArrayList<Object>(Arrays.<Object>asList(l_answer, l_check_consecutive));
            }
            // get stay info and check any reservation window/date requirements. This applies to 

            // everyone, including elite platinum and diamond.

            if (l_reservation_window != null || l_reservation_dates != null) {
                PreparedStatement pstmt2 = prepareStatement(
                          "select reservation_date"
                        + " from stay"
                        + " where stay_id = ?");
                
                if (in_stay_id != null) {
                    pstmt2.setInt(1, in_stay_id);
                }
                else {
                    pstmt2.setNull(1, Types.JAVA_OBJECT);
                }
                ResultSet rs2 = executeQuery(pstmt2);
                rs2.next();
                l_reservation_date = new DateTime(rs2.getTimestamp(1).getTime());
                pstmt2.close();
                rs2.close();
            }
            // See if booking was within x days of DOA or, heavens forbid,

            // after the DOA! (Jody had a test case for this)

            l_reservation_int = l_reservation_window;
            if (l_reservation_window != null) {
                if (l_reservation_date == null || l_reservation_date.isBefore(in_doa.minusDays(l_reservation_int)) || l_reservation_date.isAfter(in_doa)) {
                    l_answer = "F";
                }
            }
            if (l_answer.equals("F")) {
                return new ArrayList<Object>(Arrays.<Object>asList(l_answer, l_check_consecutive));
            }
            // See if booking was within a start/stop date window

            if (l_reservation_dates != null) {
                l_start_date = date(substr(l_reservation_dates, 1, 10));
                l_stop_date = date(substr(l_reservation_dates, 12, 10));
                if (l_reservation_date == null || l_reservation_date.isBefore(l_start_date) || l_reservation_date.isAfter(l_stop_date)) {
                    l_answer = "F";
                }
            }
            return new ArrayList<Object>(Arrays.<Object>asList(l_answer, l_check_consecutive));
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