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

public class get_redeem_amt extends AbstractProcedure {

	public Integer execute(Integer award_id) throws SQLException, ProcedureException {		
		/*
		 * $RCSfile$ - Get the redemtion amount of an award in program units - $Revision: 99 $
		 * 
		 * (]$[) $RCSfile$:$Revision: 99 $ | CDATE=$Date: 2012-12-19 14:14:54 -0700 (Wed, 19 Dec 2012) $ ~
		 * 
		 *       Copyright (C) 2001 Choice Hotels International, Inc.
		 *                       All Rights Reserved
		 * 
		 * This procedure returns the cost of an award in the associated programs' units.
		 */		
		
		Integer redeem_amt;		
		
		redeem_amt = null;		
		

		PreparedStatement pstmt1 = prepareStatement(
				  "select award.redeem_amt"
				+ " from award"
				+ " where award.award_id = ?");
		
		if (award_id != null) {
			pstmt1.setInt(1, award_id);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		redeem_amt = rs1.getInt(1);
		pstmt1.close();		
		
		if (redeem_amt == null) {			
			throw new ProcedureException(-746, 0, "get_redeem_amt: Unable to get redemption amount of award");
		}		
		
		return redeem_amt;
	}

}