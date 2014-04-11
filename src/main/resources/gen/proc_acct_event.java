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

public class proc_acct_event extends AbstractProcedure {

	public Integer execute(Integer event_id, Integer acct_id, String event_type) throws SQLException, ProcedureException {		
		
		/*
		 * (]$[) $RCSfile$:$Revision: 99 $ | CDATE=$Date: 2012-12-19 14:14:54 -0700 (Wed, 19 Dec 2012) $ ~
		 * 
		 *  Process an account event.
		 * 
		 *  This procedure is called to process account events that are written to
		 *  the account event queue.  Currently the only event that is supported is the
		 *  signup event.  New signups are written to this queue when the account
		 *  transitions from the pending state to active.  If there are any outstanding
		 *  promotions for the application group and the trigger is "SIGNUP", apply
		 *  the bonus points to the account.
		 * 
		 *  Returns are: 0 if no action was taken, and > 0 otherwise.
		 * 
		 * 	Copyright (C) 2003 Choice Hotels International, Inc.
		 *                       All Rights Reserved
		 */		
		
		// scratch variables
		Integer acct_trans_id;		
		
		acct_trans_id = 0;		
		
		//set debug file to '/tmp/proc_acct_event.trace';
		//trace on;
		
		acct_trans_id = new _proc_acct_event().execute(acct_id, event_type);		
		

		PreparedStatement pstmt1 = prepareStatement(
				  "delete from acct_event"
				+ " where acct_event.event_id = ?");
		if (event_id != null) {
			pstmt1.setInt(1, event_id);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		executeUpdate(pstmt1);
		pstmt1.close();		
		
		//if the delete failed, raise an exception.
		if (!dbinfo("sqlca.sqlerrd2").equals(1)) {			
			throw new ProcedureException(-746, 0, "proc_acct_event: failed dequeueing event.");
		}		
		
		return 1;
	}

}