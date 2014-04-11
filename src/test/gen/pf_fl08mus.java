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

public class pf_fl08mus extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public String execute(Integer acct_id, Integer promo_id, String stay_type, Integer prop_id, DateTime doa, Integer los, String rm_type, String srp_code, Double rm_revenue, Double fb_revenue, Double other_revenue, String curr_code, String res_source, Integer stay_id) throws SQLException, ProcedureException {
        /*
         * pf_fl08mus.sql -    Check if stay is eligible for the
         *                      'Stay 3 Times, Earn Gas Card'
         *                      Fall 2006 stay twice promotion. 
         *                      The stays in question are
         *                      restricted to DOA falling within the
         *                      promotion start and end dates. Also the
         *                      stays must have qualified for points.
         *    
         *           
         * $Id: pf_fl08mus.sql 18437 2008-08-08 21:20:03Z fbloomfi $
         *    
         *        Copyright (C) 2006 Choice Hotels International, Inc.
         */
        Integer no_bonus_cnt;
        Integer has_bonus_cnt;
        Integer des_bonus_cnt;
        Integer stay_cnt;
        DateTime start_date;
        DateTime stop_date;
        String recog_cd;
        Integer check_promo_id;
        Integer l_stay_id;
        String l_prop_cd;
        DateTime l_doa;
        DateTime l_dod;
        Integer l_linked_id;
        Integer l_trans_id;
        DateTime f_dod;
        Integer n_id;
        Integer n_link;
        DateTime offer_dtime;
        Integer elite_id;
        no_bonus_cnt = 0;
        has_bonus_cnt = 0;
        des_bonus_cnt = 0;
        stay_cnt = 0;
        start_date = null;
        stop_date = null;
        recog_cd = null;
        check_promo_id = null;
        l_stay_id = null;
        l_prop_cd = null;
        l_doa = null;
        l_dod = null;
        l_linked_id = null;
        l_trans_id = null;
        f_dod = doa.plusDays(los);
        n_id = null;
        n_link = null;
        offer_dtime = null;
        elite_id = null;
        //set debug file to '/tmp/pf_fl2006cp.trace';

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
        // Get the program code of the account

        PreparedStatement pstmt2 = prepareStatement(
                  "select acct.recog_cd"
                + " from acct"
                + " where acct.acct_id = ?");
        
        if (acct_id != null) {
            pstmt2.setInt(1, acct_id);
        }
        else {
            pstmt2.setNull(1, Types.JAVA_OBJECT);
        }
        ResultSet rs2 = executeQuery(pstmt2);
        rs2.next();
        recog_cd = rs2.getString(1);
        pstmt2.close();
        rs2.close();
        // Get the promo ID for counting awarded bonuses

        // NOTE: This MUST be adjusted for each new filter!

        PreparedStatement pstmt3 = prepareStatement(
                  "select promo.promo_id"
                + " from promo"
                + " where promo.recog_cd = ?"
                + " and promo.promo_cd = \"FL08MUS\"");
        
        if (recog_cd != null) {
            pstmt3.setString(1, recog_cd);
        }
        else {
            pstmt3.setNull(1, Types.JAVA_OBJECT);
        }
        ResultSet rs3 = executeQuery(pstmt3);
        rs3.next();
        check_promo_id = rs3.getInt(1);
        pstmt3.close();
        rs3.close();
        // Fall 08 Gas Card

        // Determine if they are Diamond or Platinum elite in CURRENT year. 

        // If so, reservation source can be skipped.

        PreparedStatement pstmt4 = prepareStatement(
                  "select a.elite_level_id"
                + " from acct_elite_level a, elite_level l"
                + " where a.acct_id = ?"
                + " and a.elite_level_id = l.elite_level_id"
                + " and l.eff_year = extend(current,  to )"
                + " and l.name in (\"Diamond\", \"Platinum\")");
        
