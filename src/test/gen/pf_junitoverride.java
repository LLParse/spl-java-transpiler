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

public class pf_junitoverride extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public String execute(Integer in_acct_id, Integer in_promo_id, String in_stay_type, Integer in_prop_id, DateTime in_doa, Integer in_los, String in_rm_type, String in_srp_code, Double in_rm_revenue, Double in_fb_revenue, Double in_other_revenue, String in_curr_code, String in_res_source, Integer in_stay_id) throws SQLException, ProcedureException {
        /*
         * pf_junitoverride.sql - A promotion filter for the special junit override promotion. Always returns
         *                        false so bonus is never awarded. This allows any stay to be eligible for an 
         *                        override. Used for Selenium/JUNIT testing.
         * 
         * $Id: pf_junitoverride.sql 48496 2012-02-28 00:47:19Z rick_shepherd $
         * 
         *      Copyright (C) 2012 Choice Hotels International, Inc.
         *      All Rights Reserved
         */
        return "F";
    }

}