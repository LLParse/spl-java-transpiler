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

public class pf_amex extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public String execute(Integer acct_id, Integer promo_id, String stay_type, Integer prop_id, DateTime doa, Integer los, String rm_type, String srp_code, Double rm_revenue, Double fb_revenue, Double other_revenue, String curr_code, String res_source, Integer stay_id) throws SQLException, ProcedureException {
        /*
         * TITLE $RCSfile$ - AMEX promo filter - $Revision: 12170 $ 
         * 
         * (]$[) $RCSfile$:$Revision: 12170 $ | CDATE=$Date: 2007-06-28 08:43:03 -0700 (Thu, 28 Jun 2007) $ ~
         * 
         *       Copyright (C) 2000 Choice Hotels International, Inc.
         *                       All Rights Reserved
         */
        Integer cust_id;
        String ax_cc_ok;
        String first_stay_ok;
        // Initialize defined variables to null

        cust_id = null;ax_cc_ok = null;first_stay_ok = null;
        cust_id = new get_pri_cust_id().execute(acct_id);
        ax_cc_ok = new cust_has_cc().execute(cust_id, "AX");
        first_stay_ok = new pf_firststayever().execute(acct_id, promo_id, stay_type, prop_id, doa, los, rm_type, srp_code, rm_revenue, fb_revenue, other_revenue, curr_code, res_source, stay_id);
        if (ax_cc_ok.equals("T") && first_stay_ok.equals("T")) {
            return "T";
        }
        else {
            return "F";
        }
    }

}