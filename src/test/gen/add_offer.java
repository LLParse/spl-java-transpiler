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

public class add_offer extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public Integer execute(String offer_cd, String offer_desc, DateTime start_date, DateTime stop_date, Integer campaign_id, String contact_method, String recog_cd, String user_name) throws SQLException, ProcedureException {
        /*
         * (]$[) $RCSfile$:$Revision: 36780 $ | CDATE=$Date: 2011-02-24 09:42:19 -0700 (Thu, 24 Feb 2011) $ ~
         * 
         *  Add an offer to the system.
         * 
         * 	Copyright (C) 2002 Choice Hotels International, Inc.
         *                       All Rights Reserved
         */
        Integer offer_id;
        Integer user_id;
        offer_id = null;
        user_id = null;
        // set debug file to '/tmp/add_offer.trace';

        // trace on;

        offer_cd = trim(offer_cd);
        if (length(offer_cd) < 4) {
            throw new ProcedureException(-746, 0, "add_offer: a meaningful offer_cd must be specified");
        }
        offer_desc = trim(offer_desc);
        if (length(offer_desc) < 10) {
            throw new ProcedureException(-746, 0, "add_offer: a meaningful offer_desc must be specified");
        }
        // grant some slack when adding offers, same as when adding promos

        if ((start_date.isBefore(new LocalDate().toDateTimeAtStartOfDay().minusDays(2)) || start_date.isEqual(new LocalDate().toDateTimeAtStartOfDay().minusDays(2)))) {
            throw new ProcedureException(-746, 0, "add_offer: start_date must be in the future");
        }
        if (stop_date.isBefore(start_date)) {
            throw new ProcedureException(-746, 0, "add_offer: stop_date cannot be before the start_date");
        }
        if (!contact_method.equals("E") && !contact_method.equals("D") && !contact_method.equals("B") && !contact_method.equals("P") && !contact_method.equals("R")) {
            throw new ProcedureException(-746, 0, "add_offer: invalid contact_method");
        }
        user_id = new get_user_id().execute(user_name);
        if (user_id == null) {
            throw new ProcedureException(-746, 0, "add_offer: user_name is not known");
        }
        PreparedStatement pstmt1 = prepareInsert(
                  "insert into offer (offer_id, offer_cd, desc, start_date, stop_date, campaign_id, contact_method, recog_cd, entry_id, last_update_dtime, last_update_id)"
                + " values (0, ?, ?, ?, ?, ?, ?, ?, ?, current, ?)");
        if (offer_cd != null) {
            pstmt1.setString(1, offer_cd);
        }
        else {
            pstmt1.setNull(1, Types.JAVA_OBJECT);
        }
        if (offer_desc != null) {
            pstmt1.setString(2, offer_desc);
        }
        else {
            pstmt1.setNull(2, Types.JAVA_OBJECT);
        }
        if (start_date != null) {
            pstmt1.setObject(3, start_date);
        }
        else {
            pstmt1.setNull(3, Types.JAVA_OBJECT);
        }
        if (stop_date != null) {
            pstmt1.setObject(4, stop_date);
        }
        else {
            pstmt1.setNull(4, Types.JAVA_OBJECT);
        }
        if (campaign_id != null) {
            pstmt1.setInt(5, campaign_id);
        }
        else {
            pstmt1.setNull(5, Types.JAVA_OBJECT);
        }
        if (contact_method != null) {
            pstmt1.setString(6, contact_method);
        }
        else {
            pstmt1.setNull(6, Types.JAVA_OBJECT);
        }
        if (recog_cd != null) {
            pstmt1.setString(7, recog_cd);
        }
        else {
            pstmt1.setNull(7, Types.JAVA_OBJECT);
        }
        if (user_id != null) {
            pstmt1.setInt(8, user_id);
        }
        else {
            pstmt1.setNull(8, Types.JAVA_OBJECT);
        }
        if (user_id != null) {
            pstmt1.setInt(9, user_id);
        }
        else {
            pstmt1.setNull(9, Types.JAVA_OBJECT);
        }
        executeUpdate(pstmt1);
        pstmt1.close();
        offer_id = dbinfo("sqlca.sqlerrd1");
        return offer_id;
    }

}