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

public class _proc_stay_event extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public Collection<Object> execute(Integer acct_id, Integer stay_id, Integer cust_call_id, Double amount) throws SQLException, ProcedureException {
        /*
         * (]$[) $RCSfile$:$Revision: 37989 $ | CDATE=$Date: 2011-04-01 16:47:23 -0700 (Fri, 01 Apr 2011) $ ~
         * 
         *  Process a customer stay event for a given account. 
         * 
         *  If not a redemption stay, or a non-qualifying stay, Calculate the award 
         *  value of the given stay data. We can differentiate these types as follows:
         *  If the stay_type = 'R' it is a redemption stay. If amount is set to 'null',
         *  it is a non-qualifying stay. If not stay_type 'R' and amount = 0 it is a
         *  potentially qualiying stay. In otherwords we will attempt to get an award but
         *  the amount, if any, is dependant on the promotions available.
         * 
         *  Then generate the appropriate acct_trans and acct_trans_detail records.
         *  Note that all revenue is received in property currency only, customer revenue
         *  is converted prior to inserting into records. Only property revenue values
         *  are used in award calculations.
         * 
         *  Assumption is that 'BEGIN WORK' is called prior to this
         *  procedure and 'ROLLBACK/COMMIT WORK' after depending on result. Returns
         *  acct_tran_id and amount on success.
         * 
         * 
         *     Copyright (C) 2003 Choice Hotels International, Inc.
         *                       All Rights Reserved
         */
        // Scratch vars

        Integer apply;
        // promo function check flag 1 = applies, 0 = doesn't

        Double promo_amount;
        // storage for amount of one promotion

        Double stay_rev;
        // Sum of stay revenue that is award eligible

        Double total_amount;
        // totalizer for all amounts per folio

        Integer detail_id;
        // ID of trans_acct_detail_id 

        Integer detail_cntr;
        // individual detail record identifier 

        String reward_elig;
        // reward eligibility indicator

        // Data from new records/calculated values

        Integer acct_trans_id;
        Double elig_revenue_mc;
        // stay table

        Integer cust_id;
        DateTime doa;
        Integer los;
        Integer prop_id;
        String stay_type;
        String stay_status;
        String srp_code;
        String denial;
        String rm_type;
        Double elig_revenue_pc;
        Double rm_revenue_pc;
        Double fb_revenue_pc;
        Double other_revenue_pc;
        String earning_curr_cd;
        String res_source;
        Double earning_curr_rate;
        Double prop_curr_rate;
        // recog_pgm table

        Double trans_unit_value;
        // account tables

        Integer a_appl_group_id;
        Integer a_elite_level_id;
        String a_recog_cd;
        String a_recog_id;
        // property table

        String p_chain_id;
        String p_country;
        String p_mkt_area;
        String p_prop_type;
        String p_ioc_region;
        String p_prop_class;
        // promotion table

        Integer pr_promo_id;
        String pr_promo_cd;
        Integer pr_criteria;
        Double pr_value;
        String pr_comp_meth;
        String pr_rule;
        Integer pr_max_uses;
        String pr_use_fb_rev;
        String pr_use_other_rev;
        String pr_use_rm_rev;
        String pr_post_proc;
        // promotion child table

        Integer pc_promo_id;
        String pc_promo_cd;
        Integer pc_criteria;
        Double pc_value;
        String pc_comp_meth;
        String pc_rule;
        Integer pc_parent_id;
        Integer pc_max_uses;
        String pc_use_fb_rev;
        String pc_use_other_rev;
        String pc_use_rm_rev;
        String pc_post_proc;
        // values read from the temp table for insertion into acct_trans_detail

