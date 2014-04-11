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

public class get_acct_id extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public Integer execute(String recog_cd, String recog_id) throws SQLException, ProcedureException {
        /*
         * Get the account id of the specified account.
         */
        Integer acct_id;
        acct_id = null;
        PreparedStatement pstmt1 = prepareStatement(
                  "select acct.acct_id"
                + " from acct"
                + " where acct.recog_cd = ?"
                + " and acct.recog_id = ?");
        
        if (recog_cd != null) {
            pstmt1.setString(1, recog_cd);
        }
        else {
            pstmt1.setNull(1, Types.JAVA_OBJECT);
        }
        if (recog_id != null) {
            pstmt1.setString(2, recog_id);
        }
        else {
            pstmt1.setNull(2, Types.JAVA_OBJECT);
        }
        ResultSet rs1 = executeQuery(pstmt1);
        rs1.next();
        acct_id = rs1.getInt(1);
        pstmt1.close();
        rs1.close();
        return acct_id;
    }

}