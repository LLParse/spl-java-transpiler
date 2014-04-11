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

public class pf_complete_stay_by_promo_end extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public String execute(Integer acct_id, Integer promo_id, String stay_type, Integer prop_id, DateTime doa, Integer los, String rm_type, String srp_code, Double rm_revenue, Double fb_revenue, Double other_revenue, String curr_code, String res_source, Integer stay_id) throws SQLException, ProcedureException {
        /*
         * 	pf_complete_stay_by_promo_end.sql 	Checks that date of departure occurs before promo ends.
         * 
         *     Copyright (C) 2005 Choice Hotels International, Inc.
         *     (]$[) $RCSfile$:$Revision: 9224 $ | CDATE=$Date: 2006-03-09 09:45:20 -0700 (Thu, 09 Mar 2006) $ ~
         */
        DateTime end_date;
        end_date = null;
        PreparedStatement pstmt1 = prepareStatement(
                  "select stop_date"
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
        end_date = new DateTime(rs1.getTimestamp(1).getTime());
        pstmt1.close();
        rs1.close();
        if (doa.plusDays(los) > end_date) {
            return "F";
        }
        else {
            return "T";
        }
    }

}