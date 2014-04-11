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

public class pf_fl0809f extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public String execute(Integer in_acct_id, Integer in_promo_id, String in_stay_type, Integer in_prop_id, DateTime in_doa, Integer in_los, String in_rm_type, String in_srp_code, Double in_rm_revenue, Double in_fb_revenue, Double in_other_revenue, String in_curr_code, String in_res_source, Integer in_stay_id) throws SQLException, ProcedureException {
        /*
         * pf_fl0809f.sql - Check if stay is eligible for the 'Stay 3 consecutive nights' Fall 2009 promo.
         *                  The stays in question are restricted to DOA falling within the promotion start 
         *                  and end dates. Also the stays must have qualified for points.
         *                      
         *                  Stays must be booked through ch.com or central res unless elite status
         *                  is Platinum or Diamond. An enrollment stay is eligible regardless of
         *                  booking source. A GDS booking is eligible if they registered prior to 
         *                  the DOA. A bonus is awarded on the first 3 qualifying nights of the stay.
         *                  So, for linked stays, partial nights from a stay may be used to calculate
         *                  the points for the bonus.
         *    
         * $Id: pf_fl0809f.sql 23054 2009-08-24 23:28:08Z rshepher $
         *    
         *        Copyright (C) 2009 Choice Hotels International, Inc.
         */
        DateTime start_date;
        DateTime stop_date;
        String l_recog_cd;
        Integer fl0809f_promo_id;
        Integer l_stay_id;
        String l_prop_cd;
        DateTime l_doa;
        DateTime l_dod;
        DateTime l_current_doa;
        Integer l_linked_id;
        Integer l_trans_id;
        DateTime f_dod;
        Integer n_id;
        Integer n_link;
        String l_name;
        String l_res_source;
        DateTime l_signup_date;
        Integer l_linked_elig;
        Integer l_qualifies;
        Integer l_appl_group;
        Integer l_ord;
        Integer l_offer_id;
        DateTime l_offer_dtime;
        Integer l_bonus_cnt;
        Integer l_night_cnt;
        Integer l_use;
        Integer l_stop;
        Integer l_linked_qualifies;
        Integer l_linked_stay_id;
        DateTime l_linked_doa;
        Integer l_linked_los;
        String l_linked_res_source;
        Integer l_linked_trans_id;
        String l_linked_name;
        Double l_linked_elig_rev;
        Double l_prop_curr_rate;
        Double l_earn_curr_rate;
        Integer sql_error;
        Integer isam_error;
        String error_data;
        try {
            start_date = null;
            stop_date = null;
            l_recog_cd = null;
            fl0809f_promo_id = null;
            l_stay_id = null;
            l_linked_id = null;
            l_prop_cd = null;
            l_doa = null;
            l_dod = null;
            l_current_doa = null;
            l_trans_id = null;
            f_dod = in_doa.plusDays(in_los);
            n_id = null;
            n_link = null;
            l_name = null;
            l_res_source = null;
            l_signup_date = null;
            l_linked_elig = 0;
            l_qualifies = 0;
            l_appl_group = 0;
            l_ord = 1;
            l_offer_id = 0;
            l_offer_dtime = null;
            l_bonus_cnt = 0;
            l_night_cnt = 0;
            l_use = 0;
            l_stop = 0;
            l_linked_qualifies = 0;
            l_linked_stay_id = 0;
            l_linked_doa = null;
            l_linked_los = 0;
            l_linked_res_source = null;
            l_linked_trans_id = 0;
            l_linked_name = null;
            l_linked_elig_rev = 0.0;
            l_prop_curr_rate = 0.0;
            l_earn_curr_rate = 0.0;
            //set debug file to '/tmp/pf_fl0809f.trace';

            //trace on;

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
            start_date = new DateTime(rs1.getTimestamp(1).getTime());
            stop_date = new DateTime(rs1.getTimestamp(2).getTime());
            pstmt1.close();
            rs1.close();
            if (in_doa.isBefore(start_date) || in_doa.isAfter(stop_date)) {
                return "F";
            }
            // Get info for the account

            PreparedStatement pstmt2 = prepareStatement(
                      "select acct.recog_cd, acct.signup_date, acct.appl_group_id"
                    + " from acct"
                    + " where acct.acct_id = ?");
            
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
            l_appl_group = rs2.getInt(3);
            pstmt2.close();
            rs2.close();
            // get the offer_id for the promo

            PreparedStatement pstmt3 = prepareStatement(
                      "select offer_id"
                    + " from offer"
                    + " where offer_cd = \"FL0809F\""
                    + " and recog_cd = ?");
            
            if (l_recog_cd != null) {
                pstmt3.setString(1, l_recog_cd);
            }
            else {
                pstmt3.setNull(1, Types.JAVA_OBJECT);
            }
            ResultSet rs3 = executeQuery(pstmt3);
            rs3.next();
            l_offer_id = rs3.getInt(1);
            pstmt3.close();
            rs3.close();
            // get date of offer/registration if any

            PreparedStatement pstmt4 = prepareStatement(
                      "select date(offer_dtime)"
                    + " from acct_offer"
                    + " where acct_id = ?"
                    + " and offer_id = ?");
            
            if (in_acct_id != null) {
                pstmt4.setInt(1, in_acct_id);
            }
            else {
                pstmt4.setNull(1, Types.JAVA_OBJECT);
            }
            if (l_offer_id != null) {
                pstmt4.setInt(2, l_offer_id);
            }
            else {
                pstmt4.setNull(2, Types.JAVA_OBJECT);
            }
            ResultSet rs4 = executeQuery(pstmt4);
            rs4.next();
            l_offer_dtime = new DateTime(rs4.getTimestamp(1).getTime());
            pstmt4.close();
            rs4.close();
            // Get the promo ID for counting awarded bonuses

            PreparedStatement pstmt5 = prepareStatement(
                      "select promo.promo_id"
                    + " from promo"
                    + " where promo.recog_cd = ?"
                    + " and promo.promo_cd = \"FL0809F\"");
            
            if (l_recog_cd != null) {
                pstmt5.setString(1, l_recog_cd);
            }
            else {
                pstmt5.setNull(1, Types.JAVA_OBJECT);
            }
            ResultSet rs5 = executeQuery(pstmt5);
            rs5.next();
            fl0809f_promo_id = rs5.getInt(1);
            pstmt5.close();
            rs5.close();
            // Temp table to hold stays to consider

            // Temp table passed to pv_fl0809f. For a stand-alone, qualifying

            // stay of 3 nights or more, the table will contain one row, with 

            // the amount equal to the revenue for 3 nights. For a linked stay

            // scenario, the table will contain multiple rows, with each row

            // containing the revenue for each part of the stay that went into

            // making up the bonus. pv_fl0809f will simply have to sum up the

            // amount over all row(s), then multiply that by the pt/$ value.

            // Get a list of linked stays to consider
Iterator<Object> it0 = new find_linked_stays_by_promo().execute(in_promo_id, start_date, stop_date, in_acct_id).iterator();
            while (it0.hasNext()) {
                l_stay_id = (Integer) it0.next();
                l_prop_cd = (String) it0.next();
                l_doa = (DateTime) it0.next();
                l_dod = (DateTime) it0.next();
                l_linked_id = (Integer) it0.next();
                l_trans_id = (Integer) it0.next();
                PreparedStatement pstmt6 = prepareInsert(
                          "insert into recent_stay_trans_list_fl0809f (stay_id, prop_cd, doa, dod, linked_id, acct_trans_id, ord)"
                        + " values (?, ?, ?, ?, nvl(?, ?), ?, ?)");
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
                executeUpdate(pstmt6);
                pstmt6.close();
                l_ord = l_ord + 1;
            }
            // see if current stay is linked to any others

            PreparedStatement pstmt7 = prepareStatement(
                      "select max(l.id)"
                    + " from recent_stay_trans_list_fl0809f l"
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
                        + " from recent_stay_trans_list_fl0809f l"
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
            // Examine the current stay to see if it is eligible.

            // Rules are:

            //        Stay must be 3 or more consecutive nights

            //        Enrollment stay is valid regardless of booking source.

            //        Platinum and Diamond at DOA are eligible regardless of booking source. 

            //        Non-elite or Gold must have booked through ch.com or central res to be eligible

            //        Stay booked via GDS is eligible if member registered prior to DOA

            // before getting a bunch of info that may not be needed, do a quick check for 

            // a stand-alone stay being less than 3 nights.

            if (n_link.equals(in_stay_id) && in_los < 3) {
                PreparedStatement pstmt9 = prepareStatement("drop table recent_stay_trans_list_fl0809f");
                executeUpdate(pstmt9);
                pstmt9.close();
                PreparedStatement pstmt10 = prepareStatement("drop table pv_fl0809f_temp");
                executeUpdate(pstmt10);
                pstmt10.close();
                return "F";
            }
            // get booking source, doa, and currency rates for current stay

            PreparedStatement pstmt11 = prepareStatement(
                      "select s.res_source, s.doa, s.prop_curr_rate, s.earning_curr_rate"
                    + " from stay s"
                    + " where s.stay_id = ?");
            
            if (in_stay_id != null) {
                pstmt11.setInt(1, in_stay_id);
            }
            else {
                pstmt11.setNull(1, Types.JAVA_OBJECT);
            }
            ResultSet rs8 = executeQuery(pstmt11);
            rs8.next();
            l_res_source = rs8.getString(1);
            l_doa = new DateTime(rs8.getTimestamp(2).getTime());
            l_prop_curr_rate = rs8.getDouble(3);
            l_earn_curr_rate = rs8.getDouble(4);
            pstmt11.close();
            rs8.close();
            // get elite level at time of doa

            PreparedStatement pstmt12 = prepareStatement(
                      "select elvl.name"
                    + " from acct_elite_level_hist aelh, elite_level elvl"
                    + " where aelh.acct_id = ?"
                    + " and aelh.elite_level_id = elvl.elite_level_id"
                    + " and aelh.acct_elite_level_hist_id = 
                        + "select max(els.acct_elite_level_hist_id)"
                        + " from acct_elite_level_hist els"
                        + "  inner join elite_level es on els.elite_level_id = es.elite_level_id"
                        + " where els.acct_id = ?"
                        + " and els.date_acquired <= ?"
                        + " and year(es.eff_year) = year(?)");
                
                if (in_acct_id != null) {
                    pstmt12.setInt(1, in_acct_id);
                }
                else {
                    pstmt12.setNull(1, Types.JAVA_OBJECT);
                }
                if (in_acct_id != null) {
                    pstmt12.setInt(2, in_acct_id);
                }
                else {
                    pstmt12.setNull(2, Types.JAVA_OBJECT);
                }
                if (l_doa != null) {
                    pstmt12.setObject(3, l_doa);
                }
                else {
                    pstmt12.setNull(3, Types.JAVA_OBJECT);
                }
                if (l_doa != null) {
                    pstmt12.setObject(4, l_doa);
                }
                else {
                    pstmt12.setNull(4, Types.JAVA_OBJECT);
                }
                ResultSet rs9 = executeQuery(pstmt12);
                rs9.next();
                l_name = rs9.getString(1);
                rs9.close();
                // If doa and sign up date are the same, or if Platinum or Diamond, or if

                // ch.com or central res booking, or if GDS booking and resgistered before DOA,

                // then the stay qualifies 

                if () {
                    l_qualifies = 1;
                }
                else if (l_name != null && (l_name.equals("Platinum") || l_name.equals("Diamond"))) {
                    l_qualifies = 1;
                }
                else if (l_res_source != null && (l_res_source.equals("C") || l_res_source.equals("N"))) {
                    l_qualifies = 1;
                }
                else if (l_res_source != null && l_res_source.equals("G")) {
                    if (l_offer_dtime != null && (l_offer_dtime.isBefore(in_doa) || l_offer_dtime.isEqual(in_doa))) {
                        l_qualifies = 1;
                    }
                    else {
                        l_qualifies = 0;
                    }
                }
                else {
                    l_qualifies = 0;
                }
                // if the stay is not linked, test on its own merits

                if (n_link.equals(in_stay_id)) {
                    // yes, see if it qualified from earlier checks performed.

                    if (l_qualifies.equals(1)) {
                        // pass the 3 night revenue value for bonus calculation

                        // pv_fl0809f will determine pt/$ rate.... 

                        PreparedStatement pstmt13 = prepareInsert(
                                  "insert into pv_fl0809f_temp (los, amount)"
                                + " values (?, trunc(new convert_currency().execute(l_prop_curr_rate, l_earn_curr_rate, in_rm_revenue / in_los.doubleValue() * 3.0)))");
                        if (in_los != null) {
                            pstmt13.setInt(1, in_los);
                        }
                        else {
                            pstmt13.setNull(1, Types.JAVA_OBJECT);
                        }
                        executeUpdate(pstmt13);
                        pstmt13.close();
                        PreparedStatement pstmt14 = prepareStatement("drop table recent_stay_trans_list_fl0809f");
                        executeUpdate(pstmt14);
                        pstmt14.close();
                        return "T";
                    }
                    else {
                        // all tests failed so drop temp tables and return

                        PreparedStatement pstmt15 = prepareStatement("drop table recent_stay_trans_list_fl0809f");
                        executeUpdate(pstmt15);
                        pstmt15.close();
                        PreparedStatement pstmt16 = prepareStatement("drop table pv_fl0809f_temp");
                        executeUpdate(pstmt16);
                        pstmt16.close();
                        return "F";
                    }
                }
                // If we get to here, the current stay is linked so we need to examine

                // all of the other linked stays associated with it to determine if and

                // what qualifies. Out of this, the linked stays may provide the 3 nights

                // qualification or we may have to use some linked nights and some/all of

                // the current stay's nights.

                // This cur1 cursor processes stays that are linked to the

                // current stay. They are in ascending order by DOA in the

                // recent_stay_trans_list_fl0809f table. Determine if the 

                // bonus has been applied for any of these stays. If so, then

                // the current stay does not qualify.

                l_current_doa = in_doa;
                PreparedStatement pstmt17 = prepareStatement(
                          "select stay_id, acct_trans_id, doa"
                        + " from recent_stay_trans_list_fl0809f"
                        + " where linked_id = ?");
                
                if (n_link != null) {
                    pstmt17.setInt(1, n_link);
                }
                else {
                    pstmt17.setNull(1, Types.JAVA_OBJECT);
                }
                ResultSet rs10 = executeQuery(pstmt17);
                while (rs10.next()) {
                    l_linked_stay_id = rs10.getInt(1);
                    l_linked_trans_id = rs10.getInt(2);
                    l_doa = new DateTime(rs10.getTimestamp(3).getTime());
                    // if bonus already awarded for one of the linked

                    // stays, whether same doa or not, then we can just

                    // get out since we only award it once.

                    l_bonus_cnt = 0;
                    PreparedStatement pstmt18 = prepareStatement(
                              "select count(*)"
                            + " from acct_trans_detail d, promo p"
                            + " where d.acct_trans_id = ?"
                            + " and d.promo_id = ?"
                            + " and d.promo_id = p.promo_id"
                            + " and p.rule = \"S\"");
                    
                    if (l_linked_trans_id != null) {
                        pstmt18.setInt(1, l_linked_trans_id);
                    }
                    else {
                        pstmt18.setNull(1, Types.JAVA_OBJECT);
                    }
                    if (fl0809f_promo_id != null) {
                        pstmt18.setInt(2, fl0809f_promo_id);
                    }
                    else {
                        pstmt18.setNull(2, Types.JAVA_OBJECT);
                    }
                    ResultSet rs11 = executeQuery(pstmt18);
                    rs11.next();
                    l_bonus_cnt = rs11.getInt(1);
                    pstmt18.close();
                    rs11.close();
                    if (!l_bonus_cnt.equals(0)) {
                        // can't drop tables and return within cursor so set flag to quit

                        l_stop = 1;
                        break;
                    }
                    // if the linked stay has the same doa as previous stay

                    // then we have a concurrent stay and we just want to 

                    // not count it. 

                    if () {
                        continue;
                    }
                    l_current_doa = l_doa;
                    // now determine if the current linked stay being processed

                    // qualifies. First, get stay info.

                    PreparedStatement pstmt19 = prepareStatement(
                              "select s.res_source, s.los, s.doa, s.elig_revenue_pc"
                            + " from stay s"
                            + " where s.stay_id = ?");
                    
                    if (l_linked_stay_id != null) {
                        pstmt19.setInt(1, l_linked_stay_id);
                    }
                    else {
                        pstmt19.setNull(1, Types.JAVA_OBJECT);
                    }
                    ResultSet rs12 = executeQuery(pstmt19);
                    rs12.next();
                    l_linked_res_source = rs12.getString(1);
                    l_linked_los = rs12.getInt(2);
                    l_linked_doa = new DateTime(rs12.getTimestamp(3).getTime());
                    l_linked_elig_rev = rs12.getDouble(4);
                    pstmt19.close();
                    rs12.close();
                    // Next, get elite level info at time of doa

                    // get elite level at time of doa

                    PreparedStatement pstmt20 = prepareStatement(
                              "select elvl.name"
                            + " from acct_elite_level_hist aelh, elite_level elvl"
                            + " where aelh.acct_id = ?"
                            + " and aelh.elite_level_id = elvl.elite_level_id"
                            + " and aelh.acct_elite_level_hist_id = 
                                + "select max(els.acct_elite_level_hist_id)"
                                + " from acct_elite_level_hist els"
                                + "  inner join elite_level es on els.elite_level_id = es.elite_level_id"
                                + " where els.acct_id = ?"
                                + " and els.date_acquired <= ?"
                                + " and year(es.eff_year) = year(?)");
                        
                        if (in_acct_id != null) {
                            pstmt20.setInt(1, in_acct_id);
                        }
                        else {
                            pstmt20.setNull(1, Types.JAVA_OBJECT);
                        }
                        if (in_acct_id != null) {
                            pstmt20.setInt(2, in_acct_id);
                        }
                        else {
                            pstmt20.setNull(2, Types.JAVA_OBJECT);
                        }
                        if (l_linked_doa != null) {
                            pstmt20.setObject(3, l_linked_doa);
                        }
                        else {
                            pstmt20.setNull(3, Types.JAVA_OBJECT);
                        }
                        if (l_linked_doa != null) {
                            pstmt20.setObject(4, l_linked_doa);
                        }
                        else {
                            pstmt20.setNull(4, Types.JAVA_OBJECT);
                        }
                        ResultSet rs13 = executeQuery(pstmt20);
                        rs13.next();
                        l_linked_name = rs13.getString(1);
                        rs13.close();
                        if () {
                            l_linked_qualifies = 1;
                        }
                        else if (l_linked_name != null && (l_linked_name.equals("Platinum") || l_linked_name.equals("Diamond"))) {
                            l_linked_qualifies = 1;
                        }
                        else if (l_linked_res_source != null && (l_linked_res_source.equals("C") || l_linked_res_source.equals("N"))) {
                            l_linked_qualifies = 1;
                        }
                        else if (l_linked_res_source != null && l_linked_res_source.equals("G")) {
                            if (l_offer_dtime != null && (l_offer_dtime.isBefore(l_linked_doa) || l_offer_dtime.isEqual(l_linked_doa))) {
                                l_linked_qualifies = 1;
                            }
                            else {
                                l_linked_qualifies = 0;
                            }
                        }
                        else {
                            l_linked_qualifies = 0;
                        }
                        // if this one does not qualify, just go get the next one

                        if (l_linked_qualifies.equals(0)) {
                            continue;
                        }
                        // This linked stay qualifies. We need to determine how many nights 

                        // of this linked stay to use, based on how many nights we have already

                        // accumulated from previous linked stays processed.

                        if (l_linked_los < 3 - l_night_cnt) {
                            l_use = l_linked_los;
                        }
                        else {
                            l_use = 3 - l_night_cnt;
                        }
                        l_night_cnt = l_night_cnt + l_use;
                        // store amount based on revenue and number of nights being used

                        PreparedStatement pstmt21 = prepareInsert(
                                  "insert into pv_fl0809f_temp (los, amount)"
                                + " values (?, trunc(new convert_currency().execute(l_prop_curr_rate, l_earn_curr_rate, l_linked_elig_rev / l_linked_los.doubleValue() * l_use.doubleValue())))");
                        if (l_linked_los != null) {
                            pstmt21.setInt(1, l_linked_los);
                        }
                        else {
                            pstmt21.setNull(1, Types.JAVA_OBJECT);
                        }
                        executeUpdate(pstmt21);
                        pstmt21.close();
                        // if we've used our 3 nights, then get out

                        if (l_night_cnt.equals(3)) {
                            break;
                        }
                    }
                    pstmt20.close();
                    
                    // If the cursor said to stop, drop tables and get out

                    if (l_stop.equals(1)) {
                        PreparedStatement pstmt22 = prepareStatement("drop table recent_stay_trans_list_fl0809f");
                        executeUpdate(pstmt22);
                        pstmt22.close();
                        PreparedStatement pstmt23 = prepareStatement("drop table pv_fl0809f_temp");
                        executeUpdate(pstmt23);
                        pstmt23.close();
                        return "F";
                    }
                    // Done with linked stays. See if we have enough nights with

                    // qualifying linked nights and current stay nights to 

                    // trigger the bonus award.

                    // See how many current stay nights we need to use based on

                    // how many nights have been accumulated.

                    if (in_los < 3 - l_night_cnt) {
                        l_use = in_los;
                    }
                    else {
                        l_use = 3 - l_night_cnt;
                    }
                    l_night_cnt = l_night_cnt + l_use;
                    // If not enough nights were found, then drop and get out

                    if (l_night_cnt < 3) {
                        PreparedStatement pstmt24 = prepareStatement("drop table recent_stay_trans_list_fl0809f");
                        executeUpdate(pstmt24);
                        pstmt24.close();
                        PreparedStatement pstmt25 = prepareStatement("drop table pv_fl0809f_temp");
                        executeUpdate(pstmt25);
                        pstmt25.close();
                        return "F";
                    }
                    // if current qualifies and we need to use some of its nights,

                    // then insert row for pv, drop table and return true

                    if (l_qualifies.equals(1) && l_use > 0) {
                        PreparedStatement pstmt26 = prepareInsert(
                                  "insert into pv_fl0809f_temp (los, amount)"
                                + " values (?, trunc(new convert_currency().execute(l_prop_curr_rate, l_earn_curr_rate, in_rm_revenue / in_los.doubleValue() * l_use.doubleValue())))");
                        if (l_linked_los != null) {
                            pstmt26.setInt(1, l_linked_los);
                        }
                        else {
                            pstmt26.setNull(1, Types.JAVA_OBJECT);
                        }
                        executeUpdate(pstmt26);
                        pstmt26.close();
                        PreparedStatement pstmt27 = prepareStatement("drop table recent_stay_trans_list_fl0809f");
                        executeUpdate(pstmt27);
                        pstmt27.close();
                        return "T";
                    }
                    // no bonus eligible so drop tables and return false

                    PreparedStatement pstmt28 = prepareStatement("drop table recent_stay_trans_list_fl0809f");
                    executeUpdate(pstmt28);
                    pstmt28.close();
                    PreparedStatement pstmt29 = prepareStatement("drop table pv_fl0809f_temp");
                    executeUpdate(pstmt29);
                    pstmt29.close();
                    return "F";
                }
                catch (SQLException e) {
                    sql_error = e.getErrorCode();
                    isam_error = 0;
                    error_data = e.getMessage();
                    {
                        PreparedStatement pstmt30 = prepareStatement("drop table recent_stay_trans_list_fl0809f");
                        executeUpdate(pstmt30);
                        pstmt30.close();
                        PreparedStatement pstmt31 = prepareStatement("drop table pv_fl0809f_temp");
                        executeUpdate(pstmt31);
                        pstmt31.close();
                        throw new ProcedureException(sql_error, isam_error, error_data);
                    }
                }
            }
        
        }