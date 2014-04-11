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

public class round_up extends AbstractProcedure {

	public Double execute(Double amount, Double trans_unit_value) throws SQLException, ProcedureException {		
		/*
		 * (]$[) $RCSfile$:$Revision: 99 $ | CDATE=$Date: 2012-12-19 14:14:54 -0700 (Wed, 19 Dec 2012) $ ~
		 * 
		 *   Round the given amount up to the nearest program unit.
		 * 
		 *       Copyright (C) 2004 Choice Hotels International, Inc.
		 *                       All Rights Reserved
		 */		
		
		Double in_amount;		
		Double in_unit_value;		
		Double new_amount;		
		Integer check_amount;		
		
		// set debug file to '/tmp/round_up.trace';
		// trace on;
		
		new_amount = null;		
		check_amount = null;		
		
		if (trans_unit_value == null) {			
			throw new ProcedureException(-746, 0, "round_up: 'unit_value' is null.");
		}		
		if (trans_unit_value.equals(0)) {			
			throw new ProcedureException(-746, 0, "round_up: 'unit_value' is zero.");
		}		
		
		// shift cents into integer because mod strips floating decimals
		in_amount = amount * 100;		
		in_unit_value = trans_unit_value * 100;		
		
		// check if rounding needed
		check_amount = mod(in_amount, in_unit_value);		
		
		if (check_amount > 0) {			
			new_amount = trunc(amount, 0) + trans_unit_value;
		}		// yes round up
		else {			
			new_amount = amount;
		}		
		
		return new_amount;
	}

}