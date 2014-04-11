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

public class _proc_acct_event extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public Integer execute(Integer acct_id, String event_type) throws SQLException, ProcedureException {
        /*
         * (]$[) $RCSfile$:$Revision: 23167 $ | CDATE=$Date: 2009-09-02 10:12:52 -0700 (Wed, 02 Sep 2009) $ ~
         * 
         *  Process an account event.
         * 
         *  This procedure is called to process account events that are written to
         *  the account event queue.  Currently the only event that is supported is the
         *  signup event.  New signups are written to this queue when the account
         *  transitions from the pending state to active.  If there are any outstanding
         *  promotions for the application group and the trigger is "SIGNUP", apply
         *  the bonus points to the account.
         * 
         *  We are assuming that 'BEGIN WORK' is called prior to this procedure
         *  and that 'ROLLBACK/COMMIT WORK' is called after returning depending
         *  on the result.
         * 
         *  Returns are: 0 if no action was taken, and > 0 otherwise.
         * 
         * 	Copyright (C) 2003 Choice Hotels International, Inc.
         *                       All Rights Reserved
         */
        // Scratch vars

        Integer validate_property;
        Integer apply;
        Double amount;
        Integer detail_id;
        // Application group table

        Integer a_appl_group_id;
        String a_recog_cd;
        String a_recog_id;
        DateTime a_signup_date;
        Integer a_proc_loc;
        // Customer account table

        Integer ca_cust_id;
        // promotion table

        Integer pr_promo_id;
        String pr_promo_cd;
        Integer pr_criteria;
        Double pr_value;
        String pr_comp_meth;
        String pr_rule;
        Integer pr_max_uses;
        // recog_pgm table

        Double trans_unit_value;
        // property table

        String p_chain_id;
        String p_country;
        String p_mkt_area;
        String p_prop_type;
        String p_ioc_region;
        String p_prop_class;
        // return values

        Integer acct_trans_id;
        //set debug file to '/tmp/_proc_acct_event.trace';

        //trace on;

        // Initialize everyone to null.

        apply = null;
        amount = null;
        detail_id = null;
        a_appl_group_id = null;
        a_recog_cd = null;
        a_recog_id = null;
        a_signup_date = null;
        a_proc_loc = null;
        ca_cust_id = null;
        pr_promo_id = null;
        pr_promo_cd = null;
        pr_criteria = null;
        pr_value = null;
        pr_comp_meth = null;
        pr_rule = null;
        pr_max_uses = null;
        trans_unit_value = null;
        p_chain_id = null;
        p_country = null;
        p_mkt_area = null;
        p_prop_type = null;
        p_ioc_region = null;
        p_prop_class = null;
        acct_trans_id = 0;
        // For now validating the application sign-up location is disabled. We're

        // not comfortable with the property value stored with the application as

        // it is not validated and could be just about anything!

        validate_property = 0;
        //Validate the input values

        //Get the application id, recog code and recog id from the acct table

        PreparedStatement pstmt1 = prepareStatement(
                  "select acct.appl_group_id, acct.recog_cd, acct.recog_id, acct.signup_date, acct.appl_proc_loc, recog_pgm.trans_unit_value"
                + " from acct, recog_pgm"
                + " where acct.acct_id = ?"
                + " and acct.recog_cd = recog_pgm.recog_cd");
        
        if (acct_id != null) {
            pstmt1.setInt(1, acct_id);
        }
        else {
            pstmt1.setNull(1, Types.JAVA_OBJECT);
        }
        ResultSet rs1 = executeQuery(pstmt1);
        rs1.next();
        a_appl_group_id = rs1.getInt(1);
        a_recog_cd = rs1.getString(2);
        a_recog_id = rs1.getString(3);
        a_signup_date = new DateTime(rs1.getTimestamp(4).getTime());
        a_proc_loc = rs1.getInt(5);
        trans_unit_value = rs1.getDouble(6);
        pstmt1.close();
        rs1.close();
        if (validate_property.equals(1)) {
            // Validate property existence based on prop_id.

            PreparedStatement pstmt2 = prepareStatement(
                      "select prop.chain_id, prop.country, prop.mkt_area, prop.prop_type, prop.ioc_region, prop.prop_class"
                    + " from prop"
                    + " where prop.prop_id = ?");
            
            if (a_proc_loc != null) {
                pstmt2.setInt(1, a_proc_loc);
            }
            else {
                pstmt2.setNull(1, Types.JAVA_OBJECT);
            }
            ResultSet rs2 = executeQuery(pstmt2);
            rs2.next();
            p_chain_id = rs2.getString(1);
            p_country = rs2.getString(2);
            p_mkt_area = rs2.getString(3);
            p_prop_type = rs2.getString(4);
            p_ioc_region = rs2.getString(5);
            p_prop_class = rs2.getString(6);
            pstmt2.close();
            rs2.close();
        }
        // Find the primary customer ID for this account

