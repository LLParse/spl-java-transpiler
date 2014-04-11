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

public class pf_find_consecutive_economy_norules extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public Collection<Object> execute(Integer in_acct_id, Integer in_stay_id, DateTime in_doa, Integer in_link, String in_offer_cd) throws SQLException, ProcedureException {
        /*
         * $Id: pf_find_consecutive_economy_norules.sql 52644 2012-06-25 23:47:41Z rick_shepherd $
         * 
         *   Description: 
         * 
         *   See if there is another economy stay that is consecutive with the stay at hand that would
         *   make it eligible for the 2 night minimum rule. No other rules qualifications are required
         *   for this procedure.
         *   
         *   If it exists but is not eligible, the stay at hand may be eligible which makes it a valid
         *   economy stay. If it exists and is eligible, then it does not matter if the stay at hand
         *   is eligible; the stay as a whole is eligible.
         *   
         *   no consecutive stays found returns: false, false
         *   consecutive stay(s) found, booking/registration rules met returns: true, true
         *   consecutive stay(s) found, booking/registration rules not met return: true, false
         *   
         *   TODO: if the found stay is the same DOA (multi-room) but its length of stay is more than one
         *         night, do we consider that as meeting the 2 night minimum rule?
         *   
         *       Copyright (C) 2012 Choice Hotels International, Inc.
         *                       All Rights Reserved
         */
        Integer l_stay_id;
        DateTime l_doa;
        DateTime l_dod;
        Integer l_acct_trans_id;
        Integer l_ord;
        Integer l_rev_acct_trans_id;
        String l_consec_found;
        String l_rules_met;
        Integer sql_error;
        Integer isam_error;
        String error_data;
        try {
            l_stay_id = null;
            l_doa = null;
            l_dod = null;
            l_acct_trans_id = null;
            l_ord = null;
            l_rev_acct_trans_id = null;
            l_consec_found = "F";
            l_rules_met = "T";
            // are no rules but we keep it here for no good reason

            PreparedStatement pstmt1 = prepareStatement(
                      "select stay_id, doa, dod, acct_trans_id"
                    + " from recent_stay_list"
                    + " where linked_id = ?"
                    + " and stay_id != ?");
            
            if (in_link != null) {
                pstmt1.setInt(1, in_link);
            }
            else {
                pstmt1.setNull(1, Types.JAVA_OBJECT);
            }
            if (in_stay_id != null) {
                pstmt1.setInt(2, in_stay_id);
            }
            else {
                pstmt1.setNull(2, Types.JAVA_OBJECT);
            }
            ResultSet rs1 = executeQuery(pstmt1);
            while (rs1.next()) {
                l_stay_id = rs1.getInt(1);
                l_doa = new DateTime(rs1.getTimestamp(2).getTime());
                l_dod = new DateTime(rs1.getTimestamp(3).getTime());
                l_acct_trans_id = rs1.getInt(4);
                // multi room check for room with LOS > 1, does that make it qualify?

                if (l_doa.isEqual(in_doa)) {
                    if (l_dod.minusDays(l_doa) > 1) {
                        l_consec_found = "T";
                    }
                    else {
                        continue;
                    }
                }
                // ensure it has not been reversed

                PreparedStatement pstmt2 = prepareStatement(
                          "select rev_acct_trans_id"
                        + " from acct_trans"
                        + " where acct_trans_id = ?");
                
                if (l_acct_trans_id != null) {
                    pstmt2.setInt(1, l_acct_trans_id);
                }
                else {
                    pstmt2.setNull(1, Types.JAVA_OBJECT);
                }
                ResultSet rs2 = executeQuery(pstmt2);
                rs2.next();
                l_rev_acct_trans_id = rs2.getInt(1);
                pstmt2.close();
                rs2.close();
                if (l_rev_acct_trans_id == null) {
                    // found a consecutive

                    l_consec_found = "T";
                    break;
                }
            }
            pstmt1.close();
            rs1.close();
            return new ArrayList<Object>(Arrays.<Object>asList(l_consec_found, l_rules_met));
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