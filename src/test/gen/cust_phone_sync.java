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

public class cust_phone_sync extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public void execute(Integer cust_id, String action, DateTime last_update_dtime) throws SQLException, ProcedureException {
        /*
         * Procedure to insert entry into cust_phone_sync table. Used to bring the
         *   new cust_phone table up-to-date after it has been loaded.
         *  
         *   $Id: cust_phone_sync.sql 47089 2011-12-28 19:05:13Z mgiroux $
         */
        PreparedStatement pstmt1 = prepareInsert(
                  "insert into cust_phone_sync (cust_phone_sync_id, cust_id, action, last_update_dtime)"
                + " values (0, ?, ?, ?)");
        if (cust_id != null) {
            pstmt1.setInt(1, cust_id);
        }
        else {
            pstmt1.setNull(1, Types.JAVA_OBJECT);
        }
        if (action != null) {
            pstmt1.setString(2, action);
        }
        else {
            pstmt1.setNull(2, Types.JAVA_OBJECT);
        }
        if (last_update_dtime != null) {
            pstmt1.setObject(3, last_update_dtime);
        }
        else {
            pstmt1.setNull(3, Types.JAVA_OBJECT);
        }
        executeUpdate(pstmt1);
        pstmt1.close();
    }

}