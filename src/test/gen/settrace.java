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

public class settrace extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public String execute(String in_proc_name) throws SQLException, ProcedureException {
        /*
         * settrace.sql - Check app_config for input procedure name and return 
         *                debug flag from table, or 'F' if not set
         *    
         * $Id: settrace.sql 34693 2010-11-29 23:00:10Z rshepher $
         *    
         *        Copyright (C) 2010 Choice Hotels International, Inc.
         */
        String l_debug;
        String l_fullkey;
        Integer sql_error;
        Integer isam_error;
        String error_data;
        try {
            l_fullkey = "stored.procedure.debug." + trim(in_proc_name);
            l_debug = "F";
            PreparedStatement pstmt1 = prepareStatement(
                      "select value"
                    + " from app_config"
                    + " where key = ?");
            
            if (l_fullkey != null) {
                pstmt1.setString(1, l_fullkey);
            }
            else {
                pstmt1.setNull(1, Types.JAVA_OBJECT);
            }
            ResultSet rs1 = executeQuery(pstmt1);
            rs1.next();
            l_debug = rs1.getString(1);
            pstmt1.close();
            rs1.close();
            return l_debug;
        }
        catch (SQLException e) {
            sql_error = e.getErrorCode();
            isam_error = 0;
            error_data = e.getMessage();
            {
                throw new ProcedureException(sql_error, isam_error, error_data);
            }
        }
    }

}