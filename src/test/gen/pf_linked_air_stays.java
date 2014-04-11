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

public class pf_linked_air_stays extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public String execute(Integer acct_id_in, Integer promo_id_in, String stay_type_in, Integer prop_id_in, DateTime doa_in, Integer los_in, String rm_type_in, String srp_code_in, Double rm_revenue_in, Double fb_revenue_in, Double other_rev_in, String curr_code_in, String res_source_in, Integer stay_id_in) throws SQLException, ProcedureException {
        /*
         * pf_linked_air_stays - checks if air miles stay is linked to others
         * 
         *         $Id: pf_linked_air_stays.sql 11530 2007-04-06 19:57:03Z fbloomfi $
         */
        DateTime l_start_date;
        DateTime l_stop_date;
        Integer l_stay_id;
        String l_prop_cd;
        DateTime l_doa;
        DateTime l_dod;
        Integer l_linked_id;
        Integer l_trans_id;
        DateTime l_stay_dod;
        Integer l_max_id;
        // find stays within an 18 month window

        l_start_date = date(new DateTime()).minusDays(540);
        l_stop_date = date(new DateTime());
        l_stay_id = null;
        l_prop_cd = null;
        l_doa = null;
        l_dod = null;
        l_linked_id = null;
        l_trans_id = null;
        l_stay_dod = doa_in.plusDays(los_in);
        l_max_id = null;
        // Get the list of linked stays to consider
Iterator<Object> it0 = new find_linked_air_stays().execute(acct_id_in, l_start_date, l_stop_date).iterator();
        while (it0.hasNext()) {
            l_stay_id = (Integer) it0.next();
            l_prop_cd = (String) it0.next();
            l_doa = (DateTime) it0.next();
            l_dod = (DateTime) it0.next();
            l_linked_id = (Integer) it0.next();
            l_trans_id = (Integer) it0.next();
            PreparedStatement pstmt1 = prepareInsert(
                      "insert into recent_stay_trans_list (stay_id, prop_cd, doa, dod, linked_id, acct_trans_id)"
                    + " values (?, ?, ?, ?, nvl(?, ?), ?)");
            if (l_stay_id != null) {
                pstmt1.setInt(1, l_stay_id);
            }
            else {
                pstmt1.setNull(1, Types.JAVA_OBJECT);
            }
            if (l_prop_cd != null) {
                pstmt1.setString(2, l_prop_cd);
            }
            else {
                pstmt1.setNull(2, Types.JAVA_OBJECT);
            }
            if (l_doa != null) {
                pstmt1.setObject(3, l_doa);
            }
            else {
                pstmt1.setNull(3, Types.JAVA_OBJECT);
            }
            if (l_dod != null) {
                pstmt1.setObject(4, l_dod);
            }
            else {
                pstmt1.setNull(4, Types.JAVA_OBJECT);
            }
            if (l_linked_id != null) {
                pstmt1.setInt(5, l_linked_id);
            }
            else {
                pstmt1.setNull(5, Types.JAVA_OBJECT);
            }
            if (l_stay_id != null) {
                pstmt1.setInt(6, l_stay_id);
            }
            else {
                pstmt1.setNull(6, Types.JAVA_OBJECT);
            }
            if (l_trans_id != null) {
                pstmt1.setInt(7, l_trans_id);
            }
            else {
                pstmt1.setNull(7, Types.JAVA_OBJECT);
            }
            executeUpdate(pstmt1);
            pstmt1.close();
        }
        // since air rewards are "stay" based as opposed to

        // "revenue" based, we simply want to check for the existence

        // of a detail record for this promo within the set of linked

        // stays.

        PreparedStatement pstmt2 = prepareStatement(
                  "select max(l.id)"
                + " from recent_stay_trans_list l, acct_trans_detail d"
                + " where l.prop_cd = new get_prop_cd().execute(prop_id_in)"
                + " and l.acct_trans_id = d.acct_trans_id"
                + " and d.promo_id = ?"
                + " and  ||  ||  || ");
        
        if (promo_id_in != null) {
            pstmt2.setInt(1, promo_id_in);
        }
        else {
            pstmt2.setNull(1, Types.JAVA_OBJECT);
        }
        ResultSet rs1 = executeQuery(pstmt2);
        rs1.next();
        l_max_id = rs1.getInt(1);
        pstmt2.close();
        rs1.close();
        PreparedStatement pstmt3 = prepareStatement("drop table recent_stay_trans_list");
        executeUpdate(pstmt3);
        pstmt3.close();
        // if we don't find the current promo within the set of linked

        // stays then return "T"rue to award promo

        if (l_max_id == null) {
            return "T";
        }
        else {
            return "F";
        }
    }

}