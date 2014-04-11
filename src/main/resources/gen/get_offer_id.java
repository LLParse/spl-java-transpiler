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

public class get_offer_id extends AbstractProcedure {

	public Integer execute(String recog_cd, String offer_cd) throws SQLException, ProcedureException {		
		
		/*
		 * Get the offer id corresponding to the
		 *   specified offer code for the specified 
		 *   program.
		 */		
		
		Integer offer_id;		
		
		offer_id = null;		
		
		//lookup the offer_id by recog_cd and offer_cd

		PreparedStatement pstmt1 = prepareStatement(
				  "select offer.offer_id"
				+ " from offer"
				+ " where offer.recog_cd = ?"
				+ " and offer.offer_cd = ?");
		
		if (recog_cd != null) {
			pstmt1.setString(1, recog_cd);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		if (offer_cd != null) {
			pstmt1.setString(2, offer_cd);
		}
		else {
			pstmt1.setNull(2, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		offer_id = rs1.getInt(1);
		pstmt1.close();		
		
		//return the offer id
		return offer_id;
	}

}