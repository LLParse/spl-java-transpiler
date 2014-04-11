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

public class trans_has_bonus extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public String execute(Integer acct_trans_id) throws SQLException, ProcedureException {
        /*
         * trans_has_bonus.sql - Determine if transaction has bonus detail item
         * 
         * (]$[) $RCSfile$:$Revision: 5804 $ | CDATE=$Date: 2004-12-13 04:10:28 -0700 (Mon, 13 Dec 2004) $ ~
         * 
         * 
         *       Copyright (C) 2001 Choice Hotels International, Inc.
         * 		    All Rights Reserved
         * 
         *   This procedure checks if a transaction has a promotion associated that is
         *   not of type 'A'lways.
         */
        Integer has_bonus;
        has_bonus = null;
        // set debug file to '/tmp/trans_has_bonus.trace';

        // trace on;

        PreparedStatement pstmt1 = prepareStatement(
                  "select count(*)"
                + " from acct_trans_detail, promo"
                + " where acct_trans_detail.acct_trans_id = ?"
                + " and promo.promo_id = acct_trans_detail.promo_id"
                + " and promo.rule != \"A\"");
        
        if (acct_trans_id != null) {
            pstmt1.setInt(1, acct_trans_id);
        }
        else {
            pstmt1.setNull(1, Types.JAVA_OBJECT);
        }
        ResultSet rs1 = executeQuery(pstmt1);
        rs1.next();
        has_bonus = rs1.getInt(1);
        pstmt1.close();
        rs1.close();
        if (has_bonus > 0) {
            return "Y";
        }
        return "N";
    }

}