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

public class pf_booking extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public String execute(Integer in_acct_id, Integer in_promo_id, String in_stay_type, Integer in_prop_id, DateTime in_doa, Integer in_los, String in_rm_type, String in_srp_code, Double in_rm_revenue, Double in_fb_revenue, Double in_other_revenue, String in_curr_code, String in_res_source, Integer in_stay_id) throws SQLException, ProcedureException {
        /*
         * pf_booking.sql - Generic booking source filter that can be used by the Promotion Config Tool to
         *                  configure basic promotions; e.g. Mobile booking promotions
         *                  
         *                  The Promotion Config Tool will create the promotion and a promo_acct_elig
         *                  entry pointing to this filter. The Tool will also create promo_filter and
         *                  promo_filter item entries which will drive the logic of this filter. The
         *                  promo_filter name will be pf_booking_PROMO_CD where PROMO_CD is the promotion
         *                  code of the specific promotion being created.
         *                  
         *                  This filter expects the promo_filter_item rows to look like:
         *                  
         *                     ord=1
         *                     impl=BookingSourceFilter (required)
         *                     comparator=StringIn
         *                     param_list=comma separated list of booking sources
         *                     
         *                     ord=2
         *                     impl=ReservationDateFilter (optional - no reservation date requirement)
         *                     comparator=DateWithin
         *                     param_list=number of days reservation date prior to DOA
         *                     
         *                     OR (cannot have both ReservationDateFilter and ReservationWindowFilter)
         *                     
         *                     ord=2
         *                     impl=ReservationWindowFilter(optional - booking window start/stop dates)
         *                     comparator=DateBetween
         *                     param_list=start date, stop date
         *                     
         *                     ord=3
         *                     impl=BonusNotAwardedFilter (optional - if not present, all consecutive stays eligible for bonus)
         *                     comparator=n/a
         *                     param_list=promo_cd of promotion
         *                  
         *    
         * $Id: pf_booking.sql 49480 2012-03-30 21:12:54Z rick_shepherd $
         *    
         *        Copyright (C) 2012 Choice Hotels International, Inc.
         */
        DateTime l_start_date;
        DateTime l_stop_date;
        String l_promo_cd;
        String l_eligible;
        String l_answer;
        String l_debug;
        Integer l_database_res_channel;
        Integer l_stay_res_channel;
        DateTime l_doa;
        DateTime l_reservation_date;
        String l_impl;
        String l_operator;
        String l_param_list;
        String l_booking_sources;
        Integer l_reservation_window;
        String l_reservation_dates;
        String l_check_consecutive;
        String l_res_source_found;
        Integer l_reservation_int;
        Integer l_i;
        Integer sql_error;
        Integer isam_error;
        String error_data;
        try {
            l_start_date = null;
            l_stop_date = null;
            l_promo_cd = null;
            l_eligible = "F";
            l_answer = "F";
            l_debug = "F";
            l_database_res_channel = null;
            l_stay_res_channel = null;
            l_doa = null;
            l_reservation_date = null;
            l_booking_sources = null;
            l_reservation_window = null;
            l_reservation_dates = null;
            l_impl = null;
            l_operator = null;
            l_param_list = null;
            l_check_consecutive = null;
            l_res_source_found = "F";
            l_reservation_int = null;
            l_i = 1;
            // if there was no booking/reservation source, there is no reason to continue

            if (in_res_source == null) {
                return l_answer;
            }
            // set up tracing based on app_config entry

            l_debug = (String) new settrace().execute("pf_booking");
            if (l_debug.equals("T")) {
                setDebugFile("/tmp/pf_booking_" + dbinfo("sessionid") + ".trace");
                trace("on");
            }
            // Get some promo data

            PreparedStatement pstmt1 = prepareStatement(
                      "select promo.start_date, promo.stop_date, promo.promo_cd"
                    + " from promo"
                    + " where promo.promo_id = ?");
            
            if (in_promo_id != null) {
                pstmt1.setInt(1, in_promo_id);
            }
            else {
                pstmt1.setNull(1, Types.JAVA_OBJECT);
            }
            ResultSet rs1 = executeQuery(pstmt1);
            rs1.next();
            l_start_date = new DateTime(rs1.getTimestamp(1).getTime());
            l_stop_date = new DateTime(rs1.getTimestamp(2).getTime());
            l_promo_cd = rs1.getString(3);
            pstmt1.close();
            rs1.close();
            // The promo_filter_items were populated by the Promo Config Tool as if the filters

            // were written in Java for the Promotion Engine. We are doing things in stored 

            // procedures in order for the bonus acct_trans_detail record to be stored with base transaction

            // acct_trans_detail. Promotion engine would store a separate acct_trans/acct_trans_detail

            // pair which would be hard to reconcile in statements, stay details, etc...

            PreparedStatement pstmt2 = prepareStatement(
                      "select pfi.impl, pfi.operator, pfi.param_list"
                    + " from promo_filter pf"
                    + "  inner join promo_filter_item pfi on pf.filter_id = pfi.filter_id"
                    + " where pf.name = \"pf_booking_\" || ?");
            
            if (l_promo_cd != null) {
                pstmt2.setString(1, l_promo_cd);
            }
            else {
                pstmt2.setNull(1, Types.JAVA_OBJECT);
            }
            ResultSet rs2 = executeQuery(pstmt2);
            while (rs2.next()) {
                l_impl = rs2.getString(1);
                l_operator = rs2.getString(2);
                l_param_list = rs2.getString(3);
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
                    l_check_consecutive = l_param_list;
                }
            }
            pstmt2.close();
            rs2.close();
            // booking source entry is required - just deny or should throw an exception??? Let's throw an exception