        ca_cust_id = new get_pri_cust_id().execute(acct_id);
        //----------------------------------------------------------

        //Process application enrollment events

        //----------------------------------------------------------

        if (event_type.equals("S")) {
            PreparedStatement pstmt3 = prepareStatement(
                      "select promo.promo_id, promo_cd, promo.rule, promo.max_uses, promo_value.criteria, promo_value.value, promo_value.comp_meth"
                    + " from promo, promo_acct_elig, promo_value"
                    + " where trigger = \"SIGNUP\""
                    + " and ? >= promo.start_date"
                    + " and ? <= promo.stop_date"
                    + " and promo.recog_cd = ?"
                    + " and promo_acct_elig.promo_id = promo.promo_id"
                    + " and promo_acct_elig.appl_group_id = ?"
                    + " and promo_value.promo_id = promo.promo_id"
                    + " and promo_value.curr_cd = \"USD\""
                    + " and promo.status = \"ACTIVE\""
                    + " and ? >= promo_value.start_date"
                    + " and ? <= promo_value.stop_date");
            
            if (a_signup_date != null) {
                pstmt3.setObject(1, a_signup_date);
            }
            else {
                pstmt3.setNull(1, Types.JAVA_OBJECT);
            }
            if (a_signup_date != null) {
                pstmt3.setObject(2, a_signup_date);
            }
            else {
                pstmt3.setNull(2, Types.JAVA_OBJECT);
            }
            if (a_recog_cd != null) {
                pstmt3.setString(3, a_recog_cd);
            }
            else {
                pstmt3.setNull(3, Types.JAVA_OBJECT);
            }
            if (a_appl_group_id != null) {
                pstmt3.setInt(4, a_appl_group_id);
            }
            else {
                pstmt3.setNull(4, Types.JAVA_OBJECT);
            }
            if (a_signup_date != null) {
                pstmt3.setObject(5, a_signup_date);
            }
            else {
                pstmt3.setNull(5, Types.JAVA_OBJECT);
            }
            if (a_signup_date != null) {
                pstmt3.setObject(6, a_signup_date);
            }
            else {
                pstmt3.setNull(6, Types.JAVA_OBJECT);
            }
            ResultSet rs3 = executeQuery(pstmt3);
            while (rs3.next()) {
                pr_promo_id = rs3.getInt(1);
                pr_promo_cd = rs3.getString(2);
                pr_rule = rs3.getString(3);
                pr_max_uses = rs3.getInt(4);
                pr_criteria = rs3.getInt(5);
                pr_value = rs3.getDouble(6);
                pr_comp_meth = rs3.getString(7);
                // Check to see if this member has exceeded the maximum uses of

                // this promotion.

                apply = new chk_promo_max().execute(acct_id, pr_promo_id, pr_max_uses);
                if (apply.equals(0)) {
                    // Used up promotion

                    continue;
                }
                if (validate_property.equals(1)) {
                    // Check any property_participation records

                    apply = new chk_prop_partic().execute(acct_id, null, null, null, null, null, null, null, null, pr_promo_id, a_recog_cd, null, a_appl_group_id, p_chain_id, p_mkt_area, p_country, p_prop_type, p_ioc_region, p_prop_class, null, null);
                    if (apply.equals(0)) {
                        // If apply is 0, the promo does not apply.

                        // Continue to next promo.

                        continue;
                    }
                }
                // The following code was copied from _proc_bonus_event.  Since the

                // signup bonus is applied when the application is completed, there

                // won't be any members in the acct_offer table.  Thus, we needed to

                // bypass much of the code in the existing procedure.  For now, we'll

                // replicate the code.

                // Promotion allowed calculate points

                amount = new calc_amount().execute(acct_id, ca_cust_id, pr_promo_id, pr_criteria, pr_value, pr_comp_meth, null, null, null, trans_unit_value, null);
                acct_trans_id = new _post_trans().execute(acct_id, ca_cust_id, "B", null, null, null, amount, null);
                // stay type doesn't apply

                detail_id = new _ins_acct_trans_de().execute(acct_trans_id, 1, amount, pr_promo_id, "N");
            }
            pstmt3.close();
            rs3.close();
        }
        // Return the result.  If acct_trans_id is still zero, there were no

        // events to be processed.  Otherwise, we'll return the record ID of

        // the bonus transaction.

        return acct_trans_id;
    }

}