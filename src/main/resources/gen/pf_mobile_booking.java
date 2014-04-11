/* Generated on 02-25-2013 12:49:25 PM by SPLParser v0.9 */
package com.choicehotels.gen;

import java.lang.reflect.Method;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class pf_mobile_booking extends AbstractProcedure {

	public String execute(Integer acct_id, Integer promo_id, String stay_type, Integer prop_id, Timestamp doa, Integer los, String rm_type, String srp_code, Double rm_revenue, Double fb_revenue, Double other_revenue, String curr_code, String res_source, Integer p_stay_id) throws SQLException, ProcedureException {		
		
		/*
		 * $RCSfile$ -  Booking Source - $Revision: 99 $
		 * 	
		 * 	(]$[) $RCSfile$:$Revision: 99 $ | CDATE=$Date: 2012-12-19 14:14:54 -0700 (Wed, 19 Dec 2012) $ ~
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
		l_debug = new settrace().execute("pf_mobile_booking");		
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
		
		
		if ((res_source != null && res_source.equals("M")) || l_res_channel_id.equals(125)) {			
			return "T";
		}		
		else {			
			return "F";
		}
	}

}