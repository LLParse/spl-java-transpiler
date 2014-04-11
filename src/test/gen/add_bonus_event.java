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

public class add_bonus_event extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public Collection<Object> execute(Integer acct_id, Integer cust_id, String promo_cd, String force_bonus, Integer filter_data) throws SQLException, ProcedureException {
        /*
         * (]$[) $RCSfile$:$Revision: 20839 $ | CDATE=$Date: 2009-02-23 08:34:37 -0700 (Mon, 23 Feb 2009) $ ~
         * 
         *   add_bonus_event - Apply a promotional bonus to a customer's account
         * 
         *       Copyright (C) 2000 Choice Hotels International, Inc.
         *                       All Rights Reserved
         */
        Integer acct_trans_id;
        Double trans_amount;
        String acct_status;
        String recog_cd;
        Integer promo_id;
        Integer sql_error;
        Integer isam_error;
        String error_data;
        String val_cust_acct;
        try {
            acct_trans_id = null;
            trans_amount = null;
            acct_status = null;
            recog_cd = null;
            promo_id = null;
            val_cust_acct = null;
            // set debug file to '/tmp/add_bonus_event.trace';

            // trace on;

            // Validate customer and account relationship

            val_cust_acct = new chk_cust_acct().execute(acct_id, cust_id);
            if (val_cust_acct.equals("N")) {
                throw new ProcedureException(-746, 0, "add_bonus_event: Customer not linked to account.");
            }
            // Next validate the members' association with this promo.

            recog_cd = new get_recog_cd().execute(acct_id);
            if (recog_cd == null) {
                throw new ProcedureException(-746, 0, "add_bonus_event: Unable to determine recognition code");
            }
            // Validate the promo against this member.

            promo_id = new get_promo_id().execute(recog_cd, promo_cd);
            if (promo_id == null) {
                throw new ProcedureException(-746, 0, "add_bonus_event: Invalid promo cd");
            }
            // Try and apply the promotion to the members' account.

            Iterator<Object> it0 = new _proc_bonus_event().execute(acct_id, cust_id, promo_id, force_bonus, filter_data).iterator();
            acct_trans_id = (Integer) it0.next();
            trans_amount = (Double) it0.next();
            // Return the result.

            return new ArrayList<Object>(Arrays.<Object>asList(acct_trans_id, trans_amount));
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