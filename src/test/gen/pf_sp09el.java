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

public class pf_sp09el extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public String execute(Integer in_acct_id, Integer in_promo_id, String in_stay_type, Integer in_prop_id, DateTime in_doa, Integer in_los, String in_rm_type, String in_srp_code, Double in_rm_revenue, Double in_fb_revenue, Double in_other_revenue, String in_curr_code, String in_res_source, Integer in_stay_id) throws SQLException, ProcedureException {
        /*
         * pf_sp09el.sql -    Check if stay is eligible for the 'Stay 2 times and earn 1 free night'
         *                      Spring 2009 Elite stay twice promotion. The stays in question are
         *                      restricted to DOA falling within the promotion start and end dates. Also the
         *                      stays must have qualified for points.
         *                      
         *                      Note that since this promotion requires Platinum or Diamond Elite, stays for 
         *                      non-elite or just Gold will get knocked off at the account eligibility test.
         *                      It is the other promotion SP09NE where we specifically test for no/Gold elite.
         *                      
         *    
         * $Id: pf_sp09el.sql 21655 2009-04-15 23:48:28Z kdamron $
         *    
         *        Copyright (C) 2009 Choice Hotels International, Inc.
         */
        Integer no_bonus_cnt;
        Integer has_bonus_cnt;
        Integer des_bonus_cnt;
        Integer stay_cnt;
        Integer exclusion_cnt;
        DateTime start_date;
        DateTime stop_date;
        String recog_cd;
        Integer chk_el_promo_id;
        Integer chk_ne_promo_id;
        Integer l_stay_id;
        String l_prop_cd;
        DateTime l_doa;
        DateTime l_dod;
        Integer l_linked_id;
        Integer l_trans_id;
        DateTime f_dod;
        Integer n_id;
        Integer n_link;
        DateTime offer_dtime;
        String l_name;
        String l_res_source;
        DateTime l_signup_date;
        no_bonus_cnt = 0;
        has_bonus_cnt = 0;
        des_bonus_cnt = 0;
        stay_cnt = 0;
        exclusion_cnt = 0;
        start_date = null;
        stop_date = null;
        recog_cd = null;
        chk_el_promo_id = null;
        chk_ne_promo_id = null;
        l_stay_id = null;
        l_prop_cd = null;
        l_doa = null;
        l_dod = null;
        l_linked_id = null;
        l_trans_id = null;
        f_dod = in_doa.plusDays(in_los);
        n_id = null;
        n_link = null;
        offer_dtime = null;
        l_name = null;
        l_res_source = null;
        l_signup_date = null;
        //set debug file to '/tmp/pf_sp09el.trace';

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
                  "select acct.recog_cd, acct.signup_date"
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
        recog_cd = rs2.getString(1);
        l_signup_date = new DateTime(rs2.getTimestamp(2).getTime());
        pstmt2.close();
        rs2.close();
        // Chk that the acct is Platinum or Diamond on the DOA not posting date.

