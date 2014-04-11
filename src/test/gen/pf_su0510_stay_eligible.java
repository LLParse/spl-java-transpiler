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

public class pf_su0510_stay_eligible extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public String execute(Integer in_acct_id, Integer in_stay_id, Integer in_promo_id, DateTime in_signup_date, String in_recog_cd, Integer in_flag) throws SQLException, ProcedureException {
        /*
         * $Id: pf_su0510_stay_eligible.sql 28454 2010-04-29 22:11:27Z rshepher $
         * 
         *   Description: 
         * 
         *   This procedure is used by the pf_su0510awardbonus procedure to determine if
         *   a stay being considered as part of the bonus calculation meets the promo 
         *   requirements. We are looking at past stays that meet the promo criteria for 
         *   eligibility. The procedure has two functions. One is to simply determine if
         *   an existing stay is eligible based on promo requirements. The other is to also 
         *   determine if the stay has contributed to a previous bonus. This second function
         *   is used so a past stay is not chosen more than one time when rolling up points
         *   for the bonus by querying the acct_trans_contrib table.
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
        Integer l_count;
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
            l_count = 0;
            //set debug file to '/tmp/pf_su0510_stay_eligible.trace';

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
            // platinum or diamond on doa of stay qualifies

            l_isplat = (String) new pf_isplatinum_ondoa().execute(in_acct_id, l_doa);
            if (l_isplat.equals("F")) {
                l_isdiamond = (String) new pf_isdiamond_ondoa().execute(in_acct_id, l_doa);
            }
            if (l_isplat.equals("F") && l_isdiamond.equals("F")) {
                return "F";
            }
            // Platinum or Diamond so now:

            // has the stay already contributed to a previous award bonus? For this

            // we need to look at the acct_trans_contrib table to see if the stay

            // is present for this promo

            if (in_flag.equals(1)) {
                PreparedStatement pstmt2 = prepareStatement(
                          "select count(*)"
                        + " from acct_trans_contrib atc, acct_trans_detail atd"
                        + " where atc.acct_trans_id = atd.acct_trans_id"
                        + " and atc.contrib_stay_id = ?"
                        + " and atd.promo_id = ?");
                
                if (in_stay_id != null) {
                    pstmt2.setInt(1, in_stay_id);
                }
                else {
                    pstmt2.setNull(1, Types.JAVA_OBJECT);
                }
                if (in_promo_id != null) {
                    pstmt2.setInt(2, in_promo_id);
                }
                else {
                    pstmt2.setNull(2, Types.JAVA_OBJECT);
                }
                ResultSet rs2 = executeQuery(pstmt2);
                rs2.next();
                l_count = rs2.getInt(1);
                pstmt2.close();
                rs2.close();
            }
            if (l_count > 0) {
                return "F";
            }
            else {
                return "T";
            }
        }
        catch (SQLException e) {
            sql_error = e.getErrorCode();
            isam_error = 0;
            error_data = e.getMessage();
            {
                PreparedStatement pstmt3 = prepareStatement("drop table qualified_stays_pv_sp10");
                executeUpdate(pstmt3);
                pstmt3.close();
                throw new ProcedureException(sql_error, isam_error, error_data);
            }
        }
    }

}