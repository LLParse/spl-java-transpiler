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

public class chk_pri_cust_id extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public Integer execute(Integer acct_id) throws SQLException, ProcedureException {
        /*
         * $RCSfile$ - Get the primary cust_id from account ID- $Revision: 5804 $
         * 
         * (]$[) $RCSfile$:$Revision: 5804 $ | CDATE=$Date: 2004-12-13 04:10:28 -0700 (Mon, 13 Dec 2004) $ ~
         * 
         *       Copyright (C) 2001 Choice Hotels International, Inc.
         *                       All Rights Reserved
         * 
         * This procedure checks the existence of a customer record given an acct_id. 
         * Returns the cust_id or null if not found.
         * 
         * Note: This is an inquiry function. If the customer must or should exist, 
         *       use get_pri_cust_id which will raise exception on error.
         */
        Integer cust_id;
        cust_id = null;
        PreparedStatement pstmt1 = prepareStatement(
                  "select cust_acct.cust_id"
                + " from cust_acct"
                + " where cust_acct.acct_id = ?"
                + " and cust_acct.primary_cust = \"Y\"");
        
        if (acct_id != null) {
            pstmt1.setInt(1, acct_id);
        }
        else {
            pstmt1.setNull(1, Types.JAVA_OBJECT);
        }
        ResultSet rs1 = executeQuery(pstmt1);
        rs1.next();
        cust_id = rs1.getInt(1);
        pstmt1.close();
        rs1.close();
        return cust_id;
    }

}