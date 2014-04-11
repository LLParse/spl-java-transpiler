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

public class pf_fl0809b extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public String execute(Integer in_acct_id, Integer in_promo_id, String in_stay_type, Integer in_prop_id, DateTime in_doa, Integer in_los, String in_rm_type, String in_srp_code, Double in_rm_revenue, Double in_fb_revenue, Double in_other_revenue, String in_curr_code, String in_res_source, Integer in_stay_id) throws SQLException, ProcedureException {
        /*
         * pf_fl0809b.sql - Check if stay is eligible for the '500 pts for completed stay' promo.
         *                  The stays in question are restricted to DOA falling within the promotion start 
         *                  and end dates. Also the stays must have qualified for points.
         *                      
         *                  Stays must be booked through ch.com or central res unless elite status
         *                  is Platinum or Diamond. An enrollment stay is also eligible regardless of
         *                  booking source.      
         *                  
         *                  Consecutive and concurrent stays are examined and only one bonus is
         *                  awarded for those situations.
         *    
         * $Id: pf_fl0809b.sql 23448 2009-09-29 23:18:35Z kdamron $
         *    
         *        Copyright (C) 2009 Choice Hotels International, Inc.
         */
        DateTime start_date;
        DateTime stop_date;
        String l_recog_cd;
        Integer fl0809b_promo_id;
        Integer l_stay_id;
        String l_prop_cd;
        DateTime l_doa;
        DateTime l_dod;
        Integer l_linked_id;
        Integer l_trans_id;
        DateTime f_dod;
        Integer n_id;
        Integer n_link;
        String l_name;
        DateTime l_signup_date;
        Integer l_qualifies;
        Integer l_ord;
        Integer l_linked_trans_id;
        Integer l_bonus_cnt;
        Integer sql_error;
        Integer isam_error;
        String error_data;
        try {
            start_date = null;
            stop_date = null;
            l_recog_cd = null;
            fl0809b_promo_id = null;
            l_stay_id = null;
            l_prop_cd = null;
            l_doa = null;
            l_dod = null;
            l_linked_id = null;
            l_trans_id = null;
            f_dod = null;
            n_id = null;
            n_link = null;
            l_name = null;
            l_signup_date = null;
            l_qualifies = 0;
            l_ord = 0;
            l_linked_trans_id = null;
            l_bonus_cnt = 0;
            //set debug file to '/tmp/pf_fl0809b.trace';

            //trace on;

            // Get the program code of the account

            PreparedStatement pstmt1 = prepareStatement(
                      "select acct.recog_cd, acct.signup_date"
                    + " from acct"
                    + " where acct.acct_id = ?");
            
            if (in_acct_id != null) {
                pstmt1.setInt(1, in_acct_id);
            }
            else {
                pstmt1.setNull(1, Types.JAVA_OBJECT);
            }
            ResultSet rs1 = executeQuery(pstmt1);
            rs1.next();
            l_recog_cd = rs1.getString(1);
            l_signup_date = new DateTime(rs1.getTimestamp(2).getTime());
            pstmt1.close();
            rs1.close();
            // Get the promo ID for counting awarded bonuses

            PreparedStatement pstmt2 = prepareStatement(
                      "select promo.promo_id, promo.start_date, promo.stop_date"
                    + " from promo"
                    + " where promo.recog_cd = ?"
                    + " and promo.promo_cd = \"FL0809B\"");
            
            if (l_recog_cd != null) {
                pstmt2.setString(1, l_recog_cd);
            }
            else {
                pstmt2.setNull(1, Types.JAVA_OBJECT);
            }
            ResultSet rs2 = executeQuery(pstmt2);
            rs2.next();
            fl0809b_promo_id = rs2.getInt(1);
            start_date = new DateTime(rs2.getTimestamp(2).getTime());
            stop_date = new DateTime(rs2.getTimestamp(3).getTime());
            pstmt2.close();
            rs2.close();
            if (in_doa.isBefore(start_date) || in_doa.isAfter(stop_date)) {
                return "F";
            }
            // Now, determine if this stay is qualfied for the bonus.

            // If not, then get out. If it is, then need to check if

            // there are any concurrent stays that may have already been

            // awarded the bonus.

            // if hotel enrollment stay, then it qualifies

            if () {
                l_qualifies = 1;
            }
            // if ch.com or 800-4CHOCIE booking source, then it qualifies

            if (in_res_source != null && (in_res_source.equals("C") || in_res_source.equals("N"))) {
                l_qualifies = 1;
            }
            // if Platinum or Diamond, it qualifies regardless of booking source

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
                // if not central res or internet booked and not at least

                // Platinum, exclude it

                if (l_name != null && (l_name.equals("Platinum") || l_name.equals("Diamond"))) {
                    l_qualifies = 1;
                }
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
                    PreparedStatement pstmt4 = prepareInsert(
                              "insert into recent_stay_trans_list_fl0809b (stay_id, prop_cd, doa, dod, linked_id, acct_trans_id, ord)"
                            + " values (?, ?, ?, ?, nvl(?, ?), ?, ?)");
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
                    executeUpdate(pstmt4);
                    pstmt4.close();
                    l_ord = l_ord + 1;
                }
                // see if current stay is linked to any others

