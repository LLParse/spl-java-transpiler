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

public class get_partner_id extends AbstractProcedure {

	public Integer execute(String partner_cd) throws SQLException, ProcedureException {		
		
		/*
		 * Get the partner id corresponding to the
		 *   specified partner code.
		 */		
		
		Integer partner_id;		
		
		partner_id = null;		
		
		//lookup the partner_id by partner_cd

		PreparedStatement pstmt1 = prepareStatement(
				  "select partner.partner_id"
				+ " from partner"
				+ " where partner.partner_cd = ?");
		
		if (partner_cd != null) {
			pstmt1.setString(1, partner_cd);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		partner_id = rs1.getInt(1);
		pstmt1.close();		
		
		//return the partner id
		return partner_id;
	}

}