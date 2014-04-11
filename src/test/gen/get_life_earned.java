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

public class get_life_earned extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public Integer execute(Integer acct_id) throws SQLException, ProcedureException {
        /*
         * $RCSfile$ - Get the lifetime balance of an account - $Revision: 5804 $
         * 
         * (]$[) $RCSfile$:$Revision: 5804 $ | CDATE=$Date: 2004-12-13 04:10:28 -0700 (Mon, 13 Dec 2004) $ ~
         * 
         *       Copyright (C) 2003 Choice Hotels International, Inc.
         * 		    All Rights Reserved
         * 
         *   Retrieve the account balance for a given account.
         */
        Integer last_stmnt_id;
        Integer balance;
        last_stmnt_id = null;
        balance = null;
        // set debug file to '/tmp/get_life_earned.trace';

        // trace on;

        PreparedStatement pstmt1 = prepareStatement(
                  "select max(s.stmnt_id)"
                + " from acct_stmnt s"
                + " where s.acct_id = ?");
        
        if (acct_id != null) {
            pstmt1.setInt(1, acct_id);
        }
        else {
            pstmt1.setNull(1, Types.JAVA_OBJECT);
        }
        ResultSet rs1 = executeQuery(pstmt1);
        rs1.next();
        last_stmnt_id = rs1.getInt(1);
        pstmt1.close();
        rs1.close();
        if (last_stmnt_id == null) {
            return 0;
        }
        PreparedStatement pstmt2 = prepareStatement(
                  "select s.amt_life_earned"
                + " from acct_stmnt s"
                + " where s.acct_id = ?"
                + " and s.stmnt_id = ?");
        
        if (acct_id != null) {
            pstmt2.setInt(1, acct_id);
        }
        else {
            pstmt2.setNull(1, Types.JAVA_OBJECT);
        }
        if (last_stmnt_id != null) {
            pstmt2.setInt(2, last_stmnt_id);
        }
        else {
            pstmt2.setNull(2, Types.JAVA_OBJECT);
        }
        ResultSet rs2 = executeQuery(pstmt2);
        rs2.next();
        balance = rs2.getInt(1);
        pstmt2.close();
        rs2.close();
        if (balance == null) {
            return 0;
        }
        return balance;
    }

}