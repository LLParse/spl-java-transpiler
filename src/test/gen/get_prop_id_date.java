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

public class get_prop_id_date extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public Integer execute(String prop_cd, DateTime eff_date) throws SQLException, ProcedureException {
        /*
         * Get the most current property id of the specified property code
         *   for the specified date.
         */
        Integer prop_id;
        prop_id = null;
        PreparedStatement pstmt1 = prepareStatement(
                  "select prop.prop_id"
                + " from prop"
                + " where prop.prop_cd = ?"
                + " and prop.eff_date = 
                    + "select max(prop.eff_date)"
                    + " from prop"
                    + " where prop.prop_cd = ?"
                    + " and prop.eff_date <= ?");
            
            if (prop_cd != null) {
                pstmt1.setString(1, prop_cd);
            }
            else {
                pstmt1.setNull(1, Types.JAVA_OBJECT);
            }
            if (prop_cd != null) {
                pstmt1.setString(2, prop_cd);
            }
            else {
                pstmt1.setNull(2, Types.JAVA_OBJECT);
            }
            if (eff_date != null) {
                pstmt1.setObject(3, eff_date);
            }
            else {
                pstmt1.setNull(3, Types.JAVA_OBJECT);
            }
            ResultSet rs1 = executeQuery(pstmt1);
            rs1.next();
            prop_id = rs1.getInt(1);
            rs1.close();
            return prop_id;
        }
    
    }