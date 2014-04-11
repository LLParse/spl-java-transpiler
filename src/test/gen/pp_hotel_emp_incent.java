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

public class pp_hotel_emp_incent extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public void execute(Integer acct_id, Integer cust_id) throws SQLException, ProcedureException {
        /*
         * Determine whether or not the member is associated with a
         * hotel employee incentive.
         *    
         *           
         * $Id: pp_hotel_emp_incent.sql 23158 2009-09-01 21:54:38Z kdamron $
         *    
         *        Copyright (C) 2009 Choice Hotels International, Inc.
         */
        Integer raf_reg_id;
        Integer referring_acct_id;
        raf_reg_id = null;
        referring_acct_id = null;
        // Use the acct_id passed into process_stay to search for raf_reg records

        // that have referred_acct_id = acct_id, referral_type = EMP and a bounty that

        // has not yet been rewarded.

        PreparedStatement pstmt1 = prepareStatement(
                  "select min(raf_reg.raf_reg_id)"
                + " from raf_reg"
                + " where raf_reg.referred_acct_id = ?"
                + " and raf_reg.referral_type = \"EMP\""
                + " and raf_reg.bounty_acct_trans_id is null");
        
        if (acct_id != null) {
            pstmt1.setInt(1, acct_id);
        }
        else {
            pstmt1.setNull(1, Types.JAVA_OBJECT);
        }
        ResultSet rs1 = executeQuery(pstmt1);
        rs1.next();
        raf_reg_id = rs1.getInt(1);
        pstmt1.close();
        rs1.close();
        if (raf_reg_id != null) {
            PreparedStatement pstmt2 = prepareStatement(
                      "select raf_reg.referring_acct_id"
                    + " from raf_reg"
                    + " where raf_reg.raf_reg_id = ?");
            
            if (raf_reg_id != null) {
                pstmt2.setInt(1, raf_reg_id);
            }
            else {
                pstmt2.setNull(1, Types.JAVA_OBJECT);
            }
            ResultSet rs2 = executeQuery(pstmt2);
            rs2.next();
            referring_acct_id = rs2.getInt(1);
            pstmt2.close();
            rs2.close();
            PreparedStatement pstmt3 = prepareInsert(
                      "insert into acct_event (event_id, acct_id, event_type, data, event_dtime)"
                    + " values (0, ?, \"R\", ?, current)");
            if (referring_acct_id != null) {
                pstmt3.setInt(1, referring_acct_id);
            }
            else {
                pstmt3.setNull(1, Types.JAVA_OBJECT);
            }
            if (raf_reg_id != null) {
                pstmt3.setInt(2, raf_reg_id);
            }
            else {
                pstmt3.setNull(2, Types.JAVA_OBJECT);
            }
            executeUpdate(pstmt3);
            pstmt3.close();
        }
    }

}