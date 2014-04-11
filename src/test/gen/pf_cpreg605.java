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

public class pf_cpreg605 extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public String execute(Integer acct_id, Integer promo_id, String stay_type, Integer prop_id, DateTime doa, Integer los, String rm_type, String srp_code, Double rm_revenue, Double fb_revenue, Double other_revenue, String curr_code, String res_source, Integer stay_id) throws SQLException, ProcedureException {
        /*
         * pf_cpreg605.sql - Check if stay is eligable for the
         *                   'Stay 2 times and earn 1 free night'
         *                   registration promotion for summer 2005. 
         * 		  The stays in question are
         *                   restricted to DOA falling within the
         *                   promotion start and end dates. Also the
         *                   stays must have qualified for points.
         * 
         *        
         * (]$[) $RCSfile$:$Revision: 9224 $ | CDATE=$Date: 2006-03-09 09:45:20 -0700 (Thu, 09 Mar 2006) $ ~
         * 
         *        Copyright (C) 2005 Choice Hotels International, Inc.
         */
        Integer no_bonus_cnt;
        Integer has_bonus_cnt;
        DateTime start_date;
        DateTime stop_date;
        Integer free_night_offer;
        no_bonus_cnt = 0;
        has_bonus_cnt = 0;
        start_date = null;
        stop_date = null;
        free_night_offer = null;
        //set debug file to '/tmp/pf_cpreg605.trace';

        //trace on;

        // Check for valid doa

        PreparedStatement pstmt1 = prepareStatement(
                  "select promo.start_date, promo.stop_date"
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
        start_date = new DateTime(rs1.getTimestamp(1).getTime());
        stop_date = new DateTime(rs1.getTimestamp(2).getTime());
        pstmt1.close();
        rs1.close();
        if (doa.isBefore(start_date) || doa.isAfter(stop_date)) {
            return "F";
        }
        // non reversed stays with promotion

        PreparedStatement pstmt2 = prepareStatement(
                  "select count(*)"
                + " from stay s, acct_trans a, prop p"
                + " where a.acct_id = ?"
                + " and a.stay_id = s.stay_id"
                + " and s.stay_type in (\"N\", \"F\")"
                + " and s.doa >= ?"
                + " and s.doa <= ?"
                + " and p.prop_id = s.prop_id"
                + " and p.country = \"US\""
                + " and a.rev_acct_trans_id is null"
                + " and new get_acct_trans_sum().execute(a.acct_trans_id) > 0"
                + " and a.acct_trans_id in ("
                    + "select ad.acct_trans_id"
                    + " from acct_trans_detail ad"
                    + " where ad.acct_trans_id = a.acct_trans_id"
                    + " and ad.promo_id in ("
                        + "select ?"
                        + " from promo"
                        + " where promo_cd = \"CPREG605\""
                        + ")"
                        + ")");
                
                if (acct_id != null) {
                    pstmt2.setInt(1, acct_id);
                }
                else {
                    pstmt2.setNull(1, Types.JAVA_OBJECT);
                }
                if (start_date != null) {
                    pstmt2.setObject(2, start_date);
                }
                else {
                    pstmt2.setNull(2, Types.JAVA_OBJECT);
                }
                if (stop_date != null) {
                    pstmt2.setObject(3, stop_date);
                }
                else {
                    pstmt2.setNull(3, Types.JAVA_OBJECT);
                }
                if (promo_id != null) {
                    pstmt2.setInt(4, promo_id);
                }
                else {
                    pstmt2.setNull(4, Types.JAVA_OBJECT);
                }
                ResultSet rs2 = executeQuery(pstmt2);
                rs2.next();
                has_bonus_cnt = rs2.getInt(1);
                rs2.close();
                // non reversed stays without promotion

                PreparedStatement pstmt3 = prepareStatement(
                          "select count(*)"
                        + " from stay s, acct_trans a, prop p"
                        + " where a.acct_id = ?"
                        + " and a.stay_id = s.stay_id"
                        + " and s.stay_type in (\"N\", \"F\")"
                        + " and s.doa >= ?"
                        + " and s.doa <= ?"
                        + " and p.prop_id = s.prop_id"
                        + " and p.country = \"US\""
                        + " and a.rev_acct_trans_id is null"
                        + " and new get_acct_trans_sum().execute(a.acct_trans_id) > 0"
                        + " and a.acct_trans_id not in ("
                            + "select ad.acct_trans_id"
                            + " from acct_trans_detail ad"
                            + " where ad.acct_trans_id = a.acct_trans_id"
                            + " and ad.promo_id in ("
                                + "select ?"
                                + " from promo"
                                + " where promo_cd = \"CPREG605\""
                                + ")"
                                + ")");
                        
                        if (acct_id != null) {
                            pstmt3.setInt(1, acct_id);
                        }
                        else {
                            pstmt3.setNull(1, Types.JAVA_OBJECT);
                        }
                        if (start_date != null) {
                            pstmt3.setObject(2, start_date);
                        }
                        else {
                            pstmt3.setNull(2, Types.JAVA_OBJECT);
                        }
                        if (stop_date != null) {
                            pstmt3.setObject(3, stop_date);
                        }
                        else {
                            pstmt3.setNull(3, Types.JAVA_OBJECT);
                        }
                        if (promo_id != null) {
                            pstmt3.setInt(4, promo_id);
                        }
                        else {
                            pstmt3.setNull(4, Types.JAVA_OBJECT);
                        }
                        ResultSet rs3 = executeQuery(pstmt3);
                        rs3.next();
                        no_bonus_cnt = rs3.getInt(1);
                        rs3.close();
                        if (has_bonus_cnt - no_bonus_cnt >= 0) {
                            return "F";
                        }
                        return "T";
                    }
                
                }