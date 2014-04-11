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

public class chk_urgent_call extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public String execute(Integer cust_id, Integer acct_id) throws SQLException, ProcedureException {
        /*
         * $RCSfile$ - Check for an urgent call ticket - $Revision: 7872 $
         * 
         * (]$[) $RCSfile$:$Revision: 7872 $ | CDATE=$Date: 2005-09-16 08:17:58 -0700 (Fri, 16 Sep 2005) $ ~
         * 
         *       Copyright (C) 2002 Choice Hotels International, Inc.
         * 		    All Rights Reserved
         * 
         *   Check for a urgent call ticket.
         *   Returns 'Y' if an urgent call ticket is pending, 'N' otherwise.
         */
        Integer urgent_calls;
        // set debug file to '/tmp/chk_urgent_call.trace';

        // trace on;

        urgent_calls = null;
        if (cust_id != null && acct_id != null) {
            PreparedStatement pstmt1 = prepareStatement(
                      "select count(*)"
                    + " from cust_call c"
                    + " where c.cust_id = ?"
                    + " and c.acct_id = ?"
                    + " and c.call_status = \"O\""
                    + " and c.severity = \"U\"");
            
            if (cust_id != null) {
                pstmt1.setInt(1, cust_id);
            }
            else {
                pstmt1.setNull(1, Types.JAVA_OBJECT);
            }
            if (acct_id != null) {
                pstmt1.setInt(2, acct_id);
            }
            else {
                pstmt1.setNull(2, Types.JAVA_OBJECT);
            }
            ResultSet rs1 = executeQuery(pstmt1);
            rs1.next();
            urgent_calls = rs1.getInt(1);
            pstmt1.close();
            rs1.close();
        }
        else if (cust_id != null) {
            PreparedStatement pstmt2 = prepareStatement(
                      "select count(*)"
                    + " from cust_call c"
                    + " where c.cust_id = ?"
                    + " and c.call_status = \"O\""
                    + " and c.severity = \"U\"");
            
            if (cust_id != null) {
                pstmt2.setInt(1, cust_id);
            }
            else {
                pstmt2.setNull(1, Types.JAVA_OBJECT);
            }
            ResultSet rs2 = executeQuery(pstmt2);
            rs2.next();
            urgent_calls = rs2.getInt(1);
            pstmt2.close();
            rs2.close();
        }
        else if (acct_id != null) {
            PreparedStatement pstmt3 = prepareStatement(
                      "select count(*)"
                    + " from cust_call c"
                    + " where c.acct_id = ?"
                    + " and c.call_status = \"O\""
                    + " and c.severity = \"U\"");
            
            if (acct_id != null) {
                pstmt3.setInt(1, acct_id);
            }
            else {
                pstmt3.setNull(1, Types.JAVA_OBJECT);
            }
            ResultSet rs3 = executeQuery(pstmt3);
            rs3.next();
            urgent_calls = rs3.getInt(1);
            pstmt3.close();
            rs3.close();
        }
        else {
            throw new ProcedureException(-746, 0, "chk_urgent_call: cust_id and/or acct_id must be specified");
        }
        if (urgent_calls == null || urgent_calls.equals(0)) {
            return "N";
        }
        else {
            return "Y";
        }
    }

}