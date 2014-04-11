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

public class get_currency_conv extends AbstractProcedure {

	public Double execute(String curr_cd) throws SQLException, ProcedureException {		
		
		/*
		 * Get the currency conversion rate for the specified currency.
		 * 
		 *   The conversion rate will be the most recent average rate.
		 */		
		
		Double conv_rate;		
		
		conv_rate = null;		
		

		PreparedStatement pstmt1 = prepareStatement(
				  "select avg_rate"
				+ " from currency_conv"
				+ " where currency_conv.curr_cd = ?"
				+ " and currency_conv.conv_date = ("
					+ "select max(currency_conv.conv_date)"
					+ " from currency_conv"
					+ " where currency_conv.curr_cd = ?"
				+ ")");
		
		if (curr_cd != null) {
			pstmt1.setString(1, curr_cd);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		if (curr_cd != null) {
			pstmt1.setString(2, curr_cd);
		}
		else {
			pstmt1.setNull(2, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		conv_rate = rs1.getDouble(1);
		pstmt1.close();		
		
		return conv_rate;
	}

}