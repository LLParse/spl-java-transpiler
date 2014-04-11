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

public class get_promo_id extends AbstractProcedure {

	public Integer execute(String recog_cd, String promo_cd) throws SQLException, ProcedureException {		
		
		/*
		 * Get the promotion id corresponding to the
		 *   specified promotion code for the specified 
		 *   program.
		 */		
		
		Integer promo_id;		
		
		promo_id = null;		
		
		//lookup the promo_id by recog_cd and promo_cd

		PreparedStatement pstmt1 = prepareStatement(
				  "select promo.promo_id"
				+ " from promo"
				+ " where promo.recog_cd = ?"
				+ " and promo.promo_cd = ?");
		
		if (recog_cd != null) {
			pstmt1.setString(1, recog_cd);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		if (promo_cd != null) {
			pstmt1.setString(2, promo_cd);
		}
		else {
			pstmt1.setNull(2, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		promo_id = rs1.getInt(1);
		pstmt1.close();		
		
		//return the promotion id
		return promo_id;
	}

}