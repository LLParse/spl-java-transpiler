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

public class convert_currency extends AbstractProcedure {

	public Double execute(Double from_conv_rate, Double to_conv_rate, Double amount) throws SQLException, ProcedureException {		
		
		/*
		 * (]$[) $RCSfile$:$Revision: 99 $ | CDATE=$Date: 2012-12-19 14:14:54 -0700 (Wed, 19 Dec 2012) $ ~
		 * 
		 *   Convert the specified amount from one currency to another.
		 * 
		 *       Copyright (C) 2001 Choice Hotels International, Inc.
		 *                       All Rights Reserved
		 */		
		
		Double new_amount;		
		
		new_amount = null;		
		
		// convert amount
		if (to_conv_rate <= 0 || to_conv_rate == null) {			
			throw new ProcedureException(-746, 0, "convert_currency: Invalid destination rate.");
		}		
		
		if (from_conv_rate <= 0 || from_conv_rate == null) {			
			throw new ProcedureException(-746, 0, "convert_currency: Invalid source rate.");
		}		
		new_amount = amount * to_conv_rate / from_conv_rate;		
		
		return new_amount;
	}

}