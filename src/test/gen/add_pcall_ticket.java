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

public class add_pcall_ticket extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public Integer execute(Integer prop_id, String call_status, String severity, Integer std_desc_id, String problem_desc, String assoc_recog_cd, String assoc_recog_id, String recog_id, Integer entry_id, String notes, String contact_title, String contact_frst_name, String contact_last_name, String contact_phone, String contact_fax, String contact_email_addr) throws SQLException, ProcedureException {
        /*
         * (]$[) $RCSfile$:$Revision: 8487 $ | CDATE=$Date: 2005-11-08 09:40:41 -0700 (Tue, 08 Nov 2005) $ ~
         * 
         *  Generate one each prop_call and prop_call_log records.
         * 
         *  This procedure is a wrapper for _add_pcall_ticket adding only BEGIN and 
         *  COMMIT/ROLLBACK.
         *  
         * 	Copyright (C) 2002 Choice Hotels International, Inc.
         *                       All Rights Reserved
         */
        Integer prop_call_id;
        Integer sql_error;
        Integer isam_error;
        String error_data;
        try {
            // set debug file to '/tmp/add_call_ticket.trace';

            // trace on;

            prop_call_id = null;
            begin();
            prop_call_id = new _add_pcall_ticket().execute(prop_id, call_status, severity, std_desc_id, problem_desc, assoc_recog_cd, assoc_recog_id, recog_id, entry_id, notes, contact_title, contact_frst_name, contact_last_name, contact_phone, contact_fax, contact_email_addr);
            commit();
            // Return success

            return prop_call_id;
        }
        catch (SQLException e) {
            sql_error = e.getErrorCode();
            isam_error = 0;
            error_data = e.getMessage();
            {
                rollback();
                throw new ProcedureException(sql_error, isam_error, error_data);
            }
        }
    }

}