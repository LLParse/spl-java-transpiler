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

public class pf_stay_thresh20 extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public String execute(Integer acct_id, Integer promo_id, String stay_type, Integer prop_id, DateTime doa, Integer los, String rm_type, String srp_code, Double rm_revenue, Double fb_revenue, Double other_revenue, String curr_code, String res_source, Integer stay_id) throws SQLException, ProcedureException {
        /*
         * (]$[) $RCSfile$:$Revision: 9224 $ | CDATE=$Date: 2006-03-09 09:45:20 -0700 (Thu, 09 Mar 2006) $ ~
         * 
         *   Promotion Filter: Customer has more than 20 qualifying stays during the
         *     promotion period
         * 
         *   Copyright 2003, Choice Hotels International, Inc
         */
        DateTime sdate;
        DateTime edate;
        Integer cust_id;
        Integer count_of_stays;
        sdate = null;edate = null;
        cust_id = null;
        //set debug file to '/tmp/pf_stay_thresh20.trace';

        // First get the cust associated with stay

        cust_id = new get_pri_cust_id().execute(acct_id);
        // Obtain start and end dates for this promo and

        PreparedStatement pstmt1 = prepareStatement(
                  "select pm.start_date, pm.stop_date"
                + " from promo pm"
                + " where pm.promo_id = ?");
        
        if (promo_id != null) {
            pstmt1.setInt(1, promo_id);
        }
        else {
            pstmt1.setNull(1, Types.JAVA_OBJECT);
        }
        ResultSet rs1 = executeQuery(pstmt1);
        rs1.next();
        sdate = new DateTime(rs1.getTimestamp(1).getTime());
        edate = new DateTime(rs1.getTimestamp(2).getTime());
        pstmt1.close();
        rs1.close();
        // incoming promo

        // count the stays for the promotion period

        PreparedStatement pstmt2 = prepareStatement(
                  "select count(s.acct_trans_id)"
                + " from stay s, prop p, acct_trans a"
                + " where s.prop_id = p.prop_id"
                + " and s.acct_trans_id = a.acct_trans_id"
                + " and p.chain_id in (\"C\", \"Q\", \"R\", \"Z\")"
                + " and s.stay_type in (\"N\", \"F\")"
                + " and s.cxl_flag = \"N\""
                + " and s.doa + s.los >= ?"
                + " and s.doa <= ?"
                + " and s.rm_revenue_pc > 0"
                + " and s.cust_id = ?"
                + " and a.rev_acct_trans_id is null");
        
        if (sdate != null) {
            pstmt2.setObject(1, sdate);
        }
        else {
            pstmt2.setNull(1, Types.JAVA_OBJECT);
        }
        if (edate != null) {
            pstmt2.setObject(2, edate);
        }
        else {
            pstmt2.setNull(2, Types.JAVA_OBJECT);
        }
        if (cust_id != null) {
            pstmt2.setInt(3, cust_id);
        }
        else {
            pstmt2.setNull(3, Types.JAVA_OBJECT);
        }
        ResultSet rs2 = executeQuery(pstmt2);
        rs2.next();
        count_of_stays = rs2.getInt(1);
        pstmt2.close();
        rs2.close();
        //trace 'count_of_stays = ' || count_of_stays;

        if (count_of_stays > 20) {
            return "T";
        }
        else {
            return "F";
        }
    }

}