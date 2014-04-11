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

public class pf_sp0311s_awardbonus extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public String execute(Integer in_acct_id, Integer in_promo_id, String in_stay_type, Integer in_prop_id, DateTime in_doa, Integer in_los, String in_rm_type, String in_srp_code, Double in_rm_revenue, Double in_fb_revenue, Double in_other_revenue, String in_curr_code, String in_res_source, Integer in_stay_id, String in_platinum, String in_diamond) throws SQLException, ProcedureException {
        /*
         * pf_sp0311s_awardbonus.sql - If this routine is called, then the stay being processed meets the
         *                           basic Spring 2011 criteria. Now see if the bonus should be awarded; i.e.
         *                           are there two stays that meet the criteria; the current one being
         *                           looked at and one previous. If so, insert the point values into
         *                           a temp table to be used by the pv_sp0311s routine to calculate
         *                           the bonus points awarded.
         *                           
         *                           Caveat: an Economy stay may only be for one night, but otherwise meets
         *                           the promotion criteria. Thus, this code must check if economy/one night
         *                           and see if there is another linked stay that makes the entire stay at 
         *                           least two nights. If so, it does not matter if previous stays are eligible
         *                           or not since the stay at hand is, so just combine them. If not, just get out.
         *                           
         *                           Caveat: it must be determined if the stay at hand is part of a stay that
         *                           has already been awarded a Spring bonus. If so, then the stay at hand
         *                           needs to be added to the acct_trans_contrib table as a non-contributing
         *                           stay for that bonus. This will prevent the stay at hand from being utilized
         *                           in future bonus determinations. ONce this is done, get out.
         *                           
         *                           Caveat: the eligible stay at hand may be linked to prior stays that were
         *                           not eligible and have not participated in a bonus. In this case, the stay at hand
         *                           must be combined with these linked stays to form the triggering stay. So all
         *                           combined stays must be stored in the acct_trans_contrib is a second stay is
         *                           found to pair with the triggering stay.
         *                           
         *    
         * $Id: pf_sp0311s_awardbonus.sql 38094 2011-04-07 18:53:02Z mgiroux $
         *    
         *        Copyright (C) 2011 Choice Hotels International, Inc.
         */
        DateTime l_start_date;
        DateTime l_stop_date;
        String l_recog_cd;
        Integer l_stay_id;
        Integer l_stay_id2;
        String l_prop_cd;
        String l_prop_cd2;
        DateTime l_current_doa;
        DateTime l_doa;
        DateTime l_doa2;
        DateTime l_dod;
        DateTime l_dod2;
        Integer l_linked_id;
        Integer l_trans_id;
        Integer l_trans_id2;
        DateTime f_dod;
        Integer n_id;
        Integer n_link;
        DateTime l_signup_date;
        Integer l_acct_trans_id;
        Double l_current_amount;
        Integer l_total_qualified;
        Integer l_ord;
        DateTime l_post_dtime;
        DateTime l_curr_post_dtime;
        Double l_prop_curr_rate;
        Double l_earn_curr_rate;
        String l_chain_group_cd;
        Double l_current_points;
        Double l_linked_points;
        Double l_linked_amount;
        Integer l_total_points;
        Double l_pts_dollar;
        String l_is_elig;
        Integer l_linked_stays;
        String l_act_insert_needed;
        Integer l_linked_same_doa;
        Integer l_act_acct_trans_id;
        Integer l_act_contrib_acct_trans_id;
        String l_is_econo;
        String l_econo_eligible;
        Double l_same_doa_points;
        String l_debug;
        Integer l_rev_acct_trans_id;
        Integer l_offer_id;
        Integer l_bonus_cnt;
        Integer l_max_awards;
        String l_registered;
        String l_made_eligible;
        Integer sql_error;
        Integer isam_error;
        String error_data;
        try {
            l_start_date = null;
            l_stop_date = null;
            l_recog_cd = null;
            l_stay_id = null;
            l_stay_id2 = null;
            l_linked_id = null;
            l_prop_cd = null;
            l_prop_cd2 = null;
            l_doa = null;
            l_doa2 = null;
            l_current_doa = null;
            l_dod = null;
            l_dod2 = null;
            l_trans_id = null;
            l_trans_id2 = null;
            f_dod = in_doa.plusDays(in_los);
            n_id = null;
            n_link = null;
            l_signup_date = null;
            l_acct_trans_id = 0;
            l_current_amount = 0.0;
            l_total_qualified = 0;
            l_ord = 1;
            l_post_dtime = null;
            l_curr_post_dtime = null;
            l_prop_curr_rate = 0.0;
            l_earn_curr_rate = 0.0;
            l_chain_group_cd = null;
            l_current_points = 0.0;
            l_linked_points = 0.0;
            l_linked_amount = 0.0;
            l_total_points = 0;
            l_pts_dollar = 0.0;
            l_is_elig = null;
            l_linked_stays = 0;
            l_act_insert_needed = "F";
            l_linked_same_doa = 0;
            l_act_acct_trans_id = null;
            l_act_contrib_acct_trans_id = null;
            l_is_econo = "F";
            l_econo_eligible = "F";
            l_same_doa_points = null;
            l_debug = "F";
            l_rev_acct_trans_id = null;
            l_offer_id = null;
            l_bonus_cnt = 0;
            l_max_awards = 1;
            l_registered = "F";
            l_made_eligible = "F";
            // set up tracing based on app_config entry

            l_debug = (String) new settrace().execute("pf_sp0311s_awardbonus");
            if (l_debug.equals("T")) {
                setDebugFile("/tmp/pf_sp0311s_awardbonus.trace");
                trace("on");
            }
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
            // Get the program code of the account

            PreparedStatement pstmt2 = prepareStatement(
                      "select a.recog_cd, a.signup_date"
                    + " from acct a"
                    + " where a.acct_id = ?");
            
            if (in_acct_id != null) {
                pstmt2.setInt(1, in_acct_id);
            }
            else {
                pstmt2.setNull(1, Types.JAVA_OBJECT);
            }
            ResultSet rs2 = executeQuery(pstmt2);
            rs2.next();
            l_recog_cd = rs2.getString(1);
            l_signup_date = new DateTime(rs2.getTimestamp(2).getTime());
            pstmt2.close();
            rs2.close();
            // The stay being processed does not have an acct_trans_detail record

            // stored yet. So, we need to calculate its points based on rates,

            // property type, currency, etc.. Gather up that info then calculate

            // the number of points for the stay.

            PreparedStatement pstmt3 = prepareStatement(
                      "select s.prop_curr_rate, s.earning_curr_rate, s.post_dtime"
                    + " from stay s"
                    + " where s.stay_id = ?");
            
            if (in_stay_id != null) {
                pstmt3.setInt(1, in_stay_id);
            }
            else {
                pstmt3.setNull(1, Types.JAVA_OBJECT);
            }
            ResultSet rs3 = executeQuery(pstmt3);
            rs3.next();
            l_prop_curr_rate = rs3.getDouble(1);
            l_earn_curr_rate = rs3.getDouble(2);
            l_curr_post_dtime = new DateTime(rs3.getTimestamp(3).getTime());
            pstmt3.close();
            rs3.close();
            PreparedStatement pstmt4 = prepareStatement(
                      "select cg.chain_group_cd"
                    + " from chain_group cg, chain_group_detail cgd, prop p"
                    + " where cg.chain_group_id = cgd.chain_group_id"
                    + " and cgd.chain_id = p.chain_id"
                    + " and p.prop_id = ?");
            
            if (in_prop_id != null) {
                pstmt4.setInt(1, in_prop_id);
            }
            else {
                pstmt4.setNull(1, Types.JAVA_OBJECT);
            }
            ResultSet rs4 = executeQuery(pstmt4);
            rs4.next();
            l_chain_group_cd = rs4.getString(1);
            pstmt4.close();
            rs4.close();
            l_current_amount = trunc(new convert_currency().execute(l_prop_curr_rate, l_earn_curr_rate, in_rm_revenue));
            if ((l_chain_group_cd.equals("MS") || l_chain_group_cd.equals("US"))) {
                l_pts_dollar = 10.0;
            }
            else {
                l_pts_dollar = 5.0;
            }
            l_current_points = l_current_amount * l_pts_dollar;
            // Temp table to hold stays to consider

            // Temp table passed to pv_sp0311. It will hold the point values

            // for the stays making up the award bonus. Should be at least two entries

            // for any bonus awarded.

            // Get a list of linked stays to consider
Iterator<Object> it0 = new find_linked_stays_by_promo().execute(in_promo_id, l_start_date, l_stop_date, in_acct_id).iterator();
            while (it0.hasNext()) {
                l_stay_id = (Integer) it0.next();
                l_prop_cd = (String) it0.next();
                l_doa = (DateTime) it0.next();
                l_dod = (DateTime) it0.next();
                l_linked_id = (Integer) it0.next();
                l_trans_id = (Integer) it0.next();
                PreparedStatement pstmt5 = prepareStatement(
                          "select post_dtime"
                        + " from stay"
                        + " where stay_id = ?");
                
                if (l_stay_id != null) {
                    pstmt5.setInt(1, l_stay_id);
                }
                else {
                    pstmt5.setNull(1, Types.JAVA_OBJECT);
                }
                ResultSet rs5 = executeQuery(pstmt5);
                rs5.next();
                l_post_dtime = new DateTime(rs5.getTimestamp(1).getTime());
                pstmt5.close();
                rs5.close();
                PreparedStatement pstmt6 = prepareInsert(
                          "insert into recent_stay_trans_list_sp0311 (stay_id, prop_cd, doa, dod, linked_id, acct_trans_id, ord, post_dtime)"
                        + " values (?, ?, ?, ?, nvl(?, ?), ?, ?, ?)");
                if (l_stay_id != null) {
                    pstmt6.setInt(1, l_stay_id);
                }
                else {
                    pstmt6.setNull(1, Types.JAVA_OBJECT);
                }
                if (l_prop_cd != null) {
                    pstmt6.setString(2, l_prop_cd);
                }
                else {
                    pstmt6.setNull(2, Types.JAVA_OBJECT);
                }
                if (l_doa != null) {
                    pstmt6.setObject(3, l_doa);
                }
                else {
                    pstmt6.setNull(3, Types.JAVA_OBJECT);
                }
                if (l_dod != null) {
                    pstmt6.setObject(4, l_dod);
                }
                else {
                    pstmt6.setNull(4, Types.JAVA_OBJECT);
                }
                if (l_linked_id != null) {
                    pstmt6.setInt(5, l_linked_id);
                }
                else {
                    pstmt6.setNull(5, Types.JAVA_OBJECT);
                }
                if (l_stay_id != null) {
                    pstmt6.setInt(6, l_stay_id);
                }
                else {
                    pstmt6.setNull(6, Types.JAVA_OBJECT);
                }
                if (l_trans_id != null) {
                    pstmt6.setInt(7, l_trans_id);
                }
                else {
                    pstmt6.setNull(7, Types.JAVA_OBJECT);
                }
                if (l_ord != null) {
                    pstmt6.setInt(8, l_ord);
                }
                else {
                    pstmt6.setNull(8, Types.JAVA_OBJECT);
                }
                if (l_post_dtime != null) {
                    pstmt6.setObject(9, l_post_dtime);
                }
                else {
                    pstmt6.setNull(9, Types.JAVA_OBJECT);
                }
                executeUpdate(pstmt6);
                pstmt6.close();
                l_ord = l_ord + 1;
            }
            // add current stay to list

            PreparedStatement pstmt7 = prepareStatement(
                      "select max(l.id)"
                    + " from recent_stay_trans_list_sp0311 l"
                    + " where l.prop_cd = new get_prop_cd().execute(in_prop_id)"
                    + " and  ||  ||  || ");
            
            ResultSet rs6 = executeQuery(pstmt7);
            rs6.next();
            n_id = rs6.getInt(1);
            pstmt7.close();
            rs6.close();
            if (n_id != null) {
                PreparedStatement pstmt8 = prepareStatement(
                          "select nvl(l.linked_id, l.stay_id)"
                        + " from recent_stay_trans_list_sp0311 l"
                        + " where l.id = ?");
                
                if (n_id != null) {
                    pstmt8.setInt(1, n_id);
                }
                else {
                    pstmt8.setNull(1, Types.JAVA_OBJECT);
                }
                ResultSet rs7 = executeQuery(pstmt8);
                rs7.next();
                n_link = rs7.getInt(1);
                pstmt8.close();
                rs7.close();
            }
            else {
                n_link = in_stay_id;
            }
            PreparedStatement pstmt9 = prepareStatement(
                      "select post_dtime"
                    + " from stay"
                    + " where stay_id = ?");
            
            if (in_stay_id != null) {
                pstmt9.setInt(1, in_stay_id);
            }
            else {
                pstmt9.setNull(1, Types.JAVA_OBJECT);
            }
            ResultSet rs8 = executeQuery(pstmt9);
            rs8.next();
            l_post_dtime = new DateTime(rs8.getTimestamp(1).getTime());
            pstmt9.close();
            rs8.close();
            PreparedStatement pstmt10 = prepareInsert(
                      "insert into recent_stay_trans_list_sp0311 (stay_id, prop_cd, doa, dod, linked_id, ord, post_dtime)"
                    + " values (?, new get_prop_cd().execute(in_prop_id), ?, ?, ?, ?, ?)");
            if (in_stay_id != null) {
                pstmt10.setInt(1, in_stay_id);
            }
            else {
                pstmt10.setNull(1, Types.JAVA_OBJECT);
            }
            if (in_doa != null) {
                pstmt10.setObject(2, in_doa);
            }
            else {
                pstmt10.setNull(2, Types.JAVA_OBJECT);
            }
            if (f_dod != null) {
                pstmt10.setObject(3, f_dod);
            }
            else {
                pstmt10.setNull(3, Types.JAVA_OBJECT);
            }
            if (n_link != null) {
                pstmt10.setInt(4, n_link);
            }
            else {
                pstmt10.setNull(4, Types.JAVA_OBJECT);
            }
            if (l_ord != null) {
                pstmt10.setInt(5, l_ord);
            }
            else {
                pstmt10.setNull(5, Types.JAVA_OBJECT);
            }
            if (l_curr_post_dtime != null) {
                pstmt10.setObject(6, l_curr_post_dtime);
            }
            else {
                pstmt10.setNull(6, Types.JAVA_OBJECT);
            }
            executeUpdate(pstmt10);
            pstmt10.close();
            PreparedStatement pstmt11 = prepareStatement(
                      "select ao.offer_id"
                    + " from acct_offer ao"
                    + " where ao.acct_id = ?"
                    + " and ao.offer_id = 
                        + "select o.offer_id"
                        + " from offer o"
                        + " where o.offer_cd = \"SP0311S\""
                        + " and o.recog_cd = ?");
                
                if (in_acct_id != null) {
                    pstmt11.setInt(1, in_acct_id);
                }
                else {
                    pstmt11.setNull(1, Types.JAVA_OBJECT);
                }
                if (l_recog_cd != null) {
                    pstmt11.setString(2, l_recog_cd);
                }
                else {
                    pstmt11.setNull(2, Types.JAVA_OBJECT);
                }
                ResultSet rs9 = executeQuery(pstmt11);
                rs9.next();
                l_offer_id = rs9.getInt(1);
                rs9.close();
                if (l_offer_id != null) {
                    l_registered = (String) new pf_isregistered().execute(l_offer_id, in_acct_id, in_doa);
                }
                // if registered then the max bonuses awarded is increased based on elite level

                if (l_registered.equals("T")) {
                    if (in_platinum.equals("T") || in_diamond.equals("T")) {
                        l_max_awards = 5;
                    }
                    else {
                        l_max_awards = 2;
                    }
                }
                // Stay is eligible and may trigger a bonus, but first check to see if the max number of 

                // bonuses has been reached. If they are maxed out, then no sense continuing. However, we

                // must first check if stay is linked to stays already awarded a bonus. We need to insert

                // stay into acct_trans_contrib so it will never be eligible.

                PreparedStatement pstmt12 = prepareStatement(
                          "select count(*)"
                        + " from acct_trans a, acct_trans_detail ad"
                        + " where a.acct_trans_id = ad.acct_trans_id"
                        + " and a.acct_id = ?"
                        + " and a.rev_acct_trans_id is null"
                        + " and ad.promo_id = ?");
                
                if (in_acct_id != null) {
                    pstmt12.setInt(1, in_acct_id);
                }
                else {
                    pstmt12.setNull(1, Types.JAVA_OBJECT);
                }
                if (in_promo_id != null) {
                    pstmt12.setInt(2, in_promo_id);
                }
                else {
                    pstmt12.setNull(2, Types.JAVA_OBJECT);
                }
                ResultSet rs10 = executeQuery(pstmt12);
                rs10.next();
                l_bonus_cnt = rs10.getInt(1);
                pstmt12.close();
                rs10.close();
                // See if the stay at hand is part of a stay set that has already triggered or contributed to 

                // an sp0311s bonus. We do this by examining any linked stays for the stay at hand and

                // seeing if the linked stays' acct_trans_id are in the acct_trans_contrib table. If we get a 

                // hit, we need to add the stay at hand to the acct_trans_contrib table so it's 

                // not used in any future bonus calculation and then get out since the stay at hand is not

                // eligible for the bonus. One caveat...if found in the acct_trans_contrib, then we need

                // to see if the bonus triggering stay has been reversed. If so, then that bonus has been

                // backed out and the stay at hand could be eligible for the bonus.

                PreparedStatement pstmt13 = prepareStatement(
                          "select stay_id, acct_trans_id, doa"
                        + " from recent_stay_trans_list_sp0311"
                        + " where linked_id = ?"
                        + " and stay_id != ?");
                
                if (n_link != null) {
                    pstmt13.setInt(1, n_link);
                }
                else {
                    pstmt13.setNull(1, Types.JAVA_OBJECT);
                }
                if (in_stay_id != null) {
                    pstmt13.setInt(2, in_stay_id);
                }
                else {
                    pstmt13.setNull(2, Types.JAVA_OBJECT);
                }
                ResultSet rs11 = executeQuery(pstmt13);
                while (rs11.next()) {
                    l_stay_id = rs11.getInt(1);
                    l_acct_trans_id = rs11.getInt(2);
                    l_doa = new DateTime(rs11.getTimestamp(3).getTime());
                    // add up linked stays and those with the same doa, used when looking at an economy stay

                    // if we find no previous bonus awarded.

                    l_linked_stays = l_linked_stays + 1;
                    if (l_doa.isEqual(in_doa)) {
                        l_linked_same_doa = l_linked_same_doa + 1;
                    }
                    l_acct_trans_id = null;l_ord = null;l_act_contrib_acct_trans_id = null;
                    PreparedStatement pstmt14 = prepareStatement(
                              "select acct_trans_id, ord, contrib_acct_trans_id"
                            + " from acct_trans_contrib"
                            + " where contrib_stay_id = ?");
                    
                    if (l_stay_id != null) {
                        pstmt14.setInt(1, l_stay_id);
                    }
                    else {
                        pstmt14.setNull(1, Types.JAVA_OBJECT);
                    }
                    ResultSet rs12 = executeQuery(pstmt14);
                    while (rs12.next()) {
                        l_act_acct_trans_id = rs12.getInt(1);
                        l_ord = rs12.getInt(2);
                        l_act_contrib_acct_trans_id = rs12.getInt(3);
                        // see if triggering is reversed

                        PreparedStatement pstmt15 = prepareStatement(
                                  "select rev_acct_trans_id"
                                + " from acct_trans"
                                + " where acct_trans_id = ?");
                        
                        if (l_act_acct_trans_id != null) {
                            pstmt15.setInt(1, l_act_acct_trans_id);
                        }
                        else {
                            pstmt15.setNull(1, Types.JAVA_OBJECT);
                        }
                        ResultSet rs13 = executeQuery(pstmt15);
                        rs13.next();
                        l_rev_acct_trans_id = rs13.getInt(1);
                        pstmt15.close();
                        rs13.close();
                        if (l_rev_acct_trans_id == null) {
                            l_act_insert_needed = "Y";
                            // bonus active so add stay at hand to acct_trans_contrib

                            break;
                        }
                    }
                    pstmt14.close();
                    rs12.close();
                    if (l_act_insert_needed.equals("Y")) {
                        break;
                    }
                }
                pstmt13.close();
                rs11.close();
                if (l_act_insert_needed.equals("Y")) {
                    // linked stay with bonus still active so add stay at hand to acct_trans_contrib 

                    // to prevent it from being used in future bonus calculations. Be sure to indicate

                    // it did not contribute to the bonus calculation.

                    PreparedStatement pstmt16 = prepareInsert(
                              "insert into t_acct_trans_contrib (acct_trans_id, promo_id, ord, contrib_stay_id, bonus_contrib)"
                            + " values (?, ?, ?, ?, \"N\")");
                    if (l_act_acct_trans_id != null) {
                        pstmt16.setInt(1, l_act_acct_trans_id);
                    }
                    else {
                        pstmt16.setNull(1, Types.JAVA_OBJECT);
                    }
                    if (in_promo_id != null) {
                        pstmt16.setInt(2, in_promo_id);
                    }
                    else {
                        pstmt16.setNull(2, Types.JAVA_OBJECT);
                    }
                    if (l_ord != null) {
                        pstmt16.setInt(3, l_ord);
                    }
                    else {
                        pstmt16.setNull(3, Types.JAVA_OBJECT);
                    }
                    if (in_stay_id != null) {
                        pstmt16.setInt(4, in_stay_id);
                    }
                    else {
                        pstmt16.setNull(4, Types.JAVA_OBJECT);
                    }
                    executeUpdate(pstmt16);
                    pstmt16.close();
                    PreparedStatement pstmt17 = prepareStatement("drop table recent_stay_trans_list_sp0311");
                    executeUpdate(pstmt17);
                    pstmt17.close();
                    PreparedStatement pstmt18 = prepareStatement("drop table qualified_stays_pv_sp0311");
                    executeUpdate(pstmt18);
                    pstmt18.close();
                    return "F";
                }
                // Now safe to check if the account has exceeded the max bonuses since if

                // it was linked to an stays already awarded the bonus, acct_trans_contrib

                // entries have been persisted.

                if (l_bonus_cnt >= l_max_awards) {
                    PreparedStatement pstmt19 = prepareStatement("drop table recent_stay_trans_list_sp0311");
                    executeUpdate(pstmt19);
                    pstmt19.close();
                    PreparedStatement pstmt20 = prepareStatement("drop table qualified_stays_pv_sp0311");
                    executeUpdate(pstmt20);
                    pstmt20.close();
                    return "F";
                }
                // See if an economy, LOS=1 stay with no linked stays that might make it eligible.

                // If so, then we are done.

                if ((l_chain_group_cd.equals("EC") || l_chain_group_cd.equals("ES")) && in_los < 2 && l_linked_stays.equals(l_linked_same_doa)) {
                    PreparedStatement pstmt21 = prepareStatement("drop table qualified_stays_pv_sp0311");
                    executeUpdate(pstmt21);
                    pstmt21.close();
                    PreparedStatement pstmt22 = prepareStatement("drop table recent_stay_trans_list_sp0311");
                    executeUpdate(pstmt22);
                    pstmt22.close();
                    return "F";
                }
                // We now know the the stay at hand is eligible and is not part of a stay set that has

                // been awarded the bonus. Now, see if there are linked stays to

                // go with the stay at hand. We don't care if they are eligible or not since the stay at hand

                // is eligible, but we do need their points (watch out for multiple roooms, only first one counts), 

                // and we do need to add them to the acct_trans_contrib table in case the stay pairs up with

                // a second stay and awards the bonus.

                // This picks up, for example, one night economy stays that have links to prior stays, consecutive

                // night midscale where linked stay 1 is not eligible but the stay at hand is, etc... I hope!!

                l_current_doa = null;
                l_linked_stays = 0;
                PreparedStatement pstmt23 = prepareStatement(
                          "select stay_id, acct_trans_id, doa"
                        + " from recent_stay_trans_list_sp0311"
                        + " where linked_id = ?"
                        + " and stay_id != ?"
                        + " order by doa, stay_id");
                
                if (n_link != null) {
                    pstmt23.setInt(1, n_link);
                }
                else {
                    pstmt23.setNull(1, Types.JAVA_OBJECT);
                }
                if (in_stay_id != null) {
                    pstmt23.setInt(2, in_stay_id);
                }
                else {
                    pstmt23.setNull(2, Types.JAVA_OBJECT);
                }
                ResultSet rs14 = executeQuery(pstmt23);
                while (rs14.next()) {
                    l_stay_id = rs14.getInt(1);
                    l_acct_trans_id = rs14.getInt(2);
                    l_doa = new DateTime(rs14.getTimestamp(3).getTime());
                    // get the first room only on a multi-room stay. Second room marked as

                    // non contributing but part of the stay set

                    if (l_current_doa != null && l_doa.isEqual(l_current_doa)) {
                        PreparedStatement pstmt24 = prepareInsert(
                                  "insert into t_acct_trans_contrib (promo_id, contrib_stay_id, contrib_acct_trans_id, bonus_contrib)"
                                + " values (?, ?, ?, \"N\")");
                        if (in_promo_id != null) {
                            pstmt24.setInt(1, in_promo_id);
                        }
                        else {
                            pstmt24.setNull(1, Types.JAVA_OBJECT);
                        }
                        if (l_stay_id != null) {
                            pstmt24.setInt(2, l_stay_id);
                        }
                        else {
                            pstmt24.setNull(2, Types.JAVA_OBJECT);
                        }
                        if (l_acct_trans_id != null) {
                            pstmt24.setInt(3, l_acct_trans_id);
                        }
                        else {
                            pstmt24.setNull(3, Types.JAVA_OBJECT);
                        }
                        executeUpdate(pstmt24);
                        pstmt24.close();
                        continue;
                    }
                    l_current_doa = l_doa;
                    l_linked_amount = (Double) new get_base_points().execute(l_acct_trans_id);
                    if (l_linked_amount != null) {
                        l_linked_points = l_linked_points + l_linked_amount;
                        PreparedStatement pstmt25 = prepareInsert(
                                  "insert into t_acct_trans_contrib (promo_id, contrib_stay_id, contrib_acct_trans_id, bonus_contrib)"
                                + " values (?, ?, ?, \"Y\")");
                        if (in_promo_id != null) {
                            pstmt25.setInt(1, in_promo_id);
                        }
                        else {
                            pstmt25.setNull(1, Types.JAVA_OBJECT);
                        }
                        if (l_stay_id != null) {
                            pstmt25.setInt(2, l_stay_id);
                        }
                        else {
                            pstmt25.setNull(2, Types.JAVA_OBJECT);
                        }
                        if (l_acct_trans_id != null) {
                            pstmt25.setInt(3, l_acct_trans_id);
                        }
                        else {
                            pstmt25.setNull(3, Types.JAVA_OBJECT);
                        }
                        executeUpdate(pstmt25);
                        pstmt25.close();
                    }
                }
                pstmt23.close();
                rs14.close();
                l_current_points = l_current_points + l_linked_points;
                // go ahead and insert points for current stay

                PreparedStatement pstmt26 = prepareInsert(
                          "insert into qualified_stays_pv_sp0311 (amount)"
                        + " values (?)");
                if (l_current_points != null) {
                    pstmt26.setDouble(1, l_current_points);
                }
                else {
                    pstmt26.setNull(1, Types.JAVA_OBJECT);
                }
                executeUpdate(pstmt26);
                pstmt26.close();
                // add stay at hand to acct_trans_contrib

                PreparedStatement pstmt27 = prepareInsert(
                          "insert into t_acct_trans_contrib (promo_id, contrib_stay_id, bonus_contrib)"
                        + " values (?, ?, \"Y\")");
                if (in_promo_id != null) {
                    pstmt27.setInt(1, in_promo_id);
                }
                else {
                    pstmt27.setNull(1, Types.JAVA_OBJECT);
                }
                if (in_stay_id != null) {
                    pstmt27.setInt(2, in_stay_id);
                }
                else {
                    pstmt27.setNull(2, Types.JAVA_OBJECT);
                }
                executeUpdate(pstmt27);
                pstmt27.close();
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

                PreparedStatement pstmt28 = prepareStatement(
                          "select stay_id, linked_id, acct_trans_id, doa, dod, prop_cd"
                        + " from recent_stay_trans_list_sp0311"
                        + " where stay_id = linked_id"
                        + " and stay_id != ?"
                        + " order by post_dtime desc");
                
                if (n_link != null) {
                    pstmt28.setInt(1, n_link);
                }
                else {
                    pstmt28.setNull(1, Types.JAVA_OBJECT);
                }
                ResultSet rs15 = executeQuery(pstmt28);
                while (rs15.next()) {
                    l_stay_id = rs15.getInt(1);
                    l_linked_id = rs15.getInt(2);
                    l_acct_trans_id = rs15.getInt(3);
                    l_doa = new DateTime(rs15.getTimestamp(4).getTime());
                    l_dod = new DateTime(rs15.getTimestamp(5).getTime());
                    l_prop_cd = rs15.getString(6);
                    if (l_stay_id.equals(in_stay_id)) {
                        continue;
                    }
                    // see if already part of a bonus by checking acct_trans_contrib 

                    l_bonus_cnt = 0;
                    PreparedStatement pstmt29 = prepareStatement(
                              "select acct_trans_id"
                            + " from acct_trans_contrib"
                            + " where contrib_stay_id = ?");
                    
                    if (in_stay_id != null) {
                        pstmt29.setInt(1, in_stay_id);
                    }
                    else {
                        pstmt29.setNull(1, Types.JAVA_OBJECT);
                    }
                    ResultSet rs16 = executeQuery(pstmt29);
                    while (rs16.next()) {
                        l_act_acct_trans_id = rs16.getInt(1);
                        PreparedStatement pstmt30 = prepareStatement(
                                  "select count(*)"
                                + " from acct_trans a, acct_trans_detail ad"
                                + " where a.acct_trans_id = ad.acct_trans_id"
                                + " and a.acct_trans_id = ?"
                                + " and ad.promo_id = ?"
                                + " and a.rev_acct_trans_id is null");
                        
                        if (l_act_acct_trans_id != null) {
                            pstmt30.setInt(1, l_act_acct_trans_id);
                        }
                        else {
                            pstmt30.setNull(1, Types.JAVA_OBJECT);
                        }
                        if (in_promo_id != null) {
                            pstmt30.setInt(2, in_promo_id);
                        }
                        else {
                            pstmt30.setNull(2, Types.JAVA_OBJECT);
                        }
                        ResultSet rs17 = executeQuery(pstmt30);
                        rs17.next();
                        l_bonus_cnt = rs17.getInt(1);
                        pstmt30.close();
                        rs17.close();
                        if (l_bonus_cnt > 0) {
                            break;
                        }
                    }
                    pstmt29.close();
                    rs16.close();
                    if (l_bonus_cnt > 0) {
                        continue;
                    }
                    // see if this stay is eligible; return values whether it meets basic criteria, is an

                    // economy stay and if the economy stay meets 2 night minimum requirement.

                    Iterator<Object> it0 = new pf_sp0311s_stay_eligible().execute(in_acct_id, l_stay_id, in_promo_id, l_signup_date, l_recog_cd).iterator();;
                    if (l_is_econo.equals("T")) {
                        // an economy stay found, determine eligiblity along with its other linked stays

                        if (l_econo_eligible.equals("F")) {
                            // this one not eligible (not a 2 night stay evidently). See if any consecutive

                            // stays that make it eligible

                            PreparedStatement pstmt31 = prepareStatement(
                                      "select stay_id, acct_trans_id, doa, dod"
                                    + " from recent_stay_trans_list_sp0311"
                                    + " where linked_id = ?"
                                    + " and stay_id != ?"
                                    + " order by doa, stay_id");
                            
                            if (l_stay_id != null) {
                                pstmt31.setInt(1, l_stay_id);
                            }
                            else {
                                pstmt31.setNull(1, Types.JAVA_OBJECT);
                            }
                            if (l_stay_id != null) {
                                pstmt31.setInt(2, l_stay_id);
                            }
                            else {
                                pstmt31.setNull(2, Types.JAVA_OBJECT);
                            }
                            ResultSet rs18 = executeQuery(pstmt31);
                            while (rs18.next()) {
                                l_stay_id2 = rs18.getInt(1);
                                l_trans_id2 = rs18.getInt(2);
                                l_doa2 = new DateTime(rs18.getTimestamp(3).getTime());
                                l_dod2 = new DateTime(rs18.getTimestamp(4).getTime());
                                if (l_doa.isEqual(l_doa2)) {
                                    // check if the same doa stay might be eligible

                                    Iterator<Object> it0 = new pf_sp0311s_stay_eligible().execute(in_acct_id, l_stay_id2, in_promo_id, l_signup_date, l_recog_cd).iterator();;
                                    if (l_is_elig.equals("T")) {
                                        l_made_eligible = "T";
                                        break;
                                    }
                                    else {
                                        continue;
                                    }
                                }
                                l_made_eligible = "T";
                                break;
                            }
                            pstmt31.close();
                            rs18.close();
                        }
                        else {
                            l_made_eligible = "T";
                        }
                        // if this one is eligible somehow, now go through and persist and add up the

                        // stays that contribute to it, including itself. By default, all linked stays

                        // are eligible so no need to check that fact.

                        l_current_points = null;
                        l_current_doa = null;
                        if (l_made_eligible.equals("T")) {
                            PreparedStatement pstmt32 = prepareStatement(
                                      "select stay_id, acct_trans_id, doa, dod"
                                    + " from recent_stay_trans_list_sp0311"
                                    + " where linked_id = ?"
                                    + " order by doa, stay_id");
                            
                            if (l_stay_id != null) {
                                pstmt32.setInt(1, l_stay_id);
                            }
                            else {
                                pstmt32.setNull(1, Types.JAVA_OBJECT);
                            }
                            ResultSet rs19 = executeQuery(pstmt32);
                            while (rs19.next()) {
                                l_stay_id2 = rs19.getInt(1);
                                l_trans_id2 = rs19.getInt(2);
                                l_doa2 = new DateTime(rs19.getTimestamp(3).getTime());
                                l_dod2 = new DateTime(rs19.getTimestamp(4).getTime());
                                if (l_current_doa != null && l_doa2.isEqual(l_current_doa)) {
                                    // a repeat doa, persist to acct_trans_contrib but non-contributor to bonus calc

                                    PreparedStatement pstmt33 = prepareInsert(
                                              "insert into t_acct_trans_contrib (promo_id, contrib_stay_id, contrib_acct_trans_id, bonus_contrib)"
                                            + " values (?, ?, ?, \"N\")");
                                    if (in_promo_id != null) {
                                        pstmt33.setInt(1, in_promo_id);
                                    }
                                    else {
                                        pstmt33.setNull(1, Types.JAVA_OBJECT);
                                    }
                                    if (l_stay_id2 != null) {
                                        pstmt33.setInt(2, l_stay_id2);
                                    }
                                    else {
                                        pstmt33.setNull(2, Types.JAVA_OBJECT);
                                    }
                                    if (l_trans_id2 != null) {
                                        pstmt33.setInt(3, l_trans_id2);
                                    }
                                    else {
                                        pstmt33.setNull(3, Types.JAVA_OBJECT);
                                    }
                                    executeUpdate(pstmt33);
                                    pstmt33.close();
                                    continue;
                                }
                                l_current_doa = l_doa2;
                                l_current_points = (Double) new get_base_points().execute(l_trans_id2);
                                if (l_current_points != null) {
                                    l_total_points = l_total_points + l_current_points.intValue();
                                    PreparedStatement pstmt34 = prepareInsert(
                                              "insert into t_acct_trans_contrib (promo_id, contrib_stay_id, contrib_acct_trans_id, bonus_contrib)"
                                            + " values (?, ?, ?, \"Y\")");
                                    if (in_promo_id != null) {
                                        pstmt34.setInt(1, in_promo_id);
                                    }
                                    else {
                                        pstmt34.setNull(1, Types.JAVA_OBJECT);
                                    }
                                    if (l_stay_id2 != null) {
                                        pstmt34.setInt(2, l_stay_id2);
                                    }
                                    else {
                                        pstmt34.setNull(2, Types.JAVA_OBJECT);
                                    }
                                    if (l_trans_id2 != null) {
                                        pstmt34.setInt(3, l_trans_id2);
                                    }
                                    else {
                                        pstmt34.setNull(3, Types.JAVA_OBJECT);
                                    }
                                    executeUpdate(pstmt34);
                                    pstmt34.close();
                                }
                            }
                            pstmt32.close();
                            rs19.close();
                        }
                    }
                    else if (l_is_elig.equals("T")) {
                        // a midscale eligible, already know it is eligible, so

                        // Now look at stays that might be linked to this one; i.e. stays where the linked_stay_id 

                        // equals the stay_id being processed in cursor cur1. Doesn't matter if they

                        // are eligible or not because the first stay is, but we only want first room of 

                        // multi-room stays to count towards points.  

                        l_current_doa = null;
                        PreparedStatement pstmt35 = prepareStatement(
                                  "select stay_id, acct_trans_id, doa"
                                + " from recent_stay_trans_list_sp0311"
                                + " where linked_id = ?"
                                + " order by doa, stay_id");
                        
                        if (l_stay_id != null) {
                            pstmt35.setInt(1, l_stay_id);
                        }
                        else {
                            pstmt35.setNull(1, Types.JAVA_OBJECT);
                        }
                        ResultSet rs20 = executeQuery(pstmt35);
                        while (rs20.next()) {
                            l_stay_id2 = rs20.getInt(1);
                            l_trans_id2 = rs20.getInt(2);
                            l_doa2 = new DateTime(rs20.getTimestamp(3).getTime());
                            if (l_current_doa != null && l_doa2.isEqual(l_current_doa)) {
                                // don't count these points, but do insert into acct_trans_contrib

                                PreparedStatement pstmt36 = prepareInsert(
                                          "insert into t_acct_trans_contrib (promo_id, contrib_stay_id, contrib_acct_trans_id, bonus_contrib)"
                                        + " values (?, ?, ?, \"N\")");
                                if (in_promo_id != null) {
                                    pstmt36.setInt(1, in_promo_id);
                                }
                                else {
                                    pstmt36.setNull(1, Types.JAVA_OBJECT);
                                }
                                if (l_stay_id2 != null) {
                                    pstmt36.setInt(2, l_stay_id2);
                                }
                                else {
                                    pstmt36.setNull(2, Types.JAVA_OBJECT);
                                }
                                if (l_trans_id2 != null) {
                                    pstmt36.setInt(3, l_trans_id2);
                                }
                                else {
                                    pstmt36.setNull(3, Types.JAVA_OBJECT);
                                }
                                executeUpdate(pstmt36);
                                pstmt36.close();
                                continue;
                            }
                            else {
                                l_current_doa = l_doa2;
                                l_current_points = (Double) new get_base_points().execute(l_trans_id2);
                                if (l_current_points != null) {
                                    l_total_points = l_total_points + l_current_points.intValue();
                                    PreparedStatement pstmt37 = prepareInsert(
                                              "insert into t_acct_trans_contrib (promo_id, contrib_stay_id, contrib_acct_trans_id, bonus_contrib)"
                                            + " values (?, ?, ?, \"Y\")");
                                    if (in_promo_id != null) {
                                        pstmt37.setInt(1, in_promo_id);
                                    }
                                    else {
                                        pstmt37.setNull(1, Types.JAVA_OBJECT);
                                    }
                                    if (l_stay_id2 != null) {
                                        pstmt37.setInt(2, l_stay_id2);
                                    }
                                    else {
                                        pstmt37.setNull(2, Types.JAVA_OBJECT);
                                    }
                                    if (l_trans_id2 != null) {
                                        pstmt37.setInt(3, l_trans_id2);
                                    }
                                    else {
                                        pstmt37.setNull(3, Types.JAVA_OBJECT);
                                    }
                                    executeUpdate(pstmt37);
                                    pstmt37.close();
                                }
                            }
                        }
                        pstmt35.close();
                        rs20.close();
                    }
                    // flat out not eligible, but it is a midscale since we've already screened for economy and 

                    // midscale eligible

                    else {
                        PreparedStatement pstmt38 = prepareStatement(
                                  "select stay_id, acct_trans_id, doa"
                                + " from recent_stay_trans_list_sp0311"
                                + " where linked_id = ?"
                                + " order by doa, stay_id");
                        
                        if (l_stay_id != null) {
                            pstmt38.setInt(1, l_stay_id);
                        }
                        else {
                            pstmt38.setNull(1, Types.JAVA_OBJECT);
                        }
                        ResultSet rs21 = executeQuery(pstmt38);
                        while (rs21.next()) {
                            l_stay_id2 = rs21.getInt(1);
                            l_trans_id2 = rs21.getInt(2);
                            l_doa2 = new DateTime(rs21.getTimestamp(3).getTime());
                            Iterator<Object> it0 = new pf_sp0311s_stay_eligible().execute(in_acct_id, l_stay_id2, in_promo_id, l_signup_date, l_recog_cd).iterator();;
                            if (l_is_elig.equals("T")) {
                                break;
                            }
                        }
                        pstmt38.close();
                        rs21.close();
                        if (l_is_elig.equals("T")) {
                            // we found at least one eligible in the linked stays. Add in the first stay, then

                            // process all of the other ones

                            l_current_doa = null;
                            PreparedStatement pstmt39 = prepareStatement(
                                      "select stay_id, acct_trans_id, doa"
                                    + " from recent_stay_trans_list_sp0311"
                                    + " where linked_id = ?"
                                    + " order by doa, stay_id");
                            
                            if (l_stay_id != null) {
                                pstmt39.setInt(1, l_stay_id);
                            }
                            else {
                                pstmt39.setNull(1, Types.JAVA_OBJECT);
                            }
                            ResultSet rs22 = executeQuery(pstmt39);
                            while (rs22.next()) {
                                l_stay_id2 = rs22.getInt(1);
                                l_trans_id2 = rs22.getInt(2);
                                l_doa2 = new DateTime(rs22.getTimestamp(3).getTime());
                                if (l_current_doa != null && l_doa2.isEqual(l_current_doa)) {
                                    // don't count these points, but do insert into acct_trans_contrib

                                    PreparedStatement pstmt40 = prepareInsert(
                                              "insert into t_acct_trans_contrib (promo_id, contrib_stay_id, contrib_acct_trans_id, bonus_contrib)"
                                            + " values (?, ?, ?, \"N\")");
                                    if (in_promo_id != null) {
                                        pstmt40.setInt(1, in_promo_id);
                                    }
                                    else {
                                        pstmt40.setNull(1, Types.JAVA_OBJECT);
                                    }
                                    if (l_stay_id2 != null) {
                                        pstmt40.setInt(2, l_stay_id2);
                                    }
                                    else {
                                        pstmt40.setNull(2, Types.JAVA_OBJECT);
                                    }
                                    if (l_trans_id2 != null) {
                                        pstmt40.setInt(3, l_trans_id2);
                                    }
                                    else {
                                        pstmt40.setNull(3, Types.JAVA_OBJECT);
                                    }
                                    executeUpdate(pstmt40);
                                    pstmt40.close();
                                    continue;
                                }
                                l_current_doa = l_doa2;
                                l_current_points = (Double) new get_base_points().execute(l_trans_id2);
                                if (l_current_points != null) {
                                    l_total_points = l_total_points + l_current_points.intValue();
                                    PreparedStatement pstmt41 = prepareInsert(
                                              "insert into t_acct_trans_contrib (promo_id, contrib_stay_id, contrib_acct_trans_id, bonus_contrib)"
                                            + " values (?, ?, ?, \"Y\")");
                                    if (in_promo_id != null) {
                                        pstmt41.setInt(1, in_promo_id);
                                    }
                                    else {
                                        pstmt41.setNull(1, Types.JAVA_OBJECT);
                                    }
                                    if (l_stay_id2 != null) {
                                        pstmt41.setInt(2, l_stay_id2);
                                    }
                                    else {
                                        pstmt41.setNull(2, Types.JAVA_OBJECT);
                                    }
                                    if (l_trans_id2 != null) {
                                        pstmt41.setInt(3, l_trans_id2);
                                    }
                                    else {
                                        pstmt41.setNull(3, Types.JAVA_OBJECT);
                                    }
                                    executeUpdate(pstmt41);
                                    pstmt41.close();
                                }
                            }
                            pstmt39.close();
                            rs22.close();
                        }
                    }
                    // if we have a qualified stay, insert into table for pv_ code.

                    if (l_total_points > 0.0) {
                        PreparedStatement pstmt42 = prepareInsert(
                                  "insert into qualified_stays_pv_sp0311 (amount)"
                                + " values (?)");
                        if (l_total_points != null) {
                            pstmt42.setInt(1, l_total_points);
                        }
                        else {
                            pstmt42.setNull(1, Types.JAVA_OBJECT);
                        }
                        executeUpdate(pstmt42);
                        pstmt42.close();
                        l_total_qualified = l_total_qualified + 1;
                        break;
                    }
                }
                pstmt28.close();
                rs15.close();
                if (l_total_qualified < 2) {
                    // Perhaps current stay qualified but no others so no bonus for you!

                    // Delete anything we stored in the temp acct_trans_contrib table

                    // so it is not persisted.

                    PreparedStatement pstmt43 = prepareStatement(
                              "delete from t_acct_trans_contrib"
                            + " where promo_id = ?");
                    if (in_promo_id != null) {
                        pstmt43.setInt(1, in_promo_id);
                    }
                    else {
                        pstmt43.setNull(1, Types.JAVA_OBJECT);
                    }
                    executeUpdate(pstmt43);
                    pstmt43.close();
                    PreparedStatement pstmt44 = prepareStatement("drop table qualified_stays_pv_sp0311");
                    executeUpdate(pstmt44);
                    pstmt44.close();
                    PreparedStatement pstmt45 = prepareStatement("drop table recent_stay_trans_list_sp0311");
                    executeUpdate(pstmt45);
                    pstmt45.close();
                    return "F";
                }
                // all is good and bonus will be awarded

                PreparedStatement pstmt46 = prepareStatement("drop table recent_stay_trans_list_sp0311");
                executeUpdate(pstmt46);
                pstmt46.close();
                return "T";
            }
            catch (SQLException e) {
                sql_error = e.getErrorCode();
                isam_error = 0;
                error_data = e.getMessage();
                {
                    PreparedStatement pstmt47 = prepareStatement(
                              "delete from t_acct_trans_contrib"
                            + " where promo_id = ?");
                    if (in_promo_id != null) {
                        pstmt47.setInt(1, in_promo_id);
                    }
                    else {
                        pstmt47.setNull(1, Types.JAVA_OBJECT);
                    }
                    executeUpdate(pstmt47);
                    pstmt47.close();
                    PreparedStatement pstmt48 = prepareStatement("drop table recent_stay_trans_list_sp0311");
                    executeUpdate(pstmt48);
                    pstmt48.close();
                    PreparedStatement pstmt49 = prepareStatement("drop table qualified_stays_pv_sp0311");
                    executeUpdate(pstmt49);
                    pstmt49.close();
                    throw new ProcedureException(sql_error, isam_error, error_data);
                }
            }
        }
    
    }