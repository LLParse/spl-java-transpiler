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

public class pf_valid_referral_email extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public String execute(Integer acct_id, Integer promo_id, String stay_type, Integer prop_id, DateTime doa, Integer los, String rm_type, String srp_code, Double rm_revenue, Double fb_revenue, Double other_revenue, String curr_code, String res_source, Integer raf_reg_id) throws SQLException, ProcedureException {
        /*
         * Determine whether or not the referred member has a valid email address.
         *    
         *           
         * $Id: pf_valid_referral_email.sql 21367 2009-03-24 23:16:36Z jgardner $
         *    
         *        Copyright (C) 2009 Choice Hotels International, Inc.
         */
        Integer referred_acct_id;
        Integer email_stat;
        String answer;
        referred_acct_id = null;
        email_stat = 0;
        answer = null;
        if (raf_reg_id == null) {
            return "F";
        }
        // Lookup the referral registration

        PreparedStatement pstmt1 = prepareStatement(
                  "select raf_reg.referred_acct_id"
                + " from raf_reg"
                + " where raf_reg.raf_reg_id = ?");
        
        if (raf_reg_id != null) {
            pstmt1.setInt(1, raf_reg_id);
        }
        else {
            pstmt1.setNull(1, Types.JAVA_OBJECT);
        }
        ResultSet rs1 = executeQuery(pstmt1);
        rs1.next();
        referred_acct_id = rs1.getInt(1);
        pstmt1.close();
        rs1.close();
        if (referred_acct_id == null) {
            return "F";
        }
        PreparedStatement pstmt2 = prepareStatement(
                  "select 1"
                + " from cust_email"
                + " where length(email_addr) > 0"
                + " and undeliverable = \"N\""
                + " and cust_id = new get_pri_cust_id().execute(referred_acct_id)");
        
        ResultSet rs2 = executeQuery(pstmt2);
        rs2.next();
        email_stat = rs2.getInt(1);
        pstmt2.close();
        rs2.close();
        if (email_stat.equals(1)) {
            return "T";
        }
        else {
            return "F";
        }
    }

}