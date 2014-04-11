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

public class pf_vanilla_revenue_awardbonus extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public String execute(Integer in_acct_id, Integer in_promo_id, String in_stay_type, Integer in_prop_id, DateTime in_doa, Integer in_los, String in_rm_type, String in_srp_code, Double in_rm_revenue, Double in_fb_revenue, Double in_other_revenue, String in_curr_code, String in_res_source, Integer in_stay_id, Integer in_link) throws SQLException, ProcedureException {
        /*
         * pf_vanilla_revenue_awardbonus.sql - If this routine is called, then the stay being processed meets the
         *                                     basic vanilla revenue promotion criteria. Now, check to see that this stay is
         *                                     not consecutive with another stay that has already been awarded the bonus. 
         *                                     
         *                                     This procedure will only be called if the promotion was set up to check 
         *                                     consecutiveness of stays.
         *    
         * $Id: pf_vanilla_revenue_awardbonus.sql 57606 2012-10-30 22:12:08Z rick_shepherd $
         *    
         *        Copyright (C) 2012 Choice Hotels International, Inc.
         */
        DateTime l_start_date;
        DateTime l_stop_date;
        Integer l_stay_id;
        Integer l_acct_trans_id;
        String l_debug;
        Integer l_bonus_cnt;
        String l_already_has_bonus;
        Integer sql_error;
        Integer isam_error;
        String error_data;
        try {
            l_start_date = null;
            l_stop_date = null;
            l_stay_id = null;
            l_acct_trans_id = 0;
            l_debug = "F";
            l_bonus_cnt = 0;
            l_already_has_bonus = "F";
            PreparedStatement pstmt1 = prepareStatement(
                      "select start_date, stop_date"
                    + " from promo"
                    + " where promo_id = ?");
            
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
            // See if the stay at hand is part of a stay set that has already triggered

            // a promotion bonus. We do this by examining any linked stays for the stay at hand and

            // seeing if the linked stays have been awarded a bonus for the promo at hand.

            PreparedStatement pstmt2 = prepareStatement(
                      "select stay_id, acct_trans_id"
                    + " from recent_stay_list"
                    + " where linked_id = ?"
                    + " and stay_id != ?");
            
            if (in_link != null) {
                pstmt2.setInt(1, in_link);
            }
            else {
                pstmt2.setNull(1, Types.JAVA_OBJECT);
            }
            if (in_stay_id != null) {
                pstmt2.setInt(2, in_stay_id);
            }
            else {
                pstmt2.setNull(2, Types.JAVA_OBJECT);
            }
            ResultSet rs2 = executeQuery(pstmt2);
            while (rs2.next()) {
                l_stay_id = rs2.getInt(1);
                l_acct_trans_id = rs2.getInt(2);
                l_bonus_cnt = 0;
                PreparedStatement pstmt3 = prepareStatement(
                          "select count(*)"
                        + " from acct_trans a"
                        + "  inner join acct_trans_detail ad on a.acct_trans_id = ad.acct_trans_id"
                        + " where a.acct_trans_id = ?"
                        + " and a.rev_acct_trans_id is null"
                        + " and a.acct_id = ?"
                        + " and ad.promo_id = ?");
                
                if (l_acct_trans_id != null) {
                    pstmt3.setInt(1, l_acct_trans_id);
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
                if (in_promo_id != null) {
                    pstmt3.setInt(3, in_promo_id);
                }
                else {
                    pstmt3.setNull(3, Types.JAVA_OBJECT);
                }
                ResultSet rs3 = executeQuery(pstmt3);
                rs3.next();
                l_bonus_cnt = rs3.getInt(1);
                pstmt3.close();
                rs3.close();
                if (l_bonus_cnt > 0) {
                    l_already_has_bonus = "T";
                    break;
                }
            }
            pstmt2.close();
            rs2.close();
            if (l_already_has_bonus.equals("T")) {
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
                throw new ProcedureException(sql_error, isam_error, error_data);
            }
        }
    }

}