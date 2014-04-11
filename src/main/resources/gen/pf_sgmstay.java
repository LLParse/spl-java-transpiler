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

public class pf_sgmstay extends AbstractProcedure {

	public String execute(Integer acct_id, Integer promo_id, String stay_type, String prop, Timestamp doa, Integer los, String rm_type, String srp_code, Double rm_revenue, Double fb_revenue, Double other_revenue, String curr_code, String res_source, Integer stay_id) throws SQLException, ProcedureException {		
		
		Integer chk_promo_cd;		
		Integer chk_offer_cd;		
		chk_promo_cd = null;
		chk_offer_cd = null;		
		
		if (srp_code.equals("SGM")) {			
			return "T";
		}		
		else {			
			return "F";
		}
	}

}