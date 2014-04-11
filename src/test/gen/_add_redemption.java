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

public class _add_redemption extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public Integer execute(Integer acct_id, Integer cust_id, Integer entry_id, Integer last_update_id, Integer award_proc_id, Integer award_id, String express_mail, Integer promo_id_in) throws SQLException, ProcedureException {
        /*
         * $RCSfile$ - Add a redeption - $Revision: 19842 $
         * 
         *  (]$[) $RCSfile$:$Revision: 19842 $ | CDATE=$Date: 2008-11-18 11:15:56 -0700 (Tue, 18 Nov 2008) $ ~
         *  
         *  Generate one redemption record
         *  
         *  Assumption is that 'BEGIN WORK' is called prior to this
         *  procedure and 'ROLLBACK/COMMIT WORK' after depending on result.
         * 
         * 	Copyright (C) 2001 Choice Hotels International, Inc.
         *                       All Rights Reserved
         */
        Integer redemption_id;
        redemption_id = null;
        // set debug file to '/tmp/_add_redemption.trace';

        // trace on;

        PreparedStatement pstmt1 = prepareInsert(
                  "insert into redemption (redemption_id, cust_id, redeem_dtime, award_id, award_proc_id, express_mail, fulfillment_date, promo_id, entry_id, last_update_dtime, last_update_id)"
                + " values (0, ?, current, ?, ?, ?, null, ?, ?, current, ?)");
        if (cust_id != null) {
            pstmt1.setInt(1, cust_id);
        }
        else {
            pstmt1.setNull(1, Types.JAVA_OBJECT);
        }
        if (award_id != null) {
            pstmt1.setInt(2, award_id);
        }
        else {
            pstmt1.setNull(2, Types.JAVA_OBJECT);
        }
        if (award_proc_id != null) {
            pstmt1.setInt(3, award_proc_id);
        }
        else {
            pstmt1.setNull(3, Types.JAVA_OBJECT);
        }
        if (express_mail != null) {
            pstmt1.setString(4, express_mail);
        }
        else {
            pstmt1.setNull(4, Types.JAVA_OBJECT);
        }
        if (promo_id_in != null) {
            pstmt1.setInt(5, promo_id_in);
        }
        else {
            pstmt1.setNull(5, Types.JAVA_OBJECT);
        }
        if (entry_id != null) {
            pstmt1.setInt(6, entry_id);
        }
        else {
            pstmt1.setNull(6, Types.JAVA_OBJECT);
        }
        if (entry_id != null) {
            pstmt1.setInt(7, entry_id);
        }
        else {
            pstmt1.setNull(7, Types.JAVA_OBJECT);
        }
        executeUpdate(pstmt1);
        pstmt1.close();
        redemption_id = dbinfo("sqlca.sqlerrd1");
        return redemption_id;
    }

}