                PreparedStatement pstmt5 = prepareStatement(
                          "select max(l.id)"
                        + " from recent_stay_trans_list_fl0809b l"
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
                            + " from recent_stay_trans_list_fl0809b l"
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
                // if this stay not linked then see if it qualified

                if (n_link.equals(in_stay_id)) {
                    PreparedStatement pstmt7 = prepareStatement("drop table recent_stay_trans_list_fl0809b");
                    executeUpdate(pstmt7);
                    pstmt7.close();
                    if (l_qualifies.equals(1)) {
                        return "T";
                    }
                    else {
                        return "F";
                    }
                }
                // There are linked stays. Need to determine if this is a concurrent stay and

                // if the bonus has already been awarded.

                PreparedStatement pstmt8 = prepareStatement(
                          "select acct_trans_id"
                        + " from recent_stay_trans_list_fl0809b"
                        + " where linked_id = ?");
                
                if (n_link != null) {
                    pstmt8.setInt(1, n_link);
                }
                else {
                    pstmt8.setNull(1, Types.JAVA_OBJECT);
                }
                ResultSet rs6 = executeQuery(pstmt8);
                while (rs6.next()) {
                    l_linked_trans_id = rs6.getInt(1);
                    // see if bonus already awarded

                    PreparedStatement pstmt9 = prepareStatement(
                              "select count(*)"
                            + " from acct_trans_detail d, promo p"
                            + " where d.acct_trans_id = ?"
                            + " and d.promo_id = ?"
                            + " and d.promo_id = p.promo_id"
                            + " and p.rule = \"S\"");
                    
                    if (l_linked_trans_id != null) {
                        pstmt9.setInt(1, l_linked_trans_id);
                    }
                    else {
                        pstmt9.setNull(1, Types.JAVA_OBJECT);
                    }
                    if (fl0809b_promo_id != null) {
                        pstmt9.setInt(2, fl0809b_promo_id);
                    }
                    else {
                        pstmt9.setNull(2, Types.JAVA_OBJECT);
                    }
                    ResultSet rs7 = executeQuery(pstmt9);
                    rs7.next();
                    l_bonus_cnt = rs7.getInt(1);
                    pstmt9.close();
                    rs7.close();
                    // already awarded, can't give them another one

                    if (l_bonus_cnt > 0) {
                        l_qualifies = 0;
                        break;
                    }
                }
                pstmt8.close();
                rs6.close();
                PreparedStatement pstmt10 = prepareStatement("drop table recent_stay_trans_list_fl0809b");
                executeUpdate(pstmt10);
                pstmt10.close();
                if (l_qualifies.equals(1)) {
                    return "T";
                }
                else {
                    return "F";
                }
            }
            catch (SQLException e) {
                sql_error = e.getErrorCode();
                isam_error = 0;
                error_data = e.getMessage();
                {
                    PreparedStatement pstmt11 = prepareStatement("drop table recent_stay_trans_list_fl0809b");
                    executeUpdate(pstmt11);
                    pstmt11.close();
                    throw new ProcedureException(sql_error, isam_error, error_data);
                }
            }
        }
    
    }