        PreparedStatement pstmt3 = prepareStatement(
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
                pstmt3.setInt(1, in_acct_id);
            }
            else {
                pstmt3.setNull(1, Types.JAVA_OBJECT);
            }
            if (in_acct_id != null) {
                pstmt3.setInt(2, in_acct_id);
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
            if (in_doa != null) {
                pstmt3.setObject(4, in_doa);
            }
            else {
                pstmt3.setNull(4, Types.JAVA_OBJECT);
            }
            ResultSet rs3 = executeQuery(pstmt3);
            rs3.next();
            l_name = rs3.getString(1);
            rs3.close();
            if (l_name == null) {
                return "F";
            }
            if (l_name.equals("Gold")) {
                return "F";
            }
            // Get the promo IDs for counting awarded bonuses

            // NOTE: The spring 2009 has 2 competing promotions elite/non-elite so we get both IDs!

            PreparedStatement pstmt4 = prepareStatement(
                      "select promo.promo_id"
                    + " from promo"
                    + " where promo.recog_cd = ?"
                    + " and promo.promo_cd = \"SP09EL\"");
            
            if (recog_cd != null) {
                pstmt4.setString(1, recog_cd);
            }
            else {
                pstmt4.setNull(1, Types.JAVA_OBJECT);
            }
            ResultSet rs4 = executeQuery(pstmt4);
            rs4.next();
            chk_el_promo_id = rs4.getInt(1);
            pstmt4.close();
            rs4.close();
            PreparedStatement pstmt5 = prepareStatement(
                      "select promo.promo_id"
                    + " from promo"
                    + " where promo.recog_cd = ?"
                    + " and promo.promo_cd = \"SP09NE\"");
            
            if (recog_cd != null) {
                pstmt5.setString(1, recog_cd);
            }
            else {
                pstmt5.setNull(1, Types.JAVA_OBJECT);
            }
            ResultSet rs5 = executeQuery(pstmt5);
            rs5.next();
            chk_ne_promo_id = rs5.getInt(1);
            pstmt5.close();
            rs5.close();
            // Get a list of stays to consider

            // Fortunately, both promotions have the same property participation requirements thus

            // checking on the current promotion is enough. The resultant list of stays will be accurate
Iterator<Object> it0 = new find_linked_stays_by_promo().execute(in_promo_id, start_date, stop_date, in_acct_id).iterator();
            while (it0.hasNext()) {
                l_stay_id = (Integer) it0.next();
                l_prop_cd = (String) it0.next();
                l_doa = (DateTime) it0.next();
                l_dod = (DateTime) it0.next();
                l_linked_id = (Integer) it0.next();
                l_trans_id = (Integer) it0.next();
                PreparedStatement pstmt6 = prepareInsert(
                          "insert into recent_stay_trans_list (stay_id, prop_cd, doa, dod, linked_id, acct_trans_id)"
                        + " values (?, ?, ?, ?, nvl(?, ?), ?)");
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
                executeUpdate(pstmt6);
                pstmt6.close();
            }
            // add current stay to list

            PreparedStatement pstmt7 = prepareStatement(
                      "select max(l.id)"
                    + " from recent_stay_trans_list l"
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
                        + " from recent_stay_trans_list l"
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
                      "insert into recent_stay_trans_list (stay_id, prop_cd, doa, dod, linked_id)"
                    + " values (?, new get_prop_cd().execute(in_prop_id), ?, ?, ?)");
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
            executeUpdate(pstmt9);
            pstmt9.close();
            // Examine each unique stay for elite level at time of doa. If elite

            // or Gold and was not booked through central res or internet, then

            // it should be excluded. If it was first stay, regardless of elite

            // level or booking, it should not be excluded.

            PreparedStatement pstmt10 = prepareStatement(
                      "select distinct linked_id"
                    + " from recent_stay_trans_list");
            
            ResultSet rs8 = executeQuery(pstmt10);
            while (rs8.next()) {
                l_linked_id = rs8.getInt(1);
                // current stay excluded later

                if (l_linked_id.equals(in_stay_id)) {
                    continue;
                }
                // get booking source and doa for stay

                PreparedStatement pstmt11 = prepareStatement(
                          "select s.res_source, s.doa"
                        + " from stay s"
                        + " where s.stay_id = ?");
                
                if (l_linked_id != null) {
                    pstmt11.setInt(1, l_linked_id);
                }
                else {
                    pstmt11.setNull(1, Types.JAVA_OBJECT);
                }
                ResultSet rs9 = executeQuery(pstmt11);
                rs9.next();
                l_res_source = rs9.getString(1);
                l_doa = new DateTime(rs9.getTimestamp(2).getTime());
                pstmt11.close();
                rs9.close();
                // first stay is counted regardless; geared to customer signing

                // up at hotel and staying on same day

                if () {
                    continue;
                }
                // get elite level at time for the stay being examined

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
                    ResultSet rs10 = executeQuery(pstmt12);
                    rs10.next();
                    l_name = rs10.getString(1);
                    rs10.close();
                    // if not central res or internet booked and not at least

                    // Platinum, exclude it

                    if (l_name == null || l_name.equals("Gold")) {
                        if (l_res_source == null || (!l_res_source.equals("C") && !l_res_source.equals("N"))) {
                            exclusion_cnt = exclusion_cnt + 1;
                        }
                    }
                }
                pstmt12.close();
                
                // Get a count of non-reversed stays with either promotion.

                PreparedStatement pstmt13 = prepareStatement(
                          "select count(*)"
                        + " from recent_stay_trans_list l"
                        + " where l.acct_trans_id in ("
                            + "select ad.acct_trans_id"
                            + " from acct_trans_detail ad"
                            + " where ad.acct_trans_id = l.acct_trans_id"
                            + " and ad.promo_id in (?, ?)"
                            + " and l.doa >= ?"
                            + " and l.doa <= ?"
                            + ")");
                    
                    if (chk_el_promo_id != null) {
                        pstmt13.setInt(1, chk_el_promo_id);
                    }
                    else {
                        pstmt13.setNull(1, Types.JAVA_OBJECT);
                    }
                    if (chk_ne_promo_id != null) {
                        pstmt13.setInt(2, chk_ne_promo_id);
                    }
                    else {
                        pstmt13.setNull(2, Types.JAVA_OBJECT);
                    }
                    if (start_date != null) {
                        pstmt13.setObject(3, start_date);
                    }
                    else {
                        pstmt13.setNull(3, Types.JAVA_OBJECT);
                    }
                    if (stop_date != null) {
                        pstmt13.setObject(4, stop_date);
                    }
                    else {
                        pstmt13.setNull(4, Types.JAVA_OBJECT);
                    }
                    ResultSet rs11 = executeQuery(pstmt13);
                    rs11.next();
                    has_bonus_cnt = rs11.getInt(1);
                    rs11.close();
                    // Get a count of stays 

                    PreparedStatement pstmt14 = prepareStatement(
                              "select count(distinct linked_id)"
                            + " from recent_stay_trans_list l"
                            + " where l.doa >= ?"
                            + " and l.doa <= ?");
                    
                    if (start_date != null) {
                        pstmt14.setObject(1, start_date);
                    }
                    else {
                        pstmt14.setNull(1, Types.JAVA_OBJECT);
                    }
                    if (stop_date != null) {
                        pstmt14.setObject(2, stop_date);
                    }
                    else {
                        pstmt14.setNull(2, Types.JAVA_OBJECT);
                    }
                    ResultSet rs12 = executeQuery(pstmt14);
                    rs12.next();
                    stay_cnt = rs12.getInt(1);
                    pstmt14.close();
                    rs12.close();
                    // Drop the temp table 

                    PreparedStatement pstmt15 = prepareStatement("drop table recent_stay_trans_list");
                    executeUpdate(pstmt15);
                    pstmt15.close();
                    if (stay_cnt > 0) {
                        des_bonus_cnt = stay_cnt - has_bonus_cnt - exclusion_cnt - 1;
                        // excludes current stay being processed

                        if (des_bonus_cnt > has_bonus_cnt) {
                            return "T";
                        }
                        else {
                            return "F";
                        }
                    }
                    else {
                        return "F";
                    }
                }
            
            }