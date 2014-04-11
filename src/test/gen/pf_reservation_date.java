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

public class pf_reservation_date extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public String execute(Integer acct_id, Integer promo_id, String stay_type, Integer prop_id, DateTime doa, Integer los, String rm_type, String srp_code, Double rm_revenue, Double fb_revenue, Double other_revenue, String curr_code, String res_source, Integer stay_id) throws SQLException, ProcedureException {
        /*
         * 	pf_reservation_date.sql	- Check that reservation date is null or within promo date range
         * 	(]$[) $RCSfile$:$Revision: 11644 $ | CDATE=$Date: 2007-04-30 16:35:46 -0700 (Mon, 30 Apr 2007) $ ~
         */
        DateTime res_date;
        DateTime pb_date;
        DateTime pe_date;
        res_date = null;
        pb_date = null;
        pe_date = null;
        PreparedStatement pstmt1 = prepareStatement(
                  "select p.start_date, p.stop_date"
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
        pe_date = new DateTime(rs1.getTimestamp(2).getTime());
        pstmt1.close();
        rs1.close();
        PreparedStatement pstmt2 = prepareStatement(
                  "select s.reservation_date"
                + " from stay s"
                + " where s.stay_id = ?");
        
        if (stay_id != null) {
            pstmt2.setInt(1, stay_id);
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
            return "T";
        }
        if ((res_date.isAfter(pb_date) || res_date.isEqual(pb_date)) && (res_date.isBefore(pe_date) || res_date.isEqual(pe_date))) {
            return "T";
        }
        return "F";
    }

}