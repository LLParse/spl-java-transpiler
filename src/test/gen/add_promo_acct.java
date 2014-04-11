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

public class add_promo_acct extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public Integer execute(Integer promo_id, String elig_type, String recog_cd, String appl_group_cd, String offer_cd, String location, String payment_meth, String filter_name, String elite_level_name, String user_name, DateTime start_date_in, DateTime stop_date_in) throws SQLException, ProcedureException {
        /*
         * (]$[) $RCSfile$:$Revision: 19803 $ | CDATE=$Date: 2008-11-13 20:59:18 -0700 (Thu, 13 Nov 2008) $ ~
         * 
         *  Add a account eligiblity specifcation to a promotion.
         * 
         * 	Copyright (C) 2002 Choice Hotels International, Inc.
         *                       All Rights Reserved
         */
        Integer ord;
        String promo_recog_cd;
        Integer appl_group_id;
        Integer offer_id;
        Integer filter_id;
        Integer elite_level_id;
        Integer user_id;
        ord = null;
        promo_recog_cd = null;
        appl_group_id = null;
        offer_id = null;
        filter_id = null;
        elite_level_id = null;
        user_id = null;
        // set debug file to '/tmp/add_promo_acct.trace';

        // trace on;

        // compute the ord value

        PreparedStatement pstmt1 = prepareStatement(
                  "select max(pa.ord)"
                + " from promo_acct_elig pa"
                + " where pa.promo_id = ?");
        
        if (promo_id != null) {
            pstmt1.setInt(1, promo_id);
        }
        else {
            pstmt1.setNull(1, Types.JAVA_OBJECT);
        }
        ResultSet rs1 = executeQuery(pstmt1);
        rs1.next();
        ord = rs1.getInt(1);
        pstmt1.close();
        rs1.close();
        if (ord == null) {
            ord = 1;
        }
        else {
            ord = ord + 1;
        }
        // get the recog_cd associated with the promotion

        PreparedStatement pstmt2 = prepareStatement(
                  "select p.recog_cd"
                + " from promo p"
                + " where p.promo_id = ?");
        
        if (promo_id != null) {
            pstmt2.setInt(1, promo_id);
        }
        else {
            pstmt2.setNull(1, Types.JAVA_OBJECT);
        }
        ResultSet rs2 = executeQuery(pstmt2);
        rs2.next();
        promo_recog_cd = rs2.getString(1);
        pstmt2.close();
        rs2.close();
        // validate the elig_type

        if (elig_type != null) {
            if (!elig_type.equals("A")) {
                throw new ProcedureException(-746, 0, "add_promo_acct: elig_type is invalid");
            }
        }
        // validate and encode the appl_group_cd

        if (appl_group_cd != null) {
            appl_group_id = new get_appl_group_id().execute(promo_recog_cd, appl_group_cd);
            if (appl_group_id == null) {
                throw new ProcedureException(-746, 0, "add_promo_acct: appl_group_cd is invalid");
            }
        }
        // validate and encode the offer_cd

        if (offer_cd != null) {
            offer_id = new get_offer_id().execute(promo_recog_cd, offer_cd);
            if (offer_id == null) {
                throw new ProcedureException(-746, 0, "add_promo_acct: offer_cd is invalid");
            }
        }
        // validate the location

        if (location != null) {
            throw new ProcedureException(-746, 0, "add_promo_acct: location must be null");
        }
        // validate the payment_meth

        if (payment_meth != null) {
            if (!payment_meth.equals("CASH")) {
                if (
                PreparedStatement pstmt3 = prepareStatement(
                          "select *"
                        + " from cc"
                        + " where cc_cd = ?");
                
                if (payment_meth != null) {
                    pstmt3.setString(1, payment_meth);
                }
                else {
                    pstmt3.setNull(1, Types.JAVA_OBJECT);
                }
                ResultSet rs3 = executeQuery(pstmt3);) {
                    throw new ProcedureException(-746, 0, "add_promo_acct: payment_meth is invalid");
                }
            }
        }
        // validate and encode the filter_name

        if (filter_name != null) {
            PreparedStatement pstmt4 = prepareStatement(
                      "select pf.filter_id"
                    + " from promo_filter pf"
                    + " where pf.name = ?");
            
            if (filter_name != null) {
                pstmt4.setString(1, filter_name);
            }
            else {
                pstmt4.setNull(1, Types.JAVA_OBJECT);
            }
            ResultSet rs4 = executeQuery(pstmt4);
            rs4.next();
            filter_id = rs4.getInt(1);
            pstmt4.close();
            rs4.close();
            if (filter_id == null) {
                throw new ProcedureException(-746, 0, "add_promo_acct: filter_id is invalid");
            }
        }
        // validate and encode the elite_level_name

