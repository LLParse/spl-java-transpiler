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

public class update_cumul extends AbstractProcedure {

	public String execute(String trans_type, String stay_type) throws SQLException, ProcedureException {		
		
		/*
		 * $RCSfile$ - Update cumulative balance? - $Revision: 99 $
		 * 
		 * (]$[) $RCSfile$:$Revision: 99 $ | CDATE=$Date: 2012-12-19 14:14:54 -0700 (Wed, 19 Dec 2012) $ ~
		 * 
		 *   Returns 'Y' or 'N' depending on if the transaction award amount is used to
		 *   adjust the members' lifetime account balance.
		 * 
		 *   Partner associated with transaction must be an internal partner.
		 * 
		 *   Trans_type 
		 *   
		 *   'S' - yes if associated stay record is type 'N'ormal or 'F'olio
		 *   'B' - yes on all award bonuses
		 *   'R' - no on all redemptions
		 *   'A' - yes on all adjustments
		 *   'E' - no on all account expirations
		 *   'V' - invalid, must use originating transaction type.
		 */		
		
		String update_cumul;		
		
		update_cumul = "N";		// Default to not update lifetime account balance
		
		if (trans_type.equals("V")) {			
			throw new ProcedureException(-746, 0, "update_cumul: Invalid transaction type 'V'");
		}		
		
		// If transaction is stay, check if type redemption or cancellation
		if (trans_type.equals("S")) {			
			// Consider only normal or folio stays
			if (stay_type.equals("N") || stay_type.equals("F")) {				
				update_cumul = "Y";
			}
		}		// looking at stays
		
		if (trans_type.equals("B") || trans_type.equals("A")) {			
			update_cumul = "Y";
		}		
		
		return update_cumul;
	}

}