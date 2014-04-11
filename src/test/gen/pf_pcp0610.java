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

public class pf_pcp0610 extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public String execute(Integer in_acct_id, Integer in_promo_id, String in_stay_type, Integer in_prop_id, DateTime in_doa, Integer in_los, String in_rm_type, String in_srp_code, Double in_rm_revenue, Double in_fb_revenue, Double in_other_revenue, String in_curr_code, String in_res_source, Integer in_stay_id) throws SQLException, ProcedureException {
        /*
         * pf_pcp0610.sql - Check for rate PCPPTS.
         *    
         * $Id: pf_pcp0610.sql 28865 2010-05-16 17:01:59Z rshepher $
         * 
         *       Copyright (C) 2010 Choice Hotels International, Inc.
         */
        String l_answer;
        DateTime l_start_date;
        DateTime l_stop_date;
        Integer l_stay_id;
        String l_prop_cd;
        DateTime l_doa;
        DateTime l_doa2;
        DateTime l_dod;
        Integer l_linked_id;
        Integer l_trans_id;
        DateTime f_dod;
        Integer n_id;
        Integer n_link;
        Integer l_acct_trans_id;
        Integer l_ord;
        DateTime l_post_dtime;
        DateTime l_curr_post_dtime;
        Integer l_already_awarded;
        Double l_linked_amount;
        Integer l_temp_created;
        Integer sql_error;
        Integer isam_error;
        String error_data;
        try {
            l_answer = "F";
            // assume it doesn't qualify

            l_start_date = null;
            l_stop_date = null;
            l_stay_id = null;
            l_linked_id = null;
            l_prop_cd = null;
            l_doa = null;
            l_doa2 = null;
            l_dod = null;
            l_trans_id = null;
            f_dod = in_doa.plusDays(in_los);
            n_id = null;
            n_link = null;
            l_acct_trans_id = 0;
            l_ord = 1;
            l_post_dtime = null;
            l_curr_post_dtime = null;
            l_already_awarded = 0;
            l_linked_amount = 0.0;
            l_temp_created = 0;
            //set debug file to '/tmp/pf_pcp0610.trace';

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
            l_start_date = new DateTime(rs1.getTimestamp(1).getTime());
            l_stop_date = new DateTime(rs1.getTimestamp(2).getTime());
            pstmt1.close();
            rs1.close();
            if (in_srp_code == null || !in_srp_code.equals("PCPPTS")) {
                return "F";
            }
            // it's the right rate, now check for linked stays and if the bonus

            // has already been awarded.

            // Temp table to hold stays to consider

            l_temp_created = 1;
            // Get a list of linked stays to consider
Iterator<Object> it0 = new find_linked_stays_by_promo().execute(in_promo_id, l_start_date, l_stop_date, in_acct_id).iterator();
            while (it0.hasNext()) {
                l_stay_id = (Integer) it0.next();
                l_prop_cd = (String) it0.next();
                l_doa = (DateTime) it0.next();
                l_dod = (DateTime) it0.next();
                l_linked_id = (Integer) it0.next();
                l_trans_id = (Integer) it0.next();
                PreparedStatement pstmt2 = prepareStatement(
                          "select post_dtime"
                        + " from stay"
                        + " where stay_id = ?");
                
                if (l_stay_id != null) {
                    pstmt2.setInt(1, l_stay_id);
                }
                else {
                    pstmt2.setNull(1, Types.JAVA_OBJECT);
                }
                ResultSet rs2 = executeQuery(pstmt2);
                rs2.next();
                l_post_dtime = new DateTime(rs2.getTimestamp(1).getTime());
                pstmt2.close();
                rs2.close();
                PreparedStatement pstmt3 = prepareInsert(
                          "insert into recent_stay_trans_list_pcppts (stay_id, prop_cd, doa, dod, linked_id, acct_trans_id, ord, post_dtime)"
                        + " values (?, ?, ?, ?, nvl(?, ?), ?, ?, ?)");
                if (l_stay_id != null) {
                    pstmt3.setInt(1, l_stay_id);
                }
                else {
                    pstmt3.setNull(1, Types.JAVA_OBJECT);
                }
                if (l_prop_cd != null) {
                    pstmt3.setString(2, l_prop_cd);
                }
                else {
                    pstmt3.setNull(2, Types.JAVA_OBJECT);
                }
                if (l_doa != null) {
                    pstmt3.setObject(3, l_doa);
                }
                else {
                    pstmt3.setNull(3, Types.JAVA_OBJECT);
                }
                if (l_dod != null) {
                    pstmt3.setObject(4, l_dod);
                }
                else {
                    pstmt3.setNull(4, Types.JAVA_OBJECT);
                }
                if (l_linked_id != null) {
                    pstmt3.setInt(5, l_linked_id);
                }
                else {
                    pstmt3.setNull(5, Types.JAVA_OBJECT);
                }
                if (l_stay_id != null) {
                    pstmt3.setInt(6, l_stay_id);
                }
                else {
                    pstmt3.setNull(6, Types.JAVA_OBJECT);
                }
                if (l_trans_id != null) {
                    pstmt3.setInt(7, l_trans_id);
                }
                else {
                    pstmt3.setNull(7, Types.JAVA_OBJECT);
                }
                if (l_ord != null) {
                    pstmt3.setInt(8, l_ord);
                }
                else {
                    pstmt3.setNull(8, Types.JAVA_OBJECT);
                }
                if (l_post_dtime != null) {
                    pstmt3.setObject(9, l_post_dtime);
                }
                else {
                    pstmt3.setNull(9, Types.JAVA_OBJECT);
                }
                executeUpdate(pstmt3);
                pstmt3.close();
                l_ord = l_ord + 1;
            }
            // add current stay to list

            PreparedStatement pstmt4 = prepareStatement(
                      "select max(l.id)"
                    + " from recent_stay_trans_list_pcppts l"
                    + " where l.prop_cd = new get_prop_cd().execute(in_prop_id)"
                    + " and  ||  ||  || ");
            
            ResultSet rs3 = executeQuery(pstmt4);
            rs3.next();
            n_id = rs3.getInt(1);
            pstmt4.close();
            rs3.close();
            if (n_id != null) {
                PreparedStatement pstmt5 = prepareStatement(
                          "select nvl(l.linked_id, l.stay_id)"
                        + " from recent_stay_trans_list_pcppts l"
                        + " where l.id = ?");
                
                if (n_id != null) {
                    pstmt5.setInt(1, n_id);
                }
                else {
                    pstmt5.setNull(1, Types.JAVA_OBJECT);
                }
                ResultSet rs4 = executeQuery(pstmt5);
                rs4.next();
                n_link = rs4.getInt(1);
                pstmt5.close();
                rs4.close();
            }
            else {
                n_link = in_stay_id;
            }
            PreparedStatement pstmt6 = prepareStatement(
                      "select post_dtime"
                    + " from stay"
                    + " where stay_id = ?");
            
            if (in_stay_id != null) {
                pstmt6.setInt(1, in_stay_id);
            }
            else {
                pstmt6.setNull(1, Types.JAVA_OBJECT);
            }
            ResultSet rs5 = executeQuery(pstmt6);
            rs5.next();
            l_post_dtime = new DateTime(rs5.getTimestamp(1).getTime());
            pstmt6.close();
            rs5.close();
            PreparedStatement pstmt7 = prepareInsert(
                      "insert into recent_stay_trans_list_pcppts (stay_id, prop_cd, doa, dod, linked_id, ord, post_dtime)"
                    + " values (?, new get_prop_cd().execute(in_prop_id), ?, ?, ?, ?, ?)");
            if (in_stay_id != null) {
                pstmt7.setInt(1, in_stay_id);
            }
            else {
                pstmt7.setNull(1, Types.JAVA_OBJECT);
            }
            if (in_doa != null) {
                pstmt7.setObject(2, in_doa);
            }
            else {
                pstmt7.setNull(2, Types.JAVA_OBJECT);
            }
            if (f_dod != null) {
                pstmt7.setObject(3, f_dod);
            }
            else {
                pstmt7.setNull(3, Types.JAVA_OBJECT);
            }
            if (n_link != null) {
                pstmt7.setInt(4, n_link);
            }
            else {
                pstmt7.setNull(4, Types.JAVA_OBJECT);
            }
            if (l_ord != null) {
                pstmt7.setInt(5, l_ord);
            }
            else {
                pstmt7.setNull(5, Types.JAVA_OBJECT);
            }
            if (l_curr_post_dtime != null) {
                pstmt7.setObject(6, l_curr_post_dtime);
            }
            else {
                pstmt7.setNull(6, Types.JAVA_OBJECT);
            }
            executeUpdate(pstmt7);
            pstmt7.close();
            // Examine stays. If current stay is not linked then it is eligible for the 

            // bonus. If it is linked, then need to see if the bonus has already been awarded.

            PreparedStatement pstmt8 = prepareStatement(
                      "select stay_id, acct_trans_id, doa"
                    + " from recent_stay_trans_list_pcppts"
                    + " where linked_id = ?"
                    + " and stay_id != ?");
            
            if (n_link != null) {
                pstmt8.setInt(1, n_link);
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
            ResultSet rs6 = executeQuery(pstmt8);
            while (rs6.next()) {
                l_stay_id = rs6.getInt(1);
                l_acct_trans_id = rs6.getInt(2);
                l_doa = new DateTime(rs6.getTimestamp(3).getTime());
                // see if linked stay has already been awarded the bonus

                l_linked_amount = 0.0;
                PreparedStatement pstmt9 = prepareStatement(
                          "select ad.amount"
                        + " from acct_trans a, acct_trans_detail ad"
                        + " where a.acct_trans_id = ad.acct_trans_id"
                        + " and a.acct_trans_id = ?"
                        + " and a.rev_acct_trans_id is null"
                        + " and ad.promo_id = ?");
                
                if (l_acct_trans_id != null) {
                    pstmt9.setInt(1, l_acct_trans_id);
                }
                else {
                    pstmt9.setNull(1, Types.JAVA_OBJECT);
                }
                if (in_promo_id != null) {
                    pstmt9.setInt(2, in_promo_id);
                }
                else {
                    pstmt9.setNull(2, Types.JAVA_OBJECT);
                }
                ResultSet rs7 = executeQuery(pstmt9);
                rs7.next();
                l_linked_amount = rs7.getDouble(1);
                pstmt9.close();
                rs7.close();
                if (l_linked_amount > 0.0) {
                    l_already_awarded = 1;
                    break;
                }
            }
            pstmt8.close();
            rs6.close();
            PreparedStatement pstmt10 = prepareStatement("drop table recent_stay_trans_list_pcppts");
            executeUpdate(pstmt10);
            pstmt10.close();
            if (l_already_awarded.equals(0)) {
                l_answer = "T";
            }
            return l_answer;
        }
        catch (SQLException e) {
            sql_error = e.getErrorCode();
            isam_error = 0;
            error_data = e.getMessage();
            {
                if (l_temp_created.equals(1)) {
                    PreparedStatement pstmt11 = prepareStatement("drop table recent_stay_trans_list_pcppts");
                    executeUpdate(pstmt11);
                    pstmt11.close();
                }
                throw new ProcedureException(sql_error, isam_error, error_data);
            }
        }
    }

}