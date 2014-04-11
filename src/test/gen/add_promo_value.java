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

public class add_promo_value extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public void execute(Integer promo_id_in, String curr_cd, Double value, Integer criteria, String comp_meth, String params_in, DateTime start_date_in, DateTime stop_date_in, Double min_value_in, Double max_value_in) throws SQLException, ProcedureException {
        /*
         * (]$[) $RCSfile$:$Revision: 24857 $ | CDATE=$Date: 2010-01-07 10:56:17 -0700 (Thu, 07 Jan 2010) $ ~
         * 
         *  Add a reward value specifcation to a promotion.
         * 
         * 	Copyright (C) 2002 Choice Hotels International, Inc.
         *                       All Rights Reserved
         */
        Integer counter;
        Integer ord_l;
        counter = 0;
        ord_l = -1;
        // set debug file to '/tmp/add_promo_value.trace';

        // trace on;

        // validate the curr_cd

        if (
        PreparedStatement pstmt1 = prepareStatement(
                  "select *"
                + " from currency c"
                + " where c.curr_cd = ?");
        
        if (curr_cd != null) {
            pstmt1.setString(1, curr_cd);
        }
        else {
            pstmt1.setNull(1, Types.JAVA_OBJECT);
        }
        ResultSet rs1 = executeQuery(pstmt1);) {
            throw new ProcedureException(-746, 0, "add_promo_value: curr_cd is invalid");
        }
        // validate the value

        if (value < 0.0) {
            throw new ProcedureException(-746, 0, "add_promo_value: value cannot be negative");
        }
        // validate the criteria

        if (criteria < 0) {
            throw new ProcedureException(-746, 0, "add_promo_value: criteria cannot be negative");
        }
        // validate the comp_meth

        if (comp_meth == null || !substring(comp_meth, 1, 3).equals("pv_")) {
            throw new ProcedureException(-746, 0, "add_promo_value: comp_meth is not valid");
        }
        // validate the min and max values

        if (min_value_in < 0.0 || max_value_in < 0.0) {
            throw new ProcedureException(-746, 0, "add_promo_value: min_value or max_value cannot be negative");
        }
        // we can only have 1 promo_value at any point in time

        // so check the following conditions:

        //      1. new pv start_date after existing pv start_date but before existing pv stop_date

        //      2. new pv stop_date after existing pv start_date but before existing pv stop_date

        PreparedStatement pstmt2 = prepareStatement(
                  "select count(*)"
                + " from promo_value"
                + " where ? >= start_date"
                + " and ? <= stop_date"
                + " and promo_id = ?");
        
        if (start_date_in != null) {
            pstmt2.setObject(1, start_date_in);
        }
        else {
            pstmt2.setNull(1, Types.JAVA_OBJECT);
        }
        if (start_date_in != null) {
            pstmt2.setObject(2, start_date_in);
        }
        else {
            pstmt2.setNull(2, Types.JAVA_OBJECT);
        }
        if (promo_id_in != null) {
            pstmt2.setInt(3, promo_id_in);
        }
        else {
            pstmt2.setNull(3, Types.JAVA_OBJECT);
        }
        ResultSet rs2 = executeQuery(pstmt2);
        rs2.next();
        counter = rs2.getInt(1);
        pstmt2.close();
        rs2.close();
        if (counter > 0) {
            throw new ProcedureException(-746, 0, "add_promo_value: overlapping promo_value dates");
        }
        PreparedStatement pstmt3 = prepareStatement(
                  "select count(*)"
                + " from promo_value"
                + " where ? >= start_date"
                + " and ? <= stop_date"
                + " and promo_id = ?");
        
        if (stop_date_in != null) {
            pstmt3.setObject(1, stop_date_in);
        }
        else {
            pstmt3.setNull(1, Types.JAVA_OBJECT);
        }
        if (stop_date_in != null) {
            pstmt3.setObject(2, stop_date_in);
        }
        else {
            pstmt3.setNull(2, Types.JAVA_OBJECT);
        }
        if (promo_id_in != null) {
            pstmt3.setInt(3, promo_id_in);
        }
        else {
            pstmt3.setNull(3, Types.JAVA_OBJECT);
        }
        ResultSet rs3 = executeQuery(pstmt3);
        rs3.next();
        counter = rs3.getInt(1);
        pstmt3.close();
        rs3.close();
        if (counter > 0) {
            throw new ProcedureException(-746, 0, "add_promo_value: overlapping promo_value dates");
        }
        PreparedStatement pstmt4 = prepareStatement(
                  "select max(ord) + 1"
                + " from promo_value"
                + " where promo_value.promo_id = ?");
        
        if (promo_id_in != null) {
            pstmt4.setInt(1, promo_id_in);
        }
        else {
            pstmt4.setNull(1, Types.JAVA_OBJECT);
        }
        ResultSet rs4 = executeQuery(pstmt4);
        rs4.next();
        ord_l = rs4.getInt(1);
        pstmt4.close();
        rs4.close();
        if (ord_l == null) {
            ord_l = 1;
        }
        PreparedStatement pstmt5 = prepareInsert(
                  "insert into promo_value (promo_id, ord, curr_cd, value, criteria, comp_meth, params, start_date, stop_date, min_value, max_value)"
                + " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        if (promo_id_in != null) {
            pstmt5.setInt(1, promo_id_in);
        }
        else {
            pstmt5.setNull(1, Types.JAVA_OBJECT);
        }
        if (ord_l != null) {
            pstmt5.setInt(2, ord_l);
        }
        else {
            pstmt5.setNull(2, Types.JAVA_OBJECT);
        }
        if (curr_cd != null) {
            pstmt5.setString(3, curr_cd);
        }
        else {
            pstmt5.setNull(3, Types.JAVA_OBJECT);
        }
        if (value != null) {
            pstmt5.setDouble(4, value);
        }
        else {
            pstmt5.setNull(4, Types.JAVA_OBJECT);
        }
        if (criteria != null) {
            pstmt5.setInt(5, criteria);
        }
        else {
            pstmt5.setNull(5, Types.JAVA_OBJECT);
        }
        if (comp_meth != null) {
            pstmt5.setString(6, comp_meth);
        }
        else {
            pstmt5.setNull(6, Types.JAVA_OBJECT);
        }
        if (params_in != null) {
            pstmt5.setString(7, params_in);
        }
        else {
            pstmt5.setNull(7, Types.JAVA_OBJECT);
        }
        if (start_date_in != null) {
            pstmt5.setObject(8, start_date_in);
        }
        else {
            pstmt5.setNull(8, Types.JAVA_OBJECT);
        }
        if (stop_date_in != null) {
            pstmt5.setObject(9, stop_date_in);
        }
        else {
            pstmt5.setNull(9, Types.JAVA_OBJECT);
        }
        if (min_value_in != null) {
            pstmt5.setDouble(10, min_value_in);
        }
        else {
            pstmt5.setNull(10, Types.JAVA_OBJECT);
        }
        if (max_value_in != null) {
            pstmt5.setDouble(11, max_value_in);
        }
        else {
            pstmt5.setNull(11, Types.JAVA_OBJECT);
        }
        executeUpdate(pstmt5);
        pstmt5.close();
    }

}