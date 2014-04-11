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

public class pf_dine2 extends AbstractProcedure {

	public String execute(Integer acct_id, Integer promo_id, String stay_type, Integer prop_id, Timestamp doa, Integer los, String rm_type, String srp_code, Double rm_revenue, Double fb_revenue, Double other_revenue, String curr_code, String res_source, Integer stay_id) throws SQLException, ProcedureException {		
		
		Integer cust_id;		
		String dc_cc_ok;		
		cust_id = null;
		dc_cc_ok = null;		
		
		cust_id = new get_pri_cust_id().execute(acct_id);		
		
		dc_cc_ok = new cust_has_cc().execute(cust_id, "DC");		
		
		if (dc_cc_ok.equals("T")) {			
			return "T";
		}		
		else {			
			return "F";
		}
	}

}