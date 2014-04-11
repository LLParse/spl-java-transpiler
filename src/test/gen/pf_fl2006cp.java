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

public class pf_fl2006cp extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public String execute(Integer acct_id, Integer promo_id, String stay_type, Integer prop_id, DateTime doa, Integer los, String rm_type, String srp_code, Double rm_revenue, Double fb_revenue, Double other_revenue, String curr_code, String res_source, Integer stay_id) throws SQLException, ProcedureException {
        /*
         * pf_fl2006cp.sql -    Check if stay is eligible for the
         *                      'Stay 2 times and earn 1 free night'
         *                      Fall 2006 stay twice promotion. 
         *                      The stays in question are
         *                      restricted to DOA falling within the
         *                      promotion start and end dates. Also the
         *                      stays must have qualified for points.
         *    
         *           
         * $Id: pf_fl2006cp.sql 11530 2007-04-06 19:57:03Z fbloomfi $
         *    
         *        Copyright (C) 2006 Choice Hotels International, Inc.
         */
        Integer no_bonus_cnt;
        Integer has_bonus_cnt;
        Integer des_bonus_cnt;
        Integer stay_cnt;
        DateTime start_date;
        DateTime stop_date;
        String recog_cd;
        Integer check_promo_id;
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
        no_bonus_cnt = 0;
        has_bonus_cnt = 0;
        des_bonus_cnt = 0;
        stay_cnt = 0;
        start_date = null;
        stop_date = null;
        recog_cd = null;
        check_promo_id = null;
        l_stay_id = null;
        l_prop_cd = null;
        l_doa = null;
        l_dod = null;
        l_linked_id = null;
        l_trans_id = null;
        f_dod = doa.plusDays(los);
        n_id = null;
        n_link = null;
        offer_dtime = null;
        //set debug file to '/tmp/pf_fl2006cp.trace';

        //trace on;

        // Check for valid doa

        PreparedStatement pstmt1 = prepareStatement(
                  "select promo.start_date, promo.stop_date"
                + " from promo"
                + " where promo.promo_id = ?");
        