        if (elite_level_name != null) {
            PreparedStatement pstmt5 = prepareStatement(
                      "select e.elite_level_id"
                    + " from elite_level e, promo p"
                    + " where e.recog_cd = ?"
                    + " and e.name = ?"
                    + " and p.promo_id = ?"
                    + " and ");
            
            if (promo_recog_cd != null) {
                pstmt5.setString(1, promo_recog_cd);
            }
            else {
                pstmt5.setNull(1, Types.JAVA_OBJECT);
            }
            if (elite_level_name != null) {
                pstmt5.setString(2, elite_level_name);
            }
            else {
                pstmt5.setNull(2, Types.JAVA_OBJECT);
            }
            if (promo_id != null) {
                pstmt5.setInt(3, promo_id);
            }
            else {
                pstmt5.setNull(3, Types.JAVA_OBJECT);
            }
            ResultSet rs5 = executeQuery(pstmt5);
            rs5.next();
            elite_level_id = rs5.getInt(1);
            pstmt5.close();
            rs5.close();
            if (elite_level_id == null) {
                throw new ProcedureException(-746, 0, "add_promo_acct: elite_level_name is invalid");
            }
        }
        // validate and encode the user_name

        user_id = new get_user_id().execute(user_name);
        if (user_id == null) {
            throw new ProcedureException(-746, 0, "add_promo_acct: user_name is not known");
        }
        // validate the dates

        if (start_date_in.isBefore(new LocalDate().toDateTimeAtStartOfDay().minusDays(5))) {
            throw new ProcedureException(-746, 0, "start date is out of range");
        }
        if (stop_date_in.isBefore(new LocalDate().toDateTimeAtStartOfDay())) {
            throw new ProcedureException(-746, 0, "stop date is out of range");
        }
        PreparedStatement pstmt6 = prepareInsert(
                  "insert into promo_acct_elig (promo_id, ord, elig_type, recog_cd, appl_group_id, offer_id, location, payment_meth, filter_id, elite_level_id, entry_id, last_update_dtime, last_update_id, start_date, stop_date)"
                + " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, current, ?, ?, ?)");
        if (promo_id != null) {
            pstmt6.setInt(1, promo_id);
        }
        else {
            pstmt6.setNull(1, Types.JAVA_OBJECT);
        }
        if (ord != null) {
            pstmt6.setInt(2, ord);
        }
        else {
            pstmt6.setNull(2, Types.JAVA_OBJECT);
        }
        if (elig_type != null) {
            pstmt6.setString(3, elig_type);
        }
        else {
            pstmt6.setNull(3, Types.JAVA_OBJECT);
        }
        if (recog_cd != null) {
            pstmt6.setString(4, recog_cd);
        }
        else {
            pstmt6.setNull(4, Types.JAVA_OBJECT);
        }
        if (appl_group_id != null) {
            pstmt6.setInt(5, appl_group_id);
        }
        else {
            pstmt6.setNull(5, Types.JAVA_OBJECT);
        }
        if (offer_id != null) {
            pstmt6.setInt(6, offer_id);
        }
        else {
            pstmt6.setNull(6, Types.JAVA_OBJECT);
        }
        if (location != null) {
            pstmt6.setString(7, location);
        }
        else {
            pstmt6.setNull(7, Types.JAVA_OBJECT);
        }
        if (payment_meth != null) {
            pstmt6.setString(8, payment_meth);
        }
        else {
            pstmt6.setNull(8, Types.JAVA_OBJECT);
        }
        if (filter_id != null) {
            pstmt6.setInt(9, filter_id);
        }
        else {
            pstmt6.setNull(9, Types.JAVA_OBJECT);
        }
        if (elite_level_id != null) {
            pstmt6.setInt(10, elite_level_id);
        }
        else {
            pstmt6.setNull(10, Types.JAVA_OBJECT);
        }
        if (user_id != null) {
            pstmt6.setInt(11, user_id);
        }
        else {
            pstmt6.setNull(11, Types.JAVA_OBJECT);
        }
        if (user_id != null) {
            pstmt6.setInt(12, user_id);
        }
        else {
            pstmt6.setNull(12, Types.JAVA_OBJECT);
        }
        if (start_date_in != null) {
            pstmt6.setObject(13, start_date_in);
        }
        else {
            pstmt6.setNull(13, Types.JAVA_OBJECT);
        }
        if (stop_date_in != null) {
            pstmt6.setObject(14, stop_date_in);
        }
        else {
            pstmt6.setNull(14, Types.JAVA_OBJECT);
        }
        executeUpdate(pstmt6);
        pstmt6.close();
        return ord;
    }

}