        if (acct_id != null) {
            pstmt4.setInt(1, acct_id);
        }
        else {
            pstmt4.setNull(1, Types.JAVA_OBJECT);
        }
        ResultSet rs4 = executeQuery(pstmt4);
        rs4.next();
        elite_id = rs4.getInt(1);
        pstmt4.close();
        rs4.close();
        // Get a list of stays to consider
Iterator<Object> it0 = new find_linked_stays_by_promo().execute(promo_id, start_date, stop_date, acct_id).iterator();
        while (it0.hasNext()) {
            l_stay_id = (Integer) it0.next();
            l_prop_cd = (String) it0.next();
            l_doa = (DateTime) it0.next();
            l_dod = (DateTime) it0.next();
            l_linked_id = (Integer) it0.next();
            l_trans_id = (Integer) it0.next();
            PreparedStatement pstmt5 = prepareInsert(
                      "insert into recent_stay_trans_list (stay_id, prop_cd, doa, dod, linked_id, acct_trans_id)"
                    + " values (?, ?, ?, ?, nvl(?, ?), ?)");
            if (l_stay_id != null) {
                pstmt5.setInt(1, l_stay_id);
            }
            else {
                pstmt5.setNull(1, Types.JAVA_OBJECT);
            }
            if (l_prop_cd != null) {
                pstmt5.setString(2, l_prop_cd);
            }
            else {
                pstmt5.setNull(2, Types.JAVA_OBJECT);
            }
            if (l_doa != null) {
                pstmt5.setObject(3, l_doa);
            }
            else {
                pstmt5.setNull(3, Types.JAVA_OBJECT);
            }
            if (l_dod != null) {
                pstmt5.setObject(4, l_dod);
            }
            else {
                pstmt5.setNull(4, Types.JAVA_OBJECT);
            }
            if (l_linked_id != null) {
                pstmt5.setInt(5, l_linked_id);
            }
            else {
                pstmt5.setNull(5, Types.JAVA_OBJECT);
            }
            if (l_stay_id != null) {
                pstmt5.setInt(6, l_stay_id);
            }
            else {
                pstmt5.setNull(6, Types.JAVA_OBJECT);
            }
            if (l_trans_id != null) {
                pstmt5.setInt(7, l_trans_id);
            }
            else {
                pstmt5.setNull(7, Types.JAVA_OBJECT);
            }
            executeUpdate(pstmt5);
            pstmt5.close();
        }
        // add current stay to list

        PreparedStatement pstmt6 = prepareStatement(
                  "select max(l.id)"
                + " from recent_stay_trans_list l"
                + " where l.prop_cd = new get_prop_cd().execute(prop_id)"
                + " and  ||  ||  || ");
        
        ResultSet rs5 = executeQuery(pstmt6);
        rs5.next();
        n_id = rs5.getInt(1);
        pstmt6.close();
        rs5.close();
        if (n_id != null) {
            PreparedStatement pstmt7 = prepareStatement(
                      "select nvl(l.linked_id, l.stay_id)"
                    + " from recent_stay_trans_list l"
                    + " where l.id = ?");
            
            if (n_id != null) {
                pstmt7.setInt(1, n_id);
            }
            else {
                pstmt7.setNull(1, Types.JAVA_OBJECT);
            }
            ResultSet rs6 = executeQuery(pstmt7);
            rs6.next();
            n_link = rs6.getInt(1);
            pstmt7.close();
            rs6.close();
        }
        else {
            n_link = stay_id;
        }
        PreparedStatement pstmt8 = prepareInsert(
                  "insert into recent_stay_trans_list (stay_id, prop_cd, doa, dod, linked_id)"
                + " values (?, new get_prop_cd().execute(prop_id), ?, ?, ?)");
        if (stay_id != null) {
            pstmt8.setInt(1, stay_id);
        }
        else {
            pstmt8.setNull(1, Types.JAVA_OBJECT);
        }
        if (doa != null) {
            pstmt8.setObject(2, doa);
        }
        else {
            pstmt8.setNull(2, Types.JAVA_OBJECT);
        }
        if (f_dod != null) {
            pstmt8.setObject(3, f_dod);
        }
        else {
            pstmt8.setNull(3, Types.JAVA_OBJECT);
        }
        if (n_link != null) {
            pstmt8.setInt(4, n_link);
        }
        else {
            pstmt8.setNull(4, Types.JAVA_OBJECT);
        }
        executeUpdate(pstmt8);
        pstmt8.close();
        // Get a count of non-reversed stays with promotion

