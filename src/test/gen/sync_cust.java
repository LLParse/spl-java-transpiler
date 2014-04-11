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

public class sync_cust extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public void execute(Integer cust_id, String action) throws SQLException, ProcedureException {
        Integer acct_id;
        String acct_status;
        acct_id = null;
        acct_status = null;
        PreparedStatement pstmt1 = prepareStatement(
                  "select cust_acct.acct_id, acct.acct_status"
                + " from cust_acct, acct"
                + " where cust_acct.cust_id = ?"
                + " and acct.acct_id = cust_acct.acct_id"
                + " and cust_acct.primary_cust = \"Y\"");
        
        if (cust_id != null) {
            pstmt1.setInt(1, cust_id);
        }
        else {
            pstmt1.setNull(1, Types.JAVA_OBJECT);
        }
        ResultSet rs1 = executeQuery(pstmt1);
        while (rs1.next()) {
            acct_id = rs1.getInt(1);
            acct_status = rs1.getString(2);
            new sync_acct().execute(acct_id, acct_status, action);
        }
        pstmt1.close();
        rs1.close();
    }

}