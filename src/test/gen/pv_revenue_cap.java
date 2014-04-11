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

public class pv_revenue_cap extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public Double execute(Integer in_acct_id, Integer in_cust_id, Integer in_promo_id, Integer in_criteria, Double in_value, Double in_stay_rev, String in_chain_cd, Integer in_los, Integer in_stay_id) throws SQLException, ProcedureException {
        /*
         * $Id: pv_revenue_cap.sql 57052 2012-10-23 05:11:27Z rick_shepherd $
         * 
         *   Description: Promo value handler to calculate vanilla revenue type promotions. The promotion
         *                may or may not have a cap or max_amount associated with it.
         *                
         *                Points are calculated at 10 points per dollar for midscale and upscale properties, 
         *                5 points per dollar for economy and extended stay properties.
         *                
         *                The in_value parameter must specify the *X points to be awarded. That is, 2 implies
         *                double points, 3 implies triple points, 4 implies quadruple points.
         */
        Double l_current_bonus;
        String l_chain_group_cd;
        Integer l_base;
        Double l_total_promo_points;
        Integer l_max_amount;
        // variables for thrown exceptions

        Integer sql_error;
        Integer isam_error;
        String error_data;
        try {
            l_current_bonus = null;
            l_chain_group_cd = null;
            l_base = null;
            l_total_promo_points = null;
            l_max_amount = null;
            PreparedStatement pstmt1 = prepareStatement(
                      "select chain_group_cd"
                    + " from chain_group cg"
                    + "  inner join chain_group_detail cgd on cg.chain_group_id = cgd.chain_group_id"
                    + " where cgd.chain_id = ?");
            
            if (in_chain_cd != null) {
                pstmt1.setString(1, in_chain_cd);
            }
            else {
                pstmt1.setNull(1, Types.JAVA_OBJECT);
            }
            ResultSet rs1 = executeQuery(pstmt1);
            rs1.next();
            l_chain_group_cd = rs1.getString(1);
            pstmt1.close();
            rs1.close();
            if (!in_value.equals(2.0) && !in_value.equals(3.0) && !in_value.equals(4.0)) {
                throw new ProcedureException(-746, 0, "pf_revenue_cap: not a valid value parameter " + in_value);
            }
            // l_base is the points per dollar value for the different chain groups

            // i.e. midscale/upscale is 10 points per doller

            //      economy/extended stay is 5 points per dollar

            if (l_chain_group_cd.equals("MS") || l_chain_group_cd.equals("US")) {
                l_base = 10;
            }
            else if (l_chain_group_cd.equals("EC") || l_chain_group_cd.equals("ES")) {
                l_base = 5;
            }
            else {
                throw new ProcedureException(-746, 0, "pf_revenue_cap: could not map chain group code " + l_chain_group_cd);
            }
            // in_value will indicate if double(2), triple(3), or quadruple points(4)

            l_current_bonus = in_value - 1.0 * l_base.doubleValue() * trunc(in_stay_rev, 0) / 1.0;
            PreparedStatement pstmt2 = prepareStatement(
                      "select max_amount"
                    + " from promo"
                    + " where promo_id = ?");
            
            if (in_promo_id != null) {
                pstmt2.setInt(1, in_promo_id);
            }
            else {
                pstmt2.setNull(1, Types.JAVA_OBJECT);
            }
            ResultSet rs2 = executeQuery(pstmt2);
            rs2.next();
            l_max_amount = rs2.getInt(1);
            pstmt2.close();
            rs2.close();
            // if we have a max amount on the bonus, then we must not exceed it

            if (l_max_amount != null) {
                // find total points earned so far for the promotion

                PreparedStatement pstmt3 = prepareStatement(
                          "select sum(ad.amount)"
                        + " from acct_trans a"
                        + "  inner join acct_trans_detail ad on a.acct_trans_id = ad.acct_trans_id"
                        + " where a.acct_id = ?"
                        + " and ad.promo_id = ?"
                        + " and a.rev_acct_trans_id is null");
                
                if (in_acct_id != null) {
                    pstmt3.setInt(1, in_acct_id);
                }
                else {
                    pstmt3.setNull(1, Types.JAVA_OBJECT);
                }
                if (in_promo_id != null) {
                    pstmt3.setInt(2, in_promo_id);
                }
                else {
                    pstmt3.setNull(2, Types.JAVA_OBJECT);
                }
                ResultSet rs3 = executeQuery(pstmt3);
                rs3.next();
                l_total_promo_points = rs3.getDouble(1);
                pstmt3.close();
                rs3.close();
                // if already at the max, no more points for you!

                if (l_total_promo_points >= l_max_amount.doubleValue()) {
                    l_current_bonus = 0.0;
                }
                // if will go over the max, just award enough points to get them to the max

                else if (l_current_bonus + l_total_promo_points > l_max_amount) {
                    l_current_bonus = l_max_amount.doubleValue() - l_total_promo_points;
                }
            }
            return l_current_bonus;
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