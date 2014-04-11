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

public class get_acct_summary extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public Collection<Object> execute(String recog_cd, String recog_id) throws SQLException, ProcedureException {
        //point balance

        /*
         * (]$[) $RCSfile$:$Revision: 8604 $ | CDATE=$Date: 2005-11-29 12:19:55 -0700 (Tue, 29 Nov 2005) $ ~
         * 
         *   get_acct_summary -  get account summary information
         * 
         *       Copyright (C) 2002 Choice Hotels International, Inc.
         *                       All Rights Reserved
         */
        Integer cust_id;
        Integer acct_id;
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
        String hphone;
        String bphone;
        String phone;
        Integer acct_bal;
        //  set debug file to '/tmp/get_acct_summary.trace';

        //  trace on;

        cust_id = null;
        acct_id = null;
        alt_acct_num = null;
        //Perform an alternate lookup for old CP Canada numbers.

        //Use the normal lookup for all others.

        if (recog_cd.equals("CN") && length(recog_id) <= 8) {
            PreparedStatement pstmt1 = prepareStatement(
                      "select a.acct_id, a.recog_id"
                    + " from acct a"
                    + " where a.recog_cd = ?"
                    + " and a.alt_recog_id = ?");
            
            if (recog_cd != null) {
                pstmt1.setString(1, recog_cd);
            }
            else {
                pstmt1.setNull(1, Types.JAVA_OBJECT);
            }
            if (recog_id != null) {
                pstmt1.setString(2, recog_id);
            }
            else {
                pstmt1.setNull(2, Types.JAVA_OBJECT);
            }
            ResultSet rs1 = executeQuery(pstmt1);
            rs1.next();
            acct_id = rs1.getInt(1);
            alt_acct_num = rs1.getString(2);
            pstmt1.close();
            rs1.close();
        }
        else {
            PreparedStatement pstmt2 = prepareStatement(
                      "select a.acct_id"
                    + " from acct a"
                    + " where a.recog_cd = ?"
                    + " and a.recog_id = ?");
            
            if (recog_cd != null) {
                pstmt2.setString(1, recog_cd);
            }
            else {
                pstmt2.setNull(1, Types.JAVA_OBJECT);
            }
            if (recog_id != null) {
                pstmt2.setString(2, recog_id);
            }
            else {
                pstmt2.setNull(2, Types.JAVA_OBJECT);
            }
            ResultSet rs2 = executeQuery(pstmt2);
            rs2.next();
            acct_id = rs2.getInt(1);
            pstmt2.close();
            rs2.close();
        }
        if (acct_id == null) {
            return new ArrayList<Object>(Arrays.<Object>asList(null, null, null, null, null, null, null, null, null, null, null, null, null, null));
        }
        acct_status = null;
        frst_name = null;
        mid_initial = null;
        last_name = null;
        hphone = null;
        bphone = null;
        acct_bal = null;
        PreparedStatement pstmt3 = prepareStatement(
                  "select c.cust_id, decode(a.acct_status, \"R\", \"PENDING\", \"X\", \"REJECTED\", \"A\", \"ACTIVE\", \"I\", \"INACTIVE\", \"T\", \"TERMINATED\"), c.frst_name, c.mid_initial, c.last_name, c.hphone, c.bphone, new get_acct_bal().execute(a.acct_id)"
                + " from acct a, cust c"
                + " where a.acct_id = ?"
                + " and c.cust_id = new get_pri_cust_id().execute(a.acct_id)");
        
        if (acct_id != null) {
            pstmt3.setInt(1, acct_id);
        }
        else {
            pstmt3.setNull(1, Types.JAVA_OBJECT);
        }
        ResultSet rs3 = executeQuery(pstmt3);
        rs3.next();
        cust_id = rs3.getInt(1);
        acct_status = rs3.getString(2);
        frst_name = rs3.getString(3);
        mid_initial = rs3.getString(4);
        last_name = rs3.getString(5);
        hphone = rs3.getString(6);
        bphone = rs3.getString(7);
        acct_bal = rs3.getInt(8);
        pstmt3.close();
        rs3.close();
        phone = null;
        if (hphone != null) {
            phone = hphone;
        }
        else if (bphone != null) {
            phone = bphone;
        }
        addr_type = null;
        addr_1 = null;
        addr_2 = null;
        city = null;
        state = null;
        country = null;
        zip = null;
        PreparedStatement pstmt4 = prepareStatement(
                  "select decode(a.addr_type, \"H\", \"HOME\", \"B\", \"BUSINESS\"), a.addr_1, a.addr_2, a.city, s.name, c.name, a.zip"
                + " from cust_addr a, state s, country c"
                + " where a.cust_id = ?"
                + " and a.state = s.state_cd"
                + " and a.country = s.country_cd"
                + " and a.country = c.country_cd"
                + " and a.primary_addr = \"Y\"");
        
        if (cust_id != null) {
            pstmt4.setInt(1, cust_id);
        }
        else {
            pstmt4.setNull(1, Types.JAVA_OBJECT);
        }
        ResultSet rs4 = executeQuery(pstmt4);
        rs4.next();
        addr_type = rs4.getString(1);
        addr_1 = rs4.getString(2);
        addr_2 = rs4.getString(3);
        city = rs4.getString(4);
        state = rs4.getString(5);
        country = rs4.getString(6);
        zip = rs4.getString(7);
        pstmt4.close();
        rs4.close();
        return new ArrayList<Object>(Arrays.<Object>asList(acct_status, alt_acct_num, frst_name, mid_initial, last_name, addr_type, addr_1, addr_2, city, state, country, zip, phone, acct_bal));
    }

}