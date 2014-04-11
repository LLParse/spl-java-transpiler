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

public class _post_trans extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public Integer execute(Integer acct_id, Integer cust_id, String trans_type, Integer stay_id, Integer cust_call_id, Integer redemption_id, Double amount, String stay_type) throws SQLException, ProcedureException {
        /*
         * $RCSfile$ - Insert one acct_trans record - $Revision: 5804 $
         * 
         * (]$[) $RCSfile$:$Revision: 5804 $ | CDATE=$Date: 2004-12-13 04:10:28 -0700 (Mon, 13 Dec 2004) $ ~
         * 
         *       Copyright (C) 2001 Choice Hotels International, Inc.
         *                       All Rights Reserved
         * 
         * This procedure inserts one account transaction record, and, depending on
         * partner type completes the posting process. If the partner is external and the
         * transaction is of the type to be approved, it is queued for further processing.
         * If the partner is internal, the associated account tables are updated and the
         * posting is complete.
         * 
         * This procedure must be called inside a transaction to ensure data integrity.
         */
        String external_pgm;
        Integer partner_id;
        Integer acct_trans_id;
        Integer par_id;
        external_pgm = null;
        partner_id = null;
        acct_trans_id = null;
        par_id = null;
        // set debug file to '/tmp/_post_trans.trace';

        // trace on;

        // Determine partner type associated with this transaction.

        external_pgm = new get_pgm_type().execute(acct_id);
        if (external_pgm.equals("Y")) {
            if (trans_type.equals("E") || trans_type.equals("V")) {
                throw new ProcedureException(-746, 0, "_post_trans: illegal transaction type for an extneral account");
            }
            if (amount < 0.0) {
                throw new ProcedureException(-746, 0, "_post_trans: cannot post negative transactions to an extneral account");
            }
        }
        // Post the transaction to the account

        acct_trans_id = new _ins_acct_trans().execute(acct_id, trans_type, "P", stay_id, cust_call_id, redemption_id);
        // Complete posting process based on the program type of the account 

        if (external_pgm.equals("Y")) {
            // External account, queue a partner activity request

            par_id = new _ins_par().execute(acct_id, cust_id, trans_type, acct_trans_id);
        }
        else {
            // Internal partner, set transaction status to applied

            new _apply_trans().execute(acct_id, external_pgm, acct_trans_id, trans_type, amount, stay_type);
        }
        return acct_trans_id;
    }

}