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

public class pf_su0510 extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public String execute(Integer in_acct_id, Integer in_promo_id, String in_stay_type, Integer in_prop_id, DateTime in_doa, Integer in_los, String in_rm_type, String in_srp_code, Double in_rm_revenue, Double in_fb_revenue, Double in_other_revenue, String in_curr_code, String in_res_source, Integer in_stay_id) throws SQLException, ProcedureException {
        /*
         * pf_su0510.sql - Check if the stay was booked via ch.com or central reservations
         *                 or was an enrollment stay or is a Platinum/Diamond elite level. 
         *                 If so, then call the routine to check if a bonus should be awarded.
         *    
         * $Id: pf_su0510.sql 27972 2010-04-16 23:29:13Z rshepher $
         * 
         *       Copyright (C) 2010 Choice Hotels International, Inc.
         */
        String l_answer;
        DateTime l_start_date;
        DateTime l_stop_date;
        String l_isenrollstay;
        String l_platinum;
        String l_diamond;
        l_answer = null;
        l_start_date = null;
        l_stop_date = null;
        l_isenrollstay = null;
        l_platinum = "F";
        l_diamond = "F";
        //set debug file to '/tmp/pf_su0510.trace';

        //trace on;

        // Check for valid doa

        PreparedStatement pstmt1 = prepareStatement(
                  "select promo.start_date, promo.stop_date"
                + " from promo"
                + " where promo.promo_id = ?");
        
        if (in_promo_id != null) {
            pstmt1.setInt(1, in_promo_id);
        }
        else {
            pstmt1.setNull(1, Types.JAVA_OBJECT);
        }
        ResultSet rs1 = executeQuery(pstmt1);
        rs1.next();
        l_start_date = new DateTime(rs1.getTimestamp(1).getTime());
        l_stop_date = new DateTime(rs1.getTimestamp(2).getTime());
        pstmt1.close();
        rs1.close();
        if (in_doa.isBefore(l_start_date) || in_doa.isAfter(l_stop_date)) {
            return "F";
        }
        l_isenrollstay = (String) new pf_isenrollstay().execute(in_acct_id, in_doa);
        l_platinum = (String) new pf_isplatinum_ondoa().execute(in_acct_id, in_doa);
        if (l_platinum.equals("F")) {
            l_diamond = (String) new pf_isdiamond_ondoa().execute(in_acct_id, in_doa);
        }
        // If enrollment stay, Plat/Diamond, or internet/central res booking

        // then continue to see if bonus should be given.

        if (l_isenrollstay.equals("F")) {
            if (l_platinum.equals("F") && l_diamond.equals("F")) {
                if (in_res_source == null || (!in_res_source.equals("C") && !in_res_source.equals("N"))) {
                    return "F";
                }
            }
        }
        // Got an eligible stay, check if bonus should be awarded

        l_answer = (String) new pf_su0510awardbonus().execute(in_acct_id, in_promo_id, in_stay_type, in_prop_id, in_doa, in_los, in_rm_type, in_srp_code, in_rm_revenue, in_fb_revenue, in_other_revenue, in_curr_code, in_res_source, in_stay_id);
        return l_answer;
    }

}