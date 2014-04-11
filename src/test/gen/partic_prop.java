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

public class partic_prop extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public String execute(String recog_cd, Integer prop_id) throws SQLException, ProcedureException {
        /*
         * (]$[) $RCSfile$:$Revision: 5804 $ | CDATE=$Date: 2004-12-13 04:10:28 -0700 (Mon, 13 Dec 2004) $ ~
         *   Check if the property participates in the specified recognition program.  
         * 
         *       Copyright (C) 2003 Choice Hotels International, Inc.
         *                       All Rights Reserved
         */
        // Data from recog_prop_partic

        String r_country;
        String r_master_fran;
        String r_control_ofc;
        String r_mgt_company;
        String r_chain_id;
        // Data from prop

        String p_country;
        String p_master_fran;
        String p_control_ofc;
        String p_mgt_company;
        String p_chain_id;
        r_country = null;
        r_master_fran = null;
        r_control_ofc = null;
        r_mgt_company = null;
        r_chain_id = null;
        p_country = null;
        p_master_fran = null;
        p_control_ofc = null;
        p_mgt_company = null;
        p_chain_id = null;
        // Get the data

        PreparedStatement pstmt1 = prepareStatement(
                  "select country, master_fran, cntrl_ofc, mgt_company, chain_id"
                + " from prop"
                + " where prop.prop_id = ?");
        
        if (prop_id != null) {
            pstmt1.setInt(1, prop_id);
        }
        else {
            pstmt1.setNull(1, Types.JAVA_OBJECT);
        }
        ResultSet rs1 = executeQuery(pstmt1);
        rs1.next();
        p_country = rs1.getString(1);
        p_master_fran = rs1.getString(2);
        p_control_ofc = rs1.getString(3);
        p_mgt_company = rs1.getString(4);
        p_chain_id = rs1.getString(5);
        pstmt1.close();
        rs1.close();
        // set empty strings to null

        if (length(trim(p_country)).equals(0)) {
            p_country = null;
        }
        if (length(trim(p_master_fran)).equals(0)) {
            p_master_fran = null;
        }
        if (length(trim(p_control_ofc)).equals(0)) {
            p_control_ofc = null;
        }
        if (length(trim(p_mgt_company)).equals(0)) {
            p_mgt_company = null;
        }
        if (length(trim(p_chain_id)).equals(0)) {
            p_chain_id = null;
        }
        PreparedStatement pstmt2 = prepareStatement(
                  "select country, master_fran, cntrl_ofc, mgt_company, chain_id"
                + " from recog_prop_partic"
                + " where recog_prop_partic.recog_cd = ?");
        
        if (recog_cd != null) {
            pstmt2.setString(1, recog_cd);
        }
        else {
            pstmt2.setNull(1, Types.JAVA_OBJECT);
        }
        ResultSet rs2 = executeQuery(pstmt2);
        while (rs2.next()) {
            r_country = rs2.getString(1);
            r_master_fran = rs2.getString(2);
            r_control_ofc = rs2.getString(3);
            r_mgt_company = rs2.getString(4);
            r_chain_id = rs2.getString(5);
            if (r_country != null) {
                if (p_country != null) {
                    if (!p_country.equals(r_country)) {
                        continue;
                    }
                }
                else {
                    continue;
                }
            }
            if (r_master_fran != null) {
                if (p_master_fran != null) {
                    if (!p_master_fran.equals(r_master_fran)) {
                        continue;
                    }
                }
                else {
                    continue;
                }
            }
            if (r_control_ofc != null) {
                if (p_control_ofc != null) {
                    if (!p_control_ofc.equals(r_control_ofc)) {
                        continue;
                    }
                }
                else {
                    continue;
                }
            }
            if (r_mgt_company != null) {
                if (p_mgt_company != null) {
                    if (!p_mgt_company.equals(r_mgt_company)) {
                        continue;
                    }
                }
                else {
                    continue;
                }
            }
            if (r_chain_id != null) {
                if (p_chain_id != null) {
                    if (!p_chain_id.equals(r_chain_id)) {
                        continue;
                    }
                }
                else {
                    continue;
                }
            }
            // to make it here is to survive all property checks for one record

            return "Y";
        }
        pstmt2.close();
        rs2.close();
        // looked at all the records and none worked.

        return "N";
    }

}