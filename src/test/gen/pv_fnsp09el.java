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

public class pv_fnsp09el extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public Double execute(Integer acct_id, Integer cust_id, Integer promo_id, Integer criteria, Double value, Double stay_rev, String chain_cd, Integer los, Integer stay_id) throws SQLException, ProcedureException {
        /*
         * $Id: pv_fnsp09el.sql 20931 2009-02-26 22:48:48Z rshepher $
         * 
         *   Description: 
         * 
         *   Compute the amount of points to award a free night, up to 8000 
         *   if the property either stay is Modscale. Up to 6000 if both are
         *   economy. The last stay base points + base points of this stay
         *   are taken into account.
         * 
         *       Copyright (C) 2009 Choice Hotels International, Inc.
         *                       All Rights Reserved
         */
        Double amount;
        Integer last_elig_tran;
        Double last_stay_value;
        Double this_stay_value;
        Double residual;
        DateTime promo_start_date;
        DateTime promo_stop_date;
        Integer l_stay_id;
        String l_prop_cd;
        DateTime l_doa;
        DateTime l_dod;
        Integer l_linked_id;
        Integer l_trans_id;
        DateTime offer_dtime;
        String last_chain;
        amount = null;
        last_elig_tran = null;
        last_stay_value = 0.0;
        this_stay_value = 0.0;
        residual = 0.0;
        promo_start_date = null;
        promo_stop_date = null;
        l_stay_id = null;
        l_prop_cd = null;
        l_doa = null;
        l_dod = null;
        l_linked_id = null;
        l_trans_id = null;
        offer_dtime = null;
        last_chain = null;
        //set debug file to '/tmp/pv_fnsp09el.trace';

        //trace on;

        // First get the date range in question

        PreparedStatement pstmt1 = prepareStatement(
                  "select start_date, stop_date"
                + " from promo"
                + " where promo.promo_id = ?");
        
        if (promo_id != null) {
            pstmt1.setInt(1, promo_id);
        }
        else {
            pstmt1.setNull(1, Types.JAVA_OBJECT);
        }
        ResultSet rs1 = executeQuery(pstmt1);
        rs1.next();
        promo_start_date = new DateTime(rs1.getTimestamp(1).getTime());
        promo_stop_date = new DateTime(rs1.getTimestamp(2).getTime());
        pstmt1.close();
        rs1.close();
        // Get a list of stays to consider
Iterator<Object> it0 = new find_linked_stays_by_promo().execute(promo_id, promo_start_date, promo_stop_date, acct_id).iterator();
        while (it0.hasNext()) {
            l_stay_id = (Integer) it0.next();
            l_prop_cd = (String) it0.next();
            l_doa = (DateTime) it0.next();
            l_dod = (DateTime) it0.next();
            l_linked_id = (Integer) it0.next();
            l_trans_id = (Integer) it0.next();
            PreparedStatement pstmt2 = prepareInsert(
                      "insert into recent_stay_trans_list (stay_id, prop_cd, doa, dod, linked_id, acct_trans_id)"
                    + " values (?, ?, ?, ?, nvl(?, ?), ?)");
            if (l_stay_id != null) {
                pstmt2.setInt(1, l_stay_id);
            }
            else {
                pstmt2.setNull(1, Types.JAVA_OBJECT);
            }
            if (l_prop_cd != null) {
                pstmt2.setString(2, l_prop_cd);
            }
            else {
                pstmt2.setNull(2, Types.JAVA_OBJECT);
            }
            if (l_doa != null) {
                pstmt2.setObject(3, l_doa);
            }
            else {
                pstmt2.setNull(3, Types.JAVA_OBJECT);
            }
            if (l_dod != null) {
                pstmt2.setObject(4, l_dod);
            }
            else {
                pstmt2.setNull(4, Types.JAVA_OBJECT);
            }
            if (l_linked_id != null) {
                pstmt2.setInt(5, l_linked_id);
            }
            else {
                pstmt2.setNull(5, Types.JAVA_OBJECT);
            }
            if (l_stay_id != null) {
                pstmt2.setInt(6, l_stay_id);
            }
            else {
                pstmt2.setNull(6, Types.JAVA_OBJECT);
            }
            if (l_trans_id != null) {
                pstmt2.setInt(7, l_trans_id);
            }
            else {
                pstmt2.setNull(7, Types.JAVA_OBJECT);
            }
            executeUpdate(pstmt2);
            pstmt2.close();
        }
        // Next locate the base amounts for the last stay

        PreparedStatement pstmt3 = prepareStatement(
                  "select max(acct_trans_id)"
                + " from recent_stay_trans_list");
        
        ResultSet rs2 = executeQuery(pstmt3);
        rs2.next();
        last_elig_tran = rs2.getInt(1);
        pstmt3.close();
        rs2.close();
        // Next determine the chain associated with the transaction

        PreparedStatement pstmt4 = prepareStatement(
                  "select p.chain_id"
                + " from stay s, prop p, acct_trans t"
                + " where t.acct_trans_id = ?"
                + " and s.stay_id = t.stay_id"
                + " and p.prop_id = s.prop_id");
        
        if (last_elig_tran != null) {
            pstmt4.setInt(1, last_elig_tran);
        }
        else {
            pstmt4.setNull(1, Types.JAVA_OBJECT);
        }
        ResultSet rs3 = executeQuery(pstmt4);
        rs3.next();
        last_chain = rs3.getString(1);
        pstmt4.close();
        rs3.close();
        PreparedStatement pstmt5 = prepareStatement(
                  "select d.amount"
                + " from acct_trans_detail d, promo p"
                + " where d.acct_trans_id = ?"
                + " and d.promo_id = p.promo_id"
                + " and p.rule = \"A\"");
        
        if (last_elig_tran != null) {
            pstmt5.setInt(1, last_elig_tran);
        }
        else {
            pstmt5.setNull(1, Types.JAVA_OBJECT);
        }
        ResultSet rs4 = executeQuery(pstmt5);
        rs4.next();
        last_stay_value = rs4.getDouble(1);
        pstmt5.close();
        rs4.close();
        // Compute the value of the current stay based on revenue and chain

        if ((chain_cd.equals("E") || chain_cd.equals("W") || chain_cd.equals("S") || chain_cd.equals("M"))) {
            this_stay_value = 5.0 * trunc(stay_rev, 0) / criteria.doubleValue();
        }
        else {
            this_stay_value = 10.0 * trunc(stay_rev, 0) / criteria.doubleValue();
        }
        if (this_stay_value.equals(0.0)) {
            PreparedStatement pstmt6 = prepareStatement("drop table recent_stay_trans_list");
            executeUpdate(pstmt6);
            pstmt6.close();
            return 0;
        }
        this_stay_value = this_stay_value + last_stay_value;
        // Now see what they get, 2 economy/extended stay get 1 value,

        // a combo of mid and econ gets another

        if ((last_chain.equals("E") || last_chain.equals("W") || last_chain.equals("S") || last_chain.equals("M")) && (chain_cd.equals("E") || chain_cd.equals("W") || chain_cd.equals("S") || chain_cd.equals("M"))) {
            residual = mod(this_stay_value, 6000);
            if (residual < 1000.0) {
                amount = 6000.0 - residual;
            }
            else {
                amount = 5000.0;
            }
        }
        else {
            residual = mod(this_stay_value, 8000);
            if (residual < 3000.0) {
                amount = 8000.0 - residual;
            }
            else {
                amount = 5000.0;
            }
        }
        PreparedStatement pstmt7 = prepareStatement("drop table recent_stay_trans_list");
        executeUpdate(pstmt7);
        pstmt7.close();
        return amount;
    }

}