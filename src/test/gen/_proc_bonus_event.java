/* Generated on 03-01-2013 12:10:56 PM by SPLParser v0.9 */
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

public class _proc_bonus_event extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public Collection<Object> execute(Integer acct_id, Integer cust_id, Integer promo_id, String force_bonus, Integer filter_data) throws SQLException, ProcedureException {
        /*
         * (]$[) $RCSfile$:$Revision: 23167 $ | CDATE=$Date: 2009-09-02 10:12:52 -0700 (Wed, 02 Sep 2009) $ ~
         * 
         *   _proc_bonus_event - Calculate and apply a bonus to an account
         * 
         *   This procedure must be called inside a transation to ensure data integrity.
         * 
         *       Copyright (C) 2001 Choice Hotels International, Inc.
         *                       All Rights Reserved
         */
        DateTime post_time;
        Integer acct_trans_id;
        Double total_amount;
        Double promo_amount;
        Integer elite_level_id;
        Integer appl_group;
        Integer max_uses;
        Integer criteria;
        String comp_meth;
        Double value;
        String recog_cd;
        Double trans_unit_value;
        String pgm_curr_cd;
        Integer detail_id;
        // ID of trans_acct_detail_id 

        Integer detail_cntr;
        Integer apply;
        // promo function check flag 1 = applies, 0 = doesn't

        Integer t_promo_id;
        Double t_amount;
        String t_post_proc;
        // promotion child variables

        Integer pc_promo_id;
        String pc_promo_cd;
        Integer pc_criteria;
        Double pc_value;
        String pc_comp_meth;
        String pc_rule;
        Integer pc_parent_id;
        Integer pc_max_uses;
        String pc_post_proc;
        post_time = new DateTime();
        acct_trans_id = null;
        total_amount = 0.0;
        promo_amount = null;
        elite_level_id = null;
        appl_group = null;
        max_uses = null;
        criteria = null;
        comp_meth = null;
        value = null;
        recog_cd = null;
        trans_unit_value = null;
        pgm_curr_cd = null;
        detail_id = null;
        detail_cntr = 0;
        apply = null;
        t_promo_id = null;
        t_amount = null;
        t_post_proc = null;
        pc_promo_id = null;
        pc_promo_cd = null;
        pc_criteria = null;
        pc_comp_meth = null;
        pc_value = null;
        pc_rule = null;
        pc_parent_id = null;
        pc_max_uses = null;
        pc_post_proc = null;
        //  set debug file to '/tmp/_proc_bonus_event.trace';

        //  trace on;

        // Get member parameters for the promotion check

        PreparedStatement pstmt1 = prepareStatement(
                  "select a.recog_cd, a.appl_group_id, r.trans_unit_value, r.earning_curr_cd"
                + " from acct a, recog_pgm r"
                + " where a.acct_id = ?"
                + " and a.recog_cd = r.recog_cd");
        
        if (acct_id != null) {
            pstmt1.setInt(1, acct_id);
        }
        else {
            pstmt1.setNull(1, Types.JAVA_OBJECT);
        }
        ResultSet rs1 = executeQuery(pstmt1);
        rs1.next();
        recog_cd = rs1.getString(1);
        appl_group = rs1.getInt(2);
        trans_unit_value = rs1.getDouble(3);
        pgm_curr_cd = rs1.getString(4);
        pstmt1.close();
        rs1.close();
        PreparedStatement pstmt2 = prepareStatement(
                  "select e.elite_level_id"
                + " from acct a, acct_elite_level al, elite_level e"
                + " where a.acct_id = ?"
                + " and al.acct_id = ?"
                + " and al.elite_level_id = e.elite_level_id"
                + " and e.eff_year = extend(?,  to )");
        
        if (acct_id != null) {
            pstmt2.setInt(1, acct_id);
        }
        else {
            pstmt2.setNull(1, Types.JAVA_OBJECT);
        }
        if (acct_id != null) {
            pstmt2.setInt(2, acct_id);
        }
        else {
            pstmt2.setNull(2, Types.JAVA_OBJECT);
        }
        if (post_time != null) {
            pstmt2.setObject(3, post_time);
        }
        else {
            pstmt2.setNull(3, Types.JAVA_OBJECT);
        }
        ResultSet rs2 = executeQuery(pstmt2);
        rs2.next();
        elite_level_id = rs2.getInt(1);
        pstmt2.close();
        rs2.close();
        // Get promotion parameters for checks

        PreparedStatement pstmt3 = prepareStatement(
                  "select promo.max_uses, promo_value.criteria, promo_value.value, promo_value.comp_meth"
                + " from promo, promo_value"
                + " where promo.promo_id = ?"
                + " and promo_value.promo_id = ?"
                + " and promo.status = \"ACTIVE\""
                + " and "
                + " and ");
        
