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

public class submit_ext_trans extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public Collection<Object> execute(Integer par_id) throws SQLException, ProcedureException {
        /*
         * (]$[) $RCSfile$:$Revision: 6375 $ | CDATE=$Date: 2005-03-07 06:42:34 -0700 (Mon, 07 Mar 2005) $ ~
         * 
         *   Update the par and acct_trans tables to note that the transaction
         *   has been submitted to an external partner.
         * 
         * 	Copyright (C) 2002 Choice Hotels International, Inc.
         *                       All Rights Reserved
         */
        Integer acct_id;
        Integer acct_trans_id;
        String acct_status;
        // Initialize defined variables to null

        acct_id = null;
        acct_trans_id = null;
        acct_status = null;
        //set debug file to '/tmp/submit_ext_trans.trace';

        //trace on;

        //----------------------------------------------------------

        // First determine that the member is at an 'A'ctive status

        //----------------------------------------------------------

        PreparedStatement pstmt1 = prepareStatement(
                  "select a.acct_id, a.acct_trans_id"
                + " from acct_trans a, par p"
                + " where a.acct_trans_id = p.acct_trans_id"
                + " and p.par_id = ?");
        
        if (par_id != null) {
            pstmt1.setInt(1, par_id);
        }
        else {
            pstmt1.setNull(1, Types.JAVA_OBJECT);
        }
        ResultSet rs1 = executeQuery(pstmt1);
        rs1.next();
        acct_id = rs1.getInt(1);
        acct_trans_id = rs1.getInt(2);
        pstmt1.close();
        rs1.close();
        if (acct_id == null) {
            return new ArrayList<Object>(Arrays.<Object>asList(0, "submit_ext_trans: Account not found."));
        }
        acct_status = new get_acct_status().execute(acct_id);
        if (acct_status != "A") {
            return new ArrayList<Object>(Arrays.<Object>asList(0, "submit_ext_trans: Account status is not 'A'ctive."));
        }
        new _submit_trans().execute(acct_trans_id);
        return new ArrayList<Object>(Arrays.<Object>asList(1, ""));
    }

}