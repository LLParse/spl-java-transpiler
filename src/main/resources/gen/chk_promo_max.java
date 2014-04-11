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

public class chk_promo_max extends AbstractProcedure {

	public Integer execute(Integer acct_id, Integer promo_id, Integer max_uses) throws SQLException, ProcedureException {		
		
		/*
		 * (]$[) $RCSfile$:$Revision: 99 $ | CDATE=$Date: 2012-12-19 14:14:54 -0700 (Wed, 19 Dec 2012) $ ~
		 *   chk_promo_max looks at number of times a promotion was
		 *   awarded. returns 1 if below max , 0 if at max
		 * 
		 *         Copyright (C) 2003 Choice Hotels International, Inc.
		 *                       All Rights Reserved
		 */		
		
		Integer count;		
		
		// If max_uses is null the promotion is unlimited
		if (max_uses == null) {			
			return 1;
		}		
		
		// There is a limit check it ignoring the reversed transactions
		count = new chk_promo_use_cnt().execute(promo_id, acct_id);		
		
		if (count >= max_uses) {			
			return 0;
		}		// promotion not valid
		else {			
			return 1;
		}
	}

}