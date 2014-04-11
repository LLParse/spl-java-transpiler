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

public class pf_vanilla_revenue_reg extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public String execute(Integer in_acct_id, Integer in_promo_id, String in_stay_type, Integer in_prop_id, DateTime in_doa, Integer in_los, String in_rm_type, String in_srp_code, Double in_rm_revenue, Double in_fb_revenue, Double in_other_revenue, String in_curr_code, String in_res_source, Integer in_stay_id) throws SQLException, ProcedureException {
        /*
         * Promo filter for a generic, standard, vanilla flavored revenue type promotion. 
         *          The promotions using this filter will be configured by Marketing using the Promo Config Tool
         *          or can be loaded with the batch CampaignImport loader program using an xml input file.
         *                      
         *          The standard, unchanging requirements for a stay to be awarded a bonus are:
         *    
         *              1) The account must be registered for the promotion
         *              2) DOAs of stay must be on or after registration date
         *              3) Booking source is configured in promo filter item for promotion
         *                 e.g. ch.com, central res, gds, mobile unless Platinum or Diamond elite (see 5)
         *              4) Signup stay automatically qualifies if booking source not met (signup date during stay AND was done at the hotel)
         *              5) Platinum or Diamond accounts can book through any channel
         *              6) Reservation date requirements are configured in promo_filter_item entries; i.e.
         *                 - reservation is within 'x' days prior to DOA
         *                 - reservation is between 'a' and 'b' dates
         *              6) promo.prerequisite_stays is checked to see if 'n' number of stays must have been completed before
         *                 bonuses are awarded.
         *                 
         *          NOTE: If the promotion has a max_amount or bonus cap limit, that check if deferred to the promo_value
         *          handler. All promotions in the campaign with a promo.class = 'RVCAP' must be examined to get a sum of points 
         *          across all promotions.
         *    
         *        $Id: pf_vanilla_revenue_reg.sql 57606 2012-10-30 22:12:08Z rick_shepherd $
         *    
         *        Copyright (C) 2012 Choice Hotels International, Inc.
         */
        // define local variables to be used in the procedure

        DateTime l_start_date;
        DateTime l_stop_date;
        DateTime l_doa;
        DateTime l_doa2;
        String l_recog_cd;
        String l_offer_cd;
        String l_promo_cd;
        DateTime l_offer_dtime;
        Integer l_link;
        String l_answer;
        String l_debug;
        String l_temp_exists;
        String l_check_consecutive;
        Integer l_stay_count;
        Integer l_stay_id;
        Integer l_prerequisite_stays;
        String l_srp_code;
        Double l_points;
        Integer l_bonus_cnt;
        Integer l_acct_trans_id;
        // debug variables for dumping out the temporary recent_stay_list table if debug trace is turned on

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
        // variables for thrown exceptions

