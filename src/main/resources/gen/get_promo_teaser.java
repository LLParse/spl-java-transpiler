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

public class get_promo_teaser extends AbstractProcedure {

	public String execute(String recog_cd, String recog_id, String campaign_name) throws SQLException, ProcedureException {		//teaser code
		
		/*
		 * (]$[) $RCSfile$:$Revision: 99 $ | CDATE=$Date: 2012-12-19 14:14:54 -0700 (Wed, 19 Dec 2012) $ ~
		 * 
		 * 
		 *   get_promo_teaser -  get promotion teaser text for a account and promotion
		 * 
		 *       Copyright (C) 2003 Choice Hotels International, Inc.
		 *                       All Rights Reserved
		 */		
		
		Integer sql_error;		
		Integer isam_error;		
		String error_data;		
		String teaser_resp;		
		
		//  set debug file to '/tmp/get_promo_teaser.trace';
		//  trace on;
		
		try {			
			
			teaser_resp = null;			
			
			// Determine teaser based on campaign ID
			if (campaign_name.equals("FL2004")) {				
				teaser_resp = fl2004_teaser(recog_cd, recog_id);
			}			
			
			return teaser_resp;			
			
			//  set debug file to '/tmp/get_promo_teaser.trace';
			//  trace on;
			

		}
		catch (SQLException e) {
			sql_error = e.getErrorCode();
			isam_error = 0;
			error_data = e.getMessage();			
			throw new ProcedureException(sql_error, isam_error, error_data);
		}
	}

}