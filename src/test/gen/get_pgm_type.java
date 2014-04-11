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

public class get_pgm_type extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public String execute(Integer acct_id) throws SQLException, ProcedureException {
        /*
         * $RCSfile$ - Checks program type associated with an account - $Revision: 5804 $
         * 
         * (]$[) $RCSfile$:$Revision: 5804 $ | CDATE=$Date: 2004-12-13 04:10:28 -0700 (Mon, 13 Dec 2004) $ ~
         * 
         *       Copyright (C) 2001 Choice Hotels International, Inc.
         *                       All Rights Reserved
         * 
         * This procedure returns the type of partner associated with an account. 
         * Returns 'Y' if partner is external, 'N' if internal.
         */
        String external_pgm;
        external_pgm = null;
        // Determine partner type associated with this account.

        PreparedStatement pstmt1 = prepareStatement(
                  "select recog_pgm.external_pgm"
                + " from recog_pgm"
                + " where recog_pgm.recog_cd = new get_recog_cd().execute(acct_id)");
        
        ResultSet rs1 = executeQuery(pstmt1);
        rs1.next();
        external_pgm = rs1.getString(1);
        pstmt1.close();
        rs1.close();
        if (external_pgm == null) {
            throw new ProcedureException(-746, 0, "get_pgm_type: Unable to determine partner type");
        }
        return external_pgm;
    }

}