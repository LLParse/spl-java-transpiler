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

public class get_cust_call_id extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public Integer execute(Integer acct_trans_id) throws SQLException, ProcedureException {
        /*
         * Get the cust call id associated with the specified transaction.
         */
        Integer cust_call_id;
        cust_call_id = null;
        PreparedStatement pstmt1 = prepareStatement(
                  "select cust_call.cust_call_id"
                + " from cust_call"
                + " where cust_call.acct_trans_id = ?");
        
        if (acct_trans_id != null) {
            pstmt1.setInt(1, acct_trans_id);
        }
        else {
            pstmt1.setNull(1, Types.JAVA_OBJECT);
        }
        ResultSet rs1 = executeQuery(pstmt1);
        rs1.next();
        cust_call_id = rs1.getInt(1);
        pstmt1.close();
        rs1.close();
        return cust_call_id;
    }

}