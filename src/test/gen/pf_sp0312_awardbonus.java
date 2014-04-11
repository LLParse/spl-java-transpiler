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

public class pf_sp0312_awardbonus extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public String execute(Integer in_acct_id, Integer in_promo_id, String in_stay_type, Integer in_prop_id, DateTime in_doa, Integer in_los, String in_rm_type, String in_srp_code, Double in_rm_revenue, Double in_fb_revenue, Double in_other_revenue, String in_curr_code, String in_res_source, Integer in_stay_id, Integer in_link, DateTime in_offer_dtime) throws SQLException, ProcedureException {
        /*
         * pf_sp0312_awardbonus.sql - If this routine is called, then the stay being processed meets the
         *                           basic Spring 2012 criteria. Now see if the bonus should be awarded; i.e.
         *                           are there two stays that meet the criteria; the current one being
         *                           looked at and one previous. If so, insert the point values into
         *                           a temp table to be used by the pv_sp0312p routine to calculate
         *                           the bonus points awarded.
         *                           
         *                           Caveat: if this is an economy stay, we have already verified it is for
         *                           at least two nights and eligible or that there is a consecutive, eligible
         *                           stay in the recent_stay_list table. So, we just need to find a second, 
         *                           qualifying stay to award the bonus.
         *                           
         *                           Caveat: we have already determined if the stay at hand is consecutive with
         *                           a stay that has already been awarded the bonus, and alread inserted an
         *                           entry in the acct_trans_contrib table.
         *                           
         *                           Caveat: the eligible stay at hand may be linked to prior stays that were
         *                           not eligible and have not participated in a bonus. In this case, the stay at hand
         *                           must be combined with these linked stays to form the triggering stay. So all
         *                           combined stays must be stored in the acct_trans_contrib if a second stay is
         *                           found to pair with the triggering stay.
         *                           
         *                           TODO: multi-room issue
         *                           
         *    
         *        $Id: pf_sp0312_awardbonus.sql 49703 2012-04-06 20:46:25Z rick_shepherd $
         *    
         *        Copyright (C) 2012 Choice Hotels International, Inc.
         */
        Integer l_stay_id;
        Integer l_stay_id2;
        String l_prop_cd;
        String l_prop_cd2;
        Integer l_prop_id;
        Integer l_samedoa;
        DateTime l_current_doa;
        DateTime l_doa;
        DateTime l_doa2;
        DateTime l_dod;
        DateTime l_dod2;
        Integer l_linked_id;
        Integer l_trans_id;
        Integer l_trans_id2;
        Integer l_acct_trans_id;
        Double l_current_amount;
        Integer l_total_qualified;
        Double l_prop_curr_rate;
        Double l_earn_curr_rate;
        String l_chain_group_cd;
        Double l_current_points;
        Double l_linked_points;
        Double l_linked_amount;
        Integer l_total_points;
        Double l_pts_dollar;
        String l_eligible;
        Integer l_linked_stays;
        Integer l_linked_same_doa;
        Integer l_act_acct_trans_id;
        Integer l_act_contrib_acct_trans_id;
        String l_res_source;
        String l_res_source2;
        String l_is_econo;
        String l_econo_eligible;
        Double l_same_doa_points;
        Integer l_bonus_cnt;
        String l_bonus_contrib;
        String l_economy;
        String l_economy_eligible;
        String l_consec_econ;
        String l_rules_met;
        String l_srp_code;
        Integer sql_error;
        Integer isam_error;
        String error_data;
        try {
            l_stay_id = null;
            l_stay_id2 = null;
            l_linked_id = null;
            l_prop_cd = null;
            l_prop_cd2 = null;
            l_prop_id = null;
            l_samedoa = 0;
            l_doa = null;
            l_doa2 = null;
            l_current_doa = null;
            l_dod = null;
            l_dod2 = null;
            l_trans_id = null;
            l_trans_id2 = null;
            l_acct_trans_id = 0;
            l_current_amount = 0.0;
            l_total_qualified = 0;
            l_prop_curr_rate = 0.0;
            l_earn_curr_rate = 0.0;
            l_chain_group_cd = null;
            l_current_points = 0.0;
            l_linked_points = 0.0;
            l_linked_amount = 0.0;
            l_total_points = 0;
            l_pts_dollar = 0.0;
            l_eligible = null;
            l_linked_stays = 0;
            l_linked_same_doa = 0;
            l_act_acct_trans_id = null;
            l_act_contrib_acct_trans_id = null;
            l_res_source = null;
            l_res_source2 = null;
            l_is_econo = "F";
            l_econo_eligible = "F";
            l_same_doa_points = null;
            l_bonus_cnt = 0;
            l_bonus_contrib = "N";
            l_economy = "F";
            l_economy_eligible = "F";
            l_consec_econ = "F";
            l_rules_met = "F";
            l_srp_code = null;
            // The stay being processed does not have an acct_trans_detail record

            // stored yet. So, we need to calculate its points based on rates,

            // property type, currency, etc.. Gather up that info then calculate

            // the number of points for the stay.

            PreparedStatement pstmt1 = prepareStatement(
                      "select s.prop_curr_rate, s.earning_curr_rate"
                    + " from stay s"
                    + " where s.stay_id = ?");
            
            if (in_stay_id != null) {
                pstmt1.setInt(1, in_stay_id);
            }
            else {
                pstmt1.setNull(1, Types.JAVA_OBJECT);
            }
            ResultSet rs1 = executeQuery(pstmt1);
            rs1.next();
            l_prop_curr_rate = rs1.getDouble(1);
            l_earn_curr_rate = rs1.getDouble(2);
            pstmt1.close();
            rs1.close();
            PreparedStatement pstmt2 = prepareStatement(
                      "select cg.chain_group_cd"
                    + " from chain_group cg, chain_group_detail cgd, prop p"
                    + " where cg.chain_group_id = cgd.chain_group_id"
                    + " and cgd.chain_id = p.chain_id"
                    + " and p.prop_id = ?");
            
            if (in_prop_id != null) {
                pstmt2.setInt(1, in_prop_id);
            }
            else {
                pstmt2.setNull(1, Types.JAVA_OBJECT);
            }
            ResultSet rs2 = executeQuery(pstmt2);
            rs2.next();
            l_chain_group_cd = rs2.getString(1);
            pstmt2.close();
            rs2.close();
            l_current_amount = trunc(new convert_currency().execute(l_prop_curr_rate, l_earn_curr_rate, in_rm_revenue));
            if ((l_chain_group_cd.equals("MS") || l_chain_group_cd.equals("US"))) {
                l_pts_dollar = 10.0;
            }
            else {
                l_pts_dollar = 5.0;
            }
            l_current_points = l_current_amount * l_pts_dollar;
            // Temp table passed to pv_sp0312p. It will hold the point values

            // for the stays making up the award bonus. Should be at least two entries

            // for any bonus awarded.

            // The stay at hand is eligible or is eligible due to the linked stays in the

            // recent_stays_list temp table. So, roll up any points for this first stay.

            l_current_doa = null;
            l_linked_stays = 0;
            // get any multi-room/same doa stays for stay at hand and pick the lowest point value as the contributing stay. 

            // We must store acct_trans_contrib entries for each one, however, with only the lowest point value room

            // getting the nod as the contributing stay.

            // Can this happen? yes:

            //   room one processed and was not eligible

            //   room two processed and was eligible 

            PreparedStatement pstmt3 = prepareStatement(
                      "select stay_id, acct_trans_id, points"
                    + " from recent_stay_list"
                    + " where linked_id = ?"
                    + " and stay_id != ?"
                    + " and doa = ?"
                    + " order by points");
            
            if (in_link != null) {
                pstmt3.setInt(1, in_link);
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
            if (in_doa != null) {
                pstmt3.setObject(3, in_doa);
            }
            else {
                pstmt3.setNull(3, Types.JAVA_OBJECT);
            }
            ResultSet rs3 = executeQuery(pstmt3);
            while (rs3.next()) {
                l_stay_id = rs3.getInt(1);
                l_acct_trans_id = rs3.getInt(2);
                l_linked_amount = rs3.getDouble(3);
                if (l_samedoa.equals(0) && l_linked_amount < l_current_points) {
                    l_bonus_contrib = "Y";
                    l_samedoa = 1;
                    l_current_points = l_linked_amount;
                }
                // lower point doa replaces stay at hand

                else {
                    l_bonus_contrib = "N";
                }
                PreparedStatement pstmt4 = prepareInsert(
                          "insert into t_acct_trans_contrib (promo_id, contrib_stay_id, contrib_acct_trans_id, bonus_contrib)"
                        + " values (?, ?, ?, ?)");
                if (in_promo_id != null) {
                    pstmt4.setInt(1, in_promo_id);
                }
                else {
                    pstmt4.setNull(1, Types.JAVA_OBJECT);
                }
                if (l_stay_id != null) {
                    pstmt4.setInt(2, l_stay_id);
                }
                else {
                    pstmt4.setNull(2, Types.JAVA_OBJECT);
                }
                if (l_acct_trans_id != null) {
                    pstmt4.setInt(3, l_acct_trans_id);
                }
                else {
                    pstmt4.setNull(3, Types.JAVA_OBJECT);
                }
                if (l_bonus_contrib != null) {
                    pstmt4.setString(4, l_bonus_contrib);
                }
                else {
                    pstmt4.setNull(4, Types.JAVA_OBJECT);
                }
                executeUpdate(pstmt4);
                pstmt4.close();
            }
            pstmt3.close();
            rs3.close();
            // now process any consecutive stays

            PreparedStatement pstmt5 = prepareStatement(
                      "select stay_id, acct_trans_id, doa, points"
                    + " from recent_stay_list"
                    + " where linked_id = ?"
                    + " and stay_id != ?"
                    + " and doa != ?");
            
            if (in_link != null) {
                pstmt5.setInt(1, in_link);
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
            if (in_doa != null) {
                pstmt5.setObject(3, in_doa);
            }
            else {
                pstmt5.setNull(3, Types.JAVA_OBJECT);
            }
            ResultSet rs4 = executeQuery(pstmt5);
            while (rs4.next()) {
                l_stay_id = rs4.getInt(1);
                l_acct_trans_id = rs4.getInt(2);
                l_doa = new DateTime(rs4.getTimestamp(3).getTime());
                l_linked_amount = rs4.getDouble(4);
                // do not think we care about order since we've already processed same doa      

                //    order by doa, points desc  

                l_bonus_contrib = "N";
                // SRD stays may be present and are non-contributing

                if (l_linked_amount != null) {
                    l_linked_points = l_linked_points + l_linked_amount;
                    l_bonus_contrib = "Y";
                }
                PreparedStatement pstmt6 = prepareInsert(
                          "insert into t_acct_trans_contrib (promo_id, contrib_stay_id, contrib_acct_trans_id, bonus_contrib)"
                        + " values (?, ?, ?, ?)");
                if (in_promo_id != null) {
                    pstmt6.setInt(1, in_promo_id);
                }
                else {
                    pstmt6.setNull(1, Types.JAVA_OBJECT);
                }
                if (l_stay_id != null) {
                    pstmt6.setInt(2, l_stay_id);
                }
                else {
                    pstmt6.setNull(2, Types.JAVA_OBJECT);
                }
                if (l_acct_trans_id != null) {
                    pstmt6.setInt(3, l_acct_trans_id);
                }
                else {
                    pstmt6.setNull(3, Types.JAVA_OBJECT);
                }
                if (l_bonus_contrib != null) {
                    pstmt6.setString(4, l_bonus_contrib);
                }
                else {
                    pstmt6.setNull(4, Types.JAVA_OBJECT);
                }
                executeUpdate(pstmt6);
                pstmt6.close();
            }
            pstmt5.close();
            rs4.close();
            l_current_points = l_current_points + l_linked_points;
            // go ahead and insert points for current "stay"

            PreparedStatement pstmt7 = prepareInsert(
                      "insert into qualified_stays_pv_sp0312 (amount)"
                    + " values (?)");
            if (l_current_points != null) {
                pstmt7.setDouble(1, l_current_points);
            }
            else {
                pstmt7.setNull(1, Types.JAVA_OBJECT);
            }
            executeUpdate(pstmt7);
            pstmt7.close();
            // add stay at hand to acct_trans_contrib. If there was a same doa with fewer points, 

            // then stay at hand is non contributing

            if (l_samedoa.equals(1)) {
                l_bonus_contrib = "N";
            }
            else {
                l_bonus_contrib = "Y";
            }
            PreparedStatement pstmt8 = prepareInsert(
                      "insert into t_acct_trans_contrib (promo_id, contrib_stay_id, bonus_contrib)"
                    + " values (?, ?, ?)");
            if (in_promo_id != null) {
                pstmt8.setInt(1, in_promo_id);
            }
            else {
                pstmt8.setNull(1, Types.JAVA_OBJECT);
            }
            if (in_stay_id != null) {
                pstmt8.setInt(2, in_stay_id);
            }
            else {
                pstmt8.setNull(2, Types.JAVA_OBJECT);
            }
            if (l_bonus_contrib != null) {
                pstmt8.setString(3, l_bonus_contrib);
            }
            else {
                pstmt8.setNull(3, Types.JAVA_OBJECT);
            }
            executeUpdate(pstmt8);
            pstmt8.close();
            l_total_qualified = l_total_qualified + 1;
            // Milestone: we now have processed the stay at hand with any of its linked stays and

            // updated the appropriate temp tables. Now, see if there is a second "stay" that

            // can be paired with the stay at hand in order to award the bonus.

            // The cur1 cursor processes each stay that is not linked to a different stay and is

            // not the current stay at hand. In other words, take a look at all previous stays that

            // are linked to themselves.

            // We will see if any qualify to be paired with the stay at hand, or the triggering stay.

            // More or less same rules apply...already part of bonus, watch out for linked stays, multi-room 

            // stays, if any stay of a linked set qualifies, then the whole set qualifies, etc...

            // Also, even though a stay from the cursor may not be eligible, it might have linked stays

            // that ARE eligible, thus making the stay set eligible...PITA!! Also have the economy 2 nights

            // restriction, a bigger PITA.

            PreparedStatement pstmt9 = prepareStatement(
                      "select stay_id, linked_id, acct_trans_id, doa, dod, prop_cd, res_source"
                    + " from recent_stay_list"
                    + " where stay_id = linked_id"
                    + " and stay_id != ?"
                    + " order by post_dtime desc");
            
            if (in_link != null) {
                pstmt9.setInt(1, in_link);
            }
            else {
                pstmt9.setNull(1, Types.JAVA_OBJECT);
            }
            ResultSet rs5 = executeQuery(pstmt9);
            while (rs5.next()) {
                l_stay_id = rs5.getInt(1);
                l_linked_id = rs5.getInt(2);
                l_acct_trans_id = rs5.getInt(3);
                l_doa = new DateTime(rs5.getTimestamp(4).getTime());
                l_dod = new DateTime(rs5.getTimestamp(5).getTime());
                l_prop_cd = rs5.getString(6);
                l_res_source = rs5.getString(7);
                // look at latest stays posted since they are most likely to be eligible as second stay

                // is it an SRD stay with no points so we don't want to count it as a legit stay for the bonus

                // since it's a stand alone stay at this point. We should include in acct_trans_contrib if it is

                // consecutive with other stays

                PreparedStatement pstmt10 = prepareStatement(
                          "select s.srp_code"
                        + " from stay s"
                        + " where s.stay_id = ?");
                
                if (l_stay_id != null) {
                    pstmt10.setInt(1, l_stay_id);
                }
                else {
                    pstmt10.setNull(1, Types.JAVA_OBJECT);
                }
                ResultSet rs6 = executeQuery(pstmt10);
                rs6.next();
                l_srp_code = rs6.getString(1);
                pstmt10.close();
                rs6.close();
                if (l_srp_code != null && l_srp_code.equals("SRD")) {
                    continue;
                }
                // see if stay is already part of a bonus by checking acct_trans_contrib . Use a cursor even

                // though a stay should only be in acct_trans_contrib once; prevents an unwanted exception from

                // being thrown

                l_bonus_cnt = (Integer) new pf_acct_trans_contrib_chk().execute(l_stay_id, in_promo_id);
                if (l_bonus_cnt > 0) {
                    continue;
                }
                // see if this stay is eligible; return values whether it meets basic criteria, is an

                // economy stay and if the economy stay meets 2 night minimum requirement.

                l_prop_id = new get_prop_id_date().execute(l_prop_cd, l_doa);
                l_economy = (String) new pf_iseconomy().execute(l_prop_id);
                if (l_economy.equals("T") && l_dod.minusDays(l_doa) > 1) {
                    l_economy_eligible = "T";
                }
                // If we have a stay that is an economy stay that is not eligible due to the minimum LOS rule; 

                // i.e. only 1 night stay, we  need to check to see if it might be consecutive with

                // another stay (1 night or more, it does not matter) to meet the two night minimum requirement. 

                // If a consecutive, eligible stay is not found, then the stay at hand is not eligible period.

                if (l_economy.equals("T") && l_economy_eligible.equals("F")) {
                    Iterator<Object> it0 = new pf_find_consecutive_economy().execute(in_acct_id, l_stay_id, l_doa, l_linked_id, "SP0312O").iterator();;
                    if (l_consec_econ.equals("F")) {
                        l_eligible = "F";
                    }
                    // no consecutive found

                    else if (l_rules_met.equals("F")) {
                        // consecutive found but did not meet rules, see if stay at hand meets rules

                        l_eligible = (String) new pf_std_booking_rules().execute(in_acct_id, l_doa, l_dod.minusDays(l_doa), l_res_source);
                    }
                    else {
                        l_eligible = "T";
                    }
                }
                else {
                    // midscale or at least a 2 night economy so see if it meets standard booking rules

                    l_eligible = (String) new pf_std_booking_rules().execute(in_acct_id, l_doa, l_dod.minusDays(l_doa), l_res_source);
                }
                // does the doa meet the registration rule 

                if (in_offer_dtime.isAfter(l_doa)) {
                    l_eligible = "F";
                    continue;
                }
                if (l_eligible.equals("T")) {
                    break;
                }
                // l_stay_id is not eligible, but we now need to check if anything linked to it

                // makes it eligible; i.e. consecutive stays may be a mix of eligible and ineligible

                // stays. If any stay in the set is eligible, then the whole set is eligible.

                PreparedStatement pstmt11 = prepareStatement(
                          "select stay_id, linked_id, acct_trans_id, doa, dod, prop_cd, res_source"
                        + " from recent_stay_list"
                        + " where linked_id = ?"
                        + " and stay_id != ?"
                        + " order by doa, stay_id");
                
                if (l_stay_id != null) {
                    pstmt11.setInt(1, l_stay_id);
                }
                else {
                    pstmt11.setNull(1, Types.JAVA_OBJECT);
                }
                if (l_stay_id != null) {
                    pstmt11.setInt(2, l_stay_id);
                }
                else {
                    pstmt11.setNull(2, Types.JAVA_OBJECT);
                }
                ResultSet rs7 = executeQuery(pstmt11);
                while (rs7.next()) {
                    l_stay_id2 = rs7.getInt(1);
                    l_linked_id = rs7.getInt(2);
                    l_trans_id2 = rs7.getInt(3);
                    l_doa2 = new DateTime(rs7.getTimestamp(4).getTime());
                    l_dod2 = new DateTime(rs7.getTimestamp(5).getTime());
                    l_prop_cd2 = rs7.getString(6);
                    l_res_source2 = rs7.getString(7);
                    // we don't have to consider economy stays here since they would have been caught in the

                    // checks prior to this cursor. So we just need to check booking and registration rules

                    l_eligible = (String) new pf_std_booking_rules().execute(in_acct_id, l_doa2, l_dod2.minusDays(l_doa2), l_res_source2);
                    if (in_offer_dtime.isAfter(l_doa2)) {
                        l_eligible = "F";
                    }
                    if (l_eligible.equals("T")) {
                        break;
                    }
                }
                pstmt11.close();
                rs7.close();
            }
            pstmt9.close();
            rs5.close();
            if (l_eligible.equals("T")) {
                // we now have found a second stay to pair with the stay at hand. Now, accumulate the

                // points for this second stay.

                l_current_doa = null;
                PreparedStatement pstmt12 = prepareStatement(
                          "select stay_id, acct_trans_id, doa, points"
                        + " from recent_stay_list"
                        + " where linked_id = ?"
                        + " order by doa, points");
                
                if (l_stay_id != null) {
                    pstmt12.setInt(1, l_stay_id);
                }
                else {
                    pstmt12.setNull(1, Types.JAVA_OBJECT);
                }
                ResultSet rs8 = executeQuery(pstmt12);
                while (rs8.next()) {
                    l_stay_id2 = rs8.getInt(1);
                    l_trans_id2 = rs8.getInt(2);
                    l_doa2 = new DateTime(rs8.getTimestamp(3).getTime());
                    l_current_points = rs8.getDouble(4);
                    if (l_current_doa != null && l_doa2.isEqual(l_current_doa)) {
                        // don't count these points, but do insert into acct_trans_contrib

                        PreparedStatement pstmt13 = prepareInsert(
                                  "insert into t_acct_trans_contrib (promo_id, contrib_stay_id, contrib_acct_trans_id, bonus_contrib)"
                                + " values (?, ?, ?, \"N\")");
                        if (in_promo_id != null) {
                            pstmt13.setInt(1, in_promo_id);
                        }
                        else {
                            pstmt13.setNull(1, Types.JAVA_OBJECT);
                        }
                        if (l_stay_id2 != null) {
                            pstmt13.setInt(2, l_stay_id2);
                        }
                        else {
                            pstmt13.setNull(2, Types.JAVA_OBJECT);
                        }
                        if (l_trans_id2 != null) {
                            pstmt13.setInt(3, l_trans_id2);
                        }
                        else {
                            pstmt13.setNull(3, Types.JAVA_OBJECT);
                        }
                        executeUpdate(pstmt13);
                        pstmt13.close();
                        continue;
                    }
                    else {
                        l_current_doa = l_doa2;
                        l_bonus_contrib = "N";
                        if (l_current_points != null) {
                            l_total_points = l_total_points + l_current_points.intValue();
                            l_bonus_contrib = "Y";
                        }
                        PreparedStatement pstmt14 = prepareInsert(
                                  "insert into t_acct_trans_contrib (promo_id, contrib_stay_id, contrib_acct_trans_id, bonus_contrib)"
                                + " values (?, ?, ?, ?)");
                        if (in_promo_id != null) {
                            pstmt14.setInt(1, in_promo_id);
                        }
                        else {
                            pstmt14.setNull(1, Types.JAVA_OBJECT);
                        }
                        if (l_stay_id2 != null) {
                            pstmt14.setInt(2, l_stay_id2);
                        }
                        else {
                            pstmt14.setNull(2, Types.JAVA_OBJECT);
                        }
                        if (l_trans_id2 != null) {
                            pstmt14.setInt(3, l_trans_id2);
                        }
                        else {
                            pstmt14.setNull(3, Types.JAVA_OBJECT);
                        }
                        if (l_bonus_contrib != null) {
                            pstmt14.setString(4, l_bonus_contrib);
                        }
                        else {
                            pstmt14.setNull(4, Types.JAVA_OBJECT);
                        }
                        executeUpdate(pstmt14);
                        pstmt14.close();
                    }
                }
                pstmt12.close();
                rs8.close();
                if (l_total_points > 0.0) {
                    PreparedStatement pstmt15 = prepareInsert(
                              "insert into qualified_stays_pv_sp0312 (amount)"
                            + " values (?)");
                    if (l_total_points != null) {
                        pstmt15.setInt(1, l_total_points);
                    }
                    else {
                        pstmt15.setNull(1, Types.JAVA_OBJECT);
                    }
                    executeUpdate(pstmt15);
                    pstmt15.close();
                    l_total_qualified = l_total_qualified + 1;
                }
            }
            if (l_total_qualified < 2) {
                // Perhaps current stay qualified but no others so no bonus for you!

                // Delete anything we stored in the temp acct_trans_contrib table

                // so it is not persisted.

                PreparedStatement pstmt16 = prepareStatement(
                          "delete from t_acct_trans_contrib"
                        + " where promo_id = ?");
                if (in_promo_id != null) {
                    pstmt16.setInt(1, in_promo_id);
                }
                else {
                    pstmt16.setNull(1, Types.JAVA_OBJECT);
                }
                executeUpdate(pstmt16);
                pstmt16.close();
                PreparedStatement pstmt17 = prepareStatement("drop table qualified_stays_pv_sp0312");
                executeUpdate(pstmt17);
                pstmt17.close();
                return "F";
            }
            // all is good and bonus will be awarded

            return "T";
        }
        catch (SQLException e) {
            sql_error = e.getErrorCode();
            isam_error = 0;
            error_data = e.getMessage();
            {
                PreparedStatement pstmt18 = prepareStatement(
                          "delete from t_acct_trans_contrib"
                        + " where promo_id = ?");
                if (in_promo_id != null) {
                    pstmt18.setInt(1, in_promo_id);
                }
                else {
                    pstmt18.setNull(1, Types.JAVA_OBJECT);
                }
                executeUpdate(pstmt18);
                pstmt18.close();
                PreparedStatement pstmt19 = prepareStatement("drop table qualified_stays_pv_sp0312");
                executeUpdate(pstmt19);
                pstmt19.close();
                throw new ProcedureException(sql_error, isam_error, error_data);
            }
        }
    }

}