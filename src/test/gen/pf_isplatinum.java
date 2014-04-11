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

public class pf_isplatinum extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public String execute(Integer in_acct_id, Integer in_promo_id, String in_stay_type, Integer in_prop_id, DateTime in_doa, Integer in_los, String in_rm_type, String in_srp_code, Double in_rm_revenue, Double in_fb_revenue, Double in_other_revenue, String in_curr_code, String in_res_source, Integer in_stay_id) throws SQLException, ProcedureException {
        /*
         * 
pf_isplatinum.sql - Check to see if account was Platinum on DOA

(]$[) $RCSfile$:$Revision: $ | CDATE=$Date:  $ ~

       Copyright (C) 2008 Choice Hotels International, Inc.
     All Rights Reserved

         */
        String l_name;
        String l_result;
        Integer l_id;
        Integer l_count;
        l_name = null;
        l_result = "F";
        // assume not Platinum

        l_id = null;
        l_count = 0;
        //set debug file to '/tmp/pf_isplatinum.trace';

        //trace on;

        PreparedStatement pstmt1 = prepareStatement(
                  "select elvl.name, elvl.elite_level_id"
                + " from acct_elite_level_hist aelh, elite_level elvl"
                + " where aelh.acct_id = ?"
                + " and aelh.elite_level_id = elvl.elite_level_id"
                + " and aelh.acct_elite_level_hist_id = 
                    + "select max(els.acct_elite_level_hist_id)"
                    + " from acct_elite_level_hist els"
                    + "  inner join elite_level es on els.elite_level_id = es.elite_level_id"
                    + " where els.acct_id = ?"
                    + " and els.date_acquired <= ?"
                    + " and year(es.eff_year) = year(?)");
            
            if (in_acct_id != null) {
                pstmt1.setInt(1, in_acct_id);
            }
            else {
                pstmt1.setNull(1, Types.JAVA_OBJECT);
            }
            if (in_acct_id != null) {
                pstmt1.setInt(2, in_acct_id);
            }
            else {
                pstmt1.setNull(2, Types.JAVA_OBJECT);
            }
            if (in_doa != null) {
                pstmt1.setObject(3, in_doa);
            }
            else {
                pstmt1.setNull(3, Types.JAVA_OBJECT);
            }
            if (in_doa != null) {
                pstmt1.setObject(4, in_doa);
            }
            else {
                pstmt1.setNull(4, Types.JAVA_OBJECT);
            }
            ResultSet rs1 = executeQuery(pstmt1);
            rs1.next();
            l_name = rs1.getString(1);
            l_id = rs1.getInt(2);
            rs1.close();
            // DE191: case where acct_elite_level_hist has Platinum entry but acct_elite_level does 

            // not since they were downgraded to nothing. So, check if they currently have the elite level

            // in acct_elite_level

            // DE559: update code for DE191 such that current elite level does not have to equal the history elite level.

            // As long as there IS a current elite level for current year, then award them Platinum. There is a dilema here; stay

            // bumps them to next elite level (processed on DOA+x number of days. Then, another stay comes in

            // at DOA+(x-1), for example. From the history, they are still Platinum, but when being processed, they

            // are technically at the next elite level. We will go with the elite level at DOA, and so they will get

            // another Platinum. That's how it would have worked prior to DE191 fix.

            if (l_name.equals("Platinum")) {
                PreparedStatement pstmt2 = prepareStatement(
                          "select count(*)"
                        + " from acct_elite_level ael, elite_level el"
                        + " where ael.elite_level_id = el.elite_level_id"
                        + " and acct_id = ?"
                        + " and year(?) = year(el.eff_year)");
                
                if (in_acct_id != null) {
                    pstmt2.setInt(1, in_acct_id);
                }
                else {
                    pstmt2.setNull(1, Types.JAVA_OBJECT);
                }
                if (in_doa != null) {
                    pstmt2.setObject(2, in_doa);
                }
                else {
                    pstmt2.setNull(2, Types.JAVA_OBJECT);
                }
                ResultSet rs2 = executeQuery(pstmt2);
                rs2.next();
                l_count = rs2.getInt(1);
                pstmt2.close();
                rs2.close();
                if (l_count.equals(0)) {
                    return l_result;
                }
            }
            // passed elite level check, now check mutli-room

            if (l_name.equals("Platinum")) {
                l_result = (String) new pf_chk_promo_award().execute(in_acct_id, in_promo_id, in_stay_type, in_prop_id, in_doa, in_los, in_rm_type, in_srp_code, in_rm_revenue, in_fb_revenue, in_other_revenue, in_curr_code, in_res_source, in_stay_id);
            }
            return l_result;
        }
    
    }