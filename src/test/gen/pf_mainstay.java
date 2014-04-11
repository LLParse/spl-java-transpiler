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

public class pf_mainstay extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public String execute(Integer acct_id, Integer promo_id, String stay_type, Integer prop_id, DateTime doa, Integer los, String rm_type, String srp_code, Double rm_revenue, Double fb_revenue, Double other_revenue, String curr_code, String res_source, Integer stay_id) throws SQLException, ProcedureException {
        /*
         * $RCSfile$ - Validate Mainstay stay for eligibility $Revision: 9224 $ 
         * 
         * (]$[) $RCSfile$:$Revision: 9224 $ | CDATE=$Date: 2006-03-09 09:45:20 -0700 (Thu, 09 Mar 2006) $ ~
         * 
         *        Copyright (C) 2003 Choice Hotels International, Inc.
         */
        if (los < 5) {
            return "F";
        }
        // Must stay minimum of 5 nights      

        else {
            return "T";
        }
    }

}