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

public class get_acct_trans_sum extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public Double execute(Integer acct_trans_id) throws SQLException, ProcedureException {
        /*
         * $RCSfile$ - Get the sum of an account transaction - $Revision: 5804 $
         * 
         * (]$[) $RCSfile$:$Revision: 5804 $ | CDATE=$Date: 2004-12-13 04:10:28 -0700 (Mon, 13 Dec 2004) $ ~
         * 
         *       Copyright (C) 2000 Choice Hotels International, Inc.
         * 		    All Rights Reserved
         * 
         *   This procedure sums the detail records for one account transaction and 
         *   returns the amount.
         */
        Double amount;
        amount = 0.0;
        // set debug file to '/tmp/get_acct_trans_sum.trace';

        // trace on;

        // Sum the transaction details to get total amount of transaction

        PreparedStatement pstmt1 = prepareStatement(
                  "select sum(acct_trans_detail.amount)"
                + " from acct_trans_detail"
                + " where acct_trans_detail.acct_trans_id = ?");
        
        if (acct_trans_id != null) {
            pstmt1.setInt(1, acct_trans_id);
        }
        else {
            pstmt1.setNull(1, Types.JAVA_OBJECT);
        }
        ResultSet rs1 = executeQuery(pstmt1);
        rs1.next();
        amount = rs1.getDouble(1);
        pstmt1.close();
        rs1.close();
        return amount;
    }

}