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

public class get_user_name extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public String execute(Integer user_id) throws SQLException, ProcedureException {
        /*
         * Get the user name corresponding to the specified user id.
         */
        String user_name;
        user_name = null;
        PreparedStatement pstmt1 = prepareStatement(
                  "select user.user_name"
                + " from user"
                + " where user.user_id = ?");
        
        if (user_id != null) {
            pstmt1.setInt(1, user_id);
        }
        else {
            pstmt1.setNull(1, Types.JAVA_OBJECT);
        }
        ResultSet rs1 = executeQuery(pstmt1);
        rs1.next();
        user_name = rs1.getString(1);
        pstmt1.close();
        rs1.close();
        return user_name;
    }

}