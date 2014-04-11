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

public class pf_cpm0611 extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public String execute(Integer in_acct_id, Integer in_promo_id, String in_stay_type, Integer in_prop_id, DateTime in_doa, Integer in_los, String in_rm_type, String in_srp_code, Double in_rm_revenue, Double in_fb_revenue, Double in_other_revenue, String in_curr_code, String in_res_source, Integer in_stay_id) throws SQLException, ProcedureException {
        /*
         * 
pf_cpm0611.sql - Summer 2011 Mobile promo filter. The filter checks to see if the criteria are met for
                 the promotion. This is a GP and CN promotion.
                 
                 Stay eligibility criteria for this promo are:
   
                 1) booked via the mobile booking reservation channel
                 2) DOA must be within 48 hours (2 days) of booking the reservation
                 3) Consecutive stays are a factor so there is only one bonus allowed
                    for a 'stay'.
                 
   
$Id$
   
       Copyright (C) 2011 Choice Hotels International, Inc.

         */
        DateTime l_start_date;
        DateTime l_stop_date;
        String l_promo_cd;
        String l_eligible;
        String l_answer;
        String l_debug;
        Integer l_mobile_res_channel;
        Integer l_stay_res_channel;
        DateTime l_doa;
        DateTime l_reservation_date;
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
            l_mobile_res_channel = null;
            l_stay_res_channel = null;
            l_doa = null;
            l_reservation_date = null;
            // set up tracing based on app_config entry

            l_debug = (String) new settrace().execute("pf_cpm0611");
            if (l_debug.equals("T")) {
                setDebugFile("/tmp/pf_cpm0611_" + dbinfo("sessionid") + ".trace");
                trace("on");
            }
            // Check for valid doa

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
            if (in_doa.isBefore(l_start_date) || in_doa.isAfter(l_stop_date)) {
                return l_answer;
            }
            // check if reservation channel id is from mobile booking

            PreparedStatement pstmt2 = prepareStatement(
                      "select reservation_channel_id"
                    + " from reservation_channel"
                    + " where desc = \"CH.com Mobile Interface\"");
            
            ResultSet rs2 = executeQuery(pstmt2);
            rs2.next();
            l_mobile_res_channel = rs2.getInt(1);
            pstmt2.close();
            rs2.close();
            if (l_mobile_res_channel == null) {
                throw new ProcedureException(-746, 0, "pf_cpm0611: CH.com Mobile Interface reservation_channel_id not found.");
            }
            PreparedStatement pstmt3 = prepareStatement(
                      "select reservation_channel_id, doa, reservation_date"
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
            l_stay_res_channel = rs3.getInt(1);
            l_doa = new DateTime(rs3.getTimestamp(2).getTime());
            l_reservation_date = new DateTime(rs3.getTimestamp(3).getTime());
            pstmt3.close();
            rs3.close();
            if (in_res_source != null && !in_res_source.equals("M")) {
                if (l_stay_res_channel == null || !l_mobile_res_channel.equals(l_stay_res_channel)) {
                    return l_answer;
                }
            }
            // now see if booking was within 2 days of DOA or, heavens forbid,

            // after the DOA! (Jody had a test case for this)

            if (l_reservation_date == null || l_reservation_date.isBefore(l_doa.minusDays(2)) || l_reservation_date.isAfter(l_doa)) {
                return l_answer;
            }
            // Stay appears eligible, but we need to see if it is consecutive with any

            // other stays that may have been awarded the bonus before giving a final

            // thumbs up.

            l_answer = (String) new pf_cpm0611_awardbonus().execute(in_acct_id, in_promo_id, in_stay_type, in_prop_id, in_doa, in_los, in_rm_type, in_srp_code, in_rm_revenue, in_fb_revenue, in_other_revenue, in_curr_code, in_res_source, in_stay_id);
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