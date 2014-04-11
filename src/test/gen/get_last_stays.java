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

public class get_last_stays extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public Collection<Object> execute(String recog_cd, String recog_id, Integer max_stays) throws SQLException, ProcedureException {
        //bonus name

        /*
         * (]$[) $RCSfile$:$Revision: 5804 $ | CDATE=$Date: 2004-12-13 04:10:28 -0700 (Mon, 13 Dec 2004) $ ~
         * 
         * 
         *   get_last_stays -  get the last N stays for an account
         * 
         *       Copyright (C) 2002 Choice Hotels International, Inc.
         *                       All Rights Reserved
         */
        Integer sql_error;
        Integer isam_error;
        String error_data;
        Integer acct_id;
        Integer acct_trans_id;
        String prop_cd;
        String prop_name;
        String city;
        String state;
        String country;
        DateTime doa;
        Integer los;
        Double amount_earned;
        Double bonus_amount;
        Double t_bonus_amount;
        String bonus_name;
        String t_bonus_name;
        Integer ord;
        //set debug file to '/tmp/get_last_stays.trace';

        //trace on;

        try {
            acct_id = null;
            acct_trans_id = null;
            prop_cd = null;
            prop_name = null;
            city = null;
            state = null;
            country = null;
            doa = null;
            los = null;
            amount_earned = null;
            bonus_amount = 0.0;
            bonus_name = null;
            ord = 0;
            acct_id = new get_acct_id().execute(recog_cd, recog_id);
            PreparedStatement pstmt1 = prepareStatement(
                      "select t.acct_trans_id, p.prop_cd, p.name, p.city, e.name, c.name, s.doa, s.los, new get_acct_trans_sum().execute(t.acct_trans_id)"
                    + " from stay s, acct_trans t, prop p, state e, country c"
                    + " where t.acct_id = ?"
                    + " and t.stay_id = s.stay_id"
                    + " and p.prop_id = s.prop_id"
                    + " and e.state_cd = p.state"
                    + " and e.country_cd = p.country"
                    + " and c.country_cd = p.country"
                    + " and s.stay_type in (\"N\", \"F\")"
                    + " and t.final_dtime > current.minusYears(2)"
                    + " and t.rev_acct_trans_id is null"
                    + " and new get_acct_trans_sum().execute(t.acct_trans_id) > 0.0"
                    + " order by t.acct_trans_id desc");
            
            if (acct_id != null) {
                pstmt1.setInt(1, acct_id);
            }
            else {
                pstmt1.setNull(1, Types.JAVA_OBJECT);
            }
            ResultSet rs1 = executeQuery(pstmt1);
            while (rs1.next()) {
                acct_trans_id = rs1.getInt(1);
                prop_cd = rs1.getString(2);
                prop_name = rs1.getString(3);
                city = rs1.getString(4);
                state = rs1.getString(5);
                country = rs1.getString(6);
                doa = new DateTime(rs1.getTimestamp(7).getTime());
                los = rs1.getInt(8);
                amount_earned = rs1.getDouble(9);
                ord = ord + 1;
                bonus_amount = 0.0;
                t_bonus_amount = 0.0;
                bonus_name = null;
                t_bonus_name = null;
                // Scan details for largest promotional bonus

                PreparedStatement pstmt2 = prepareStatement(
                          "select d.amount, p.name"
                        + " from acct_trans_detail d, promo p"
                        + " where d.acct_trans_id = ?"
                        + " and d.promo_id = p.promo_id"
                        + " and p.rule <> \"A\"");
                
                if (acct_trans_id != null) {
                    pstmt2.setInt(1, acct_trans_id);
                }
                else {
                    pstmt2.setNull(1, Types.JAVA_OBJECT);
                }
                ResultSet rs2 = executeQuery(pstmt2);
                while (rs2.next()) {
                    t_bonus_amount = rs2.getDouble(1);
                    t_bonus_name = rs2.getString(2);
                    if (t_bonus_amount > bonus_amount) {
                        bonus_amount = t_bonus_amount;
                        bonus_name = t_bonus_name;
                    }
                }
                pstmt2.close();
                rs2.close();
                // if no bonus found set to null

                if (bonus_amount.equals(0)) {
                    bonus_amount = null;
                }
                if (amount_earned.equals(0)) {
                    continue;
                }
                PreparedStatement pstmt3 = prepareInsert(
                          "insert into last_stays"
                        + " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                if (ord != null) {
                    pstmt3.setInt(1, ord);
                }
                else {
                    pstmt3.setNull(1, Types.JAVA_OBJECT);
                }
                if (prop_cd != null) {
                    pstmt3.setString(2, prop_cd);
                }
                else {
                    pstmt3.setNull(2, Types.JAVA_OBJECT);
                }
                if (prop_name != null) {
                    pstmt3.setString(3, prop_name);
                }
                else {
                    pstmt3.setNull(3, Types.JAVA_OBJECT);
                }
                if (city != null) {
                    pstmt3.setString(4, city);
                }
                else {
                    pstmt3.setNull(4, Types.JAVA_OBJECT);
                }
                if (state != null) {
                    pstmt3.setString(5, state);
                }
                else {
                    pstmt3.setNull(5, Types.JAVA_OBJECT);
                }
                if (country != null) {
                    pstmt3.setString(6, country);
                }
                else {
                    pstmt3.setNull(6, Types.JAVA_OBJECT);
                }
                if (doa != null) {
                    pstmt3.setObject(7, doa);
                }
                else {
                    pstmt3.setNull(7, Types.JAVA_OBJECT);
                }
                if (los != null) {
                    pstmt3.setInt(8, los);
                }
                else {
                    pstmt3.setNull(8, Types.JAVA_OBJECT);
                }
                if (amount_earned != null) {
                    pstmt3.setDouble(9, amount_earned);
                }
                else {
                    pstmt3.setNull(9, Types.JAVA_OBJECT);
                }
                if (bonus_amount != null) {
                    pstmt3.setDouble(10, bonus_amount);
                }
                else {
                    pstmt3.setNull(10, Types.JAVA_OBJECT);
                }
                if (bonus_name != null) {
                    pstmt3.setString(11, bonus_name);
                }
                else {
                    pstmt3.setNull(11, Types.JAVA_OBJECT);
                }
                executeUpdate(pstmt3);
                pstmt3.close();
                if (ord.equals(max_stays)) {
                    break;
                }
            }
            pstmt1.close();
            rs1.close();
            ord = null;
            prop_cd = null;
            prop_name = null;
            city = null;
            state = null;
            country = null;
            doa = null;
            los = null;
            amount_earned = null;
            PreparedStatement pstmt4 = prepareStatement(
                      "select l.ord, l.prop_cd, l.prop_name, l.city, l.state, l.country, l.doa, l.los, l.amount_earned, l.bonus_amount, l.bonus_name"
                    + " from last_stays l"
                    + " order by l.doa desc");
            
            ResultSet rs3 = executeQuery(pstmt4);
            while (rs3.next()) {
                ord = rs3.getInt(1);
                prop_cd = rs3.getString(2);
                prop_name = rs3.getString(3);
                city = rs3.getString(4);
                state = rs3.getString(5);
                country = rs3.getString(6);
                doa = new DateTime(rs3.getTimestamp(7).getTime());
                los = rs3.getInt(8);
                amount_earned = rs3.getDouble(9);
                bonus_amount = rs3.getDouble(10);
                bonus_name = rs3.getString(11);
                return new ArrayList<Object>(Arrays.<Object>asList(prop_cd, prop_name, city, state, country, doa, los, amount_earned, bonus_amount, bonus_name));
            }
            pstmt4.close();
            rs3.close();
            PreparedStatement pstmt5 = prepareStatement("drop table last_stays");
            executeUpdate(pstmt5);
            pstmt5.close();
            return new ArrayList<Object>(Arrays.<Object>asList(null, null, null, null, null, null, null, null, null, null));
            //set debug file to '/tmp/get_last_stays.trace';

            //trace on;

        }
        catch (SQLException e) {
            sql_error = e.getErrorCode();
            isam_error = 0;
            error_data = e.getMessage();
            PreparedStatement pstmt6 = prepareStatement("drop table last_stays");
            executeUpdate(pstmt6);
            pstmt6.close();
            throw new ProcedureException(sql_error, isam_error, error_data);
        }
    }

}