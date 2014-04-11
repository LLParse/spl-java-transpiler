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

public class pf_fl0811d extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public String execute(Integer in_acct_id, Integer in_promo_id, String in_stay_type, Integer in_prop_id, DateTime in_doa, Integer in_los, String in_rm_type, String in_srp_code, Double in_rm_revenue, Double in_fb_revenue, Double in_other_revenue, String in_curr_code, String in_res_source, Integer in_stay_id) throws SQLException, ProcedureException {
        /*
         * pf_fl0811d.sql - Fall 2011 promo filter for double points if registered:
         *                 1) Must be registered
         *                 2) Not booked centrally via ch.com, 18004Choice, mobile
         *                 3) Non-elite or Gold
         *                 
         *                 This filter is only entered via the FL0811[ME]D promo_acct_elig 
         *                 if there is an acct_offer entry
         *    
         * $Id: pf_fl0811d.sql 41698 2011-08-02 23:26:38Z rshepher $
         *    
         *        Copyright (C) 2011 Choice Hotels International, Inc.
         */
        DateTime l_start_date;
        DateTime l_stop_date;
        DateTime l_offer_dtime;
        String l_recog_cd;
        DateTime l_signup_date;
        Integer l_offer_id;
        String l_platinum;
        String l_diamond;
        String l_eligible;
        String l_hotel;
        String l_winai;
        String l_debug;
        Integer sql_error;
        Integer isam_error;
        String error_data;
        try {
            l_start_date = null;
            l_stop_date = null;
            l_offer_dtime = null;
            l_recog_cd = null;
            l_signup_date = null;
            l_offer_id = null;
            l_platinum = "F";
            l_diamond = "F";
            l_eligible = "F";
            l_hotel = "F";
            l_winai = "F";
            l_debug = "F";
            // set up tracing based on app_config entry

            l_debug = (String) new settrace().execute("pf_fl0811d");
            if (l_debug.equals("T")) {
                setDebugFile("/tmp/pf_fl0811d_" + dbinfo("sessionid") + ".trace");
                trace("on");
            }
            // get some account info

            PreparedStatement pstmt1 = prepareStatement(
                      "select a.recog_cd, a.signup_date"
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
            l_recog_cd = rs1.getString(1);
            l_signup_date = new DateTime(rs1.getTimestamp(2).getTime());
            pstmt1.close();
            rs1.close();
            // see if registered on or before DOA

            PreparedStatement pstmt2 = prepareStatement(
                      "select offer_dtime"
                    + " from acct_offer"
                    + " where acct_id = ?"
                    + " and offer_id = 
                        + "select offer_id"
                        + " from offer"
                        + " where offer_cd = \"FL0811O\""
                        + " and recog_cd = ?");
                
                if (in_acct_id != null) {
                    pstmt2.setInt(1, in_acct_id);
                }
                else {
                    pstmt2.setNull(1, Types.JAVA_OBJECT);
                }
                if (l_recog_cd != null) {
                    pstmt2.setString(2, l_recog_cd);
                }
                else {
                    pstmt2.setNull(2, Types.JAVA_OBJECT);
                }
                ResultSet rs2 = executeQuery(pstmt2);
                rs2.next();
                l_offer_dtime = new DateTime(rs2.getTimestamp(1).getTime());
                rs2.close();
                if (l_offer_dtime.isAfter(in_doa)) {
                    return l_eligible;
                }
                // if platinum or diamond then they can't get double

                l_platinum = (String) new pf_isplatinum_ondoa().execute(in_acct_id, in_doa);
                if (l_platinum.equals("F")) {
                    l_diamond = (String) new pf_isdiamond_ondoa().execute(in_acct_id, in_doa);
                }
                // if Plat or Diamond on DOA, then always eligible for quadruple

                if (l_platinum.equals("T") || l_diamond.equals("T")) {
                    return l_eligible;
                }
                // See if enrollment stay at the hotel. If so, then it's not eligible

                // for double points since it should have gotten quadruple points from

                // the pf_fl0811 filter. So, not eligible and no further checking is

                // required.

                l_eligible = (String) new pf_isenrollstay_v2().execute(in_acct_id, in_doa, in_doa.plusDays(in_los));
                if (l_eligible.equals("T")) {
                    return "F";
                }
                // if not a required quadruple points booking source, then eligible for double

                if (in_res_source == null || (!in_res_source.equals("N") && !in_res_source.equals("C") && !in_res_source.equals("M"))) {
                    l_eligible = "T";
                }
                if (l_eligible.equals("F")) {
                    return l_eligible;
                }
                // all basic criteria have been met and it looks like bonus should be awarded 

                // but do final check on multiple rooms and already awarded bonus.

                l_eligible = (String) new pf_fl0811_awardbonus().execute(in_acct_id, in_promo_id, in_stay_type, in_prop_id, in_doa, in_los, in_rm_type, in_srp_code, in_rm_revenue, in_fb_revenue, in_other_revenue, in_curr_code, in_res_source, in_stay_id, l_start_date, l_stop_date);
                return l_eligible;
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