        if (promo_id != null) {
            pstmt3.setInt(1, promo_id);
        }
        else {
            pstmt3.setNull(1, Types.JAVA_OBJECT);
        }
        if (promo_id != null) {
            pstmt3.setInt(2, promo_id);
        }
        else {
            pstmt3.setNull(2, Types.JAVA_OBJECT);
        }
        ResultSet rs3 = executeQuery(pstmt3);
        rs3.next();
        max_uses = rs3.getInt(1);
        criteria = rs3.getInt(2);
        value = rs3.getDouble(3);
        comp_meth = rs3.getString(4);
        pstmt3.close();
        rs3.close();
        // If a promotion matching the promo_id and promotion date range

        // does not exist, bail.

        // A rowcount from the previous statement == 0 implies this condition.

        if (!dbinfo("sqlca.sqlerrd2").equals(1)) {
            return new ArrayList<Object>(Arrays.<Object>asList(0, 0.0));
        }
        // check account eligibility

        apply = new chk_acct_elig().execute(acct_id, null, null, null, null, null, null, null, null, promo_id, null, null, appl_group, elite_level_id, null, filter_data);
        // no stay_id, use filter_data instead

        if (!apply.equals(1)) {
            if (!force_bonus.equals("Y")) {
                return new ArrayList<Object>(Arrays.<Object>asList(0, 0.0));
            }
        }
        apply = new chk_promo_max().execute(acct_id, promo_id, max_uses);
        if (!apply.equals(1)) {
            if (!force_bonus.equals("Y")) {
                return new ArrayList<Object>(Arrays.<Object>asList(0, 0.0));
            }
        }
        //----------------------------------------------------------

        // This temp table will hold the results of award processing

        // so that it's contents are readily available to generate

        // acct_trans_detail records at the end of processing.

        //----------------------------------------------------------

        // Promotion allowed calculate points

