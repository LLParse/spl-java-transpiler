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

public class pf_vanilla_elite_stay_twice extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public String execute(Integer in_acct_id, Integer in_promo_id, String in_stay_type, Integer in_prop_id, DateTime in_doa, Integer in_los, String in_rm_type, String in_srp_code, Double in_rm_revenue, Double in_fb_revenue, Double in_other_revenue, String in_curr_code, String in_res_source, Integer in_stay_id) throws SQLException, ProcedureException {
        /*
         * Promo filter for a generic, standard stay twice, special elite bonus promotion.
         *          The promotions using this filter will be configured by Marketing using the Promo Config Tool. 
         *          The Tool will configure a standard vanilla stay twice promotion, and have a check box to
         *          also indicate a flat, elite bonus promotion is also desired.
         *                      
         *          The standard, unchanging requirements for a stay to be awarded a bonus are:
         *    
         *              1) The account must be registered for the promotion on or before the DOA
         *              2) The account is Platinum or Diamond on the DOA
         *              3) Only accounts selected in the Promo Tool are eligible
         *              4) Booking source is ch.com or mobile     
         *              5) An econonmy stay must be at least 2 nights, consecutive stays meet criteria
         *              6) A midscale stay may only be 1 night to qualify
         *              7) Only one bonus per "stay"; i.e. check consecutiveness and multiple roooms
         *    
         * $Id: pf_vanilla_elite_stay_twice.sql 53328 2012-07-19 21:52:41Z rick_shepherd $
         *    
         *        Copyright (C) 2012 Choice Hotels International, Inc.
         */
        DateTime l_start_date;
        DateTime l_stop_date;
        String l_recog_cd;
        String l_offer_cd;
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
        Integer l_bonus_count;
        Integer l_acct_trans_id;
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
            l_recog_cd = null;
            l_offer_cd = null;
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
            l_bonus_count = 0;
            l_acct_trans_id = 0;
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

            l_debug = (String) new settrace().execute("pf_vanilla_elite_stay_twice");
            if (l_debug.equals("T")) {
                setDebugFile("/tmp/pf_vanilla_elite_stay_twice_" + dbinfo("sessionid") + ".trace");
                trace("on");
            }
            // Retrieve some promo and offer info

            PreparedStatement pstmt1 = prepareStatement(
                      "select p.start_date, p.stop_date, p.recog_cd, o.offer_cd"
                    + " from promo p, promo_acct_elig pae, offer o"
                    + " where p.promo_id = pae.promo_id"
                    + " and pae.offer_id = o.offer_id"
                    + " and p.promo_id = ?");
            
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
            l_recog_cd = rs1.getString(3);
            l_offer_cd = rs1.getString(4);
            pstmt1.close();
            rs1.close();
            if (l_offer_cd == null) {
                throw new ProcedureException(-746, 0, "pf_vanilla_elite_stay_twice: did not find offer_cd for promo_id" + in_promo_id);
            }
            // we know they are registered, otherwise we would not be in this filter. But we need

            // to see when they registered in relation to the DOA to see if we should proceed in this

            // filter.

            PreparedStatement pstmt2 = prepareStatement(
                      "select ao.offer_dtime"
                    + " from acct_offer ao, offer o"
                    + " where ao.offer_id = o.offer_id"
                    + " and ao.acct_id = ?"
                    + " and o.offer_cd = ?");
            
            if (in_acct_id != null) {
                pstmt2.setInt(1, in_acct_id);
            }
            else {
                pstmt2.setNull(1, Types.JAVA_OBJECT);
            }
            if (l_offer_cd != null) {
                pstmt2.setString(2, l_offer_cd);
            }
            else {
                pstmt2.setNull(2, Types.JAVA_OBJECT);
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
            // check that they are Platinum or Diamond on DOA. If not, then not eligible

            l_platinum = (String) new pf_isplatinum_ondoa().execute(in_acct_id, in_doa);
            if (l_platinum.equals("F")) {
                l_diamond = (String) new pf_isdiamond_ondoa().execute(in_acct_id, in_doa);
            }
            if (l_platinum.equals("F") && l_diamond.equals("F")) {
                return l_answer;
            }
            // they must book ch.com or mobile

            if (in_res_source == null || (!in_res_source.equals("N") && !in_res_source.equals("M"))) {
                return l_answer;
            }
            // Now know account is registered, is at the appropriate elite level, and booked properly. So next,

            // load all stays for the account for the current promo period, including the stay at hand, into the temp

            // table, recent_stay_list

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
                Iterator<Object> it0 = new pf_find_consecutive_economy_norules().execute(in_acct_id, in_stay_id, in_doa, l_link, l_offer_cd).iterator();;
                if (l_consec_econ.equals("F")) {
                    l_eligible = "F";
                }
                // no consecutive found

                else {
                    l_eligible = "T";
                }
            }
            else {
                // midscale or at least a 2 night economy so it is eligible

                l_eligible = "T";
            }
            // one final check before off to find second stay

