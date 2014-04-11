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

public class get_exp_date extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public DateTime execute(String recog_cd, DateTime in_date) throws SQLException, ProcedureException {
        /*
         * $RCSfile$ - Calculate the expiration date for transactions by program - $Revision: 11106 $
         * 
         * $Id: get_exp_date.sql 11106 2007-02-16 16:04:23Z fbloomfi $
         * 
         *       Copyright (C) 2001 Choice Hotels International, Inc.
         *                       All Rights Reserved
         * 
         * This procedure returns the expiration date for an award based on the current
         * date and program type.
         */
        DateTime exp_date;
        String external_pgm;
        exp_date = null;
        external_pgm = null;
        // Determine partner type.

        PreparedStatement pstmt1 = prepareStatement(
                  "select r.external_pgm"
                + " from recog_pgm r"
                + " where r.recog_cd = ?");
        
        if (recog_cd != null) {
            pstmt1.setString(1, recog_cd);
        }
        else {
            pstmt1.setNull(1, Types.JAVA_OBJECT);
        }
        ResultSet rs1 = executeQuery(pstmt1);
        rs1.next();
        external_pgm = rs1.getString(1);
        pstmt1.close();
        rs1.close();
        if (external_pgm.equals("N")) {
            exp_date = mdy(12, 31, year(in_date) + 2);
        }
        return exp_date;
    }

}