        PreparedStatement pstmt9 = prepareStatement(
                  "select count(*)"
                + " from recent_stay_trans_list l"
                + " where l.acct_trans_id in ("
                    + "select ad.acct_trans_id"
                    + " from acct_trans_detail ad"
                    + " where ad.acct_trans_id = l.acct_trans_id"
                    + " and ad.promo_id = ?"
                    + ")"
                    + " and l.doa >= ?"
                    + " and l.doa <= ?");
            
            if (check_promo_id != null) {
                pstmt9.setInt(1, check_promo_id);
            }
            else {
                pstmt9.setNull(1, Types.JAVA_OBJECT);
            }
            if (start_date != null) {
                pstmt9.setObject(2, start_date);
            }
            else {
                pstmt9.setNull(2, Types.JAVA_OBJECT);
            }
            if (stop_date != null) {
                pstmt9.setObject(3, stop_date);
            }
            else {
                pstmt9.setNull(3, Types.JAVA_OBJECT);
            }
            ResultSet rs7 = executeQuery(pstmt9);
            rs7.next();
            has_bonus_cnt = rs7.getInt(1);
            rs7.close();
            if (elite_id == null) {
                // no current diamond or platinum elite

                // Get a count of stays dependant on res source

                PreparedStatement pstmt10 = prepareStatement(
                          "select count(distinct linked_id)"
                        + " from recent_stay_trans_list l, stay s, prop p"
                        + " where l.doa >= ?"
                        + " and l.doa <= ?"
                        + " and l.linked_id = s.stay_id"
                        + " and s.res_source in (\"C\", \"N\")"
                        + " and p.prop_id = s.prop_id"
                        + " and p.country in ("
                            + "select country"
                            + " from region_country"
                            + " where region_id in (1, 2)"
                            + ")");
                    
                    if (start_date != null) {
                        pstmt10.setObject(1, start_date);
                    }
                    else {
                        pstmt10.setNull(1, Types.JAVA_OBJECT);
                    }
                    if (stop_date != null) {
                        pstmt10.setObject(2, stop_date);
                    }
                    else {
                        pstmt10.setNull(2, Types.JAVA_OBJECT);
                    }
                    ResultSet rs8 = executeQuery(pstmt10);
                    rs8.next();
                    stay_cnt = rs8.getInt(1);
                    rs8.close();
                }
                else {
                    // Get a count of stays dependant on res source

                    PreparedStatement pstmt11 = prepareStatement(
                              "select count(distinct linked_id)"
                            + " from recent_stay_trans_list l, stay s, prop p"
                            + " where l.doa >= ?"
                            + " and l.doa <= ?"
                            + " and l.linked_id = s.stay_id"
                            + " and p.prop_id = s.prop_id"
                            + " and p.country in ("
                                + "select country"
                                + " from region_country"
                                + " where region_id in (1, 2)"
                                + ")");
                        
                        if (start_date != null) {
                            pstmt11.setObject(1, start_date);
                        }
                        else {
                            pstmt11.setNull(1, Types.JAVA_OBJECT);
                        }
                        if (stop_date != null) {
                            pstmt11.setObject(2, stop_date);
                        }
                        else {
                            pstmt11.setNull(2, Types.JAVA_OBJECT);
                        }
                        ResultSet rs9 = executeQuery(pstmt11);
                        rs9.next();
                        stay_cnt = rs9.getInt(1);
                        rs9.close();
                    }
                    // Drop the temp table 

                    PreparedStatement pstmt12 = prepareStatement("drop table recent_stay_trans_list");
                    executeUpdate(pstmt12);
                    pstmt12.close();
                    if (stay_cnt > 0) {
                        // Current stay deserves bonus because total stays / 3 = number of bonuses

                        // Subtract the number of bonuses deserved from the amount they have.

                        // If they have less than they deserve, give 'em one.

                        des_bonus_cnt = stay_cnt / 3 - has_bonus_cnt;
                        if (des_bonus_cnt > 0.0) {
                            return "T";
                        }
                        else {
                            return "F";
                        }
                    }
                    else {
                        return "F";
                    }
                }
            
            }