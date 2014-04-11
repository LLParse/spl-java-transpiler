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

public class get_promo_teaser extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public String execute(String recog_cd, String recog_id, String campaign_name) throws SQLException, ProcedureException {
        //teaser code

        /*
         * (]$[) $RCSfile$:$Revision: 5804 $ | CDATE=$Date: 2004-12-13 04:10:28 -0700 (Mon, 13 Dec 2004) $ ~
         * 
         * 
         *   get_promo_teaser -  get promotion teaser text for a account and promotion
         * 
         *       Copyright (C) 2003 Choice Hotels International, Inc.
         *                       All Rights Reserved
         */
        Integer sql_error;
        Integer isam_error;
        String error_data;
        String teaser_resp;
        //  set debug file to '/tmp/get_promo_teaser.trace';

        //  trace on;

        try {
            teaser_resp = null;
            // Determine teaser based on campaign ID

            if (campaign_name.equals("FL2004")) {
                teaser_resp = fl2004_teaser(recog_cd, recog_id);
            }
            return teaser_resp;
            //  set debug file to '/tmp/get_promo_teaser.trace';

            //  trace on;

        }
        catch (SQLException e) {
            sql_error = e.getErrorCode();
            isam_error = 0;
            error_data = e.getMessage();
            PreparedStatement pstmt1 = prepareStatement("drop table last_stays");
            executeUpdate(pstmt1);
            pstmt1.close();
            throw new ProcedureException(sql_error, isam_error, error_data);
        }
    }

}