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

public class get_acct_status extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public String execute(Integer acct_id) throws SQLException, ProcedureException {
        /*
         * Get the account status of the specified account.
         */
        String acct_status;
        acct_status = null;
        PreparedStatement pstmt1 = prepareStatement(
                  "select acct.acct_status"
                + " from acct"
                + " where acct.acct_id = ?");
        
        if (acct_id != null) {
            pstmt1.setInt(1, acct_id);
        }
        else {
            pstmt1.setNull(1, Types.JAVA_OBJECT);
        }
        ResultSet rs1 = executeQuery(pstmt1);
        rs1.next();
        acct_status = rs1.getString(1);
        pstmt1.close();
        rs1.close();
        if (acct_status == null) {
            throw new ProcedureException(-746, 0, "get_acct_status: Account not found.");
        }
        return acct_status;
    }

}