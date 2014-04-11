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

public class pf_mobile_booking extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public String execute(Integer acct_id, Integer promo_id, String stay_type, Integer prop_id, DateTime doa, Integer los, String rm_type, String srp_code, Double rm_revenue, Double fb_revenue, Double other_revenue, String curr_code, String res_source, Integer p_stay_id) throws SQLException, ProcedureException {
        /*
         * $RCSfile$ -  Booking Source - $Revision: 39481 $
         * 	
         * 	(]$[) $RCSfile$:$Revision: 39481 $ | CDATE=$Date: 2011-06-01 17:13:17 -0700 (Wed, 01 Jun 2011) $ ~
         * 	
         * 	       Copyright (C) 2003 Choice Hotels International, Inc.
         * 	     All Rights Reserved
         * 	
         */
        Integer l_res_channel_id;
        String l_debug;
        l_res_channel_id = -1;
        l_debug = "F";
        // set up tracing based on app_config entry

        l_debug = (String) new settrace().execute("pf_mobile_booking");
        if (l_debug.equals("T")) {
            setDebugFile("/tmp/pf_mobile_booking.trace");
            trace("on");
        }
        // Check the res_source or res_channel to see if they are set for mobile.

        PreparedStatement pstmt1 = prepareStatement(
                  "select reservation_channel_id"
                + " from stay"
                + " where stay_id = ?");
        
        if (p_stay_id != null) {
            pstmt1.setInt(1, p_stay_id);
        }
        else {
            pstmt1.setNull(1, Types.JAVA_OBJECT);
        }
        ResultSet rs1 = executeQuery(pstmt1);
        rs1.next();
        l_res_channel_id = rs1.getInt(1);
        pstmt1.close();
        rs1.close();
        if (res_source != null && res_source.equals("M") || l_res_channel_id.equals(125)) {
            return "T";
        }
        else {
            return "F";
        }
    }

}