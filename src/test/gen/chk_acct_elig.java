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

public class chk_acct_elig extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public Integer execute(Integer acct_id, Integer prop_id, DateTime doa, Integer los, String rm_type, String srp_code, Double rm_revenue, Double fb_revenue, Double other_revenue, Integer promo_id, String recog_cd, String curr_cd, Integer appl_group, Integer elite_level_id, String res_source, Integer filter_data) throws SQLException, ProcedureException {
        /*
         * (]$[) $RCSfile$:$Revision: 20840 $ | CDATE=$Date: 2009-02-23 11:16:11 -0700 (Mon, 23 Feb 2009) $ ~
         * 
         *   chk_acct_elig Checks account specific items against the
         *   current promo. Returns 1 on success, 0 on failure.
         *   As with procstay LOCATION and ELIG_TYPE are ignored.
         * 
         * 
         *       Copyright (C) 2003 Choice Hotels International, Inc.
         *                       All Rights Reserved
         */
        String answer;
        Integer filter_id;
        String filter;
        Integer level;
        Integer group_id;
        Integer offer_id;
        Integer offer_count;
        // Look at all promo_mbr_elig records which match current promo and

        // that meet elite level and appl_groups if present in promo.

        answer = "N";
        PreparedStatement pstmt1 = prepareStatement(
                  "select promo_acct_elig.filter_id, promo_acct_elig.elite_level_id, promo_acct_elig.appl_group_id, promo_acct_elig.offer_id"
                + " from promo_acct_elig"
                + " where promo_acct_elig.promo_id = ?");
        
        if (promo_id != null) {
            pstmt1.setInt(1, promo_id);
        }
        else {
            pstmt1.setNull(1, Types.JAVA_OBJECT);
        }
        ResultSet rs1 = executeQuery(pstmt1);
        while (rs1.next()) {
            filter_id = rs1.getInt(1);
            level = rs1.getInt(2);
            group_id = rs1.getInt(3);
            offer_id = rs1.getInt(4);
            if (level != null) {
                if (elite_level_id != null) {
                    if (!level.equals(elite_level_id)) {
                        continue;
                    }
                }
                else {
                    continue;
                }
            }
            if (group_id != null) {
                if (appl_group != null) {
                    if (!appl_group.equals(group_id)) {
                        continue;
                    }
                }
                else {
                    continue;
                }
            }
            if (offer_id != null) {
                PreparedStatement pstmt2 = prepareStatement(
                          "select count(*)"
                        + " from acct_offer"
                        + " where acct_offer.offer_id = ?"
                        + " and acct_offer.acct_id = ?");
                
                if (offer_id != null) {
                    pstmt2.setInt(1, offer_id);
                }
                else {
                    pstmt2.setNull(1, Types.JAVA_OBJECT);
                }
                if (acct_id != null) {
                    pstmt2.setInt(2, acct_id);
                }
                else {
                    pstmt2.setNull(2, Types.JAVA_OBJECT);
                }
                ResultSet rs2 = executeQuery(pstmt2);
                rs2.next();
                offer_count = rs2.getInt(1);
                pstmt2.close();
                rs2.close();
                // Check now that they are entitled to it, i.e. have been offered.

                if (offer_count.equals(0)) {
                    continue;
                }
            }
            PreparedStatement pstmt3 = prepareStatement(
                      "select promo_filter.name"
                    + " from promo_filter"
                    + " where promo_filter.filter_id = ?");
            
            if (filter_id != null) {
                pstmt3.setInt(1, filter_id);
            }
            else {
                pstmt3.setNull(1, Types.JAVA_OBJECT);
            }
            ResultSet rs3 = executeQuery(pstmt3);
            rs3.next();
            filter = rs3.getString(1);
            pstmt3.close();
            rs3.close();
            if (filter != null) {
                // look up that procedure and run the thing.

                answer = new check_filter().execute(filter_id, acct_id, promo_id, "F", prop_id, doa, los, rm_type, srp_code, curr_cd, rm_revenue, fb_revenue, other_revenue, res_source, filter_data);
                if (!answer.equals("T")) {
                    continue;
                }
            }
            // end if looking at filters

            // to make it here is to satisfy  one mbr_elig rec for the promo.

            return 1;
        }
        pstmt1.close();
        rs1.close();
        // looked at all the records and none worked.

        return 0;
    }

}