        Integer t_promo_id;
        Double t_amount;
        Integer t_acct_trans_id;
        Double t_max_exclusive_amt;
        Integer t_max_promo_id;
        Double test_ex_amt;
        Integer test_promo_id;
        Integer test_base_id;
        String t_post_proc;
        Integer l_promo_id;
        Integer l_ord;
        Integer l_base_ord;
        Integer l_stay_id;
        Integer l_contrib_stay_id;
        Integer l_contrib_act_id;
        String l_bonus_contrib;
        Integer l_acct_trans_id;
        String l_debug;
        Integer sql_error;
        Integer isam_error;
        String error_data;
        try {
            // Initialize all defined variables to null.

            apply = null;
            promo_amount = null;
            stay_rev = null;
            total_amount = null;
            detail_id = null;
            reward_elig = null;
            acct_trans_id = null;
            elig_revenue_mc = null;
            a_appl_group_id = null;
            a_elite_level_id = null;
            a_recog_cd = null;
            a_recog_id = null;
            p_chain_id = null;
            p_country = null;
            p_mkt_area = null;
            p_prop_type = null;
            p_ioc_region = null;
            p_prop_class = null;
            pr_promo_id = null;
            pr_promo_cd = null;
            pr_criteria = null;
            pr_comp_meth = null;
            pr_value = null;
            pr_rule = null;
            pr_max_uses = null;
            pr_use_fb_rev = null;
            pr_use_other_rev = null;
            pr_use_rm_rev = null;
            pr_post_proc = null;
            pc_promo_id = null;
            pc_promo_cd = null;
            pc_criteria = null;
            pc_comp_meth = null;
            pc_value = null;
            pc_rule = null;
            pc_parent_id = null;
            pc_max_uses = null;
            pc_use_fb_rev = null;
            pc_use_other_rev = null;
            pc_use_rm_rev = null;
            pc_post_proc = null;
            t_promo_id = null;
            t_max_promo_id = null;
            t_max_exclusive_amt = 0.0;
            test_ex_amt = 0.0;
            test_promo_id = null;
            test_base_id = null;
            t_amount = null;
            t_acct_trans_id = null;
            t_post_proc = null;
            cust_id = null;
            doa = null;
            los = null;
            prop_id = null;
            stay_type = null;
            stay_status = null;
            srp_code = null;
            denial = null;
            rm_type = null;
            elig_revenue_pc = null;
            rm_revenue_pc = null;
            fb_revenue_pc = null;
            other_revenue_pc = null;
            earning_curr_cd = null;
            res_source = null;
            earning_curr_rate = null;
            prop_curr_rate = null;
            trans_unit_value = null;
            l_promo_id = null;
            l_ord = null;
            l_base_ord = null;
            l_stay_id = null;
            l_contrib_stay_id = null;
            l_contrib_act_id = null;
            l_bonus_contrib = null;
            l_acct_trans_id = null;
            l_debug = "F";
            // set up tracing based on app_config entry

            l_debug = (String) new settrace().execute("_proc_stay_event");
            if (l_debug.equals("T")) {
                setDebugFile("/tmp/_proc_stay_event_" + dbinfo("sessionid") + ".trace");
                trace("on");
            }
            //----------------------------------------------------------

            // First validate all input variables

            //----------------------------------------------------------

            // Validate the stay info

            PreparedStatement pstmt1 = prepareStatement(
                      "select stay.cust_id, stay.doa, stay.los, stay.prop_id, stay.stay_type, stay.stay_status, stay.srp_code, stay.denial, stay.rm_type, stay.srp_code, stay.elig_revenue_pc, stay.rm_revenue_pc, stay.fb_revenue_pc, stay.other_revenue_pc, stay.earning_curr_cd, stay.res_source, stay.earning_curr_rate, stay.prop_curr_rate"
                    + " from stay"
                    + " where stay.stay_id = ?");
            
            if (stay_id != null) {
                pstmt1.setInt(1, stay_id);
            }
            else {
                pstmt1.setNull(1, Types.JAVA_OBJECT);
            }
            ResultSet rs1 = executeQuery(pstmt1);
            rs1.next();
            cust_id = rs1.getInt(1);
            doa = new DateTime(rs1.getTimestamp(2).getTime());
            los = rs1.getInt(3);
            prop_id = rs1.getInt(4);
            stay_type = rs1.getString(5);
            stay_status = rs1.getString(6);
            srp_code = rs1.getString(7);
            denial = rs1.getString(8);
            rm_type = rs1.getString(9);
            srp_code = rs1.getString(10);
            elig_revenue_pc = rs1.getDouble(11);
            rm_revenue_pc = rs1.getDouble(12);
            fb_revenue_pc = rs1.getDouble(13);
            other_revenue_pc = rs1.getDouble(14);
            earning_curr_cd = rs1.getString(15);
            res_source = rs1.getString(16);
            earning_curr_rate = rs1.getDouble(17);
            prop_curr_rate = rs1.getDouble(18);
            pstmt1.close();
            rs1.close();
            // Validate account id by Getting account bonus info

            PreparedStatement pstmt2 = prepareStatement(
                      "select a.elite_level_id"
                    + " from acct_elite_level a, elite_level l"
                    + " where a.acct_id = ?"
                    + " and a.elite_level_id = l.elite_level_id"
                    + " and l.eff_year = extend(?,  to )");
            
            if (acct_id != null) {
                pstmt2.setInt(1, acct_id);
            }
            else {
                pstmt2.setNull(1, Types.JAVA_OBJECT);
            }
            if (doa != null) {
                pstmt2.setObject(2, doa);
            }
            else {
                pstmt2.setNull(2, Types.JAVA_OBJECT);
            }
            ResultSet rs2 = executeQuery(pstmt2);
            rs2.next();
            a_elite_level_id = rs2.getInt(1);
            pstmt2.close();
            rs2.close();
            PreparedStatement pstmt3 = prepareStatement(
                      "select a.recog_cd, a.appl_group_id"
                    + " from acct a"
                    + " where a.acct_id = ?");
            
            if (acct_id != null) {
                pstmt3.setInt(1, acct_id);
            }
            else {
                pstmt3.setNull(1, Types.JAVA_OBJECT);
            }
            ResultSet rs3 = executeQuery(pstmt3);
            rs3.next();
            a_recog_cd = rs3.getString(1);
            a_appl_group_id = rs3.getInt(2);
            pstmt3.close();
            rs3.close();
            // Validate property existence based on prop_id.

            PreparedStatement pstmt4 = prepareStatement(
                      "select p.chain_id, p.country, p.mkt_area, p.prop_type, p.ioc_region, p.prop_class"
                    + " from prop p, stay s, country c"
                    + " where s.stay_id = ?"
                    + " and p.prop_id = s.prop_id"
                    + " and c.country_cd = p.country");
            
            if (stay_id != null) {
                pstmt4.setInt(1, stay_id);
            }
            else {
                pstmt4.setNull(1, Types.JAVA_OBJECT);
            }
            ResultSet rs4 = executeQuery(pstmt4);
            rs4.next();
            p_chain_id = rs4.getString(1);
            p_country = rs4.getString(2);
            p_mkt_area = rs4.getString(3);
            p_prop_type = rs4.getString(4);
            p_ioc_region = rs4.getString(5);
            p_prop_class = rs4.getString(6);
            pstmt4.close();
            rs4.close();
            // Determine the transaction unit value

            PreparedStatement pstmt5 = prepareStatement(
                      "select r.trans_unit_value"
                    + " from recog_pgm r"
                    + " where r.recog_cd = ?");
            
            if (a_recog_cd != null) {
                pstmt5.setString(1, a_recog_cd);
            }
            else {
                pstmt5.setNull(1, Types.JAVA_OBJECT);
            }
            ResultSet rs5 = executeQuery(pstmt5);
            rs5.next();
            trans_unit_value = rs5.getDouble(1);
            pstmt5.close();
            rs5.close();
            //----------------------------------------------------------

            // The data appears valid. If this is a redemption stay we

            // just add an acct_trans and stay record and leave.

            //----------------------------------------------------------

            if (stay_type.equals("R")) {
                acct_trans_id = new _post_trans().execute(acct_id, cust_id, "R", stay_id, null, null, amount, stay_type);
                detail_id = new _ins_acct_trans_de().execute(acct_trans_id, 1, amount, null, "N");
                return new ArrayList<Object>(Arrays.<Object>asList(acct_trans_id, amount));
            }
            //----------------------------------------------------------

            // Generate all missing revenue values, member equivelants

            // of room, food and other amounts. 

            //----------------------------------------------------------

            elig_revenue_mc = new convert_currency().execute(prop_curr_rate, earning_curr_rate, elig_revenue_pc);
            //----------------------------------------------------------

            //----------------------------------------------------------

            // Determine reward eligibility. If this stay is not eligible 

            // for rewards, just post a 0 value transaction.

            //----------------------------------------------------------

            reward_elig = new reward_elig_stay().execute(a_recog_cd, prop_id, stay_type, stay_status, srp_code, denial);
            // Check that there is at least $1,00 revenue to be eligible                                  

            if (elig_revenue_mc < 1.0) {
                reward_elig = "N";
            }
            if (reward_elig.equals("N")) {
                acct_trans_id = new _post_trans().execute(acct_id, cust_id, "S", stay_id, cust_call_id, null, 0.0, stay_type);
                detail_id = new _ins_acct_trans_de().execute(acct_trans_id, 1, 0.0, null, "N");
                return new ArrayList<Object>(Arrays.<Object>asList(acct_trans_id, 0));
            }
            //----------------------------------------------------------

            //----------------------------------------------------------

            // The stay is award eligible. Calculate award amount...

            //----------------------------------------------------------

            //----------------------------------------------------------

            // Do sanity check on dates

            //----------------------------------------------------------

            if (doa.isAfter(new LocalDate().toDateTimeAtStartOfDay()) || los.equals(0)) {
                throw new ProcedureException(-746, 0, "_proc_stay_event: Invalid stay date info.");
            }
            if (doa.plusDays(los) > new LocalDate().toDateTimeAtStartOfDay()) {
                throw new ProcedureException(-746, 0, "_proc_stay_event: Date of departure must not be in the future.");
            }
            //----------------------------------------------------------

            // This temp table will hold the results of award processing

            // so that it's contents are readily available to generate

            // acct_bonus records for the stay. This is essentially a

            // copy of the acct_bonus table.

            //----------------------------------------------------------

            // This temp table will hold promo, stay, and acct_trans

            // info so the acct_trans_contrib table can be populated

            // The promo_id is needed in order to get the 'ord' for 

            // the data being persisted.

            //----------------------------------------------------------

            // Next look at promotions one at a time that correspond

            // to the customers' stay. Initially just get those within the

            // date range and who match the recognition code. For each one

            // that passes, create a acct_trans_detail record and store running

            // total of amount earned.

            //----------------------------------------------------------

            total_amount = 0.0;
            stay_rev = 0.0;
            PreparedStatement pstmt6 = prepareStatement(
                      "select promo.promo_id, promo_cd, promo.rule, promo.max_uses, promo.use_fb_rev, promo.use_other_rev, promo.use_rm_rev, promo_value.criteria, promo_value.value, promo_value.comp_meth, promo.post_proc"
                    + " from promo, promo_value"
                    + " where trigger = \"STAY\""
                    + " and ? >= promo.start_date"
                    + " and ? <= promo.stop_date"
                    + " and promo.recog_cd = ?"
                    + " and promo_value.promo_id = promo.promo_id"
                    + " and promo_value.curr_cd = ?"
                    + " and promo.parent_promo_id is null"
                    + " and promo.status = \"ACTIVE\""
                    + " and ? >= promo_value.start_date"
                    + " and ? <= promo_value.stop_date");
            
            if (doa != null) {
                pstmt6.setObject(1, doa);
            }
            else {
                pstmt6.setNull(1, Types.JAVA_OBJECT);
            }
            if (doa != null) {
                pstmt6.setObject(2, doa);
            }
            else {
                pstmt6.setNull(2, Types.JAVA_OBJECT);
            }
            if (a_recog_cd != null) {
                pstmt6.setString(3, a_recog_cd);
            }
            else {
                pstmt6.setNull(3, Types.JAVA_OBJECT);
            }
            if (earning_curr_cd != null) {
                pstmt6.setString(4, earning_curr_cd);
            }
            else {
                pstmt6.setNull(4, Types.JAVA_OBJECT);
            }
            if (doa != null) {
                pstmt6.setObject(5, doa);
            }
            else {
                pstmt6.setNull(5, Types.JAVA_OBJECT);
            }
            if (doa != null) {
                pstmt6.setObject(6, doa);
            }
            else {
                pstmt6.setNull(6, Types.JAVA_OBJECT);
            }
            ResultSet rs6 = executeQuery(pstmt6);
            while (rs6.next()) {
                pr_promo_id = rs6.getInt(1);
                pr_promo_cd = rs6.getString(2);
                pr_rule = rs6.getString(3);
                pr_max_uses = rs6.getInt(4);
                pr_use_fb_rev = rs6.getString(5);
                pr_use_other_rev = rs6.getString(6);
                pr_use_rm_rev = rs6.getString(7);
                pr_criteria = rs6.getInt(8);
                pr_value = rs6.getDouble(9);
                pr_comp_meth = rs6.getString(10);
                pr_post_proc = rs6.getString(11);
                // Make sure promo_amount is reset for each promotion

                promo_amount = 0.0;
                // Check if this member has exceeded the maximum uses of this promotion.

                apply = new chk_promo_max().execute(acct_id, pr_promo_id, pr_max_uses);
                if (apply.equals(0)) {
                    // Used up promotion

                    continue;
                }
                // If the promotion is revenue based, compute the revenue for the stay

                if (!pr_criteria.equals(0)) {
                    stay_rev = elig_revenue_mc;
                    // If the stay revenue is zero,

                    // there is no need to proceed with this promotion

                    if (stay_rev.equals(0)) {
                        continue;
                    }
                }
                // Check if any promo_acct_elig records match

                apply = new chk_acct_elig().execute(acct_id, prop_id, doa, los, rm_type, srp_code, elig_revenue_pc, fb_revenue_pc, other_revenue_pc, pr_promo_id, a_recog_cd, earning_curr_cd, a_appl_group_id, a_elite_level_id, res_source, stay_id);
                if (apply.equals(0)) {
                    //  The promotion does not apply continue to next

                    continue;
                }
                // Check any property_participation records

                apply = new chk_prop_partic().execute(acct_id, prop_id, doa, los, rm_type, srp_code, elig_revenue_pc, fb_revenue_pc, other_revenue_pc, pr_promo_id, a_recog_cd, earning_curr_cd, a_appl_group_id, p_chain_id, p_mkt_area, p_country, p_prop_type, p_ioc_region, p_prop_class, stay_id, stay_type);
                if (apply.equals(0)) {
                    // If apply is 0, the promo does not apply continue to next

                    continue;
                }
                //  The promo is legit calc its' value

                promo_amount = new calc_amount().execute(acct_id, cust_id, pr_promo_id, pr_criteria, pr_value, pr_comp_meth, stay_rev, p_chain_id, los, trans_unit_value, stay_id);
                if (promo_amount > 0.0) {
                    total_amount = total_amount + promo_amount;
                    // Keep a running total.

                    // update the temp table. The date and transaction ID are filled in

                    // at acct_bonus creation.

                    // record the rule for the exclusive test. set the parent_promo_id to 0

                    // because this IS the parent.

                    PreparedStatement pstmt7 = prepareInsert(
                              "insert into t_acct_detail"
                            + " values (?, ?, 0, ?, ?)");
                    if (pr_promo_id != null) {
                        pstmt7.setInt(1, pr_promo_id);
                    }
                    else {
                        pstmt7.setNull(1, Types.JAVA_OBJECT);
                    }
                    if (pr_rule != null) {
                        pstmt7.setString(2, pr_rule);
                    }
                    else {
                        pstmt7.setNull(2, Types.JAVA_OBJECT);
                    }
                    if (pr_post_proc != null) {
                        pstmt7.setString(3, pr_post_proc);
                    }
                    else {
                        pstmt7.setNull(3, Types.JAVA_OBJECT);
                    }
                    if (promo_amount != null) {
                        pstmt7.setDouble(4, promo_amount);
                    }
                    else {
                        pstmt7.setNull(4, Types.JAVA_OBJECT);
                    }
                    executeUpdate(pstmt7);
                    pstmt7.close();
                    // check child promotions ONLY if current promotion got an award

                    // uses same logic as normal promo

                    if (promo_amount > 0.0) {
                        PreparedStatement pstmt8 = prepareStatement(
                                  "select promo.promo_id, promo_cd, promo.rule, promo.max_uses, promo.use_fb_rev, promo.use_other_rev, promo.use_rm_rev, promo_value.criteria, promo_value.value, promo_value.comp_meth, promo.parent_promo_id, promo.post_proc"
                                + " from promo, promo_value"
                                + " where trigger = \"STAY\""
                                + " and ?.plusDays(?) >= promo.start_date"
                                + " and ? <= promo.stop_date"
                                + " and promo.recog_cd = ?"
                                + " and promo_value.promo_id = promo.promo_id"
                                + " and promo_value.curr_cd = ?"
                                + " and promo.parent_promo_id = ?"
                                + " and promo.parent_promo_id is not null"
                                + " and promo.status = \"ACTIVE\""
                                + " and ? >= promo_value.start_date"
                                + " and ? <= promo_value.stop_date");
                        
                        if (doa != null) {
                            pstmt8.setObject(1, doa);
                        }
                        else {
                            pstmt8.setNull(1, Types.JAVA_OBJECT);
                        }
                        if (los != null) {
                            pstmt8.setInt(2, los);
                        }
                        else {
                            pstmt8.setNull(2, Types.JAVA_OBJECT);
                        }
                        if (doa != null) {
                            pstmt8.setObject(3, doa);
                        }
                        else {
                            pstmt8.setNull(3, Types.JAVA_OBJECT);
                        }
                        if (a_recog_cd != null) {
                            pstmt8.setString(4, a_recog_cd);
                        }
                        else {
                            pstmt8.setNull(4, Types.JAVA_OBJECT);
                        }
                        if (earning_curr_cd != null) {
                            pstmt8.setString(5, earning_curr_cd);
                        }
                        else {
                            pstmt8.setNull(5, Types.JAVA_OBJECT);
                        }
                        if (pr_promo_id != null) {
                            pstmt8.setInt(6, pr_promo_id);
                        }
                        else {
                            pstmt8.setNull(6, Types.JAVA_OBJECT);
                        }
                        if (doa != null) {
                            pstmt8.setObject(7, doa);
                        }
                        else {
                            pstmt8.setNull(7, Types.JAVA_OBJECT);
                        }
                        if (doa != null) {
                            pstmt8.setObject(8, doa);
                        }
                        else {
                            pstmt8.setNull(8, Types.JAVA_OBJECT);
                        }
                        ResultSet rs7 = executeQuery(pstmt8);
                        while (rs7.next()) {
                            pc_promo_id = rs7.getInt(1);
                            pc_promo_cd = rs7.getString(2);
                            pc_rule = rs7.getString(3);
                            pc_max_uses = rs7.getInt(4);
                            pc_use_fb_rev = rs7.getString(5);
                            pc_use_other_rev = rs7.getString(6);
                            pc_use_rm_rev = rs7.getString(7);
                            pc_criteria = rs7.getInt(8);
                            pc_value = rs7.getDouble(9);
                            pc_comp_meth = rs7.getString(10);
                            pc_parent_id = rs7.getInt(11);
                            pc_post_proc = rs7.getString(12);
                            // Check if this member has exceeded the maximum uses of this promotion.

                            apply = new chk_promo_max().execute(acct_id, pc_promo_id, pc_max_uses);
                            if (apply.equals(0)) {
                                // Used up promotion

                                continue;
                            }
                            // If the promotion is revenue based, compute the revenue for the stay

                            if (!pc_criteria.equals(0)) {
                                stay_rev = elig_revenue_mc;
                                // If the stay revenue is zero,

                                // there is no need to proceed with this promotion

                                if (stay_rev.equals(0)) {
                                    continue;
                                }
                            }
                            // Check if any promo_acct_elig records match

                            apply = new chk_acct_elig().execute(acct_id, prop_id, doa, los, rm_type, srp_code, elig_revenue_pc, fb_revenue_pc, other_revenue_pc, pc_promo_id, a_recog_cd, earning_curr_cd, a_appl_group_id, a_elite_level_id, res_source, stay_id);
                            if (apply.equals(0)) {
                                //  The promotion does not apply continue to next

                                continue;
                            }
                            // Check any property_participation records

                            apply = new chk_prop_partic().execute(acct_id, prop_id, doa, los, rm_type, srp_code, elig_revenue_pc, fb_revenue_pc, other_revenue_pc, pc_promo_id, a_recog_cd, earning_curr_cd, a_appl_group_id, p_chain_id, p_mkt_area, p_country, p_prop_type, p_ioc_region, p_prop_class, stay_id, stay_type);
                            if (apply.equals(0)) {
                                // If apply is 0, the promo does not apply continue to next

                                continue;
                            }
                            //  The promo is legit calc its' value

                            promo_amount = new calc_amount().execute(acct_id, cust_id, pc_promo_id, pc_criteria, pc_value, pc_comp_meth, stay_rev, p_chain_id, los, trans_unit_value, stay_id);
                            if (promo_amount > 0.0) {
                                total_amount = total_amount + promo_amount;
                                // Keep a running total.

                                // update the temp table. The date and transaction ID are filled in

                                // at acct_bonus creation.

                                PreparedStatement pstmt9 = prepareInsert(
                                          "insert into t_acct_detail"
                                        + " values (?, ?, ?, ?, ?)");
                                if (pc_promo_id != null) {
                                    pstmt9.setInt(1, pc_promo_id);
                                }
                                else {
                                    pstmt9.setNull(1, Types.JAVA_OBJECT);
                                }
                                if (pc_rule != null) {
                                    pstmt9.setString(2, pc_rule);
                                }
                                else {
                                    pstmt9.setNull(2, Types.JAVA_OBJECT);
                                }
                                if (pc_parent_id != null) {
                                    pstmt9.setInt(3, pc_parent_id);
                                }
                                else {
                                    pstmt9.setNull(3, Types.JAVA_OBJECT);
                                }
                                if (pc_post_proc != null) {
                                    pstmt9.setString(4, pc_post_proc);
                                }
                                else {
                                    pstmt9.setNull(4, Types.JAVA_OBJECT);
                                }
                                if (promo_amount != null) {
                                    pstmt9.setDouble(5, promo_amount);
                                }
                                else {
                                    pstmt9.setNull(5, Types.JAVA_OBJECT);
                                }
                                executeUpdate(pstmt9);
                                pstmt9.close();
                            }
                        }
                        pstmt8.close();
                        rs7.close();
                    }
                }
            }
            pstmt6.close();
            rs6.close();
            // End for each promotion

            //----------------------------------------------------------

            // Keep only the largest exclusive amount detail item. Adjust

            // the total_amount for each detail removed

            //----------------------------------------------------------

            // Look to see if base awarded, rule = 'A'

            PreparedStatement pstmt10 = prepareStatement(
                      "select t_acct_detail.promo_id"
                    + " from t_acct_detail"
                    + " where t_acct_detail.rule in (\"A\")");
            
            ResultSet rs8 = executeQuery(pstmt10);
            while (rs8.next()) {
                test_base_id = rs8.getInt(1);
            }
            pstmt10.close();
            rs8.close();
            // Find the largest valued exclusive promotion ONLY if a base was awarded

            if (test_base_id != null) {
                PreparedStatement pstmt11 = prepareStatement(
                          "select t_acct_detail.amount, t_acct_detail.promo_id"
                        + " from t_acct_detail"
                        + " where t_acct_detail.rule in (\"E\")");
                
                ResultSet rs9 = executeQuery(pstmt11);
                while (rs9.next()) {
                    test_ex_amt = rs9.getDouble(1);
                    test_promo_id = rs9.getInt(2);
                    if (test_ex_amt > t_max_exclusive_amt) {
                        t_max_exclusive_amt = test_ex_amt;
                        t_max_promo_id = test_promo_id;
                    }
                }
                pstmt11.close();
                rs9.close();
            }
            // Now decrement the total amount by all Exclusive detail

            // items that don't match

            // Find the largest valued exclusive promotion ONLY if a base was awarded

            if (test_base_id != null) {
                PreparedStatement pstmt12 = prepareStatement(
                          "select t_acct_detail.amount, t_acct_detail.promo_id"
                        + " from t_acct_detail"
                        + " where t_acct_detail.rule in (\"E\")");
                
                ResultSet rs10 = executeQuery(pstmt12);
                while (rs10.next()) {
                    test_ex_amt = rs10.getDouble(1);
                    test_promo_id = rs10.getInt(2);
                    if (t_max_promo_id != test_promo_id) {
                        // NOT the largest exclusive. 

                        total_amount = total_amount - test_ex_amt;
                    }
                }
                pstmt12.close();
                rs10.close();
            }
            //----------------------------------------------------------

            // Now check what happened, if total_amount is 0 then

            // the stay gets nothing. This is not an error.

            //----------------------------------------------------------

            //----------------------------------------------------------

            // generate the acct_trans and acct stay record.

            //----------------------------------------------------------

            acct_trans_id = new _post_trans().execute(acct_id, cust_id, "S", stay_id, cust_call_id, null, total_amount, stay_type);
            //----------------------------------------------------------

            // We have at least one row. Process each row by writing an

            // acct_trans_detail record. Numbering them beginning at 1.

            //----------------------------------------------------------

            detail_cntr = 1;
            // start counting details at one.

            //----------------------------------------------------------

            // Check the total_amount. If 0 then no details have been

            // determined. Generate a 0 value detail with no promotion

            //----------------------------------------------------------

            if (total_amount.equals(0.0) || test_base_id == null) {
                detail_id = new _ins_acct_trans_de().execute(acct_trans_id, detail_cntr, 0.0, null, "N");
                total_amount = 0.0;
            }
            //----------------------------------------------------------

            // Generate 1 detail for each non-zero award. To keep things

            // orderly we will generate by rule: A and S then E. This

            // gives us Base, elite, then seasonal item order.

            //----------------------------------------------------------

            if (test_base_id != null) {
                PreparedStatement pstmt13 = prepareStatement(
                          "select t_acct_detail.promo_id, t_acct_detail.amount, t_acct_detail.post_proc"
                        + " from t_acct_detail"
                        + " where t_acct_detail.amount > 0.0"
                        + " and t_acct_detail.rule in (\"A\", \"S\")");
                
                ResultSet rs11 = executeQuery(pstmt13);
                while (rs11.next()) {
                    t_promo_id = rs11.getInt(1);
                    t_amount = rs11.getDouble(2);
                    t_post_proc = rs11.getString(3);
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
                pstmt13.close();
                rs11.close();
            }
            // If t_max_exclusive_amt is greater than 0 we know a base was awarded

            // thus it is safe to give out the exclusive promotion.

            if (t_max_exclusive_amt > 0.0) {
                PreparedStatement pstmt14 = prepareStatement(
                          "select t_acct_detail.promo_id, t_acct_detail.amount, t_acct_detail.post_proc"
                        + " from t_acct_detail"
                        + " where t_acct_detail.amount = ?"
                        + " and t_acct_detail.promo_id = ?");
                
                if (t_max_exclusive_amt != null) {
                    pstmt14.setDouble(1, t_max_exclusive_amt);
                }
                else {
                    pstmt14.setNull(1, Types.JAVA_OBJECT);
                }
                if (t_max_promo_id != null) {
                    pstmt14.setInt(2, t_max_promo_id);
                }
                else {
                    pstmt14.setNull(2, Types.JAVA_OBJECT);
                }
                ResultSet rs12 = executeQuery(pstmt14);
                rs12.next();
                t_promo_id = rs12.getInt(1);
                t_amount = rs12.getDouble(2);
                t_post_proc = rs12.getString(3);
                pstmt14.close();
                rs12.close();
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
            // load the acct_trans_contrib table for any promos that populated the temp table. This may be for

            // a non-contributing stay, but the stay may be linked to an existing entry in the acct_trans_contrib

            // so we need to store it.

            PreparedStatement pstmt15 = prepareStatement(
                      "select t.acct_trans_id, t.promo_id, t.ord, t.contrib_stay_id, t.contrib_linked_stay_id, t.contrib_acct_trans_id, t.bonus_contrib"
                    + " from t_acct_trans_contrib t");
            
            ResultSet rs13 = executeQuery(pstmt15);
            while (rs13.next()) {
                l_acct_trans_id = rs13.getInt(1);
                l_promo_id = rs13.getInt(2);
                l_ord = rs13.getInt(3);
                l_stay_id = rs13.getInt(4);
                l_contrib_stay_id = rs13.getInt(5);
                l_contrib_act_id = rs13.getInt(6);
                l_bonus_contrib = rs13.getString(7);
                if (l_ord == null) {
                    // the ord used is from the bonus acct_trans_detail for the promo

                    PreparedStatement pstmt16 = prepareStatement(
                              "select ad.ord"
                            + " from acct_trans_detail ad"
                            + " where ad.acct_trans_id = ?"
                            + " and ad.promo_id = ?");
                    
                    if (acct_trans_id != null) {
                        pstmt16.setInt(1, acct_trans_id);
                    }
                    else {
                        pstmt16.setNull(1, Types.JAVA_OBJECT);
                    }
                    if (l_promo_id != null) {
                        pstmt16.setInt(2, l_promo_id);
                    }
                    else {
                        pstmt16.setNull(2, Types.JAVA_OBJECT);
                    }
                    ResultSet rs14 = executeQuery(pstmt16);
                    rs14.next();
                    l_ord = rs14.getInt(1);
                    pstmt16.close();
                    rs14.close();
                }
                // if still null, then evidently no base points awarded due to non-qualifying reason, thus no bonus points

                // so no ord for this promo; e.g. try property BR029

                if (l_ord == null) {
                    continue;
                }
                // acct_trans_id for acct_trans_contrib will always be the one for the stay being processed

                if (l_contrib_act_id == null) {
                    l_contrib_act_id = acct_trans_id;
                }
                if (l_acct_trans_id == null) {
                    l_acct_trans_id = acct_trans_id;
                }
                // for promotions prior to SP0311S, when bonus_contrib was not present, set to Y since that is

                // how prior promotions worked.

                if (l_bonus_contrib == null) {
                    l_bonus_contrib = "Y";
                }
                PreparedStatement pstmt17 = prepareInsert(
                          "insert into acct_trans_contrib (acct_trans_id, ord, contrib_stay_id, contrib_linked_stay_id, contrib_acct_trans_id, bonus_contrib)"
                        + " values (?, ?, ?, ?, ?, ?)");
                if (l_acct_trans_id != null) {
                    pstmt17.setInt(1, l_acct_trans_id);
                }
                else {
                    pstmt17.setNull(1, Types.JAVA_OBJECT);
                }
                if (l_ord != null) {
                    pstmt17.setInt(2, l_ord);
                }
                else {
                    pstmt17.setNull(2, Types.JAVA_OBJECT);
                }
                if (l_stay_id != null) {
                    pstmt17.setInt(3, l_stay_id);
                }
                else {
                    pstmt17.setNull(3, Types.JAVA_OBJECT);
                }
                if (l_contrib_stay_id != null) {
                    pstmt17.setInt(4, l_contrib_stay_id);
                }
                else {
                    pstmt17.setNull(4, Types.JAVA_OBJECT);
                }
                if (l_contrib_act_id != null) {
                    pstmt17.setInt(5, l_contrib_act_id);
                }
                else {
                    pstmt17.setNull(5, Types.JAVA_OBJECT);
                }
                if (l_bonus_contrib != null) {
                    pstmt17.setString(6, l_bonus_contrib);
                }
                else {
                    pstmt17.setNull(6, Types.JAVA_OBJECT);
                }
                executeUpdate(pstmt17);
                pstmt17.close();
            }
            pstmt15.close();
            rs13.close();
            // Get rid of the temp table.

            PreparedStatement pstmt18 = prepareStatement("drop table t_acct_detail");
            executeUpdate(pstmt18);
            pstmt18.close();
            PreparedStatement pstmt19 = prepareStatement("drop table t_acct_trans_contrib");
            executeUpdate(pstmt19);
            pstmt19.close();
            // Return success

            return new ArrayList<Object>(Arrays.<Object>asList(acct_trans_id, total_amount));
        }
        catch (SQLException e) {
            sql_error = e.getErrorCode();
            isam_error = 0;
            error_data = e.getMessage();
            {
                PreparedStatement pstmt20 = prepareStatement("drop table t_acct_detail");
                executeUpdate(pstmt20);
                pstmt20.close();
                PreparedStatement pstmt21 = prepareStatement("drop table t_acct_trans_contrib");
                executeUpdate(pstmt21);
                pstmt21.close();
                throw new ProcedureException(sql_error, isam_error, error_data);
            }
        }
    }

}