            // we know the stay at hand has passed the registration check but

            // see if the stay at hand is consecutive with any earlier DOA stay where the earlier DOA

            // is BEFORE the registration date. That makes the "stay" ineligible.

            PreparedStatement pstmt4 = prepareStatement(
                      "select count(*)"
                    + " from recent_stay_list"
                    + " where linked_id = ?"
                    + " and ? > doa");
            
            if (l_link != null) {
                pstmt4.setInt(1, l_link);
            }
            else {
                pstmt4.setNull(1, Types.JAVA_OBJECT);
            }
            if (l_offer_dtime != null) {
                pstmt4.setObject(2, l_offer_dtime);
            }
            else {
                pstmt4.setNull(2, Types.JAVA_OBJECT);
            }
            ResultSet rs4 = executeQuery(pstmt4);
            rs4.next();
            l_register_check = rs4.getInt(1);
            pstmt4.close();
            rs4.close();
            if (l_register_check > 0) {
                l_eligible = "F";
            }
            if (l_eligible.equals("T")) {
                // now see if any consecutive stays have been awarded the bonus

                PreparedStatement pstmt5 = prepareStatement(
                          "select acct_trans_id"
                        + " from recent_stay_list"
                        + " where linked_id = ?"
                        + " and stay_id != ?");
                
                if (l_link != null) {
                    pstmt5.setInt(1, l_link);
                }
                else {
                    pstmt5.setNull(1, Types.JAVA_OBJECT);
                }
                if (in_stay_id != null) {
                    pstmt5.setInt(2, in_stay_id);
                }
                else {
                    pstmt5.setNull(2, Types.JAVA_OBJECT);
                }
                ResultSet rs5 = executeQuery(pstmt5);
                while (rs5.next()) {
                    l_acct_trans_id = rs5.getInt(1);
                    // do not think we care about order since we've already processed same doa      

                    // order by doa, points desc  

                    PreparedStatement pstmt6 = prepareStatement(
                              "select count(*)"
                            + " from acct_trans a"
                            + "  inner join acct_trans_detail ad on a.acct_trans_id = ad.acct_trans_id"
                            + " where a.acct_trans_id = ?"
                            + " and a.rev_acct_trans_id is null"
                            + " and ad.promo_id = ?");
                    
                    if (l_acct_trans_id != null) {
                        pstmt6.setInt(1, l_acct_trans_id);
                    }
                    else {
                        pstmt6.setNull(1, Types.JAVA_OBJECT);
                    }
                    if (in_promo_id != null) {
                        pstmt6.setInt(2, in_promo_id);
                    }
                    else {
                        pstmt6.setNull(2, Types.JAVA_OBJECT);
                    }
                    ResultSet rs6 = executeQuery(pstmt6);
                    rs6.next();
                    l_bonus_count = rs6.getInt(1);
                    pstmt6.close();
                    rs6.close();
                    if (l_bonus_count > 0) {
                        l_eligible = "F";
                        // no soup for you

                        break;
                    }
                }
                pstmt5.close();
                rs5.close();
            }
            l_answer = l_eligible;
            PreparedStatement pstmt7 = prepareStatement("drop table recent_stay_list");
            executeUpdate(pstmt7);
            pstmt7.close();
            return l_answer;
        }
        catch (SQLException e) {
            sql_error = e.getErrorCode();
            isam_error = 0;
            error_data = e.getMessage();
            {
                if (l_temp_exists.equals("T")) {
                    PreparedStatement pstmt8 = prepareStatement("drop table recent_stay_list");
                    executeUpdate(pstmt8);
                    pstmt8.close();
                }
                throw new ProcedureException(sql_error, isam_error, error_data);
            }
        }
    }

}