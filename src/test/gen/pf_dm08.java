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

public class pf_dm08 extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public String execute(Integer acct_id, Integer promo_id, String stay_type, Integer prop_id, DateTime doa, Integer los, String rm_type, String srp_code, Double rm_revenue, Double fb_revenue, Double other_revenue, String curr_code, String res_source, Integer stay_id) throws SQLException, ProcedureException {
        /*
         * 	pf_dm08.sql	- Check that acct_offer date is before date of reservation but not
         *     more than 48 hours after. 
         * 	(]$[) $RCSfile$:$Revision: 17586 $ | CDATE=$Date: 2008-05-28 10:44:41 -0700 (Wed, 28 May 2008) $ ~
         */
        DateTime offer_date;
        DateTime res_date;
        offer_date = null;
        res_date = null;
        //set debug file to '/tmp/pf_dm08.trace';

        //trace on;

        // get the reservation date

        PreparedStatement pstmt1 = prepareStatement(
                  "select reservation_date"
                + " from stay"
                + " where stay.stay_id = ?");
        
        if (stay_id != null) {
            pstmt1.setInt(1, stay_id);
        }
        else {
            pstmt1.setNull(1, Types.JAVA_OBJECT);
        }
        ResultSet rs1 = executeQuery(pstmt1);
        rs1.next();
        res_date = new DateTime(rs1.getTimestamp(1).getTime());
        pstmt1.close();
        rs1.close();
        if (res_date == null) {
            return "F";
        }
        // there are potentially multiple offers so just pick

        // the earliest one

        PreparedStatement pstmt2 = prepareStatement(
                  "select date(min(a.offer_dtime))"
                + " from promo_acct_elig p, acct_offer a"
                + " where p.promo_id = ?"
                + " and a.acct_id = ?"
                + " and p.offer_id = a.offer_id");
        
        if (promo_id != null) {
            pstmt2.setInt(1, promo_id);
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
        offer_date = new DateTime(rs2.getTimestamp(1).getTime());
        pstmt2.close();
        rs2.close();
        if (offer_date != null) {
            if (offer_date.isAfter(res_date)) {
                return "F";
            }
            if (offer_date.plusHours(48) < res_date) {
                return "F";
            }
        }
        return "T";
    }

}