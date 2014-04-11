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

public class pf_mbr_has_ax_cc extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public String execute(Integer mbr_id, Integer promo_id, String stay_type, Integer prop_id, DateTime doa, Integer los, String rm_type, String srp_code, Double rm_revenue, Double fb_revenue, Double other_revenue, String curr_code) throws SQLException, ProcedureException {
        Integer ax_cc_count;
        ax_cc_count = null;
        PreparedStatement pstmt1 = prepareStatement(
                  "select count(*)"
                + " from mbr_cc"
                + " where mbr_cc.mbr_id = ?"
                + " and mbr_cc.cc_cd = \"AX\""
                + " and mbr_cc.cc_encrypted_id != null");
        
        if (mbr_id != null) {
            pstmt1.setInt(1, mbr_id);
        }
        else {
            pstmt1.setNull(1, Types.JAVA_OBJECT);
        }
        ResultSet rs1 = executeQuery(pstmt1);
        rs1.next();
        ax_cc_count = rs1.getInt(1);
        pstmt1.close();
        rs1.close();
        if (ax_cc_count.equals(0)) {
            return "F";
        }
        else {
            return "T";
        }
    }

}