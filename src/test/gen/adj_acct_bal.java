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

public class adj_acct_bal extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public Integer execute(Integer acct_id, Integer cust_call_id, Double amount, Integer orig_trans_id) throws SQLException, ProcedureException {
        /*
         * (]$[) $RCSfile$:$Revision: 5804 $ | CDATE=$Date: 2004-12-13 04:10:28 -0700 (Mon, 13 Dec 2004) $ ~
         * 
         *  Process one acount adjustment transaction. 
         *  Returns acct_trans_id on success.
         * 
         * 
         * 	Copyright (C) 2000 Choice Hotels International, Inc.
         *                       All Rights Reserved
         */
        // Data from new records/calculations

        Integer acct_trans_id;
        Integer detail_id;
        Integer cust_id;
        // Initialize defined variables to null

        acct_trans_id = null;detail_id = null;cust_id = null;
        //set debug file to '/tmp/adj_acct_bal.trace';

        //trace on;

        // Get customer for this call ticket

        PreparedStatement pstmt1 = prepareStatement(
                  "select cust_call.cust_id"
                + " from cust_call"
                + " where cust_call.cust_call_id = ?");
        
        if (cust_call_id != null) {
            pstmt1.setInt(1, cust_call_id);
        }
        else {
            pstmt1.setNull(1, Types.JAVA_OBJECT);
        }
        ResultSet rs1 = executeQuery(pstmt1);
        rs1.next();
        cust_id = rs1.getInt(1);
        pstmt1.close();
        rs1.close();
        if (cust_id == null) {
            throw new ProcedureException(-746, 0, "Adjustment requires valid cust_call_id");
        }
        acct_trans_id = new _post_trans().execute(acct_id, cust_id, "A", null, cust_call_id, null, amount, null);
        detail_id = new _ins_acct_trans_de().execute(acct_trans_id, 1, amount, null, "N");
        //---------------------------------------------------------

        // Finally if this adjustment is a resubmitted one link them

        //---------------------------------------------------------

        if (orig_trans_id != null) {
            orig_trans_id = new link_rsub_trans().execute(orig_trans_id, acct_trans_id);
        }
        return acct_trans_id;
    }

}