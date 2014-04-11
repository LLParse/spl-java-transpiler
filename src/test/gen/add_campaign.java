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

public class add_campaign extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public Integer execute(String cpgn_name, String cpgn_desc, String cpgn_class, String real_time, DateTime start_date, DateTime stop_date) throws SQLException, ProcedureException {
        /*
         * (]$[) $RCSfile$:$Revision: 23158 $ | CDATE=$Date: 2009-09-01 14:54:38 -0700 (Tue, 01 Sep 2009) $ ~
         * 
         *  Add a campaign to the system.
         * 
         * 	Copyright (C) 2003 Choice Hotels International, Inc.
         *                       All Rights Reserved
         */
        Integer campaign_id;
        campaign_id = null;
        // set debug file to '/tmp/add_campaign.trace';

        // trace on;

        cpgn_name = trim(cpgn_name);
        if (length(cpgn_name) < 5) {
            throw new ProcedureException(-746, 0, "add_campaign: a meaningful cpgn_name must be specified");
        }
        //

        // check for a campaign matching the name

        // return the campaign_id if found

        // otherwise continue with the insert

        //

        PreparedStatement pstmt1 = prepareStatement(
                  "select c.campaign_id"
                + " from campaign c"
                + " where c.name = ?");
        
        if (cpgn_name != null) {
            pstmt1.setString(1, cpgn_name);
        }
        else {
            pstmt1.setNull(1, Types.JAVA_OBJECT);
        }
        ResultSet rs1 = executeQuery(pstmt1);
        rs1.next();
        campaign_id = rs1.getInt(1);
        pstmt1.close();
        rs1.close();
        if (dbinfo("sqlca.sqlerrd2").equals(1)) {
            return campaign_id;
        }
        cpgn_desc = trim(cpgn_desc);
        if (length(cpgn_desc) < 10) {
            throw new ProcedureException(-746, 0, "add_campaign: a meaningful cpgn_desc must be specified");
        }
        real_time = trim(real_time);
        if (real_time == null) {
            real_time = "N";
        }
        else {
            if ((!real_time.equals("Y") && !real_time.equals("N"))) {
                throw new ProcedureException(-746, 0, "add_campaign: real_time must be Y, N or null");
            }
        }
        if ((start_date.isBefore(new LocalDate().toDateTimeAtStartOfDay().minusDays(2)) || start_date.isEqual(new LocalDate().toDateTimeAtStartOfDay().minusDays(2)))) {
            throw new ProcedureException(-746, 0, "add_campaign: start_date must be in the future");
        }
        if (stop_date.isBefore(start_date)) {
            throw new ProcedureException(-746, 0, "add_campaign: stop_date cannot be before the start_date");
        }
        PreparedStatement pstmt2 = prepareInsert(
                  "insert into campaign (campaign_id, name, desc, clazz, real_time, start_date, stop_date)"
                + " values (0, ?, ?, ?, ?, ?, ?)");
        if (cpgn_name != null) {
            pstmt2.setString(1, cpgn_name);
        }
        else {
            pstmt2.setNull(1, Types.JAVA_OBJECT);
        }
        if (cpgn_desc != null) {
            pstmt2.setString(2, cpgn_desc);
        }
        else {
            pstmt2.setNull(2, Types.JAVA_OBJECT);
        }
        if (cpgn_class != null) {
            pstmt2.setString(3, cpgn_class);
        }
        else {
            pstmt2.setNull(3, Types.JAVA_OBJECT);
        }
        if (real_time != null) {
            pstmt2.setString(4, real_time);
        }
        else {
            pstmt2.setNull(4, Types.JAVA_OBJECT);
        }
        if (start_date != null) {
            pstmt2.setObject(5, start_date);
        }
        else {
            pstmt2.setNull(5, Types.JAVA_OBJECT);
        }
        if (stop_date != null) {
            pstmt2.setObject(6, stop_date);
        }
        else {
            pstmt2.setNull(6, Types.JAVA_OBJECT);
        }
        executeUpdate(pstmt2);
        pstmt2.close();
        campaign_id = dbinfo("sqlca.sqlerrd1");
        return campaign_id;
    }

}