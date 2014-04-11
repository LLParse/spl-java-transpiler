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

public class pf_wi09cp extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public String execute(Integer in_acct_id, Integer in_promo_id, String in_stay_type, Integer in_prop_id, DateTime in_doa, Integer in_los, String in_rm_type, String in_srp_code, Double in_rm_revenue, Double in_fb_revenue, Double in_other_revenue, String in_curr_code, String in_res_source, Integer in_stay_id) throws SQLException, ProcedureException {
        /*
         * pf_wi09cp.sql - Check if stay is eligible for the Winter 2009 promo. Customer must have registered
         *                 prior to DOA to be eligible. Stay must be at least the second stay within the
         *                 promotion period.
         *                  
         *                 For CP stays, the stay at hand counts.
         *                  
         *                 The stays in question are restricted to DOA falling within the promotion start 
         *                 and end dates. Also the stays must have qualified for points.
         *                 
         *                 This filter is validating whether the account is eligible for the promo since
         *                 we are using this filter for both midscale and economy properties. The stay will
         *                 be kicked out for property eligibility by the chk_prop_partic spl.
         *                         
         * $Id: pf_wi09cp.sql 23859 2009-11-12 14:08:06Z rshepher $
         *    
         *        Copyright (C) 2009 Choice Hotels International, Inc.
         */
        DateTime l_start_date;
        DateTime l_stop_date;
        String l_recog_cd;
        DateTime l_doa;
        String l_name;
        String l_res_source;
        DateTime l_signup_date;
        Integer l_qualifies;
        Integer l_registered;
        Integer l_stay_cnt;
        Integer l_offer_id;
        DateTime l_offer_dtime;
        Integer sql_error;
        Integer isam_error;
        String error_data;
        try {
            l_start_date = null;
            l_stop_date = null;
            l_recog_cd = null;
            l_doa = null;
            l_name = null;
            l_res_source = null;
            l_signup_date = null;
            l_qualifies = 0;
            l_registered = 0;
            l_stay_cnt = 0;
            l_offer_id = 0;
            l_offer_dtime = null;
            //set debug file to '/tmp/pf_wi09cp.trace';

            //trace on;

            // Get the program code and signup date of the account

            PreparedStatement pstmt1 = prepareStatement(
                      "select a.recog_cd, a.signup_date"
                    + " from acct a"
                    + " where a.acct_id = ?");
            
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
            // Get the promo dates

            PreparedStatement pstmt2 = prepareStatement(
                      "select p.start_date, p.stop_date"
                    + " from promo p"
                    + " where p.recog_cd = ?"
                    + " and p.promo_id = ?");
            
            if (l_recog_cd != null) {
                pstmt2.setString(1, l_recog_cd);
            }
            else {
                pstmt2.setNull(1, Types.JAVA_OBJECT);
            }
            if (in_promo_id != null) {
                pstmt2.setInt(2, in_promo_id);
            }
            else {
                pstmt2.setNull(2, Types.JAVA_OBJECT);
            }
            ResultSet rs2 = executeQuery(pstmt2);
            rs2.next();
            l_start_date = new DateTime(rs2.getTimestamp(1).getTime());
            l_stop_date = new DateTime(rs2.getTimestamp(2).getTime());
            pstmt2.close();
            rs2.close();
            if (l_start_date == null) {
                return "F";
            }
            if (in_doa.isBefore(l_start_date) || in_doa.isAfter(l_stop_date)) {
                return "F";
            }
            // get booking source, doa

            PreparedStatement pstmt3 = prepareStatement(
                      "select s.res_source, s.doa"
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
            l_res_source = rs3.getString(1);
            l_doa = new DateTime(rs3.getTimestamp(2).getTime());
            pstmt3.close();
            rs3.close();
            // Loop through possible offers for this promo and recog_cd and

            // see if this account is registered for one of them. Note: should only 

            // be registered for one.

            PreparedStatement pstmt4 = prepareStatement(
                      "select pae.offer_id"
                    + " from promo_acct_elig pae"
                    + " where pae.promo_id = ?"
                    + " and pae.recog_cd = ?");
            
            if (in_promo_id != null) {
                pstmt4.setInt(1, in_promo_id);
            }
            else {
                pstmt4.setNull(1, Types.JAVA_OBJECT);
            }
            if (l_recog_cd != null) {
                pstmt4.setString(2, l_recog_cd);
            }
            else {
                pstmt4.setNull(2, Types.JAVA_OBJECT);
            }
            ResultSet rs4 = executeQuery(pstmt4);
            while (rs4.next()) {
                l_offer_id = rs4.getInt(1);
                if (l_offer_id == null) {
                    break;
                }
                // get date of offer/registration if any

                PreparedStatement pstmt5 = prepareStatement(
                          "select date(ao.offer_dtime)"
                        + " from acct_offer ao"
                        + " where ao.acct_id = ?"
                        + " and ao.offer_id = ?");
                
                if (in_acct_id != null) {
                    pstmt5.setInt(1, in_acct_id);
                }
                else {
                    pstmt5.setNull(1, Types.JAVA_OBJECT);
                }
                if (l_offer_id != null) {
                    pstmt5.setInt(2, l_offer_id);
                }
                else {
                    pstmt5.setNull(2, Types.JAVA_OBJECT);
                }
                ResultSet rs5 = executeQuery(pstmt5);
                rs5.next();
                l_offer_dtime = new DateTime(rs5.getTimestamp(1).getTime());
                pstmt5.close();
                rs5.close();
                // check if they registered for this offer before the stay or if it was an enrollment stay

                if (l_offer_dtime == null) {
                    if (!l_signup_date.isEqual(in_doa)) {
                        l_registered = 0;
                    }
                    // not an signup stay

                    else {
                        l_registered = 1;
                        // signup stay so they were auto registered

                        break;
                    }
                }
                else if (l_offer_dtime.isAfter(in_doa)) {
                    l_registered = 0;
                }
                // registered after doa so deny for now

                else {
                    l_registered = 1;
                    // registered before doa

                    break;
                }
            }
            pstmt4.close();
            rs4.close();
            // if they did not register, deny any bonus

            if (l_registered.equals(0)) {
                return "F";
            }
            // get number of stays within the promo period

            PreparedStatement pstmt6 = prepareStatement(
                      "select count(*)"
                    + " from acct_trans a, stay s"
                    + " where a.acct_id = ?"
                    + " and a.stay_id = s.stay_id"
                    + " and s.stay_type in (\"N\", \"F\")"
                    + " and s.doa >= ?"
                    + " and s.doa <= ?"
                    + " and a.rev_acct_trans_id is null"
                    + " and new get_acct_trans_sum().execute(a.acct_trans_id) > 0");
            
            if (in_acct_id != null) {
                pstmt6.setInt(1, in_acct_id);
            }
            else {
                pstmt6.setNull(1, Types.JAVA_OBJECT);
            }
            if (l_start_date != null) {
                pstmt6.setObject(2, l_start_date);
            }
            else {
                pstmt6.setNull(2, Types.JAVA_OBJECT);
            }
            if (l_stop_date != null) {
                pstmt6.setObject(3, l_stop_date);
            }
            else {
                pstmt6.setNull(3, Types.JAVA_OBJECT);
            }
            ResultSet rs6 = executeQuery(pstmt6);
            rs6.next();
            l_stay_cnt = rs6.getInt(1);
            pstmt6.close();
            rs6.close();
            // get elite level at time of doa

            PreparedStatement pstmt7 = prepareStatement(
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
                    pstmt7.setInt(1, in_acct_id);
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
                if (l_doa != null) {
                    pstmt7.setObject(3, l_doa);
                }
                else {
                    pstmt7.setNull(3, Types.JAVA_OBJECT);
                }
                if (l_doa != null) {
                    pstmt7.setObject(4, l_doa);
                }
                else {
                    pstmt7.setNull(4, Types.JAVA_OBJECT);
                }
                ResultSet rs7 = executeQuery(pstmt7);
                rs7.next();
                l_name = rs7.getString(1);
                rs7.close();
                // So, we now know that account has registered, check elite level and reservation source

                // If  Platinum or Diamond, then mark as qualified

                // If not Platinum or Diamond and but centrally booked, then mark as qualified

                // If it doesn't pass these first two, then it does not qualify

                if (l_name != null && (l_name.equals("Platinum") || l_name.equals("Diamond"))) {
                    l_qualifies = 1;
                }
                else if (l_res_source != null && (l_res_source.equals("C") || l_res_source.equals("N"))) {
                    l_qualifies = 1;
                }
                else if () {
                    l_qualifies = 1;
                }
                else {
                    l_qualifies = 0;
                }
                // must be at least the second stay and have qualified, so previous 

                // stay count must just be gt zero

                if (l_stay_cnt > 0 && l_qualifies.equals(1)) {
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
                    throw new ProcedureException(sql_error, isam_error, error_data);
                }
            }
        }
    
    }