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

public class pf_sgm01 extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public String execute(Integer mbr_id, String promo_code, String stay_type, String prop, DateTime doa, Integer los, String rm_type, String srp_code, Double rm_revenue, Double fb_revenue, Double other_revenue, String curr_code, String res_source, Integer stay_id) throws SQLException, ProcedureException {
        Integer chk_promo_cd;
        Integer chk_offer_cd;
        chk_promo_cd = null;chk_offer_cd = null;
        //update statistics for procedure pf_sgm01;

        //set debug file to '/tmp/pf_sgm01.trace';

        // Check if the member has been awarded activation bonus already. 

        //trace 'mbr_id = ' || mbr_id;

        PreparedStatement pstmt1 = prepareStatement(
                  "select count(*)"
                + " from mbr_bonus mb"
                + " where mb.promo_cd = \"SGM01\""
                + " and mb.mbr_id = ?");
        
        if (mbr_id != null) {
            pstmt1.setInt(1, mbr_id);
        }
        else {
            pstmt1.setNull(1, Types.JAVA_OBJECT);
        }
        ResultSet rs1 = executeQuery(pstmt1);
        rs1.next();
        chk_promo_cd = rs1.getInt(1);
        pstmt1.close();
        rs1.close();
        //trace 'chk_promo_cd = ' || chk_promo_cd;

        if (chk_promo_cd > 0) {
            return "F";
        }
        // Determine if the member has been offered the promo  SGM01

        // Obtain required parameters

        PreparedStatement pstmt2 = prepareStatement(
                  "select count(*)"
                + " from mbr_offer"
                + " where mbr_offer.mbr_id = ?"
                + " and mbr_offer.offer_cd = \"SGM01\"");
        
        if (mbr_id != null) {
            pstmt2.setInt(1, mbr_id);
        }
        else {
            pstmt2.setNull(1, Types.JAVA_OBJECT);
        }
        ResultSet rs2 = executeQuery(pstmt2);
        rs2.next();
        chk_offer_cd = rs2.getInt(1);
        pstmt2.close();
        rs2.close();
        //trace 'chk_offer_cd = ' || chk_offer_cd;

        if (chk_offer_cd <= 0) {
            return "F";
        }
        if (srp_code.equals("SGM")) {
            return "T";
        }
        else {
            return "F";
        }
    }

}