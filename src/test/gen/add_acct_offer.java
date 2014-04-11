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

public class add_acct_offer extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public void execute(String recog_cd, String offer_cd, String recog_id) throws SQLException, ProcedureException {
        /*
         * (]$[) $RCSfile$:$Revision: 29852 $ | CDATE=$Date: 2010-07-02 14:54:26 -0700 (Fri, 02 Jul 2010) $ ~
         * 
         *  Add a account-offer association to the system.
         * 
         * 	Copyright (C) 2002 Choice Hotels International, Inc.
         *                       All Rights Reserved
         */
        Integer offer_id;
        Integer acct_id;
        String registration_channel;
        registration_channel = "ECRM";
        // see PromoRegistrationChannel.ECRM

        // set debug file to '/tmp/add_acct_offer.trace';

        // trace on;

        // encode the offer_id

        offer_id = new get_offer_id().execute(recog_cd, offer_cd);
        // encode the acct_id

        acct_id = new get_acct_id().execute(recog_cd, recog_id);
        PreparedStatement pstmt1 = prepareInsert(
                  "insert into acct_offer (acct_id, offer_id, offer_dtime, registration_channel)"
                + " values (?, ?, current, ?)");
        if (acct_id != null) {
            pstmt1.setInt(1, acct_id);
        }
        else {
            pstmt1.setNull(1, Types.JAVA_OBJECT);
        }
        if (offer_id != null) {
            pstmt1.setInt(2, offer_id);
        }
        else {
            pstmt1.setNull(2, Types.JAVA_OBJECT);
        }
        if (registration_channel != null) {
            pstmt1.setString(3, registration_channel);
        }
        else {
            pstmt1.setNull(3, Types.JAVA_OBJECT);
        }
        executeUpdate(pstmt1);
        pstmt1.close();
    }

}