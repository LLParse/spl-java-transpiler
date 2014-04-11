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

public class ppp_raf_activation extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public void execute(Integer acct_id, Integer acct_trans_id) throws SQLException, ProcedureException {
        /*
         * Refer a freind - Bounty event processor 
         *  (]$[) $RCSfile$:$Revision: 11820 $ | CDATE=$Date: 2007-06-01 14:55:37 -0700 (Fri, 01 Jun 2007) $ ~
         * 
         *   Given an account ID, queue up an acct_event record to award the bonus.
         * 
         *       Copyright (C) 2006 Choice Hotels International, Inc.
         * 		    All Rights Reserved
         */
        Integer raf_reg_id;
        Integer referring_acct_id;
        Integer orig_version;
        raf_reg_id = null;
        referring_acct_id = null;
        orig_version = null;
        //set debug file to '/tmp/ppp_raf_activation.trace';

        //trace on;

        PreparedStatement pstmt1 = prepareStatement(
                  "select raf_reg.raf_reg_id, raf_reg.referring_acct_id, version"
                + " from raf_reg"
                + " where raf_reg.referred_acct_id = ?"
                + " and raf_reg.reg_status = \"E\"");
        
        if (acct_id != null) {
            pstmt1.setInt(1, acct_id);
        }
        else {
            pstmt1.setNull(1, Types.JAVA_OBJECT);
        }
        ResultSet rs1 = executeQuery(pstmt1);
        rs1.next();
        raf_reg_id = rs1.getInt(1);
        referring_acct_id = rs1.getInt(2);
        orig_version = rs1.getInt(3);
        pstmt1.close();
        rs1.close();
        if (raf_reg_id == null) {
            return;
        }
        //add an event for the referring members's bounty  

        PreparedStatement pstmt2 = prepareInsert(
                  "insert into acct_event (event_id, acct_id, event_type, data, event_dtime)"
                + " values (0, ?, \"R\", ?, current)");
        if (referring_acct_id != null) {
            pstmt2.setInt(1, referring_acct_id);
        }
        else {
            pstmt2.setNull(1, Types.JAVA_OBJECT);
        }
        if (raf_reg_id != null) {
            pstmt2.setInt(2, raf_reg_id);
        }
        else {
            pstmt2.setNull(2, Types.JAVA_OBJECT);
        }
        executeUpdate(pstmt2);
        pstmt2.close();
        //update the registration record to reference the new stay transaction

        PreparedStatement pstmt3 = prepareStatement(
                  "update raf_reg"
                + " set stay_acct_trans_id = ?, version = ? + 1"
                + " where raf_reg.raf_reg_id = ?"
                + " and raf_reg.version = ?");
        
        if (acct_trans_id != null) {
            pstmt3.setInt(1, acct_trans_id);
        }
        else {
            pstmt3.setNull(1, Types.JAVA_OBJECT);
        }
        if (orig_version != null) {
            pstmt3.setInt(2, orig_version);
        }
        else {
            pstmt3.setNull(2, Types.JAVA_OBJECT);
        }
        if (raf_reg_id != null) {
            pstmt3.setInt(3, raf_reg_id);
        }
        else {
            pstmt3.setNull(3, Types.JAVA_OBJECT);
        }
        if (orig_version != null) {
            pstmt3.setInt(4, orig_version);
        }
        else {
            pstmt3.setNull(4, Types.JAVA_OBJECT);
        }
        executeUpdate(pstmt3);
        pstmt3.close();
        // Check that we still have the current record

        if (!dbinfo("sqlca.sqlerrd2").equals(1)) {
            throw new ProcedureException(-746, 0, "ppp_raf_activation: raf_reg record has changed before currrent update");
        }
    }

}