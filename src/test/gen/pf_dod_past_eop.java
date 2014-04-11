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

public class pf_dod_past_eop extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public String execute(Integer acct_id, Integer promo_id, String stay_type, Integer prop_id, DateTime doa, Integer los, String rm_type, String srp_code, Double rm_revenue, Double fb_revenue, Double other_revenue, String curr_code, String res_source, Integer stay_id) throws SQLException, ProcedureException {
        DateTime stop_date;
        stop_date = null;
        //set debug file to '/tmp/pf_dod_past_eop.trace';

        // Check that the date of departure is not past the end of the promotion.

        PreparedStatement pstmt1 = prepareStatement(
                  "select p.stop_date"
                + " from promo p"
                + " where p.promo_id = ?");
        
        if (promo_id != null) {
            pstmt1.setInt(1, promo_id);
        }
        else {
            pstmt1.setNull(1, Types.JAVA_OBJECT);
        }
        ResultSet rs1 = executeQuery(pstmt1);
        rs1.next();
        stop_date = new DateTime(rs1.getTimestamp(1).getTime());
        pstmt1.close();
        rs1.close();
        if (doa.plusDays(los) > stop_date) {
            return "F";
        }
        return "T";
    }

}