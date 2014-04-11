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

public class pf_sp0311s extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public String execute(Integer in_acct_id, Integer in_promo_id, String in_stay_type, Integer in_prop_id, DateTime in_doa, Integer in_los, String in_rm_type, String in_srp_code, Double in_rm_revenue, Double in_fb_revenue, Double in_other_revenue, String in_curr_code, String in_res_source, Integer in_stay_id) throws SQLException, ProcedureException {
        /*
         * pf_sp0311s.sql - Spring 2011 2 promo filter for the stay twice promotion. This filter processes stays for the
         *                  second spring 2011 promotion, determining if the stay at hand triggers the awarding of 
         *                  the $50 cash card bonus. This is a GP only promotion.
         *                  
         *                  Stay eligibility criteria for this promo are:
         *    
         *                  1) booked via ch.com, central res
         *                  2) signup stay automatically qualifies (DOA = signup date AND was done at the hotel)
         *                  3) Platinum or Diamond can book through any channel
         *                  4) An econonmy stay must be at least 2 nights, consecutive stays meet criteria
         *                  5) A midscale stay must only be 1 night to qualify
         *                  If those criteria are met, then determine if this is the second qualifying stay before
         *                  awarding the bonus. Note: the midsacle and economy 2 night requirement is not checked in this procedure, 
         *                  rather it is deferred to pf_sp0311s_awardbonus since that is where all the stays for the
         *                  promo period are retrieved and analyzed.
         *    
         * $Id: pf_sp0311s.sql 39481 2011-06-02 00:13:17Z rshepher $
         *    
         *        Copyright (C) 2011 Choice Hotels International, Inc.
         */
        /*
         * Procedure logic:
         * 
         * If the app_config table indicates debug this procedure, then turn on tracing
         * If the stay is not within the promotion start/stop dates, then the stay is not eligible
         * If the stay is booked via ch.com or central res,  then the stay is eligible
         * If the account is platinum or diamond on doa, then the stay is eligible
         * If the stay is an enrollment stay, then the stay is eligible
         * If the account is registered for the Spring 2 offer on or before the DOA and account is platinum or diamond, then
         *    the max bonuses allowed is 5
         * If the account is registered for the Spring 2 offer on or before the DOA and account is non-elite or gold, then
         *    the max bonuses allowed is 2
         * If the bonuses already received for this promo are equal or greater than the max bonuses allowed, then the stay is not eligible
         * If the account has another qualified/eligible stay within the promotion period, then the stay triggers the awarding of the bonus
         */
        DateTime l_start_date;
        DateTime l_stop_date;
        String l_promo_cd;
        String l_recog_cd;
        DateTime l_signup_date;
        String l_platinum;
        String l_diamond;
        String l_enrollstay;
        String l_eligible;
        String l_answer;
        String l_debug;
        Integer sql_error;
        Integer isam_error;
        String error_data;
        try {
            l_start_date = null;
            l_stop_date = null;
            l_promo_cd = null;
            l_recog_cd = null;
            l_signup_date = null;
            l_platinum = "F";
            l_diamond = "F";
            l_enrollstay = "F";
            l_eligible = "F";
            l_answer = "F";
            l_debug = "F";
            // set up tracing based on app_config entry

            l_debug = (String) new settrace().execute("pf_sp0311s");
            if (l_debug.equals("T")) {
                setDebugFile("/tmp/pf_sp0311s.trace");
                trace("on");
            }
            // Check for valid doa

            PreparedStatement pstmt1 = prepareStatement(
                      "select promo.start_date, promo.stop_date, promo.promo_cd"
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
            l_promo_cd = rs1.getString(3);
            pstmt1.close();
            rs1.close();
            if (in_doa.isBefore(l_start_date) || in_doa.isAfter(l_stop_date)) {
                return l_answer;
            }
            // get some account info

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
            // check if centrally booked

            if (in_res_source != null && (in_res_source.equals("N") || in_res_source.equals("C") || in_res_source.equals("M"))) {
                l_eligible = "T";
            }
            // check if platinum or diamond elite, needed for max bonus calculation

            l_platinum = (String) new pf_isplatinum_ondoa().execute(in_acct_id, in_doa);
            if (l_platinum.equals("F")) {
                l_diamond = (String) new pf_isdiamond_ondoa().execute(in_acct_id, in_doa);
            }
            if (l_platinum.equals("T") || l_diamond.equals("T")) {
                l_eligible = "T";
            }
            // Enrollment stay?

            if (l_eligible.equals("F")) {
                l_enrollstay = (String) new pf_isenrollstay_v2().execute(in_acct_id, in_doa, in_doa.plusDays(in_los));
                if (l_enrollstay.equals("T")) {
                    l_eligible = "T";
                }
            }
            // None of the basic critera were met, so no need to continue. This stay

            // will not trigger the bonus.

            if (l_eligible.equals("F")) {
                return l_answer;
            }
            // Stay is hot, see if it actually triggers a bonus; i.e. do all the heavy

            // lifting in regards to pairing up with another elgible stay that

            // has not been awarded the bonus.

            l_answer = (String) new pf_sp0311s_awardbonus().execute(in_acct_id, in_promo_id, in_stay_type, in_prop_id, in_doa, in_los, in_rm_type, in_srp_code, in_rm_revenue, in_fb_revenue, in_other_revenue, in_curr_code, in_res_source, in_stay_id, l_platinum, l_diamond);
            return l_answer;
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