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

public class pf_su09m extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public String execute(Integer in_acct_id, Integer in_promo_id, String in_stay_type, Integer in_prop_id, DateTime in_doa, Integer in_los, String in_rm_type, String in_srp_code, Double in_rm_revenue, Double in_fb_revenue, Double in_other_revenue, String in_curr_code, String in_res_source, Integer in_stay_id) throws SQLException, ProcedureException {
        /*
         * pf_su09m.sql - Check if stay is eligible for the 'Stay 3 times and earn points for cash card'
         *                The stays in question are restricted to DOA falling within the promotion start 
         *                and end dates. Also the stays must have qualified for points.
         *                      
         *                Stays must be booked through ch.com or central res unless elite status
         *                is Platinum or Diamond. An enrollment stay is eligible regardless of
         *                booking source.         
         *    
         * $Id: pf_su09m.sql 22868 2009-08-05 19:01:21Z eelliot $
         *    
         *        Copyright (C) 2009 Choice Hotels International, Inc.
         */
        Integer answer;
        Integer has_bonus_cnt;
        Integer des_bonus_cnt;
        DateTime start_date;
        DateTime stop_date;
        String l_recog_cd;
        Integer su09m_promo_id;
        Integer l_stay_id;
        Integer l_stay_id2;
        String l_prop_cd;
        DateTime l_doa;
        DateTime l_dod;
        Integer l_linked_id;
        Integer l_trans_id;
        DateTime f_dod;
        Integer n_id;
        Integer n_link;
        String l_name;
        String l_res_source;
        DateTime l_signup_date;
        Integer l_max_trans_id;
        Integer l_linked_elig;
        Integer l_qualifies;
        Double l_current_amount;
        Double l_total_amount;
        Integer l_total_qualified;
        Integer l_seen_id;
        Integer l_appl_group;
        String l_chain_id;
        String l_mkt_area;
        String l_country;
        String l_prop_country;
        String l_prop_type;
        String l_ioc_region;
        String l_prop_class;
        String l_prop_curr_cd;
        String l_stay_type;
        Integer l_ord;
        Integer sql_error;
        Integer isam_error;
        String error_data;
        try {
            answer = 0;
            has_bonus_cnt = 0;
            des_bonus_cnt = 0;
            start_date = null;
            stop_date = null;
            l_recog_cd = null;
            su09m_promo_id = null;
            l_stay_id = null;
            l_stay_id2 = null;
            l_linked_id = null;
            l_prop_cd = null;
            l_doa = null;
            l_dod = null;
            l_trans_id = null;
            f_dod = in_doa.plusDays(in_los);
            n_id = null;
            n_link = null;
            l_name = null;
            l_res_source = null;
            l_signup_date = null;
            l_max_trans_id = 0;
            l_linked_elig = 0;
            l_qualifies = 0;
            l_current_amount = 0.0;
            l_total_amount = 0.0;
            l_total_qualified = 0;
            l_seen_id = 0;
            l_appl_group = 0;
            l_chain_id = null;
            l_mkt_area = null;
            l_country = null;
            l_prop_country = null;
            l_prop_type = null;
            l_ioc_region = null;
            l_prop_class = null;
            l_prop_curr_cd = null;
            l_stay_type = null;
            l_ord = 1;
            //set debug file to '/tmp/pf_su09m.trace';

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
            // Get the program code of the account

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
            // Get the promo ID for counting awarded bonuses

            PreparedStatement pstmt3 = prepareStatement(
                      "select promo.promo_id"
                    + " from promo"
                    + " where promo.recog_cd = ?"
                    + " and promo.promo_cd = \"SU09M\"");
            
            if (l_recog_cd != null) {
                pstmt3.setString(1, l_recog_cd);
            }
            else {
                pstmt3.setNull(1, Types.JAVA_OBJECT);
            }
            ResultSet rs3 = executeQuery(pstmt3);
            rs3.next();
            su09m_promo_id = rs3.getInt(1);
            pstmt3.close();
            rs3.close();
            // need to check if the current stay property is valid before doing anything 

            // else. For example, an economy stay can show up as the current stay and

            // it will get processed as a mid-scale! 

            PreparedStatement pstmt4 = prepareStatement(
                      "select stay_type, prop_curr_cd"
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
            l_stay_type = rs4.getString(1);
            l_prop_curr_cd = rs4.getString(2);
            pstmt4.close();
            rs4.close();
            PreparedStatement pstmt5 = prepareStatement(
                      "select chain_id, mkt_area, country, prop_type, ioc_region, prop_class, country"
                    + " from prop"
                    + " where prop_id = ?");
            
            if (in_prop_id != null) {
                pstmt5.setInt(1, in_prop_id);
            }
            else {
                pstmt5.setNull(1, Types.JAVA_OBJECT);
            }
            ResultSet rs5 = executeQuery(pstmt5);
            rs5.next();
            l_chain_id = rs5.getString(1);
            l_mkt_area = rs5.getString(2);
            l_country = rs5.getString(3);
            l_prop_type = rs5.getString(4);
            l_ioc_region = rs5.getString(5);
            l_prop_class = rs5.getString(6);
            l_prop_country = rs5.getString(7);
            pstmt5.close();
            rs5.close();
            answer = new chk_prop_partic().execute(in_acct_id, in_prop_id, in_doa, in_los, in_rm_type, in_srp_code, in_rm_revenue, in_fb_revenue, in_other_revenue, in_promo_id, l_recog_cd, l_prop_curr_cd, l_appl_group, l_chain_id, l_mkt_area, l_prop_country, l_prop_type, l_ioc_region, l_prop_class, in_stay_id, l_stay_type);
            if (answer.equals(0)) {
                return "F";
            }
            // Temp table to hold stays to consider

            // Temp table passed to pv_su09m. It will hold the stays

            // which qualify for the bonus. The first three stays

            // will be the stays utilized by pv_su09m.

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
                          "insert into recent_stay_trans_list_su09 (stay_id, prop_cd, doa, dod, linked_id, acct_trans_id, ord)"
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
            // add current stay to list

            PreparedStatement pstmt7 = prepareStatement(
                      "select max(l.id)"
                    + " from recent_stay_trans_list_su09 l"
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
                        + " from recent_stay_trans_list_su09 l"
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
            PreparedStatement pstmt9 = prepareInsert(
                      "insert into recent_stay_trans_list_su09 (stay_id, prop_cd, doa, dod, linked_id, ord)"
                    + " values (?, new get_prop_cd().execute(in_prop_id), ?, ?, ?, ?)");
            if (in_stay_id != null) {
                pstmt9.setInt(1, in_stay_id);
            }
            else {
                pstmt9.setNull(1, Types.JAVA_OBJECT);
            }
            if (in_doa != null) {
                pstmt9.setObject(2, in_doa);
            }
            else {
                pstmt9.setNull(2, Types.JAVA_OBJECT);
            }
            if (f_dod != null) {
                pstmt9.setObject(3, f_dod);
            }
            else {
                pstmt9.setNull(3, Types.JAVA_OBJECT);
            }
            if (n_link != null) {
                pstmt9.setInt(4, n_link);
            }
            else {
                pstmt9.setNull(4, Types.JAVA_OBJECT);
            }
            if (l_ord != null) {
                pstmt9.setInt(5, l_ord);
            }
            else {
                pstmt9.setNull(5, Types.JAVA_OBJECT);
            }
            executeUpdate(pstmt9);
            pstmt9.close();
            // Examine each unique stay to determine if it should be excluded from eligibility

            // Rules are:

            //        Enrollment stay is valid regardless of booking source.

            //        Platinum and Diamond at DOA are eligible regardless of booking source. 

            //        Non-elite or Gold must have booked through ch.com or central res to be eligible

            // first, count current number of bonuses that have already been awarded

            PreparedStatement pstmt10 = prepareStatement(
                      "select count(*)"
                    + " from recent_stay_trans_list_su09 l"
                    + " where l.acct_trans_id in ("
                        + "select ad.acct_trans_id"
                        + " from acct_trans_detail ad"
                        + " where ad.acct_trans_id = l.acct_trans_id"
                        + " and ad.promo_id = ?"
                        + " and l.doa >= ?"
                        + " and l.doa <= ?"
                        + ")");
                
                if (su09m_promo_id != null) {
                    pstmt10.setInt(1, su09m_promo_id);
                }
                else {
                    pstmt10.setNull(1, Types.JAVA_OBJECT);
                }
                if (start_date != null) {
                    pstmt10.setObject(2, start_date);
                }
                else {
                    pstmt10.setNull(2, Types.JAVA_OBJECT);
                }
                if (stop_date != null) {
                    pstmt10.setObject(3, stop_date);
                }
                else {
                    pstmt10.setNull(3, Types.JAVA_OBJECT);
                }
                ResultSet rs8 = executeQuery(pstmt10);
                rs8.next();
                has_bonus_cnt = rs8.getInt(1);
                rs8.close();
                // This cur1 cursor processes each stay that is not linked. We will

                // see if each one qualifies. If it does, we collect the amount of 

                // points for it. Then, we need to check any linked stays using 

                // another cursor to see if their amount of points should be added

                // to the current cur1 stay we are processing.

                PreparedStatement pstmt11 = prepareStatement(
                          "select stay_id, linked_id, acct_trans_id"
                        + " from recent_stay_trans_list_su09"
                        + " where stay_id = linked_id"
                        + " order by ord desc");
                
                ResultSet rs9 = executeQuery(pstmt11);
                while (rs9.next()) {
                    l_stay_id = rs9.getInt(1);
                    l_linked_id = rs9.getInt(2);
                    l_max_trans_id = rs9.getInt(3);
                    l_total_amount = 0.0;
                    l_qualifies = 0;
                    // get booking source and doa for stay

                    PreparedStatement pstmt12 = prepareStatement(
                              "select s.res_source, s.doa"
                            + " from stay s"
                            + " where s.stay_id = ?");
                    
                    if (l_stay_id != null) {
                        pstmt12.setInt(1, l_stay_id);
                    }
                    else {
                        pstmt12.setNull(1, Types.JAVA_OBJECT);
                    }
                    ResultSet rs10 = executeQuery(pstmt12);
                    rs10.next();
                    l_res_source = rs10.getString(1);
                    l_doa = new DateTime(rs10.getTimestamp(2).getTime());
                    pstmt12.close();
                    rs10.close();
                    // Calculate/get the number of points for the stay.

                    // The current stay does not have an acct_trans record yet. So,

                    // the points are determined by revenue passed in whereas all the

                    // other stays can get their amount from the acct_trans_detail record.

                    l_current_amount = 0.0;
                    if (l_max_trans_id != null) {
                        PreparedStatement pstmt13 = prepareStatement(
                                  "select d.amount"
                                + " from acct_trans_detail d, promo p"
                                + " where d.acct_trans_id = ?"
                                + " and d.promo_id = p.promo_id"
                                + " and p.rule = \"A\"");
                        
                        if (l_max_trans_id != null) {
                            pstmt13.setInt(1, l_max_trans_id);
                        }
                        else {
                            pstmt13.setNull(1, Types.JAVA_OBJECT);
                        }
                        ResultSet rs11 = executeQuery(pstmt13);
                        rs11.next();
                        l_current_amount = rs11.getDouble(1);
                        pstmt13.close();
                        rs11.close();
                    }
                    else {
                        l_current_amount = in_rm_revenue * 10.0;
                    }
                    // Determine if the stay qualifies for the bonus.

                    // If doa and sign up date are the same, it is counted regardless

                    if () {
                        l_qualifies = 1;
                        l_total_amount = l_total_amount + l_current_amount;
                    }
                    // Get elite level at time for the stay being examined if it has not

                    // already qualified.

                    if (l_qualifies.equals(0)) {
                        PreparedStatement pstmt14 = prepareStatement(
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
                                pstmt14.setInt(1, in_acct_id);
                            }
                            else {
                                pstmt14.setNull(1, Types.JAVA_OBJECT);
                            }
                            if (in_acct_id != null) {
                                pstmt14.setInt(2, in_acct_id);
                            }
                            else {
                                pstmt14.setNull(2, Types.JAVA_OBJECT);
                            }
                            if (l_doa != null) {
                                pstmt14.setObject(3, l_doa);
                            }
                            else {
                                pstmt14.setNull(3, Types.JAVA_OBJECT);
                            }
                            if (l_doa != null) {
                                pstmt14.setObject(4, l_doa);
                            }
                            else {
                                pstmt14.setNull(4, Types.JAVA_OBJECT);
                            }
                            ResultSet rs12 = executeQuery(pstmt14);
                            rs12.next();
                            l_name = rs12.getString(1);
                            rs12.close();
                            // if not central res or internet booked and not at least

                            // Platinum, it does not qualify.

                            if (l_name != null && (l_name.equals("Platinum") || l_name.equals("Diamond"))) {
                                l_qualifies = 1;
                                l_total_amount = l_total_amount + l_current_amount;
                            }
                            else {
                                if (l_res_source != null && (l_res_source.equals("C") || l_res_source.equals("N"))) {
                                    l_qualifies = 1;
                                    l_total_amount = l_total_amount + l_current_amount;
                                }
                            }
                        }
                        // Now look at potential linked stays; i.e. stays where the linked_stay_id 

                        // equals the stay_id being processed in cursor cur1. If any are found,

                        // see if each is eligible, and accumulate for the "stay".

                        PreparedStatement pstmt15 = prepareStatement(
                                  "select stay_id, acct_trans_id"
                                + " from recent_stay_trans_list_su09"
                                + " where linked_id = ?"
                                + " and stay_id != ?");
                        
                        if (l_stay_id != null) {
                            pstmt15.setInt(1, l_stay_id);
                        }
                        else {
                            pstmt15.setNull(1, Types.JAVA_OBJECT);
                        }
                        if (l_stay_id != null) {
                            pstmt15.setInt(2, l_stay_id);
                        }
                        else {
                            pstmt15.setNull(2, Types.JAVA_OBJECT);
                        }
                        ResultSet rs13 = executeQuery(pstmt15);
                        while (rs13.next()) {
                            l_stay_id2 = rs13.getInt(1);
                            l_trans_id = rs13.getInt(2);
                            // Now see if this linked stay is eligible. Go through the same

                            // checks as the cur1 stay. If any of these are eligible, add

                            // their point amount to the current running total.

                            l_linked_elig = 0;
                            // get booking source and doa for stay

                            PreparedStatement pstmt16 = prepareStatement(
                                      "select s.res_source, s.doa"
                                    + " from stay s"
                                    + " where s.stay_id = ?");
                            
                            if (l_stay_id2 != null) {
                                pstmt16.setInt(1, l_stay_id2);
                            }
                            else {
                                pstmt16.setNull(1, Types.JAVA_OBJECT);
                            }
                            ResultSet rs14 = executeQuery(pstmt16);
                            rs14.next();
                            l_res_source = rs14.getString(1);
                            l_doa = new DateTime(rs14.getTimestamp(2).getTime());
                            pstmt16.close();
                            rs14.close();
                            if (l_trans_id != null) {
                                PreparedStatement pstmt17 = prepareStatement(
                                          "select d.amount"
                                        + " from acct_trans_detail d, promo p"
                                        + " where d.acct_trans_id = ?"
                                        + " and d.promo_id = p.promo_id"
                                        + " and p.rule = \"A\"");
                                
                                if (l_trans_id != null) {
                                    pstmt17.setInt(1, l_trans_id);
                                }
                                else {
                                    pstmt17.setNull(1, Types.JAVA_OBJECT);
                                }
                                ResultSet rs15 = executeQuery(pstmt17);
                                rs15.next();
                                l_current_amount = rs15.getDouble(1);
                                pstmt17.close();
                                rs15.close();
                            }
                            else {
                                l_current_amount = in_rm_revenue * 10.0;
                            }
                            if () {
                                l_total_amount = l_total_amount + l_current_amount;
                                l_linked_elig = 1;
                            }
                            if (l_linked_elig.equals(0)) {
                                PreparedStatement pstmt18 = prepareStatement(
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
                                        pstmt18.setInt(1, in_acct_id);
                                    }
                                    else {
                                        pstmt18.setNull(1, Types.JAVA_OBJECT);
                                    }
                                    if (in_acct_id != null) {
                                        pstmt18.setInt(2, in_acct_id);
                                    }
                                    else {
                                        pstmt18.setNull(2, Types.JAVA_OBJECT);
                                    }
                                    if (l_doa != null) {
                                        pstmt18.setObject(3, l_doa);
                                    }
                                    else {
                                        pstmt18.setNull(3, Types.JAVA_OBJECT);
                                    }
                                    if (l_doa != null) {
                                        pstmt18.setObject(4, l_doa);
                                    }
                                    else {
                                        pstmt18.setNull(4, Types.JAVA_OBJECT);
                                    }
                                    ResultSet rs16 = executeQuery(pstmt18);
                                    rs16.next();
                                    l_name = rs16.getString(1);
                                    rs16.close();
                                    // if not central res or internet booked and not at least

                                    // Platinum, exclude it

                                    if (l_name != null && (l_name.equals("Platinum") || l_name.equals("Diamond"))) {
                                        l_total_amount = l_total_amount + l_current_amount;
                                        l_linked_elig = 1;
                                    }
                                    else {
                                        if (l_res_source != null && (l_res_source.equals("C") || l_res_source.equals("N"))) {
                                            l_total_amount = l_total_amount + l_current_amount;
                                            l_linked_elig = 1;
                                        }
                                    }
                                }
                            }
                            
                            // if we have a qualified stay, insert into table for pv_ code

                            if (l_qualifies > 0 || l_linked_elig > 0) {
                                PreparedStatement pstmt19 = prepareInsert(
                                          "insert into qualified_stays_pv_su09 (amount)"
                                        + " values (?)");
                                if (l_total_amount != null) {
                                    pstmt19.setDouble(1, l_total_amount);
                                }
                                else {
                                    pstmt19.setNull(1, Types.JAVA_OBJECT);
                                }
                                executeUpdate(pstmt19);
                                pstmt19.close();
                                l_total_qualified = l_total_qualified + 1;
                            }
                        }
                        pstmt15.close();
                        rs13.close();
                        PreparedStatement pstmt20 = prepareStatement("drop table recent_stay_trans_list_su09");
                        executeUpdate(pstmt20);
                        pstmt20.close();
                        if (l_total_qualified > 0) {
                            des_bonus_cnt = l_total_qualified / 3;
                            if (des_bonus_cnt > has_bonus_cnt) {
                                // sanity check to see if we've written enough rows to table for pv_su09m

                                if (l_total_qualified < 3) {
                                    throw new ProcedureException(-746, 0, "pf_su09m: Bonus deserved but not enough data for pv_su09m");
                                }
                                return "T";
                            }
                        }
                        PreparedStatement pstmt21 = prepareStatement("drop table qualified_stays_pv_su09");
                        executeUpdate(pstmt21);
                        pstmt21.close();
                        return "F";
                    }
                    catch (SQLException e) {
                        sql_error = e.getErrorCode();
                        isam_error = 0;
                        error_data = e.getMessage();
                        {
                            PreparedStatement pstmt22 = prepareStatement("drop table recent_stay_trans_list_su09");
                            executeUpdate(pstmt22);
                            pstmt22.close();
                            PreparedStatement pstmt23 = prepareStatement("drop table qualified_stays_pv_su09");
                            executeUpdate(pstmt23);
                            pstmt23.close();
                            throw new ProcedureException(sql_error, isam_error, error_data);
                        }
                    }
                }
            
            }