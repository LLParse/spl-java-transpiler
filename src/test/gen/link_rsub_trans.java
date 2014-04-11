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

public class link_rsub_trans extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public Integer execute(Integer orig_acct_trans_id, Integer new_acct_trans_id) throws SQLException, ProcedureException {
        /*
         * (]$[) $RCSfile$:$Revision: 5804 $ | CDATE=$Date: 2004-12-13 04:10:28 -0700 (Mon, 13 Dec 2004) $ ~
         * 
         *       Copyright (C) 2002 Choice Hotels International, Inc.
         * 		    All Rights Reserved
         * 
         * 
         *   Link a resubmitted transaction to the orginal transaction.
         */
        Integer rsub_acct_trans_id;
        String orig_trans_type;
        String orig_trans_status;
        String new_trans_type;
        rsub_acct_trans_id = null;
        orig_trans_type = null;
        orig_trans_status = null;
        new_trans_type = null;
        //set debug file to '/tmp/link_rsub_trans.trace';

        //trace on;

        PreparedStatement pstmt1 = prepareStatement(
                  "select acct_trans.rsub_acct_trans_id, acct_trans.trans_type, acct_trans.trans_status"
                + " from acct_trans"
                + " where acct_trans.acct_trans_id = ?");
        
        if (orig_acct_trans_id != null) {
            pstmt1.setInt(1, orig_acct_trans_id);
        }
        else {
            pstmt1.setNull(1, Types.JAVA_OBJECT);
        }
        ResultSet rs1 = executeQuery(pstmt1);
        rs1.next();
        rsub_acct_trans_id = rs1.getInt(1);
        orig_trans_type = rs1.getString(2);
        orig_trans_status = rs1.getString(3);
        pstmt1.close();
        rs1.close();
        PreparedStatement pstmt2 = prepareStatement(
                  "select acct_trans.trans_type"
                + " from acct_trans"
                + " where acct_trans.acct_trans_id = ?");
        
        if (new_acct_trans_id != null) {
            pstmt2.setInt(1, new_acct_trans_id);
        }
        else {
            pstmt2.setNull(1, Types.JAVA_OBJECT);
        }
        ResultSet rs2 = executeQuery(pstmt2);
        rs2.next();
        new_trans_type = rs2.getString(1);
        pstmt2.close();
        rs2.close();
        if (!new_trans_type.equals(orig_trans_type)) {
            throw new ProcedureException(-746, 0, "link_rsub_trans: transactions must be of the same type");
        }
        if (!orig_trans_status.equals("R")) {
            throw new ProcedureException(-746, 0, "link_rsub_trans: original trans status must be rejected");
        }
        //Check the original transaction to make sure it hasn't already been resubmitted

        if (rsub_acct_trans_id != null) {
            if (!rsub_acct_trans_id.equals(new_acct_trans_id)) {
                throw new ProcedureException(-746, 0, "link_rsub_trans: transaction already resubmitted");
            }
        }
        //Update the original transaction with the resubmitted transaction id.

        PreparedStatement pstmt3 = prepareStatement(
                  "update acct_trans"
                + " set acct_trans.rsub_acct_trans_id = ?"
                + " where acct_trans.acct_trans_id = ?");
        
        if (new_acct_trans_id != null) {
            pstmt3.setInt(1, new_acct_trans_id);
        }
        else {
            pstmt3.setNull(1, Types.JAVA_OBJECT);
        }
        if (orig_acct_trans_id != null) {
            pstmt3.setInt(2, orig_acct_trans_id);
        }
        else {
            pstmt3.setNull(2, Types.JAVA_OBJECT);
        }
        executeUpdate(pstmt3);
        pstmt3.close();
        return orig_acct_trans_id;
    }

}