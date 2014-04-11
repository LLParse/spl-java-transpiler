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

public class get_response_id extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public Integer execute(String recog_cd, String response_cd) throws SQLException, ProcedureException {
        /*
         * Get the response id corresponding to the
         *   specified response code for the specified
         *   program.
         */
        Integer response_id;
        response_id = null;
        //lookup the response_id by recog_cd and response_cd

        PreparedStatement pstmt1 = prepareStatement(
                  "select response.response_id"
                + " from response"
                + " where response.recog_cd = ?"
                + " and response.response_cd = ?");
        
        if (recog_cd != null) {
            pstmt1.setString(1, recog_cd);
        }
        else {
            pstmt1.setNull(1, Types.JAVA_OBJECT);
        }
        if (response_cd != null) {
            pstmt1.setString(2, response_cd);
        }
        else {
            pstmt1.setNull(2, Types.JAVA_OBJECT);
        }
        ResultSet rs1 = executeQuery(pstmt1);
        rs1.next();
        response_id = rs1.getInt(1);
        pstmt1.close();
        rs1.close();
        //return the promotion id

        return response_id;
    }

}