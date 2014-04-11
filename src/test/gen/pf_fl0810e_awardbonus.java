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

public class pf_fl0810e_awardbonus extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public String execute(Integer in_acct_id, Integer in_promo_id, String in_stay_type, Integer in_prop_id, DateTime in_doa, Integer in_los, String in_rm_type, String in_srp_code, Double in_rm_revenue, Double in_fb_revenue, Double in_other_revenue, String in_curr_code, String in_res_source, Integer in_stay_id) throws SQLException, ProcedureException {
        /*
         * pf_fl0810e_awardbonus.sql - If this routine is called, then the stay being processed meets the
         *                             Fall 2010 criteria. Now ensure that the bonus has not already been
         *                             awarded from some other consecutive or multi room stay. We just have
         *                             to check if the bonus has been awarded, we do not have to worry whether
         *                             linked stays are eligible.
         *    
         * $Id: pf_fl0810e_awardbonus.sql 31665 2010-09-14 17:28:45Z mgiroux $
         *    
         *        Copyright (C) 2010 Choice Hotels International, Inc.
         */
        DateTime start_date;
        DateTime stop_date;
        String l_recog_cd;
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
        DateTime l_signup_date;
        Integer l_acct_trans_id;
        String l_linked_elig;
        Double l_current_amount;
        Integer l_total_qualified;
        Integer l_ord;
        DateTime l_post_dtime;
        DateTime l_curr_post_dtime;
        Integer l_already_awarded;
        Double l_linked_amount;
        Integer l_offer_id;
        Integer l_total_points;
        Double l_pts_dollar;
        Integer l_elig_stay_count;
        String l_is_elig;
        Integer sql_error;
        Integer isam_error;
        String error_data;
        try {
            start_date = null;
            stop_date = null;
            l_recog_cd = null;
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
            l_signup_date = null;
            l_acct_trans_id = 0;
            l_linked_elig = null;
            l_current_amount = 0.0;
            l_total_qualified = 0;
            l_ord = 1;
            l_post_dtime = null;
            l_curr_post_dtime = null;
            l_already_awarded = 0;
            l_linked_amount = 0.0;
            l_offer_id = null;
            l_total_points = 0;
            l_pts_dollar = 0.0;
            l_elig_stay_count = 0;
            l_is_elig = null;
            //set debug file to '/tmp/pf_fl0810e_awardbonus.trace';

            //trace on;

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
            // Temp table to hold stays to consider

            // Get a list of linked stays to consider
Iterator<Object> it0 = new find_linked_stays_by_promo().execute(in_promo_id, start_date, stop_date, in_acct_id).iterator();
            while (it0.hasNext()) {
                l_stay_id = (Integer) it0.next();
                l_prop_cd = (String) it0.next();
                l_doa = (DateTime) it0.next();
                l_dod = (DateTime) it0.next();
                l_linked_id = (Integer) it0.next();
                l_trans_id = (Integer) it0.next();
                PreparedStatement pstmt3 = prepareStatement(
                          "select post_dtime"
                        + " from stay"
                        + " where stay_id = ?");
                
                if (l_stay_id != null) {
                    pstmt3.setInt(1, l_stay_id);
                }
                else {
                    pstmt3.setNull(1, Types.JAVA_OBJECT);
                }
                ResultSet rs3 = executeQuery(pstmt3);
                rs3.next();
                l_post_dtime = new DateTime(rs3.getTimestamp(1).getTime());
                pstmt3.close();
                rs3.close();
                PreparedStatement pstmt4 = prepareInsert(
                          "insert into recent_stay_trans_list_fl10e (stay_id, prop_cd, doa, dod, linked_id, acct_trans_id, ord, post_dtime)"
                        + " values (?, ?, ?, ?, nvl(?, ?), ?, ?, ?)");
                if (l_stay_id != null) {
                    pstmt4.setInt(1, l_stay_id);
                }
                else {
                    pstmt4.setNull(1, Types.JAVA_OBJECT);
                }
                if (l_prop_cd != null) {
                    pstmt4.setString(2, l_prop_cd);
                }
                else {
                    pstmt4.setNull(2, Types.JAVA_OBJECT);
                }
                if (l_doa != null) {
                    pstmt4.setObject(3, l_doa);
                }
                else {
                    pstmt4.setNull(3, Types.JAVA_OBJECT);
                }
                if (l_dod != null) {
                    pstmt4.setObject(4, l_dod);
                }
                else {
                    pstmt4.setNull(4, Types.JAVA_OBJECT);
                }
                if (l_linked_id != null) {
                    pstmt4.setInt(5, l_linked_id);
                }
                else {
                    pstmt4.setNull(5, Types.JAVA_OBJECT);
                }
                if (l_stay_id != null) {
                    pstmt4.setInt(6, l_stay_id);
                }
                else {
                    pstmt4.setNull(6, Types.JAVA_OBJECT);
                }
                if (l_trans_id != null) {
                    pstmt4.setInt(7, l_trans_id);
                }
                else {
                    pstmt4.setNull(7, Types.JAVA_OBJECT);
                }
                if (l_ord != null) {
                    pstmt4.setInt(8, l_ord);
                }
                else {
                    pstmt4.setNull(8, Types.JAVA_OBJECT);
                }
                if (l_post_dtime != null) {
                    pstmt4.setObject(9, l_post_dtime);
                }
                else {
                    pstmt4.setNull(9, Types.JAVA_OBJECT);
                }
                executeUpdate(pstmt4);
                pstmt4.close();
                l_ord = l_ord + 1;
            }
            // add current stay to list

            PreparedStatement pstmt5 = prepareStatement(
                      "select max(l.id)"
                    + " from recent_stay_trans_list_fl10e l"
                    + " where l.prop_cd = new get_prop_cd().execute(in_prop_id)"
                    + " and  ||  ||  || ");
            
            ResultSet rs4 = executeQuery(pstmt5);
            rs4.next();
            n_id = rs4.getInt(1);
            pstmt5.close();
            rs4.close();
            if (n_id != null) {
                PreparedStatement pstmt6 = prepareStatement(
                          "select nvl(l.linked_id, l.stay_id)"
                        + " from recent_stay_trans_list_fl10e l"
                        + " where l.id = ?");
                
                if (n_id != null) {
                    pstmt6.setInt(1, n_id);
                }
                else {
                    pstmt6.setNull(1, Types.JAVA_OBJECT);
                }
                ResultSet rs5 = executeQuery(pstmt6);
                rs5.next();
                n_link = rs5.getInt(1);
                pstmt6.close();
                rs5.close();
            }
            else {
                n_link = in_stay_id;
            }
            PreparedStatement pstmt7 = prepareStatement(
                      "select post_dtime"
                    + " from stay"
                    + " where stay_id = ?");
            
            if (in_stay_id != null) {
                pstmt7.setInt(1, in_stay_id);
            }
            else {
                pstmt7.setNull(1, Types.JAVA_OBJECT);
            }
            ResultSet rs6 = executeQuery(pstmt7);
            rs6.next();
            l_post_dtime = new DateTime(rs6.getTimestamp(1).getTime());
            pstmt7.close();
            rs6.close();
            PreparedStatement pstmt8 = prepareInsert(
                      "insert into recent_stay_trans_list_fl10e (stay_id, prop_cd, doa, dod, linked_id, ord, post_dtime)"
                    + " values (?, new get_prop_cd().execute(in_prop_id), ?, ?, ?, ?, ?)");
            if (in_stay_id != null) {
                pstmt8.setInt(1, in_stay_id);
            }
            else {
                pstmt8.setNull(1, Types.JAVA_OBJECT);
            }
            if (in_doa != null) {
                pstmt8.setObject(2, in_doa);
            }
            else {
                pstmt8.setNull(2, Types.JAVA_OBJECT);
            }
            if (f_dod != null) {
                pstmt8.setObject(3, f_dod);
            }
            else {
                pstmt8.setNull(3, Types.JAVA_OBJECT);
            }
            if (n_link != null) {
                pstmt8.setInt(4, n_link);
            }
            else {
                pstmt8.setNull(4, Types.JAVA_OBJECT);
            }
            if (l_ord != null) {
                pstmt8.setInt(5, l_ord);
            }
            else {
                pstmt8.setNull(5, Types.JAVA_OBJECT);
            }
            if (l_curr_post_dtime != null) {
                pstmt8.setObject(6, l_curr_post_dtime);
            }
            else {
                pstmt8.setNull(6, Types.JAVA_OBJECT);
            }
            executeUpdate(pstmt8);
            pstmt8.close();
            // we only want to award bonus for one room, so need to check if

            // any linked stays are the same doa

            PreparedStatement pstmt9 = prepareStatement(
                      "select stay_id, acct_trans_id, doa"
                    + " from recent_stay_trans_list_fl10e"
                    + " where linked_id = ?"
                    + " and stay_id != ?"
                    + " order by post_dtime desc");
            
            if (n_link != null) {
                pstmt9.setInt(1, n_link);
            }
            else {
                pstmt9.setNull(1, Types.JAVA_OBJECT);
            }
            if (in_stay_id != null) {
                pstmt9.setInt(2, in_stay_id);
            }
            else {
                pstmt9.setNull(2, Types.JAVA_OBJECT);
            }
            ResultSet rs7 = executeQuery(pstmt9);
            while (rs7.next()) {
                l_stay_id = rs7.getInt(1);
                l_acct_trans_id = rs7.getInt(2);
                l_doa = new DateTime(rs7.getTimestamp(3).getTime());
                if (l_doa.isEqual(in_doa)) {
                    // see if same doa stay has already been awarded the bonus

                    l_linked_amount = 0.0;
                    PreparedStatement pstmt10 = prepareStatement(
                              "select ad.amount"
                            + " from acct_trans a, acct_trans_detail ad"
                            + " where a.acct_trans_id = ad.acct_trans_id"
                            + " and a.acct_trans_id = ?"
                            + " and a.rev_acct_trans_id is null"
                            + " and ad.promo_id = ?");
                    
                    if (l_acct_trans_id != null) {
                        pstmt10.setInt(1, l_acct_trans_id);
                    }
                    else {
                        pstmt10.setNull(1, Types.JAVA_OBJECT);
                    }
                    if (in_promo_id != null) {
                        pstmt10.setInt(2, in_promo_id);
                    }
                    else {
                        pstmt10.setNull(2, Types.JAVA_OBJECT);
                    }
                    ResultSet rs8 = executeQuery(pstmt10);
                    rs8.next();
                    l_linked_amount = rs8.getDouble(1);
                    pstmt10.close();
                    rs8.close();
                    if (l_linked_amount > 0.0) {
                        l_already_awarded = 1;
                        break;
                    }
                }
            }
            pstmt9.close();
            rs7.close();
            PreparedStatement pstmt11 = prepareStatement("drop table recent_stay_trans_list_fl10e");
            executeUpdate(pstmt11);
            pstmt11.close();
            if (l_already_awarded.equals(1)) {
                return "F";
            }
            else {
                return "T";
            }
        }
        catch (SQLException e) {
            sql_error = e.getErrorCode();
            isam_error = 0;
            error_data = e.getMessage();
            {
                PreparedStatement pstmt12 = prepareStatement("drop table recent_stay_trans_list_fl10e");
                executeUpdate(pstmt12);
                pstmt12.close();
                throw new ProcedureException(sql_error, isam_error, error_data);
            }
        }
    }

}