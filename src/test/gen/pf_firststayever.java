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

public class pf_firststayever extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public String execute(Integer acct_id, Integer promo_id, String stay_type, Integer prop_id, DateTime doa, Integer los, String rm_type, String srp_code, Double rm_revenue, Double fb_revenue, Double other_revenue, String curr_code, String res_source, Integer stay_id) throws SQLException, ProcedureException {
        /*
         * 	(]$[) $RCSfile$:$Revision: 10832 $ | CDATE=$Date: 2006-12-12 16:37:23 -0700 (Tue, 12 Dec 2006) $ ~
         * 
         * 	determine whether the member has stayed ever before?
         * 
         *        Copyright (C) 2000 Choice Hotels International, Inc.
         */
        Integer stay_count;
        stay_count = null;
        PreparedStatement pstmt1 = prepareStatement(
                  "select count(*)"
                + " from acct_trans a, stay s, acct_trans_detail ad"
                + " where a.stay_id = s.stay_id"
                + " and a.trans_type = \"S\""
                + " and a.acct_trans_id = ad.acct_trans_id"
                + " and ad.amount != 0"
                + " and a.rev_acct_trans_id is null"
                + " and s.stay_type in (\"N\", \"F\")"
                + " and s.stay_status = \"S\""
                + " and a.acct_id = ?");
        
        if (acct_id != null) {
            pstmt1.setInt(1, acct_id);
        }
        else {
            pstmt1.setNull(1, Types.JAVA_OBJECT);
        }
        ResultSet rs1 = executeQuery(pstmt1);
        rs1.next();
        stay_count = rs1.getInt(1);
        pstmt1.close();
        rs1.close();
        if (stay_count > 0) {
            return "F";
        }
        else {
            return "T";
        }
    }

}