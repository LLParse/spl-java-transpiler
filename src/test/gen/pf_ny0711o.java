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

public class pf_ny0711o extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public String execute(Integer in_acct_id, Integer in_promo_id, String in_stay_type, Integer in_prop_id, DateTime in_doa, Integer in_los, String in_rm_type, String in_srp_code, Double in_rm_revenue, Double in_fb_revenue, Double in_other_revenue, String in_curr_code, String in_res_source, Integer in_stay_id) throws SQLException, ProcedureException {
        /*
         * pf_ny0711o.sql - New York on Sale Summer promotion. This filter checks:
         *                  1) if the account has been registered for the offer
         *                  2) the booking source is ch.com or mobile
         *                  3) reservation made between 7/21/2011 and 9/30/2011
         *                  
         *    
         * $Id: pf_ny0711o.sql 40197 2011-06-25 01:26:17Z rshepher $
         *    
         *        Copyright (C) 2011 Choice Hotels International, Inc.
         */
        DateTime l_start_date;
        DateTime l_stop_date;
        String l_debug;
        Integer l_acct_appl_group_id;
        Integer l_appl_group_id;
        DateTime l_reservation_date;
        DateTime l_offer_dtime;
        String l_answer;
        String l_resv_start_date;
        String l_resv_stop_date;
        Integer sql_error;
        Integer isam_error;
        String error_data;
        try {
            l_start_date = null;
            l_stop_date = null;
            l_debug = "F";
            l_acct_appl_group_id = null;
            l_appl_group_id = null;
            l_reservation_date = null;
            l_offer_dtime = null;
            l_answer = "F";
            l_resv_start_date = null;
            l_resv_stop_date = null;
            // set up tracing based on app_config entry

            l_debug = (String) new settrace().execute("pf_ny0711o");
            if (l_debug.equals("T")) {
                setDebugFile("/tmp/pf_ny0711o_" + dbinfo("sessionid") + ".trace");
                trace("on");
            }
            // Check for valid doa

            PreparedStatement pstmt1 = prepareStatement(
                      "select promo.start_date, promo.stop_date"
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
            pstmt1.close();
            rs1.close();
            if (in_doa.isBefore(l_start_date) || in_doa.isAfter(l_stop_date)) {
                return l_answer;
            }
            // get account info

            PreparedStatement pstmt2 = prepareStatement(
                      "select appl_group_id"
                    + " from acct"
                    + " where acct_id = ?");
            
            if (in_acct_id != null) {
                pstmt2.setInt(1, in_acct_id);
            }
            else {
                pstmt2.setNull(1, Types.JAVA_OBJECT);
            }
            ResultSet rs2 = executeQuery(pstmt2);
            rs2.next();
            l_acct_appl_group_id = rs2.getInt(1);
            pstmt2.close();
            rs2.close();
            // get acct_offer record

            PreparedStatement pstmt3 = prepareStatement(
                      "select offer_dtime"
                    + " from acct_offer"
                    + " where acct_id = ?"
                    + " and offer_id = 
                        + "select offer_id"
                        + " from offer"
                        + " where offer_cd = \"NY0711O\"");
                
                if (in_acct_id != null) {
                    pstmt3.setInt(1, in_acct_id);
                }
                else {
                    pstmt3.setNull(1, Types.JAVA_OBJECT);
                }
                ResultSet rs3 = executeQuery(pstmt3);
                rs3.next();
                l_offer_dtime = new DateTime(rs3.getTimestamp(1).getTime());
                rs3.close();
                // if no registration, then no bonus

                if (l_offer_dtime == null) {
                    return "F";
                }
                // if registered but did not book via ch.com or mobile, then no bonus

                if (in_res_source == null || (!in_res_source.equals("N") && !in_res_source.equals("M"))) {
                    return l_answer;
                }
                // get reservation date from stay record

                PreparedStatement pstmt4 = prepareStatement(
                          "select reservation_date"
                        + " from stay"
                        + " where stay_id = ?");
                
                if (in_stay_id != null) {
                    pstmt4.setInt(1, in_stay_id);
                }
                else {
                    pstmt4.setNull(1, Types.JAVA_OBJECT);
                }
                ResultSet rs4 = executeQuery(pstmt4);
                rs4.next();
                l_reservation_date = new DateTime(rs4.getTimestamp(1).getTime());
                pstmt4.close();
                rs4.close();
                // get the reservation control dates from the app_config table

                PreparedStatement pstmt5 = prepareStatement(
                          "select value"
                        + " from app_config"
                        + " where key = \"promo.ny0711.reservation_start_date \"");
                
                ResultSet rs5 = executeQuery(pstmt5);
                rs5.next();
                l_resv_start_date = rs5.getString(1);
                pstmt5.close();
                rs5.close();
                PreparedStatement pstmt6 = prepareStatement(
                          "select value"
                        + " from app_config"
                        + " where key = \"promo.ny0711.reservation_stop_date \"");
                
                ResultSet rs6 = executeQuery(pstmt6);
                rs6.next();
                l_resv_stop_date = rs6.getString(1);
                pstmt6.close();
                rs6.close();
                if (l_reservation_date == null || l_reservation_date.isBefore(date(l_resv_start_date)) || l_reservation_date.isAfter(date(l_resv_stop_date))) {
                    return l_answer;
                }
                // all the basic criteria has passed, now check if another room has already received the bonus

                l_answer = (String) new pf_ny0711_awardbonus().execute(in_acct_id, in_promo_id, in_stay_type, in_prop_id, in_doa, in_los, in_rm_type, in_srp_code, in_rm_revenue, in_fb_revenue, in_other_revenue, in_curr_code, in_res_source, in_stay_id);
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