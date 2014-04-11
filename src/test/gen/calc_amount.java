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

public class calc_amount extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public Double execute(Integer acct_id, Integer cust_id, Integer promo_id, Integer criteria, Double value, String comp_meth, Double stay_rev, String chain_id, Integer los, Double trans_unit_value, Integer stay_id) throws SQLException, ProcedureException {
        /*
         * $Id: calc_amount.sql 56787 2012-10-17 18:04:10Z rick_shepherd $
         * 
         *   Description: 
         *   This procedure returns the amount that a promotion is worth based on the
         *   computation method for the promotion. If the computation method is 'pv_revenue',
         *   this procedure returns the amount or value that is equal to amount per 
         *   criteria whole units of stay_rev. If the computation method is 'pv_flat', then
         *   this procedure simply returns the amount equal to value.
         * 
         *   If the computation is anything other than 'pv_flat' or 'pv_revenue' the amount
         *   is returned by a stored procedure whose name is the computation method 'comp_meth'.
         * 
         *   Algorithm: 
         *   If the criteria is zero then the value is the result, otherwise compute 
         *   the amount as follows.  First, truncate the stay_rev to a whole number 
         *   since the amount earned is on whole number amounts only.  Next, divide
         *   the truncated stay_rev by the criteria to get a real number. 
         *   Finally, multiply this real number by the value to get the final amount. 
         * 
         *   Examples: 
         *   1.  Ten points per dollar on a revenue of 45.91 gives 450 points. 
         *   2.  100 points per 10 dollars on a revenue of 201.12 gives 2010 points. 
         * 
         *       Copyright (C) 2003 Choice Hotels International, Inc.
         *                       All Rights Reserved
         */
        Double amount;
        amount = 0.0;
        if (comp_meth.equals("pv_flat")) {
            amount = value;
        }
        else if (comp_meth.equals("pv_revenue")) {
            amount = value * trunc(stay_rev, 0) / criteria.doubleValue();
        }
        else if (comp_meth.equals("pv_free_night")) {
            amount = new pv_free_night().execute(acct_id, cust_id, promo_id, criteria, value, stay_rev, chain_id, los, stay_id);
        }
        else if (comp_meth.equals("pv_revenue_cap")) {
            amount = new pv_revenue_cap().execute(acct_id, cust_id, promo_id, criteria, value, stay_rev, chain_id, los, stay_id);
        }
        else if (comp_meth.equals("pv_vanilla_stay_twice")) {
            amount = new pv_vanilla_stay_twice().execute(acct_id, cust_id, promo_id, criteria, value, stay_rev, chain_id, los, stay_id);
        }
        else if (comp_meth.equals("pv_fn8000")) {
            amount = pv_fn8000(acct_id, cust_id, promo_id, criteria, value, stay_rev, chain_id, los, stay_id);
        }
        else if (comp_meth.equals("pv_fn8000sp06")) {
            amount = new pv_fn8000sp06().execute(acct_id, cust_id, promo_id, criteria, value, stay_rev, chain_id, los, stay_id);
        }
        else if (comp_meth.equals("pv_fn8000su06")) {
            amount = pv_fn8000su06(acct_id, cust_id, promo_id, criteria, value, stay_rev, chain_id, los, stay_id);
        }
        else if (comp_meth.equals("pv_fn8000fl06")) {
            amount = new pv_fn8000fl06().execute(acct_id, cust_id, promo_id, criteria, value, stay_rev, chain_id, los, stay_id);
        }
        else if (comp_meth.equals("pv_fn8000consecutive")) {
            amount = new pv_fn8000consecutive().execute(acct_id, cust_id, promo_id, criteria, value, stay_rev, chain_id, los, stay_id);
        }
        else if (comp_meth.equals("pv_fnsu08m")) {
            amount = new pv_fnsu08m().execute(acct_id, cust_id, promo_id, criteria, value, stay_rev, chain_id, los, stay_id);
        }
        else if (comp_meth.equals("pv_fnfl08m")) {
            amount = new pv_fnfl08m().execute(acct_id, cust_id, promo_id, criteria, value, stay_rev, chain_id, los, stay_id);
        }
        else if (comp_meth.equals("pv_fnsp09el")) {
            amount = new pv_fnsp09el().execute(acct_id, cust_id, promo_id, criteria, value, stay_rev, chain_id, los, stay_id);
        }
        else if (comp_meth.equals("pv_fnsp09ne")) {
            amount = new pv_fnsp09ne().execute(acct_id, cust_id, promo_id, criteria, value, stay_rev, chain_id, los, stay_id);
        }
        else if (comp_meth.equals("pv_localcurrency")) {
            amount = new pv_localcurrency().execute(acct_id, cust_id, promo_id, criteria, value, stay_rev, chain_id, los, stay_id);
        }
        else if (comp_meth.equals("pv_su09m")) {
            amount = new pv_su09m().execute(acct_id, cust_id, promo_id, criteria, value, stay_rev, chain_id, los, stay_id);
        }
        else if (comp_meth.equals("pv_fl0809f")) {
            amount = new pv_fl0809f().execute(acct_id, cust_id, promo_id, criteria, value, stay_rev, chain_id, los, stay_id);
        }
        else if (comp_meth.equals("pv_sp0210")) {
            amount = new pv_sp0210().execute(acct_id, cust_id, promo_id, criteria, value, stay_rev, chain_id, los, stay_id);
        }
        else if (comp_meth.equals("pv_su0510")) {
            amount = new pv_su0510().execute(acct_id, cust_id, promo_id, criteria, value, stay_rev, chain_id, los, stay_id);
        }
        else if (comp_meth.equals("pv_fl0810")) {
            amount = new pv_fl0810().execute(acct_id, cust_id, promo_id, criteria, value, stay_rev, chain_id, los, stay_id);
        }
        else if (comp_meth.equals("pv_sp0311s")) {
            amount = new pv_sp0311s().execute(acct_id, cust_id, promo_id, criteria, value, stay_rev, chain_id, los, stay_id);
        }
        else if (comp_meth.equals("pv_su0511s")) {
            amount = new pv_su0511s().execute(acct_id, cust_id, promo_id, criteria, value, stay_rev, chain_id, los, stay_id);
        }
        else if (comp_meth.equals("pv_sp0312p")) {
            amount = new pv_sp0312p().execute(acct_id, cust_id, promo_id, criteria, value, stay_rev, chain_id, los, stay_id);
        }
        else if (comp_meth.equals("pv_su0512s")) {
            amount = new pv_su0512s().execute(acct_id, cust_id, promo_id, criteria, value, stay_rev, chain_id, los, stay_id);
        }
        else {
            throw new ProcedureException(-746, 0, "calc_amount: Unknown computation method");
        }
        // Round if neccessary

        amount = new round_up().execute(amount, trans_unit_value);
        return amount;
    }

}