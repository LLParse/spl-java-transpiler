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

public class add_user extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public Integer execute(String user_name) throws SQLException, ProcedureException {
        /*
         * Add a user and return the corresponding user id.  If
         *   the user already exists just return the user id.
         */
        Integer user_id;
        user_id = null;
        user_name = upper(user_name);
        //lookup the user_id by user_name

        PreparedStatement pstmt1 = prepareStatement(
                  "select user.user_id"
                + " from user"
                + " where user.user_name = ?");
        
        if (user_name != null) {
            pstmt1.setString(1, user_name);
        }
        else {
            pstmt1.setNull(1, Types.JAVA_OBJECT);
        }
        ResultSet rs1 = executeQuery(pstmt1);
        rs1.next();
        user_id = rs1.getInt(1);
        pstmt1.close();
        rs1.close();
        //if the record does not exist insert a new record

        //otherwise just update the timestamp

        if (dbinfo("sqlca.sqlerrd2").equals(0)) {
            PreparedStatement pstmt2 = prepareInsert(
                      "insert into user (user_id, user_name, last_access_dtime)"
                    + " values (0, ?, current)");
            if (user_name != null) {
                pstmt2.setString(1, user_name);
            }
            else {
                pstmt2.setNull(1, Types.JAVA_OBJECT);
            }
            executeUpdate(pstmt2);
            pstmt2.close();
            user_id = dbinfo("sqlca.sqlerrd1");
        }
        //return the new or existing user_id

        return user_id;
    }

}