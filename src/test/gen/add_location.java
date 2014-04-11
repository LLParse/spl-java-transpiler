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

public class add_location extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public Integer execute(String location_name) throws SQLException, ProcedureException {
        /*
         * Add a location and return the corresponding location id.  If
         *   the location already exists just return the location id.
         */
        Integer location_id;
        location_id = null;
        location_name = upper(location_name);
        //lookup the location_id by location_name

        PreparedStatement pstmt1 = prepareStatement(
                  "select location.location_id"
                + " from location"
                + " where location.location_name = ?");
        
        if (location_name != null) {
            pstmt1.setString(1, location_name);
        }
        else {
            pstmt1.setNull(1, Types.JAVA_OBJECT);
        }
        ResultSet rs1 = executeQuery(pstmt1);
        rs1.next();
        location_id = rs1.getInt(1);
        pstmt1.close();
        rs1.close();
        //if the record does not exist insert a new record

        if (dbinfo("sqlca.sqlerrd2").equals(0)) {
            PreparedStatement pstmt2 = prepareInsert(
                      "insert into location (location_id, location_name)"
                    + " values (0, ?)");
            if (location_name != null) {
                pstmt2.setString(1, location_name);
            }
            else {
                pstmt2.setNull(1, Types.JAVA_OBJECT);
            }
            executeUpdate(pstmt2);
            pstmt2.close();
            location_id = dbinfo("sqlca.sqlerrd1");
        }
        //return the new or existing location_id

        return location_id;
    }

}