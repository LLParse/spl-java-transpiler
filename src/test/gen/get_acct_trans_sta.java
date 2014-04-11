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

public class get_acct_trans_sta extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public String execute(Integer acct_trans_id) throws SQLException, ProcedureException {
        /*
         * Get the status of the specified account transaction.
         */
        String trans_status;
        trans_status = null;
        PreparedStatement pstmt1 = prepareStatement(
                  "select acct_trans.trans_status"
                + " from acct_trans"
                + " where acct_trans.acct_trans_id = ?");
        
        if (acct_trans_id != null) {
            pstmt1.setInt(1, acct_trans_id);
        }
        else {
            pstmt1.setNull(1, Types.JAVA_OBJECT);
        }
        ResultSet rs1 = executeQuery(pstmt1);
        rs1.next();
        trans_status = rs1.getString(1);
        pstmt1.close();
        rs1.close();
        if (trans_status == null) {
            throw new ProcedureException(-746, 0, "get_acct_trans_sta: Account transaction not found.");
        }
        return trans_status;
    }

}