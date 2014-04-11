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

public class get_partner_id extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public Integer execute(String partner_cd) throws SQLException, ProcedureException {
        /*
         * Get the partner id corresponding to the
         *   specified partner code.
         */
        Integer partner_id;
        partner_id = null;
        //lookup the partner_id by partner_cd

        PreparedStatement pstmt1 = prepareStatement(
                  "select partner.partner_id"
                + " from partner"
                + " where partner.partner_cd = ?");
        
        if (partner_cd != null) {
            pstmt1.setString(1, partner_cd);
        }
        else {
            pstmt1.setNull(1, Types.JAVA_OBJECT);
        }
        ResultSet rs1 = executeQuery(pstmt1);
        rs1.next();
        partner_id = rs1.getInt(1);
        pstmt1.close();
        rs1.close();
        //return the partner id

        return partner_id;
    }

}