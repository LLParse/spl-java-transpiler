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

public class pf_isplatinum_ondoa extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public String execute(Integer in_acct_id, DateTime in_doa) throws SQLException, ProcedureException {
        /*
         * pf_isplatinum_ondoa.sql - Determine if the elite status on the DOA of the stay was Platinum.
         * 
         * $Id: pf_isplatinum_ondoa.sql 25075 2010-01-15 22:32:02Z rshepher $
         * 
         *        Copyright (C) 2010 Choice Hotels International, Inc.
         */
        String l_name;
        String l_result;
        l_name = null;
        l_result = "F";
        // assume not Platinum

        //set debug file to '/tmp/pf_isplatinum_ondoa.trace';

        //trace on;

        PreparedStatement pstmt1 = prepareStatement(
                  "select elvl.name"
                + " from acct_elite_level_hist aelh, elite_level elvl"
                + " where aelh.acct_id = ?"
                + " and aelh.elite_level_id = elvl.elite_level_id"
                + " and aelh.acct_elite_level_hist_id = 
                    + "select max(els.acct_elite_level_hist_id)"
                    + " from acct_elite_level_hist els"
                    + "  inner join elite_level es on els.elite_level_id = es.elite_level_id"
                    + " where els.acct_id = ?"
                    + " and els.date_acquired <= ?"
                    + " and year(es.eff_year) = year(?)");
            
            if (in_acct_id != null) {
                pstmt1.setInt(1, in_acct_id);
            }
            else {
                pstmt1.setNull(1, Types.JAVA_OBJECT);
            }
            if (in_acct_id != null) {
                pstmt1.setInt(2, in_acct_id);
            }
            else {
                pstmt1.setNull(2, Types.JAVA_OBJECT);
            }
            if (in_doa != null) {
                pstmt1.setObject(3, in_doa);
            }
            else {
                pstmt1.setNull(3, Types.JAVA_OBJECT);
            }
            if (in_doa != null) {
                pstmt1.setObject(4, in_doa);
            }
            else {
                pstmt1.setNull(4, Types.JAVA_OBJECT);
            }
            ResultSet rs1 = executeQuery(pstmt1);
            rs1.next();
            l_name = rs1.getString(1);
            rs1.close();
            if (l_name.equals("Platinum")) {
                l_result = "T";
            }
            return l_result;
        }
    
    }