            // so perhaps, someone notices

            if (l_booking_sources == null) {
                throw new ProcedureException(-746, 0, "pf_booking: No booking source values items found in promo_filter_item table for promo_cd " + l_promo_cd);
            }
            if (l_reservation_window != null && l_reservation_dates != null) {
                throw new ProcedureException(-746, 0, "pf_booking: Both reservation date and reservation window found for promo_cd " + l_promo_cd);
            }
            // parse booking sources to see if stay res_source matches any of them

            while (l_i <= length(l_booking_sources)) {
                if (substr(l_booking_sources, l_i, 1).equals(in_res_source)) {
                    l_res_source_found = "T";
                    break;
                }
                l_i = l_i + 2;
                if (l_i > 25) {
                    break;
                }
            }
            if (l_res_source_found.equals("F")) {
                return l_answer;
            }
            // at this point, booking source requirement met so tentatively say OK unless

            // we get rejected for reservation window/dates or consecutiveness

            l_answer = "T";
            // get stay info

            PreparedStatement pstmt3 = prepareStatement(
                      "select doa, reservation_date"
                    + " from stay"
                    + " where stay_id = ?");
            
            if (in_stay_id != null) {
                pstmt3.setInt(1, in_stay_id);
            }
            else {
                pstmt3.setNull(1, Types.JAVA_OBJECT);
            }
            ResultSet rs3 = executeQuery(pstmt3);
            rs3.next();
            l_doa = new DateTime(rs3.getTimestamp(1).getTime());
            l_reservation_date = new DateTime(rs3.getTimestamp(2).getTime());
            pstmt3.close();
            rs3.close();
            // See if booking was within x days of DOA or, heavens forbid,

            // after the DOA! (Jody had a test case for this)

            l_reservation_int = l_reservation_window;
            if (l_reservation_window != null) {
                if (l_reservation_date == null || l_reservation_date.isBefore(l_doa.minusDays(l_reservation_int)) || l_reservation_date.isAfter(l_doa)) {
                    l_answer = "F";
                    return l_answer;
                }
            }
            // See if booking was within a start/stop date window

            if (l_reservation_dates != null) {
                l_start_date = date(substr(l_reservation_dates, 1, 10));
                l_stop_date = date(substr(l_reservation_dates, 12, 10));
                if (l_reservation_date == null || l_reservation_date.isBefore(l_start_date) || l_reservation_date.isAfter(l_stop_date)) {
                    l_answer = "F";
                    return l_answer;
                }
            }
            // Stay appears eligible, but we need to see if it is consecutiveness needs to be

            // checked and if so, is the stay consecutive with any other stays that may have been 

            // awarded the bonus before giving a final thumbs up.

            if (l_check_consecutive != null) {
                l_answer = (String) new pf_booking_awardbonus().execute(in_acct_id, in_promo_id, in_stay_type, in_prop_id, in_doa, in_los, in_rm_type, in_srp_code, in_rm_revenue, in_fb_revenue, in_other_revenue, in_curr_code, in_res_source, in_stay_id);
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