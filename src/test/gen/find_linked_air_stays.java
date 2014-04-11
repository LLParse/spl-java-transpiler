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

public class find_linked_air_stays extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public Collection<Object> execute(Integer acct_id_in, DateTime begin_date_in, DateTime end_date_in) throws SQLException, ProcedureException {
        /*
         * find_linked_air_stays.sql - iterates through stays and links concurrent and consequtive stays
         *           
         * $Id: find_linked_air_stays.sql 31220 2010-08-27 15:29:26Z rshepher $
         *    
         *        Copyright (C) 2005 Choice Hotels International, Inc.
         */
        Integer f_id;
        DateTime f_doa;
        DateTime f_dod;
        String f_prop;
        Integer f_link;
        Integer n_id;
        Integer n_link;
        Integer n_cnt;
        Integer f_trans_id;
        f_id = null;
        f_doa = null;
        f_dod = null;
        f_prop = null;
        f_link = null;
        n_id = null;
        n_link = null;
        n_cnt = null;
        f_trans_id = null;
        //    set debug file to '/tmp/find_linked_air_stays.trace';

        //    trace on;

        if (begin_date_in == null) {
            begin_date_in = date(new DateTime().minusDays(540));
        }
        if (end_date_in == null) {
            end_date_in = date(new DateTime());
        }
        PreparedStatement pstmt1 = prepareStatement(
                  "select s.stay_id, new get_prop_cd().execute(s.prop_id), s.doa, s.doa + s.los, -1, t.acct_trans_id"
                + " from stay s, acct_trans t"
                + " where s.stay_id = t.stay_id"
                + " and t.acct_id = ?"
                + " and t.rev_acct_trans_id is null"
                + " and new get_acct_trans_sum().execute(t.acct_trans_id) > 0 || s.srp_code = \"SRD\""
                + " and s.doa >= ?"
                + " and s.doa <= ?"
                + " order by doa, stay_id");
        
        if (acct_id_in != null) {
            pstmt1.setInt(1, acct_id_in);
        }
        else {
            pstmt1.setNull(1, Types.JAVA_OBJECT);
        }
        if (begin_date_in != null) {
            pstmt1.setObject(2, begin_date_in);
        }
        else {
            pstmt1.setNull(2, Types.JAVA_OBJECT);
        }
        if (end_date_in != null) {
            pstmt1.setObject(3, end_date_in);
        }
        else {
            pstmt1.setNull(3, Types.JAVA_OBJECT);
        }
        ResultSet rs1 = executeQuery(pstmt1);
        while (rs1.next()) {
            f_id = rs1.getInt(1);
            f_prop = rs1.getString(2);
            f_doa = new DateTime(rs1.getTimestamp(3).getTime());
            f_dod = new DateTime(rs1.getTimestamp(4).getTime());
            f_link = rs1.getInt(5);
            f_trans_id = rs1.getInt(6);
            PreparedStatement pstmt2 = prepareStatement(
                      "select max(l.id)"
                    + " from linked_stays_tmp l"
                    + " where l.prop_cd = ?"
                    + " and  || ");
            
            if (f_prop != null) {
                pstmt2.setString(1, f_prop);
            }
            else {
                pstmt2.setNull(1, Types.JAVA_OBJECT);
            }
            ResultSet rs2 = executeQuery(pstmt2);
            rs2.next();
            n_id = rs2.getInt(1);
            pstmt2.close();
            rs2.close();
            if (n_id != null) {
                PreparedStatement pstmt3 = prepareStatement(
                          "select nvl(linked_id, stay_id)"
                        + " from linked_stays_tmp"
                        + " where id = ?");
                
                if (n_id != null) {
                    pstmt3.setInt(1, n_id);
                }
                else {
                    pstmt3.setNull(1, Types.JAVA_OBJECT);
                }
                ResultSet rs3 = executeQuery(pstmt3);
                rs3.next();
                n_link = rs3.getInt(1);
                pstmt3.close();
                rs3.close();
            }
            else {
                n_link = f_id;
            }
            PreparedStatement pstmt4 = prepareInsert(
                      "insert into linked_stays_tmp (stay_id, prop_cd, doa, dod, linked_id)"
                    + " values (?, ?, ?, ?, ?)");
            if (f_id != null) {
                pstmt4.setInt(1, f_id);
            }
            else {
                pstmt4.setNull(1, Types.JAVA_OBJECT);
            }
            if (f_prop != null) {
                pstmt4.setString(2, f_prop);
            }
            else {
                pstmt4.setNull(2, Types.JAVA_OBJECT);
            }
            if (f_doa != null) {
                pstmt4.setObject(3, f_doa);
            }
            else {
                pstmt4.setNull(3, Types.JAVA_OBJECT);
            }
            if (f_dod != null) {
                pstmt4.setObject(4, f_dod);
            }
            else {
                pstmt4.setNull(4, Types.JAVA_OBJECT);
            }
            if (n_link != null) {
                pstmt4.setInt(5, n_link);
            }
            else {
                pstmt4.setNull(5, Types.JAVA_OBJECT);
            }
            executeUpdate(pstmt4);
            pstmt4.close();
            return new ArrayList<Object>(Arrays.<Object>asList(f_id, f_prop, f_doa, f_dod, n_link, f_trans_id));
        }
        pstmt1.close();
        rs1.close();
        PreparedStatement pstmt5 = prepareStatement("drop table linked_stays_tmp");
        executeUpdate(pstmt5);
        pstmt5.close();
    }

}