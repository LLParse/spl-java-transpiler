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

public class cust_has_cc extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public String execute(Integer cust_id, String cc_cd) throws SQLException, ProcedureException {
        Integer cc_count;
        /*
         * $RCSfile$ - does a member have the specified credit card - $Revision: 12170 $
         * 
         * (]$[) $RCSfile$:$Revision: 12170 $ | CDATE=$Date: 2007-06-28 08:43:03 -0700 (Thu, 28 Jun 2007) $ ~
         * 
         *       Copyright (C) 2000 Choice Hotels International, Inc.
         *                       All Rights Reserved
         */
        cc_count = 0;
        PreparedStatement pstmt1 = prepareStatement(
                  "select count(*)"
                + " from cust_cc"
                + " where cust_cc.cust_id = ?"
                + " and cust_cc.cc_cd = ?"
                + " and cust_cc.cc_encrypted_id is not null");
        
        if (cust_id != null) {
            pstmt1.setInt(1, cust_id);
        }
        else {
            pstmt1.setNull(1, Types.JAVA_OBJECT);
        }
        if (cc_cd != null) {
            pstmt1.setString(2, cc_cd);
        }
        else {
            pstmt1.setNull(2, Types.JAVA_OBJECT);
        }
        ResultSet rs1 = executeQuery(pstmt1);
        rs1.next();
        cc_count = rs1.getInt(1);
        pstmt1.close();
        rs1.close();
        if (cc_count.equals(0)) {
            return "F";
        }
        else {
            return "T";
        }
    }

}