        promo_amount = new calc_amount().execute(acct_id, cust_id, promo_id, criteria, value, comp_meth, 0.0, null, null, trans_unit_value, null);
        if (promo_amount > 0.0) {
            total_amount = total_amount + promo_amount;
            // Keep a running total.

            PreparedStatement pstmt4 = prepareInsert(
                      "insert into t_acct_detail (promo_id, amount)"
                    + " values (?, ?)");
            if (promo_id != null) {
                pstmt4.setInt(1, promo_id);
            }
            else {
                pstmt4.setNull(1, Types.JAVA_OBJECT);
            }
            if (promo_amount != null) {
                pstmt4.setDouble(2, promo_amount);
            }
            else {
                pstmt4.setNull(2, Types.JAVA_OBJECT);
            }
            executeUpdate(pstmt4);
            pstmt4.close();
            //----------------------------------------------------------

            // Process child promotions of this promotion.

            //----------------------------------------------------------

            PreparedStatement pstmt5 = prepareStatement(
                      "select promo.promo_id, promo_cd, promo.rule, promo.max_uses, promo_value.criteria, promo_value.value, promo_value.comp_meth, promo.parent_promo_id, promo.post_proc"
                    + " from promo, promo_value"
                    + " where ? >= promo.start_date"
                    + " and ? <= promo.stop_date"
                    + " and promo.recog_cd = ?"
                    + " and promo_value.promo_id = promo.promo_id"
                    + " and promo_value.curr_cd = ?"
                    + " and promo.parent_promo_id = ?"
                    + " and promo.parent_promo_id is not null"
                    + " and promo.status = \"ACTIVE\""
                    + " and ? >= promo_value.start_date"
                    + " and ? <= promo_value.stop_date");
            
            if (post_time != null) {
                pstmt5.setObject(1, post_time);
            }
            else {
                pstmt5.setNull(1, Types.JAVA_OBJECT);
            }
            if (post_time != null) {
                pstmt5.setObject(2, post_time);
            }
            else {
                pstmt5.setNull(2, Types.JAVA_OBJECT);
            }
            if (recog_cd != null) {
                pstmt5.setString(3, recog_cd);
            }
            else {
                pstmt5.setNull(3, Types.JAVA_OBJECT);
            }
            if (pgm_curr_cd != null) {
                pstmt5.setString(4, pgm_curr_cd);
            }
            else {
                pstmt5.setNull(4, Types.JAVA_OBJECT);
            }
            if (promo_id != null) {
                pstmt5.setInt(5, promo_id);
            }
            else {
                pstmt5.setNull(5, Types.JAVA_OBJECT);
            }
            if (post_time != null) {
                pstmt5.setObject(6, post_time);
            }
            else {
                pstmt5.setNull(6, Types.JAVA_OBJECT);
            }
            if (post_time != null) {
                pstmt5.setObject(7, post_time);
            }
            else {
                pstmt5.setNull(7, Types.JAVA_OBJECT);
            }
            ResultSet rs4 = executeQuery(pstmt5);
            while (rs4.next()) {
                pc_promo_id = rs4.getInt(1);
                pc_promo_cd = rs4.getString(2);
                pc_rule = rs4.getString(3);
                pc_max_uses = rs4.getInt(4);
                pc_criteria = rs4.getInt(5);
                pc_value = rs4.getDouble(6);
                pc_comp_meth = rs4.getString(7);
                pc_parent_id = rs4.getInt(8);
                pc_post_proc = rs4.getString(9);
                // Check if this member has exceeded the maximum uses of this promotion.

                apply = new chk_promo_max().execute(acct_id, pc_promo_id, pc_max_uses);
                if (!apply.equals(1)) {
                    // Used up promotion

                    if (!force_bonus.equals("Y")) {
                        continue;
                    }
                }
                // Check if any promo_acct_elig records match

                apply = new chk_acct_elig().execute(acct_id, null, null, null, null, null, null, null, null, pc_promo_id, null, null, appl_group, elite_level_id, null, filter_data);
                // no stay_id, use filter_data instead

                if (!apply.equals(1)) {
                    // The promotion does not apply continue to next

                    if (!force_bonus.equals("Y")) {
                        continue;
                    }
                }
                //  The promo is legit calc its' value

                promo_amount = new calc_amount().execute(acct_id, cust_id, pc_promo_id, pc_criteria, pc_value, pc_comp_meth, 0.0, null, null, trans_unit_value, null);
                if (promo_amount > 0.0) {
                    total_amount = total_amount + promo_amount;
                    // Keep a running total.

                    // update the temp table. The date and transaction ID are filled in

                    // at acct_bonus creation.

                    PreparedStatement pstmt6 = prepareInsert(
                              "insert into t_acct_detail"
                            + " values (?, ?, ?, ?, ?)");
                    if (pc_promo_id != null) {
                        pstmt6.setInt(1, pc_promo_id);
                    }
                    else {
                        pstmt6.setNull(1, Types.JAVA_OBJECT);
                    }
                    if (pc_rule != null) {
                        pstmt6.setString(2, pc_rule);
                    }
                    else {
                        pstmt6.setNull(2, Types.JAVA_OBJECT);
                    }
                    if (pc_parent_id != null) {
                        pstmt6.setInt(3, pc_parent_id);
                    }
                    else {
                        pstmt6.setNull(3, Types.JAVA_OBJECT);
                    }
                    if (pc_post_proc != null) {
                        pstmt6.setString(4, pc_post_proc);
                    }
                    else {
                        pstmt6.setNull(4, Types.JAVA_OBJECT);
                    }
                    if (promo_amount != null) {
                        pstmt6.setDouble(5, promo_amount);
                    }
                    else {
                        pstmt6.setNull(5, Types.JAVA_OBJECT);
                    }
                    executeUpdate(pstmt6);
                    pstmt6.close();
                }
            }
            pstmt5.close();
            rs4.close();
            // end child promo foreach

            acct_trans_id = new _post_trans().execute(acct_id, cust_id, "B", null, null, null, total_amount, null);
            // stay type doesn't apply

            //----------------------------------------------------------

            // We have at least one row. Process each row by writing an

            // acct_trans_detail record. Numbering them beginning at 1.

            //----------------------------------------------------------

            detail_cntr = 1;
            // start counting details at one.

            //----------------------------------------------------------

            // Generate 1 detail for each non-zero award.

            //----------------------------------------------------------

            PreparedStatement pstmt7 = prepareStatement(
                      "select t_acct_detail.promo_id, t_acct_detail.amount, t_acct_detail.post_proc"
                    + " from t_acct_detail"
                    + " where t_acct_detail.amount > 0.0");
            
            ResultSet rs5 = executeQuery(pstmt7);
            while (rs5.next()) {
                t_promo_id = rs5.getInt(1);
                t_amount = rs5.getDouble(2);
                t_post_proc = rs5.getString(3);
                detail_id = new _ins_acct_trans_de().execute(acct_trans_id, detail_cntr, t_amount, t_promo_id, "N");
                detail_cntr = detail_cntr + 1;
                // Check if this detail refers to a post processing routine

                if (t_post_proc != null) {
                    
                    try {
                        Class<?> clazz = Class.forName(t_post_proc);
                        Method method = clazz.getDeclaredMethod("execute", String.class, String.class);
                        method.invoke(acct_id, acct_trans_id);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    };
                }
            }
            pstmt7.close();
            rs5.close();
        }
        // Get rid of the temp table.

        PreparedStatement pstmt8 = prepareStatement("drop table t_acct_detail");
        executeUpdate(pstmt8);
        pstmt8.close();
        // Return success

        return new ArrayList<Object>(Arrays.<Object>asList(acct_trans_id, total_amount));
    }

}