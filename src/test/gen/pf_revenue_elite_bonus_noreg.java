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

public class pf_revenue_elite_bonus_noreg extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public String execute(Integer in_acct_id, Integer in_promo_id, String in_stay_type, Integer in_prop_id, DateTime in_doa, Integer in_los, String in_rm_type, String in_srp_code, Double in_rm_revenue, Double in_fb_revenue, Double in_other_revenue, String in_curr_code, String in_res_source, Integer in_stay_id) throws SQLException, ProcedureException {
        /*
         * Promo filter for a generic special elite bonus promotion.
         *          The promotions using this filter may be configured by Marketing using the Promo Config Tool. 
         *          The Tool will configure a revenue times X promotion, and have a check box to
         *          also indicate a flat, elite bonus promotion is also desired.
         *                      
         *          The standard, unchanging requirements for a stay to be awarded a bonus are:
         *    
         *              1) The account must be registered for the promotion on or before the DOA
         *              2) The account is elite level Platinum or Diamond on the DOA
         *              3) Only accounts selected in the Promo Tool are eligible
         *              4) Booking source is ch.com or mobile  
         *              5) Only one bonus per "stay"; i.e. check consecutiveness and multiple roooms
         *    
         * $Id: pf_revenue_elite_bonus_noreg.sql 57251 2012-10-23 20:40:18Z rick_shepherd $
         *    
         *        Copyright (C) 2012 Choice Hotels International, Inc.
         */
        DateTime l_start_date;
        DateTime l_stop_date;
        Integer l_link;
        String l_platinum;
        String l_diamond;
        String l_eligible;
        String l_answer;
        String l_debug;
        String l_temp_exists;
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
        String l_dbg_res_source;
        Double l_dbg_points;
        Integer sql_error;
        Integer isam_error;
        String error_data;
        try {
            l_start_date = null;
            l_stop_date = null;
            l_link = null;
            l_platinum = "F";
            l_diamond = "F";
            l_eligible = "T";
            l_answer = "F";
            l_debug = "F";
            l_temp_exists = "F";
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
            l_dbg_res_source = null;
            l_dbg_points = null;
            // set up tracing based on app_config entry

            l_debug = (String) new settrace().execute("pf_revenue_elite_bonus_noreg");
            if (l_debug.equals("T")) {
                setDebugFile("/tmp/pf_revenue_elite_bonus_noreg_" + dbinfo("sessionid") + ".trace");
                trace("on");
            }
            // Retrieve some promo and offer info

            PreparedStatement pstmt1 = prepareStatement(
                      "select p.start_date, p.stop_date"
                    + " from promo p"
                    + " where p.promo_id = ?");
            
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
            // check that they are Platinum or Diamond on DOA. If not, then not eligible

            l_platinum = (String) new pf_isplatinum_ondoa().execute(in_acct_id, in_doa);
            if (l_platinum.equals("F")) {
                l_diamond = (String) new pf_isdiamond_ondoa().execute(in_acct_id, in_doa);
            }
            if (l_platinum.equals("F") && l_diamond.equals("F")) {
                return l_answer;
            }
            // Now know account is at the appropriate elite level. So next,

            // load all stays for the account for the current promo period, including the stay at hand, into the temp

            // table, recent_stay_list

            l_link = (Integer) new pf_load_recent_stay_list().execute(in_acct_id, in_stay_id, in_promo_id, in_prop_id, in_doa, in_doa.plusDays(in_los), l_start_date, l_stop_date);
            l_temp_exists = "T";
            if (l_debug.equals("T")) {
                PreparedStatement pstmt2 = prepareStatement(
                          "select stay_id, prop_cd, doa, dod, linked_id, acct_trans_id, ord, post_dtime, res_source, points"
                        + " from recent_stay_list");
                
                ResultSet rs2 = executeQuery(pstmt2);
                while (rs2.next()) {
                    l_dbg_stay_id = rs2.getInt(1);
                    l_dbg_prop_cd = rs2.getString(2);
                    l_dbg_doa = new DateTime(rs2.getTimestamp(3).getTime());
                    l_dbg_dod = new DateTime(rs2.getTimestamp(4).getTime());
                    l_dbg_linked_id = rs2.getInt(5);
                    l_dbg_acct_trans_id = rs2.getInt(6);
                    l_dbg_ord = rs2.getInt(7);
                    l_dbg_post_dtime = new DateTime(rs2.getTimestamp(8).getTime());
                    l_dbg_res_source = rs2.getString(9);
                    l_dbg_points = rs2.getDouble(10);
                }
                pstmt2.close();
                rs2.close();
            }
            // now see if any consecutive stays have been awarded the bonus

            PreparedStatement pstmt3 = prepareStatement(
                      "select acct_trans_id"
                    + " from recent_stay_list"
                    + " where linked_id = ?"
                    + " and stay_id != ?");
            
            if (l_link != null) {
                pstmt3.setInt(1, l_link);
            }
            else {
                pstmt3.setNull(1, Types.JAVA_OBJECT);
            }
            if (in_stay_id != null) {
                pstmt3.setInt(2, in_stay_id);
            }
            else {
                pstmt3.setNull(2, Types.JAVA_OBJECT);
            }
            ResultSet rs3 = executeQuery(pstmt3);
            while (rs3.next()) {
                l_acct_trans_id = rs3.getInt(1);
                // do not think we care about order since we've already processed same doa      

                // order by doa, points desc  

                PreparedStatement pstmt4 = prepareStatement(
                          "select count(*)"
                        + " from acct_trans a"
                        + "  inner join acct_trans_detail ad on a.acct_trans_id = ad.acct_trans_id"
                        + " where a.acct_trans_id = ?"
                        + " and a.rev_acct_trans_id is null"
                        + " and ad.promo_id = ?");
                
                if (l_acct_trans_id != null) {
                    pstmt4.setInt(1, l_acct_trans_id);
                }
                else {
                    pstmt4.setNull(1, Types.JAVA_OBJECT);
                }
                if (in_promo_id != null) {
                    pstmt4.setInt(2, in_promo_id);
                }
                else {
                    pstmt4.setNull(2, Types.JAVA_OBJECT);
                }
                ResultSet rs4 = executeQuery(pstmt4);
                rs4.next();
                l_bonus_count = rs4.getInt(1);
                pstmt4.close();
                rs4.close();
                if (l_bonus_count > 0) {
                    l_eligible = "F";
                    // no soup for you

                    break;
                }
            }
            pstmt3.close();
            rs3.close();
            l_answer = l_eligible;
            PreparedStatement pstmt5 = prepareStatement("drop table recent_stay_list");
            executeUpdate(pstmt5);
            pstmt5.close();
            return l_answer;
        }
        catch (SQLException e) {
            sql_error = e.getErrorCode();
            isam_error = 0;
            error_data = e.getMessage();
            {
                if (l_temp_exists.equals("T")) {
                    PreparedStatement pstmt6 = prepareStatement("drop table recent_stay_list");
                    executeUpdate(pstmt6);
                    pstmt6.close();
                }
                throw new ProcedureException(sql_error, isam_error, error_data);
            }
        }
    }

}