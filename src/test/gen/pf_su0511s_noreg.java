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

public class pf_su0511s_noreg extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public String execute(Integer in_acct_id, Integer in_promo_id, String in_stay_type, Integer in_prop_id, DateTime in_doa, Integer in_los, String in_rm_type, String in_srp_code, Double in_rm_revenue, Double in_fb_revenue, Double in_other_revenue, String in_curr_code, String in_res_source, Integer in_stay_id) throws SQLException, ProcedureException {
        /*
         * pf_su0511s_noreg.sql - Summer 2011 promo filter for non-registered accounts for the stay twice promotion. This 
         *                        filter processes stays, determining if the stay at hand triggers the awarding of free
         *                        night bonus. This promo applies to all CP programs. This filter applies to 
         *                        all CP accounts. If this filter is executed, then all programs must be taken into 
         *                        account. Since they are not registered, number of bonuses is limited to 2 for GP/CN
         *                        accounts and 10 for AU/CE/MC accounts.
         *                      
         *                  
         *                  Stay eligibility criteria for this promo are:
         *    
         *                  1) booked via ch.com, central res
         *                  2) signup stay automatically qualifies (DOA = signup date AND was done at the hotel)
         *                  3) Platinum or Diamond or AU/CE/MC accounts can book through any channel
         *                  4) An econonmy stay must be at least 2 nights, consecutive stays meet criteria
         *                  5) A midscale stay must only be 1 night to qualify
         *                  If those criteria are met, then determine if this is the second qualifying stay before
         *                  awarding the bonus. Note: the midsacle and economy 2 night requirement is not checked in this procedure, 
         *                  rather it is deferred to pf_su0511s_awardbonus since that is where all the stays for the
         *                  promo period are retrieved and analyzed.
         *    
         * $Id: pf_su0511s_noreg.sql 39481 2011-06-02 00:13:17Z rshepher $
         *    
         *        Copyright (C) 2011 Choice Hotels International, Inc.
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
        Integer l_max_awards;
        DateTime l_offer_dtime;
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
            l_max_awards = 2;
            // assume non registered CP US/CN

            l_offer_dtime = null;
            // set up tracing based on app_config entry

            l_debug = (String) new settrace().execute("pf_su0511s_noreg");
            if (l_debug.equals("T")) {
                setDebugFile("/tmp/pf_su0511s_noreg_" + dbinfo("sessionid") + ".trace");
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
            // if they are registered before the DOA, we don't want to continue with this filter. Let the 

            // filter that handles registrations decide the fate of this stay. See CQ 52446.

            PreparedStatement pstmt3 = prepareStatement(
                      "select ao.offer_dtime"
                    + " from acct_offer ao, offer o"
                    + " where ao.offer_id = o.offer_id"
                    + " and ao.acct_id = ?"
                    + " and o.offer_cd = \"SU0511O\"");
            
            if (in_acct_id != null) {
                pstmt3.setInt(1, in_acct_id);
            }
            else {
                pstmt3.setNull(1, Types.JAVA_OBJECT);
            }
            ResultSet rs3 = executeQuery(pstmt3);
            rs3.next();
            l_offer_dtime = new DateTime(rs3.getTimestamp(1).getTime());
            pstmt3.close();
            rs3.close();
            if ((l_offer_dtime.isBefore(in_doa) || l_offer_dtime.isEqual(in_doa))) {
                return l_answer;
            }
            // check if centrally booked

            if (in_res_source != null && (in_res_source.equals("N") || in_res_source.equals("C") || in_res_source.equals("G") || in_res_source.equals("M"))) {
                l_eligible = "T";
            }
            // AU/CE/MC have no booking channel requirements, plus they get more bonuses

            if ((l_recog_cd.equals("AU") || l_recog_cd.equals("CE") || l_recog_cd.equals("MC"))) {
                l_eligible = "T";
                l_max_awards = 10;
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
            // None of the basic critera were met, so no need to continue. This stay will not trigger the bonus.

            if (l_eligible.equals("F")) {
                return l_answer;
            }
            // Stay is hot, see if it actually triggers a bonus; i.e. do all the heavy

            // lifting in regards to pairing up with another elgible stay that

            // has not been awarded the bonus.

            // Note: we don't check bonus count here and get out if they've used all of

            // their bonuses, because this stay may be consecutive or overlapping with an

            // existing stay that has been part of a bonus. We need to persist this

            // stay in the acct_trans_contrib table as a non-contributing stay since that

            // bonus has already been awarded. We do that next, when we find all of the linked

            // stays. 

            l_answer = (String) new pf_su0511s_awardbonus().execute(in_acct_id, in_promo_id, in_stay_type, in_prop_id, in_doa, in_los, in_rm_type, in_srp_code, in_rm_revenue, in_fb_revenue, in_other_revenue, in_curr_code, in_res_source, in_stay_id, l_platinum, l_diamond, l_max_awards);
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