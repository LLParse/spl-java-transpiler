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

public class get_program_units extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public String execute(Integer acct_id) throws SQLException, ProcedureException {
        /*
         * $RCSfile$ - Get the units of measure for a program - $Revision: 5804 $
         * 
         * (]$[) $RCSfile$:$Revision: 5804 $ | CDATE=$Date: 2004-12-13 04:10:28 -0700 (Mon, 13 Dec 2004) $ ~
         * 
         *       Copyright (C) 2001 Choice Hotels International, Inc.
         *                       All Rights Reserved
         * 
         * This procedure returns the units of measre based on account.
         */
        String recog_cd;
        String trans_unit_cd;
        recog_cd = null;trans_unit_cd = null;
        // Find the program associated with the account.

        recog_cd = new get_recog_cd().execute(acct_id);
        if (recog_cd == null) {
            throw new ProcedureException(-746, 0, "get_program_units: Unable to determine program code from acct_id");
        }
        PreparedStatement pstmt1 = prepareStatement(
                  "select recog_pgm.trans_unit_cd"
                + " from recog_pgm"
                + " where recog_pgm.recog_cd = ?");
        
        if (recog_cd != null) {
            pstmt1.setString(1, recog_cd);
        }
        else {
            pstmt1.setNull(1, Types.JAVA_OBJECT);
        }
        ResultSet rs1 = executeQuery(pstmt1);
        rs1.next();
        trans_unit_cd = rs1.getString(1);
        pstmt1.close();
        rs1.close();
        if (trans_unit_cd == null) {
            throw new ProcedureException(-746, 0, "get_program_units: Unable to get transaction units from program ID");
        }
        return trans_unit_cd;
    }

}