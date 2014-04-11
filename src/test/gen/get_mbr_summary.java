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

public class get_mbr_summary extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public Collection<Object> execute(String recog_cd, String recog_id) throws SQLException, ProcedureException {
        //point balance

        /*
         * (]$[) $RCSfile$:$Revision: 8604 $ | CDATE=$Date: 2005-11-29 12:19:55 -0700 (Tue, 29 Nov 2005) $ ~
         * 
         *   get_mbr_summary -  get member summary information
         * 
         *       Copyright (C) 2002 Choice Hotels International, Inc.
         *                       All Rights Reserved
         */
        String acct_status;
        String alt_acct_num;
        String frst_name;
        String mid_initial;
        String last_name;
        String addr_type;
        String addr_1;
        String addr_2;
        String city;
        String state;
        String country;
        String zip;
        String phone;
        Integer acct_bal;
        //  set debug file to '/tmp/get_mbr_summary.trace';

        //  trace on;

        acct_status = null;
        alt_acct_num = null;
        frst_name = null;
        mid_initial = null;
        last_name = null;
        addr_type = null;
        addr_1 = null;
        addr_2 = null;
        city = null;
        state = null;
        country = null;
        zip = null;
        phone = null;
        acct_bal = null;
        Iterator<Object> it0 = new get_acct_summary().execute(recog_cd, recog_id).iterator();
        acct_status = (String) it0.next();
        alt_acct_num = (String) it0.next();
        frst_name = (String) it0.next();
        mid_initial = (String) it0.next();
        last_name = (String) it0.next();
        addr_type = (String) it0.next();
        addr_1 = (String) it0.next();
        addr_2 = (String) it0.next();
        city = (String) it0.next();
        state = (String) it0.next();
        country = (String) it0.next();
        zip = (String) it0.next();
        phone = (String) it0.next();
        acct_bal = (Integer) it0.next();
        return new ArrayList<Object>(Arrays.<Object>asList(acct_status, alt_acct_num, frst_name, mid_initial, last_name, addr_type, addr_1, addr_2, city, state, country, zip, phone, acct_bal));
    }

}