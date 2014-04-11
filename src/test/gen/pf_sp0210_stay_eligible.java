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

public class pf_sp0210_stay_eligible extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public String execute(Integer in_acct_id, Integer in_stay_id, Integer in_promo_id, DateTime in_signup_date, String in_recog_cd) throws SQLException, ProcedureException {
        /*
         * $Id: pf_sp0210_stay_eligible.sql 25075 2010-01-15 22:32:02Z rshepher $
         * 
         *   Description: 
         * 
         *   This procedure is used by the pf_sp0210awardbonus procedure to determine if
         *   a stay being considered as part of the bonus calculation meets the promo 
         *   requirements. It is really a combination of the initial qualification logic of the
         *   pf_sp0210booking and pf_sp0210register filters, but we are looking at past stays
         *   that meet the promo criteria for eligibility.
         *   
         *       Copyright (C) 2010 Choice Hotels International, Inc.
         *                       All Rights Reserved
         */
        String l_res_source;
        DateTime l_doa;
        String l_isplat;
        String l_isdiamond;
        Integer l_offer_id;
        DateTime l_offer_date;
        Integer sql_error;
        Integer isam_error;
        String error_data;
        try {
            l_res_source = null;
            l_doa = null;
            l_isplat = null;
            l_isdiamond = null;
            l_offer_id = null;
            l_offer_date = null;
            //set debug file to '/tmp/pf_sp0210_stay_eligible.trace';

            //trace on;

            PreparedStatement pstmt1 = prepareStatement(
                      "select res_source, doa"
                    + " from stay"
                    + " where stay_id = ?");
            
            if (in_stay_id != null) {
                pstmt1.setInt(1, in_stay_id);
            }
            else {
                pstmt1.setNull(1, Types.JAVA_OBJECT);
            }
            ResultSet rs1 = executeQuery(pstmt1);
            rs1.next();
            l_res_source = rs1.getString(1);
            l_doa = new DateTime(rs1.getTimestamp(2).getTime());
            pstmt1.close();
            rs1.close();
            // central booking qualifies

            if (l_res_source != null && (l_res_source.equals("C") || l_res_source.equals("N"))) {
                return "T";
            }
            // enrollment stay qualifies

            if (l_doa.isEqual(in_signup_date)) {
                return "T";
            }
            // platinum or diamond on doa of stay qualifies if registered

            l_isplat = (String) new pf_isplatinum_ondoa().execute(in_acct_id, l_doa);
            if (l_isplat.equals("F")) {
                l_isdiamond = (String) new pf_isdiamond_ondoa().execute(in_acct_id, l_doa);
            }
            if (l_isplat.equals("F") && l_isdiamond.equals("F")) {
                return "F";
            }
            // plat or diamond, see if registered

            // get the current offer_id to be used in later checking. Should only find one.

            PreparedStatement pstmt2 = prepareStatement(
                      "select pae.offer_id"
                    + " from promo_acct_elig pae"
                    + " where pae.promo_id = ?"
                    + " and pae.recog_cd = ?"
                    + " and pae.offer_id is not null");
            
            if (in_promo_id != null) {
                pstmt2.setInt(1, in_promo_id);
            }
            else {
                pstmt2.setNull(1, Types.JAVA_OBJECT);
            }
            if (in_recog_cd != null) {
                pstmt2.setString(2, in_recog_cd);
            }
            else {
                pstmt2.setNull(2, Types.JAVA_OBJECT);
            }
            ResultSet rs2 = executeQuery(pstmt2);
            rs2.next();
            l_offer_id = rs2.getInt(1);
            pstmt2.close();
            rs2.close();
            if (l_offer_id == null) {
                throw new ProcedureException(-746, 0, "pf_sp0210stay_elig: no offer found for Spring 2010 promo");
            }
            PreparedStatement pstmt3 = prepareStatement(
                      "select date(ao.offer_dtime)"
                    + " from acct_offer ao"
                    + " where ao.acct_id = ?"
                    + " and ao.offer_id = ?");
            
            if (in_acct_id != null) {
                pstmt3.setInt(1, in_acct_id);
            }
            else {
                pstmt3.setNull(1, Types.JAVA_OBJECT);
            }
            if (l_offer_id != null) {
                pstmt3.setInt(2, l_offer_id);
            }
            else {
                pstmt3.setNull(2, Types.JAVA_OBJECT);
            }
            ResultSet rs3 = executeQuery(pstmt3);
            rs3.next();
            l_offer_date = new DateTime(rs3.getTimestamp(1).getTime());
            pstmt3.close();
            rs3.close();
            // check if they registered for this offer before the stay

            if (l_offer_date != null && (l_offer_date.isBefore(l_doa) || l_offer_date.isEqual(l_doa))) {
                return "T";
            }
            return "F";
        }
        catch (SQLException e) {
            sql_error = e.getErrorCode();
            isam_error = 0;
            error_data = e.getMessage();
            {
                PreparedStatement pstmt4 = prepareStatement("drop table qualified_stays_pv_sp10");
                executeUpdate(pstmt4);
                pstmt4.close();
                throw new ProcedureException(sql_error, isam_error, error_data);
            }
        }
    }

}