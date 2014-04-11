/* Generated on 03-01-2013 12:10:56 PM by SPLParser v0.9 */
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

public class chk_promo_use_cnt extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public Integer execute(Integer promo_id, Integer acct_id) throws SQLException, ProcedureException {
        /*
         * (]$[) $RCSfile$:$Revision: 5804 $ | CDATE=$Date: 2004-12-13 04:10:28 -0700 (Mon, 13 Dec 2004) $ ~
         *   chk_promo_use_cnt counts the number of times a promo
         *   has been used, ignoring promotion usage that has since
         *   been reversed.
         * 
         * 
         * 	  Copyright (C) 2003 Choice Hotels International, Inc.
         * 			All Rights Reserved
         */
        Integer count;
        count = 0;
        PreparedStatement pstmt1 = prepareStatement(
                  "select count(*)"
                + " from acct_trans_detail ad, acct_trans a"
                + " where a.acct_id = ?"
                + " and a.acct_trans_id = ad.acct_trans_id"
                + " and a.rev_acct_trans_id is null"
                + " and a.rsub_acct_trans_id is null"
                + " and ad.promo_id = ?");
        
        if (acct_id != null) {
            pstmt1.setInt(1, acct_id);
        }
        else {
            pstmt1.setNull(1, Types.JAVA_OBJECT);
        }
        if (promo_id != null) {
            pstmt1.setInt(2, promo_id);
        }
        else {
            pstmt1.setNull(2, Types.JAVA_OBJECT);
        }
        ResultSet rs1 = executeQuery(pstmt1);
        rs1.next();
        count = rs1.getInt(1);
        pstmt1.close();
        rs1.close();
        return count;
    }

}