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

public class pf_vanilla_stay_twice_noreg extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public String execute(Integer in_acct_id, Integer in_promo_id, String in_stay_type, Integer in_prop_id, DateTime in_doa, Integer in_los, String in_rm_type, String in_srp_code, Double in_rm_revenue, Double in_fb_revenue, Double in_other_revenue, String in_curr_code, String in_res_source, Integer in_stay_id) throws SQLException, ProcedureException {
        /*
         * pf_vanilla_stay_twice_noreg.sql - 
         *           
         *          Promo filter for a generic, standard, vanilla flavored stay twice promotion with no registration
         *          and no booking source requirements.
         *          The promotions using this filter will be configured by Marketing using the Promo Config Tool. 
         *                      
         *          The standard, unchanging requirements for a stay to be awarded a bonus are:
         *    
         *              1) The account must be registered for the promotion
         *              2) An econonmy stay must be at least 2 nights, consecutive stays meet criteria
         *              3) A midscale stay may only be 1 night to qualify
         *                  
         *          If those criteria are met, then determine if this is the second qualifying stay before
         *          awarding the bonus. Note: the midscale and economy 2 night requirement is not checked in this procedure, 
         *          rather it is deferred to pf_vanilla_stay_twice_awardbonus_noreg since that is where all the stays for the
         *          promo period are retrieved and analyzed.
         *                  
         *    
         * $Id: pf_vanilla_stay_twice_noreg.sql 52644 2012-06-25 23:47:41Z rick_shepherd $
         *    
         *        Copyright (C) 2012 Choice Hotels International, Inc.
         */
        DateTime l_start_date;
        DateTime l_stop_date;
        Integer l_link;
        String l_eligible;
        String l_answer;
        String l_debug;
        String l_economy;
        String l_economy_eligible;
        String l_consec_econ;
        String l_res_source;
        String l_temp_exists;
        String l_did_insert;
        String l_rules_met;
        Double l_points;
        Integer l_dbg_stay_id;
        String l_dbg_prop_cd;
        DateTime l_dbg_doa;
        DateTime l_dbg_dod;
        Integer l_dbg_linked_id;
        Integer l_dbg_acct_trans_id;
        Integer l_dbg_ord;
        DateTime l_dbg_post_dtime;
        Integer sql_error;
        Integer isam_error;
        String error_data;
        try {
            l_start_date = null;
            l_stop_date = null;
            l_link = null;
            l_eligible = "F";
            l_answer = "F";
            l_debug = "F";
            l_economy = "F";
            l_economy_eligible = "F";
            l_consec_econ = "F";
            l_res_source = null;
            l_temp_exists = "F";
            l_did_insert = "F";
            l_rules_met = "F";
            l_points = null;
            l_dbg_stay_id = null;
            l_dbg_prop_cd = null;
            l_dbg_doa = null;
            l_dbg_dod = null;
            l_dbg_linked_id = null;
            l_dbg_acct_trans_id = null;
            l_dbg_ord = null;
            l_dbg_post_dtime = null;
            // set up tracing based on app_config entry

            l_debug = (String) new settrace().execute("pf_vanilla_stay_twice_noreg");
            if (l_debug.equals("T")) {
                setDebugFile("/tmp/pf_vanilla_stay_twice_noreg_" + dbinfo("sessionid") + ".trace");
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
            // load all stays for the account for the current promo period, including the stay at hand, into the temp

            // table, recent_stay_list, created above

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
                    l_res_source = rs2.getString(9);
                    l_points = rs2.getDouble(10);
                }
                pstmt2.close();
                rs2.close();
            }
            // regardless of this stay's eligibility, we need to check to see if it's consecutive with a stay(s)

            // that have already participated in the bonus. This call will insert into acct_trans_contrib if

            // that is the case and will return true. If true, then there is no need to proceed; the stay

            // at hand does not trigger a bonus, it's considered a non-contributing stay to the existing bonus.

            l_did_insert = (String) new pf_acct_trans_contrib_insert().execute(in_stay_id, in_promo_id, l_link);
            if (l_did_insert.equals("T")) {
                PreparedStatement pstmt3 = prepareStatement("drop table recent_stay_list");
                executeUpdate(pstmt3);
                pstmt3.close();
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
                Iterator<Object> it0 = new pf_find_consecutive_economy_norules().execute(in_acct_id, in_stay_id, in_doa, l_link, null).iterator();;
                if (l_consec_econ.equals("T")) {
                    l_eligible = "T";
                }
            }
            else {
                l_eligible = "T";
            }
            if (l_eligible.equals("T")) {
                l_answer = (String) new pf_vanilla_stay_twice_awardbonus_noreg().execute(in_acct_id, in_promo_id, in_stay_type, in_prop_id, in_doa, in_los, in_rm_type, in_srp_code, in_rm_revenue, in_fb_revenue, in_other_revenue, in_curr_code, in_res_source, in_stay_id, l_link);
            }
            PreparedStatement pstmt4 = prepareStatement("drop table recent_stay_list");
            executeUpdate(pstmt4);
            pstmt4.close();
            return l_answer;
        }
        catch (SQLException e) {
            sql_error = e.getErrorCode();
            isam_error = 0;
            error_data = e.getMessage();
            {
                if (l_temp_exists.equals("T")) {
                    PreparedStatement pstmt5 = prepareStatement("drop table recent_stay_list");
                    executeUpdate(pstmt5);
                    pstmt5.close();
                }
                throw new ProcedureException(sql_error, isam_error, error_data);
            }
        }
    }

}