        if (promo_id != null) {
            pstmt1.setInt(1, promo_id);
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
        if (doa.isBefore(start_date) || doa.isAfter(stop_date)) {
            return "F";
        }
        // Get the program code of the account

        PreparedStatement pstmt2 = prepareStatement(
                  "select acct.recog_cd"
                + " from acct"
                + " where acct.acct_id = ?");
        
        if (acct_id != null) {
            pstmt2.setInt(1, acct_id);
        }
        else {
            pstmt2.setNull(1, Types.JAVA_OBJECT);
        }
        ResultSet rs2 = executeQuery(pstmt2);
        rs2.next();
        recog_cd = rs2.getString(1);
        pstmt2.close();
        rs2.close();
        // Get the promo ID for counting awarded bonuses

        // NOTE: This MUST be adjusted for each new filter!

        PreparedStatement pstmt3 = prepareStatement(
                  "select promo.promo_id"
                + " from promo"
                + " where promo.recog_cd = ?"
                + " and promo.promo_cd = \"FL2006CP\"");
        
        if (recog_cd != null) {
            pstmt3.setString(1, recog_cd);
        }
        else {
            pstmt3.setNull(1, Types.JAVA_OBJECT);
        }
        ResultSet rs3 = executeQuery(pstmt3);
        rs3.next();
        check_promo_id = rs3.getInt(1);
        pstmt3.close();
        rs3.close();
        // Get a list of stays to consider
Iterator<Object> it0 = new find_linked_stays_by_promo().execute(promo_id, start_date, stop_date, acct_id).iterator();
        while (it0.hasNext()) {
            l_stay_id = (Integer) it0.next();
            l_prop_cd = (String) it0.next();
            l_doa = (DateTime) it0.next();
            l_dod = (DateTime) it0.next();
            l_linked_id = (Integer) it0.next();
            l_trans_id = (Integer) it0.next();
            PreparedStatement pstmt4 = prepareInsert(
                      "insert into recent_stay_trans_list (stay_id, prop_cd, doa, dod, linked_id, acct_trans_id)"
                    + " values (?, ?, ?, ?, nvl(?, ?), ?)");
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
            executeUpdate(pstmt4);
            pstmt4.close();
        }
        // add current stay to list

        PreparedStatement pstmt5 = prepareStatement(
                  "select max(l.id)"
                + " from recent_stay_trans_list l"
                + " where l.prop_cd = new get_prop_cd().execute(prop_id)"
                + " and  ||  ||  || ");
        
        ResultSet rs4 = executeQuery(pstmt5);
        rs4.next();
        n_id = rs4.getInt(1);
        pstmt5.close();
        rs4.close();
        if (n_id != null) {
            PreparedStatement pstmt6 = prepareStatement(
                      "select nvl(l.linked_id, l.stay_id)"
                    + " from recent_stay_trans_list l"
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
            n_link = stay_id;
        }
        PreparedStatement pstmt7 = prepareInsert(
                  "insert into recent_stay_trans_list (stay_id, prop_cd, doa, dod, linked_id)"
                + " values (?, new get_prop_cd().execute(prop_id), ?, ?, ?)");
        if (stay_id != null) {
            pstmt7.setInt(1, stay_id);
        }
        else {
            pstmt7.setNull(1, Types.JAVA_OBJECT);
        }
        if (doa != null) {
            pstmt7.setObject(2, doa);
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
        executeUpdate(pstmt7);
        pstmt7.close();
        // Get a count of non-reversed stays with promotion

        PreparedStatement pstmt8 = prepareStatement(
                  "select count(*)"
                + " from recent_stay_trans_list l"
                + " where l.acct_trans_id in ("
                    + "select ad.acct_trans_id"
                    + " from acct_trans_detail ad"
                    + " where ad.acct_trans_id = l.acct_trans_id"
                    + " and ad.promo_id = ?"
                    + ")"
                    + " and l.doa >= ?"
                    + " and l.doa <= ?");
            
            if (check_promo_id != null) {
                pstmt8.setInt(1, check_promo_id);
            }
            else {
                pstmt8.setNull(1, Types.JAVA_OBJECT);
            }
            if (start_date != null) {
                pstmt8.setObject(2, start_date);
            }
            else {
                pstmt8.setNull(2, Types.JAVA_OBJECT);
            }
            if (stop_date != null) {
                pstmt8.setObject(3, stop_date);
            }
            else {
                pstmt8.setNull(3, Types.JAVA_OBJECT);
            }
            ResultSet rs6 = executeQuery(pstmt8);
            rs6.next();
            has_bonus_cnt = rs6.getInt(1);
            rs6.close();
            // Get a count of stays 

            PreparedStatement pstmt9 = prepareStatement(
                      "select count(distinct linked_id)"
                    + " from recent_stay_trans_list l"
                    + " where l.doa >= ?"
                    + " and l.doa <= ?");
            
            if (start_date != null) {
                pstmt9.setObject(1, start_date);
            }
            else {
                pstmt9.setNull(1, Types.JAVA_OBJECT);
            }
            if (stop_date != null) {
                pstmt9.setObject(2, stop_date);
            }
            else {
                pstmt9.setNull(2, Types.JAVA_OBJECT);
            }
            ResultSet rs7 = executeQuery(pstmt9);
            rs7.next();
            stay_cnt = rs7.getInt(1);
            pstmt9.close();
            rs7.close();
            // Drop the temp table 

            PreparedStatement pstmt10 = prepareStatement("drop table recent_stay_trans_list");
            executeUpdate(pstmt10);
            pstmt10.close();
            if (stay_cnt > 0) {
                des_bonus_cnt = stay_cnt - has_bonus_cnt - 1;
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