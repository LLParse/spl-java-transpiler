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

public class calc_stay_rev extends AbstractProcedure {

	public Integer execute(Integer rm_rev, Integer fb_rev, Integer other_rev, String use_rm, String use_fb, String use_other) throws SQLException, ProcedureException {		
		/*
		 * (]$[) $RCSfile$:$Revision: 99 $ | CDATE=$Date: 2012-12-19 14:14:54 -0700 (Wed, 19 Dec 2012) $ ~
		 *   calc_stay_rev sums the revenue for the stay given a
		 *   promotions' revenue source criteria.
		 * 
		 *       Copyright (C) 2003 Choice Hotels International, Inc.
		 *                       All Rights Reserved
		 */		
		
		Integer total;		
		
		total = 0;		// default to no revenue
		if (rm_rev > 0 && use_rm.equals("Y")) {			
			total = total + rm_rev;
		}		
		
		if (fb_rev > 0 && use_fb.equals("Y")) {			
			total = total + fb_rev;
		}		
		
		if (other_rev > 0 && use_other.equals("Y")) {			
			total = total + other_rev;
		}		
		
		return total;
	}

}