        Integer sql_error;
        Integer isam_error;
        String error_data;
        try {
            // initialize all local variables

            l_start_date = null;
            l_stop_date = null;
            l_doa = null;
            l_doa2 = null;
            l_recog_cd = null;
            l_offer_cd = null;
            l_promo_cd = null;
            l_offer_dtime = null;
            l_link = null;
            l_answer = "F";
            l_debug = "F";
            l_temp_exists = "F";
            l_check_consecutive = "F";
            l_stay_count = 0;
            l_stay_id = null;
            l_prerequisite_stays = 0;
            l_srp_code = null;
            l_points = 0.0;
            l_bonus_cnt = null;
            l_acct_trans_id = null;
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

            l_debug = (String) new settrace().execute("pf_vanilla_revenue_reg");
            if (l_debug.equals("T")) {
                setDebugFile("/tmp/pf_vanilla_revenue_reg_" + dbinfo("sessionid") + ".trace");
                trace("on");
            }
            // Retrieve some promo and offer info

            PreparedStatement pstmt1 = prepareStatement(
                      "select p.start_date, p.stop_date, p.recog_cd, o.offer_cd, p.prerequisite_stays, p.promo_cd"
                    + " from promo p"
                    + "  inner join promo_acct_elig pae on p.promo_id = pae.promo_id inner join offer o on pae.offer_id = o.offer_id"
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
            l_recog_cd = rs1.getString(3);
            l_offer_cd = rs1.getString(4);
            l_prerequisite_stays = rs1.getInt(5);
            l_promo_cd = rs1.getString(6);
            pstmt1.close();
            rs1.close();
            if (l_offer_cd == null) {
                throw new ProcedureException(-746, 0, "pf_vanilla_revenue_reg: did not find offer_cd for promo_id" + in_promo_id);
            }
            // we know they are registered, otherwise we would not be in this filter. But we need

            // to see when they registered in relation to the DOA to see if we should proceed in this

            // filter.

            PreparedStatement pstmt2 = prepareStatement(
                      "select ao.offer_dtime"
                    + " from acct_offer ao"
                    + "  inner join offer o on ao.offer_id = o.offer_id"
                    + " where ao.acct_id = ?"
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
            // see if any booking requirements were met

            Iterator<Object> it0 = new pf_revenue_booking().execute(in_acct_id, in_doa, in_los, in_res_source, in_stay_id, l_promo_cd).iterator();;
            if (l_answer.equals("F")) {
                return l_answer;
            }
            // if we have a number of stays required before we begin awarding a bonus, then we need to check

            // for consecutiveness to make sure the prerequisite number of stays is met. For example, if 

            // prerequisite_stays is 1, and we have check-in/check-outs on 3/1, 3/2, 3/3 at the samp property, then those 3 stays

            // are really on one logical stay so a bonus would not be awarded until, for example, a 3/6 stay.

            // We also have to be aware of SRD (redemption) stays. Those get returned in the recent_stay_list temp table,

            // but they should not count as a first stay.

            // For consecutive stays, we need to find at least one where the DOA was on or after the registration date in

            // order for the stay to be counted.

            if (l_prerequisite_stays != null) {
                l_link = (Integer) new pf_load_recent_stay_list().execute(in_acct_id, in_stay_id, in_promo_id, in_prop_id, in_doa, in_doa.plusDays(in_los), l_start_date, l_stop_date);
                l_temp_exists = "T";
                // if we are debugging, dump out recent_stay_list table

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
                        l_dbg_res_source = rs3.getString(9);
                        l_dbg_points = rs3.getDouble(10);
                    }
                    pstmt3.close();
                    rs3.close();
                }
                // count how many stays there have been since registration

                PreparedStatement pstmt4 = prepareStatement(
                          "select stay_id, doa, srp_code"
                        + " from recent_stay_list"
                        + " where stay_id = linked_id"
                        + " order by doa");
                
                ResultSet rs4 = executeQuery(pstmt4);
                while (rs4.next()) {
                    l_stay_id = rs4.getInt(1);
                    l_doa = new DateTime(rs4.getTimestamp(2).getTime());
                    l_srp_code = rs4.getString(3);
                    // if we have an SRD stay, check others that are linked to it in case one of them was

                    // a point earning stay. If earned points and is on or after registration then we 

                    // can count this "stay" and move on to the next parent.

                    if (l_srp_code != null && l_srp_code.equals("SRD")) {
                        PreparedStatement pstmt5 = prepareStatement(
                                  "select doa, points"
                                + " from recent_stay_list"
                                + " where linked_id = ?");
                        
                        if (l_stay_id != null) {
                            pstmt5.setInt(1, l_stay_id);
                        }
                        else {
                            pstmt5.setNull(1, Types.JAVA_OBJECT);
                        }
                        ResultSet rs5 = executeQuery(pstmt5);
                        while (rs5.next()) {
                            l_doa2 = new DateTime(rs5.getTimestamp(1).getTime());
                            l_points = rs5.getDouble(2);
                            if (l_points > 0.0 && (l_doa2.isAfter(l_offer_dtime) || l_doa2.isEqual(l_offer_dtime))) {
                                l_stay_count = l_stay_count + 1;
                                break;
                            }
                        }
                        pstmt5.close();
                        rs5.close();
                    }
                    // not SRD so just check registration date against DOA

                    else if ((l_doa.isAfter(l_offer_dtime) || l_doa.isEqual(l_offer_dtime))) {
                        l_stay_count = l_stay_count + 1;
                        continue;
                    }
                }
                pstmt4.close();
                rs4.close();
                if (l_stay_count <= l_prerequisite_stays) {
                    l_answer = "F";
                }
            }
            // make check for stay with same doa as input stay and already awarded a bonus

            if (l_answer.equals("T")) {
                l_doa = null;
                l_stay_id = null;
                PreparedStatement pstmt6 = prepareStatement(
                          "select stay_id, acct_trans_id, doa"
                        + " from recent_stay_list"
                        + " where linked_id = ?"
                        + " and stay_id != ?");
                
                if (l_link != null) {
                    pstmt6.setInt(1, l_link);
                }
                else {
                    pstmt6.setNull(1, Types.JAVA_OBJECT);
                }
                if (in_stay_id != null) {
                    pstmt6.setInt(2, in_stay_id);
                }
                else {
                    pstmt6.setNull(2, Types.JAVA_OBJECT);
                }
                ResultSet rs6 = executeQuery(pstmt6);
                while (rs6.next()) {
                    l_stay_id = rs6.getInt(1);
                    l_acct_trans_id = rs6.getInt(2);
                    l_doa = new DateTime(rs6.getTimestamp(3).getTime());
                    l_bonus_cnt = 0;
                    if (l_doa.isEqual(in_doa)) {
                        PreparedStatement pstmt7 = prepareStatement(
                                  "select count(*)"
                                + " from acct_trans a"
                                + "  inner join acct_trans_detail ad on a.acct_trans_id = ad.acct_trans_id"
                                + " where a.acct_trans_id = ?"
                                + " and a.rev_acct_trans_id is null"
                                + " and a.acct_id = ?"
                                + " and ad.promo_id = ?");
                        
                        if (l_acct_trans_id != null) {
                            pstmt7.setInt(1, l_acct_trans_id);
                        }
                        else {
                            pstmt7.setNull(1, Types.JAVA_OBJECT);
                        }
                        if (in_acct_id != null) {
                            pstmt7.setInt(2, in_acct_id);
                        }
                        else {
                            pstmt7.setNull(2, Types.JAVA_OBJECT);
                        }
                        if (in_promo_id != null) {
                            pstmt7.setInt(3, in_promo_id);
                        }
                        else {
                            pstmt7.setNull(3, Types.JAVA_OBJECT);
                        }
                        ResultSet rs7 = executeQuery(pstmt7);
                        rs7.next();
                        l_bonus_cnt = rs7.getInt(1);
                        pstmt7.close();
                        rs7.close();
                    }
                    if (l_bonus_cnt > 0) {
                        l_answer = "F";
                        break;
                    }
                }
                pstmt6.close();
                rs6.close();
            }
            // if we've not passed above tests, then get out, dropping temp table if it was created

            if (l_answer.equals("F")) {
                if (l_temp_exists.equals("T")) {
                    PreparedStatement pstmt8 = prepareStatement("drop table recent_stay_list");
                    executeUpdate(pstmt8);
                    pstmt8.close();
                }
                return l_answer;
            }
            // Stay appears eligible, but we need to see if consecutiveness needs to be

            // checked and if so, is the stay consecutive with any other stays that may have been 

            // awarded the bonus before giving a final thumbs up.

            if (l_check_consecutive.equals("T")) {
                if (l_temp_exists.equals("F")) {
                    l_link = (Integer) new pf_load_recent_stay_list().execute(in_acct_id, in_stay_id, in_promo_id, in_prop_id, in_doa, in_doa.plusDays(in_los), l_start_date, l_stop_date);
                    l_temp_exists = "T";
                }
                l_answer = (String) new pf_vanilla_revenue_awardbonus().execute(in_acct_id, in_promo_id, in_stay_type, in_prop_id, in_doa, in_los, in_rm_type, in_srp_code, in_rm_revenue, in_fb_revenue, in_other_revenue, in_curr_code, in_res_source, in_stay_id, l_link);
            }
            if (l_temp_exists.equals("T")) {
                PreparedStatement pstmt9 = prepareStatement("drop table recent_stay_list");
                executeUpdate(pstmt9);
                pstmt9.close();
            }
            return l_answer;
        }
        catch (SQLException e) {
            sql_error = e.getErrorCode();
            isam_error = 0;
            error_data = e.getMessage();
            {
                if (l_temp_exists.equals("T")) {
                    PreparedStatement pstmt10 = prepareStatement("drop table recent_stay_list");
                    executeUpdate(pstmt10);
                    pstmt10.close();
                }
                throw new ProcedureException(sql_error, isam_error, error_data);
            }
        }
    }

}