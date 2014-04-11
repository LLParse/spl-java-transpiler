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

public class pf_no_inet_bking extends AbstractProcedure {

	public String execute(Integer acct_id, Integer promo_id, String stay_type, Integer prop_id, Timestamp doa, Integer los, String rm_type, String srp_code, Double rm_revenue, Double fb_revenue, Double other_revenue, String curr_code, String res_source, Integer stay_id) throws SQLException, ProcedureException {		
		
		//set debug file to '/tmp/pf_no_inet_bking.trace';
		
		// Check the res_source to see if it's NOT set to 'N' for non-internet.
		
		if (res_source.equals("N")) {			
			return "F";
		}		// was from internet
		else {			
			return "T";
		}
	}

}