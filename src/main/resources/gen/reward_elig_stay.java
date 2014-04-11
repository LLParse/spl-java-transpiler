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

public class reward_elig_stay extends AbstractProcedure {

	public String execute(String recog_cd, Integer prop_id, String stay_type, String cxl_flag, String srp_code, String denial) throws SQLException, ProcedureException {		
		
		/*
		 * (]$[) $RCSfile$:$Revision: 99 $ | CDATE=$Date: 2012-12-19 14:14:54 -0700 (Wed, 19 Dec 2012) $ ~
		 * 
		 *   Check reward eligibility for a stay. Returns 'Y' if eligible for reward, 'N' 
		 *   if not. The following are checked:
		 * 
		 *   Must not be a denial.
		 *   The type of stay, must be 'N'ormal or 'F'olio.
		 *   Must not be cancelled.
		 *   The SRP, if present must be reward eligible. 
		 *   The property against the recognition program.
		 */		
		
		// set debug file to '/tmp/reward_elig_stay.trace';
		// trace on;
		
		// Check the denial indicator
		if (denial.equals("Y")) {			
			return "N";
		}		
		
		// Check stay type
		if (!stay_type.equals("N") && !stay_type.equals("F")) {			
			return "N";
		}		
		
		// Check if cancelled
		if (!cxl_flag.equals("S")) {			
			if ((cxl_flag.equals("X") || cxl_flag.equals("N")) && ((!srp_code.equals("SADV")) && (!srp_code.equals("LADV")))) {				
				return "N";
			}
		}		
		
		// Check the SRP
		if (new elig_srp().execute(recog_cd, srp_code).equals("N")) {			
			return "N";
		}		
		
		// All checks passed
		return "Y";
	}

}