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

public class pf_chk_promo_award extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public String execute(Integer acct_id, Integer promo_id, String stay_type, Integer prop_id, DateTime doa, Integer los, String rm_type, String srp_code, Double rm_revenue, Double fb_revenue, Double other_revenue, String curr_code, String res_source, Integer stay_id) throws SQLException, ProcedureException {
        Integer bonus_cnt;
        bonus_cnt = null;
        //set debug file to '/tmp/pf_chk_promo_award.trace';

        //trace on;

        PreparedStatement pstmt1 = prepareStatement(
                  "select count(*)"
                + " from stay s, acct_trans t, acct_trans_detail d"
                + " where s.doa + s.los = ?.plusDays(?)"
                + " and s.prop_id = ?"
                + " and s.stay_id = t.stay_id"
                + " and t.acct_trans_id = d.acct_trans_id"
                + " and d.promo_id = ?"
                + " and t.acct_id = ?"
                + " and t.rev_acct_trans_id is null");
        
        if (doa != null) {
            pstmt1.setObject(1, doa);
        }
        else {
            pstmt1.setNull(1, Types.JAVA_OBJECT);
        }
        if (los != null) {
            pstmt1.setInt(2, los);
        }
        else {
            pstmt1.setNull(2, Types.JAVA_OBJECT);
        }
        if (prop_id != null) {
            pstmt1.setInt(3, prop_id);
        }
        else {
            pstmt1.setNull(3, Types.JAVA_OBJECT);
        }
        if (promo_id != null) {
            pstmt1.setInt(4, promo_id);
        }
        else {
            pstmt1.setNull(4, Types.JAVA_OBJECT);
        }
        if (acct_id != null) {
            pstmt1.setInt(5, acct_id);
        }
        else {
            pstmt1.setNull(5, Types.JAVA_OBJECT);
        }
        ResultSet rs1 = executeQuery(pstmt1);
        rs1.next();
        bonus_cnt = rs1.getInt(1);
        pstmt1.close();
        rs1.close();
        if (bonus_cnt > 0) {
            return "F";
        }
        return "T";
    }

}