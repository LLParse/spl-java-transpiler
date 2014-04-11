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

public class pf_is_wknd_stay extends AbstractProcedure {

	public String execute(Integer acct_id, Integer promo_id, String stay_type, Integer prop_id, Timestamp doa, Integer los, String rm_type, String srp_code, Double rm_revenue, Double fb_revenue, Double other_revenue, String curr_code, String res_source, Integer stay_id) throws SQLException, ProcedureException {		
		
		/*
		 * 	pf_is_wknd_stay.sql	- Check that some or all of stay falls on a weekend night, Friday, Saturday,
		 *     or Sunday. If any, return True. 
		 * 	(]$[) $RCSfile$:$Revision: 99 $ | CDATE=$Date: 2012-12-19 14:14:54 -0700 (Wed, 19 Dec 2012) $ ~
		 */		
		Integer day;		
		Timestamp stay_date;		
		day = null;		
		stay_date = null;
		for (day = 0; day <= los; day++) {			
			stay_date = doa + day;			
			if (((weekday(stay_date).equals(0) || weekday(stay_date).equals(5) || weekday(stay_date).equals(6)))) {				
				return "T";
			}
		}		
		return "F";
	}

}