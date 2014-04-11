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

public class pf_mb0710 extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public String execute(Integer acct_id, Integer promo_id, String stay_type, Integer prop_id, DateTime doa, Integer los, String rm_type, String srp_code, Double rm_revenue, Double fb_revenue, Double other_revenue, String curr_code, String res_source, Integer p_stay_id) throws SQLException, ProcedureException {
        /*
         * $RCSfile$ -  Booking Source - $Revision: 30558 $
         * 	
         * 	(]$[) $RCSfile$:$Revision: 30558 $ | CDATE=$Date: 2010-08-03 09:18:11 -0700 (Tue, 03 Aug 2010) $ ~
         * 	
         * 	       Copyright (C) 2003 Choice Hotels International, Inc.
         * 	     All Rights Reserved
         * 	
         */
        //set debug file to '/tmp/pf_mb0710.trace';

        String answer;
        DateTime res_date;
        DateTime pb_date;
        answer = "F";
        res_date = null;
        pb_date = null;
        answer = (String) new pf_mobile_booking().execute(acct_id, promo_id, stay_type, prop_id, doa, los, rm_type, srp_code, rm_revenue, fb_revenue, other_revenue, curr_code, res_source, p_stay_id);
        if (answer.equals("F")) {
            return answer;
        }
        PreparedStatement pstmt1 = prepareStatement(
                  "select p.start_date"
                + " from promo p"
                + " where p.promo_id = ?");
        
        if (promo_id != null) {
            pstmt1.setInt(1, promo_id);
        }
        else {
            pstmt1.setNull(1, Types.JAVA_OBJECT);
        }
        ResultSet rs1 = executeQuery(pstmt1);
        rs1.next();
        pb_date = new DateTime(rs1.getTimestamp(1).getTime());
        pstmt1.close();
        rs1.close();
        PreparedStatement pstmt2 = prepareStatement(
                  "select s.reservation_date"
                + " from stay s"
                + " where s.stay_id = ?");
        
        if (p_stay_id != null) {
            pstmt2.setInt(1, p_stay_id);
        }
        else {
            pstmt2.setNull(1, Types.JAVA_OBJECT);
        }
        ResultSet rs2 = executeQuery(pstmt2);
        rs2.next();
        res_date = new DateTime(rs2.getTimestamp(1).getTime());
        pstmt2.close();
        rs2.close();
        if (res_date == null) {
            return "F";
        }
        if ((res_date.isAfter(pb_date) || res_date.isEqual(pb_date)) && (res_date.isBefore(date("9/28/10")) || res_date.isEqual(date("9/28/10")))) {
            answer = "T";
        }
        else {
            return "F";
        }
    }

}