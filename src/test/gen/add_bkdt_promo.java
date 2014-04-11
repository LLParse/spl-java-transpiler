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

public class add_bkdt_promo extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public Integer execute(String promo_cd, String promo_name, String promo_desc, String trigger, DateTime start_date, DateTime stop_date, String rule, Integer max_uses, String use_rm_rev, String use_fb_rev, String use_other_rev, Integer campaign_id, Integer billing_cat_id, String recog_cd, String user_name) throws SQLException, ProcedureException {
        /*
         * (]$[) $RCSfile$:$Revision: 8487 $ | CDATE=$Date: 2005-11-08 09:40:41 -0700 (Tue, 08 Nov 2005) $ ~
         * 
         *  Add a backdated promotion to the system.
         * 
         * 	Copyright (C) 2003 Choice Hotels International, Inc.
         *                       All Rights Reserved
         */
        Integer promo_id;
        Integer user_id;
        promo_id = null;
        user_id = null;
        // set debug file to '/tmp/add_promo.trace';

        // trace on;

        promo_cd = trim(promo_cd);
        if (length(promo_cd) < 4) {
            throw new ProcedureException(-746, 0, "add_promo: a meaningful promo_cd must be specified");
        }
        promo_name = trim(promo_name);
        if (length(promo_name) < 10) {
            throw new ProcedureException(-746, 0, "add_promo: a meaningful promo_name must be specified");
        }
        promo_desc = trim(promo_desc);
        if (length(promo_desc) < 10) {
            throw new ProcedureException(-746, 0, "add_promo: a meaningful promo_desc must be specified");
        }
        if (!trigger.equals("STAY") && !trigger.equals("NONE")) {
            throw new ProcedureException(-746, 0, "add_promo: invalid promotion trigger");
        }
        if (stop_date.isBefore(start_date)) {
            throw new ProcedureException(-746, 0, "add_promo: stop_date cannot be before the start_date");
        }
        if (!rule.equals("A") && !rule.equals("S") && !rule.equals("E")) {
            throw new ProcedureException(-746, 0, "add_promo: invalid promtion rule");
        }
        if (max_uses != null) {
            if (max_uses < 1) {
                throw new ProcedureException(-746, 0, "add_promo: max_uses must be greater than zero");
            }
        }
        if (!use_rm_rev.equals("Y") && !use_rm_rev.equals("N")) {
            throw new ProcedureException(-746, 0, "add_promo: use_rm_rev must be either 'Y' or 'N'");
        }
        if (!use_fb_rev.equals("Y") && !use_fb_rev.equals("N")) {
            throw new ProcedureException(-746, 0, "add_promo: use_fb_rev must be either 'Y' or 'N'");
        }
        if (!use_other_rev.equals("Y") && !use_other_rev.equals("N")) {
            throw new ProcedureException(-746, 0, "add_promo: use_other_rev must be either 'Y' or 'N'");
        }
        user_id = new get_user_id().execute(user_name);
        if (user_id == null) {
            throw new ProcedureException(-746, 0, "add_promo: user_name is not known");
        }
        PreparedStatement pstmt1 = prepareInsert(
                  "insert into promo (promo_id, promo_cd, name, desc, trigger, start_date, stop_date, rule, max_uses, use_rm_rev, use_fb_rev, use_other_rev, campaign_id, billing_cat_id, recog_cd, entry_id, last_update_dtime, last_update_id)"
                + " values (0, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, current, ?)");
        if (promo_cd != null) {
            pstmt1.setString(1, promo_cd);
        }
        else {
            pstmt1.setNull(1, Types.JAVA_OBJECT);
        }
        if (promo_name != null) {
            pstmt1.setString(2, promo_name);
        }
        else {
            pstmt1.setNull(2, Types.JAVA_OBJECT);
        }
        if (promo_desc != null) {
            pstmt1.setString(3, promo_desc);
        }
        else {
            pstmt1.setNull(3, Types.JAVA_OBJECT);
        }
        if (trigger != null) {
            pstmt1.setString(4, trigger);
        }
        else {
            pstmt1.setNull(4, Types.JAVA_OBJECT);
        }
        if (start_date != null) {
            pstmt1.setObject(5, start_date);
        }
        else {
            pstmt1.setNull(5, Types.JAVA_OBJECT);
        }
        if (stop_date != null) {
            pstmt1.setObject(6, stop_date);
        }
        else {
            pstmt1.setNull(6, Types.JAVA_OBJECT);
        }
        if (rule != null) {
            pstmt1.setString(7, rule);
        }
        else {
            pstmt1.setNull(7, Types.JAVA_OBJECT);
        }
        if (max_uses != null) {
            pstmt1.setInt(8, max_uses);
        }
        else {
            pstmt1.setNull(8, Types.JAVA_OBJECT);
        }
        if (use_rm_rev != null) {
            pstmt1.setString(9, use_rm_rev);
        }
        else {
            pstmt1.setNull(9, Types.JAVA_OBJECT);
        }
        if (use_fb_rev != null) {
            pstmt1.setString(10, use_fb_rev);
        }
        else {
            pstmt1.setNull(10, Types.JAVA_OBJECT);
        }
        if (use_other_rev != null) {
            pstmt1.setString(11, use_other_rev);
        }
        else {
            pstmt1.setNull(11, Types.JAVA_OBJECT);
        }
        if (campaign_id != null) {
            pstmt1.setInt(12, campaign_id);
        }
        else {
            pstmt1.setNull(12, Types.JAVA_OBJECT);
        }
        if (billing_cat_id != null) {
            pstmt1.setInt(13, billing_cat_id);
        }
        else {
            pstmt1.setNull(13, Types.JAVA_OBJECT);
        }
        if (recog_cd != null) {
            pstmt1.setString(14, recog_cd);
        }
        else {
            pstmt1.setNull(14, Types.JAVA_OBJECT);
        }
        if (user_id != null) {
            pstmt1.setInt(15, user_id);
        }
        else {
            pstmt1.setNull(15, Types.JAVA_OBJECT);
        }
        if (user_id != null) {
            pstmt1.setInt(16, user_id);
        }
        else {
            pstmt1.setNull(16, Types.JAVA_OBJECT);
        }
        executeUpdate(pstmt1);
        pstmt1.close();
        promo_id = dbinfo("sqlca.sqlerrd1");
        return promo_id;
    }

}