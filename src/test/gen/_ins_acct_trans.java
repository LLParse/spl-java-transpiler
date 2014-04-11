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

public class _ins_acct_trans extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public Integer execute(Integer acct_id, String trans_type, String trans_status, Integer stay_id, Integer cust_call_id, Integer redemption_id) throws SQLException, ProcedureException {
        /*
         * $RCSfile$ - Add an acct_trans record for an account - $Revision: 5804 $
         * 
         * (]$[) $RCSfile$:$Revision: 5804 $ | CDATE=$Date: 2004-12-13 04:10:28 -0700 (Mon, 13 Dec 2004) $ ~
         * 
         *       Copyright (C) 2000 Choice Hotels International, Inc.
         *                       All Rights Reserved
         * 
         * This procedure adds one acct_trans record for an account. Depending on point
         * adjustment value and transaction type, points will be addod or removed from
         * a members account. 
         * 
         * Returns the resultant acct_trans_id on success.
         */
        Integer acct_trans_id;
        acct_trans_id = null;
        //set debug file to '/tmp/_ins_acct_trans.trace';

        //trace on;

        //

        //Validate trans_type

        //

        if (!trans_type.equals("S") && !trans_type.equals("R") && !trans_type.equals("B") && !trans_type.equals("A") && !trans_type.equals("E") && !trans_type.equals("V")) {
            throw new ProcedureException(-746, 0, "_ins_acct_trans: Invalid trans_type.");
        }
        //

        //Validate that associated ID's are present

        //

        if (trans_type.equals("S") && stay_id == null) {
            throw new ProcedureException(-746, 0, "_ins_acct_trans: Invalid stay_id for stay transaction");
        }
        if (trans_type.equals("R") && stay_id == null && redemption_id == null) {
            throw new ProcedureException(-746, 0, "_ins_acct_trans: Invalid stay or redemption identifier for redemption transaction");
        }
        if (trans_type.equals("A") && cust_call_id == null) {
            throw new ProcedureException(-746, 0, "_ins_acct_trans: Invalid cust_call_id for adjustment transaction");
        }
        PreparedStatement pstmt1 = prepareInsert(
                  "insert into acct_trans (acct_trans_id, acct_id, init_dtime, final_dtime, trans_type, trans_status, rev_acct_trans_id, rsub_acct_trans_id, stay_id, redemption_id, cust_call_id)"
                + " values (0, ?, current, null, ?, ?, null, null, ?, ?, ?)");
        if (acct_id != null) {
            pstmt1.setInt(1, acct_id);
        }
        else {
            pstmt1.setNull(1, Types.JAVA_OBJECT);
        }
        if (trans_type != null) {
            pstmt1.setString(2, trans_type);
        }
        else {
            pstmt1.setNull(2, Types.JAVA_OBJECT);
        }
        if (trans_status != null) {
            pstmt1.setString(3, trans_status);
        }
        else {
            pstmt1.setNull(3, Types.JAVA_OBJECT);
        }
        if (stay_id != null) {
            pstmt1.setInt(4, stay_id);
        }
        else {
            pstmt1.setNull(4, Types.JAVA_OBJECT);
        }
        if (redemption_id != null) {
            pstmt1.setInt(5, redemption_id);
        }
        else {
            pstmt1.setNull(5, Types.JAVA_OBJECT);
        }
        if (cust_call_id != null) {
            pstmt1.setInt(6, cust_call_id);
        }
        else {
            pstmt1.setNull(6, Types.JAVA_OBJECT);
        }
        executeUpdate(pstmt1);
        pstmt1.close();
        //Get the serial value from the insert.

        acct_trans_id = dbinfo("sqlca.sqlerrd1");
        return acct_trans_id;
    }

}