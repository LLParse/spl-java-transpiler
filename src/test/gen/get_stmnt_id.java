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

public class get_stmnt_id extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public Integer execute(DateTime stmnt_date, String recog_cd) throws SQLException, ProcedureException {
        /*
         * $RCSfile$ - Get the statement ID for a given date - $Revision: 5804 $
         * 
         * (]$[) $RCSfile$:$Revision: 5804 $ | CDATE=$Date: 2004-12-13 04:10:28 -0700 (Mon, 13 Dec 2004) $ ~
         * 
         *       Copyright (C) 2001 Choice Hotels International, Inc.
         *                       All Rights Reserved
         * 
         * This procedure returns the statement ID for a given date.
         */
        Integer stmnt_id;
        stmnt_id = null;
        PreparedStatement pstmt1 = prepareStatement(
                  "select stmnt_cycle.stmnt_id"
                + " from stmnt_cycle"
                + " where stmnt_cycle.recog_cd = ?"
                + " and ");
        
        if (recog_cd != null) {
            pstmt1.setString(1, recog_cd);
        }
        else {
            pstmt1.setNull(1, Types.JAVA_OBJECT);
        }
        ResultSet rs1 = executeQuery(pstmt1);
        rs1.next();
        stmnt_id = rs1.getInt(1);
        pstmt1.close();
        rs1.close();
        if (stmnt_id == null) {
            throw new ProcedureException(-746, 0, "get_stmnt_id: No statement cycle defined for given date");
        }
        return stmnt_id;
    }

}