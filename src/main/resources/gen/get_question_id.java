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

public class get_question_id extends AbstractProcedure {

	public Integer execute(String recog_cd, String question_cd) throws SQLException, ProcedureException {		
		
		/*
		 * Get the question id corresponding to the
		 *   specified question code for the specified
		 *   program.
		 */		
		
		Integer question_id;		
		
		question_id = null;		
		
		//lookup the question_id by recog_cd and question_cd

		PreparedStatement pstmt1 = prepareStatement(
				  "select question.question_id"
				+ " from question"
				+ " where question.recog_cd = ?"
				+ " and question.question_cd = ?");
		
		if (recog_cd != null) {
			pstmt1.setString(1, recog_cd);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		if (question_cd != null) {
			pstmt1.setString(2, question_cd);
		}
		else {
			pstmt1.setNull(2, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		question_id = rs1.getInt(1);
		pstmt1.close();		
		
		//return the promotion id
		return question_id;
	}

}