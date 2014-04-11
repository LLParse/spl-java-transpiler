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

public class pf_find_consecutive_economy extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public Collection<Object> execute(Integer in_acct_id, Integer in_stay_id, DateTime in_doa, Integer in_link, String in_offer_cd) throws SQLException, ProcedureException {
        /*
         * $Id: pf_find_consecutive_economy.sql 47525 2012-01-25 18:51:02Z rshepher $
         * 
         *   Description: 
         * 
         *   See if there is another economy stay that is consecutive with the stay at hand that would
         *   make it eligible for the 2 night minimum rule. In addition to finding a consecutive stay,
         *   we also need to check to see if that stay meets the other eligibility rules; i.e. booking 
         *   source, registration. Seems like we need to return two values; "exists" and "meets rules". 
         *   Eligible here means meets booking source and registration prior to DOA rules.
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
        String l_debug;
        Integer l_stay_id;
        DateTime l_doa;
        DateTime l_dod;
        String l_res_source;
        Integer l_acct_trans_id;
        Integer l_ord;
        Integer l_rev_acct_trans_id;
        DateTime l_offer_dtime;
        String l_booking_rules;
        String l_consec_found;
        String l_rules_met;
        Integer sql_error;
        Integer isam_error;
        String error_data;
        try {
            l_debug = "F";
            l_stay_id = null;
            l_doa = null;
            l_dod = null;
            l_res_source = null;
            l_acct_trans_id = null;
            l_ord = null;
            l_rev_acct_trans_id = null;
            l_offer_dtime = null;
            l_booking_rules = "F";
            l_consec_found = "F";
            l_rules_met = "F";
            PreparedStatement pstmt1 = prepareStatement(
                      "select stay_id, doa, dod, res_source, acct_trans_id"
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
                l_res_source = rs1.getString(4);
                l_acct_trans_id = rs1.getInt(5);
                // multi room check for room with LOS > 1

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
                    // found a consecutive, does it meet the booking rules?

                    l_consec_found = "T";
                    l_booking_rules = (String) new pf_std_booking_rules().execute(in_acct_id, l_doa, l_dod.minusDays(l_doa), l_res_source);
                    if (l_booking_rules.equals("T")) {
                        // does it meet the registration rule?

                        PreparedStatement pstmt3 = prepareStatement(
                                  "select ao.offer_dtime"
                                + " from acct_offer ao, offer o"
                                + " where ao.offer_id = o.offer_id"
                                + " and ao.acct_id = ?"
                                + " and o.offer_cd = trim(?)");
                        
                        if (in_acct_id != null) {
                            pstmt3.setInt(1, in_acct_id);
                        }
                        else {
                            pstmt3.setNull(1, Types.JAVA_OBJECT);
                        }
                        if (in_offer_cd != null) {
                            pstmt3.setString(2, in_offer_cd);
                        }
                        else {
                            pstmt3.setNull(2, Types.JAVA_OBJECT);
                        }
                        ResultSet rs3 = executeQuery(pstmt3);
                        rs3.next();
                        l_offer_dtime = new DateTime(rs3.getTimestamp(1).getTime());
                        pstmt3.close();
                        rs3.close();
                        if ((l_offer_dtime.isBefore(l_doa) || l_offer_dtime.isEqual(l_doa))) {
                            l_rules_met = "T";
                            break;
                        }
                    }
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