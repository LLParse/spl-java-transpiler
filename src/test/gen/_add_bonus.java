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

public class _add_bonus extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public Integer execute(Integer mbr_id, Integer points, Integer points_tran_id, Integer promo_id) throws SQLException, ProcedureException {
        /*
         * (]$[) $RCSfile$:$Revision: 8445 $ | CDATE=$Date: 2005-11-02 09:33:55 -0700 (Wed, 02 Nov 2005) $ ~
         *  
         *  Generate one each mbr_bonus record.
         *  
         *  Assumption is that 'BEGIN WORK' is called prior to this
         *  procedure and 'ROLLBACK/COMMIT WORK' after depending on result. Returns
         *  ticket_nbr on success. 
         * 
         * 	Copyright (C) 2000 Choice Hotels International, Inc.
         *                       All Rights Reserved
         */
        Integer bonus_tran_id;
        bonus_tran_id = null;
        // set debug file to '/tmp/_add_bonus.trace';

        // trace on;

        // Generate the bonus first.

        PreparedStatement pstmt1 = prepareInsert(
                  "insert into mbr_bonus (bonus_tran_id, mbr_id, bonus_dtime, points, points_tran_id, promo_id)"
                + " values (0, ?, current, ?, ?, ?)");
        if (mbr_id != null) {
            pstmt1.setInt(1, mbr_id);
        }
        else {
            pstmt1.setNull(1, Types.JAVA_OBJECT);
        }
        if (points != null) {
            pstmt1.setInt(2, points);
        }
        else {
            pstmt1.setNull(2, Types.JAVA_OBJECT);
        }
        if (points_tran_id != null) {
            pstmt1.setInt(3, points_tran_id);
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
        executeUpdate(pstmt1);
        pstmt1.close();
        bonus_tran_id = dbinfo("sqlca.sqlerrd1");
        return bonus_tran_id;
    }

}