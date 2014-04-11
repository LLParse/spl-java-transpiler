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

public class pf_load_recent_stay_list extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public Integer execute(Integer in_acct_id, Integer in_stay_id, Integer in_promo_id, Integer in_prop_id, DateTime in_doa, DateTime in_dod, DateTime in_start_date, DateTime in_stop_date) throws SQLException, ProcedureException {
        /*
         * pf_load_recent_stay_list.sql - Load promo stays into temp table
         *    
         * $Id: pf_load_recent_stay_list.sql 52307 2012-06-18 23:30:58Z rick_shepherd $
         *    
         *        Copyright (C) 2012 Choice Hotels International, Inc.
         */
        String l_debug;
        Integer l_stay_id;
        String l_prop_cd;
        DateTime l_doa;
        DateTime l_dod;
        DateTime l_curr_post_dtime;
        Integer l_linked_id;
        Integer l_trans_id;
        Integer l_id;
        Integer l_min_id;
        Integer l_link;
        Integer l_ord;
        String l_res_source;
        Double l_points;
        String l_srp_code;
        DateTime l_post_dtime;
        Integer sql_error;
        Integer isam_error;
        String error_data;
        try {
            l_debug = "F";
            l_stay_id = null;
            l_linked_id = null;
            l_prop_cd = null;
            l_doa = null;
            l_dod = null;
            l_curr_post_dtime = null;
            l_trans_id = null;
            l_id = null;
            l_min_id = null;
            l_link = null;
            l_ord = 1;
            l_res_source = null;
            l_points = 0.0;
            l_srp_code = null;
            // create temp table to hold stays withing the promo period for the account

            // Get a list of linked stays to consider
Iterator<Object> it0 = new find_linked_stays_by_promo().execute(in_promo_id, in_start_date, in_stop_date, in_acct_id).iterator();
            while (it0.hasNext()) {
                l_stay_id = (Integer) it0.next();
                l_prop_cd = (String) it0.next();
                l_doa = (DateTime) it0.next();
                l_dod = (DateTime) it0.next();
                l_linked_id = (Integer) it0.next();
                l_trans_id = (Integer) it0.next();
                PreparedStatement pstmt1 = prepareStatement(
                          "select post_dtime, res_source, srp_code"
                        + " from stay"
                        + " where stay_id = ?");
                
                if (l_stay_id != null) {
                    pstmt1.setInt(1, l_stay_id);
                }
                else {
                    pstmt1.setNull(1, Types.JAVA_OBJECT);
                }
                ResultSet rs1 = executeQuery(pstmt1);
                rs1.next();
                l_post_dtime = new DateTime(rs1.getTimestamp(1).getTime());
                l_res_source = rs1.getString(2);
                l_srp_code = rs1.getString(3);
                pstmt1.close();
                rs1.close();
                l_points = (Double) new get_base_points().execute(l_trans_id);
                PreparedStatement pstmt2 = prepareInsert(
                          "insert into recent_stay_list (stay_id, prop_cd, doa, dod, linked_id, acct_trans_id, ord, post_dtime, res_source, points, srp_code)"
                        + " values (?, ?, ?, ?, nvl(?, ?), ?, ?, ?, ?, ?, ?)");
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
                if (l_ord != null) {
                    pstmt2.setInt(8, l_ord);
                }
                else {
                    pstmt2.setNull(8, Types.JAVA_OBJECT);
                }
                if (l_post_dtime != null) {
                    pstmt2.setObject(9, l_post_dtime);
                }
                else {
                    pstmt2.setNull(9, Types.JAVA_OBJECT);
                }
                if (l_res_source != null) {
                    pstmt2.setString(10, l_res_source);
                }
                else {
                    pstmt2.setNull(10, Types.JAVA_OBJECT);
                }
                if (l_points != null) {
                    pstmt2.setDouble(11, l_points);
                }
                else {
                    pstmt2.setNull(11, Types.JAVA_OBJECT);
                }
                if (l_srp_code != null) {
                    pstmt2.setString(12, l_srp_code);
                }
                else {
                    pstmt2.setNull(12, Types.JAVA_OBJECT);
                }
                executeUpdate(pstmt2);
                pstmt2.close();
                l_ord = l_ord + 1;
            }
            // add current stay to list

            PreparedStatement pstmt3 = prepareStatement(
                      "select max(l.id)"
                    + " from recent_stay_list l"
                    + " where l.prop_cd = new get_prop_cd().execute(in_prop_id)"
                    + " and  ||  ||  || ");
            
            ResultSet rs2 = executeQuery(pstmt3);
            rs2.next();
            l_id = rs2.getInt(1);
            pstmt3.close();
            rs2.close();
            if (l_id != null) {
                PreparedStatement pstmt4 = prepareStatement(
                          "select nvl(l.linked_id, l.stay_id)"
                        + " from recent_stay_list l"
                        + " where l.id = ?");
                
                if (l_id != null) {
                    pstmt4.setInt(1, l_id);
                }
                else {
                    pstmt4.setNull(1, Types.JAVA_OBJECT);
                }
                ResultSet rs3 = executeQuery(pstmt4);
                rs3.next();
                l_link = rs3.getInt(1);
                pstmt4.close();
                rs3.close();
            }
            else {
                l_link = in_stay_id;
            }
            PreparedStatement pstmt5 = prepareStatement(
                      "select post_dtime, res_source, srp_code"
                    + " from stay"
                    + " where stay_id = ?");
            
            if (in_stay_id != null) {
                pstmt5.setInt(1, in_stay_id);
            }
            else {
                pstmt5.setNull(1, Types.JAVA_OBJECT);
            }
            ResultSet rs4 = executeQuery(pstmt5);
            rs4.next();
            l_post_dtime = new DateTime(rs4.getTimestamp(1).getTime());
            l_res_source = rs4.getString(2);
            l_srp_code = rs4.getString(3);
            pstmt5.close();
            rs4.close();
            l_points = 0.0;
            // current stay has no acct_trans record yet

            PreparedStatement pstmt6 = prepareInsert(
                      "insert into recent_stay_list (stay_id, prop_cd, doa, dod, linked_id, ord, post_dtime, res_source, points, srp_code)"
                    + " values (?, new get_prop_cd().execute(in_prop_id), ?, ?, ?, ?, ?, ?, ?, ?)");
            if (in_stay_id != null) {
                pstmt6.setInt(1, in_stay_id);
            }
            else {
                pstmt6.setNull(1, Types.JAVA_OBJECT);
            }
            if (in_doa != null) {
                pstmt6.setObject(2, in_doa);
            }
            else {
                pstmt6.setNull(2, Types.JAVA_OBJECT);
            }
            if (in_dod != null) {
                pstmt6.setObject(3, in_dod);
            }
            else {
                pstmt6.setNull(3, Types.JAVA_OBJECT);
            }
            if (l_link != null) {
                pstmt6.setInt(4, l_link);
            }
            else {
                pstmt6.setNull(4, Types.JAVA_OBJECT);
            }
            if (l_ord != null) {
                pstmt6.setInt(5, l_ord);
            }
            else {
                pstmt6.setNull(5, Types.JAVA_OBJECT);
            }
            if (l_curr_post_dtime != null) {
                pstmt6.setObject(6, l_curr_post_dtime);
            }
            else {
                pstmt6.setNull(6, Types.JAVA_OBJECT);
            }
            if (l_res_source != null) {
                pstmt6.setString(7, l_res_source);
            }
            else {
                pstmt6.setNull(7, Types.JAVA_OBJECT);
            }
            if (l_points != null) {
                pstmt6.setDouble(8, l_points);
            }
            else {
                pstmt6.setNull(8, Types.JAVA_OBJECT);
            }
            if (l_srp_code != null) {
                pstmt6.setString(9, l_srp_code);
            }
            else {
                pstmt6.setNull(9, Types.JAVA_OBJECT);
            }
            executeUpdate(pstmt6);
            pstmt6.close();
            // see if current stay is consecutive with another stay, different from the 'max' 

            // that was determined

            if (!in_stay_id.equals(l_link)) {
                // we are consecutive with a the max id found

                PreparedStatement pstmt7 = prepareStatement(
                          "select min(l.id)"
                        + " from recent_stay_list l"
                        + " where l.prop_cd = new get_prop_cd().execute(in_prop_id)"
                        + " and  ||  ||  || ");
                
                ResultSet rs5 = executeQuery(pstmt7);
                rs5.next();
                l_min_id = rs5.getInt(1);
                pstmt7.close();
                rs5.close();
                if (l_min_id != null) {
                    PreparedStatement pstmt8 = prepareStatement(
                              "select nvl(l.linked_id, l.stay_id)"
                            + " from recent_stay_list l"
                            + " where l.id = ?");
                    
                    if (l_min_id != null) {
                        pstmt8.setInt(1, l_min_id);
                    }
                    else {
                        pstmt8.setNull(1, Types.JAVA_OBJECT);
                    }
                    ResultSet rs6 = executeQuery(pstmt8);
                    rs6.next();
                    l_link = rs6.getInt(1);
                    pstmt8.close();
                    rs6.close();
                    PreparedStatement pstmt9 = prepareStatement(
                              "update recent_stay_list"
                            + " set linked_id = ?"
                            + " where stay_id = ?");
                    
                    if (l_link != null) {
                        pstmt9.setInt(1, l_link);
                    }
                    else {
                        pstmt9.setNull(1, Types.JAVA_OBJECT);
                    }
                    if (in_stay_id != null) {
                        pstmt9.setInt(2, in_stay_id);
                    }
                    else {
                        pstmt9.setNull(2, Types.JAVA_OBJECT);
                    }
                    executeUpdate(pstmt9);
                    pstmt9.close();
                    // update stay at hand previously linked to max

                    PreparedStatement pstmt10 = prepareStatement(
                              "update recent_stay_list"
                            + " set linked_id = ?"
                            + " where id = ?");
                    
                    if (l_link != null) {
                        pstmt10.setInt(1, l_link);
                    }
                    else {
                        pstmt10.setNull(1, Types.JAVA_OBJECT);
                    }
                    if (l_id != null) {
                        pstmt10.setInt(2, l_id);
                    }
                    else {
                        pstmt10.setNull(2, Types.JAVA_OBJECT);
                    }
                    executeUpdate(pstmt10);
                    pstmt10.close();
                }
            }
            return l_link;
        }
        catch (SQLException e) {
            sql_error = e.getErrorCode();
            isam_error = 0;
            error_data = e.getMessage();
            {
                throw new ProcedureException(sql_error, isam_error, error_data);
            }
        }
    }

}