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

public class pf_su0512s_reg extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public String execute(Integer in_acct_id, Integer in_promo_id, String in_stay_type, Integer in_prop_id, DateTime in_doa, Integer in_los, String in_rm_type, String in_srp_code, Double in_rm_revenue, Double in_fb_revenue, Double in_other_revenue, String in_curr_code, String in_res_source, Integer in_stay_id) throws SQLException, ProcedureException {
        /*
         * pf_su0512s_reg.sql - promo filter for registered accounts for the stay twice promotion. This 
         *                    filter checks the basic promo criteria to see if it warrants proceeding and looking at
         *                    all the linked stays for the account. This promo applies to only GP and CN programs.
         *                      
         *                  
         *                  Stay eligibility criteria for this promo are:
         *    
         *                  1) DOAs of first and second stay must be on or after registration date
         *                  1) booked via ch.com, central res, gds, mobile unless Platinum or Diamond elite
         *                  2) signup stay automatically qualifies (signup date during stay AND was done at the hotel)
         *                  3) Platinum or Diamond accounts can book through any channel
         *                  4) An econonmy stay must be at least 2 nights, consecutive stays meet criteria
         *                  5) A midscale stay must only be 1 night to qualify
         *                  
         *                  If those criteria are met, then determine if this is the second qualifying stay before
         *                  awarding the bonus. Note: the midsacle and economy 2 night requirement is not checked in this procedure, 
         *                  rather it is deferred to pf_su0511s_awardbonus since that is where all the stays for the
         *                  promo period are retrieved and analyzed.
         * 
         *    
         * $Id: pf_su0512s_reg.sql 52307 2012-06-18 23:30:58Z rick_shepherd $
         *    
         *        Copyright (C) 2012 Choice Hotels International, Inc.
         */
        DateTime l_start_date;
        DateTime l_stop_date;
        DateTime l_offer_dtime;
        Integer l_link;
        String l_platinum;
        String l_diamond;
        String l_enrollstay;
        String l_eligible;
        String l_answer;
        String l_debug;
        String l_economy;
        String l_economy_eligible;
        String l_consec_econ;
        String l_rules_met;
        String l_temp_exists;
        String l_did_insert;
        Integer l_register_check;
        Integer l_dbg_stay_id;
        String l_dbg_prop_cd;
        DateTime l_dbg_doa;
        DateTime l_dbg_dod;
        Integer l_dbg_linked_id;
        Integer l_dbg_acct_trans_id;
        Integer l_dbg_ord;
        DateTime l_dbg_post_dtime;
        String l_res_source;
        Double l_points;
        Integer sql_error;
        Integer isam_error;
        String error_data;
        try {
            l_start_date = null;
            l_stop_date = null;
            l_offer_dtime = null;
            l_link = null;
            l_platinum = "F";
            l_diamond = "F";
            l_enrollstay = "F";
            l_eligible = "F";
            l_answer = "F";
            l_debug = "F";
            l_economy = "F";
            l_economy_eligible = "F";
            l_consec_econ = "F";
            l_rules_met = "F";
            l_temp_exists = "F";
            l_did_insert = "F";
            l_register_check = 0;
            l_dbg_stay_id = null;
            l_dbg_prop_cd = null;
            l_dbg_doa = null;
            l_dbg_dod = null;
            l_dbg_linked_id = null;
            l_dbg_acct_trans_id = null;
            l_dbg_ord = null;
            l_dbg_post_dtime = null;
            l_res_source = null;
            l_points = null;
            // set up tracing based on app_config entry

            l_debug = (String) new settrace().execute("pf_su0512s_reg");
            if (l_debug.equals("T")) {
                setDebugFile("/tmp/pf_su0512s_reg_" + dbinfo("sessionid") + ".trace");
                trace("on");
            }
            // Retrieve some promo info

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
            // we know they are registered, otherwise we would not be in this filter. But we need

            // to see when they registered in relation to the DOA to see if we should proceed in this

            // filter.

            PreparedStatement pstmt2 = prepareStatement(
                      "select ao.offer_dtime"
                    + " from acct_offer ao, offer o"
                    + " where ao.offer_id = o.offer_id"
                    + " and ao.acct_id = ?"
                    + " and o.offer_cd = \"SU0512O\"");
            
            if (in_acct_id != null) {
                pstmt2.setInt(1, in_acct_id);
            }
            else {
                pstmt2.setNull(1, Types.JAVA_OBJECT);
            }
            ResultSet rs2 = executeQuery(pstmt2);
            rs2.next();
            l_offer_dtime = new DateTime(rs2.getTimestamp(1).getTime());
            pstmt2.close();
            rs2.close();
            // if they are not registered on or before the DOA, then no need to do any further checking

            if (l_offer_dtime.isAfter(in_doa)) {
                return l_answer;
            }
            // load all stays for the account for the current promo period, including the stay at hand, into the temp

            // table, recent_stay_list, created above

            l_link = (Integer) new pf_load_recent_stay_list().execute(in_acct_id, in_stay_id, in_promo_id, in_prop_id, in_doa, in_doa.plusDays(in_los), l_start_date, l_stop_date);
            l_temp_exists = "T";
            if (l_debug.equals("T")) {
                PreparedStatement pstmt3 = prepareStatement(
                          "select stay_id, prop_cd, doa, dod, linked_id, acct_trans_id, ord, post_dtime, res_source, points"
                        + " from recent_stay_list");
                
                ResultSet rs3 = executeQuery(pstmt3);
                while (rs3.next()) {
                    l_dbg_stay_id = rs3.getInt(1);
                    l_dbg_prop_cd = rs3.getString(2);
                    l_dbg_doa = new DateTime(rs3.getTimestamp(3).getTime());
                    l_dbg_dod = new DateTime(rs3.getTimestamp(4).getTime());
                    l_dbg_linked_id = rs3.getInt(5);
                    l_dbg_acct_trans_id = rs3.getInt(6);
                    l_dbg_ord = rs3.getInt(7);
                    l_dbg_post_dtime = new DateTime(rs3.getTimestamp(8).getTime());
                    l_res_source = rs3.getString(9);
                    l_points = rs3.getDouble(10);
                }
                pstmt3.close();
                rs3.close();
            }
            // regardless of this stay's eligibility, we need to check to see if it's consecutive with a stay(s)

            // that have already participated in the bonus. This call will insert into acct_trans_contrib if

            // that is the case and will return true. If true, then there is no need to proceed; the stay

            // at hand does not trigger a bonus, it's considered a non-contributing stay to the existing bonus.

            l_did_insert = (String) new pf_acct_trans_contrib_insert().execute(in_stay_id, in_promo_id, l_link);
            if (l_did_insert.equals("T")) {
                PreparedStatement pstmt4 = prepareStatement("drop table recent_stay_list");
                executeUpdate(pstmt4);
                pstmt4.close();
                return l_answer;
            }
            // check if economy and did it meet the two night minimum requirement

            l_economy = (String) new pf_iseconomy().execute(in_prop_id);
            if (l_economy.equals("T") && in_los > 1) {
                l_economy_eligible = "T";
            }
            // If we have a stay that is an economy stay that is not eligible due to the minimum LOS rule; 

            // i.e. only 1 night stay, we  need to check to see if it might be consecutive with

            // another stay (1 night or more, it does not matter) to meet the two night minimum requirement. 

            // If a consecutive, eligible stay is not found, then the stay at hand is not eligible period.

            if (l_economy.equals("T") && l_economy_eligible.equals("F")) {
                Iterator<Object> it0 = new pf_find_consecutive_economy().execute(in_acct_id, in_stay_id, in_doa, l_link, "SU0512O").iterator();;
                if (l_consec_econ.equals("F")) {
                    l_eligible = "F";
                }
                // no consecutive found

                else if (l_rules_met.equals("F")) {
                    // consecutive found but did not meet rules, see if stay at hand meets rules

                    l_eligible = (String) new pf_std_booking_rules().execute(in_acct_id, in_doa, in_los, in_res_source);
                }
                else {
                    l_eligible = "T";
                }
            }
            else {
                // midscale or at least a 2 night economy so see if it meets standard booking rules

                l_eligible = (String) new pf_std_booking_rules().execute(in_acct_id, in_doa, in_los, in_res_source);
            }
            // one final check before off to find second stay

            // we know the stay at hand has passed the registration check but

            // see if the stay at hand is consecutive with any earlier DOA stay where the earlier DOA

            // is BEFORE the registration date. That make the "stay" ineligible.

            PreparedStatement pstmt5 = prepareStatement(
                      "select count(*)"
                    + " from recent_stay_list"
                    + " where linked_id = ?"
                    + " and ? > doa");
            
            if (l_link != null) {
                pstmt5.setInt(1, l_link);
            }
            else {
                pstmt5.setNull(1, Types.JAVA_OBJECT);
            }
            if (l_offer_dtime != null) {
                pstmt5.setObject(2, l_offer_dtime);
            }
            else {
                pstmt5.setNull(2, Types.JAVA_OBJECT);
            }
            ResultSet rs4 = executeQuery(pstmt5);
            rs4.next();
            l_register_check = rs4.getInt(1);
            pstmt5.close();
            rs4.close();
            if (l_register_check > 0) {
                l_eligible = "F";
            }
            if (l_eligible.equals("T")) {
                // this routine will do the heavy lifting of pairing up the two "stays" and accumulating

                // the points used to calculate the bonus to be awarded.

                l_answer = (String) new pf_su0512s_awardbonus().execute(in_acct_id, in_promo_id, in_stay_type, in_prop_id, in_doa, in_los, in_rm_type, in_srp_code, in_rm_revenue, in_fb_revenue, in_other_revenue, in_curr_code, in_res_source, in_stay_id, l_link, l_offer_dtime);
            }
            PreparedStatement pstmt6 = prepareStatement("drop table recent_stay_list");
            executeUpdate(pstmt6);
            pstmt6.close();
            return l_answer;
        }
        catch (SQLException e) {
            sql_error = e.getErrorCode();
            isam_error = 0;
            error_data = e.getMessage();
            {
                if (l_temp_exists.equals("T")) {
                    PreparedStatement pstmt7 = prepareStatement("drop table recent_stay_list");
                    executeUpdate(pstmt7);
                    pstmt7.close();
                }
                throw new ProcedureException(sql_error, isam_error, error_data);
            }
        }
    }

}